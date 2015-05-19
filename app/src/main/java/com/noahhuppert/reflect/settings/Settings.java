package com.noahhuppert.reflect.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

/**
 * A class that holds and retrieves information about application settings
 */
public class Settings {
    /**
     * A HashMap that stores all Reflect app settings default values
     */
    private HashMap<String, Object> defaultValues;

    /* Settings Keys */
    public static final String KEY_FIRST_TIME_SETUP_COMPLETE = "KEY_FIRST_TIME_SETUP_COMPLETE";

    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
        defaultValues = new HashMap<>();

        /* Register Defaults */
        defaultValues.put(KEY_FIRST_TIME_SETUP_COMPLETE, false);
    }

    /* Getters */
    public static SharedPreferences GetSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getString(String key, Context context){
        String defaultValue = defaultValues.containsKey(key) ? defaultValues.get(key).toString() : "";

        return GetSharedPreferences(context).getString(key, defaultValue);
    }

    public boolean getBoolean(String key, Context context){
        boolean defaultValue = defaultValues.containsKey(key) ? (boolean) defaultValues.get(key) : false;

        return GetSharedPreferences(context).getBoolean(key, defaultValue);
    }
}
