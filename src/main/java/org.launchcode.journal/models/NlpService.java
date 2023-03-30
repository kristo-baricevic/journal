package org.launchcode.journal.models;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.HeadFinder;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
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
import java.util.List;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NlpService {

    private StanfordCoreNLP pipeline;

    public NlpService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");

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

        // set up Stanford CoreNLP pipeline with part-of-speech tagging and constituency parsing
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // annotate text and iterate over sentences and parse trees to extract noun phrases
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            List<Tree> nounPhraseTrees = extractNounPhraseTrees(tree);
            for (Tree nounPhraseTree : nounPhraseTrees) {
                String nounPhrase = nounPhraseTree.getLeaves().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" "));
                nounPhrases.add(nounPhrase);
            }
        }
        return nounPhrases;
    }

    private static List<Tree> extractNounPhraseTrees(Tree tree) {
        List<Tree> nounPhrases = new ArrayList<>();
        if (tree.isLeaf()) {
            return nounPhrases;
        }
        String label = tree.label().value();
        if (label.equals("NP")) {
            nounPhrases.add(tree);
        } else {
            for (Tree child : tree.children()) {
                nounPhrases.addAll(extractNounPhraseTrees(child));
            }
        }
        return nounPhrases;
    }



    private boolean hasNounHead(Tree tree) {
        Tree head = tree.headTerminal((HeadFinder) new PennTreebankLanguagePack());
        String pos = head.label().value();
        return pos.startsWith("NN") || pos.startsWith("PRP");
    }

}

