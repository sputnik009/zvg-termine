package de.magic.creation.repo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.magic.creation.search.GeoLocation;

@Entity
public class ZvgObject implements Serializable
{
  private static final long serialVersionUID = 1L;

  @NotNull
  @Id
  private Long              id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private EKind             art;

  @NotNull
  @Size(min=1)
  private String            landAbk;

  @Size(min=3)
  @NotNull
  private String            aktenzeichen;

  @Size(min=3)
  @NotNull
  private String            detailLink;

  @Size(min=3)
  @NotNull
  private String            objekt;

  @Size(min=3)
  @NotNull
  private String            lage;

  @NotNull
  private Integer           verkerhswert;

  @NotNull
  private LocalDateTime     termin;

  @Embedded
  private GeoLocation       location;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id)
  {
    this.id = id;
  }

  public EKind getArt()
  {
    return art;
  }

  public void setArt( EKind art)
  {
    this.art = art;
  }

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
