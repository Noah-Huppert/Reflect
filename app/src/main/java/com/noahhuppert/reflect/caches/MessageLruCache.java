package com.noahhuppert.reflect.caches;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Message;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderCache;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class MessageLruCache extends LruCache<MessageLruCache.MessageKey, Message> {
    private static MessageLruCache instance;

    public static class MessageKey {
        public String id;
        public @CommunicationType String communicationType;
        public Context context;

        public MessageKey(String id, @CommunicationType String communicationType) {
            this.id = id;
            this.communicationType = communicationType;
        }
    }

    private MessageLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }

    public static MessageLruCache getInstance() {
        if(instance == null) {
            instance = new MessageLruCache();
        }

        return instance;
    }

    @WorkerThread
    @Override
    protected Message create(MessageKey key) {
        return MessagingProviderCache.get(key.communicationType).getMessage(key.context, key.id);
    }
}
