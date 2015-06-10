package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

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
     * @param id The id of the message
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void getMessage(String id, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Gets a conversation from a messaging resource
     * @param id The id of the conversation
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void getConversation(String id, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Gets a contact from a messaging resource
     * @param id The id of the contact
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void getContact(String id, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);

    /**
     * Gets all conversation ids
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void getConversationIds(Context context, ThreadResultHandler<String[]> threadResultHandler);

    /* Push */
    /**
     * Creates a message
     * @param reflectMessage The {@link ReflectMessage} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void createMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler);

    /**
     * Creates a conversation
     * @param reflectConversation The {@link ReflectConversation} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void createConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler);

    /**
     * Creates a contact
     * @param reflectContact The {@link ReflectContact} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     */
    void createContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler);
}
