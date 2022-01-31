package org.texttechnologylab.project.sentiment_radar.NLP;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;

import java.util.ArrayList;
import java.util.List;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

public class Nlp {
    public static void NLP(JCas jCas, String speechId, String speakerId, List<String> commentLst) {
        List<String> MiscList = new ArrayList<>();
        List<String> OrgList = new ArrayList<>();
        List<String> PerList = new ArrayList<>();
        List<String> LocList = new ArrayList<>();
        List<Double> SentimentList = new ArrayList<>();
        List<String> TokenList = new ArrayList<>();
        List<String> PosList = new ArrayList<>();
        AggregateBuilder builder = new AggregateBuilder();
        try {
            builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT,
                    "http://spacy.prg2021.texttechnologylab.org"
            ));
            builder.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT,
                    "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION,
                    "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));
            AnalysisEngine pAE = builder.createAggregate();
            SimplePipeline.runPipeline(jCas, pAE);
            for (Token token : JCasUtil.select(jCas, Token.class)) {
                TokenList.add(token.getCoveredText());
                PosList.add(token.getPosValue());
            }
            for (de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity entity :
                    JCasUtil.select(jCas, NamedEntity.class)) {
                if (entity.getValue().equals("MISC")) {
                    MiscList.add(entity.getCoveredText());
                }
                if (entity.getValue().equals("ORG")) {
                    OrgList.add(entity.getCoveredText());
                }
                if (entity.getValue().equals("PER")) {
                    PerList.add(entity.getCoveredText());
                }
                if (entity.getValue().equals("LOC")) {
                    LocList.add(entity.getCoveredText());
                }
            }
            for (Sentence sentence : JCasUtil.select(jCas,
                    de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence.class)) {
                for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                    SentimentList.add(sentiment.getSentiment());
                }
            }
//            double speechSentiment = SentimentList.stream().mapToDouble(val -> val).average().orElse(0.0);
//            List<Double> sentimentCommentList = Nlp.Sentiment(MongoDBConnectionHandler.ListtoListJCas(commentLst));
//            List<Integer> counterList = Nlp.processCommentSentiment(sentimentCommentList);
//            Document document = new Document()
//                    .append("speechId", speechId).append("speakerId", speakerId)
//                    .append("miscList", MiscList).append("orgList", OrgList)
//                    .append("perList", PerList).append("locList", LocList)
//                    .append("tokenList", TokenList).append("posList", PosList)
//                    .append("sentiment", speechSentiment).append("sentimentCommentList", sentimentCommentList)
//                    .append("positiveComments", counterList.get(0))
//                    .append("neutralComments", counterList.get(1))
//                    .append("negativeComments", counterList.get(2));
//            MongoDBConnectionHandler.insertintoCollection("SpeechesInfo", document);


        } catch (ResourceInitializationException | AnalysisEngineProcessException e) {
            e.printStackTrace();
        }
    }
    public static List<Double> Sentiment(List<JCas> jCasList) {
        AggregateBuilder builder = new AggregateBuilder();
        List<Double> currSentimentList = new ArrayList<>();
        List<Double> SentimentList = new ArrayList<>();
        for (JCas jCas : jCasList) {
            try {
                builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                        SpaCyMultiTagger3.PARAM_REST_ENDPOINT,
                        "http://spacy.prg2021.texttechnologylab.org"
                ));
                builder.add(createEngineDescription(GerVaderSentiment.class,
                        GerVaderSentiment.PARAM_REST_ENDPOINT,
                        "http://gervader.prg2021.texttechnologylab.org",
                        GerVaderSentiment.PARAM_SELECTION,
                        "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
                ));
                AnalysisEngine pAE = builder.createAggregate();
                SimplePipeline.runPipeline(jCas, pAE);
                for (Sentence sentence : JCasUtil.select(jCas, de.tudarmstadt.ukp.dkpro.core.
                        api.segmentation.type.Sentence.class)) {
                    for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                        currSentimentList.add(sentiment.getSentiment());
                    }
                }
            } catch (ResourceInitializationException e) {
                e.printStackTrace();
            } catch (AnalysisEngineProcessException e) {
                e.printStackTrace();
            }
            SentimentList.add(currSentimentList.stream().mapToDouble(val -> val).average().orElse(0.0));
        }
        return SentimentList;
    }
}
