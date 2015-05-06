package com.noahhuppert.reflect;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;
import com.noahhuppert.reflect.messaging.MessagingResourceType;

import java.net.URI;
import java.net.URISyntaxException;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_main);

        Log.d(TAG, "---------- RESTART ----------");
        //Test conversation
        ThreadResultHandler<ReflectConversation> reflectConversationThreadResultHandler = new ThreadResultHandler<ReflectConversation>() {
            @Override
            public void onDone(ReflectConversation data) {
                Log.d(TAG, data.toString());
            }

            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "Thread Result Exception", exception);
            }
        };

        try {
            URI conversationUri = MessagingUriBuilder.Build(MessagingResourceType.CONVERSATION, CommunicationType.SMS, "210");
            MessagingProviderManager.getInstance().fetchConversation(conversationUri, getBaseContext(), reflectConversationThreadResultHandler);
        } catch (URISyntaxException e){
            Log.e(TAG, "Exception", e);
        } catch (InvalidUriException e){
            Log.e(TAG, "Exception", e);
        }
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
