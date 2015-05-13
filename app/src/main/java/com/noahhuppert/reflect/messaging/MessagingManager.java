package com.noahhuppert.reflect.messaging;

import android.content.Context;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.messaging.providers.XMPPMessagingProvider.XMPPMessagingProvider;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;

/**
 * A class that brings together the various {@link MessagingProvider}s
 */
public class MessagingManager implements MessagingProvider{
    private static MessagingManager ourInstance = new MessagingManager();

    public static MessagingManager getInstance() {
        return ourInstance;
    }

    private MessagingManager() {
    }

    /* MessagingProvider Fetch */
    @Override
    public void fetchMessage(URI uri, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidUriException {
        getMessagingProviderFromUri(uri).fetchMessage(uri, context, threadResultHandler);
    }

    @Override
    public void fetchConversation(URI uri, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidUriException {
        getMessagingProviderFromUri(uri).fetchConversation(uri, context, threadResultHandler);
    }

    @Override
    public void fetchContact(URI uri, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidUriException {
        getMessagingProviderFromUri(uri).fetchContact(uri, context, threadResultHandler);
    }

    /* MessagingProvider Push */
    @Override
    public void pushMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidMessagingProviderPushData {
        if(reflectMessage.getProtocol() == null){
            throw new InvalidMessagingProviderPushData("ReflectMessage.protocol can not be null", reflectMessage.toString());
        }

        MessagingProviders.GetMessagingProviderForCommunicationType(reflectMessage.getProtocol()).pushMessage(reflectMessage, context, threadResultHandler);
    }

    @Override
    public void pushConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidMessagingProviderPushData {
        if(reflectConversation.getProtocol() == null){
            throw new InvalidMessagingProviderPushData("ReflectConversation.protocol can not be null", reflectConversation.toString());
        }

        MessagingProviders.GetMessagingProviderForCommunicationType(reflectConversation.getProtocol()).pushConversation(reflectConversation, context, threadResultHandler);
    }

    @Override
    public void pushContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidMessagingProviderPushData {
        if(reflectContact.getProtocol() == null){
            throw new InvalidMessagingProviderPushData("ReflectContact.protocol can not be null", reflectContact.toString());
        }

        MessagingProviders.GetMessagingProviderForCommunicationType(reflectContact.getProtocol()).pushContact(reflectContact, context, threadResultHandler);
    }

    /* Messaging Providers */
    public enum MessagingProviders{
        SMS(new SmsMessagingProvider(), CommunicationType.SMS),
        XMPP(new XMPPMessagingProvider(), CommunicationType.XMPP);

        private final MessagingProvider messagingProvider;
        private final CommunicationType communicationType;

        MessagingProviders(MessagingProvider messagingProvider, CommunicationType communicationType){
            this.messagingProvider = messagingProvider;
            this.communicationType = communicationType;
        }

        /* Getters */
        public static MessagingProvider GetMessagingProviderForCommunicationType(CommunicationType communicationType){
            for(MessagingProviders messagingProviders : values()){
                if(messagingProviders.getCommunicationType().equals(communicationType)){
                    return messagingProviders.getMessagingProvider();
                }
            }

            return null;
        }

        public MessagingProvider getMessagingProvider() {
            return messagingProvider;
        }

        public CommunicationType getCommunicationType() {
            return communicationType;
        }
    }

    /* Getters */
    public MessagingProvider getMessagingProviderFromUri(URI uri) throws InvalidUriException{
        CommunicationType communicationType = MessagingUriUtils.GetResourceProvider(uri);
        MessagingProvider messagingProvider = MessagingProviders.GetMessagingProviderForCommunicationType(communicationType);

        return messagingProvider;
    }
}
