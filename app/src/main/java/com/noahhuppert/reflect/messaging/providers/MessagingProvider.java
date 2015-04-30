package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;

import java.net.URI;

/**
 * A abstract class to provide a framework for fetching messaging resources
 */
public abstract class MessagingProvider {
    /**
     * Gets a message from a messaging resource
     * @param uri The URI of the message
     * @param context The context of the application
     * @return The message
     */
    public abstract ReflectMessage fetchMessage(URI uri, Context context) throws InvalidUriException;

    /**
     * Gets a conversation from a messaging resource
     * @param uri The URI of the conversation
     * @param context The context of the application
     * @return The conversation
     */
    public abstract ReflectConversation fetchConversation(URI uri, Context context) throws InvalidUriException;

    /**
     * Gets a contact from a messaging resource
     * @param uri The URI of the contact
     * @param context The context of the application
     * @return The contact
     */
    public abstract ReflectContact fetchContact(URI uri, Context context) throws InvalidUriException;

    /* Interfaces */

    /**
     * A basic
     * @param <ResourceType>
     */
    public static interface FetchResourceHandler<ResourceType>{
        public void onDone(ResourceType resource);
        public void onException(Exception exception);
    }

    public static interface FetchMessageHandler extends FetchResourceHandler<ReflectMessage>{}
    public static interface FetchConversationHandler extends FetchResourceHandler<ReflectConversation>{}
    public static interface FetchContactHandler extends FetchResourceHandler<ReflectContact>{}
}
