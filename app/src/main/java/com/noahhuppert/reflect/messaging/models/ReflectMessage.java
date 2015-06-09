package com.noahhuppert.reflect.messaging.models;

import android.net.Uri;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessageState;

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
    private @CommunicationType String protocol;

    /**
     * The uri of the person who received the message using the "messaging" scheme or the "jabber"
     * scheme
     */
    private Uri receiverUri;

    /**
     * The uri of the person who sent the message using the "messaging" scheme or the "jabber"
     * scheme
     */
    private Uri senderUri;

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
     * The state of the message, weather it is sending, sent, received, or read
     */
    private @MessageState int messageState;

    /* Actions */
    @Override
    public String toString() {
        return "[id: " + getId() +
               ", protocol: " + getProtocol() +
               ", receiverUri: " + getReceiverUri() +
               ", senderUri: " + getSenderUri() +
               ", body: " + getBody() +
               ", sentTimestamp: " + getSentTimestamp() +
               ", receivedTimestamp: " + getReceivedTimestamp() +
               ", messageState: " + getMessageState() + "]";
    }

    /* Getters */
    public String getId() {
        return id;
    }

    public @CommunicationType String getProtocol() {
        return protocol;
    }

    public Uri getReceiverUri() {
        return receiverUri;
    }

    public Uri getSenderUri() {
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

    public @MessageState int getMessageState(){
        return messageState;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setProtocol(@CommunicationType String protocol) {
        this.protocol = protocol;
    }

    public void setReceiverUri(Uri receiverUri) {
        this.receiverUri = receiverUri;
    }

    public void setSenderUri(Uri senderUri) {
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

    public void setMessageState(@MessageState int messageState){
        this.messageState = messageState;
    }
}
