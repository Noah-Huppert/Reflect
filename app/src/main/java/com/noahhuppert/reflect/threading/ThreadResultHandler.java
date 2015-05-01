package com.noahhuppert.reflect.threading;

public interface ThreadResultHandler<DataType> {
    public void onDone(DataType data);
    public void onError(Exception exception);
}
