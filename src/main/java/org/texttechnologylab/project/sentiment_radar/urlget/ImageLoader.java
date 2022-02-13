package org.texttechnologylab.project.sentiment_radar.urlget;

import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.Iterator;
import java.util.List;


public class ImageLoader {
    MongoDatabase database;
    public ImageLoader() {
        String uri = "mongodb://" +
                "PRG_WiSe21_Gruppe_7_1" + ":" +
                "MY8RBPHr" + "@" +
                "prg2021.texttechnologylab.org" + ":" + "27020" +  "/" +
                "PRG_WiSe21_Gruppe_7_1" + "?authSource=" +
                "PRG_WiSe21_Gruppe_7_1";

        MongoClient client = MongoClients.create(uri);
        MongoDatabase db = client.getDatabase("PRG_WiSe21_Gruppe_7_1");
        this.database = db;
    }
    public void add_all_imgs() {
        MongoCollection coll = this.database.getCollection("Personen");
        // coll.insertOne(new Document().append("dies", "das"));

        FindIterable<Bson> iterDoc = coll.find();
        Iterator it = iterDoc.iterator();

        while(it.hasNext()) {
            Document doc = (Document) it.next();
            String vorname = (String) doc.get("vorname");
            String nachname = (String) doc.get("nachname");
            System.out.println(vorname + " " + nachname);
            List<String> url = SiteScanner.get_pic_url(vorname, nachname);
            System.out.println(url.get(0));
            System.out.println(url.get(1));
            try {
                Thread.sleep(3000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            coll.updateOne((Bson) doc, Updates.set("url", url));
        }
        System.out.println("Alle Bilder einfefügt");
    }
}
