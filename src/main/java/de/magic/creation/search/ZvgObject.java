package de.magic.creation.search;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ZvgObject implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String            id;

  private String            landAbk;

  private String            aktenzeichen;

  private String            detailLink;

  private String            objekt;

  private String            lage;

  private Integer           verkerhswert;

  private LocalDateTime     termin;

  private GeoLocation       location;

  public String getAktenzeichen()
  {
    return aktenzeichen;
  }

  public void setAktenzeichen( String aktenzeichen)
  {
    this.aktenzeichen = aktenzeichen;
  }

  public String getDetailLink()
  {
    return detailLink;
  }

  public void setDetailLink( String detailLink)
  {
    this.detailLink = detailLink;
  }

  public String getObjekt()
  {
    return objekt;
  }

  public void setObjekt( String objekt)
  {
    this.objekt = objekt;
  }

  public String getLage()
  {
    return lage;
  }

  public void setLage( String lage)
  {
    this.lage = lage;
  }

  public Integer getVerkerhswert()
  {
    return verkerhswert;
  }

  public void setVerkerhswert( Integer verkerhswert)
  {
    this.verkerhswert = verkerhswert;
  }

  public LocalDateTime getTermin()
  {
    return termin;
  }

  public void setTermin( LocalDateTime termin)
  {
    this.termin = termin;
  }

  public GeoLocation getLocation()
  {
    return location;
  }

  public void setLocation( GeoLocation location)
  {
    this.location = location;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id)
  {
    this.id = id;
  }

  public String getLandAbk()
  {
    return landAbk;
  }

  public void setLandAbk( String landAbk)
  {
    this.landAbk = landAbk;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( "ZvgObject [id=");
    builder.append( id);
    builder.append( ", aktenzeichen=");
    builder.append( aktenzeichen);
    builder.append( ", detailLink=");
    builder.append( detailLink);
    builder.append( ", objekt=");
    builder.append( objekt);
    builder.append( ", lage=");
    builder.append( lage);
    builder.append( ", verkerhswert=");
    builder.append( verkerhswert);
    builder.append( ", termin=");
    builder.append( termin);
    builder.append( ", location=");
    builder.append( location);
    builder.append( "]");
    return builder.toString();
  }
}
