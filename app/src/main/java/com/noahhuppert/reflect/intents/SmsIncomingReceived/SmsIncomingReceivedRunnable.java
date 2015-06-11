package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

        String[] getConversationProjection = {Telephony.TextBasedSmsColumns.THREAD_ID, Telephony.ThreadsColumns.RECIPIENT_IDS};//ERROR Column recipient_ids not found
        String getConversationQuery = Telephony.ThreadsColumns.RECIPIENT_IDS + " = ?";
        String[] getConversationQueryArgs = {senderPhoneNumber};

        Cursor cursor;

        synchronized (context){
            cursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                    getConversationProjection,
                    getConversationQuery,
                    getConversationQueryArgs,
                    null);
        }

        if(cursor.getCount() < 0){
            throw new WTFException("Could not find a conversation for the received sms message", "Sender phone number: " + senderPhoneNumber);
        }

        String[] threadIds = new String[cursor.getCount()];

        for(int i = 0; i < cursor.getCount(); i++){
            threadIds[i] = cursor.getString(0);
        }

        Log.d(TAG, threadIds + "");
        //TODO Show notification and update UI

        return null;
    }
}
