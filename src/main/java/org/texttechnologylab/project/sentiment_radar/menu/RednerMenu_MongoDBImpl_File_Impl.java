package org.texttechnologylab.project.sentiment_radar.menu;


import org.texttechnologylab.project.sentiment_radar.abstracts.Menu_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class RednerMenu_MongoDBImpl_File_Impl extends Menu_MongoDB_Impl {
  private final int MENU_LENGTH = 3;

  public RednerMenu_MongoDBImpl_File_Impl(List<Sitzung> sitzungen) {
    super(sitzungen);
  }

  @Override
  protected int getMenulength() {
    return MENU_LENGTH;
  }

  @Override
  protected void printMenu() {
    System.out.println("Wie sollen die Redner aufgelistet werden?");
    System.out.println("1 -- Alle Redner inklusive Fraktionszugehörigkeit");
    System.out.println("2 -- Namensfilter");
    System.out.println("3 -- Auswahl einer Partei/Fraktion");
    System.out.println("0 -- zurück");
  }

  @Override
  protected void ausfuehren(int auswahl) {
    setExit(true);
    System.out.println();
    switch (auswahl) {
      case 0:
        break;
      case 1:
        System.out.println("Liste der Abgeordneten inklusive Anzahl Zeichen: ");
        getAlleRedner().forEach(System.out::println);
        break;
      case 2:
        System.out.println("Gib  einen Namen ein nach dem du suchen möchtest:");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        if (name.isEmpty()) {
          getAlleRedner().forEach(System.out::println);
        } else {
          List<String> result = getAlleRedner().stream().filter(Objects::nonNull)
              .filter(s -> s.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
              .collect(Collectors.toList());
          if (result.size() > 0) {
            result.forEach(System.out::println);
          } else {
            System.out.println("Es wurde kein Abgeordneter mit dem Filter " + name + " gefunden.");
          }
        }
        break;
      case 3:
        System.out.println("Bitte wähle eine Partei aus.");
        Arrays.stream(Fraktion.values()).forEach(f -> System.out.println((f.ordinal() + 1) + " -- " + f.getName()));
        System.out.println((Fraktion.values().length + 2) + " -- abbrechen");
        System.out.println("0 -- Programm beenden");
        int parteiauswahl = getInput(Fraktion.values().length + 1);
        if (parteiauswahl != 0) {
          Fraktion fraktion = Fraktion.values()[parteiauswahl - 1];
          System.out.println(fraktion.getName() + ": ");
          getRednerFuerFraktion(fraktion).forEach(System.out::println);
        }
        break;
    }
    System.out.println();
  }

  private List<String> getAlleRedner() {
      return sitzungen.stream()
          .map(Sitzung::getTagesordnungspunkte).flatMap(List::stream) // Alle Reden
          .map(Tagesordnungspunkt::getReden).flatMap(List::stream) // Alle Tagesordnungspunkte
          .map(Rede::getRedner) // alle Redner
          .map(Person::getName) // Namen der Personen
          .distinct() // Einzigartig
          .collect(Collectors.toList()); // In Liste
  }

  private List<String> getRednerFuerFraktion(Fraktion fraktion) {
    if (fraktion == null) {
      return Collections.emptyList();
    }
    return sitzungen.stream()
        .map(Sitzung::getTagesordnungspunkte).flatMap(List::stream) // Alle Tagesordnungspunkte
        .map(Tagesordnungspunkt::getReden).flatMap(List::stream) // Alle Reden
        .map(Rede::getRedner) // Die Redner der Reden
        .filter(r -> r instanceof Abgeordneter && ((Abgeordneter) r).getFraktion().equals(fraktion)) // Filter alle Abgeordneten nach der Fraktion
        .map(Person::getName) // Der Name der Personen
        .distinct()
        .collect(Collectors.toList());
  }
}