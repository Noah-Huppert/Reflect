package com.noahhuppert.reflect.caches;

import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.messaging.models.Message;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class MessageLruCache extends LruCache<String, Message> {
    public MessageLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }
}
