package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderFetchRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;
import com.venmo.cursor.IterableCursor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * A runnable thread that fetches a conversation based on the provided URI
 */
public class SmsFetchConversationRunnable extends MessagingProviderFetchRunnable<ReflectConversation> {
    public SmsFetchConversationRunnable(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
        super(uri, context, threadResultHandler);
    }

    @Override
    public ReflectConversation execute() throws Exception {
        synchronized (uri){
            MessagingUriUtils.CheckForValidMessagingUri(uri);
        }

        Cursor cursor = null;

        try{
            long conversationId;
            synchronized (uri){
                conversationId = Long.parseLong(uri.getPath().substring(1));
            }

            String getConversationQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
            String[] getConversationQueryArgs = new String[]{"" + conversationId};
            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                        SmsMessagingProvider.SMS_CONVERSATION_PROJECTION,
                        getConversationQuery,
                        getConversationQueryArgs, null);
            }

            if(cursor == null){
                synchronized (uri){
                    throw new InvalidUriException("Uri does not point to any conversations", uri.toString());
                }
            }

            IterableCursor<ReflectConversation> reflectConversations = new ReflectConversation.SmsCursor(cursor);
            if(reflectConversations.getCount() < 1){
                synchronized (uri){
                    throw new InvalidUriException("Uri does not point to any conversations", uri.toString());
                }
            }

            if(reflectConversations.getCount() > 1){
                synchronized (uri){
                    throw  new InvalidUriException("Uri points to more than one conversation", uri.toString());
                }
            }

            reflectConversations.moveToFirst();
            ReflectConversation reflectConversation = reflectConversations.peek();
            reflectConversation.setMessageIds(getConversationMessageIds(conversationId));

            return reflectConversation;
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private List<String> getConversationMessageIds(long conversationId){
        Cursor cursor = null;
        List<String> messageIds = new ArrayList<>();

        try {
            String[] getMessagesProjection = new String[]{BaseColumns._ID};
            String getMessagesQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
            String[] getMessagesQueryArgs = new String[]{conversationId + ""};

            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                        getMessagesProjection,
                        getMessagesQuery,
                        getMessagesQueryArgs,
                        null);
            }

            if(cursor == null){
                return messageIds;
            }

            if(cursor.getCount() < 1){
                return  messageIds;
            }

            while(cursor.moveToNext()){
                messageIds.add(cursor.getString(0));
            }

            return messageIds;
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
