package com.noahhuppert.reflect.threading;

public interface StreamingResultHandler<DataType> {
    void onProgress(DataType result);
}
