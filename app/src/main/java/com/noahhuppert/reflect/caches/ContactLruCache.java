package com.noahhuppert.reflect.caches;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class ContactLruCache extends LruCache<Uri, Contact> {
    public ContactLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }
}
