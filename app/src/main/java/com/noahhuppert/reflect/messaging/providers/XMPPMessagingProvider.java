package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;

import java.net.URI;

/**
 * A messaging provider that fetches resources from XMPP
 */
public class XMPPMessagingProvider extends MessagingProvider {
    public static final int DEFAULT_XMPP_PORT = 5222;

    @Override
    public ReflectMessage fetchMessage(URI uri, Context context) throws InvalidUriException {
        return null;
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
