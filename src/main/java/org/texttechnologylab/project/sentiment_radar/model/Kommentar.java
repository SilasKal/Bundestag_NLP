package org.texttechnologylab.project.sentiment_radar.model;

import org.texttechnologylab.project.sentiment_radar.abstracts.MongoDBDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Variablen die ein Kommentar haben kann.
 */
public class Kommentar extends MongoDBDocument {
  public static final String MONGO_DB_COLLECTION_NAME = "Kommentar";
  private String inhalt;
  private Boolean isZuruf;
  private Rede rede;
  private List<Fraktion> zurufsFraktionen = new ArrayList<>();

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

  public Rede getRede() {
    return this.rede;
  }

  public void setRede(Rede rede) {
    this.rede = rede;
  }

  public List<Fraktion> getZurufsFraktion() {
    return zurufsFraktionen;
  }
  // Setzt die Fraktion von der ein Zuruf kam
  public void setZurufsFraktion(List<Fraktion> zurufsFraktion) {
    this.zurufsFraktionen = zurufsFraktion;
  }
  // Fügt ggf eine Weitere Fraktion hinzu, falls Zurufe von mehrern Fraktionen gleichzeitig erfolgten.
  public void addZurufsFraktion(Fraktion fraktion) {
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
