package com.noahhuppert.reflect.messaging;

import android.database.Cursor;
import android.provider.Telephony;

import com.venmo.cursor.IterableCursorWrapper;

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
    private CommunicationType protocol;

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

    /* Cursor Wrappers */
    /**
     * A cursor wrapper for taking a SMS Conversation table query and converting it to a
     * ReflectConversation
     */
    public static class SmsCursor extends IterableCursorWrapper<ReflectConversation> {
        public static final String KEY_ID = Telephony.TextBasedSmsColumns.THREAD_ID;
        public static final String KEY_SNIPPET = Telephony.Sms.Conversations.SNIPPET;

        public SmsCursor(Cursor cursor) {
            super(cursor);
        }

        @Override
        public ReflectConversation peek() {
            //Get information
            String id = getString(KEY_ID, "");
            CommunicationType protocol = CommunicationType.SMS;
            String snippet = getString(KEY_SNIPPET, "");

            //Set data
            ReflectConversation reflectConversation = new ReflectConversation();
            reflectConversation.setId(id);
            reflectConversation.setProtocol(protocol);
            reflectConversation.setSnippet(snippet);

            return reflectConversation;
        }
    }

    /* Getters */
    public String getId() {
        return id;
    }

    public CommunicationType getProtocol() {
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

    public void setProtocol(CommunicationType protocol) {
        this.protocol = protocol;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }
}
