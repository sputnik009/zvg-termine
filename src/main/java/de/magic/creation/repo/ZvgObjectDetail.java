package de.magic.creation.repo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class ZvgObjectDetail implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Size(min = 3)
  @NotNull
  @Lob
  @Column(length = 1000)
  private String            grundbuch;

  @Size(min = 3)
  @NotNull
  @Lob
  @Column(length = 1000)
  private String            beschreibung;

  @Size(min = 3)
  @NotNull
  private String            ortVersteigerung;

  public String getGrundbuch()
  {
    return grundbuch;
  }

  public void setGrundbuch( String grundbuch)
  {
    this.grundbuch = grundbuch;
  }

  public String getBeschreibung()
  {
    return beschreibung;
  }

  public void setBeschreibung( String beschreibung)
  {
    this.beschreibung = beschreibung;
  }

  public String getOrtVersteigerung()
  {
    return ortVersteigerung;
  }

  public void setOrtVersteigerung( String ortVersteigerung)
  {
    this.ortVersteigerung = ortVersteigerung;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( "ZvgObjectDetail [grundbuch=");
    builder.append( grundbuch);
    builder.append( ", beschreibung=");
    builder.append( beschreibung);
    builder.append( ", ortVersteigerung=");
    builder.append( ortVersteigerung);
    builder.append( "]");
    return builder.toString();
  }
}
