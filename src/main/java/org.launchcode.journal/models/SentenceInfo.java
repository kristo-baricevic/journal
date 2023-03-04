package org.launchcode.journal.models;

import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

import java.util.List;

public class SentenceInfo {
    private String text;
    private final CoreSentence sentence;
    private final String sentiment;
    private final List<CoreEntityMention> entityMentions;

    public SentenceInfo(String text, CoreSentence sentence, String sentiment, List<CoreEntityMention> entityMentions) {
        this.text = text;
        this.sentence = sentence;
        this.sentiment = sentiment;
        this.entityMentions = entityMentions;
    }

    public String getText() {
        return text;
    }

    public CoreSentence getSentence() {
        return sentence;
    }

    public String getSentiment() {
        return sentiment;
    }

    public List<CoreEntityMention> getEntityMentions() {
        return entityMentions;
    }
}

