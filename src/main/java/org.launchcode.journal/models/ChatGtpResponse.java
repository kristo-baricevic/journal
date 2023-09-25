package org.launchcode.journal.models;


import java.util.List;

public class ChatGtpResponse {

    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}

