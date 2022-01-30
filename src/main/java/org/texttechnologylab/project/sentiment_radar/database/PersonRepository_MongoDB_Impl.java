package org.texttechnologylab.project.sentiment_radar.database;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.project.sentiment_radar.abstracts.AbstractRepository;
import org.texttechnologylab.project.sentiment_radar.model.Abgeordneter;
import org.texttechnologylab.project.sentiment_radar.model.Fraktion;
import org.texttechnologylab.project.sentiment_radar.model.Person;

import java.util.Optional;

public class PersonRepository_MongoDB_Impl implements AbstractRepository<Person> {

  public Optional<Person> findByGivenId(String givenId) {
    if (givenId == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(getCollection()
            .find(Filters.eq("given_id", givenId))
            .first())
        .map(this::getObject);
  }

  @Override
  public String getCollectionName() {
    return Person.MONGO_DB_COLLECTION_NAME;
  }

  @Override
  public Document createDocument(Person person) {
    Document document = new Document();
    document.append("given_id", person.getGivenId());
    document.append("vorname", person.getVorname());
    document.append("nachname", person.getNachname());
    document.append("namenszusatz", person.getNamenszusatz());
    document.append("ortszusatz", person.getOrtszusatz());
    document.append("rolle_kurz", person.getRolleKurz());
    document.append("rolle_name", person.getRolleName());
    document.append("titel", person.getTitel());
    document.append("bild_url", person.getBildUrl());
    String fraktion = null;
    if (person instanceof Abgeordneter) {
      fraktion = ((Abgeordneter) person).getFraktion().name();
    }
    document.append("fraktion", fraktion);
    return document;
  }

  @Override
  public Person persist(Person person) {
    if (person == null) {
      return null;
    }
    Optional<Person> savedPerson = this.findByGivenId(person.getGivenId());
    if (savedPerson.isPresent()) {
      person.setId(savedPerson.get().getId());
      return person;
    }
    Document document = createDocument(person);
    getCollection().insertOne(document);
    person.setId(document.get("_id", ObjectId.class));
    return person;
  }

  @Override
  public Person getObject(Document document) {
    Person person = new Person();
    if (document.get("fraktion", String.class) != null) {
      person = new Abgeordneter();
      ((Abgeordneter) person).setFraktion(Fraktion.valueOf(document.get("fraktion", String.class)));
    }
    person.setId(document.get("_id", ObjectId.class));
    person.setGivenId(document.get("given_id", String.class));
    person.setVorname(document.get("vorname", String.class));
    person.setNachname(document.get("nachname", String.class));
    person.setNamenszusatz(document.get("namenszusatz", String.class));
    person.setOrtszusatz(document.get("ortszusatz", String.class));
    person.setRolleKurz(document.get("rolle_kurz", String.class));
    person.setRolleName(document.get("rolle_name", String.class));
    person.setTitel(document.get("titel", String.class));
    return person;
  }
}
