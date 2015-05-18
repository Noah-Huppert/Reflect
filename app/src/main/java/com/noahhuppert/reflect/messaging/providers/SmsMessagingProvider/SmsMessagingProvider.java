package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.content.Intent;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.intents.IntentHandler;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A messaging provider that fetches resources from SMS
 */
public class SmsMessagingProvider implements MessagingProvider {
    public static final String SMS_URI_SCHEME = "sms";

    public static final String INTENT_ACTION_MESSAGE_SENT = SmsMessagingProvider.class.getName() + "INTENT_ACTION_MESSAGE_SENT";
    public static final String INTENT_ACTION_MESSAGE_DELIVERED = SmsMessagingProvider.class.getName() + "INTENT_ACTION_MESSAGE_DELIVERED";

    public static final String INTENT_EXTRA_TOTAL_MESSAGE_PARTS = "INTENT_EXTRA_TOTAL_MESSAGE_PARTS";
    public static final String INTENT_EXTRA_MESSAGE_PART = "INTENT_EXTRA_MESSAGE_PART";

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

    /* Fetch */
    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidUriException {
        SmsFetchMessageRunnable smsFetchMessageRunnable = new SmsFetchMessageRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchMessageRunnable);
    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidUriException {
        SmsFetchConversationRunnable smsFetchConversationRunnable = new SmsFetchConversationRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchConversationRunnable);
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidUriException {
        SmsFetchContactRunnable smsFetchContactRunnable = new SmsFetchContactRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchContactRunnable);
    }

    /* Push */
    @Override
    public void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidMessagingProviderPushData {
        SmsPushMessageRunnable smsPushMessageRunnable = new SmsPushMessageRunnable(reflectMessage, context, threadResultHandler, new IntentHandler() {
            @Override
            public void onReceive(Intent intent, Context context, URI uri) {
                Log.d("TAG", intent.getDataString());
            }
        });
        MainThreadPool.getInstance().getPool().submit(smsPushMessageRunnable);
    }

    @Override
    public void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidMessagingProviderPushData {

    }

    @Override
    public void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidMessagingProviderPushData {

    }
}
