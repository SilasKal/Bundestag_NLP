package org.texttechnologylab.project.sentiment_radar;

import org.texttechnologylab.project.sentiment_radar.menu.MainMenu_MongoDBImpl_File_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.parser.PlenarsitzungParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Der BundestagsAnalizer dient zum Einlesen von Daten und zur Analyse dieser.
 *
 * @author Philipp Dächert s3391912@stud.uni-frankfurt.de
 * Matrikelnummer: 7550687
 * @version 1.0
 */
public class BundestagsAnalizer {

  private static final int FILE_COUNT = 239;
  private static final List<Sitzung> sitzungen = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("\t\tBundestagsAnalizer von Philipp Dächert\n\t\tMatrikelnummer: 7550687");
    System.out.println("Lade Sitzungen...");
    parseXmls();
    System.out.println("Sitzungen geladen!");
    MainMenu_MongoDBImpl_File_Impl mainMenu = new MainMenu_MongoDBImpl_File_Impl(sitzungen);
    mainMenu.runMenu();
  }

  private static void parseXmls() {
    /**
     * Diese Funktion liest alle XML Datein in Resourcen ein
     */
    for (int i = 1; i < FILE_COUNT + 1; i++) {
      sitzungen.add(PlenarsitzungParser.parseXmlFile(Integer.toString(i)));
    }
  }
}


