package org.texttechnologylab.project.sentiment_radar.model;

import java.util.Objects;

/**
 * Abgeordnete sind Personen und können einer Fraktion angehören
 */
public class Abgeordneter extends org.texttechnologylab.project.sentiment_radar.model.Person {
  private org.texttechnologylab.project.sentiment_radar.model.Fraktion fraktion;

  public Abgeordneter() {
  }

  public Abgeordneter(String id, String vorname, String nachname, String namenszusatz, String ortszusatz, org.texttechnologylab.project.sentiment_radar.model.Rolle rolle,
                      String titel, org.texttechnologylab.project.sentiment_radar.model.Fraktion fraktion) {
    super(id, vorname, nachname, namenszusatz, ortszusatz, rolle, titel);
    this.fraktion = fraktion;
  }

  @Override
  public String getName() {
    return super.getName() + " (" + fraktion.getName() + ")";
  }

  public org.texttechnologylab.project.sentiment_radar.model.Fraktion getFraktion() {
    return fraktion;
  }

  public void setFraktion(org.texttechnologylab.project.sentiment_radar.model.Fraktion fraktion) {
    this.fraktion = fraktion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Abgeordneter that = (Abgeordneter) o;
    return Objects.equals(fraktion, that.fraktion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), fraktion);
  }
}
