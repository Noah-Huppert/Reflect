package com.noahhuppert.reflect.messaging.providers.threads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.messaging.providers.threads.base.MessagingProviderRunnable;

public class GetConversationMessageIdsRunnable extends MessagingProviderRunnable {
    private String conversationId;

    public GetConversationMessageIdsRunnable(@NonNull Handler handler, Context context, @CommunicationType String communicationType, @NonNull String conversationId) {
        super(handler, HandlerMessagePayload.MESSAGE_IDS, context, communicationType);
        this.conversationId = conversationId;
    }

    @Override
    public Object onRun() {
        return MessagingProviderManager.get(communicationType).getConversationMessageIds(context, conversationId);
    }
}
