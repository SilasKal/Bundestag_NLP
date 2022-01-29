package org.texttechnologylab.project.sentiment_radar.menu;

import org.texttechnologylab.project.sentiment_radar.abstracts.Menu;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;

import java.util.List;

public class MainMenu_File_Impl extends Menu {
  private final int MENU_LENGTH = 3;

  public MainMenu_File_Impl(List<Sitzung> sitzungen) {
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
        org.texttechnologylab.project.sentiment_radar.menu.RednerMenu_File_Impl rednerMenu = new org.texttechnologylab.project.sentiment_radar.menu.RednerMenu_File_Impl(sitzungen);
        rednerMenu.runMenu();
        break;
      case 2:
        org.texttechnologylab.project.sentiment_radar.menu.AvgRede_File_Impl avgRedeMenu = new org.texttechnologylab.project.sentiment_radar.menu.AvgRede_File_Impl(sitzungen);
        avgRedeMenu.runMenu();
        break;
      case 3:
        ZurufeMenu_File_Impl zurufeMenu = new ZurufeMenu_File_Impl(sitzungen);
        zurufeMenu.runMenu();
        break;
    }
  }
}