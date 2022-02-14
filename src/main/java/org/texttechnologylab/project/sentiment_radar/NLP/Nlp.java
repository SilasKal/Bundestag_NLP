package org.texttechnologylab.project.sentiment_radar.NLP;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;
import org.texttechnologylab.project.sentiment_radar.database.RedeRepository_MongoDB_Impl;

import java.util.ArrayList;
import java.util.List;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * This class provides the implementation of the NLP and inserts the analysed data into the database
 * @author Silas
 * @see org.texttechnologylab.project.sentiment_radar.database.MongoDBConnectionHandler
 */
public class Nlp {
    /**
     * @author Silas
     * gives needed information to NLP function to analyse the given speech
     */
    public static void main(String[] args) {
        NLPprocess();
    }
    public static void NLPprocess() {
        RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
        List <Document> documentList = redeRepository_mongoDB_.findallRede();
        int counter = 0;
        for (Document document: documentList) {
            System.out.println(counter);
            NLP(RedetoJCas(document), document.getObjectId("_id"));
            counter +=1;
//            if (counter >= 5000) {
//                System.out.println(counter);
//                NLP(RedetoJCas(document), document.getObjectId("_id"));
//                counter +=1;
//            }
//            System.out.println(counter);
//            counter +=1;
        }
    }
    /**
     * @author Silas
     * converts given document into JCas with German as language
     * @param doc document which should be converted
     */
    public static JCas RedetoJCas(Document doc) {
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(doc.getString("text"), "de");
        } catch (UIMAException e) {
            e.printStackTrace();
        }
        return jCas;
    }
    /**
     * @author Silas
     * analyses given JCas document
     * inspired by slides from Giuseppe Abrami
     */
    public static void NLP(JCas jCas, ObjectId objectId) {
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
            double speechSentiment = SentimentList.stream().mapToDouble(val -> val).average().orElse(0.0);
            RedeRepository_MongoDB_Impl redeRepository_mongoDB_ = new RedeRepository_MongoDB_Impl();
            redeRepository_mongoDB_.updateRedeDouble(objectId, "sentiment", speechSentiment);
            redeRepository_mongoDB_.updateRedeList(objectId, "miscList", MiscList);
            redeRepository_mongoDB_.updateRedeList(objectId, "orgList", OrgList);
            redeRepository_mongoDB_.updateRedeList(objectId, "perList", PerList);
            redeRepository_mongoDB_.updateRedeList(objectId, "locList", LocList);
            redeRepository_mongoDB_.updateRedeList(objectId, "tokenList", TokenList);
            redeRepository_mongoDB_.updateRedeList(objectId, "posList", PosList);

        } catch (ResourceInitializationException | AnalysisEngineProcessException e) {
            e.printStackTrace();
        }
    }
}
