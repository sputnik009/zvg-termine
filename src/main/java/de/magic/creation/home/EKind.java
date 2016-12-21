package de.magic.creation.home;

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
 * <option value="13">Wohn-/Geschäftshaus</option>
 * <option value="9">Garage</option>
 * <option value="10">Kfz-Stellplatz</option>
 * <option value="11">Kfz-Stellplatz (Tiefgarage)</option>
 * <option value="12">sonstiges Teileigentum (z.B. Keller, Hobbyraum)</option>
 * <option value="14">gewerblich genutztes Grundstück</option>
 * <option value="15">Baugrundstück</option>
 * <option value="16">unbebautes Grundstück</option>
 * <option value="17">land- und forstwirtschaftlich genutztes Grundstück</option>
 * <option value="18">Sonstiges</option>
 * </select>
 */
public enum EKind
{
    Einfamilienhaus( 3, "Einfamilienhaus"),
    Zweifamilienhaus( 19, "Zweifamilienhaus"),
    Doppelhaushälfte( 2, "Doppelhaushälfte"),
    Eigentumswohnung_1_2( 5, "Eigentumswohnung (1 bis 2 Zimmer)"),
    Eigentumswohnung_3_4( 6, "Eigentumswohnung (3 bis 4 Zimmer)"),
    Baugrundstück( 15, "Baugrundstück"),
    UnbebautesGrundstück( 16, "unbebautes Grundstück");

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
}
