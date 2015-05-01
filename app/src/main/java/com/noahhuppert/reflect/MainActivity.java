package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;
import com.noahhuppert.reflect.uri.MessagingUriResourceProvider;
import com.noahhuppert.reflect.uri.MessagingUriResourceType;

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

        /*Cursor contactsCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

        String out = "[";

        while(contactsCursor.moveToNext()){
            for(int i = 0; i < contactsCursor.getColumnCount(); i++){
                if(i != 0){
                    out += ", ";
                }

                out += contactsCursor.getColumnName(i) + ": " + contactsCursor.getString(i);
            }
        }

        out += "]";

        Log.d(TAG, out);*/

        Log.d(TAG, "------------------- RESTART -------------------");

        try {
            MessagingProviderManager.getInstance().fetchMessage(MessagingUriBuilder.Build(MessagingUriResourceType.MESSAGE, MessagingUriResourceProvider.SMS, "60"), getBaseContext(), new ThreadResultHandler<ReflectMessage>() {
                @Override
                public void onDone(ReflectMessage data) {
                    Log.d(TAG, data.toString());
                }

                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "Exception: ", exception);
                }
            });
        } catch(URISyntaxException e){
            Log.e(TAG, e.toString());
        } catch(InvalidUriException e){
            Log.e(TAG, e.toString());
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
