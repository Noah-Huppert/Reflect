package com.noahhuppert.reflect.messaging.providers.threads.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.utils.ThreadingUtils;

public abstract class MessagingProviderRunnable implements Runnable {
    private Handler handler;
    private final int handleWhat;

    protected final Context context;
    protected final @CommunicationType String communicationType;

    public MessagingProviderRunnable(@NonNull Handler handler, int handleWhat, Context context, @CommunicationType String communicationType) {
        this.handler = handler;
        this.handleWhat = handleWhat;

        this.context = context;
        this.communicationType = communicationType;
    }

    public abstract Object onRun();

    @Override
    public void run() {
        ThreadingUtils.SetThreadPriority();

        Object result = onRun();

        Message message = handler.obtainMessage(handleWhat, result);
        handler.sendMessage(message);
    }
}
