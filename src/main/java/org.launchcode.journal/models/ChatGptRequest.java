package org.launchcode.journal.models;

import org.launchcode.journal.models.Message;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class ChatGptRequest {

    @Value("${openai.api.model}")
    private String model;

    private List<Message> messages = new ArrayList<>();

    public ChatGptRequest(){};

    public ChatGptRequest (String model, String query ) {
        this.model = model;
        System.out.println("query: " + query);
        Message userMessage = new Message("user", query);
        this.messages.add(userMessage);
        System.out.println("Message: " + userMessage);
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

    // Add a method to retrieve the user's prompt
    public String getPrompt() {
        if (messages != null && messages.size() > 0) {
            for (Message message : messages) {
                if ("user".equals(message.getRole())) {
                    return message.getContent();
                }
            }
        }
        return null; // Return null if no user message is found
    }

    @Override
    public String toString() {
        return "ChatGptRequest{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                '}';
    }
}
