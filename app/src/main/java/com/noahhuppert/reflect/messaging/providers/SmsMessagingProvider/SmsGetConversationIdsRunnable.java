package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.os.*;
import android.os.Process;

import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderCache;
import com.noahhuppert.reflect.threading.MainThreadPool;

public class SmsGetConversationIdsRunnable implements Runnable {
    private final Context context;
    private final Handler handler;

    public SmsGetConversationIdsRunnable(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        String[] conversationIds = MessagingProviderCache.get(SmsMessagingProvider.class).getConversationIds(context);

        Message message = handler.obtainMessage(SmsMessagingProvider.HandlerMessagePayload.CONVERSATION_IDS, conversationIds);
        handler.sendMessage(message);
    }
}
