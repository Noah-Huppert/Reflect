package com.noahhuppert.reflect.uri;

/**
 * An enum holding the possible values of the resource type part of the messaging scheme URI
 */
public enum MessagingUriResourceType {
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
    CONTACT;
}
