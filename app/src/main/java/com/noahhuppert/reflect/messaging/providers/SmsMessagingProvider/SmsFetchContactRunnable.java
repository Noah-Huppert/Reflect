package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderFetchRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;
import com.venmo.cursor.IterableCursor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A runnable thread task that fetches the contact pointed to by the URI
 */
public class SmsFetchContactRunnable extends MessagingProviderFetchRunnable<ReflectContact> {
    private static final String TAG = SmsFetchContactRunnable.class.getSimpleName();

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
            long contactId;
            synchronized (uri){
                contactId = Long.parseLong(uri.getPath().substring(1));
            }


            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            synchronized (context){
               cursor = context.getContentResolver().query(contactUri,
                       SmsMessagingProvider.SMS_CONTACT_PROJECTION,
                       null, null, null);
            }

            if(cursor == null){
                synchronized (uri) {
                    throw new InvalidUriException("Uri does not point to any contacts", uri.toString());
                }
            }

            IterableCursor<ReflectContact> reflectContacts = new ReflectContact.SmsCursor(cursor);
            if(reflectContacts.getCount() < 1){
                synchronized (uri) {
                    throw new InvalidUriException("Uri does not point to any message", uri.toString());
                }
            }
            if(reflectContacts.getCount() > 1){
                synchronized (uri){
                    throw new InvalidUriException("Uri points to more than one message", uri.toString());
                }
            }

            reflectContacts.moveToFirst();
            ReflectContact reflectContact = reflectContacts.peek();

            URI reflectContactUri = getContactUri(reflectContact.getId());
            reflectContact.setUri(reflectContactUri);

            return reflectContact;
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private URI getContactUri(String lookupKey) throws InvalidUriException{
        Cursor cursor;

        synchronized (context){
            cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        }

        if(cursor == null){
            try {
                Log.d(TAG, "NONE");
                return null;
            } finally {
                cursor.close();
            }
        }

        if(cursor.getCount() < 1){
            try {
                Log.d(TAG, "COUNT " + cursor.getCount());
                return null;
            } finally {
                cursor.close();
            }
        }

        cursor.moveToFirst();
        String phoneNumber = cursor.getString(0);

        Log.d(TAG, "PHONE NUMBER " + phoneNumber);

        try {
            return new URI("sms://" + phoneNumber);
        } catch (URISyntaxException e){
            return null;
        } finally{
            cursor.close();
        }
    }
}
