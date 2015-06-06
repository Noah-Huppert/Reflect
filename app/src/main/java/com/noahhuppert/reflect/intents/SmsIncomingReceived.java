package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsIncomingReceived extends BroadcastReceiver {
    private static final String TAG = SmsIncomingReceived.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Incoming sms message received");
        //TODO Handle incoming sms messages
    }
}
