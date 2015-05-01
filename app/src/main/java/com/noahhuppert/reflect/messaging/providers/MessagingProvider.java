package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A abstract class to provide a framework for fetching messaging resources
 */
public abstract class MessagingProvider {
    /**
     * Gets a message from a messaging resource
     * @param uri The URI of the message
     * @param context The context of the application
     * @param threadResultHandler
     * @return The message
     */
    public abstract void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Gets a conversation from a messaging resource
     * @param uri The URI of the conversation
     * @param context The context of the application
     * @param threadResultHandler
     * @return The conversation
     */
    public abstract void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Gets a contact from a messaging resource
     * @param uri The URI of the contact
     * @param context The context of the application
     * @param threadResultHandler
     * @return The contact
     */
    public abstract void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);
}
