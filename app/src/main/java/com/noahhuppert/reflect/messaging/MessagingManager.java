package com.noahhuppert.reflect.messaging;

import com.noahhuppert.reflect.messaging.providers.MessagingProvider;

public class MessagingManager implements MessagingProvider{
    private static MessagingManager ourInstance = new MessagingManager();

    public static MessagingManager getInstance() {
        return ourInstance;
    }

    private MessagingManager() {
    }
}
