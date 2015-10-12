package com.noahhuppert.reflect.messaging.providers.threads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.messaging.providers.threads.base.MessagingProviderRunnable;

public class GetMessageRunnable extends MessagingProviderRunnable {
    private String messageId;

    public GetMessageRunnable(@NonNull Handler handler, int handleWhat, Context context, @CommunicationType String communicationType, @NonNull String messageId) {
        super(handler, handleWhat, context, communicationType);
        this.messageId = messageId;
    }

    @Override
    public Object onRun() {
        return MessagingProviderManager.get(communicationType).getMessage(context, messageId);
    }
}
