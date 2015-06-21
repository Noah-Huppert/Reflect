package com.noahhuppert.reflect.messaging;

import android.support.annotation.IntDef;

import com.noahhuppert.reflect.exceptions.Errors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MessageType.DRAFT,
        MessageType.RECEIVED,
        MessageType.SENDING,
        MessageType.SENT
})
public @interface MessageType {
    /**
     * A message that has been composed, however the actions of sending the message has not been
     * initiated by the user.
     *
     * Possible {@link Errors}s:
     *      - {@link Errors#OK}: OK
     */
    int DRAFT = 0;

    /**
     * A message that was sent by another party and was received by the user
     *
     * Possible {@link Errors}s:
     *      - {@link Errors#OK}: OK
     */
    int RECEIVED = 1;

    /**
     * A message that is in the process of sending
     *
     *
     * Possible {@link Errors}s:
     *      - {@link Errors#OK}: The message is sending
     *      - {@link Errors#FAILED}: The message failed to send
     */
    int SENDING = 2;

    /**
     * A message that has been sent
     *
     *  Possible {@link Errors}s:
     *      - {@link Errors#OK}: The message was sent
     */
    int SENT = 3;
}
