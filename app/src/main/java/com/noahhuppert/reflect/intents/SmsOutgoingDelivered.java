package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsOutgoingDelivered extends BroadcastReceiver {
    private static final String TAG = SmsOutgoingDelivered.class.getSimpleName();

    public static final String ACTION_DELIVERED = "com.noahhuppert.reflect.intent.actions.DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO Handle sms outgoing delivery intent
    }
}
