package com.noahhuppert.reflect.messaging.providers.XMPPMessagingProvider;

import android.content.Context;

import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A messaging provider that fetches resources from XMPP
 */
public class XMPPMessagingProvider extends MessagingProvider {
    public static final int DEFAULT_XMPP_PORT = 5222;

    /* Fetch */
    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
    }

    /* Push */
    @Override
    public void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }

    @Override
    public void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {

    }

    @Override
    public void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {

    }
}
