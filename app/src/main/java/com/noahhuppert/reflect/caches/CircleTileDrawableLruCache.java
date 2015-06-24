package com.noahhuppert.reflect.caches;

import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class CircleTileDrawableLruCache extends LruCache<Object[], Drawable> {
    private static CircleTileDrawableLruCache instance;

    public static CircleTileDrawableLruCache getInstance(){
        if(instance == null){
            instance = new CircleTileDrawableLruCache();
        }

        return instance;
    }

    private CircleTileDrawableLruCache() {
        super(LruCacheUtils.GetOptimalCacheSize());
    }

    @WorkerThread
    public Drawable get(String key, String text){
        Object[] keyPair = new Object[]{
                text,
                ColorGenerator.MATERIAL.getColor(key)
        };

        return get(keyPair);
    }

    @WorkerThread
    @Override
    protected Drawable create(Object[] keys) {
        return TextDrawable.builder().buildRound(
                (String) keys[0],
                (int) keys[1]
        );
    }
}
