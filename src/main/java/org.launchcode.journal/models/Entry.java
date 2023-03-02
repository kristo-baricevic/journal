package org.launchcode.journal.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="entry")
public class Entry extends AbstractEntity{

    @Size(min=3, max=50)
    private String title;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Size(min=3, max=5000)
    private String journalEntry;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    public Entry() {
    }


    // Initialize the id and value fields.
    public Entry(String aTitle, Topic aTopic, String aJournalEntry, Mood aMood) {
        super();
        this.title = aTitle;
        this.topic = aTopic;
        this.journalEntry = aJournalEntry;
        this.mood = aMood;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(String journalEntry) {
        this.journalEntry = journalEntry;
    }

    @Override
    public String toString() {
        return title;
    }
}