package org.texttechnologylab.project.sentiment_radar.abstracts;

import org.texttechnologylab.project.sentiment_radar.model.Sitzung;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

/**
 * Hier sind die Rahmenbedingungen für Menüs gegeben.
 */
public abstract class Menu_MongoDB_Impl {
  protected boolean exit;
  protected List<Sitzung> sitzungen;

  protected boolean isExit() {
    return exit;
  }

  protected void setExit(boolean exit) {
    this.exit = exit;
  }

  public Menu_MongoDB_Impl(List<Sitzung> sitzungen) {
    this.sitzungen = sitzungen;
  }

  /**
   * Diese Funktion lässt das Menu laufen solange exit nicht True wird.
   */
  public void runMenu() {
    while (!exit) {
      printMenu();//gebe Menü aus
      int auswahl = getInput();//erhalte Input vom Benutzer
      ausfuehren(auswahl);//Führe die gewählte Funktion aus
    }
  }

  /**
   * Rundet eine Double Zahl auf zwei Nachkommastellen und gibt diese als String aus.
   * @param zeichen
   * @return
   */
  protected String getZeichenMitZweiKommastellen(double zeichen) {
    return BigDecimal.valueOf(zeichen).setScale(2, RoundingMode.HALF_UP).toString();
  }

  protected abstract int getMenulength();

  protected abstract void printMenu();

  protected abstract void ausfuehren(int auswahl);

  protected int getInput() {
    return getInput(getMenulength());
  }

  /**
   * Funktion fragt einen Wert ab und überprüft ob dieser eine Valide Eingabe in das Menü ist. Dafür muss die Anzahl
   * der möglichen Menüpunkte übergeben werden.
   * @author Philipp
   * @param length die Länge der Ausgabe
   * @return die gewählte Ausgabe
   */
  protected int getInput(int length) {
    Scanner scan = new Scanner(System.in);
    int auswahl = -1;
    while (auswahl < 0 || auswahl > length) {
      try {
        System.out.print("\nBitte triff eine Auswahl:");
        auswahl = Integer.parseInt(scan.nextLine());
        if (auswahl > length) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        System.out.println("Ungültige Eingabe");
      }
    }
    return auswahl;
  }
}
