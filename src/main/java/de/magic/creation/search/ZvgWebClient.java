package de.magic.creation.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import de.magic.creation.home.SearchSettings;
import de.magic.creation.repo.ZvgObject;
import de.magic.creation.repo.ZvgObjectDetail;

@Service
public class ZvgWebClient
{
  private final Logger                log = LoggerFactory.getLogger( ZvgWebClient.class);

  private final WebClient             webClient;

  private final URL                   baseUrl;

  private final ZvgObjectParser       parser;

  private final ZvgObjectDetailParser detailParser;

  @Autowired
  public ZvgWebClient( ZvgObjectParser parser, ZvgObjectDetailParser detailParser) throws MalformedURLException
  {
    baseUrl = new URL( "https://www.zvg-portal.de/index.php?button=Suchen&all=1");

    webClient = new WebClient( BrowserVersion.BEST_SUPPORTED);

    webClient.setAjaxController( new NicelyResynchronizingAjaxController());
    webClient.getOptions().setUseInsecureSSL( true);
    webClient.getOptions().setPopupBlockerEnabled( true);
    webClient.getOptions().setRedirectEnabled( true);
    webClient.getOptions().setCssEnabled( false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode( false);
    webClient.getOptions().setThrowExceptionOnScriptError( false);
    webClient.getOptions().setDoNotTrackEnabled( true);
    
    webClient.getCache().setMaxSize( 0);

    this.parser = parser;
    this.detailParser = detailParser;
  }

  public ZvgObjectDetail getDetails( long zvgId, String landAbk)
    throws FailingHttpStatusCodeException, IOException, URISyntaxException
  {
    ////index.php?button=showZvg&zvg_id=29218&land_abk=sn
    URL detailLink = new URL( "https://www.zvg-portal.de/index.php");
    List<NameValuePair> params = new ArrayList<>();
    params.add( new NameValuePair( "button", "showZvg"));
    params.add( new NameValuePair( "zvg_id", Long.toString( zvgId)));
    params.add( new NameValuePair( "land_abk", landAbk));

    WebRequest requestSettings = new WebRequest( detailLink, HttpMethod.GET);
    /*
     * accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp, ...
     * Accept-Encoding:gzip, deflate, sdch, br
     * Accept-Language:de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4
     * Cache-Control:max-age=0
     * Connection:keep-alive
     * DNT:1
     * Host:www.zvg-portal.de
     * Referer:https://www.zvg-portal.de/index.php?button=Suchen&seite=1&l=1&r=3&all=&order_by=-1&
     * desc=-1
     * Upgrade-Insecure-Requests:1
     */
    requestSettings.setAdditionalHeader( "Accept",
      "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    requestSettings.setAdditionalHeader( "Accept-Encoding", "gzip, deflate, sdch, br");
    requestSettings.setAdditionalHeader( "Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");

    requestSettings.setAdditionalHeader( "Cache-Control", "max-age=0");
    requestSettings.setAdditionalHeader( "DNT", "1");
    requestSettings.setAdditionalHeader( "Host", "www.zvg-portal.de");
    requestSettings.setAdditionalHeader( "Referer",
      "https://www.zvg-portal.de/index.php?button=Suchen&seite=1&l=1&r=3&all=&order_by=-1&desc=-1");
    requestSettings.setAdditionalHeader( "Upgrade-Insecure-Requests", "1");

    requestSettings.setRequestParameters( params);
    

    HtmlPage page = webClient.getPage( requestSettings);

    return detailParser.parseDetail( page);
  }

  public List<ZvgObject> search( SearchSettings settings) throws FailingHttpStatusCodeException, IOException
  {
    /*
     * ger_name:-- Alle Amtsgerichte --
     * order_by:2
     * land_abk:sn
     * ger_id:0
     * az1:
     * az2:
     * az3:
     * az4:
     * art:
     * obj:
     * obj_arr[]:3
     * obj_arr[]:19
     * obj_arr[]:6
     * obj_liste:6
     * str:
     * hnr:
     * plz:
     * ort:Leipzig
     * ortsteil:
     * vtermin:
     * btermin:
     */

    List<NameValuePair> formData = new ArrayList<>();
    formData.add( new NameValuePair( "ger_name", "-- Alle Amtsgerichte --"));
    formData.add( new NameValuePair( "order_by", "2"));
    formData.add( new NameValuePair( "land_abk", settings.getLand().getValue()));
    formData.add( new NameValuePair( "ger_id", "0"));

    Arrays.stream( settings.getKinds())
      .forEach( k -> formData.add( new NameValuePair( "obj_arr[]", Integer.toString( k.getId()))));

    if( settings.getCity() != null)
      formData.add( new NameValuePair( "ort", settings.getCity()));

    WebRequest requestSettings = new WebRequest( baseUrl, HttpMethod.POST);

    requestSettings.setAdditionalHeader( "Accept",
      "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    requestSettings.setAdditionalHeader( "Accept-Encoding", "gzip, deflate, br");
    requestSettings.setAdditionalHeader( "Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");

    requestSettings.setAdditionalHeader( "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    requestSettings.setAdditionalHeader( "Origin", "https://www.zvg-portal.de");
    requestSettings.setAdditionalHeader( "Referer", "https://www.zvg-portal.de/index.php?button=Termine%20suchen");

    requestSettings.setAdditionalHeader( "Cache-Control", "no-cache");
    requestSettings.setAdditionalHeader( "Pragma", "no-cache");

    requestSettings.setRequestParameters( formData);

    Page page = webClient.getPage( requestSettings);

    if( page instanceof HtmlPage)
    {
      return parser.parseSearchResults( (HtmlPage) page);
    }
    else
    {
      log.error( "Page is not an HtmlPage:");
      log.error( page.toString());
      return Collections.emptyList();
    }
  }
}
