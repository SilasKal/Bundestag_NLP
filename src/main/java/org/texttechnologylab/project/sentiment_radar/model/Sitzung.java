package org.texttechnologylab.project.sentiment_radar.model;

import org.texttechnologylab.project.sentiment_radar.abstracts.MongoDBDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält mehrere Tagsordnungspunkte
 * @author Philipp
 */
public class Sitzung extends MongoDBDocument {
  public static final String MONGO_DB_COLLECTION_NAME = "Sitzungen";
  private String nummer;
  private String datum;
  private String legislaturperiode;
  private List<Tagesordnungspunkt> tagesordnungspunkte = new ArrayList<>();

  public Sitzung() {

  }

  public Sitzung(String nummer, String datum, String legislaturperiode) {
    this.nummer = nummer;
    this.datum = datum;
    this.legislaturperiode = legislaturperiode;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public String getLegislaturperiode() {
    return legislaturperiode;
  }

  public void setLegislaturperiode(String legislaturperiode) {
    this.legislaturperiode = legislaturperiode;
  }

  public List<Tagesordnungspunkt> getTagesordnungspunkte() {
    return tagesordnungspunkte;
  }

  public void setTagesordnungspunkte(List<Tagesordnungspunkt> tagesordnungspunkte) {
    this.tagesordnungspunkte = tagesordnungspunkte;
  }

  public void addTagesordnungspunkt(Tagesordnungspunkt tagesordnungspunkt) {
    if (tagesordnungspunkt != null) {
      this.tagesordnungspunkte.add(tagesordnungspunkt);
      tagesordnungspunkt.setSitzung(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sitzung sitzung = (Sitzung) o;
    return Objects.equals(nummer, sitzung.nummer) && Objects.equals(datum, sitzung.datum) && Objects.equals(legislaturperiode, sitzung.legislaturperiode) && Objects.equals(tagesordnungspunkte, sitzung.tagesordnungspunkte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nummer, datum, legislaturperiode, tagesordnungspunkte);
  }
}
