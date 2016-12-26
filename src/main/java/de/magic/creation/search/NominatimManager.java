package de.magic.creation.search;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import de.magic.creation.repo.INominatimRepository;
import de.magic.creation.repo.NominatimCacheEntry;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.Address;

@Service
@CacheConfig(cacheNames = "nominatim")
public class NominatimManager
{
  private final Logger               log = LoggerFactory.getLogger( NominatimManager.class);

  private final INominatimRepository nominatimRepository;

  private NominatimClient            nominatimClient;

  public NominatimManager( INominatimRepository nominatimRepository)
  {
    this.nominatimRepository = nominatimRepository;
    HttpClient client = HttpClientBuilder.create().build();
    nominatimClient = new JsonNominatimClient( client, "sputnik009@yahoo.de");
  }

  public NominatimCacheEntry searchAddress( String query)
  {
    log.info( "searchAddress: " + query);
    if( query == null || query.trim().isEmpty()) return null;
    query = query.trim().toLowerCase();

    return searchAddressInternal( query);
  }

  @Cacheable()
  protected NominatimCacheEntry searchAddressInternal( String query)
  {
    log.info( "searchAddress (internal): " + query);

    NominatimCacheEntry cacheEntry = nominatimRepository.findOne( query);

    if( cacheEntry != null) return cacheEntry;

    cacheEntry = queryNominatimClient( query);
    if( cacheEntry == null) return null;

    synchronized( nominatimRepository)
    {
      NominatimCacheEntry doubleCheckCacheEntry = nominatimRepository.findOne( query);
      if( doubleCheckCacheEntry != null) return doubleCheckCacheEntry;

      return nominatimRepository.save( cacheEntry);
    }
  }

  private NominatimCacheEntry queryNominatimClient( String query)
  {
    log.info( "searchAddress (over NominatimClient): " + query);
    
    Address adr = queryNominatimClientInternal( query);

    if( adr == null) return null;

    de.magic.creation.repo.Address nceAddress = new de.magic.creation.repo.Address();

    nceAddress.setPlaceId( adr.getPlaceId());
    nceAddress.setOsmType( adr.getOsmType());
    nceAddress.setLongitude( adr.getLongitude());
    nceAddress.setLatitude( adr.getLatitude());
    nceAddress.setDisplayName( adr.getDisplayName());
    nceAddress.setElementClass( adr.getElementClass());
    nceAddress.setElementType( adr.getElementType());

    return new NominatimCacheEntry( query, nceAddress);
  }

  private Address queryNominatimClientInternal( String query)
  {
    try
    {
      Thread.sleep( 500);
      List<Address> addresses = nominatimClient.search( query);

      if( addresses.size() == 1) { return addresses.get( 0); }

      log.info( "Try resolve Building");
      Address adr = getBuilding( addresses);
      if( adr != null) return adr;

      if( addresses.size() > 0) { return addresses.get( 0); }

      log.warn( "address not found!");
      return null;
    } catch( IOException | InterruptedException e)
    {
      log.error( e.toString(), e);
      return null;
    }
  }

  Address getBuilding( List<Address> addresses)
  {
    if( addresses == null) return null;
    return addresses.stream().filter( a -> "building".equalsIgnoreCase( a.getElementClass())).findFirst().orElse( null);
  }
}
