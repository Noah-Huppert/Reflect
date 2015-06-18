package com.noahhuppert.reflect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.noahhuppert.reflect.R;

public class QuickReplyActivity extends AppCompatActivity {
    public static final String EXTRA_QUICK_REPLY_THREAD_ID = "thread_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quick_reply);

        //TODO Implement Quick Reply
    }
}
