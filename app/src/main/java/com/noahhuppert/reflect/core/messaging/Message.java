package com.noahhuppert.reflect.core.messaging;

import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Timestamp;
/**
 * Reflect message model. Holds information about message.
 */
@Table(databaseName = ReflectDatabase.NAME)
public class Message extends BaseModel {
    /**
     * The id of the message
     */
    @Column(columnType = Column.PRIMARY_KEY)
    String id;

    /**
     * The id of the conversation to which the message belongs.
     */
    String conversationId;

    /**
     * The id of the contact who sent the message.
     */
    String contactId;

    /**
     * The message content.
     */
    String content;

    /**
     * The timestamp of the message receival date.
     */
    Timestamp timestamp;

    /**
     * The type of communication the message was received through
     */
    CommunicationType communicationType;

    /* Getters */
    public String getId() {
        return id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }
}
