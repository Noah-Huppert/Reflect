package com.noahhuppert.reflect.threading;

/**
 * A interface used to handle the results of a {@link ResultHandlerThread}
 * @param <DataType> The type of data being returned by the {@link ResultHandlerThread}
 */
public interface ThreadResultHandler<DataType> {
    /**
     * Called when the thread has completed its task without any errors
     * @param data The data returned by the {@link ResultHandlerThread}
     */
    void onDone(DataType data);

    /**
     * Called when an error has occurred in the thread
     * @param exception The exception that occurred
     */
    void onError(Exception exception);
}
