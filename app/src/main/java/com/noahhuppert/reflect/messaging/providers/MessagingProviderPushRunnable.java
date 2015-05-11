package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public abstract class MessagingProviderPushRunnable<DataType> extends ResultHandlerThread<DataType> {
    /**
     * The data to push in the runnable thread
     */
    protected final DataType data;

    /**
     * The Android context to use in the runnable
     */
    protected final Context context;


    /**
     * Creates a new MessagingProviderPushRunnable, takes the basic arguments needed in a push task
     * @param data The data to push in the runnable thread
     * @param context The Android context to use
     * @param threadResultHandler The {@link ThreadResultHandler} to use
     */
    public MessagingProviderPushRunnable(DataType data, Context context, ThreadResultHandler<DataType> threadResultHandler) {
        super(threadResultHandler);
        this.data = data;
        this.context = context;
    }
}
