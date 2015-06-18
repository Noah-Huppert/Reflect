package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.activities.QuickReplyActivity;
import com.noahhuppert.reflect.database.IncomingSmsNotificationIdsTable;
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

        //Get message sender contact DISPLAY_NAME
        String[] getSenderContactProjection = {ContactsContract.Contacts.DISPLAY_NAME};
        Uri getSenderContactUri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
                .appendEncodedPath(message[ASSEMBLE_MESSAGE_SENDER_INDEX])
                .build();

        Cursor getSenderContactCursor;

        synchronized (context){
            getSenderContactCursor = context.getContentResolver().query(getSenderContactUri,
                    getSenderContactProjection,
                    null, null, null);
        }

        String messageSenderDisplayName;

        if(getSenderContactCursor.getCount() != 0){
            getSenderContactCursor.moveToFirst();
            messageSenderDisplayName = getSenderContactCursor.getString(0);
        } else{
            messageSenderDisplayName = message[ASSEMBLE_MESSAGE_SENDER_INDEX];
        }

        getSenderContactCursor.close();

        //Get stored notification id
        String[] getNotificationIdProjection = {IncomingSmsNotificationIdsTable.COLUMNS.NOTIFICATION_ID};
        String getNotificationIdQuery = IncomingSmsNotificationIdsTable.COLUMNS.SENDER + " = ?";
        String[] getNotificationIdQueryArgs = {message[ASSEMBLE_MESSAGE_SENDER_INDEX]};

        SQLiteDatabase getNotificationIdDatabase;

        synchronized (context){
            getNotificationIdDatabase = new IncomingSmsNotificationIdsTable(context).getWritableDatabase();
        }

        Cursor getNotificationIdCursor = getNotificationIdDatabase.query(IncomingSmsNotificationIdsTable.TABLE_NAME,
                getNotificationIdProjection,
                getNotificationIdQuery,
                getNotificationIdQueryArgs,
                null, null, null);

        int messageNotificationId = -1;

        if(getNotificationIdCursor.getCount() != 0){
            getNotificationIdCursor.moveToFirst();
            messageNotificationId = getNotificationIdCursor.getInt(0);

            getNotificationIdCursor.close();
            getNotificationIdDatabase.close();
        }

        //Get previous unread messages
        String[] getRelatedUnreadMessagesProjection = {Telephony.TextBasedSmsColumns.BODY};
        String getRelatedUnreadMessagesQuery = Telephony.TextBasedSmsColumns.ADDRESS + " = ? AND " +
                                               Telephony.TextBasedSmsColumns.SEEN + " = ?";
        String[] getRelatedUnreadMessagesQueryArgs = {message[ASSEMBLE_MESSAGE_SENDER_INDEX], "0"};

        Cursor getRelatedUnreadMessagesCursor;

        synchronized (context){
            getRelatedUnreadMessagesCursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                    getRelatedUnreadMessagesProjection,
                    getRelatedUnreadMessagesQuery,
                    getRelatedUnreadMessagesQueryArgs,
                    null);
        }

        String[] relatedUnreadMessages = new String[getRelatedUnreadMessagesCursor.getCount()];

        for (int i = 0; i < getRelatedUnreadMessagesCursor.getCount(); i++) {
            getRelatedUnreadMessagesCursor.moveToPosition(i);
            relatedUnreadMessages[i] = getRelatedUnreadMessagesCursor.getString(0);
        }

        getRelatedUnreadMessagesCursor.close();

        //Show Notification
        NotificationCompat.Builder messageNotificationBuilder;

        synchronized (context){
            messageNotificationBuilder = new NotificationCompat.Builder(context);
        }

        if(relatedUnreadMessages.length > 1) {//Display large format notification if more than one unread message
            NotificationCompat.InboxStyle messageNotificationIndexBoxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle(messageSenderDisplayName);

            for (int i = 0; i < relatedUnreadMessages.length; i++) {
                messageNotificationIndexBoxStyle.addLine(relatedUnreadMessages[i]);
            }

            messageNotificationBuilder.setStyle(messageNotificationIndexBoxStyle);
        } else {
            messageNotificationBuilder.setContentText(message[ASSEMBLE_MESSAGE_BODY_INDEX]);
        }

        //Show Notification - Add quick reply action
        Intent replyIntent;

        synchronized (context){
            replyIntent = new Intent(context, QuickReplyActivity.class);
        }

        replyIntent.putExtra(QuickReplyActivity.EXTRA_QUICK_REPLY_THREAD_ID, messageThreadId);

        PendingIntent replyPendingIntent;

        synchronized (context){
            replyPendingIntent = PendingIntent.getActivity(context, 0, replyIntent, 0);
        }

        messageNotificationBuilder
                .setSmallIcon(R.drawable.ic_message_white_48dp)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(R.drawable.ic_reply_white_48dp, "Reply", replyPendingIntent);

        NotificationManagerCompat notificationManager;

        synchronized (context){
            notificationManager = NotificationManagerCompat.from(context);
        }

        notificationManager.notify(messageNotificationId, messageNotificationBuilder.build());

        if(getNotificationIdCursor.getCount() == 0){
            ContentValues insertNotificationIdValues = new ContentValues();
            insertNotificationIdValues.put(IncomingSmsNotificationIdsTable.COLUMNS.SENDER, message[ASSEMBLE_MESSAGE_SENDER_INDEX]);
            insertNotificationIdValues.put(IncomingSmsNotificationIdsTable.COLUMNS.NOTIFICATION_ID, messageNotificationId);

            getNotificationIdDatabase.insert(IncomingSmsNotificationIdsTable.TABLE_NAME,
                    null, insertNotificationIdValues);

            getNotificationIdCursor.close();
            getNotificationIdDatabase.close();
        }
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
