package com.noahhuppert.reflect.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HeadlessSmsSendService extends Service {
    private static final String TAG = HeadlessSmsSendService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Intent to heedlessly send SMS received");
        //TODO Implement intent request to headlessly send sms
        return null;
    }
}
