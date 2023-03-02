package org.launchcode.journal.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="wordcloud")
public class WordCloud extends AbstractEntity {

    private String name;

    @ManyToMany
    private List<Entry> entries = new ArrayList<>();

    public WordCloud() {
    }

    public WordCloud(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void removeEntry(Entry entry) {
        entries.remove(entry);
    }
}
