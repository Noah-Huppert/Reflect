package com.noahhuppert.reflect.caches;

import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class ConversationLruCache extends LruCache<String, Conversation> {
    public ConversationLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }
}
