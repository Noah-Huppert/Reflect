package com.noahhuppert.reflect.core;

import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;

/**
 * Reflect message model. Holds information about message.
 */
@Table(databaseName = ReflectDatabase.NAME)
public class Message {
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
    Date timestamp;
}
