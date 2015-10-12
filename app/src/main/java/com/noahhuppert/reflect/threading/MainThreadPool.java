package com.noahhuppert.reflect.threading;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainThreadPool {
    private static final String TAG = MainThreadPool.class.getSimpleName();

    private static MainThreadPool instance;

    private ThreadPoolExecutor threadPoolExecutor;
    private final BlockingQueue<Runnable> workQueue;

    private static MainThreadPool getInstance(){
        if(instance == null){
            instance = new MainThreadPool();
        }

        return instance;
    }

    private MainThreadPool(){
        workQueue = new LinkedBlockingQueue<>();

        threadPoolExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1,
                TimeUnit.SECONDS,
                workQueue
        );
    }

    /* Getters */
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return getInstance().threadPoolExecutor;
    }
}
