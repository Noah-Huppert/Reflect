package com.noahhuppert.reflect.threading;

/**
 * A abstract runnable to automatically handle errors and return values.
 *
 * This makes it so you do not have to call {@link ThreadResultHandler#onDone(Object)} or
 * {@link ThreadResultHandler#onError(Exception)}, instead the ResultHandlerThread will take care of
 * that for you.
 *
 * @param <DataType> The type of data the runnable will return
 */
public abstract class ResultHandlerThread<DataType> implements Runnable {
    /**
     * The {@link ThreadResultHandler} to use to communicate with other threads
     */
    protected ThreadResultHandler<DataType> threadResultHandler;

    /**
     * Creates a new ResultHandlerThread
     * @param threadResultHandler {@link ResultHandlerThread#threadResultHandler}
     */
    protected ResultHandlerThread(ThreadResultHandler<DataType> threadResultHandler) {
        this.threadResultHandler = threadResultHandler;
    }

    /**
     * The method that contains the code to get the data
     * @return The data
     * @throws Exception A general exception that can be caught and dispatched to other threads with
     * {@link ThreadResultHandler#onError(Exception)}
     */
    public abstract DataType execute() throws Exception;

    @Override
    public void run() {
        try {
            DataType data = execute();
            synchronized (threadResultHandler){
                threadResultHandler.onDone(data);
            }

        } catch (Exception e){
            synchronized (threadResultHandler){
                threadResultHandler.onError(e);
            }
        }
    }
}
