package com.noahhuppert.reflect.messaging.providers;

import android.support.v4.util.SimpleArrayMap;

import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsCacheMessagingProvider;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

public class MessagingProviderManager {
    private static MessagingProviderManager instance;

    private SimpleArrayMap<String, MessagingProvider> messagingProviderCacheMap;

    private static MessagingProviderManager getInstance(){
        if(instance == null){
            instance = new MessagingProviderManager();
        }

        return instance;
    }

    private MessagingProviderManager(){}

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
                getInstance().getMessagingProviderCacheMap().put(key, new SmsCacheMessagingProvider());
            } else {
                throw new WTFException("Every CommunicationType must have a MessagingProvider mapped to it", key);
            }
        }

        return getInstance().getMessagingProviderCacheMap().get(key);
    }
}
