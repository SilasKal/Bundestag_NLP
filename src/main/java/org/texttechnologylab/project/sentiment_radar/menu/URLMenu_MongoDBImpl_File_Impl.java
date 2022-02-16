package org.texttechnologylab.project.sentiment_radar.menu;


import org.texttechnologylab.project.sentiment_radar.abstracts.Menu_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.rest.RestSchnittstelle;
import org.texttechnologylab.project.sentiment_radar.urlget.ImageLoader;

import java.util.List;

public class URLMenu_MongoDBImpl_File_Impl extends Menu_MongoDB_Impl {
  /**
   * @author Philipp
   * Submenu to specify a requests regarding URLs
   */
  private final int MENU_LENGTH = 1;

  public URLMenu_MongoDBImpl_File_Impl(List<Sitzung> sitzungen) {
    super(sitzungen);
  }

  @Override
  protected int getMenulength() {
    return MENU_LENGTH;
  }

  @Override
  protected void printMenu() {
    System.out.println("Wollen Sie URLs abfragen");
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
        System.out.println("Lade Urls");
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.add_all_imgs();
        break;
    }
  }
}