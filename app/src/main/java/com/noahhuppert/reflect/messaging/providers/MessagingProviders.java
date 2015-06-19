package com.noahhuppert.reflect.messaging.providers;

import android.support.v4.util.SimpleArrayMap;

import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.messaging.providers.XMPPMessagingProvider.XMPPMessagingProvider;

/**
 * A class that holds singleton instances of all {@link MessagingProvider}s
 */
@Deprecated
public class MessagingProviders {
    private static SimpleArrayMap<String, MessagingProvider> messagingProvidersCache;

    public static MessagingProvider getMessagingProvider(@CommunicationType String communicationType){
        if(messagingProvidersCache == null){
            messagingProvidersCache = new SimpleArrayMap<>();
        }

        MessagingProvider messagingProvider = null;

        if(communicationType == CommunicationType.SMS){
            if(messagingProvidersCache.containsKey(communicationType)){
                messagingProvider = messagingProvidersCache.get(communicationType);
            } else {
                messagingProvider = new SmsMessagingProvider();
                messagingProvidersCache.put(communicationType, messagingProvider);
            }
        } else if(communicationType == CommunicationType.XMPP){
            if(messagingProvidersCache.containsKey(communicationType)){
                messagingProvider = messagingProvidersCache.get(communicationType);
            } else {
                messagingProvider = new XMPPMessagingProvider();
                messagingProvidersCache.put(communicationType, messagingProvider);
            }
        }

        if(messagingProvider == null){
            throw new WTFException("A messaging provider instance should be created for every communicationType", communicationType + "");
        }

        return messagingProvider;
    }
}
