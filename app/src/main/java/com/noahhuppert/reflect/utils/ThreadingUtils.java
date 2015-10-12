package com.noahhuppert.reflect.utils;

import android.os.*;

/**
 * A class that contains methods to help set up threads
 */
public class ThreadingUtils {
    public static void SetThreadPriority() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
}
