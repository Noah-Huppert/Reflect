package com.noahhuppert.reflect.messaging.providers.threads.base;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        HandlerMessagePayload.CONVERSATION_IDS,
        HandlerMessagePayload.CONVERSATION,
        HandlerMessagePayload.CONTACT,
        HandlerMessagePayload.MESSAGE_IDS,
        HandlerMessagePayload.MESSAGE
})
public @interface HandlerMessagePayload{
    int CONVERSATION_IDS = 0;
    int CONVERSATION = 1;
    int CONTACT = 2;
    int MESSAGE_IDS = 3;
    int MESSAGE = 4;
}