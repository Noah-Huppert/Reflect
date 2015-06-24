package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.os.*;
import android.os.Process;

import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderCache;

public class SmsGetConversationRunnable implements Runnable {
    private final Context context;
    private final Handler handler;
    private final String threadId;

    public SmsGetConversationRunnable(Context context, Handler handler, String threadId) {
        this.context = context;
        this.handler = handler;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Conversation conversation = MessagingProviderCache.get(SmsMessagingProvider.class).getConversation(context, threadId);

        Message message = handler.obtainMessage(SmsMessagingProvider.HandlerMessagePayload.CONVERSATION, conversation);
        handler.sendMessage(message);
    }
}
