package com.noahhuppert.reflect.messaging;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MessageState.DRAFT,
        MessageState.SENDING,
        MessageState.SENDING_FAIL,
        MessageState.SENT,
        MessageState.RECEIVED,
        MessageState.READ
})
public @interface MessageState {
    /**
     * Indicates that the message is currently a draft on the device. At this point the message is
     * purely local
     */
    int DRAFT = 0;

    /**
     * Indicates that the message is currently sending
     */
    int SENDING = 1;

    /**
     * Indicates that there was an issue sending the message
     */
    int SENDING_FAIL = 2;

    /**
     * Indicates that the message was successfully sent
     */
    int SENT = 3;

    /**
     * Indicates that that message has been received
     */
    int RECEIVED = 4;

    /**
     * Indicates that the message has been read
     */
    int READ = 5;
}
