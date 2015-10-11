package com.noahhuppert.reflect.messaging.models;

import android.net.Uri;

import com.noahhuppert.reflect.exceptions.Errors;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessageType;

import java.sql.Timestamp;

/**
 * A class used to store information about a {@link com.noahhuppert.reflect.messaging.MessagingResourceType#MESSAGE}
 * retrieved by a {@link com.noahhuppert.reflect.messaging.providers.MessagingProvider}
 */
public class Message {
    /**
     * The id of the message
     */
    public String id;

    /**
     * The Uris of the other party associated with the message
     *
     * Base on the {@link #messageType} it will be:
     *      - {@link MessageType#DRAFT} | {@link MessageType#SENDING | {@link MessageType#SENT}}
     *          - otherPartyUris = [recipient1, recipient2, ...]
     *      - {@link MessageType#RECEIVED}: An array with one element which is the sender uri
     *          - otherPartyUris = [sender]
     *
     */
    public Uri[] otherPartyUris;

    /**
     * The type of message
     */
    public @MessageType int messageType;

    /**
     * The error state of the message
     */
    public @Errors int error;

    /**
     * The {@link CommunicationType} that the message was retrieved from
     */
    public @CommunicationType String communicationType;

    /**
     * The timestamp marking when the message was sent
     */
    public Timestamp sentTimestamp;

    /**
     * The timestamp marking when the message was received
     */
    public Timestamp receivedTimestamp;

    /**
     * Whether the message has been read by the user
     */
    public boolean read;

    /* Actions */

    @Override
    public String toString() {
        return "[" +
                    "id => " + id +
                    ", otherPartyUris => " + otherPartyUris +
                    ", messageType => " + messageType +
                    ", error => " + error +
                    ", communicationType => " + communicationType +
                    ", sentTimestamp => " + sentTimestamp +
                    ", receivedTimestamp => " + receivedTimestamp +
                    ", read => " + read +
                "]";
    }
}
