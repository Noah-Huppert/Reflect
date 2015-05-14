package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingManager;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.DebugThreadResultHandler;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;

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

        /* Test Get Sms */
        try {
            URI uri = MessagingUriBuilder.Build(MessagingResourceType.MESSAGE, CommunicationType.SMS, "1");
            MessagingManager.getInstance().fetchMessage(uri, getBaseContext(), new DebugThreadResultHandler(TAG));
        } catch (URISyntaxException | InvalidUriException e){
            Log.e(TAG, "Exception", e);
        }


        /* Test Send Sms */
        Button testSendSmsButton = (Button) findViewById(R.id.test_send_sms);
        testSendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getBaseContext(), "Test Sms Button", Toast.LENGTH_SHORT);
                toast.show();

                try {
                    new SmsMessagingProvider().pushMessage(null, null, new ThreadResultHandler<ReflectMessage>() {
                        @Override
                        public void onDone(ReflectMessage data) {

                        }

                        @Override
                        public void onError(Exception exception) {
                            Log.e(TAG, "Exception", exception);
                        }
                    });
                } catch (com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData invalidMessagingProviderPushData) {
                    invalidMessagingProviderPushData.printStackTrace();
                }
            }
        });
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
