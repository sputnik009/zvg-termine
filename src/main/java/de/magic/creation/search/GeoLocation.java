package de.magic.creation.search;

import java.io.Serializable;

public class GeoLocation implements Serializable
{
  private static final long serialVersionUID = 1L;

  private double            latitude;

  private double            longitude;

  public GeoLocation()
  {
  }

  public GeoLocation( double latitude, double longitude)
  {
    this.latitude = latitude;
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

  public double getLongitude()
  {
    return longitude;
  }

  public void setLongitude( double longitude)
  {
    this.longitude = longitude;
  }

  @Override
  public String toString()
  {
    return "GeoLocation [latitude=" + latitude + ", longitude=" + longitude + "]";
  }
}
