package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.net.URI;

/**
 * A interface to provide a framework for fetching messaging resources.
 *
 * It is important to note that a MessagingProvider should be stateless. This is because a new
 * instance of a MessagingProvider can and will be created every time it needs to be used.
 */
public interface MessagingProvider {
    /* Fetch */
    /**
     * Gets a message from a messaging resource
     * @param uri The URI of the message
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidUriException;

    /**
     * Gets a conversation from a messaging resource
     * @param uri The URI of the conversation
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidUriException;

    /**
     * Gets a contact from a messaging resource
     * @param uri The URI of the contact
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidUriException;

    /* Push */
    /**
     * Pushes a message to a messaging resource
     * @param reflectMessage The {@link ReflectMessage} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Pushes a conversation to a messaging resource
     * @param reflectConversation The {@link ReflectConversation} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Pushes a contact to a messaging resource
     * @param reflectContact The {@link ReflectContact} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);
}
