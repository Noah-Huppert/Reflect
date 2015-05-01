package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * An abstract thread task for MessagingProvider fetch tasks
 * @param <DataType>
 */
public abstract class MessagingProviderFetchRunnable<DataType> extends ResultHandlerThread<DataType> {
    protected URI uri;
    protected Context context;

    public MessagingProviderFetchRunnable(URI uri, Context context, ThreadResultHandler<DataType> threadResultHandler) {
        super(threadResultHandler);
        this.uri = uri;
        this.context = context;
    }
}
