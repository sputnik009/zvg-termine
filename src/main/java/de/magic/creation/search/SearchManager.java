package de.magic.creation.search;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import de.magic.creation.home.SearchSettings;
import de.magic.creation.repo.IZvgObjectRepository;
import de.magic.creation.repo.ZvgObject;
import de.magic.creation.repo.ZvgObjectDetail;

@Service
public class SearchManager
{
  private final Logger         log = LoggerFactory.getLogger( SearchManager.class);

  private SearchManagerWeb     searchManagerWeb;

  private IZvgObjectRepository zvgObjectRepository;

  @Autowired
  public SearchManager( SearchManagerWeb searchManagerWeb, IZvgObjectRepository zvgObjectRepository)
  {
    this.searchManagerWeb = searchManagerWeb;
    this.zvgObjectRepository = zvgObjectRepository;
  }

  public ZvgObject getObject( Long id)
  {
    return zvgObjectRepository.findOne( id);
  }

  public ZvgObjectDetail details( ZvgObject zvgObject)
  {
    ZvgObjectDetail details = zvgObject.getDetails();
    if( details != null) return details;

    details = searchManagerWeb.details( zvgObject.getId(), zvgObject.getLand().getValue());
    if( details != null)
    {
      zvgObject.setDetails( details);
      zvgObjectRepository.save( zvgObject);
    }

    return details;
  }

  @Cacheable(cacheNames = "searchs")
  public List<ZvgObject> search( SearchSettings settings)
  {
    log.info( "Search: " + settings);

    log.debug( "Search in db");
    
    List<ZvgObject> result;
    String stadt = settings.getCity();

    if( stadt != null && !stadt.trim().isEmpty())
      result = zvgObjectRepository.findByStadtAndLandAndArtInAndTerminAfter( stadt, settings.getLand(), settings.getKinds(), LocalDateTime.now());
    else
      result = zvgObjectRepository.findByLandAndArtInAndTerminAfter( settings.getLand(), settings.getKinds(), LocalDateTime.now());
    
    log.debug( "db results: " + result.size());

    if( result.size() > 0) return result;

    log.debug( "Search in web");
    result = searchManagerWeb.search( settings);
    result = zvgObjectRepository.save( result);
    log.debug( "web results: " + result.size());

    return result;
  }
}
