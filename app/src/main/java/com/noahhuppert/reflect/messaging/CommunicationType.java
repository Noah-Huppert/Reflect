package com.noahhuppert.reflect.messaging;

/**
 * Type of communication, either SMS or XMPP. Used to determine contact type and preferred
 * communication method.
 */
public enum CommunicationType {
    /**
     * Communication type of XMPP
     */
    XMPP,

    /**
     * Communication type of SMS
     */
    SMS;
}
