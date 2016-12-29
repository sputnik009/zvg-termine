package de.magic.creation.search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.magic.creation.repo.ZvgObjectDetail;

@Service
public class ZvgObjectDetailParser
{
  private final Logger log = LoggerFactory.getLogger( ZvgObjectDetailParser.class);

  public ZvgObjectDetail parseDetail( HtmlPage result)
  {
    log.debug( "parseDetail");

    DomElement table = result.getElementById( "anzeige");
    DomElement tbody = table.getFirstElementChild();
    List<HtmlElement> rows = ZvgObjectParser.getChildsByTagName( tbody, "tr");
    Map<String, HtmlElement> attrToVal =
      rows.stream().filter( e -> htmlToKeyName( e) != null && htmlToValueNode( e) != null)
        .collect( Collectors.toMap( this::htmlToKeyName, this::htmlToValueNode));

    ZvgObjectDetail detail = new ZvgObjectDetail();

    HtmlElement elm = attrToVal.get( "Grundbuch");
    if( elm != null)
    {
      detail.setGrundbuch( innerHtml( elm));
    }

    elm = attrToVal.get( "Beschreibung");
    if( elm != null)
    {
      detail.setBeschreibung( elm.asText().trim());
    }

    elm = attrToVal.get( "Ort");
    if( elm != null)
    {
      detail.setOrtVersteigerung( elm.asText().trim());
    }
    return detail;
  }

  private String innerHtml( HtmlElement elm)
  {
    final StringWriter stringWriter = new StringWriter();
    try (final PrintWriter printWriter = new PrintWriter( stringWriter))
    {
      DomNode child = elm.getFirstChild();
      while( child != null)
      {
        printWriter.print( child.asXml());
        child = child.getNextSibling();
      }
    }
    return stringWriter.toString();
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
}
