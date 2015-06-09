package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidMessagingIdException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.util.ArrayList;
import java.util.List;

public class SmsGetConversationRunnable extends ResultHandlerThread<ReflectConversation> {
    private long conversationId;
    private Context context;

    public SmsGetConversationRunnable(long conversationId, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
        super(threadResultHandler);
        this.conversationId = conversationId;
        this.context = context;
    }

    @Override
    protected ReflectConversation execute() throws Exception {
        Cursor cursor = null;

        try{
            String getConversationQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
            String[] getConversationQueryArgs = new String[]{conversationId + ""};

            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                        SmsMessagingProvider.SMS_CONVERSATION_PROJECTION,
                        getConversationQuery,
                        getConversationQueryArgs, null);
            }

            if(cursor.getCount() == 0){
                throw new InvalidMessagingIdException("The provided conversation id does not point to any conversations", conversationId + "");
            }

            if(cursor.getCount() > 1){
                throw new InvalidMessagingIdException("The provided conversation id points to more than one conversation", conversationId + "");
            }

            return reflectConversationFromCursor(cursor);
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private ReflectConversation reflectConversationFromCursor(Cursor cursor){
        ReflectConversation reflectConversation = new ReflectConversation();

        cursor.moveToFirst();

        String id = cursor.getString(0);
        String snippet = cursor.getString(1);
        List<String> messageIds = getConversationMessageIds();

        reflectConversation.setId(id);
        reflectConversation.setProtocol(CommunicationType.SMS);
        reflectConversation.setSnippet(snippet);
        reflectConversation.setMessageIds(messageIds);

        return reflectConversation;
    }

    private List<String> getConversationMessageIds(){
        Cursor cursor = null;
        List<String> messageIds = new ArrayList<>();

        try{
            String[] getConversationMessagesProjection = {BaseColumns._ID};

            String getMessagesQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
            String[] getMessagesQueryArgs = {conversationId + ""};

            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                        getConversationMessagesProjection,
                        getMessagesQuery,
                        getMessagesQueryArgs,
                        null);
            }

            if(cursor.getCount() == 0){
                return messageIds;
            }

            while (cursor.moveToNext()){
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
