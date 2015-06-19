package com.noahhuppert.reflect.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.WTFException;

public class SmsThreadUtils {
    public static long getOrCreateThreadId(Context context, String otherPartyPhoneNumber){
        Uri getThreadIdUri = Uri.parse("content://mms-sms/threadID").buildUpon()
                .appendQueryParameter("recipient", otherPartyPhoneNumber)
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
                    "Message Sender: " + otherPartyPhoneNumber);
        }

        getThreadIdCursor.moveToFirst();

        try {
            return getThreadIdCursor.getLong(0);
        } finally {
            getThreadIdCursor.close();
        }
    }
}
