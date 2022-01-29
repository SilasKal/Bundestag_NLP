package org.texttechnologylab.project.sentiment_radar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Informationen zu einer Rede.
 * Kann eine Vielzahl von Kommentaren beinhalten.
 */
public class Rede {
  private String text;
  private org.texttechnologylab.project.sentiment_radar.model.Person redner;
  private org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt tagesordnungspunkt;
  private List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> kommentare = new ArrayList<>();

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public org.texttechnologylab.project.sentiment_radar.model.Person getRedner() {
    return redner;
  }

  public void setRedner(org.texttechnologylab.project.sentiment_radar.model.Person redner) {
    this.redner = redner;
  }

  public org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt getTagesordnungspunkt() {
    return tagesordnungspunkt;
  }

  public void setTagesordnungspunkt(org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt tagesordnungspunkt) {
    this.tagesordnungspunkt = tagesordnungspunkt;
  }

  public List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> getKommentare() {
    return kommentare;
  }

  public void setKommentare(List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> kommentare) {
    this.kommentare = kommentare;
  }

  public void addKommentar(org.texttechnologylab.project.sentiment_radar.model.Kommentar kommentar) {
    if (kommentar != null) {
      this.kommentare.add(kommentar);
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
