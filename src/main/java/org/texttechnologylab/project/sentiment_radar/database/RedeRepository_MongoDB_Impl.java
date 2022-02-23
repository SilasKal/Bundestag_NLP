package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.S;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bytedeco.opencv.presets.opencv_core;
import org.joda.time.format.ISODateTimeFormat;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Person;
import org.texttechnologylab.project.sentiment_radar.model.Rede;
import org.texttechnologylab.project.sentiment_radar.model.Tagesordnungspunkt;

import javax.management.Query;
import javax.print.Doc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RedeRepository_MongoDB_Impl implements AbstractRepository<Rede> {
  /**
   * Operations for speeches
   * @author Silas
   * @param tagesordnungspunktId
   * @return
   */

  public List<Rede> findByTagesordnungspunktId(ObjectId tagesordnungspunktId) {
    List<Rede> reden = new ArrayList<>();
    for (Document document : getCollection().find(Filters.eq("tagesordnungspunkt_id", tagesordnungspunktId))) {
      reden.add(getObject(document));
    }
    return reden;
  }
  /**
   * @author Silas
   * returns list of speeches that match given rednerId
   */
  public List<Document> findByRednerId(ObjectId rednerId) {
    List<Document> reden = new ArrayList<>();
    for (Document document:getCollectionbyName2("Reden").find(Filters.eq("redner_id", rednerId))) {
      reden.add(document);
    }
    return reden;
  }
  /**
   * @author Silas
   * returns count of speeches that match given rednerId
   */
  public Integer findByRednerIdCount(ObjectId rednerId) {
    int counter = 0;
    for (Document document : getCollectionbyName2("Reden").find(Filters.eq("redner_id", rednerId))) {
      counter += 1;
    }
    return counter;
  }
  /**
   * @author Silas
   * returns all speeches currently in database
   */
  public List<Document> findallRede() {
    List<Document> redeList = getCollectionbyName("Reden");
    return redeList;
  }
  public int getRedesize() {
    return getCollectionSizebyName("Reden");
  }
  /**
   * @author Silas
   * returns list of speeches that match given redeIds
   */
  public List<Document> findByRedeIdList(List<ObjectId> redeIdList) {
    List<Document> reden = new ArrayList<>();
    for (Document document : getCollectionbyName2("Reden").find(Filters.in("_id", redeIdList))) {
      reden.add(document);
    }
    return reden;
  }
  /**
   * @author Silas
   * returns speech that matches given redeId
   */
  public List<Document> findByRedeId(ObjectId redeId) {
    List<Document> reden = new ArrayList<>();
    for (Document document : getCollectionbyName2("Reden").find(Filters.eq("_id", redeId))) {
      reden.add(document);
    }
    return reden;
  }
  public void updateRedeDouble(ObjectId redeId, String newValueName, double newValue) {
    getCollection().updateOne(Filters.eq("_id", redeId), new Document("$set", new Document(newValueName, newValue)));
  }
  public void updateRedeList(ObjectId redeId, String newValueName, List<String> newList) {
    getCollection().updateOne(Filters.eq("_id", redeId), new Document("$set", new Document(newValueName, newList)));
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
    document.append("datum", rede.getDatum());
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