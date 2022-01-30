package org.texttechnologylab.project.sentiment_radar.parser;

import org.texttechnologylab.project.sentiment_radar.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

/**
 * Parst alle Daten in Resource/xml/data
 * @author Philipp Dächert
 * Matrikelnummer 7550687
 */
public final class PlenarsitzungParser {

  public static void parsePlenarFile() throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
    File file = new File(PlenarsitzungParser.class.getClassLoader().getResource("dbtplenarprotokoll.dtd").toURI());
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(file);
    doc.getDocumentElement().normalize();

  }
  public static Sitzung parseXmlFile(String url) {
    try {
      // Bereite Dokument vor
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new URL(url).openStream());
      doc.getDocumentElement().normalize();

      // Lese Sitzungsdaten aus
      String datum = doc.getDocumentElement().getAttribute("sitzung-datum");
      String legislaturperiode = doc.getDocumentElement().getAttribute("wahlperiode");
      String nummer = doc.getDocumentElement().getAttribute("sitzung-nr");
      Sitzung sitzung = new Sitzung(nummer, datum, legislaturperiode);

      // Hole Tagesordnungspunkte
      NodeList tagesordnungspunkte = ((Element) doc.getDocumentElement().getElementsByTagName("sitzungsverlauf").item(0))
          .getElementsByTagName("tagesordnungspunkt");
      for (int i = 0; i < tagesordnungspunkte.getLength(); i++) {
        Node tagesordnungspunkt = tagesordnungspunkte.item(i);
        if (tagesordnungspunkt.getNodeType() == Node.ELEMENT_NODE) {
          sitzung.addTagesordnungspunkt(parseTagesordnungspunkt((Element) tagesordnungspunkt));
        }
      }
      return sitzung;
//      parseRednerListe(doc);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Tagesordnungspunkt parseTagesordnungspunkt(Element topElement) {
    Tagesordnungspunkt top = new Tagesordnungspunkt();

    top.setTagesOrdnungsUeberschrift(topElement.getAttribute("top-id"));
    // Hole reden
    NodeList reden = topElement.getElementsByTagName("rede");
    for (int j = 0; j < reden.getLength(); j++) {
      Node nRede = reden.item(j);
      if (nRede.getNodeType() == Node.ELEMENT_NODE) {
        top.addRede(parseRede((Element) nRede));
      }
    }
    return top;
  }

  private static Rede parseRede(Element redeElement) {
    Rede rede = new Rede();

    // Setze Redner
    Person redner = parseRednerElement((Element) redeElement.getElementsByTagName("redner").item(0));
    redner.addRede(rede);

    StringBuilder redenText = new StringBuilder();

    // Hole alle Paragraphen der Rede
    NodeList texte = redeElement.getElementsByTagName("p");
    for (int k = 0; k < texte.getLength(); k++) {
      Node nText = texte.item(k);
      if (nText.getNodeType() == Node.ELEMENT_NODE) {
        Element text = (Element) nText;
        if (!text.getAttribute("klasse").equals("redner")) {
          redenText.append(text.getTextContent()).append("\n");
        }
      }
    }
    rede.setText(redenText.toString());

    // Hole alle Kommentare der Rede
    NodeList kommentare = redeElement.getElementsByTagName("kommentar");
    for (int k = 0; k < kommentare.getLength(); k++) {
      Node nKommentar = kommentare.item(k);
      if (nKommentar.getNodeType() == Node.ELEMENT_NODE) {
        rede.addKommentar(parseKommentar((Element) nKommentar));
      }
    }
    return rede;
  }
  private static Kommentar parseKommentar(Element kommentarElement) {
    Kommentar kommentar = new Kommentar();
    // Setze Kommentarinhalt, entferne Klammern
    String kommentarinhalt = kommentarElement.getTextContent()
        .replaceAll("\\(", "")
        .replaceAll("\\)", "");
    kommentar.setInhalt(kommentarinhalt);
    // Teile Kommentare an "-"
    String[] inhalte = kommentarinhalt.toLowerCase(Locale.ROOT).split("-");
    // Überprüfe ob Zuruf enthalten ist
    for (String inhalt : inhalte) {
      if (inhalt.contains("zuruf")) {
        kommentar.setZuruf(Boolean.TRUE);
        for (int i = 0; i < Fraktion.values().length; i++) {
          if (inhalt.contains(Fraktion.values()[i].getName().toLowerCase(Locale.ROOT))) {
            kommentar.addZurufsFraktion(Fraktion.values()[i]);
          }
        }
      }
    }
    return kommentar;
  }

  private static Person parseRednerElement(Element rednerElement) {
    // Hole Id der Person
    String id = rednerElement.getAttribute("id");

    Node name = rednerElement.getElementsByTagName("name").item(0);
    Element nameElement = (Element) name;

    NodeList nameChildren = nameElement.getChildNodes();
    String titel = null;
    String vorname = null;
    String nachname = null;
    String namenszusatz = null;
    String fraktionskennzeichnung = null;
    String ortszusatz = null;
    String rolleKurz = null;
    String rolleLang = null;

    for (int i = 0; i < nameChildren.getLength(); i++) {
      Node childElement = nameChildren.item(i);
      if (childElement.getNodeType() == Node.ELEMENT_NODE) {
        Element eNameElement = (Element) childElement;
        switch (eNameElement.getNodeName()) {
          case "titel":
            titel = eNameElement.getTextContent();
            break;
          case "vorname":
            vorname = eNameElement.getTextContent();
            break;
          case "nachname":
            nachname = eNameElement.getTextContent();
            break;
          case "namenszusatz":
            namenszusatz = eNameElement.getTextContent();
            break;
          case "fraktion":
            fraktionskennzeichnung = eNameElement.getTextContent();
            break;
          case "ortszusatz":
            ortszusatz = eNameElement.getTextContent();
            break;
          case "rolle":
            rolleLang = eNameElement.getElementsByTagName("rolle_lang").item(0).getTextContent();
            rolleKurz = eNameElement.getElementsByTagName("rolle_kurz").item(0).getTextContent();
            break;
          default:
            break;
        }
      }
    }
    // Parse Fraktion der Person
    Fraktion fraktion = Fraktion.getByNameIgnoreSpacesCaptilization(fraktionskennzeichnung);
    if (fraktion != null) {
      return new Abgeordneter(id, vorname, nachname, namenszusatz, ortszusatz, rolleKurz, rolleLang, titel, fraktion);
    }
    return new Person(id, vorname, nachname, namenszusatz, ortszusatz, rolleKurz, rolleLang, titel);
  }
  // Parse Redner
  private static void parseRednerListe(Document doc) {
    Node rednerliste = doc.getDocumentElement().getElementsByTagName("rednerliste").item(0);
    NodeList nList = rednerliste.getChildNodes();

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Node redner = nList.item(temp);

      if (redner.getNodeType() == Node.ELEMENT_NODE) {
        Element eRedner = (Element) redner;
        Person person = parseRednerElement(eRedner);
      }
    }
  }
}
