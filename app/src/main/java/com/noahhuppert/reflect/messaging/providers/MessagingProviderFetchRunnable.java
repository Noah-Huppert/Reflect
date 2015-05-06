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
    /**
     * The Uri of the resource the runnable will fetch
     */
    protected URI uri;

    /**
     * The context to use in the runnable
     */
    protected Context context;

    /**
     * Creates a new MessagingProviderFetchRunnable, takes the basic arguments needed in a fetch task
     * @param uri The Uri of the resource that the runnable will fetch
     * @param context The Android Context to use
     * @param threadResultHandler The {@link ThreadResultHandler} to use
     */
    public MessagingProviderFetchRunnable(URI uri, Context context, ThreadResultHandler<DataType> threadResultHandler) {
        super(threadResultHandler);
        this.uri = uri;
        this.context = context;
    }
}
