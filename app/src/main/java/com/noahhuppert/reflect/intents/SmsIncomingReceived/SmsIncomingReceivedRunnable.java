package com.noahhuppert.reflect.intents.SmsIncomingReceived;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.activities.QuickReplyActivity;
import com.noahhuppert.reflect.exceptions.Errors;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.utils.SmsThreadUtils;

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
        String[] message = assembleMessage(intent);
        long messageThreadId = SmsThreadUtils.getOrCreateThreadId(context, message[ASSEMBLE_MESSAGE_SENDER_INDEX]);

        insertMessage(message, messageThreadId);

        String messageSenderDisplayName = getSenderDisplayName(message);

        String[] relatedUnreadMessages = getRelatedUnreadMessages(message);

        showNotification(message, relatedUnreadMessages, messageSenderDisplayName, messageThreadId);
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

    private void insertMessage(String[] message, long messageThreadId){
        int errorCode;

        synchronized (intent){
            errorCode = intent.getIntExtra("errorCode", Errors.ERROR_OK);
        }

        ContentValues insertMessageValues = new ContentValues();
        insertMessageValues.put(Telephony.TextBasedSmsColumns.ERROR_CODE, errorCode);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.BODY, message[ASSEMBLE_MESSAGE_BODY_INDEX]);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.ADDRESS, message[ASSEMBLE_MESSAGE_SENDER_INDEX]);
        insertMessageValues.put(Telephony.TextBasedSmsColumns.THREAD_ID, messageThreadId);

        synchronized (context){
            context.getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, insertMessageValues);
        }
    }

    private String getSenderDisplayName(String[] message){
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

        String messageSenderDisplayName = message[ASSEMBLE_MESSAGE_SENDER_INDEX];

        if(getSenderContactCursor.getCount() != 0){
            getSenderContactCursor.moveToFirst();
            messageSenderDisplayName = getSenderContactCursor.getString(0);
        }

        getSenderContactCursor.close();

        return messageSenderDisplayName;
    }

    private String[] getRelatedUnreadMessages(String[] message){
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

        return relatedUnreadMessages;
    }

    private void showNotification(String[] message, String[] relatedUnreadMessages, String messageSenderDisplayName, long messageThreadId){
        NotificationCompat.Builder messageNotificationBuilder;

        synchronized (context){
            messageNotificationBuilder = new NotificationCompat.Builder(context);
        }

        if(relatedUnreadMessages.length > 1) {//Display large format notification if more than one unread message
            NotificationCompat.InboxStyle messageNotificationIndexBoxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle(messageSenderDisplayName);

            int messageNotificationLines = 7;

            if(relatedUnreadMessages.length < 7){
                messageNotificationLines = relatedUnreadMessages.length;
            }

            for (int i = 0; i < messageNotificationLines; i++) {
                messageNotificationIndexBoxStyle.addLine(relatedUnreadMessages[i]);
            }

            if(relatedUnreadMessages.length > 7){
                messageNotificationIndexBoxStyle.setSummaryText("+" + (relatedUnreadMessages.length - 7) + " more");
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
                .setContentTitle(messageSenderDisplayName)
                .addAction(R.drawable.ic_reply_white_48dp, "Reply", replyPendingIntent);

        NotificationManagerCompat notificationManager;

        synchronized (context){
            notificationManager = NotificationManagerCompat.from(context);
        }

        //TODO Am I abusing the notification tag?
        notificationManager.notify(message[ASSEMBLE_MESSAGE_SENDER_INDEX], 0, messageNotificationBuilder.build());
    }
}
