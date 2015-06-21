package com.noahhuppert.reflect.messaging.models;

import android.net.Uri;

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
     * An array of contact display names
     */
    public String[] contactNames;

    /**
     * An array of Uris for the contacts involved in the conversation
     */
    public Uri[] contactUris;

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
        String contactNamesToString = "[";

        for(int i = 0; i < contactNames.length; i++){
            contactNamesToString += "\"" + contactNames[i] + "\"";

            if(i != contactNames.length - 1){
                contactNamesToString += ", ";
            } else{
                contactNamesToString += "]";
            }
        }

        String contactUrisToString = "[";

        for(int i = 0; i < contactUris.length; i++){
            contactUrisToString += "\"" + contactUris[i].toString() + "\"";

            if(i != contactUris.length - 1){
                contactUrisToString += ", ";
            } else{
                contactUrisToString += "]";
            }
        }

        return "[" +
                    "id => \"" + id + "\"" +
                    ", contactNames => " + Arrays.toString(contactNames) +
                    ", contactUris => " + Arrays.toString(contactUris) +
                    ", snippet => \"" + snippet + "\"" +
                    ", lastMessageTimestamp => " + lastMessageTimestamp +
                    ", communicationType => " + communicationType +
                "]";

    }
}
