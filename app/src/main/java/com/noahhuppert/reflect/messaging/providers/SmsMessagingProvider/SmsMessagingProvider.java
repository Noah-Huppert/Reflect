package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A messaging provider that fetches resources from SMS
 */
public class SmsMessagingProvider extends MessagingProvider {
    public static final String[] SMS_MESSAGE_PROJECTION = {
            BaseColumns._ID,//ReflectMessage.id
            Telephony.TextBasedSmsColumns.ADDRESS,//ReflectMessage.receiverUri
            Telephony.TextBasedSmsColumns.BODY,//ReflectMessage.body
            Telephony.TextBasedSmsColumns.DATE_SENT,//ReflectMessage.sentTimestamp
            Telephony.TextBasedSmsColumns.DATE,//ReflectMessage.receivedTimestamp
            Telephony.TextBasedSmsColumns.READ,//ReflectMessage.read
            Telephony.TextBasedSmsColumns.SEEN//ReflectMessage.seen
    };

    public static final String[] SMS_CONTACT_PROJECTION = {
            ContactsContract.Contacts.LOOKUP_KEY,//ReflectContact.id
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,//ReflectContact.displayName
            ContactsContract.Contacts.PHOTO_URI//ReflectContact.avatarUri
    };

    public static final String[] SMS_CONVERSATION_PROJECTION = {
            Telephony.TextBasedSmsColumns.THREAD_ID,//ReflectConversation.id
            Telephony.Sms.Conversations.SNIPPET//ReflectConversation.snippet
    };

    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        SmsFetchMessageRunnable smsFetchMessageRunnable = new SmsFetchMessageRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchMessageRunnable);
    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
        SmsFetchConversationRunnable smsFetchConversationRunnable = new SmsFetchConversationRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchConversationRunnable);
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
        SmsFetchContactRunnable smsFetchContactRunnable = new SmsFetchContactRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchContactRunnable);
    }
}
