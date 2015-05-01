package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;
import com.venmo.cursor.IterableCursor;

import java.net.URI;
import java.util.concurrent.Callable;

public class SmsFetchMessageRunnable extends ResultHandlerThread<ReflectMessage>{
    private URI uri;
    private Context context;

    public SmsFetchMessageRunnable(ThreadResultHandler<ReflectMessage> threadResultHandler, URI uri, Context context) {
        super(threadResultHandler);
        this.uri = uri;
        this.context = context;
    }

    @Override
    public ReflectMessage execute() throws Exception {
        synchronized (uri) {
            MessagingUriUtils.CheckForValidMessagingUri(uri);
        }

        Cursor cursor = null;

        try {
            synchronized (uri) {
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
