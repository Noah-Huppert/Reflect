package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.messaging.providers.XMPPMessagingProvider.XMPPMessagingProvider;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;

/**
 * A singleton class to automatically map messaging scheme URIs to messaging providers
 *
 * This class is now deprecated and will be replace by {@link com.noahhuppert.reflect.messaging.MessagingManager}
 * This is because of the recent design realzation that {@link MessagingProvider}s are stateless.
 * Therefor a singleton copy of each does not need to be maintained
 */
@Deprecated
public class MessagingProviderManager{
    private static final String TAG = MessagingProviderManager.class.getSimpleName();

    /**
     * The singleton instance of the MessagingProviderManager
     */
    private static MessagingProviderManager ourInstance;

    /**
     * A static method to retrieve the instance of a MessagingProviderManager
     * @return The singleton instance of the MessagingProviderManager
     */
    public static MessagingProviderManager getInstance(){
        if(ourInstance == null){
            ourInstance = new MessagingProviderManager();
        }

        return ourInstance;
    }

    /* Fetch */
    /**
     * Fetches message from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the message to get
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProviderInstance(uri);

        if(messagingProvider == null){
            throw new InvalidUriException("The Uri provided does not contain a valid MessagingProvider", uri.toString());
        }

        messagingProvider.fetchMessage(uri, context, threadResultHandler);
    }

    /**
     * Fetches contact from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the contact to get
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProviderInstance(uri);

        if(messagingProvider == null){
            throw new InvalidUriException("The Uri provided does not contain a valid resource provider", uri.toString());
        }

        messagingProvider.fetchContact(uri, context, threadResultHandler);
    }

    /**
     * Fetches conversation from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the conversation to get
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProviderInstance(uri);

        if(messagingProvider == null){
            throw new InvalidUriException("The Uri provided does not contain a valid resource provider", uri.toString());
        }

        messagingProvider.fetchConversation(uri, context, threadResultHandler);
    }

    /* Push */
    /**
     * Pushes a message to a messaging resource, automatically finds correct MessagingProvider to use
     * @param reflectMessage The {@link ReflectMessage} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidMessagingProviderPushData Thrown if the provided data has an invalid {@link CommunicationType}
     */
    public void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidMessagingProviderPushData{
        MessagingProvider messagingProvider = getMessagingProviderInstance(reflectMessage.getProtocol());

        if(messagingProvider == null){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided does not contain a valid resource provider", reflectMessage.toString());
        }

        messagingProvider.pushMessage(reflectMessage, context, threadResultHandler);
    }

    /**
     * Pushes a conversation to a messaging resource, automatically finds correct MessagingProvider to use
     * @param reflectConversation The {@link ReflectConversation} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidMessagingProviderPushData Thrown if the provided data has an invalid {@link CommunicationType}
     */
    public void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidMessagingProviderPushData{
        MessagingProvider messagingProvider = getMessagingProviderInstance(reflectConversation.getProtocol());

        if(messagingProvider == null){
            throw new InvalidMessagingProviderPushData("The ReflectConversation provided does not contain a valid resource provider", reflectConversation.toString());
        }

        messagingProvider.pushConversation(reflectConversation, context, threadResultHandler);
    }

    /**
     * Pushes a contact to a messaging resource, automatically finds correct MessagingProvider to use
     * @param reflectContact The {@link ReflectContact} to push to the messaging resource
     * @param context The context of the application
     * @param threadResultHandler The result handler used to communicate between threads
     * @throws InvalidMessagingProviderPushData Thrown if the provided data has an invalid {@link CommunicationType}
     */
    public void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidMessagingProviderPushData{
        MessagingProvider messagingProvider = getMessagingProviderInstance(reflectContact.getProtocol());

        if(messagingProvider == null){
            throw new InvalidMessagingProviderPushData("The ReflectContact provided does not contain a valid resource provider", reflectContact.toString());
        }

        messagingProvider.pushContact(reflectContact, context, threadResultHandler);
    }

    /* Getters */
    /**
     * Gets the messaging provider for the provided {@link CommunicationType}
     * @param resourceProvider The resource provider to fetch a messaging provider for
     * @return The messaging provider for the resource provider
     */
    public MessagingProvider getMessagingProviderInstance(CommunicationType resourceProvider){
        if(resourceProvider.equals(CommunicationType.SMS)){
            return new SmsMessagingProvider();
        } else if(resourceProvider.equals(CommunicationType.XMPP)){
            return new XMPPMessagingProvider();
        }

        return null;
    }

    /**
     * Gets the messaging provider for the resource provider in the URI
     * @param uri The URI to get the resource provider from
     * @return The messaging provider for the resource provider
     * @throws InvalidUriException Thrown if the URI provided is invalid
     */
    public MessagingProvider getMessagingProviderInstance(URI uri) throws InvalidUriException{
        CommunicationType resourceProvider = MessagingUriUtils.GetResourceProvider(uri);

        return getMessagingProviderInstance(resourceProvider);
    }
}
