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
  @Index(columnList = "art", name = "idxArt"),
  @Index(columnList = "land", name = "idxLand"),
  @Index(columnList = "stadt, land", name = "idxStadtLand"),
  @Index(columnList = "termin", name = "idxTermin")
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

  @NotNull
  private boolean           aufgehoben;

  @Embedded
  private GeoLocation       location;

  @Embedded
  private ZvgObjectDetail   details;

  public Long getId()
  {
    return id;
  }

  public void setId( final Long id)
  {
    this.id = id;
  }

  public EKind getArt()
  {
    return art;
  }

  public void setArt( final EKind art)
  {
    this.art = art;
  }

  public String getStadt()
  {
    return stadt;
  }

  public void setStadt( final String stadt)
  {
    this.stadt = stadt;
  }

  public String getAktenzeichen()
  {
    return aktenzeichen;
  }

  public void setAktenzeichen( final String aktenzeichen)
  {
    this.aktenzeichen = aktenzeichen;
  }

  public String getDetailLink()
  {
    return detailLink;
  }

  public void setDetailLink( final String detailLink)
  {
    this.detailLink = detailLink;
  }

  public String getObjekt()
  {
    return objekt;
  }

  public void setObjekt( String objekt)
  {
    if( objekt != null)
    {
      objekt = objekt.trim();
      final int len = objekt.length();
      if( len > 254)
      {
        objekt = objekt.substring( 0, 254);
      }
    }
    this.objekt = objekt;
  }

  public String getLage()
  {
    return lage;
  }

  public void setLage( final String lage)
  {
    this.lage = lage;
  }

  public Integer getVerkerhswert()
  {
    return verkerhswert;
  }

  public void setVerkerhswert( final Integer verkerhswert)
  {
    this.verkerhswert = verkerhswert;
  }

  public LocalDateTime getTermin()
  {
    return termin;
  }

  public void setTermin( final LocalDateTime termin)
  {
    this.termin = termin;
  }

  public GeoLocation getLocation()
  {
    return location;
  }

  public void setLocation( final GeoLocation location)
  {
    this.location = location;
  }

  public ELand getLand()
  {
    return land;
  }

  public void setLand( final ELand land)
  {
    this.land = land;
  }

  public ZvgObjectDetail getDetails()
  {
    return details;
  }

  public void setDetails( final ZvgObjectDetail details)
  {
    this.details = details;
  }

  public boolean isAufgehoben()
  {
    return aufgehoben;
  }

  public void setAufgehoben( final boolean aufgehoben)
  {
    this.aufgehoben = aufgehoben;
  }

  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
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
    builder.append( ", aufgehoben=");
    builder.append( aufgehoben);
    builder.append( ", location=");
    builder.append( location);
    builder.append( ", details=");
    builder.append( details);
    builder.append( "]");
    return builder.toString();
  }
}
