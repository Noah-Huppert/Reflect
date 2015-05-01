package com.noahhuppert.reflect.threading;

public abstract class ResultHandlerThread<DataType> implements Runnable {
    protected ThreadResultHandler<DataType> threadResultHandler;

    protected ResultHandlerThread(ThreadResultHandler<DataType> threadResultHandler) {
        this.threadResultHandler = threadResultHandler;
    }

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
