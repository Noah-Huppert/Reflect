package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsGetConversationIdsRunnable extends ResultHandlerThread<String[]> {
    private Context context;

    public SmsGetConversationIdsRunnable(Context context, ThreadResultHandler<String[]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected String[] execute() throws Exception {
        Cursor cursor = null;

        try{
           String[] getConversationsIdProjection = {Telephony.TextBasedSmsColumns.THREAD_ID};

            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                        getConversationsIdProjection,
                        null, null, null);
            }

            String[] conversationIds = new String[cursor.getCount()];

            for(int i = 0; i < conversationIds.length; i++){
                cursor.moveToPosition(i);
                conversationIds[i] = cursor.getString(0);
            }

            return conversationIds;
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
