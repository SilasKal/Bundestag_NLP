package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.texttechnologylab.project.sentiment_radar.util.PropertyUtil;

public class MongoDBConnectionHandler {
  private MongoDatabase db;

  private static MongoDBConnectionHandler instance;

  public static MongoDBConnectionHandler getInstance() {  //singleton pattern
    if(MongoDBConnectionHandler.instance == null) {
      MongoDBConnectionHandler.instance = new MongoDBConnectionHandler();
    }
    return MongoDBConnectionHandler.instance;
  }

  private MongoDBConnectionHandler() {
    String host = PropertyUtil.getRemoteHost();
    String database = PropertyUtil.getRemoteDatabase();
    String user = PropertyUtil.getRemoteUser();
    String password = PropertyUtil.getRemotePassword();
    String port = PropertyUtil.getRemotePort();

    String uri = "mongodb://" + database + ":" + password + "@" + host + ":" + port + "/" + database + "?authSource=" + user;
    System.out.println(uri);
    try {
      MongoClient client = MongoClients.create(uri);
      this.db = client.getDatabase(user);
      System.out.println("connection successful");
    } catch (Exception e) {
      System.out.println("connection error");
    }
  }

  public MongoCollection<Document> getCollection(String collection) {
    return this.db.getCollection(collection);
  }
}