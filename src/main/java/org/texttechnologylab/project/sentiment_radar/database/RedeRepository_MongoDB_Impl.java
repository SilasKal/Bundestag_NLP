package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Person;
import org.texttechnologylab.project.sentiment_radar.model.Rede;
import org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RedeRepository_MongoDB_Impl implements AbstractRepository<Rede> {

  public List<Rede> findByTagesordnungspunktId(ObjectId tagesordnungspunktId) {
    List<Rede> reden = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("tagesordnungspunkt_id", tagesordnungspunktId))) {
      reden.add(getObject(document));
    }
    return reden;
  }

  public List<Rede> findByRednerId(ObjectId rednerId) {
    List<Rede> reden = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("redner_id", rednerId))) {
      reden.add(getObject(document));
    }
    return reden;
  }

  @Override
  public String getCollectionName() {
    return Rede.MONGO_DB_COLLECTION_NAME;
  }

  @Override
  public Document createDocument(Rede rede) {
    Document document = new Document();
    document.append("text", rede.getText());
    document.append("redner_id", Optional.ofNullable(rede.getRedner()).map(Person::getId).orElse(null));
    document.append("tagesordnungspunkt_id", Optional.ofNullable(rede.getTagesordnungspunkt()).map(Tagesordnungspunkt::getId).orElse(null));
    return document;
  }

  @Override
  public Rede getObject(Document document) {
    Rede rede = new Rede();
    rede.setId(document.get("_id", ObjectId.class));
    rede.setText(document.get("text", String.class));
    return rede;
  }
}