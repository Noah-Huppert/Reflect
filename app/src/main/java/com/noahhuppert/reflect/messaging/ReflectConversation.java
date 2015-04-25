package com.noahhuppert.reflect.messaging;

import java.net.URI;
import java.util.List;

/**
 * A class for holding information about a conversation
 */
public class ReflectConversation {
    /**
     * The uri of the conversation using the "messaging" scheme
     */
    private String id;

    /**
     * A list of message uris using the "messaging" scheme
     */
    public List<URI> messageUris;

    /* Getters */
    public String getId() {
        return id;
    }

    public List<URI> getMessageUris() {
        return messageUris;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setMessageUris(List<URI> messageUris) {
        this.messageUris = messageUris;
    }
}
