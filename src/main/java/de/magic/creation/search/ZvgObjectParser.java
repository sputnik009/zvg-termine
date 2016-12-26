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
import de.magic.creation.repo.ZvgObject;

@Service
public class ZvgObjectParser
{
  private final Logger            log                      = LoggerFactory.getLogger( ZvgObjectParser.class);

  //Dienstag, 03. Januar 2017, 13:00 Uhr
  private final DateTimeFormatter germanLongDateTimeFormat =
    DateTimeFormatter.ofPattern( "cccc, dd. MMMM yyyy, HH:mm 'Uhr'", Locale.GERMAN);

  private Validator               validator;

  @Autowired
  public ZvgObjectParser( Validator validator)
  {
    this.validator = validator;
  }

  public List<ZvgObject> parseSearchResults( HtmlPage result)
  {
    log.debug( "parseSearchResults");

    DomElement form = result.getElementByName( "form_sucheZvg");
    DomNodeList<HtmlElement> tables = form.getElementsByTagName( "table");
    if( tables.size() < 2) return Collections.emptyList();

    HtmlElement resultTable = tables.get( 1);
    DomNodeList<HtmlElement> rows = resultTable.getElementsByTagName( "tr");

    List<List<HtmlElement>> objectRows = splitBySeparator( rows, this::isSplitter);

    return objectRows.stream().map( this::parse).filter( this::validate).collect( Collectors.toList());
  }

  private boolean validate( ZvgObject o)
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

    Set<ConstraintViolation<ZvgObject>> violatedConstraints = validator.validate( o);

    violatedConstraints.forEach( vc -> log.error( vc.getPropertyPath() + " " + vc.getMessage()));

    return violatedConstraints.isEmpty();
  }

  private ZvgObject parse( List<HtmlElement> objectRows)
  {
    Map<String, HtmlElement> attrToVal =
      objectRows.stream().collect( Collectors.toMap( this::htmlToKeyName, this::htmlToValueNode));

    ZvgObject obj = new ZvgObject();

    HtmlElement elm = attrToVal.get( "Aktenzeichen");
    if( elm != null)
    {
      //<b><a target="blank_" href="index.php?button=showZvg&amp;zvg_id=29212&amp;land_abk=sn"><nobr>0480 K 0489/2015&nbsp;(Detailansicht)</nobr></a></b>&nbsp;
      String text = elm.asText().trim();
      text = text.replace( "(Detailansicht)", "");
      obj.setAktenzeichen( text);

      String link = serachFirstLink( elm);
      parseZvgId( link, obj);
      obj.setDetailLink( link);
    }
    elm = attrToVal.get( "Objekt/Lage");
    if( elm != null)
    {
      //<td valign="center" align="left" colspan="2"><b>Eigentumswohnung (3 bis 4 Zimmer)<!--Lage--->:</b> F.-Jost-Str. 8/Breslauer Str.  42, 04299 Leipzig, Stötteritz</td>
      String object = extractContentOrEmpty( elm, "b").replace( ":", "");
      obj.setObjekt( object);
      obj.setArt( EKind.fromLabel( object));
      obj.setLage( extractLageOrNull( elm));
    }
    elm = attrToVal.get( "Verkehrswert");
    if( elm != null)
    {
      Integer verkerhswert = elm.getElementsByTagName( "p").stream()
        .map( p -> extractWertOrNull( p.asText()))
        .filter( w -> w != null)
        .max( Integer::compare).orElse( null);
      obj.setVerkerhswert( verkerhswert);
    }

    elm = attrToVal.get( "Termin");
    if( elm != null)
    {
      String termin = elm.asText().trim();
      try
      {
        obj.setTermin( LocalDateTime.parse( termin, germanLongDateTimeFormat));
      } catch( DateTimeParseException e)
      {
        log.error( e.toString(), e);
      }
    }

    return obj;
  }

  private void parseZvgId( String link, ZvgObject obj)
  {
    if( link == null)
    {
      log.error( "link is null!");
      return;
    }
    try
    {
      List<NameValuePair> paramsList = URLEncodedUtils.parse( new URI( link), "UTF-8");
      //index.php?button=showZvg&amp;zvg_id=29218&amp;land_abk=sn
      String zvg_id = paramsList.stream().filter( nvp -> "zvg_id".equals( nvp.getName())).map( nvp -> nvp.getValue())
        .findFirst().orElse( null);
      String landAbk = paramsList.stream().filter( nvp -> "land_abk".equals( nvp.getName())).map( nvp -> nvp.getValue())
        .findFirst().orElse( null);

      obj.setId( Long.parseLong( zvg_id));
      obj.setLandAbk( landAbk);
    } catch( Exception e)
    {
      log.error( e.toString(), e);
    }
  }

  private Integer extractWertOrNull( String wert)
  {
    if( wert == null || wert.isEmpty()) return null;

    int idxKomma = wert.indexOf( ',');

    if( idxKomma == -1) return null;

    wert = wert.substring( 0, idxKomma);
    wert = wert.replaceAll( "\\.", "");

    try
    {
      return Integer.valueOf( wert);
    } catch( NumberFormatException nfe)
    {
      return null;
    }
  }

  private String extractLageOrNull( HtmlElement elm)
  {
    DomNodeList<HtmlElement> bs = elm.getElementsByTagName( "b");
    if( bs.size() == 0) return null;

    HtmlElement b = bs.get( 0);

    DomNode lage = b.getNextSibling();
    if( lage != null) return lage.asText().trim();
    return null;
  }

  private String extractContentOrEmpty( HtmlElement elm, String tag)
  {
    Stream<DomElement> children = StreamSupport.stream( elm.getChildElements().spliterator(), false);

    String content = children.filter( e -> tag.equalsIgnoreCase( e.getTagName()))
      .map( e -> e.asText().trim())
      .findFirst().orElse( "");

    return content;
  }

  private String serachFirstLink( HtmlElement elm)
  {
    String link = elm.getElementsByTagName( "a").stream()
      .map( a -> a.getAttributeNode( "href"))
      .filter( an -> an != null)
      .map( an -> an.getValue())
      .findFirst().orElse( null);

    return link;
  }

  private String htmlToKeyName( HtmlElement elm)
  {
    DomNodeList<HtmlElement> tds = elm.getElementsByTagName( "td");
    if( tds.size() > 0)
    {
      String[] names = tds.get( 0).asText().trim().split( " |:");
      return names[0];
    }

    return null;
  }

  private HtmlElement htmlToValueNode( HtmlElement elm)
  {
    DomNodeList<HtmlElement> tds = elm.getElementsByTagName( "td");
    if( tds.size() < 2) return null;

    return tds.get( 1);
  }

  private boolean isSplitter( HtmlElement row)
  {
    // <tr><td colspan="3"><hr></td></tr>
    if( !"tr".equalsIgnoreCase( row.getTagName())) return false;

    DomNodeList<HtmlElement> tds = row.getElementsByTagName( "td");
    if( tds.size() == 0) return false;
    HtmlElement firstTd = tds.get( 0);

    String colspan = firstTd.getAttribute( "colspan");
    if( !"3".equals( colspan)) return false;

    return firstTd.getElementsByTagName( "hr").size() == 1;
  }

  private static <T> List<List<T>> splitBySeparator( List<T> list, Predicate< ? super T> predicate)
  {
    final List<List<T>> finalList = new ArrayList<>();
    int fromIndex = 0;
    int toIndex = 0;

    for( T elem : list)
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
