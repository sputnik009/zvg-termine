package de.magic.creation.repo;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NominatimCacheEntry implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String            query;

  @Embedded
  private Address           address;

  public NominatimCacheEntry()
  {
  }

  public NominatimCacheEntry( String query, Address address)
  {
    this.query = query;
    this.address = address;
  }

  public String getQuery()
  {
    return query;
  }

  public void setQuery( String query)
  {
    this.query = query;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress( Address address)
  {
    this.address = address;
  }
}
