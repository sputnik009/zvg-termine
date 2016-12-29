package de.magic.creation.repo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * <option value="1">Reihenhaus</option>
 * <option value="2">Doppelhaushälfte</option>
 * <option value="3">Einfamilienhaus</option>
 * <option value="19">Zweifamilienhaus</option>
 * <option value="4">Mehrfamilienhaus</option>
 * <option value="5">Eigentumswohnung (1 bis 2 Zimmer)</option>
 * <option value="6">Eigentumswohnung (3 bis 4 Zimmer)</option>
 * <option value="7">Eigentumswohnung (ab 5 Zimmer)</option>
 * <option value="8">Gewerbeeinheit (z.B. Laden, Büro)</option>
 * <option value="9">Garage</option>
 * <option value="10">Kfz-Stellplatz</option>
 * <option value="11">Kfz-Stellplatz (Tiefgarage)</option>
 * <option value="12">sonstiges Teileigentum (z.B. Keller, Hobbyraum)</option>
 * <option value="13">Wohn-/Geschäftshaus</option>
 * <option value="14">gewerblich genutztes Grundstück</option>
 * <option value="15">Baugrundstück</option>
 * <option value="16">unbebautes Grundstück</option>
 * <option value="17">land- und forstwirtschaftlich genutztes Grundstück</option>
 * <option value="18">Sonstiges</option>
 * </select>
 */
public enum EKind
{
    Reihenhaus( 1, "Reihenhaus"),
    Doppelhaushälfte( 2, "Doppelhaushälfte"),
    Einfamilienhaus( 3, "Einfamilienhaus"),
    Zweifamilienhaus( 19, "Zweifamilienhaus"),
    Mehrfamilienhaus( 4, "Mehrfamilienhaus"),
    Eigentumswohnung_1_2( 5, "Eigentumswohnung (1 bis 2 Zimmer)"),
    Eigentumswohnung_3_4( 6, "Eigentumswohnung (3 bis 4 Zimmer)"),
    Eigentumswohnung_5( 7, "Eigentumswohnung (ab 5 Zimmer)"),
    Gewerbeeinheit( 8, "Gewerbeeinheit"),
    Garage( 9, "Garage"),
    KfzStellplatz( 10, "Kfz-Stellplatz"),
    KfzStellplatzTiefgarage( 11, "Kfz-Stellplatz (Tiefgarage)"),
    SonstigesTeileigentum( 12, "sonstiges Teileigentum"),
    WohnGeschäftshaus( 13, "Wohn-/Geschäftshaus"),
    GewerblichGenutztesGrundstück( 14, "gewerblich genutztes Grundstück"),
    Baugrundstück( 15, "Baugrundstück"),
    UnbebautesGrundstück( 16, "unbebautes Grundstück"),
    LandUndForstwirtschaftlichGenutztesGrundstück( 17, "land- und forstwirtschaftlich genutztes Grundstück"),
    Sonstiges( 18, "Sonstiges");

  private int    id;

  private String label;

  EKind( int id, String label)
  {
    this.id = id;
    this.label = label;
  }

  public int getId()
  {
    return id;
  }

  public String getLabel()
  {
    return label;
  }

  //prevent ever and ever cloning in values()
  private static List<EKind> KIND_VALUES =
    Stream.of( values()).collect( Collectors.collectingAndThen( Collectors.toList(), Collections::unmodifiableList));

  public static List<EKind> valuesAsList()
  {
    return KIND_VALUES;
  }

  public static EKind fromLabel( String label)
  {
    if( label == null || label.trim().isEmpty()) return null;
    String cleanedLabel = cleanLabel( label);

    return KIND_VALUES.stream().filter( k -> cleanedLabel.contains( cleanLabel( k.label))).findFirst().orElse( null);
  }

  private static String cleanLabel( String label)
  {
    String cleanedLabel = label.trim().toLowerCase();

    return cleanedLabel;
  }
}
