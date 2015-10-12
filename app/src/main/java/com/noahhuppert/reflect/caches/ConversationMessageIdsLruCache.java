package com.noahhuppert.reflect.caches;

import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.utils.LruCacheUtils;

public class ConversationMessageIdsLruCache extends LruCache<String, String[]> {
    public ConversationMessageIdsLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }
}
