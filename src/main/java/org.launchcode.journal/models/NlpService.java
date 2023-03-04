package org.launchcode.journal.models;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class NlpService {

    private StanfordCoreNLP pipeline;

    public NlpService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");

        // Add the MaxentTagger initialization code here
//        String taggerPath = "english-caseless-left3words-distsim.tagger";
//        props.setProperty("pos.model", taggerPath);
//
//        MaxentTagger tagger = new MaxentTagger(taggerPath);
//        props.setProperty("pos.tagger", taggerPath);

        this.pipeline = new StanfordCoreNLP(props);
    }





    public String getSentiment(String text) {
        System.out.println("check check");
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document.sentences().get(0).sentiment();
    }

    public List<CoreEntityMention> getNamedEntities(String text) {
        System.out.println("check check");
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document.entityMentions();
    }

    public List<String> getKeyPhrases(String text) {
        System.out.println("check check");
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        SemanticGraph dependencies = document.sentences().get(0).dependencyParse();
        List<String> nounPhrases = new ArrayList<>();
        for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
            if (edge.getRelation().toString().equals("nn") && edge.getTarget().tag().startsWith("NN")) {
                nounPhrases.add(edge.getTarget().lemma());
            }
        }
        return nounPhrases;
    }

    public List<CoreSentence> getEmotions(String text) {
        System.out.println("check check");
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        List<CoreSentence> sentences = document.sentences();
        List<CoreSentence> emotions = new ArrayList<>();
        for (CoreSentence sentence : sentences) {
            String emotion = sentence.sentiment();
            if (!emotion.equals("Neutral")) {
                emotions.add(sentence);
            }
        }
        return emotions;
    }





}

