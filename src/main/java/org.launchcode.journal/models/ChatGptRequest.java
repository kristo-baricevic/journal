package org.launchcode.journal.models;

import java.util.ArrayList;
import java.util.List;

public class ChatGptRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();

    public ChatGptRequest (String model, String query ) {
        this.model = model;
        this.messages.add(new Message("user", query));
    }

}
