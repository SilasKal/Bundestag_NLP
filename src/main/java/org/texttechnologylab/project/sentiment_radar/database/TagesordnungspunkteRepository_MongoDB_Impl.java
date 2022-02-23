package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Sitzung;
import org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagesordnungspunkteRepository_MongoDB_Impl implements AbstractRepository<Tagesordnungspunkt> {
  /**
   * Operations to Tagesordnungspunkten
   * @author Philipp
   * @param tagesordnungspunktId
   * @return
   */
  public List<Tagesordnungspunkt> findBySitzungsId(ObjectId tagesordnungspunktId) {
    List<Tagesordnungspunkt> tagesordnungspunkte = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("_id", tagesordnungspunktId))) {
      tagesordnungspunkte.add(getObject(document));
    }
    return tagesordnungspunkte;
  }

  @Override
  public String getCollectionName() {
    return Tagesordnungspunkt.MONGO_DB_COLLECTION_NAME;
  }

  @Override
  public Document createDocument(Tagesordnungspunkt tagesordnung) {
    Document document = new Document();
    document.append("sitzung_id", Optional.ofNullable(tagesordnung.getSitzung()).map(Sitzung::getId).orElse(null));
    document.append("tagesOrdnungsUeberschrift", tagesordnung.getTagesOrdnungsUeberschrift());
    return document;
  }

  @Override
  public Tagesordnungspunkt getObject(Document document) {
    Tagesordnungspunkt tagesordnungspunkt = new Tagesordnungspunkt();
    tagesordnungspunkt.setId(document.get("_id", ObjectId.class));
    tagesordnungspunkt.setTagesOrdnungsUeberschrift(document.get("tagesOrdnungsUeberschrift", String.class));
    return tagesordnungspunkt;
  }
}
