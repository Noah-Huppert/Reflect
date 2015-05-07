package com.noahhuppert.reflect.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A singleton class that holds the applications main thread pool. Run all threads through this
 * pool.
 */
public class MainThreadPool {
    /**
     * The private singleton instance of the MainThreadPool
     */
    private static final MainThreadPool ourInstance = new MainThreadPool();

    /**
     * The actual thread pool
     */
    private final ExecutorService pool;

    /**
     * Gets the singleton instance of the MainThreadPool
     * @return Singleton instance of the MainThreadPool
     */
    public static MainThreadPool getInstance() {
        return ourInstance;
    }

    /**
     * Creates a new MainThreadPool singleton. This constructor is marked as private to make sure there
     * is only one MainThreadPool
     */
    private MainThreadPool() {
        pool = Executors.newCachedThreadPool();
    }

    /* Getters */
    public ExecutorService getPool() {
        return pool;
    }
}
