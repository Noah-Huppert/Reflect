package com.noahhuppert.reflect.threading;

import android.util.Log;

/**
 * A {@link ThreadResultHandler} that simpily takes the results of {@link ThreadResultHandler#onDone(Object)} and
 * {@link ThreadResultHandler#onError(Exception)} and prints them to the log
 */
public class DebugThreadResultHandler implements ThreadResultHandler{
    private String tag;

    public DebugThreadResultHandler() {
        tag = DebugThreadResultHandler.class.getSimpleName();
    }

    public DebugThreadResultHandler(String tag) {
        this.tag = tag;
    }

    @Override
    public void onDone(Object data) {
        if(data != null) {
            Log.d(tag, data.toString());
        } else {
            Log.d(tag, "null");
        }
    }

    @Override
    public void onError(Exception exception) {
        if(exception != null) {
            Log.e(tag, "Exception", exception);
        } else {
            Log.e(tag, "Exception: null");
        }
    }
}
