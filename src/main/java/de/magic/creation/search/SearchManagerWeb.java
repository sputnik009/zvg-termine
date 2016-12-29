package de.magic.creation.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import de.magic.creation.home.SearchSettings;
import de.magic.creation.repo.ELand;
import de.magic.creation.repo.NominatimCacheEntry;
import de.magic.creation.repo.ZvgObject;
import de.magic.creation.repo.ZvgObjectDetail;

@Service
public class SearchManagerWeb
{
  private final Logger     log = LoggerFactory.getLogger( SearchManagerWeb.class);

  private ZvgWebClient     zvgWebClient;

  private NominatimManager nominatimManager;

  @Autowired
  public SearchManagerWeb( ZvgWebClient zvgWebClient, NominatimManager nominatimManager)
  {
    this.zvgWebClient = zvgWebClient;
    this.nominatimManager = nominatimManager;
  }

  public ZvgObjectDetail details( Long zvgId, String landAbk)
  {
    try
    {
      return zvgWebClient.getDetails( zvgId, landAbk);
    } catch( FailingHttpStatusCodeException | IOException | URISyntaxException e)
    {
      log.error( e.toString(), e);
    }

    return null;
  }

  public List<ZvgObject> search( SearchSettings settings)
  {
    log.debug( "Search: " + settings);

    return searchInternal( settings);
  }

  public List<ZvgObject> search( ELand bundesland)
  {
    log.debug( "Search (Bundesland): " + bundesland);
    SearchSettings settings = new SearchSettings( bundesland);

    return searchInternal( settings);
  }

  private List<ZvgObject> searchInternal( SearchSettings settings)
  {
    log.debug( "Search (Internal): " + settings);
    try
    {
      List<ZvgObject> result = zvgWebClient.search( settings);
      result.forEach( this::resolveGeoLocation);
      return result;
    } catch( FailingHttpStatusCodeException | IOException e)
    {
      log.error( e.toString(), e);
    }
    return Collections.emptyList();
  }

  private void resolveGeoLocation( ZvgObject obj)
  {
    NominatimCacheEntry nce = nominatimManager.searchAddress( obj.getLage());
    if( nce != null)
      obj.setLocation( new GeoLocation( nce.getAddress().getLatitude(), nce.getAddress().getLongitude()));
    else
      obj.setLocation( new GeoLocation());
  }
}
