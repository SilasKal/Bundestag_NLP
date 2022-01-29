package org.texttechnologylab.project.sentiment_radar.model;

import java.util.Objects;

/**
 * Enthält Informationen zu besonderen Rollen einer Person
 */
public class Rolle {
  private String kurzbeschreibung;
  private String name;

  public Rolle(){
  }

  public Rolle(String kurzbeschreibung, String name) {
    this.kurzbeschreibung = kurzbeschreibung;
    this.name = name;
  }

  public String getKurzbeschreibung() {
    return kurzbeschreibung;
  }

  public void setKurzbeschreibung(String kurzbeschreibung) {
    this.kurzbeschreibung = kurzbeschreibung;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rolle rolle = (Rolle) o;
    return Objects.equals(kurzbeschreibung, rolle.kurzbeschreibung) && Objects.equals(name, rolle.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kurzbeschreibung, name);
  }
}
