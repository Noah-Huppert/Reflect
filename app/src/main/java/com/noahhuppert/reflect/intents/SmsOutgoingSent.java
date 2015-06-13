package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

import java.util.Set;

/**
 * A class for receiving Sms related intents
 */
public class SmsOutgoingSent extends BroadcastReceiver {
    private static final String TAG = SmsOutgoingSent.class.getSimpleName();

    /**
     * Sms Intent action when a message is sent
     */
    public static final String ACTION_SENT = "com.noahhuppert.reflect.intent.actions.SENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO Handle sms outgoing sent intent, Get message id somehow

        Log.d(TAG, "Sent message " + intent.getData() +
                " (" + intent.getIntExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_MESSAGE_PART, -1) + "/" +
                intent.getIntExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_TOTAL_MESSAGE_PARTS, -1) + ")");
    }
}
