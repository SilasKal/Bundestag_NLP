package org.texttechnologylab.project.sentiment_radar.abstracts;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.database.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T extends MongoDBDocument> {
  String getCollectionName();

  Document createDocument(T person);

  T getObject(Document document);

  default void delete(ObjectId id) {
    if (id != null) {
      getCollection().findOneAndDelete(Filters.eq("_id", id));
    }
  }

  default T findById(ObjectId id) {
    if (id == null) {
      return null;
    }
    return Optional.ofNullable(getCollection().find(Filters.eq("_id", id)).first())
            .map(this::getObject)
            .orElse(null);
  }

  default T persist(T object) {
    if (object == null) {
      return null;
    }
    if (object.getId() != null) {
      throw new IllegalArgumentException(object.getClass().getName() + " wurde schon gespeichert, um eine bereits gespeicherte " + object.getClass().getName() + " zu updaten, benutze bitte update!");
    }
    Document document = createDocument(object);
    getCollection().insertOne(document);
    object.setId(document.get("_id", ObjectId.class));
    return object;
  }
  default void aggregate(List<Document> documentList) {
    getCollection().aggregate(documentList);
  }
  default int count(String collectionname) {
    return getCollectionbyName(collectionname).size();
  }
  default T update(T object) {
    if (object == null) {
      return null;
    }
    if (object.getId() == null) {
      return persist(object);
    }
    try {
      getCollection().findOneAndReplace(Filters.eq("_id", object.getId()), createDocument(object));
      return object;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  default MongoCollection<Document> getCollection() {
    return MongoDBConnectionHandler.getInstance()
            .getCollection(getCollectionName());
  }
  default MongoCollection<Document> getCollectionbyName2(String collectionname) {
    return MongoDBConnectionHandler.getInstance().getCollection(collectionname);
  }

  default List<Document> getCollectionbyName(String collectionname) {
    List<Document> documentList = new ArrayList<>();
    MongoCollection collection = MongoDBConnectionHandler.getInstance().getCollection(collectionname);
    MongoCursor<Document> cursor = collection.find().iterator();
    int counter = 0;
    try {
      while (cursor.hasNext() & counter <=1000) {
        counter += 1;
        documentList.add(cursor.next());
      }
    } finally {
      cursor.close();
    }
    return documentList;
  }

}
