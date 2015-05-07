package com.noahhuppert.reflect.threading;

public interface ThreadResultHandler<DataType> {
    void onDone(DataType data);
    void onError(Exception exception);
}
