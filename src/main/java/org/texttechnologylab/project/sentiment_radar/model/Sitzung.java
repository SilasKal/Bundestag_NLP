package org.texttechnologylab.project.sentiment_radar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält mehrere Tagsordnungspunkte
 */
public class Sitzung {
  private String nummer;
  private String datum;
  private String legislaturperiode;
  private Person sitzungsleiter;
  private List<org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt> tagesordnungspunkte = new ArrayList<>();

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

  public Person getSitzungsleiter() {
    return sitzungsleiter;
  }

  public void setSitzungsleiter(Person sitzungsleiter) {
    this.sitzungsleiter = sitzungsleiter;
  }

  public List<org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt> getTagesordnungspunkte() {
    return tagesordnungspunkte;
  }

  public void setTagesordnungspunkte(List<org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt> tagesordnungspunkte) {
    this.tagesordnungspunkte = tagesordnungspunkte;
  }

  public void addTagesordnungspunkt(org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt tagesordnungspunkt) {
    if(tagesordnungspunkt != null) {
      this.tagesordnungspunkte.add(tagesordnungspunkt);
      tagesordnungspunkt.setSitzung(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sitzung sitzung = (Sitzung) o;
    return Objects.equals(nummer, sitzung.nummer) && Objects.equals(datum, sitzung.datum) && Objects.equals(legislaturperiode, sitzung.legislaturperiode) && Objects.equals(sitzungsleiter, sitzung.sitzungsleiter) && Objects.equals(tagesordnungspunkte, sitzung.tagesordnungspunkte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nummer, datum, legislaturperiode, sitzungsleiter, tagesordnungspunkte);
  }
}
