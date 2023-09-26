package org.launchcode.journal.models;

import java.util.Properties;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class DreamSummarizer {

    private StanfordCoreNLP pipeline;

    public DreamSummarizer() {
        // Initialize Stanford CoreNLP pipeline with the annotator for sentence splitting and part-of-speech tagging.
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        pipeline = new StanfordCoreNLP(props);
    }

    public String summarizeDream(String dreamEntry) {
        // Split the dream entry into paragraphs (assuming paragraphs are separated by line breaks).
        String[] paragraphs = dreamEntry.split("\\n");

        // Initialize an empty summary.
        StringBuilder summary = new StringBuilder();

        // Process each paragraph and extract the first sentence.
        for (String paragraph : paragraphs) {
            // Create a document with the paragraph text.
            Document doc = new Document(paragraph);

            // Get the first sentence of the paragraph.
            Sentence firstSentence = doc.sentences().get(0);

            // Append the first sentence to the summary.
            summary.append(firstSentence.text()).append(" ");
        }

        // Return the summarized dream entry.
        return summary.toString().trim();
    }
}
