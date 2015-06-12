package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.content.ContentValues;
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

import com.noahhuppert.reflect.database.IncomingMessagesTable;
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

        ContentValues incomingMessageValues = new ContentValues();
        incomingMessageValues.put(IncomingMessagesTable.COLUMNS.SENDER_PHONE_NUMBER, senderPhoneNumber);
        incomingMessageValues.put(IncomingMessagesTable.COLUMNS.MESSAGE_BODY, messageBody);
        incomingMessageValues.put(IncomingMessagesTable.COLUMNS.NOTIFICATION_ID, -1);

        synchronized (context) {
            IncomingMessagesTable.getInstance(context).getWritableDatabase().insert(IncomingMessagesTable.TABLE_NAME,
                    null,
                    incomingMessageValues);
        }

        return null;
    }
}
