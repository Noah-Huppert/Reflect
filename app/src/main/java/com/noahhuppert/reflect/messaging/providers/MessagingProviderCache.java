package com.noahhuppert.reflect.messaging.providers;

import android.support.v4.util.SimpleArrayMap;

import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

public class MessagingProviderCache {
    private static MessagingProviderCache instance;

    private SimpleArrayMap<String, MessagingProvider> messagingProviderCacheMap;

    private static MessagingProviderCache getInstance(){
        if(instance == null){
            instance = new MessagingProviderCache();
        }

        return instance;
    }

    private MessagingProviderCache(){}

    /* Getters */
    private SimpleArrayMap<String, MessagingProvider> getMessagingProviderCacheMap(){
        if(messagingProviderCacheMap == null){
            messagingProviderCacheMap = new SimpleArrayMap<>();
        }

        return messagingProviderCacheMap;
    }

    public static MessagingProvider get(@CommunicationType String key){
        if(!getInstance().getMessagingProviderCacheMap().containsKey(key)){
            if(key.equals(CommunicationType.SMS)) {
                getInstance().getMessagingProviderCacheMap().put(key, new SmsMessagingProvider());
            } else {
                throw new WTFException("Every CommunicationType must have a MessagingProvider mapped to it", key);
            }
        }

        return getInstance().getMessagingProviderCacheMap().get(key);
    }
}
