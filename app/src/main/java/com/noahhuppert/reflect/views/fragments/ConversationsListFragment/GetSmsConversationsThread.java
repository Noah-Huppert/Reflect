package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;

import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class GetSmsConversationsThread extends ResultHandlerThread<Conversation[]> {
    private final Context context;

    public GetSmsConversationsThread(Context context, ThreadResultHandler<Conversation[]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected Conversation[] execute() throws Exception {
        synchronized (context) {
            return new SmsMessagingProvider().getConversations(context);
        }
    }
}
