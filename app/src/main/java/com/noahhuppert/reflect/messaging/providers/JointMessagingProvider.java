package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.uri.MessagingUriResourceProvider;

import java.net.URI;

/**
 * A messaging provider that fetches resources from both SMS and XMPP
 */
public class JointMessagingProvider extends MessagingProvider {
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
     * @return Nothing because this is an invalid operation
     * @throws InvalidUriException Always
     */
    @Override
    @Deprecated
    public ReflectMessage fetchMessage(URI uri, Context context) throws InvalidUriException {
        throw new InvalidUriException("Can not fetch a message with provider " + MessagingUriResourceProvider.JOINT, uri.toString());
    }

    @Override
    public ReflectConversation fetchConversation(URI uri, Context context) throws InvalidUriException {
        return null;
    }

    @Override
    public ReflectContact fetchContact(URI uri, Context context) throws InvalidUriException {
        return null;
    }
}
