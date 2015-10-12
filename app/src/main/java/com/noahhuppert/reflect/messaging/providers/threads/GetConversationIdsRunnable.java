package com.noahhuppert.reflect.messaging.providers.threads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.messaging.providers.threads.base.MessagingProviderRunnable;

public class GetConversationIdsRunnable extends MessagingProviderRunnable {
    public GetConversationIdsRunnable(@NonNull Handler handler, @NonNull Context context, @CommunicationType String communicationType) {
        super(handler, HandlerMessagePayload.CONVERSATION_IDS, context, communicationType);
    }

    @Override
    public Object onRun() {
        return MessagingProviderManager.get(communicationType).getConversationIds(context);
    }
}
