package com.noahhuppert.reflect.caches;

import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.noahhuppert.reflect.utils.LruCacheUtils;

public class CircleTileDrawableLruCache extends LruCache<CircleTileDrawableLruCache.CircleTileDrawableKey, Drawable> {
    private static CircleTileDrawableLruCache instance;

    public static class CircleTileDrawableKey {
        String identifier;
        String text;

        public CircleTileDrawableKey(String identifier, String text) {
            this.identifier = identifier;
            this.text = text;
        }
    }

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
    @Override
    protected Drawable create(CircleTileDrawableKey key) {
        return TextDrawable.builder().buildRound(
                key.text,
                ColorGenerator.MATERIAL.getColor(key.identifier)
        );
    }
}
