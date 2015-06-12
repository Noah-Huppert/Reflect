package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsIncomingReceivedRunnable extends ResultHandlerThread {
    private static final String TAG = SmsIncomingReceivedRunnable.class.getSimpleName();

    private Intent intent;
    private Context context;

    public SmsIncomingReceivedRunnable(Intent intent, Context context, ThreadResultHandler threadResultHandler) {
        super(threadResultHandler);
        this.intent = intent;
        this.context = context;
    }

    @Override
    protected Object execute() throws Exception {
        SmsMessage[] messageParts;

        synchronized (intent){
            messageParts = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        }

        String senderPhoneNumber = messageParts[0].getDisplayOriginatingAddress();

        String messageBody = "";

        for(int i = 0; i < messageParts.length; i++){
            messageBody += messageParts[i].getDisplayMessageBody();
        }

        /*
        Alright here's the deal. When a message comes in the SQL table has not been updated yet. So
        we will set a ContentListener on the table for a new message. In this we will do what we wanted
        to do here and then we will unregister it. A new ContentListerner will be created every time a
        message is received
         */

        return null;
    }
}
