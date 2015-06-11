package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidMessagingIdException;
import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessageState;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.TelephonyUtils;
import com.noahhuppert.reflect.utils.TimestampUtils;

import java.sql.Timestamp;

public class SmsGetMessageRunnable extends ResultHandlerThread<ReflectMessage> {
    private final long messageId;
    private Object messageIdLock;

    private final Context context;

    public SmsGetMessageRunnable(long messageId, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        super(threadResultHandler);
        this.messageId = messageId;
        messageIdLock = new Object();

        this.context = context;
    }

    @Override
    protected ReflectMessage execute() throws Exception {
        Cursor cursor = null;

        try {
            Uri messageUri;

            synchronized (messageIdLock) {
                messageUri = ContentUris.withAppendedId(Telephony.Sms.Inbox.CONTENT_URI, messageId);
            }

            synchronized (context) {
                cursor = context.getContentResolver().query(messageUri,
                        SmsMessagingProvider.SMS_MESSAGE_PROJECTION,
                        null, null, null);
            }

            if (cursor.getCount() == 0) {
                throw new InvalidMessagingIdException("The provided message id provided does not point to any messages", messageId + "");
            }

            if (cursor.getCount() > 1) {
                throw new InvalidMessagingIdException("The provided message id provided points to more than one message", messageId + "");
            }

            return reflectMessageFromCursor(cursor);
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private ReflectMessage reflectMessageFromCursor(Cursor cursor) throws NoTelephonyManagerException{
        ReflectMessage reflectMessage = new ReflectMessage();

        cursor.moveToFirst();

        String id = cursor.getString(0);

        String selfPhoneNumber;

        synchronized (context){
            selfPhoneNumber = TelephonyUtils.GetTelephonyManager(context).getLine1Number();
        }

        Uri senderUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(CommunicationType.SMS)
                .appendPath(MessagingResourceType.CONTACT)
                .appendPath(selfPhoneNumber)
                .build();
        Uri receiverUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(CommunicationType.SMS)
                .appendPath(MessagingResourceType.CONTACT)
                .appendPath(cursor.getString(1))
                .build();
        String body = cursor.getString(2);
        Timestamp sentTimestamp = TimestampUtils.FromLong(cursor.getLong(3));
        Timestamp receivedTimestamp = TimestampUtils.FromLong(cursor.getLong(4));
        boolean read = cursor.getInt(5) == 1;
        @MessageState int messagingState = read ? MessageState.READ : MessageState.RECEIVED;

        reflectMessage.setId(id);
        reflectMessage.setProtocol(CommunicationType.SMS);
        reflectMessage.setSenderUri(senderUri);
        reflectMessage.setReceiverUri(receiverUri);
        reflectMessage.setBody(body);
        reflectMessage.setSentTimestamp(sentTimestamp);
        reflectMessage.setReceivedTimestamp(receivedTimestamp);
        reflectMessage.setMessageState(messagingState);

        return reflectMessage;
    }
}
