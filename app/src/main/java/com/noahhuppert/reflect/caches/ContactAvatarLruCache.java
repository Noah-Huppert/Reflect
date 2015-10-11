package com.noahhuppert.reflect.caches;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.noahhuppert.reflect.utils.LruCacheUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContactAvatarLruCache extends LruCache<ContactAvatarLruCache.ContactAvatarKey, Drawable> {
    private static final String TAG = ContactAvatarLruCache.class.getSimpleName();

    private static ContactAvatarLruCache instance;

    public static class ContactAvatarKey {
        public Uri uri;
        public Context context;

        public ContactAvatarKey(@NonNull Uri uri, @NonNull Context context) {
            this.uri = uri;
            this.context = context;
        }
    }

    private ContactAvatarLruCache(){
        super(LruCacheUtils.GetOptimalCacheSize());
    }

    public static ContactAvatarLruCache getInstance(){
        if(instance == null){
            instance = new ContactAvatarLruCache();
        }

        return instance;
    }

    @WorkerThread
    @Override
    protected Drawable create(ContactAvatarLruCache.ContactAvatarKey key) {
        try {
            InputStream inputStream = inputStream = key.context.getContentResolver().openInputStream(key.uri);
            RoundedBitmapDrawable roundedBitmapDrawable = roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(key.context.getResources(), inputStream);


            roundedBitmapDrawable.setCornerRadius(Math.max(roundedBitmapDrawable.getIntrinsicWidth(), roundedBitmapDrawable.getIntrinsicHeight() / 1.25f));
            roundedBitmapDrawable.setAntiAlias(true);

            return roundedBitmapDrawable;
        } catch (FileNotFoundException e){
            return null;
        }
    }
}
