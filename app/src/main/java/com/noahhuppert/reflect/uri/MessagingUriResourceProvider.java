package com.noahhuppert.reflect.uri;

/**
 * An enum holding the possible values for the resource provider part of a messaging scheme URI
 */
public enum MessagingUriResourceProvider {
    /**
     * Identifies that the resource is provided by a XMPP service
     */
    XMPP,

    /**
     * Identifies that the resource is provided via SMS
     */
    SMS,

    /**
     * Identifies that the resource is provided by merging data from XMPP and SMS
     */
    JOINT;
}
