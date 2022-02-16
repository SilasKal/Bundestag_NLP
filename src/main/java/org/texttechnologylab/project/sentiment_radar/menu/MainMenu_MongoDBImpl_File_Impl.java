package org.texttechnologylab.project.sentiment_radar.menu;

import org.texttechnologylab.project.sentiment_radar.abstracts.Menu_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.database.*;
import org.texttechnologylab.project.sentiment_radar.model.Rede;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt;

import java.util.List;

public class MainMenu_MongoDBImpl_File_Impl extends Menu_MongoDB_Impl {
  /**
   * This Class contains functions to run a menu, in which the user can choose options.
   */
  private final int MENU_LENGTH = 7;

  public MainMenu_MongoDBImpl_File_Impl(List<Sitzung> sitzungen) {
    super(sitzungen);
  }

  @Override
  protected int getMenulength() {
    return MENU_LENGTH;
  }

  @Override
  protected void printMenu() {
    System.out.println("Bitte gib eine der folgenden Zahlen ein um eine Kategorie zu wählen:");
    System.out.println("1 -- Auflistung von Rednern");
    System.out.println("2 -- Länge der durchschnittlichen Redebeiträge");
    System.out.println("3 -- Redebeiträge mit den meisten Zurufen");
    System.out.println("4 -- In DB uebernehmen");
    System.out.println("5 -- NLP starten");
    System.out.println("6 -- Rest Starten");
    System.out.println("7 -- Bild URLs holen");
    System.out.println("0 -- Programm beenden");
  }

  @Override
  protected void ausfuehren(int auswahl) {
    switch (auswahl) {
      case 0:
        setExit(true);
        System.out.println("Du hast das Programm beendet.");
        break;
      case 1:
        RednerMenu_MongoDBImpl_File_Impl rednerMenu = new RednerMenu_MongoDBImpl_File_Impl(sitzungen);
        rednerMenu.runMenu();
        break;
      case 2:
        AvgRede_File_Impl avgRedeMenu = new AvgRede_File_Impl(sitzungen);
        avgRedeMenu.runMenu();
        break;
      case 3:
        ZurufeMenu_MongoDBImpl_File_Impl zurufeMenu = new ZurufeMenu_MongoDBImpl_File_Impl(sitzungen);
        zurufeMenu.runMenu();
        break;
      case 4:
        upload();
        break;
      case 5:
        NLPMenu_MongoDBImpl_File_Impl nlpMenu = new NLPMenu_MongoDBImpl_File_Impl(sitzungen);
        nlpMenu.runMenu();
        break;
      case 6:
        RestMenu_MongoDBImpl_File_Impl restMenu = new RestMenu_MongoDBImpl_File_Impl(sitzungen);
        restMenu.runMenu();
        break;
      case 7:
        URLMenu_MongoDBImpl_File_Impl urlMenu = new URLMenu_MongoDBImpl_File_Impl(sitzungen);
        urlMenu.runMenu();
        break;
    }
  }

  private void upload() {
    /**
     * uploads all data to mongoDB
     */
    SitzungRepository_MongoDB_Impl sitzungRepository = new SitzungRepository_MongoDB_Impl();
    TagesordnungspunkteRepository_MongoDB_Impl topRepository = new TagesordnungspunkteRepository_MongoDB_Impl();
    PersonRepository_MongoDB_Impl personRepository = new PersonRepository_MongoDB_Impl();
    RedeRepository_MongoDB_Impl redeRepository = new RedeRepository_MongoDB_Impl();
    KommentarRepository_MongoDB_Impl kommentarRepository = new KommentarRepository_MongoDB_Impl();
    for (Sitzung sitzung : sitzungen) {
      System.out.println("Speichere Sitzung: " + sitzung.getNummer());
      sitzung = sitzungRepository.persist(sitzung);
      for (Tagesordnungspunkt tagesordnungspunkt : sitzung.getTagesordnungspunkte()) {
        tagesordnungspunkt = topRepository.persist(tagesordnungspunkt);
        for (Rede rede : tagesordnungspunkt.getReden()) {
          rede.setRedner(personRepository.persist(rede.getRedner()));
          rede = redeRepository.persist(rede);
          rede.getKommentare().forEach(kommentarRepository::persist);
        }
      }
    }
  }
}