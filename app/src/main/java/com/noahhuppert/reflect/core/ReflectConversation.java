package com.noahhuppert.reflect.core;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;

import java.util.List;

/**
 * A class for holding information about a conversation
 */
public class ReflectConversation {
    /**
     * The uri of the conversation using the "messaging" scheme
     */
    @Column(columnType = Column.PRIMARY_KEY)
    public Uri uri;

    /**
     * A list of message uris using the "messaging" scheme
     */
    public List<Uri> messageUris;
}
