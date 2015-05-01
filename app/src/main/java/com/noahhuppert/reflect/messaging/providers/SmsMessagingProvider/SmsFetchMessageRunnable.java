package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderFetchRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;
import com.venmo.cursor.IterableCursor;

import java.net.URI;

/**
 * A runnable thread task that gets the message pointed to by the URI
 */
public class SmsFetchMessageRunnable extends MessagingProviderFetchRunnable<ReflectMessage> {
    public SmsFetchMessageRunnable(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        super(uri, context, threadResultHandler);
    }

    @Override
    public ReflectMessage execute() throws Exception {
        synchronized (uri) {
            MessagingUriUtils.CheckForValidMessagingUri(uri);
        }

        Cursor cursor = null;

        try {
            //TODO FIGURE OUT SHIT
            synchronized (uri, context) {
                long messageId = Long.parseLong(uri.getPath().substring(1));
                Uri messageUri = ContentUris.withAppendedId(Telephony.Sms.Inbox.CONTENT_URI, messageId);

                cursor = context.getContentResolver().query(messageUri,
                        SmsMessagingProvider.SMS_COLUMNS_MESSAGE_PROJECTION,
                        null, null, null);

                if(cursor == null){
                    throw new InvalidUriException("Uri does not point to any messages", uri.toString());
                }

                IterableCursor<ReflectMessage> reflectMessages = new ReflectMessage.SmsCursor(context, cursor);

                if (reflectMessages.getCount() < 1) {
                    throw new InvalidUriException("Uri does not point to any messages", uri.toString());
                }

                if (reflectMessages.getCount() > 1) {
                    throw new InvalidUriException("Uri points to more than one message", uri.toString());
                }

                reflectMessages.moveToFirst();
                return reflectMessages.peek();
            }
        } catch(NumberFormatException e){
            throw new InvalidUriException("The provided message id was not valid", uri.toString());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
