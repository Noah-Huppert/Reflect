package com.noahhuppert.reflect.database;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Android application used to hold context for DBFlow.
 */
public class ReflectDBFlowApplication extends Application {
    /**
     * Called when application is created. Stores context for DBFlow.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
