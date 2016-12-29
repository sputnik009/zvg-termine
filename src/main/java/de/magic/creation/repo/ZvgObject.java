package de.magic.creation.repo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.magic.creation.search.GeoLocation;

@Entity
@Table(indexes = {
  @Index(columnList = "stadt"),
  @Index(columnList = "art")
})
public class ZvgObject implements Serializable
{
  private static final long serialVersionUID = 1L;

  @NotNull
  @Id
  private Long              id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ELand             land;

  @NotNull
  @Enumerated(EnumType.STRING)
  private EKind             art;

  @Size(min = 3)
  @NotNull
  private String            stadt;

  @Size(min = 3)
  @NotNull
  private String            aktenzeichen;

  @Size(min = 3)
  @NotNull
  private String            detailLink;

  @Size(min = 3)
  @NotNull
  private String            objekt;

  @Size(min = 3)
  @NotNull
  private String            lage;

  @NotNull
  private Integer           verkerhswert;

  @NotNull
  private LocalDateTime     termin;

  @Embedded
  private GeoLocation       location;

  @Embedded
  private ZvgObjectDetail   details;

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

  public String getStadt()
  {
    return stadt;
  }

  public void setStadt( String stadt)
  {
    this.stadt = stadt;
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

  public ELand getLand()
  {
    return land;
  }

  public void setLand( ELand land)
  {
    this.land = land;
  }

  public ZvgObjectDetail getDetails()
  {
    return details;
  }

  public void setDetails( ZvgObjectDetail details)
  {
    this.details = details;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( "ZvgObject [id=");
    builder.append( id);
    builder.append( ", land=");
    builder.append( land);
    builder.append( ", art=");
    builder.append( art);
    builder.append( ", stadt=");
    builder.append( stadt);
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
