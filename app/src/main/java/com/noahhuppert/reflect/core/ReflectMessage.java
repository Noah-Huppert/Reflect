package com.noahhuppert.reflect.core;

import android.net.Uri;

import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Timestamp;

/**
 * A class holding the information of a general message in Reflect. Every messaging provider must
 * provide this information.
 *
 * Extends a BaseModel and declares a primary key so it may be extended and saved in the database
 */
public class ReflectMessage extends BaseModel {
    /**
     * Uri of the message using the "messaging" scheme
     *
     * The protocol used to send the message can also be inferred from the host spot in the uri
     */
    @Column(columnType = Column.PRIMARY_KEY)
    public Uri uri;

    /**
     * The uri of the person who received the message using the "messaging" scheme or the "jabber"
     * scheme
     */
    public Uri receiverUri;

    /**
     * The uri of the person who sent the message using the "messaging" scheme or the "jabber"
     * scheme
     */
    public Uri senderUri;

    /**
     * Content of the message
     */
    public String body;

    /**
     * The Date and Time the message message was sent
     */
    public Timestamp sentTimestamp;

    /**
     * Has the message been read by the receiver
     */
    public boolean read;

    /**
     * Has the user seen the message, if it is there own this should be true
     */
    public boolean seen;
}
