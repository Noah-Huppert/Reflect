package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import javax.validation.constraints.NotNull;

public abstract class LocalBroadcaster {
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    public LocalBroadcaster(@NotNull IntentFilter intentFilter) {
        this.intentFilter = intentFilter;
    }


    /* Lifecycle methods */
    public void register(@NotNull Context context){
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onBroadcast(context, intent);
                }
            };
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unregister(@NotNull Context context){
        broadcastReceiver = null;//TODO Does this save memory? Or does it use more by creating a new object every time?

        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    /* Messaging methods */
    public static void sendBroadcast(Intent intent, Context context){
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public abstract void onBroadcast(@NotNull Context context, @NotNull Intent intent);
}
