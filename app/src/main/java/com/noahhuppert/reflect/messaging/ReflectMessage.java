package com.noahhuppert.reflect.messaging;

import java.net.URI;
import java.sql.Timestamp;

/**
 * A class holding the information of a general message in Reflect. Every messaging provider must
 * provide this information.
 */
public class ReflectMessage {
    /**
     * The Id of the message
     */
    private String id;

    /**
     * The protocol used to send the message
     */
    private CommunicationType protocol;

    /**
     * The uri of the person who received the message using the "messaging" scheme or the "jabber"
     * scheme
     */

    private URI receiverUri;

    /**
     * The uri of the person who sent the message using the "messaging" scheme or the "jabber"
     * scheme
     */
    private URI senderUri;

    /**
     * Content of the message
     */
    private String body;

    /**
     * The Date and Time the message message was sent
     */
    private Timestamp sentTimestamp;

    /**
     * The Date and Time the message was received
     */
    private Timestamp receivedTimestamp;

    /**
     * Has the message been read by the receiver
     */
    private boolean read;

    /**
     * Has the user seen the message
     *
     * Used to determine if a notification should be shown
     */
    private boolean seen;

    /* Getters */
    public String getId() {
        return id;
    }

    public CommunicationType getProtocol() {
        return protocol;
    }

    public URI getReceiverUri() {
        return receiverUri;
    }

    public URI getSenderUri() {
        return senderUri;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public Timestamp getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isSeen() {
        return seen;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setProtocol(CommunicationType protocol) {
        this.protocol = protocol;
    }

    public void setReceiverUri(URI receiverUri) {
        this.receiverUri = receiverUri;
    }

    public void setSenderUri(URI senderUri) {
        this.senderUri = senderUri;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSentTimestamp(Timestamp sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public void setReceivedTimestamp(Timestamp receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
