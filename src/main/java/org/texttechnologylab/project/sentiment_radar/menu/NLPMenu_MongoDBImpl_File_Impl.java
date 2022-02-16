package org.texttechnologylab.project.sentiment_radar.menu;


import org.texttechnologylab.project.sentiment_radar.NLP.Nlp;
import org.texttechnologylab.project.sentiment_radar.abstracts.Menu_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.rest.RestSchnittstelle;

import java.util.List;

public class NLPMenu_MongoDBImpl_File_Impl extends Menu_MongoDB_Impl {
  /**
   * @author Philipp
   * Submenu to specify a requests regarding NLP
   */
  private final int MENU_LENGTH = 1;

  public NLPMenu_MongoDBImpl_File_Impl(List<Sitzung> sitzungen) {
    super(sitzungen);
  }

  @Override
  protected int getMenulength() {
    return MENU_LENGTH;
  }

  @Override
  protected void printMenu() {
    System.out.println("Wollen Sie NLP starten??");
    System.out.println("1 -- Ja");
    System.out.println("0 -- Nein");
  }

  @Override
  protected void ausfuehren(int auswahl) {
    setExit(true);
    System.out.println();
    switch (auswahl) {
      case 0:
        break;
      case 1:
        System.out.println("starte NLP");
        Nlp.NLPprocess();
        break;
    }
  }
}