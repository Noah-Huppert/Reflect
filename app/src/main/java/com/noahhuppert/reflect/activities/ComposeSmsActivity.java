package com.noahhuppert.reflect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ComposeSmsActivity extends AppCompatActivity {
    private static final String TAG = ComposeSmsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Intent to create new sms received");
        //TODO Implement intent request to create new sms
    }
}
