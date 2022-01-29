package org.texttechnologylab.project.sentiment_radar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Kann eine Vielzahl von Reden enthalten und ist einer Sitzung zugeordnet.
 */
public class Tagesordnungspunkt {
  private List<Rede> reden = new ArrayList<>();
  private Sitzung sitzung;
  private String tagesOrdnungsUeberschrift;

  public String getTagesOrdnungsUeberschrift() {
    return tagesOrdnungsUeberschrift;
  }

  public void setTagesOrdnungsUeberschrift(String tagesOrdnungsUeberschrift) {
    this.tagesOrdnungsUeberschrift = tagesOrdnungsUeberschrift;
  }

  public List<Rede> getReden() {
    return reden;
  }

  public void setReden(List<Rede> reden) {
    this.reden = reden;
  }

  public Sitzung getSitzung() {
    return sitzung;
  }

  public void setSitzung(Sitzung sitzung) {
    this.sitzung = sitzung;
  }

  public void addRede(Rede rede) {
    if (rede != null) {
      this.reden.add(rede);
      rede.setTagesordnungspunkt(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tagesordnungspunkt that = (Tagesordnungspunkt) o;
    return Objects.equals(tagesOrdnungsUeberschrift, that.tagesOrdnungsUeberschrift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagesOrdnungsUeberschrift);
  }
}
