package com.noahhuppert.reflect.messaging.models;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessageState;
import com.noahhuppert.reflect.utils.TimestampUtils;
import com.venmo.cursor.IterableCursorWrapper;

import java.net.URI;
import java.net.URISyntaxException;
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
     * The state of the message, weather it is sending, sent, received, or read
     */
    private MessageState messageState;

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
    /* Cursor Wrappers */
    /**
     * A cursor wrapper for taking a SMS table query and converting it to a ReflectMessage
     */
    public static class SmsCursor extends IterableCursorWrapper<ReflectMessage> {
        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_SENDER_URI = Telephony.TextBasedSmsColumns.ADDRESS;
        public static final String KEY_BODY = Telephony.TextBasedSmsColumns.BODY;
        public static final String KEY_SENT_TIMESTAMP = Telephony.TextBasedSmsColumns.DATE_SENT;
        public static final String KEY_RECEIVED_TIMESTAMP = Telephony.TextBasedSmsColumns.DATE;
        public static final String KEY_READ = Telephony.TextBasedSmsColumns.READ;
        public static final String KEY_SEEN = Telephony.TextBasedSmsColumns.SEEN;

        public SmsCursor(Cursor cursor) {
            super(cursor);
        }

        @Override
        public ReflectMessage peek() {
            try {
                //Get information
                String id = getString(KEY_ID, "");

                //Static information
                CommunicationType protocol = CommunicationType.SMS;//Always SMS
                URI receiverUri = new URI(getString(KEY_SENDER_URI, ""));//Always the user


                URI senderUri = new URI(getString(KEY_SENDER_URI, ""));
                String body = getString(KEY_BODY, "");
                Timestamp sentTimestamp = TimestampUtils.FromLong(getLong(KEY_SENT_TIMESTAMP, 0));
                Timestamp receivedTimestamp = TimestampUtils.FromLong(getLong(KEY_RECEIVED_TIMESTAMP, 0));
                boolean read = getBoolean(KEY_READ, false);
                MessageState messageState = read ? MessageState.READ : MessageState.RECEIVED;

                //Set data
                ReflectMessage reflectMessage = new ReflectMessage();
                reflectMessage.setId(id);
                reflectMessage.setProtocol(protocol);
                reflectMessage.setReceiverUri(receiverUri);
                reflectMessage.setSenderUri(senderUri);
                reflectMessage.setBody(body);
                reflectMessage.setSentTimestamp(sentTimestamp);
                reflectMessage.setReceivedTimestamp(receivedTimestamp);
                reflectMessage.setMessageState(messageState);

                return reflectMessage;
            } catch(URISyntaxException e){
                return null;
            }
        }
    }

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

    public MessageState getMessageState(){
        return messageState;
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

    public void setMessageState(MessageState messageState){
        this.messageState = messageState;
    }
}
