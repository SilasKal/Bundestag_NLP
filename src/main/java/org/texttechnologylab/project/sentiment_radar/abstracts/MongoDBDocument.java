package org.texttechnologylab.project.sentiment_radar.abstracts;

import org.bson.types.ObjectId;

public abstract class MongoDBDocument {
  private ObjectId id;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }
}
