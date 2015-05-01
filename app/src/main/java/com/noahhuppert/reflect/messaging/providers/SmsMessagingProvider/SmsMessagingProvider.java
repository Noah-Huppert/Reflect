package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.provider.BaseColumns;
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
    public static final String[] SMS_COLUMNS_MESSAGE_PROJECTION = {
            BaseColumns._ID,//ReflectMessage.id
            Telephony.TextBasedSmsColumns.ADDRESS,//ReflectMessage.receiverUri
            Telephony.TextBasedSmsColumns.BODY,//ReflectMessage.body
            Telephony.TextBasedSmsColumns.DATE_SENT,//ReflectMessage.sentTimestamp
            Telephony.TextBasedSmsColumns.DATE,//ReflectMessage.receivedTimestamp
            Telephony.TextBasedSmsColumns.READ,//ReflectMessage.read
            Telephony.TextBasedSmsColumns.SEEN//ReflectMessage.seen
    };

    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        SmsFetchMessageRunnable smsFetchMessageRunnable = new SmsFetchMessageRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchMessageRunnable);
    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
        SmsFetchContactRunnable smsFetchContactRunnable = new SmsFetchContactRunnable(uri, context, threadResultHandler);
        MainThreadPool.getInstance().getPool().submit(smsFetchContactRunnable);
    }
}
