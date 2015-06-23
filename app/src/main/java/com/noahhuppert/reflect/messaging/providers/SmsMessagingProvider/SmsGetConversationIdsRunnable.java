package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsGetConversationIdsRunnable extends ResultHandlerThread<String[]> {
    private final Context context;

    public SmsGetConversationIdsRunnable(@NonNull final Context context, ThreadResultHandler<String[]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected String[] execute() throws Exception {
        synchronized (context) {
            return new SmsMessagingProvider().getConversationIds(context);
        }
    }
}
