package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.ReflectContact;
import com.noahhuppert.reflect.messaging.ReflectConversation;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.uri.MessagingUriResourceProvider;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A singleton class to automatically map messaging scheme URIs to messaging providers
 */
public class MessagingProviderManager {
    private static final String TAG = MessagingProviderManager.class.getSimpleName();

    /**
     * The singleton instance of the MessagingProviderManager
     */
    private static MessagingProviderManager ourInstance;

    private Map<MessagingUriResourceProvider, MessagingProvider> messagingProviders;

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

    /**
     * A private constructor to prevent use of manager outside of the singleton
     */
    private MessagingProviderManager(){
        messagingProviders = new HashMap<>();

        getMessagingProviders().put(MessagingUriResourceProvider.SMS, new SMSMessagingProvider());
        getMessagingProviders().put(MessagingUriResourceProvider.XMPP, new SMSMessagingProvider());
        getMessagingProviders().put(MessagingUriResourceProvider.JOINT, new JointMessagingProvider());
    }

    /* Actions */
    /**
     * Fetches message from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the message to get
     * @return The Message
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public ReflectMessage fetchMessage(URI uri, Context context) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProvider(uri);

        if(messagingProvider == null){
            WTFException wtfException = new WTFException("No messaging provider register for valid resource provider in uri", uri.toString());
            Log.wtf(TAG, wtfException.toString());
            throw wtfException;
        }

        return messagingProvider.fetchMessage(uri, context);
    }

    /**
     * Fetches conversation from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the conversation to get
     * @return The Conversation
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public ReflectConversation fetchConversation(URI uri, Context context) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProvider(uri);

        if(messagingProvider == null){
            WTFException wtfException = new WTFException("No messaging provider register for valid resource provider in uri", uri.toString());
            Log.wtf(TAG, wtfException.toString());
            throw wtfException;
        }

        return messagingProvider.fetchConversation(uri, context);
    }

    /**
     * Fetches contact from URI, automatically finds correct MessagingProvider to use
     * @param uri The URI of the contact to get
     * @return The Contact
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public ReflectContact fetchContact(URI uri, Context context) throws InvalidUriException{
        MessagingProvider messagingProvider = getMessagingProvider(uri);

        if(messagingProvider == null){
            WTFException wtfException = new WTFException("No messaging provider register for valid resource provider in uri", uri.toString());
            Log.wtf(TAG, wtfException.toString());
            throw wtfException;
        }

        return messagingProvider.fetchContact(uri, context);
    }

    /* Getters */
    public Map<MessagingUriResourceProvider, MessagingProvider> getMessagingProviders() {
        return messagingProviders;
    }

    /**
     * Gets the messaging provider for the provided {@link com.noahhuppert.reflect.uri.MessagingUriResourceProvider}
     * @param resourceProvider The resource provider to fetch a messaging provider for
     * @return The messaging provider for the resource provider
     */
    public MessagingProvider getMessagingProvider(MessagingUriResourceProvider resourceProvider){
        return getMessagingProviders().get(resourceProvider);
    }

    /**
     * Gets the messaging provider for the resource provider in the URI
     * @param uri The URI to get the resource provider from
     * @return The messaging provider for the resource provider
     * @throws InvalidUriException Thrown if the URI provided is invalid
     */
    public MessagingProvider getMessagingProvider(URI uri) throws InvalidUriException{
        MessagingUriResourceProvider resourceProvider = MessagingUriUtils.GetResourceProvider(uri);

        return getMessagingProvider(resourceProvider);
    }

    /* Setters */
    public void setMessagingProviders(Map<MessagingUriResourceProvider, MessagingProvider> messagingProviders) {
        this.messagingProviders = messagingProviders;
    }
}
