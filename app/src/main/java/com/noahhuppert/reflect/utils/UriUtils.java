package com.noahhuppert.reflect.utils;

import android.net.Uri;

import java.net.URI;

/**
 * A class holding various helpers for using Uris
 */
public class UriUtils {
    /**
     * Convert a java style {@link URI} into a Android style {@link Uri}
     * @param uri The java style Uri
     * @return The Android style Uri
     */
    public static Uri ToAndroidUri(URI uri){
        return Uri.parse(uri.toString());
    }

    /**
     * Converts an Android style {@link Uri} into a java style {@link URI}
     * @param uri The Android style Uri
     * @return The java style Uri
     */
    public static URI ToJavaUri(Uri uri){
        return URI.create(uri.toString());
    }
}
