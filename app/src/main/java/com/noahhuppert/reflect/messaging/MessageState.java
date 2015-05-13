package com.noahhuppert.reflect.messaging;

/**
 * An enum representing the state of a message. The values of this enum are meant to model the flow
 * of a message. So each value is not possible without the previous values happening.
 *
 * The only exception to this previous statement is {@link MessageState#SENDING_FAIL}. The message
 * flow can, and most likely will, skip over this step of the process.
 *
 * Messaging Flow:
 *            DRAFT
 *              |
 *              |
 *              v
 *           SENDING
 *              |
 *         _ _ _|_ _ _
 *        |           |
 *        |           |
 *        v           v
 *      SENT    SENDING_FAIL
 *        |
 *        |
 *        v
 *     RECEIVED
 *        |
 *        |
 *        v
 *      READ
 */
public enum MessageState {
    /**
     * Indicates that the message is currently a draft on the device. At this point the message is
     * purely local
     */
    DRAFT,

    /**
     * Indicates that the message is currently sending
     */
    SENDING,

    /**
     * Indicates that there was an issue sending the message
     */
    SENDING_FAIL,

    /**
     * Indicates that the message was successfully sent
     */
    SENT,

    /**
     * Indicates that that message has been received
     */
    RECEIVED,

    /**
     * Indicates that the message has been read
     */
    READ
}
