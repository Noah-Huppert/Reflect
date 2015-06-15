package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.activities.MainActivity;
import com.noahhuppert.reflect.database.IncomingSmsTable;
import com.noahhuppert.reflect.database.UnknownSmsMessagesTable;

import java.io.IOException;
import java.security.UnrecoverableKeyException;

public class SmsIncomingReceivedRunnable implements Runnable {
    private static final String TAG = SmsIncomingReceivedRunnable.class.getSimpleName();

    private Intent intent;
    private Context context;

    public SmsIncomingReceivedRunnable(Intent intent, Context context) {
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void run(){
        //Assemble text message
        SmsMessage[] messageParts;

        synchronized (intent){
            messageParts = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        }

        String senderPhoneNumber = messageParts[0].getDisplayOriginatingAddress();
        String messageBody = "";

        for(int i = 0; i < messageParts.length; i++){
            messageBody += messageParts[i].getDisplayMessageBody();
        }

        //Get contact name if present
        String senderName = senderPhoneNumber;
        Bitmap senderPhotoBitmap = null;

        Cursor cursor;

        Uri getSenderNameQueryUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(senderPhoneNumber));
        String[] getSenderNameQueryProjection = {
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.PHOTO_URI
        };

        synchronized (context){
            cursor = context.getContentResolver().query(getSenderNameQueryUri,
                    getSenderNameQueryProjection,
                    null, null, null);
        }

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            senderName = cursor.getString(0);
            String senderPhotoUriString = cursor.getString(1);

            if(senderPhotoUriString != null) {
                try {
                    senderPhotoBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                            Uri.parse(senderPhotoUriString));
                } catch (IOException e) {
                }
            }
        }

        cursor.close();

        //Show notification
        int notificationId = -1;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                0,
                notificationIntent,
                0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_message_white_48dp)
                .setContentTitle(senderName)
                .setContentText(messageBody)
                .setContentIntent(notificationPendingIntent);

        if(senderPhotoBitmap != null){
            notificationBuilder.setLargeIcon(senderPhotoBitmap);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());

        //Save text message in incoming message table
        ContentValues incomingMessageValues = new ContentValues();
        //TODO Make message id and insert it
        incomingMessageValues.put(UnknownSmsMessagesTable.COLUMNS.CONTENT, messageBody);
        incomingMessageValues.put(UnknownSmsMessagesTable.COLUMNS.NOTIFICATION_ID, notificationId);
        incomingMessageValues.put(UnknownSmsMessagesTable.COLUMNS.SMS_NUMBER, senderPhoneNumber);
        incomingMessageValues.put(UnknownSmsMessagesTable.COLUMNS.STATE, UnknownSmsMessagesTable.MESSAGE_STATES.INCOMING_RECEIVED);

        SQLiteDatabase db;

        synchronized (context){
            db = new UnknownSmsMessagesTable(context).getWritableDatabase();
        }

        db.insert(UnknownSmsMessagesTable.TABLE_NAME,
                null, incomingMessageValues);

        db.close();
    }
}
