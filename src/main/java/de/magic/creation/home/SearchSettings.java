package de.magic.creation.home;

import java.util.Arrays;

import de.magic.creation.repo.EKind;

public class SearchSettings
{
  private String  city;

  private EKind[] kinds;

  public SearchSettings()
  {
    city = "Leipzig";
    kinds = new EKind[] { EKind.Einfamilienhaus, EKind.Zweifamilienhaus, EKind.Eigentumswohnung_3_4 };
  }

  public String getCity()
  {
    return city;
  }

  public void setCity( String city)
  {
    this.city = city;
  }

  public EKind[] getKinds()
  {
    return kinds;
  }

  public void setKinds( EKind[] kinds)
  {
    this.kinds = kinds;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( "SearchSettings [city=");
    builder.append( city);
    builder.append( ", kinds=");
    builder.append( Arrays.toString( kinds));
    builder.append( "]");
    return builder.toString();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + Arrays.hashCode( kinds);
    return result;
  }

  @Override
  public boolean equals( Object obj)
  {
    if( this == obj) return true;
    if( obj == null) return false;
    if( getClass() != obj.getClass()) return false;
    SearchSettings other = (SearchSettings) obj;
    if( city == null)
    {
      if( other.city != null) return false;
    }
    else if( !city.equals( other.city)) return false;
    if( !Arrays.equals( kinds, other.kinds)) return false;
    return true;
  }
}
