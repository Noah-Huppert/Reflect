package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
    Uri smsUri = Uri.parse(intent.getStringExtra("uri"));
        String smsId = smsUri.getLastPathSegment();

        //TODO Handle sms outgoing sent intent
    }
}
