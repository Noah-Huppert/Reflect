package com.noahhuppert.reflect.caches;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.WorkerThread;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.noahhuppert.reflect.utils.LruCacheUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContactAvatarLruCache extends LruCache<Uri, Drawable> {
    private static final String TAG = ContactAvatarLruCache.class.getSimpleName();

    private static ContactAvatarLruCache instance;

    private Context context;

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
    public Drawable get(Uri key, Context context){
        this.context = context;

        try {
            return get(key);
        } finally {
            this.context = null;
        }
    }

    @WorkerThread
    @Override
    protected Drawable create(Uri key) {
        if(context == null){
            Log.e(TAG, "Use ContactAvatarLruCache.get(Uri key, Context) to get item");
            return null;
        }

        try {
            InputStream inputStream = inputStream = context.getContentResolver().openInputStream(key);
            RoundedBitmapDrawable roundedBitmapDrawable = roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), inputStream);


            roundedBitmapDrawable.setCornerRadius(Math.max(roundedBitmapDrawable.getIntrinsicWidth(), roundedBitmapDrawable.getIntrinsicHeight() / 1.25f));
            roundedBitmapDrawable.setAntiAlias(true);

            return roundedBitmapDrawable;
        } catch (FileNotFoundException e){
            return null;
        }
    }
}
