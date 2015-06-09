package com.noahhuppert.reflect.messaging.models;

import com.noahhuppert.reflect.messaging.CommunicationType;

import java.util.List;

/**
 * A class for holding information about a conversation
 */
public class ReflectConversation {
    /**
     * The id of the conversation
     */
    private String id;

    /**
     * The protocol used to get the conversation
     */
    private @CommunicationType String protocol;

    /**
     * A snippet of the conversation
     */
    private String snippet;

    /**
     * A list of message ids that belong to the conversation
     */
    private List<String> messageIds;

    /* Actions */
    @Override
    public String toString() {
        return "[" +
                "id: " + getId() +
                ", protocol: " + getProtocol() +
                ", snippet: " + getSnippet() +
                ", messageIds: " + getMessageIds() +
                "]";
    }

    /* Getters */
    public String getId() {
        return id;
    }

    public @CommunicationType String getProtocol() {
        return protocol;
    }

    public String getSnippet() {
        return snippet;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setProtocol(@CommunicationType String protocol) {
        this.protocol = protocol;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }
}
