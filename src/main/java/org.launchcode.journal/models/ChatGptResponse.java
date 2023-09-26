package org.launchcode.journal.models;


import java.util.List;

public class ChatGptResponse {

    private List<Choice> choices;

    public ChatGptResponse() {
    }

    public ChatGptResponse(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "ChatGptResponse{" +
                "choices=" + choices +
                '}';
    }
}

