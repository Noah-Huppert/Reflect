package com.noahhuppert.reflect.utils;

public class LruCacheUtils {
    public static int GetOptimalCacheSize(){
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        return (int) maxMemory / 8;
    }
}
