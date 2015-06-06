package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MmsIncomingReceived extends BroadcastReceiver {
    private static final String TAG = MmsIncomingReceived.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Incoming mms message received");
        //TODO Handle incoming Mms messages
    }
}
