package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;
import com.noahhuppert.reflect.uri.MessagingUriResourceProvider;
import com.noahhuppert.reflect.uri.MessagingUriResourceType;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

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

        //Get test contact
        try {
            URI contactUri = MessagingUriBuilder.Build(MessagingUriResourceType.CONTACT, MessagingUriResourceProvider.SMS, "1");

            ThreadResultHandler<ReflectContact> reflectContactThreadResultHandler = new ThreadResultHandler<ReflectContact>() {
                @Override
                public void onDone(ReflectContact data) {
                    if (data != null) {
                        Log.d(TAG, data.toString());
                    }
                }

                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "exception", exception);
                }
            };

            MessagingProviderManager.getInstance().fetchContact(contactUri, getBaseContext(), reflectContactThreadResultHandler);
        } catch (URISyntaxException e) {
            Log.e(TAG, "exception", e);
        } catch (InvalidUriException e) {
            Log.e(TAG, "exception", e);
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
