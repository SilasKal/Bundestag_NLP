package org.texttechnologylab.project.sentiment_radar.model;

import org.texttechnologylab.project.sentiment_radar.abstracts.MongoDBDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enthält Informationen zu Personen.
 * @author Philipp
 */
public class Person extends MongoDBDocument {
  public static final String MONGO_DB_COLLECTION_NAME = "Personen";
  private String givenId;
  private String vorname;
  private String nachname;
  private String namenszusatz;
  private String ortszusatz;
  private List<Rede> reden = new ArrayList<>();
  private List<Kommentar> kommentare = new ArrayList<>();
  private String rolleKurz;
  private String rolleName;
  private String titel;
  private List<String> bildUrl;
  public Person() {
  }

  public Person(String id, String vorname, String nachname, String namenszusatz, String ortszusatz, String rolleKurz, String rolleName, String titel) {
    this.givenId = id;
    this.vorname = vorname;
    this.nachname = nachname;
    this.namenszusatz = namenszusatz;
    this.ortszusatz = ortszusatz;
    this.rolleKurz = rolleKurz;
    this.rolleName = rolleName;
    this.titel = titel;
  }

  public void addRede(Rede rede) {
    if (rede != null) {
      this.reden.add(rede);
      rede.setRedner(this);
    }
  }

  public void addKommentar(Kommentar kommentar) {
    if (kommentar != null) {
      this.kommentare.add(kommentar);
    }
  }

  public List<String> getBildUrl() {
    return bildUrl;
  }

  public void setBildUrl(List<String> bildUrl) {
    this.bildUrl = bildUrl;
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

  public String getGivenId() {
    return givenId;
  }

  public void setGivenId(String givenId) {
    this.givenId = givenId;
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

  public List<Kommentar> getKommentare() {
    return kommentare;
  }

  public void setKommentare(List<Kommentar> kommentare) {
    this.kommentare = kommentare;
  }

  public String getRolleKurz() {
    return this.rolleKurz;
  }

  public void setRolleKurz(String rolleKurz) {
    this.rolleKurz = rolleKurz;
  }

  public String getRolleName() {
    return this.rolleName;
  }

  public void setRolleName(String rolleName) {
    this.rolleName = rolleName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(givenId, person.givenId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(givenId, vorname, nachname, namenszusatz, ortszusatz, reden, kommentare, rolleKurz, rolleName, titel);
  }
}
