package org.texttechnologylab.project.sentiment_radar.database;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;

public class SitzungRepository_MongoDB_Impl implements AbstractRepository<Sitzung> {

  @Override
  public String getCollectionName() {
    return Sitzung.MONGO_DB_COLLECTION_NAME;
  }

  @Override
  public Document createDocument(Sitzung sitzung) {
    Document document = new Document();
    document.append("nummer", sitzung.getNummer());
    document.append("datum", sitzung.getDatum());
    document.append("legislaturperiode", sitzung.getLegislaturperiode());
    return document;
  }

  @Override
  public Sitzung getObject(Document document) {
    Sitzung sitzung = new Sitzung();
    sitzung.setId(document.get("_id", ObjectId.class));
    sitzung.setNummer(document.get("nummer", String.class));
    sitzung.setDatum(document.get("datum", String.class));
    sitzung.setLegislaturperiode(document.get("legislaturperiode", String.class));
    return sitzung;
  }
}
