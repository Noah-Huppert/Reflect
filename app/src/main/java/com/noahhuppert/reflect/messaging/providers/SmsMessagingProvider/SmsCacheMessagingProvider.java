package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import com.noahhuppert.reflect.messaging.providers.CacheMessageProvider;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;

public class SmsCacheMessagingProvider extends CacheMessageProvider {
    public SmsCacheMessagingProvider() {
        super(new SmsMessagingProvider());
    }
}
