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
    /* Fetch */
    /**
     * Gets a message from a messaging resource
     * @param uri The URI of the message
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Gets a conversation from a messaging resource
     * @param uri The URI of the conversation
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Gets a contact from a messaging resource
     * @param uri The URI of the contact
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);

    /* Push */
    /**
     * Pushes a message to a messaging resource
     * @param reflectMessage The {@link ReflectMessage} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Pushes a conversation to a messaging resource
     * @param reflectConversation The {@link ReflectConversation} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Pushes a contact to a messaging resource
     * @param reflectContact The {@link ReflectContact} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    public abstract void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);
}
