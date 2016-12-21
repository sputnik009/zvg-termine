package de.magic.creation.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import de.magic.creation.home.SearchSettings;

@Service
@CacheConfig(cacheNames = "searchs")
public class SearchManager
{
  private final Logger           log = LoggerFactory.getLogger( SearchManager.class);

  private ZvgWebClient           zvg;

  private NominatimManager       nominatimManager;

  private Map<String, ZvgObject> data;

  @Autowired
  public SearchManager( ZvgWebClient zvg, NominatimManager nominatimManager)
  {
    this.zvg = zvg;
    this.nominatimManager = nominatimManager;
    this.data = new ConcurrentHashMap<>();
  }

  public ZvgObject getObject( String id)
  {
    return data.get( id);
  }

  @Cacheable()
  public ZvgObjectDetail details( String zvgId, String landAbk)
  {
    try
    {
      return zvg.getDetails( zvgId, landAbk);
    } catch( FailingHttpStatusCodeException | IOException | URISyntaxException e)
    {
      log.error( e.toString(), e);
    }

    return null;
  }

  @Cacheable()
  public List<ZvgObject> search( SearchSettings settings)
  {
    log.debug( "Search: " + settings);
    try
    {
      List<ZvgObject> result = zvg.search( settings);
      result.forEach( o -> resolveGeoLocation( o, settings.getCity()));
      result.forEach( o -> data.put( o.getId(), o));
      return result;
    } catch( FailingHttpStatusCodeException | IOException e)
    {
      log.error( e.toString(), e);
    }
    return Collections.emptyList();
  }

  private void resolveGeoLocation( ZvgObject obj, String city)
  {
    GeoLocation loc = nominatimManager.searchAddress( obj.getLage(), city);
    if( loc != null)
      obj.setLocation( loc);
    else
      obj.setLocation( new GeoLocation());
  }
}
