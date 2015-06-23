package com.noahhuppert.reflect.messaging.models;

import com.noahhuppert.reflect.messaging.CommunicationType;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * A class for storing information about a {@link com.noahhuppert.reflect.messaging.MessagingResourceType#CONVERSATION}
 * retrieved by a {@link com.noahhuppert.reflect.messaging.providers.MessagingProvider}
 */
public class Conversation {
    /**
     * The id of the conversation
     */
    public String id;

    /**
     * An array of {@link Contact}s assoicated with the covnersation
     */
    public Contact[] contacts;

    /**
     * A snippet of the conversation
     */
    public String snippet;

    /**
     * A timestamp marking when the last message was sent/received
     */
    public Timestamp lastMessageTimestamp;

    /**
     * The {@link CommunicationType} that the conversation was retrieved from
     */
    public @CommunicationType String communicationType;

    /* Actions */
    @Override
    public String toString() {
        return "[" +
                    "id => \"" + id + "\"" +
                    ", contacts => " + Arrays.toString(contacts) +
                    ", snippet => \"" + snippet + "\"" +
                    ", lastMessageTimestamp => " + lastMessageTimestamp +
                    ", communicationType => " + communicationType +
                "]";

    }
}
