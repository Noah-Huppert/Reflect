package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noahhuppert.reflect.threading.MainThreadPool;

public class SmsIncomingReceived extends BroadcastReceiver {
    private static final String TAG = SmsIncomingReceived.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsIncomingReceivedRunnable smsIncomingReceivedRunnable = new SmsIncomingReceivedRunnable(intent, context);

        MainThreadPool.getInstance().getPool().submit(smsIncomingReceivedRunnable);

        //TODO Figure out why incoming messages arn't being stored, do I have to store them?
        //https://github.com/android/platform_packages_apps_mms/blob/b047af4d846dea5d69e3b3d51cbf5f841dd65ca5/src/com/android/mms/transaction/SmsReceiverService.java#L377
    }
}
