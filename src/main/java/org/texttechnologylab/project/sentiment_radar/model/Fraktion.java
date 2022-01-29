package org.texttechnologylab.project.sentiment_radar.model;

import java.util.Arrays;

/**
 * Alle möglichen Fraktionen denen ein Abgeordneter angehören kann
 */
public enum Fraktion {
  CDU_CSU("CDU/CSU"),
  SPD("SPD"),
  BUENDNIS_90_DIE_GRUENEN("BÜNDNIS 90/ DIE GRÜNEN"),
  FDP("FDP"),
  AFD("AFD"),
  DIE_LINKE("DIE LINKE"),
  FRAKTIONSLOS("FRAKTIONSLOS");

  private final String name;

  Fraktion(String name) {
    this.name = name;
  }
  // Filtert einige Rechtschreibfehler im Datensatz heraus
  public static Fraktion getByNameIgnoreSpacesCaptilization(String name) {
    if(name == null) {
      return null;
    }
    return Arrays.stream(Fraktion.values())
        .filter(p -> p.name.replaceAll(" ", "")
            .equalsIgnoreCase(name.replaceAll(" ", "").replace("\u00a0", "")))
        .findAny()
        .orElse(null);
  }

  public String getName() {
    return this.name;
  }
}
