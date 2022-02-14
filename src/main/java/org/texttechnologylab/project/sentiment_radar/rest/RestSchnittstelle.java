package org.texttechnologylab.project.sentiment_radar.rest;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.texttechnologylab.project.sentiment_radar.database.PersonRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.database.RedeRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.menu.RednerMenu_MongoDBImpl_File_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Person;

import java.util.*;

import static spark.Spark.*;

/**
 * This class provides the implementation of the REST port,
 * it processes NLP data in the database to return relevant
 * information
 * @author Silas
 * @see org.texttechnologylab.project.sentiment_radar.database.MongoDBConnectionHandler
 */
public class RestSchnittstelle {
    /**
     * @author Silas
     * TODO java doc
     */
    public static void main(String[] args) {
        get("/speeches/count", (req, res) -> getSpeechesCount(""));
        get("/token/:fraktion", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllTokenwithCount("");
            }
            return getAllTokenwithCount(req.params(":fraktion"));
        });
        get("/token/:fraktion/:startdate/:enddate", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllTokenwithCount("");
            }
            return getAllTokenwithCount(req.params("fraktion"));
        });
        get("/ne/:fraktion", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllNEwithCount("");
            }
            return getAllNEwithCount(req.params(":fraktion"));
        });
        get("/ne/:fraktion/:startdate/:enddate", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllNEwithCount("");
            }
            return getAllNEwithCount(req.params(":fraktion"));
        });
        get("/sentiment/:fraktion", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllSentimentwithCount("");
            }
            return getAllSentimentwithCount(req.params(":fraktion"));
        });
        get("/sentiment/:fraktion/:startdate/:enddate", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllSentimentwithCount("");
            }
            return getAllSentimentwithCount(req.params(":fraktion"));
        });
        get("/pos/:fraktion", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return getAllPoswithCount("");
            }
            return getAllPoswithCount(req.params(":fraktion"));
        });
        get("/pos/:fraktion/:startdate/:enddate", (req, res) ->  {
            System.out.println(req.params("startdate") + req.params("enddate"));
            return getAllPoswithCount(req.params("fraktion"));
        });
        get("/speaker/:fraktion", (req, res) ->  {
            if (req.params(":fraktion").equals("all")) {
                return findSpeakerbyFraction("");
            }
            return findSpeakerbyFraction(req.params(":fraktion"));
        });
        enableCORS("*","*","*");  // enables hosting server and making client requests
    }
    /**
     * @author Silas
     * calculates speech count for speakers in a given fraction
     */
    public static JSONObject findSpeakerbyFraction(String fraktion) {
        if (fraktion.equals("")) {
            PersonRepository_MongoDB_Impl personRepository_mongoDB_ = new PersonRepository_MongoDB_Impl();
            List<Document> personen = personRepository_mongoDB_.findAllPersonDocument();
            JSONObject curr_Json = new JSONObject();
            List<JSONObject> JsonList = new ArrayList<>();
            JSONObject finalJson = new JSONObject();
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            for (Document document:personen) {
                try {
                    curr_Json = new JSONObject();
                    curr_Json.put("name", document.getString("vorname") + " " + document.getString("nachname"));
                    curr_Json.put("url", document.getList("url", String.class).get(0));
                    curr_Json.put("count", redeRepository_mongoDB_.findByRednerIdCount(document.getObjectId("_id")));
                    JsonList.add(curr_Json);
                } catch (NullPointerException e) {
                    curr_Json = new JSONObject();
                    curr_Json.put("name", document.getString("vorname") + " " + document.getString("nachname"));
                    curr_Json.put("url", "no_picture");
                    curr_Json.put("count", redeRepository_mongoDB_.findByRednerIdCount(document.getObjectId("_id")));
                    JsonList.add(curr_Json);
                }
            }
            finalJson.put("result", JsonList);
            return finalJson;
        }else {
            System.out.println("Die fraktion "+ fraktion + "wird gesucht");
            PersonRepository_MongoDB_Impl personRepository_mongoDB_ = new PersonRepository_MongoDB_Impl();
            List<Document> personen = personRepository_mongoDB_.findPersonByFractionDocument(fraktion);
            JSONObject curr_Json = new JSONObject();
            List<JSONObject> JsonList = new ArrayList<>();
            JSONObject finalJson = new JSONObject();
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            for (Document document:personen) {
                try {
                    curr_Json = new JSONObject();
                    curr_Json.put("name", document.getString("vorname") + " " + document.getString("nachname"));
                    curr_Json.put("url", document.getList("url", String.class).get(0));
                    curr_Json.put("count", redeRepository_mongoDB_.findByRednerIdCount(document.getObjectId("_id")));
                    JsonList.add(curr_Json);
                }catch (NullPointerException e){
                    curr_Json = new JSONObject();
                    curr_Json.put("name", document.getString("vorname") + " " + document.getString("nachname"));
                    curr_Json.put("url", "no_picture");
                    curr_Json.put("count", redeRepository_mongoDB_.findByRednerIdCount(document.getObjectId("_id")));
                    JsonList.add(curr_Json);
                }
            }
            finalJson.put("result", JsonList);
            return finalJson;
        }
    }
    /**
     * @author Silas
     * returns list of all speeches for given fraction
     */
    public static List<Document> findSpeechByFraction(String fraktion) {
        if (fraktion.equals("")) {
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List <Document> redenList = redeRepository_mongoDB_.getCollectionbyName("Reden");
            System.out.println("fertig mit redelist");
            return redenList;
        }else {
            System.out.println("Die fraktion "+ fraktion + "wird gesucht");
            PersonRepository_MongoDB_Impl personRepository_mongoDB_ = new PersonRepository_MongoDB_Impl();
            List<Person> personen = personRepository_mongoDB_.findPersonByFraction(fraktion);
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> reden = new ArrayList<>();
            for (Person person : personen){
                reden.addAll(redeRepository_mongoDB_.findByRednerId(person.getId()));
            }
            List<ObjectId> redenids = new ArrayList<>();
            for (Document redei : reden){
                redenids.add(redei.getObjectId("_id"));
            }
            System.out.println("fertig mit Filtern");
            return redeRepository_mongoDB_.findByRedeIdList(redenids);
        }
    }
    /**
     * @author Silas
     * returns JSON with count speeches currently in database
     */
    public static JSONObject getSpeechesCount(String fraktion) {
        List<Document> RedeList = findSpeechByFraction(fraktion);
        JSONObject JSONfinal = new JSONObject();
        JSONfinal.put("count", RedeList.size());
        return JSONfinal;
    }
    /**
     * @author Silas
     * iterates over all speeches of a given fraction and
     * passes all the tokens to processToken
     * @see RestSchnittstelle#processToken
     */
    public static JSONObject getAllTokenwithCount(String fraktion) {
        List<Document> RedeList = findSpeechByFraction(fraktion);
        List<String> TokenList = new ArrayList<>();
        int counter = 0;
        for (Document document: RedeList) {
            counter +=1;
            System.out.println(counter);
            try {
                TokenList.addAll(document.getList("tokenList", String.class));
            }catch (Exception e) {
                //System.out.println("no TokenList");
            }
        }
        System.out.println("finished List");
        return processToken(TokenList);
    }
    /**
     * @author Silas
     * calculates the count of every element in tokenList
     * and returns a JSON with the calculated information
     */
    public static JSONObject processToken(List<String> tokenList) {
        System.out.println("process" + tokenList.size());
        ArrayList<JSONObject> TokenJsonList = new ArrayList<>();
        JSONObject TokenJsonFinal = new JSONObject();
        HashMap<String, Integer> tokenMap = new HashMap<>();
        JSONObject currJson = new JSONObject();
        int counter = 0;
        for (String string:tokenList) {
            counter +=1;
            System.out.println(counter);
            if (!tokenMap.containsKey(string)) {
                tokenMap.put(string, 1);
            } else {
                tokenMap.replace(string, tokenMap.get(string) + 1);
            }
        }
        Iterator it = tokenMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            currJson = new JSONObject();
            currJson.put("token", pair.getKey());
            currJson.put("count", pair.getValue());
            TokenJsonList.add(currJson);
            it.remove();
        }
        TokenJsonFinal.put("result", TokenJsonList);
        return TokenJsonFinal;

    }
    /**
     * @author Silas
     * iterates over all speeches of a given fraction and
     * passes all the namedentities to processNE
     * @see RestSchnittstelle#processNE
     */
    public static JSONObject getAllNEwithCount(String fraktion) {
        List<Document> RedeList = findSpeechByFraction(fraktion);
        List<String> miscList = new ArrayList<>();
        List<String> orgList = new ArrayList<>();
        List<String> perList = new ArrayList<>();
        List<String> locList = new ArrayList<>();
        for (Document document: RedeList) {
            try {
                miscList.addAll(document.getList("miscList", String.class));
                orgList.addAll(document.getList("orgList", String.class));
                perList.addAll(document.getList("perList", String.class));
                locList.addAll(document.getList("locList", String.class));
            }catch (Exception e) {
                //System.out.println("no NLP data");
            }
        }
        return processNE(miscList, orgList, perList, locList);
    }
    /**
     * @author Silas
     * calculates the count of every element in list
     * and returns a JSON with the calculated information
     */
    public static JSONObject processNEList(List<String> list, String listname) {
        HashMap<String, Integer> NEMap = new HashMap<>();
        JSONObject currJson = new JSONObject();
        JSONObject finalJSON = new JSONObject();
        List<JSONObject> NeJSONList = new ArrayList<>();
        for (String string : list) {
            if (!NEMap.containsKey(string)) {
                NEMap.put(string, 1);
            } else {
                NEMap.replace(string, NEMap.get(string) + 1);
            }
        }
        Iterator it = NEMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            currJson = new JSONObject();
            currJson.put("element", pair.getKey());
            currJson.put("count", pair.getValue());
            NeJSONList.add(currJson);
            it.remove();
        }
        finalJSON.put(listname,NeJSONList);
        return finalJSON;
    }
    /**
     * @author Silas
     * merges all JSONs with the token count to one JSON containing
     * all relevant information
     */
    public static JSONObject processNE(List<String> miscList, List<String> orgList, List<String> perList, List<String> locList) {
        List<List<String>> entityList = new ArrayList<>();
        JSONObject currJson = new JSONObject();
        ArrayList<JSONObject> NEjson = new ArrayList<>();
        JSONObject NEJsonList = new JSONObject();
        entityList.add(miscList);
        entityList.add(orgList);
        entityList.add(perList);
        entityList.add(locList);
        entityList.sort((Comparator<List>) (a1, a2) -> a2.size() - a1.size());
        int counter2 = 0;
        for (List<String> list : entityList) {
            if (counter2 == 0) {
                NEjson.add(processNEList(miscList, "MISC"));
            }
            if (counter2 == 1) {
                NEjson.add(processNEList(orgList, "ORG"));
            }
            if (counter2 == 2) {
                NEjson.add(processNEList(perList, "PER"));
            }
            if (counter2 == 3) {
                NEjson.add(processNEList(locList, "LOC"));
            }
            counter2 += 1;
        }
        NEJsonList.put("result", NEjson);
        return NEJsonList;
    }
    /**
     * @author Silas
     * iterates over all speeches of a given fraction and
     * passes all the sentiments to processSentiment
     * @see RestSchnittstelle#processSentiment
     */
    public static JSONObject getAllSentimentwithCount(String fraktion) {
        List<Document> RedeList = findSpeechByFraction(fraktion);
        List<Double> SentimentList = new ArrayList<>();
        for (Document document: RedeList) {
            try {
                if (!document.getDouble("sentiment").equals(null)) {
                    SentimentList.add(document.getDouble("sentiment"));
                }
            }catch (Exception e) {
                //System.out.println("no NLP data");
            }
        }
        return processSentiment(SentimentList);
    }
    /**
     * @author Silas
     * calculates the count of every element in SentimentList
     * and returns a JSON with the calculated information
     */
    public static JSONObject processSentiment(List<Double> SentimentList) {
        HashMap<String, Integer> sentimentMap = new HashMap<>();
        List<JSONObject> SentimentJsonList = new ArrayList<>();
        JSONObject currJson = new JSONObject();
        JSONObject finalJSON = new JSONObject();
        for (Double sentiment : SentimentList) {
            if (!sentimentMap.containsKey(sentiment.toString())) {
                sentimentMap.put(sentiment.toString(), 1);
            } else {
                sentimentMap.replace(sentiment.toString(), sentimentMap.get(sentiment.toString()) + 1);
            }
        }
        Iterator it = sentimentMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            currJson = new JSONObject();
            currJson.put("sentiment", pair.getKey());
            currJson.put("count", pair.getValue());
            SentimentJsonList.add(currJson);
            it.remove();
        }
        finalJSON.put("result", SentimentJsonList);
        return finalJSON;
    }
    /**
     * @author Silas
     * iterates over all speeches of a given fraction and
     * passes all the pos to processPos
     * @see RestSchnittstelle#processPos
     */
    public static JSONObject getAllPoswithCount(String fraktion) {
        List<Document> RedeList = findSpeechByFraction(fraktion);
        List<String> PosList = new ArrayList<>();
        for (Document document: RedeList) {
            try {
                PosList.addAll(document.getList("posList", String.class));
            }catch (Exception e) {
                //System.out.println("no posList");
            }
        }
        return processPos(PosList);
    }
    /**
     * @author Silas
     * calculates the count of every element in poslist
     * and returns a JSON with the calculated information
     */
    public static JSONObject processPos(List<String> PosList) {
        HashMap<String, Integer> posMap = new HashMap<>();
        List<String> JsonPos = new ArrayList<>();
        JSONObject currJson = new JSONObject();
        JSONObject finalJSON = new JSONObject();
        List<JSONObject> POSJSONList = new ArrayList<>();
        for (String string : PosList) {
            if (!posMap.containsKey(string)) {
                posMap.put(string, 1);
            } else {
                posMap.replace(string, posMap.get(string) + 1);
            }
        }
        Iterator it = posMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            currJson = new JSONObject();
            currJson.put("pos", pair.getKey());
            currJson.put("count", pair.getValue());
            POSJSONList.add(currJson);
            it.remove();
        }
        finalJSON.put("result", POSJSONList);
        return finalJSON;
    }
    /**
     * @author Ben
     * TODO java doc
     */
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }


}

