package org.texttechnologylab.project.sentiment_radar.urlget;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ImgUrlGetter {
  /**
   *
   * @param first_name
   * @param last_name
   * @return
   */
  public static List<String> get_pic_url(String first_name, String last_name) {
    /**
     * @return picture URL
     */
    last_name = "Lindh";
    first_name = "Helge";
    String website = "https://bilddatenbank.bundestag.de/search/picture-result?filterQuery%5Bname%5D%5B%5D=" +
        last_name + "%2C+" +
        first_name +
        "&filterQuery%5Bereignis%5D%5B%5D=Porträt%2FPortrait&sortVal=3";

    Connection connection = Jsoup.connect(website);
    try {
      Document document = connection.execute().parse();
      Elements images = document.getElementsByTag("img");
      Iterator iter = images.iterator();
      Element img;
      while(iter.hasNext()) {
        img = (Element) iter.next();
        String img_description = img.attr("title");
        if (img_description.contains(last_name)) {
          String img_url = img.attr("src");
          System.out.println(website);
          return Arrays.asList(img_url, img_description);
        }
      }
      return Arrays.asList("funny_picture_url", "Leider kein Bild vorhanden");

    }
    catch(IOException e) {
      System.out.println("Fehler beim Lesen von Bilder-URL von " + first_name + " " + last_name);
      return Arrays.asList("funny_picture_url", "Leider kein Bild vorhanden");
    }
  }
}
