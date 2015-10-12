package com.noahhuppert.reflect.messaging.providers.threads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.messaging.providers.threads.base.MessagingProviderRunnable;

public class GetConversationRunnable extends MessagingProviderRunnable {
    private String conversationId;

    public GetConversationRunnable(@NonNull Handler handler, @NonNull Context context, @CommunicationType String communicationType, String conversationId) {
        super(handler, HandlerMessagePayload.CONVERSATION, context, communicationType);
        this.conversationId = conversationId;
    }

    @Override
    public Object onRun() {
        return MessagingProviderManager.get(communicationType).getConversation(context, conversationId);
    }
}
