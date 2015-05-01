package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderFetchRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;

/**
 * A runnable thread task that fetches the contact pointed to by the URI
 */
public class SmsFetchContactRunnable extends MessagingProviderFetchRunnable<ReflectContact> {
    public SmsFetchContactRunnable(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
        super(uri, context, threadResultHandler);
    }

    @Override
    public ReflectContact execute() throws Exception {
        synchronized (uri){
            MessagingUriUtils.CheckForValidMessagingUri(uri);
        }

        Cursor cursor = null;

        try{
            synchronized (uri){
                long contactId = Long.parseLong(uri.getPath().substring(1));
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, contactId);

                cursor = co
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
