package com.noahhuppert.reflect.messaging;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An enum holding the possible values of the resource type part of the messaging scheme URI
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        MessagingResourceType.MESSAGE,
        MessagingResourceType.CONVERSATION,
        MessagingResourceType.CONTACT
})
public @interface MessagingResourceType{
    /**
     * Identifies that the resource is a message
     */
    String MESSAGE = "MESSAGE";

    /**
     * Identifies that the resource is a conversation
     */
    String CONVERSATION = "CONVERSATION";

    /**
     * Identifies that the resource is a contact
     */
    String CONTACT = "CONTACT";
}