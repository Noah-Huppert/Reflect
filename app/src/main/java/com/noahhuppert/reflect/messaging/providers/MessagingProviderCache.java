package com.noahhuppert.reflect.messaging.providers;

import android.support.v4.util.SimpleArrayMap;

import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

public class MessagingProviderCache {
    private static MessagingProviderCache instance;

    private SimpleArrayMap<Class, MessagingProvider> messagingProviderCacheMap;

    private static MessagingProviderCache getInstance(){
        if(instance == null){
            instance = new MessagingProviderCache();
        }

        return instance;
    }

    private MessagingProviderCache(){}

    /* Getters */
    private SimpleArrayMap<Class, MessagingProvider> getMessagingProviderCacheMap(){
        if(messagingProviderCacheMap == null){
            messagingProviderCacheMap = new SimpleArrayMap<>();
        }

        return messagingProviderCacheMap;
    }

    public static MessagingProvider get(Class key){
        if(!getInstance().getMessagingProviderCacheMap().containsKey(key)){
            if(key == SmsMessagingProvider.class) {
                getInstance().getMessagingProviderCacheMap().put(key, new SmsMessagingProvider());
            }
        }

        return getInstance().getMessagingProviderCacheMap().get(key);
    }
}
