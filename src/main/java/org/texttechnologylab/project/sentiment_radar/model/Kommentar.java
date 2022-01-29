package org.texttechnologylab.project.sentiment_radar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Variablen die ein Kommentar haben kann.
 */
public class Kommentar {
  private String inhalt;
  private Boolean isZuruf;
  private List<org.texttechnologylab.project.sentiment_radar.model.Fraktion> zurufsFraktionen = new ArrayList<>();

  public String getInhalt() {
    return inhalt;
  }

  public void setInhalt(String inhalt) {
    this.inhalt = inhalt;
  }

  public Boolean getZuruf() {
    return isZuruf;
  }

  public void setZuruf(Boolean zuruf) {
    isZuruf = zuruf;
  }

  public List<org.texttechnologylab.project.sentiment_radar.model.Fraktion> getZurufsFraktion() {
    return zurufsFraktionen;
  }
  // Setzt die Fraktion von der ein Zuruf kam
  public void setZurufsFraktion(List<org.texttechnologylab.project.sentiment_radar.model.Fraktion> zurufsFraktion) {
    this.zurufsFraktionen = zurufsFraktion;
  }
  // Fügt ggf eine Weitere Fraktion hinzu, falls Zurufe von mehrern Fraktionen gleichzeitig erfolgten.
  public void addZurufsFraktion(org.texttechnologylab.project.sentiment_radar.model.Fraktion fraktion) {
    if (fraktion != null) {
      this.zurufsFraktionen.add(fraktion);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kommentar kommentar = (Kommentar) o;
    return Objects.equals(inhalt, kommentar.inhalt) && Objects.equals(isZuruf, kommentar.isZuruf) && Objects.equals(zurufsFraktionen, kommentar.zurufsFraktionen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inhalt, isZuruf, zurufsFraktionen);
  }
}
