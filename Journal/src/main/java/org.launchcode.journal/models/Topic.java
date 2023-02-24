package org.launchcode.journal.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="topic")
public class Topic extends AbstractEntity{

    @NotNull
    @Size(min=3, max=50)
    private String name;

    @OneToMany(mappedBy = "topic")
    private List<Entry> entries;

    public Topic() {
    }

    // Initialize the id and value fields.
    public Topic(String aName) {
        super();
        this.name = aName;
    }

    // Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
