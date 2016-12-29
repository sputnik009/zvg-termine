package de.magic.creation.repo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ELand
{
    /*
     * <select size="1" name="land_abk" onchange="updateAmtsgericht(this.value);"
     * style="width:643px">
     * <option value="0">-- Bitte Bundesland auswählen --</option><option
     * value="bw">Baden-Wuerttemberg</option>
     * <option value="by">Bayern</option>
     * <option value="be">Berlin</option>
     * <option value="br">Brandenburg</option>
     * <option value="hb">Bremen</option>
     * <option value="hh">Hamburg</option>
     * <option value="he">Hessen</option>
     * <option value="mv">Mecklenburg-Vorpommern</option>
     * <option value="ni">Niedersachsen</option>
     * <option value="nw">Nordrhein-Westfalen</option>
     * <option value="rp">Rheinland-Pfalz</option>
     * <option value="sl">Saarland</option>
     * <option value="sn" selected="">Sachsen</option>
     * <option value="st">Sachsen-Anhalt</option>
     * <option value="sh">Schleswig-Holstein</option>
     * <option value="th">Thüringen</option>
     * </select>
     */

    Bayern( "by", "Bayern"),
    Berlin( "be", "Berlin"),
    Brandenburg( "br", "Brandenburg"),
    Bremen( "hb", "Bremen"),
    Hamburg( "hh", "Hamburg"),
    Hessen( "he", "Hessen"),
    MecklenburgVorpommern( "mv", "Mecklenburg-Vorpommern"),
    Niedersachsen( "ni", "Niedersachsen"),
    NordrheinWestfalen( "nw", "Nordrhein-Westfalen"),
    RheinlandPfalz( "rp", "Rheinland-Pfalz"),
    Saarland( "sl", "Saarland"),
    Sachsen( "sn", "Sachsen"),
    SachsenAnhalt( "st", "Sachsen-Anhalt"),
    SchleswigHolstein( "sh", "Schleswig-Holstein"),
    Thüringen( "th", "Thüringen");

  private String value;

  private String label;

  ELand( String value, String label)
  {
    this.value = value;
    this.label = label;
  }

  public String getValue()
  {
    return value;
  }

  public String getLabel()
  {
    return label;
  }
  
  public static ELand fromValue( String value)
  {
    return LAND_VALUES.stream().filter( l -> value.equalsIgnoreCase( l.value)).findFirst().orElse( null);
  }

  //prevent ever and ever cloning in values()
  private static List<ELand> LAND_VALUES =
    Stream.of( values()).collect( Collectors.collectingAndThen( Collectors.toList(), Collections::unmodifiableList));

  public static List<ELand> valuesAsList()
  {
    return LAND_VALUES;
  }
}
