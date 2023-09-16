package org.launchcode.journal.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="mood")
public class Mood extends AbstractEntity{

    @NotNull
    @Size(min=3, max=50)
    private String name;


    @Size(min=3, max=200)
    private String meaning;

    @OneToMany(mappedBy = "mood")
    private List<Entry> entries;

    public Mood() {
    }

    // Initialize the id and value fields.
    public Mood(String aMood) {
        super();
        this.name = aMood;
    }

    // Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
