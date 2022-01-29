package org.texttechnologylab.project.sentiment_radar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Informationen zu Personen.
 */
public class Person {
  private String id;
  private String vorname;
  private String nachname;
  private String namenszusatz;
  private String ortszusatz;
  private List<Rede> reden = new ArrayList<>();
  private List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> kommentare = new ArrayList<>();
  private Rolle rolle;
  private String titel;

  public Person() {
  }

  public Person(String id, String vorname, String nachname, String namenszusatz, String ortszusatz, Rolle rolle, String titel) {
    this.id = id;
    this.vorname = vorname;
    this.nachname = nachname;
    this.namenszusatz = namenszusatz;
    this.ortszusatz = ortszusatz;
    this.rolle = rolle;
    this.titel = titel;
  }

  public void addRede(Rede rede) {
    if (rede != null) {
      this.reden.add(rede);
      rede.setRedner(this);
    }
  }

  public void addKommentar(org.texttechnologylab.project.sentiment_radar.model.Kommentar kommentar) {
    if (kommentar != null) {
      this.kommentare.add(kommentar);
    }
  }

  public String getName() {
    StringBuilder sb = new StringBuilder();
    if (titel != null) {
      sb.append(titel).append(" ");
    }
    sb.append(vorname).append(" ").append(nachname);
    if (namenszusatz != null) {
      sb.append(" ").append(namenszusatz);
    }
    return sb.toString();
  }

  public String getNamenszusatz() {
    return namenszusatz;
  }

  public void setNamenszusatz(String namenszusatz) {
    this.namenszusatz = namenszusatz;
  }

  public String getOrtszusatz() {
    return ortszusatz;
  }

  public void setOrtszusatz(String ortszusatz) {
    this.ortszusatz = ortszusatz;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public List<Rede> getReden() {
    return reden;
  }

  public void setReden(List<Rede> reden) {
    this.reden = reden;
  }

  public List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> getKommentare() {
    return kommentare;
  }

  public void setKommentare(List<org.texttechnologylab.project.sentiment_radar.model.Kommentar> kommentare) {
    this.kommentare = kommentare;
  }

  public Rolle getRolle() {
    return rolle;
  }

  public void setRolle(Rolle rolle) {
    this.rolle = rolle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(id, person.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vorname, nachname, namenszusatz, ortszusatz, reden, kommentare, rolle, titel);
  }
}
