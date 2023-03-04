package org.launchcode.journal.models;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.HashSet;
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
        List<String> nounPhrases = new ArrayList<>();

        // set up Stanford CoreNLP pipeline with part-of-speech tagging
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // annotate text and iterate over sentences and tokens to extract noun phrases
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (int i = 0; i < tokens.size(); i++) {
                CoreLabel token = tokens.get(i);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (pos.startsWith("N")) { // if token is a noun
                    String nounPhrase = token.word();
                    // check if next token is also a noun and combine if so
                    for (int j = i + 1; j < tokens.size(); j++) {
                        CoreLabel nextToken = tokens.get(j);
                        String nextPos = nextToken.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (nextPos.startsWith("N")) {
                            nounPhrase += " " + nextToken.word();
                            i++;
                        } else {
                            break;
                        }
                    }
                    nounPhrases.add(nounPhrase);
                }
            }
        }
        return nounPhrases;
    }


    public List<SentenceInfo> getEmotions(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        List<CoreSentence> sentences = document.sentences();
        List<SentenceInfo> emotions = new ArrayList<>();
        for (CoreSentence sentence : sentences) {
            String sentiment = sentence.sentiment();
            if (!sentiment.equals("Neutral")) {
                List<CoreEntityMention> entityMentions = sentence.entityMentions();
                SentenceInfo sentenceInfo = new SentenceInfo(sentence.text(), sentence, sentiment, entityMentions);
                emotions.add(sentenceInfo);
            }
        }
        return emotions;
    }





}

