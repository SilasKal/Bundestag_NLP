package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Fraktion;
import org.texttechnologylab.project.sentiment_radar.model.Kommentar;
import org.texttechnologylab.project.sentiment_radar.model.Person;
import org.texttechnologylab.project.sentiment_radar.model.Rede;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KommentarRepository_MongoDB_Impl implements AbstractRepository<Kommentar> {

  public List<Kommentar> findByPersonId(ObjectId personId) {
    List<Kommentar> kommentare = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("person_id", personId))) {
      kommentare.add(getObject(document));
    }
    return kommentare;
  }

  public List<Kommentar> findByRedeId(ObjectId redeId) {
    List<Kommentar> kommentare = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("rede_id", redeId))) {
      kommentare.add(getObject(document));
    }
    return kommentare;
  }

  @Override
  public String getCollectionName() {
    return Kommentar.MONGO_DB_COLLECTION_NAME;
  }

  @Override
  public Document createDocument(Kommentar kommentar) {
    Document document = new Document();
    document.append("inhalt", kommentar.getInhalt());
    document.append("is_zuruf", kommentar.getZuruf());
    document.append("person_id", Optional.ofNullable(kommentar.getRede()).map(Rede::getRedner).map(Person::getId).orElse(null));
    document.append("rede_id", Optional.ofNullable(kommentar.getRede()).map(Rede::getId).orElse(null));
    document.append("zurufs_fraktionen", kommentar.getZurufsFraktion().stream()
        .map(Fraktion::getName)
        .collect(Collectors.joining(",")));
    return document;
  }

  @Override
  public Kommentar getObject(Document document) {
    Kommentar kommentar = new Kommentar();
    kommentar.setId(document.get("_id", ObjectId.class));
    kommentar.setInhalt(document.get("inhalt", String.class));
    kommentar.setZuruf(document.get("is_zuruf", Boolean.class));
    kommentar.setZurufsFraktion(Arrays.stream(Optional.ofNullable(document.get("zurufs_fraktionen", String.class))
            .map(s -> s.split(",")).orElse(new String[0]))
        .map(Fraktion::valueOf)
        .collect(Collectors.toList()));
    return kommentar;
  }
}