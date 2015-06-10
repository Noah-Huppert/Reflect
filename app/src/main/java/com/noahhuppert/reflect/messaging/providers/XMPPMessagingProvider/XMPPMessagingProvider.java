package com.noahhuppert.reflect.messaging.providers.XMPPMessagingProvider;

import android.content.Context;

import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

/**
 * A messaging provider that fetches resources from XMPP
 */
public class XMPPMessagingProvider implements MessagingProvider {
    public static final int DEFAULT_XMPP_PORT = 5222;

    /* Fetch */
    @Override
    public void getMessage(String id, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }

    @Override
    public void getConversation(String id, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
    }

    @Override
    public void getContact(String id, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
    }

    @Override
    public void getConversationIds(Context context, ThreadResultHandler<String[]> threadResultHandler) {

    }

    /* Push */
    @Override
    public void createMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }

    @Override
    public void createConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {

    }

    @Override
    public void createContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {

    }
}
