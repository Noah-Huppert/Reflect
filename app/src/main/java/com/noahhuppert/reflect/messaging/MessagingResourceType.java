package com.noahhuppert.reflect.messaging;

/**
 * An enum holding the possible values of the resource type part of the messaging scheme URI
 */
public enum MessagingResourceType {
    /**
     * Identifies that the resource is a message
     */
    MESSAGE,

    /**
     * Identifies that the resource is a conversation
     */
    CONVERSATION,

    /**
     * Identifies that the resource is a contact
     */
    CONTACT
}
