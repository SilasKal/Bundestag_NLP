package org.texttechnologylab.project.sentiment_radar.menu;


import org.texttechnologylab.project.sentiment_radar.abstracts.Menu_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Abgeordneter;
import org.texttechnologylab.project.sentiment_radar.model.Rede;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Untermenü für Abfragen die "Redner" returnen sollen.
 */
public class AvgRede_File_Impl extends Menu_MongoDB_Impl {
  private final int MENU_LENGTH = 3;

  public AvgRede_File_Impl(List<Sitzung> sitzungen) {
    super(sitzungen);
  }

  @Override
  protected int getMenulength() {
    return MENU_LENGTH;
  }

  @Override
  protected void printMenu() {
    System.out.println("Wie sollen die durchschnittlichen Redebeiträge gefiltert werden?");
    System.out.println("1 -- aller Beiträge");
    System.out.println("2 -- pro Abgeordnetem");
    System.out.println("3 -- pro Partei");
    System.out.println("0 -- zurück");
  }
  // Erstellung des Untermenüs um eine spezifischere Abfrage zu erstellen.
  @Override
  protected void ausfuehren(int auswahl) {
    setExit(true);
    System.out.println();
    switch (auswahl) {
      case 0:
        break;
      case 1:
        System.out.println("Die durchschnittle Rededauer eines Beitrags umfasst "
            + getZeichenMitZweiKommastellen(getAlleBeitraege()) + " Zeichen.");
        break;
      case 2:
        System.out.println("Liste der Abgeordneten inklusive Anzahl Zeichen: ");
        getBeitraegeAbgeordneter().forEach((k, v) -> System.out.println(k + ": " + getZeichenMitZweiKommastellen(v)
            + " Zeichen"));
        break;
      case 3:
        System.out.println("Liste der Partein inklusive Anzahl Zeichen: ");
        getBeitraegeFraktion().forEach((k, v) -> System.out.println(k + ": " + getZeichenMitZweiKommastellen(v)
            + " Zeichen"));
        break;
    }
    System.out.println();
  }
  //
  private double getAlleBeitraege() {
    return sitzungen.stream()
        .map(Sitzung::getTagesordnungspunkte).flatMap(List::stream) // Alle Tagesordnungspunkte
        .map(Tagesordnungspunkt::getReden).flatMap(List::stream) // Alle Reden
        .map(Rede::getText)  // Die Texte der Reden
        .mapToInt(String::length) // Länge der Rede erfassen
        .average() // Durchschnitt
        .orElse(0); // Leere Reden zu 0
  }

  private Map<String, Double> getBeitraegeAbgeordneter() {
    return sitzungen.stream()
        .map(Sitzung::getTagesordnungspunkte).flatMap(List::stream) // Alle Reden
        .map(Tagesordnungspunkt::getReden).flatMap(List::stream) // Alle Tagesordnungspunkte
        .collect(Collectors.groupingBy(r -> r.getRedner().getName())) // Gruppiere nach Redner und hole Namen
        .entrySet() // Fasse in Array zusammen
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
            .mapToInt(r -> r.getText().length()) // Ordne Länge der Rede zu
            .average() // Durchschnitt
            .orElse(0))); // Leere Reden zu 0
  }

  private Map<String, Double> getBeitraegeFraktion() {
    return sitzungen.stream()
        .map(Sitzung::getTagesordnungspunkte).flatMap(List::stream) // Alle Reden
        .map(Tagesordnungspunkt::getReden).flatMap(List::stream) // Alle Tagesordnungspunkte
        .filter(r -> r.getRedner() instanceof Abgeordneter) // Filtere Redner die Abgeordnete sind
        .collect(Collectors.groupingBy(r -> ((Abgeordneter) r.getRedner()).getFraktion())) // Gruppiere nach Fraktion
        .entrySet() // Füge in einem Array zusammen
        .stream()
        .collect(Collectors.toMap(e -> e.getKey().getName(), e -> e.getValue().stream()
            .mapToInt(r -> r.getText().length()) // Ordne Länge der Rede zu
            .average() // Durchschnitt
            .orElse(0))); // Leere Reden zu 0
  }
}
