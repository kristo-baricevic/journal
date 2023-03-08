package org.launchcode.journal.models;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Index;
import edu.stanford.nlp.util.PropertiesUtils;
import org.springframework.stereotype.Service;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, List<String>> getNamedEntities(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        Map<String, List<String>> namedEntitiesMap = new HashMap<>();

        for (CoreEntityMention em : document.entityMentions()) {
            String category = em.entityType();
            String entity = em.text();
            namedEntitiesMap.putIfAbsent(category, new ArrayList<>());
            namedEntitiesMap.get(category).add(entity);
        }

        return namedEntitiesMap;
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

    public Map<String, List<String>> getPhrases(String text) {
        Map<String, List<String>> phraseMap = new HashMap<>();
        phraseMap.put("verbPhrases", new ArrayList<>());
        phraseMap.put("nounPhrases", new ArrayList<>());

        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,depparse",
                "depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz",
                "parse.keepPunct", "false");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
            System.out.println(dependencies);
            List<IndexedWord> verbs = dependencies.getAllNodesByPartOfSpeechPattern("VB|VBD|VBG|VBN|VBP|VBZ");
            for (IndexedWord verb : verbs) {
                String verbLemma = verb.get(CoreAnnotations.LemmaAnnotation.class);
                List<SemanticGraphEdge> edges = dependencies.outgoingEdgeList(verb);
                for (SemanticGraphEdge edge : edges) {
                    if (edge.getRelation().getShortName().equals("dobj")) {
                        IndexedWord object = edge.getTarget();
                        List<CoreLabel> tokens = dependencies.getNodeByIndex(object.index()).get(CoreAnnotations.TokensAnnotation.class);
                        String phrase = tokens.stream().map(CoreLabel::word).collect(Collectors.joining(" "));
                        phraseMap.get("verbPhrases").add(verbLemma + " " + phrase);
                    } else if (edge.getRelation().getShortName().equals("nsubjpass")) {
                        IndexedWord subject = edge.getTarget();
                        List<CoreLabel> tokens = dependencies.getNodeByIndex(subject.index()).get(CoreAnnotations.TokensAnnotation.class);
                        String phrase = tokens.stream().map(CoreLabel::word).collect(Collectors.joining(" "));
                        System.out.println(phrase);
                        phraseMap.get("nounPhrases").add("by " + phrase + " " + verbLemma);
                    }
                }
            }
        }
        return phraseMap;
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

