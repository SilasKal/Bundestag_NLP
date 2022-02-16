package org.texttechnologylab.project.sentiment_radar.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class DocFetch {
  public static ArrayList<String> urlCollector() throws Exception{
    int counter = 0;
    ArrayList<String> linkliste19 = new ArrayList<>();
    while (counter <= 13) {
      String url1 = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?limit=10&noFilterSet=true&offset=" + counter;
      System.out.println(url1);
      linkliste19 = getprotokolllinks(linkliste19, url1,19);
      counter+= 10;
    }
    counter = 0;
    ArrayList<String> linkliste20 = new ArrayList<>();
    while (counter <= 230) {
      String url2 = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?limit=10&noFilterSet=true&offset=" + counter;
      System.out.println(url2);
      linkliste20 = getprotokolllinks(linkliste20, url2, 20);
      counter +=10;
    }

    counter =0;
    linkliste19.addAll(linkliste20);
    return linkliste19;
  }
  public static ArrayList<String> getprotokolllinks(ArrayList<String> linkliste, String url, int wahlperiode) {
    int counter = 0;
    ArrayList<String> returnlist = new ArrayList<>();
    try {
      final Document document = Jsoup.connect(url).get();
      for (Element row : document.select("a")) {
        if (!linkliste.contains("https://www.bundestag.de"+row.attr("href"))) {
          linkliste.add("https://www.bundestag.de"+row.attr("href"));
        }
      }
      return linkliste;
    } catch (Exception e) {
      System.out.println("Website überlastet");
      return returnlist;
    }
  }
}
