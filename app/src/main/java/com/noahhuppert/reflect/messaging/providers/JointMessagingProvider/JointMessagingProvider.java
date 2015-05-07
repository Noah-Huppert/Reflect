package com.noahhuppert.reflect.messaging.providers.JointMessagingProvider;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A messaging provider that fetches resources from both SMS and XMPP
 */
public class JointMessagingProvider extends MessagingProvider {
    /* Fetch */
    /**
     * DO NOT USE:
     *      Marked as Deprecated to prevent usage
     *
     * Normally fetches a message from a messaging provider.
     *
     * However the JointMessagingProvider is a little different because it takes messages from SMS
     * and XMPP and combines them. This means you can not fetch a message that is both XMPP and SMS.
     *
     * @param uri The URI of the message
     * @param context
     * @param threadResultHandler
     * @throws InvalidUriException Always
     */
    @Override
    @Deprecated
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        //throw new InvalidUriException("Can not fetch a message with provider " + MessagingUriResourceProvider.JOINT, uri.toString());
    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {

    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {

    }

    /* Push */
    @Override
    public void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {

    }

    @Override
    public void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {

    }

    @Override
    public void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {

    }
}
