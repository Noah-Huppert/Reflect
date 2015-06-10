package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

public class SmsOutgoingDelivered extends BroadcastReceiver {
    private static final String TAG = SmsOutgoingDelivered.class.getSimpleName();

    public static final String ACTION_DELIVERED = "com.noahhuppert.reflect.intent.actions.DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO Handle sms outgoing delivery intent
        Log.d(TAG, "Delivered message " + intent.getData() +
                " (" + intent.getIntExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_MESSAGE_PART, -1) + "/" +
                intent.getIntExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_TOTAL_MESSAGE_PARTS, -1) + ")");
    }
}
