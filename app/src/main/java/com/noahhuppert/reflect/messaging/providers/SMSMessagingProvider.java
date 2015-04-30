package com.noahhuppert.reflect.messaging.providers;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.uri.MessagingUriUtils;
import com.venmo.cursor.IterableCursor;

import java.net.URI;

/**
 * A messaging provider that fetches resources from SMS
 */
public class SMSMessagingProvider extends MessagingProvider {
    public static final String[] SMS_COLUMNS_MESSAGE_PROJECTION = {
            BaseColumns._ID,//ReflectMessage.id
            Telephony.TextBasedSmsColumns.ADDRESS,//ReflectMessage.receiverUri
            Telephony.TextBasedSmsColumns.BODY,//ReflectMessage.body
            Telephony.TextBasedSmsColumns.DATE_SENT,//ReflectMessage.sentTimestamp
            Telephony.TextBasedSmsColumns.DATE,//ReflectMessage.receivedTimestamp
            Telephony.TextBasedSmsColumns.READ,//ReflectMessage.read
            Telephony.TextBasedSmsColumns.SEEN//ReflectMessage.seen
    };

    @Override
    public ReflectMessage fetchMessage(URI uri, Context context) throws InvalidUriException {
        MessagingUriUtils.CheckForValidMessagingUri(uri);

        try {
            long messageId = Long.parseLong(uri.getPath().substring(1));
            Uri messageUri = ContentUris.withAppendedId(Telephony.Sms.Inbox.CONTENT_URI, messageId);

            Cursor cursor = context.getContentResolver().query(messageUri,
                    SMS_COLUMNS_MESSAGE_PROJECTION,
                    null, null, null);

            IterableCursor<ReflectMessage> reflectMessages = new ReflectMessage.SmsCursor(context, cursor);

            if(reflectMessages.getCount() < 1){
                return null;
            }

            if(reflectMessages.getCount() > 1){
                throw new InvalidUriException("Uri points to more than one message", uri.toString());
            }

            reflectMessages.moveToFirst();
            return reflectMessages.peek();
        } catch(NumberFormatException e){
            throw new InvalidUriException("The provided message id was not valid", uri.toString());
        }
    }

    @Override
    public ReflectConversation fetchConversation(URI uri, Context context) throws InvalidUriException {
        return null;
    }

    @Override
    public ReflectContact fetchContact(URI uri, Context context) throws InvalidUriException {
        return null;
    }
}
