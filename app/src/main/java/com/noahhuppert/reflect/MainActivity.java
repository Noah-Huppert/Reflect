package com.noahhuppert.reflect;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        Cursor c = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        while(c.moveToNext()){
            String out = "";//212
            String tid = "";
            for(int i = 0; i < c.getColumnCount(); i++){
                if(c.getColumnName(i).equals(Telephony.TextBasedSmsColumns.THREAD_ID)){
                    tid = c.getString(i);
                }

                if(tid.equals("212")) {
                    out += c.getColumnName(i) + " => " + c.getString(i) + " ";
                }
            }

            Log.d(TAG, out);
        }

        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
