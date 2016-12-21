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

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.Address;

@Service
@CacheConfig(cacheNames = "nominatim")
public class NominatimManager
{
  private final Logger log = LoggerFactory.getLogger( NominatimManager.class);

  NominatimClient      nominatimClient;

  public NominatimManager()
  {
    HttpClient client = HttpClientBuilder.create().build();
    nominatimClient = new JsonNominatimClient( client, "sputnik009@yahoo.de");
  }

  @Cacheable()
  public GeoLocation searchAddress( String query, String city)
  {
    log.info( "searchAddress: " + query);
    try
    {
      Thread.sleep( 500);
      List<Address> addresses = nominatimClient.search( query);

      if( addresses.size() == 1)
      {
        Address adr = addresses.get( 0);
        return new GeoLocation( adr.getLatitude(), adr.getLongitude());
      }

      log.info( "Try resolve Building");
      Address adr = getBuilding( addresses);
      if( adr != null) return new GeoLocation( adr.getLatitude(), adr.getLongitude());

      if( addresses.size() > 0)
      {
        adr = addresses.get( 0);
        return new GeoLocation( adr.getLatitude(), adr.getLongitude());
      }

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
