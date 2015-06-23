package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsGetConversationRunnable extends ResultHandlerThread<Conversation> {
    private final Context context;
    private final String threadId;

    public SmsGetConversationRunnable(@NonNull final Context context, @NonNull final String threadId, ThreadResultHandler<Conversation> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
        this.threadId = threadId;
    }

    @Override
    protected Conversation execute() throws Exception {
        synchronized (context){
            synchronized (threadId){
                return new SmsMessagingProvider().getConversation(context, threadId);
            }
        }
    }
}
