package com.noahhuppert.reflect.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A singleton class that holds the applications main thread pool. Run all threads through this
 * pool.
 */
public class MainThreadPool {
    private static MainThreadPool ourInstance = new MainThreadPool();

    private ExecutorService pool;

    public static MainThreadPool getInstance() {
        return ourInstance;
    }

    private MainThreadPool() {
        pool = Executors.newCachedThreadPool();
    }

    /* Getters */
    public ExecutorService getPool() {
        return pool;
    }
}
