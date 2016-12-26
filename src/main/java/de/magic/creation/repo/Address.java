package de.magic.creation.repo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long              placeId;

  private String            osmType;

  private double            longitude;

  private double            latitude;

  private String            displayName;

  private String            elementClass;

  private String            elementType;

  public Long getPlaceId()
  {
    return placeId;
  }

  public void setPlaceId( Long placeId)
  {
    this.placeId = placeId;
  }

  public String getOsmType()
  {
    return osmType;
  }

  public void setOsmType( String osmType)
  {
    this.osmType = osmType;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public void setLongitude( double longitude)
  {
    this.longitude = longitude;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public void setLatitude( double latitude)
  {
    this.latitude = latitude;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName)
  {
    this.displayName = displayName;
  }

  public String getElementClass()
  {
    return elementClass;
  }

  public void setElementClass( String elementClass)
  {
    this.elementClass = elementClass;
  }

  public String getElementType()
  {
    return elementType;
  }

  public void setElementType( String elementType)
  {
    this.elementType = elementType;
  }
}
