package org.launchcode.journal.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Choice {

    private int index;
    private Message message;

    public Choice() {
    }

    public Choice(int index, Message message) {
        this.index = index;
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "Choice{" +
                "message=" + message +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice choice = (Choice) o;
        return index == choice.index && Objects.equals(message, choice.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, message);
    }
}
