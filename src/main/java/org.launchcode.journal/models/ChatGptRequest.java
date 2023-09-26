package org.launchcode.journal.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ChatGptRequest {

    @JsonProperty("model")
    private String model;

    private List<Message> messages = new ArrayList<>();

    public ChatGptRequest (String model, String query ) {
        this.model = model;
        this.messages.add(new Message("user", query));
    }

}
