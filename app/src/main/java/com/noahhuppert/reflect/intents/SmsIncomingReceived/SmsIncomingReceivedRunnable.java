package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.Errors;
import com.noahhuppert.reflect.exceptions.WTFException;

public class SmsIncomingReceivedRunnable implements Runnable {
    private static final String TAG = SmsIncomingReceivedRunnable.class.getSimpleName();

    private static final int ASSEMBLE_MESSAGE_BODY_INDEX = 0;
    private static final int ASSEMBLE_MESSAGE_SENDER_INDEX = 1;

    private final Intent intent;
    private final Context context;

    public SmsIncomingReceivedRunnable(Intent intent, Context context) {
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void run(){
        int errorCode;

        synchronized (intent){
            errorCode = intent.getIntExtra("errorCode", Errors.ERROR_OK);
        }

        String[] message = assembleMessage(intent);

        //Get THREAD_ID
        Uri getThreadIdUri = Uri.parse("content://mms-sms/threadID").buildUpon()
                .appendQueryParameter("recipient", message[ASSEMBLE_MESSAGE_SENDER_INDEX])
                .build();

        String[] getThreadIdProjection = {Telephony.Sms.Conversations.THREAD_ID};

        Cursor getThreadIdCursor;

        synchronized (context){
            getThreadIdCursor = context.getContentResolver().query(getThreadIdUri,
                    getThreadIdProjection,
                    null, null, null);
        }

        if(getThreadIdCursor == null || getThreadIdCursor.getCount() == 0){
            throw new WTFException("Failed to get or create conversation for incoming sms message",
                    "Message body: " + message[ASSEMBLE_MESSAGE_BODY_INDEX] + " Message Sender: " + message[ASSEMBLE_MESSAGE_SENDER_INDEX]);
        }

        getThreadIdCursor.moveToFirst();

        long messageThreadId = getThreadIdCursor.getLong(0);

        getThreadIdCursor.close();

        //Insert message and get _ID
        ContentValues insertMessageValues = new ContentValues();
        insertMessageValues.put(Telephony.TextBasedSmsColumns.ERROR_CODE, errorCode);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.BODY, message[ASSEMBLE_MESSAGE_BODY_INDEX]);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.ADDRESS, message[ASSEMBLE_MESSAGE_SENDER_INDEX]);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.THREAD_ID, messageThreadId);

        Uri insertedMessageUri;

        synchronized (context){
            insertedMessageUri = context.getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, insertMessageValues);
        }

        //Show Notification
        Notification.Builder messageNotificationBuilder;

        synchronized (context){
            messageNotificationBuilder = new Notification.Builder(context);
        }

        messageNotificationBuilder
                .setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
                .setContentTitle("Content title")
                .setContentText(message[ASSEMBLE_MESSAGE_BODY_INDEX]);

        //TODO Retrieve message contact display name
        //TODO Display notification
    }

    private String[] assembleMessage(Intent intent){
        SmsMessage[] messageParts;

        synchronized (intent){
            messageParts = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        }

        String messageSender = messageParts[0].getDisplayOriginatingAddress();
        String messageBody = "";


        for(int i = 0; i <messageParts.length; i++){
            messageBody += messageParts[i].getDisplayMessageBody();
        }

        String[] messageData = new String[2];

        messageData[ASSEMBLE_MESSAGE_BODY_INDEX] = messageBody;
        messageData[ASSEMBLE_MESSAGE_SENDER_INDEX] = messageSender;

        return messageData;
    }
}
