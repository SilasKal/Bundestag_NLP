package org.texttechnologylab.project.sentiment_radar.rest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.texttechnologylab.project.sentiment_radar.database.PersonRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.database.RedeRepository_MongoDB_Impl;
import org.texttechnologylab.project.sentiment_radar.menu.RednerMenu_MongoDBImpl_File_Impl;
import org.texttechnologylab.project.sentiment_radar.model.Person;

import java.util.*;

import static spark.Spark.*;



public class RestSchnittstelle {
    public static void main(String[] args) {
        get("/token/all", (req, res) -> getAllTokenwithCount(""));
        get("/speeches/all", (req, res) -> getAllSpeeches());
        get("/ne/all", (req, res) -> getAllNEwithCount(""));
        get("/sentiment/all", (req, res) -> getAllSentimentwithCount(""));
        get("/pos/all", (req, res) -> getAllPoswithCount(""));
        get("/speeches/count", (req, res) -> getSpeechesCount(""));
        get("/token/:fraktion", (req, res) ->  {
            return getAllTokenwithCount(req.params(":fraktion"));
        });
        get("/ne/:fraktion", (req, res) ->  {
            return getAllNEwithCount(req.params(":fraktion"));
        });
        get("/sentiment/:fraktion", (req, res) ->  {
            return getAllSentimentwithCount(req.params(":fraktion"));
        });
        get("/pos/:fraktion", (req, res) ->  {
            return getAllPoswithCount(req.params(":fraktion"));
        });
        enableCORS("*","*","*");  // enables hosting server and making client requests
    }
    public static List<Document> findSpeechByFraction(String fraktion) {
        if (fraktion.equals("")) {
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List <Document> redenList = redeRepository_mongoDB_.getCollectionbyName("Reden");
            return redenList;
        }else {
            System.out.println("Die fraktion "+ fraktion + "wird gesucht");
            PersonRepository_MongoDB_Impl personRepository_mongoDB_ = new PersonRepository_MongoDB_Impl();
            List<Person> personen = personRepository_mongoDB_.findPersonByFraction(fraktion);
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List <Document> filteredReden = new ArrayList<>();
            List<Document> reden = new ArrayList<>();
            for (Person person : personen){
                reden.addAll(redeRepository_mongoDB_.findByRednerId(person.getId()));
            }
            List<ObjectId> redenids = new ArrayList<>();
            for (Document redei : reden){
                redenids.add(redei.getObjectId("_id"));
            }
            for (ObjectId redenid:redenids) {
                filteredReden.addAll(redeRepository_mongoDB_.findByRedeId(redenid));
            }
            System.out.println("fertig mit Filtern");
            return filteredReden;
        }
    }
    public static List<String> getAllSpeeches() {
        RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
        List<Document> RedeList = redeRepository_mongoDB_.findallRede();
        List<String> JsonList = new ArrayList<>();
        for (Document document : RedeList) {
            JsonList.add(document.toJson());
        }
        return JsonList;
    }
    public static JSONObject getSpeechesCount(String fraktion) {
        if (!fraktion.equals("")) {
            List<Document> RedeList = findSpeechByFraction(fraktion);
            JSONObject JSONfinal = new JSONObject();
            JSONfinal.put("count", RedeList.size());
            return JSONfinal;
        }
        else{
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> RedeList = redeRepository_mongoDB_.findallRede();
            JSONObject JSONfinal = new JSONObject();
            JSONfinal.put("count", RedeList.size());
            return JSONfinal;
        }
    }
    public static JSONObject getAllTokenwithCount(String fraktion) {
        if (!fraktion.equals("")) {
            List<Document> RedeList = findSpeechByFraction(fraktion);
            List<String> TokenList = new ArrayList<>();
            for (Document document: RedeList) {
                try {
                    TokenList.addAll(document.getList("tokenList", String.class));
                }catch (Exception e) {
                    //System.out.println("no TokenList");
                }
            }
            System.out.println("finished List");
            return processToken(TokenList);
        }
        else{
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> RedeList = redeRepository_mongoDB_.findallRede();
            List<String> TokenList = new ArrayList<>();
            for (Document document: RedeList) {
                try {
                    TokenList.addAll(document.getList("tokenList", String.class));
                }catch (Exception e) {
                    //System.out.println("no TokenList");
                }
            }
            System.out.println("finished List");
            return processToken(TokenList);
        }
    }
    public static JSONObject processToken(List<String> tokenList) {
        ArrayList<JSONObject> TokenJsonList = new ArrayList<>();
        JSONObject TokenJsonFinal = new JSONObject();
        HashMap<String, Integer> tokenMap = new HashMap<>();
        JSONObject currJson = new JSONObject();
        for (String string:tokenList) {
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
    public static JSONObject getAllNEwithCount(String fraktion) {
        if (!fraktion.equals("")) {
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
        else{
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> RedeList = redeRepository_mongoDB_.findallRede();
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
    }
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
                currJson.put("type", "MISC");
                currJson.put("count", list.size());
                NEjson.add(currJson);

            }
            if (counter2 == 1) {
                currJson = new JSONObject();
                currJson.put("type", "ORG");
                currJson.put("count", list.size());
                NEjson.add(currJson);
            }
            if (counter2 == 2) {
                currJson = new JSONObject();
                currJson.put("type", "PER");
                currJson.put("count", list.size());
                NEjson.add(currJson);
            }
            if (counter2 == 3) {
                currJson = new JSONObject();
                currJson.put("type", "LOC");
                currJson.put("count", list.size());
                NEjson.add(currJson);
            }
            counter2 += 1;
        }
        NEJsonList.put("result", NEjson);
        return NEJsonList;


    }
    public static JSONObject getAllSentimentwithCount(String fraktion) {
        if (!fraktion.equals("")) {
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
        else{
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> RedeList = redeRepository_mongoDB_.findallRede();
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
    }
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
    public static JSONObject getAllPoswithCount(String fraktion) {
        if (!fraktion.equals("")) {
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
        else{
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            List<Document> RedeList = redeRepository_mongoDB_.findallRede();
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
    }
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

