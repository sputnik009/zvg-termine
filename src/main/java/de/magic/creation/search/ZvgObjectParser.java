package de.magic.creation.search;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.magic.creation.repo.EKind;
import de.magic.creation.repo.ELand;
import de.magic.creation.repo.ZvgObject;

@Service
public class ZvgObjectParser
{
  private final Logger            log                      = LoggerFactory.getLogger( ZvgObjectParser.class);

  //Dienstag, 03. Januar 2017, 13:00 Uhr
  private final DateTimeFormatter germanLongDateTimeFormat =
    DateTimeFormatter.ofPattern( "cccc, dd. MMMM yyyy, HH:mm 'Uhr'", Locale.GERMAN);

  private final Pattern           stadtPattern             = Pattern.compile( ".*\\d\\d\\d\\d\\d ([^,]*)(,.*)*");

  private final Pattern           preisPattern             = Pattern.compile( "(\\d+(.\\d\\d\\d)(,\\d\\d)?)");

  private final Validator         validator;

  @Autowired
  public ZvgObjectParser( final Validator validator)
  {
    this.validator = validator;
  }

  public List<ZvgObject> parseSearchResults( final HtmlPage result)
  {
    log.debug( "parseSearchResults");

    final DomElement form = result.getElementByName( "form_sucheZvg");
    final DomNodeList<HtmlElement> tables = form.getElementsByTagName( "table");

    if( tables.isEmpty()) return Collections.emptyList();

    HtmlElement resultTable;
    if( tables.size() < 2)
    {
      resultTable = tables.get( 0);
    }
    else
    {
      resultTable = tables.get( 1);
    }

    final DomElement tbody = resultTable.getFirstElementChild();
    final List<HtmlElement> rows = getChildsByTagName( tbody, "tr");

    final List<List<HtmlElement>> objectRows = splitBySeparator( rows, this::isSplitter);

    log.debug( "Count of Result gorups: " + objectRows.size());

    return objectRows.stream().map( this::parse).filter( this::validate).collect( Collectors.toList());
  }

  private boolean validate( final ZvgObject o)
  {
    if( o == null)
    {
      log.error( "ZvgObject is NULL!");
      return false;
    }

    if( o.getId() == null)
    {
      log.error( "ZvgObject.id is NULL!");
      return false;
    }

    if( o.getObjekt() == null)
    {
      log.error( "ZvgObject.objekt is NULL!");
      return false;
    }

    if( o.getArt() == null)
    {
      log.error( "ZvgObject.art is NULL! Objekt: " + o.getObjekt());
      return false;
    }

    final Set<ConstraintViolation<ZvgObject>> violatedConstraints = validator.validate( o);

    violatedConstraints.forEach( vc -> log.error( vc.getPropertyPath() + " " + vc.getMessage()));

    return violatedConstraints.isEmpty();
  }

  private ZvgObject parse( final List<HtmlElement> objectRows)
  {
    final Map<String, HtmlElement> attrToVal =
      objectRows.stream().collect( Collectors.toMap( this::htmlToKeyName, this::htmlToValueNode));

    final ZvgObject obj = new ZvgObject();

    HtmlElement elm = attrToVal.get( "Aktenzeichen");
    if( elm != null)
    {
      //<b><a target="blank_" href="index.php?button=showZvg&amp;zvg_id=29212&amp;land_abk=sn"><nobr>0480 K 0489/2015&nbsp;(Detailansicht)</nobr></a></b>&nbsp;
      String text = elm.asText().trim();
      text = text.replace( "(Detailansicht)", "");
      obj.setAktenzeichen( text);

      final String link = serachFirstLink( elm);
      parseZvgId( link, obj);
      obj.setDetailLink( link);
    }
    elm = attrToVal.get( "Objekt/Lage");
    if( elm != null)
    {
      //<td valign="center" align="left" colspan="2"><b>Eigentumswohnung (3 bis 4 Zimmer)<!--Lage--->:</b> F.-Jost-Str. 8/Breslauer Str.  42, 04299 Leipzig, Stötteritz</td>
      final String object = extractContentOrEmpty( elm, "b").replace( ":", "");
      obj.setObjekt( object);

      final EKind kind = EKind.fromLabel( object);
      if( kind == null) log.debug( "Kind not found from: " + object);
      obj.setArt( kind);

      final String lage = extractLageOrNull( elm);
      obj.setLage( lage);
      //Kernweg 8, 01458 Ottendorf-Okrilla, OT Medingen
      if( lage != null)
      {
        final Matcher m = stadtPattern.matcher( lage);
        if( m.matches())
        {
          final String stadt = m.group( 1).trim();
          obj.setStadt( stadt);
        }
        else
        {
          log.debug( "Stadt not found from: " + lage);
        }
      }
    }
    elm = attrToVal.get( "Verkehrswert");
    if( elm != null)
    {
      final Integer verkerhswert = elm.getElementsByTagName( "p").stream()
        .map( p -> extractWertOrNull( p.asText()))
        .filter( w -> w != null)
        .max( Integer::compare).orElse( null);
      if( verkerhswert == null)
      {
        if( log.isDebugEnabled())
        {
          final List<String> con =
            elm.getElementsByTagName( "p").stream().map( p -> p.asText()).collect( Collectors.toList());
          log.debug( "Verkahrswert not found from:\n" + con.toString());
        }
      }
      obj.setVerkerhswert( verkerhswert);
    }

    elm = attrToVal.get( "Termin");
    if( elm != null)
    {
      String termin = elm.asText().trim();
      //  Der Termin Mittwoch, 04. Januar 2017, 11:00 Uhr wurde aufgehoben.
      final boolean isAufgehoben = termin.contains( "wurde aufgehoben");
      if( isAufgehoben)
      {
        termin = termin.replace( "Der Termin ", "");
        termin = termin.replace( " wurde aufgehoben.", "");
        obj.setAufgehoben( true);
      }

      try
      {
        obj.setTermin( LocalDateTime.parse( termin, germanLongDateTimeFormat));
      } catch( final DateTimeParseException e)
      {
        log.error( e.toString(), e);
      }
    }

    return obj;
  }

