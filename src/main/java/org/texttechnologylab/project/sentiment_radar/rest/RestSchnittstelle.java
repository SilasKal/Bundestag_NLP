package org.texttechnologylab.project.sentiment_radar.rest;
import net.bytebuddy.description.ByteCodeElement;
import org.bson.Document;
import org.elasticsearch.client.license.LicensesStatus;
import org.json.simple.JSONObject;
import org.texttechnologylab.project.sentiment_radar.database.RedeRepository_MongoDB_Impl;

import javax.json.Json;
import javax.print.Doc;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static spark.Spark.*;



public class RestSchnittstelle {
    public static void main(String[] args) {
        get("/token/all", (req, res) -> {
            return getAllTokenwithCount();
        });
        get("/speeches/all", (req, res) -> {
            return getAllSpeeches();
        });
        get("/ne/all", (req, res) -> {
            return getAllNEwithCount();
        });
        get("/sentiment/all", (req, res) -> {
            return getAllSentimentwithCount();
        });
        get("/pos/all", (req, res) -> {
            return getAllPoswithCount();
        });

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
    public static List<String> getAllTokenwithCount() {
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
        return processToken(TokenList);
    }
    public static List<String> processToken(List<String> tokenList) {
        HashMap<String, Integer> tokenMap = new HashMap<>();
        List<Document> tokenCount = new ArrayList<>();
        List<String> JsonToken = new ArrayList<>();
        for (String string:tokenList) {
            if (!tokenMap.containsKey(string)) {
                tokenMap.put(string, 1);
            } else {
                tokenMap.replace(string, tokenMap.get(string) + 1);
            }
        }
        tokenMap.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> tokenCount.add(new Document().append("token", k.getKey()).append("count" , k.getValue())));
        for (int i = 0; i < tokenCount.size(); i++) {
            JsonToken.add(tokenCount.get(i).toJson());
        };
        return JsonToken;

    }
    public static List<String> getAllNEwithCount() {
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
    public static List<String> processNE(List<String> miscList, List<String> orgList, List<String> perList, List<String> locList) {
        List<List<String>> entityList = new ArrayList<>();
        List<String> NEJson = new ArrayList<>();
        entityList.add(miscList);
        entityList.add(orgList);
        entityList.add(perList);
        entityList.add(locList);
        Collections.sort(entityList, new Comparator<List>() {
            public int compare(List a1, List a2) {
                return a2.size() - a1.size();
            }
        });
        int counter2 = 0;
        for (List<String> list : entityList) {
            if (counter2 == 0) {
                String Json = new Document().append("type", "MISC").append("count", list.size()).toJson();
                NEJson.add(Json);
                //System.out.println("MISC mit " + list.size() + " Elementen. ");
            }
            if (counter2 == 1) {
                String Json = new Document().append("type", "ORG").append("count", list.size()).toJson();
                NEJson.add(Json);
                //System.out.println("ORG mit " + list.size() + " Elementen. ");
            }
            if (counter2 == 2) {
                String Json = new Document().append("type", "PER").append("count", list.size()).toJson();
                NEJson.add(Json);
                //System.out.println("PER mit " + list.size() + " Elementen. ");
            }
            if (counter2 == 3) {
                String Json = new Document().append("type", "LOC").append("count", list.size()).toJson();
                NEJson.add(Json);
                //System.out.println("LOC mit " + list.size() + " Elementen. ");
            }
            counter2 += 1;
        }
        return NEJson;


    }
    public static List<String> getAllSentimentwithCount() {
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
    public static List<String> processSentiment(List<Double> SentimentList) {
        HashMap<String, Integer> sentimentMap = new HashMap<>();
        List<Document> SentimentCount = new ArrayList<>();
        List<String> SentimentJson = new ArrayList<>();
        System.out.println(SentimentList);
        for (Double sentiment : SentimentList) {
            if (!sentimentMap.containsKey(sentiment.toString())) {
                sentimentMap.put(sentiment.toString(), 1);
            } else {
                sentimentMap.replace(sentiment.toString(), sentimentMap.get(sentiment.toString()) + 1);
            }
        }
        sentimentMap.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> SentimentCount.add(new Document().append("sentiment", k.getKey()).append("count", k.getValue())));
        for (int i = 0; i < SentimentCount.size(); i++) {
            SentimentJson.add(SentimentCount.get(i).toJson());
        }
        return SentimentJson;
    }
    public static List<String> getAllPoswithCount() {
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
    public static List<String> processPos(List<String> PosList) {
        HashMap<String, Integer> tokenMap = new HashMap<>();
        List<Document> Poscount = new ArrayList<>();
        List<String> JsonPos = new ArrayList<>();
        for (String string : PosList) {
            if (!tokenMap.containsKey(string)) {
                tokenMap.put(string, 1);
            } else {
                tokenMap.replace(string, tokenMap.get(string) + 1);
            }
        }
        tokenMap.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> Poscount.add(new Document().append("pos", k.getKey()).append("count", k.getValue())));
        for (int i = 0; i < Poscount.size(); i++) {
            JsonPos.add(Poscount.get(i).toJson());
        }
        ;
        return JsonPos;
    }

}

