package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A messaging provider that fetches resources from XMPP
 */
public class XMPPMessagingProvider extends MessagingProvider {
    public static final int DEFAULT_XMPP_PORT = 5222;

    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
    }
}