  private void parseZvgId( final String link, final ZvgObject obj)
  {
    if( link == null)
    {
      log.error( "link is null!");
      return;
    }
    try
    {
      final List<NameValuePair> paramsList = URLEncodedUtils.parse( new URI( link), "UTF-8");
      //index.php?button=showZvg&amp;zvg_id=29218&amp;land_abk=sn
      final String zvg_id =
        paramsList.stream().filter( nvp -> "zvg_id".equals( nvp.getName())).map( nvp -> nvp.getValue())
          .findFirst().orElse( null);
      final ELand land =
        paramsList.stream().filter( nvp -> "land_abk".equals( nvp.getName())).map( nvp -> nvp.getValue())
          .findFirst().map( la -> ELand.fromValue( la)).orElse( null);

      obj.setId( Long.parseLong( zvg_id));
      obj.setLand( land);
    } catch( final Exception e)
    {
      log.error( e.toString(), e);
    }
  }

  private Integer extractWertOrNull( final String wert)
  {
    if( wert == null || wert.isEmpty()) return null;

    final Matcher m = preisPattern.matcher( wert);

    final ArrayList<Integer> werte = new ArrayList<>();

    while( m.find())
    {
      final String group = m.group( 1);
      final Integer wer = extractIntWertOrNull( group);
      if( wer != null) werte.add( wer);
    }

    return werte.stream().max( Integer::compare).orElse( null);
  }

  private Integer extractIntWertOrNull( String wert)
  {
    final int idxKomma = wert.indexOf( ',');

    if( idxKomma > 0)
    {
      wert = wert.substring( 0, idxKomma);
    }
    wert = wert.replaceAll( "\\.", "");

    try
    {
      return Integer.valueOf( wert);
    } catch( final NumberFormatException nfe)
    {
      return null;
    }
  }

  private String extractLageOrNull( final HtmlElement elm)
  {
    final DomNodeList<HtmlElement> bs = elm.getElementsByTagName( "b");
    if( bs.size() == 0) return null;

    final HtmlElement b = bs.get( 0);

    final DomNode lage = b.getNextSibling();
    if( lage != null) return lage.asText().trim();

    log.debug( "Lage not found from: " + elm.asXml());
    return null;
  }

  private String extractContentOrEmpty( final HtmlElement elm, final String tag)
  {
    final Stream<DomElement> children = StreamSupport.stream( elm.getChildElements().spliterator(), false);

    final String content = children.filter( e -> tag.equalsIgnoreCase( e.getTagName()))
      .map( e -> e.asText().trim())
      .findFirst().orElse( "");

    return content;
  }

  private String serachFirstLink( final HtmlElement elm)
  {
    final String link = elm.getElementsByTagName( "a").stream()
      .map( a -> a.getAttributeNode( "href"))
      .filter( an -> an != null)
      .map( an -> an.getValue())
      .findFirst().orElse( null);

    if( link == null)
    {
      log.debug( "Link not found from: " + elm.asXml());
    }

    return link;
  }

  private String htmlToKeyName( final HtmlElement elm)
  {
    final DomNodeList<HtmlElement> tds = elm.getElementsByTagName( "td");
    if( tds.size() > 0)
    {
      final String[] names = tds.get( 0).asText().trim().split( " |:");
      final String key = names[0];
      log.debug( key);
      return key;
    }

    return null;
  }

  private HtmlElement htmlToValueNode( final HtmlElement elm)
  {
    final DomNodeList<HtmlElement> tds = elm.getElementsByTagName( "td");
    if( tds.size() < 2) return null;

    return tds.get( 1);
  }

  private boolean isSplitter( final HtmlElement row)
  {
    // <tr><td colspan="3"><hr></td></tr>
    if( !"tr".equalsIgnoreCase( row.getTagName())) return false;

    final DomNodeList<HtmlElement> tds = row.getElementsByTagName( "td");
    if( tds.size() == 0) return false;
    final HtmlElement firstTd = tds.get( 0);

    final String colspan = firstTd.getAttribute( "colspan");
    if( !"3".equals( colspan)) return false;

    return firstTd.getElementsByTagName( "hr").size() == 1;
  }

  static List<HtmlElement> getChildsByTagName( final DomElement domElement, final String tagName)
  {
    if( domElement == null) return Collections.emptyList();
    if( domElement.getChildElements() == null) return Collections.emptyList();

    final Stream<DomElement> stream = StreamSupport.stream( domElement.getChildElements().spliterator(), false);

    return stream
      .filter( elem -> elem.getLocalName().equalsIgnoreCase( tagName))
      .filter( elem -> elem instanceof HtmlElement)
      .map( elem -> (HtmlElement) elem)
      .collect( Collectors.toList());
  }

  static <T> List<List<T>> splitBySeparator( final List<T> list, final Predicate< ? super T> predicate)
  {
    final List<List<T>> finalList = new ArrayList<>();
    int fromIndex = 0;
    int toIndex = 0;

    for( final T elem : list)
    {
      if( predicate.test( elem))
      {
        finalList.add( list.subList( fromIndex, toIndex));
        fromIndex = toIndex + 1;
      }
      toIndex++;
    }

    if( fromIndex != toIndex)
    {
      finalList.add( list.subList( fromIndex, toIndex));
    }

    return finalList;
  }
}
