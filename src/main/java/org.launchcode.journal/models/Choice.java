package org.launchcode.journal.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty("index")
    private int index;
    @JsonProperty("message")
    private Message message;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
