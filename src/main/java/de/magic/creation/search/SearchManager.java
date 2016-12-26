package de.magic.creation.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import de.magic.creation.home.SearchSettings;
import de.magic.creation.repo.IZvgObjectRepository;
import de.magic.creation.repo.NominatimCacheEntry;
import de.magic.creation.repo.ZvgObject;
import de.magic.creation.repo.ZvgObjectDetail;

@Service
@CacheConfig(cacheNames = "searchs")
public class SearchManager
{
  private final Logger         log = LoggerFactory.getLogger( SearchManager.class);

  private ZvgWebClient         zvgWebClient;

  private NominatimManager     nominatimManager;

  private IZvgObjectRepository zvgObjectRepository;

  @Autowired
  public SearchManager( ZvgWebClient zvgWebClient, NominatimManager nominatimManager,
    IZvgObjectRepository zvgObjectRepository)
  {
    this.zvgWebClient = zvgWebClient;
    this.nominatimManager = nominatimManager;
    this.zvgObjectRepository = zvgObjectRepository;
  }

  public ZvgObject getObject( Long id)
  {
    return zvgObjectRepository.findOne( id);
  }

  @Cacheable()
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

  @Cacheable()
  public List<ZvgObject> search( SearchSettings settings)
  {
    log.debug( "Search: " + settings);
    try
    {
      List<ZvgObject> result = zvgWebClient.search( settings);
      result.forEach( this::resolveGeoLocation);
      //use returned object of repo
      Stream<ZvgObject> pers = result.stream().map( o -> zvgObjectRepository.save( o));
      return pers.collect( Collectors.toList());
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
