package org.texttechnologylab.project.sentiment_radar.model;

import org.texttechnologylab.project.sentiment_radar.abstracts.MongoDBDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Informationen zu einer Rede.
 * Kann eine Vielzahl von Kommentaren beinhalten.
 */
public class Rede extends MongoDBDocument {
  public static final String MONGO_DB_COLLECTION_NAME = "Reden";
  private String text;
  private Person redner;
  private Date datum;
  private Tagesordnungspunkt tagesordnungspunkt;
  private List<Kommentar> kommentare = new ArrayList<>();

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {this.datum = datum;}

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Person getRedner() {
    return redner;
  }

  public void setRedner(Person redner) {
    this.redner = redner;
  }

  public Tagesordnungspunkt getTagesordnungspunkt() {
    return tagesordnungspunkt;
  }

  public void setTagesordnungspunkt(Tagesordnungspunkt tagesordnungspunkt) {
    this.tagesordnungspunkt = tagesordnungspunkt;
  }

  public List<Kommentar> getKommentare() {
    return kommentare;
  }

  public void setKommentare(List<Kommentar> kommentare) {
    this.kommentare = kommentare;
  }

  public void addKommentar(Kommentar kommentar) {
    if (kommentar != null) {
      this.kommentare.add(kommentar);
      kommentar.setRede(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rede rede = (Rede) o;
    return Objects.equals(text, rede.text) && Objects.equals(kommentare, rede.kommentare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, kommentare);
  }
}
