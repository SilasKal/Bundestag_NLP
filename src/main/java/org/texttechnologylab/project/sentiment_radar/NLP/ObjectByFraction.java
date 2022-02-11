package org.texttechnologylab.project.sentiment_radar.NLP;

import org.texttechnologylab.project.sentiment_radar.database.PersonRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.database.RedeRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Person;
import org.texttechnologylab.project.sentiment_radar.model.Rede;

import java.util.ArrayList;
import java.util.List;

public class ObjectByFraction {
  public List<String> findSpeechByFraction(String fraction) {
    PersonRepository_MongoDB_Impl personRepository_mongoDB_ = new PersonRepository_MongoDB_Impl();
    List<Person> personen = personRepository_mongoDB_.findPersonByFraction(fraction);
    RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
    List<Rede> reden = new ArrayList<>();
    for (Person person : personen){
      reden.add((Rede) redeRepository_mongoDB_.findByRednerId(person.getId()));
    }
    List<String> redeninhalt = new ArrayList<>();
    for (Rede redei : reden){
      redeninhalt.add(redei.getText());
    }
    return redeninhalt;
  }
}
