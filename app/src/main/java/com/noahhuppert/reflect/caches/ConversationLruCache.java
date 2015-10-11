package com.noahhuppert.reflect.caches;

import android.content.Context;
import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderCache;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class ConversationLruCache extends LruCache<ConversationLruCache.ConversationKey, Conversation> {
    private static final String TAG = ConversationLruCache.class.getSimpleName();

    private static ConversationLruCache instance;

    public static class ConversationKey {
        public String id;
        public @CommunicationType String communicationType;
        public Context context;

        public ConversationKey(String id, @CommunicationType String communicationType, Context context) {
            this.id = id;
            this.communicationType = communicationType;
            this.context = context;
        }
    }

    private ConversationLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }

    public static ConversationLruCache getInstance() {
        if(instance == null) {
            instance = new ConversationLruCache();
        }

        return instance;
    }

    @Override
    protected Conversation create(ConversationKey key) {
        return MessagingProviderCache.get(key.communicationType).getConversation(key.context, key.id);
    }
}
