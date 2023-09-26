package org.launchcode.journal.models;

import org.launchcode.journal.models.Message;
import java.util.ArrayList;
import java.util.List;

public class ChatGptRequest {

    private String model;

    private List<Message> messages = new ArrayList<>();

    public ChatGptRequest(){};

    public ChatGptRequest (String model, String query ) {
        this.model = model;
        this.messages.add(new Message("user", query));
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatGptRequest{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                '}';
    }
}
