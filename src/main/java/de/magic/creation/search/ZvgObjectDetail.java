package de.magic.creation.search;

import java.io.Serializable;

public class ZvgObjectDetail implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String            grundbuch;

  private String            beschreibung;

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
