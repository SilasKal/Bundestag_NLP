package org.texttechnologylab.project.sentiment_radar;

import org.texttechnologylab.project.sentiment_radar.menu.MainMenu_MongoDBImpl_File_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.parser.PlenarsitzungParser;
import org.texttechnologylab.project.sentiment_radar.parser.docFetch;

import java.util.ArrayList;
import java.util.List;

/**
 * The BundestagsdAnalizer downloads and analyses minutes of Bundestags meetings.
 *
 * @author Philipp Dächert s3391912@stud.uni-frankfurt.de
 * Matrikelnummer: 7550687
 * @version 1.3
 */
public class BundestagsAnalizer {

  private static final List<Sitzung> sitzungen = new ArrayList<>();

  public static void main(String[] args) throws Exception {
    System.out.println("\t\tBundestagsAnalizer von\n\t\t Philipp Dächert\n\t\t\tMatrikelnummer: 7550687" +
        "\n\t\t Silas Kalinowski\n\t\t\tMatrikelnummer: 7417739\n\t\t Ben Schäfer\n\t\t\tMatrikelnummer: 7509560");
    System.out.println("Lade Sitzungen...");
    parseXmls();
    System.out.println("Sitzungen geladen!");
    MainMenu_MongoDBImpl_File_Impl mainMenu = new MainMenu_MongoDBImpl_File_Impl(sitzungen);
    mainMenu.runMenu();
  }

  private static void parseXmls() throws Exception {
    /**
     * Diese Funktion liest alle XML Datein in Resourcen ein
     */
    ArrayList<String> linkListe = new ArrayList<String>(new docFetch().urlCollector());
    for (int i = 0; i < linkListe.size(); i++) {
      sitzungen.add(PlenarsitzungParser.parseXmlFile(linkListe.get(i)));
    }
  }
}


