package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.noahhuppert.reflect.utils.UriUtils;

import java.net.URI;
import java.util.Collection;

import javax.validation.constraints.NotNull;

/**
 * A singleton class used to receive and handle intents
 */
public class IntentReceiver extends BroadcastReceiver{
    /**
     * A Map of {@link IntentFilter}s linked to {@link IntentHandler}s
     */
    private Multimap<IntentFilter, IntentHandler> intentHandlersMap;

    private static IntentReceiver ourInstance = new IntentReceiver();

    public static IntentReceiver getInstance() {
        return ourInstance;
    }

    private IntentReceiver() {
        intentHandlersMap = ArrayListMultimap.create();
    }

    /* Actions */
    @Override
    public void onReceive(Context context, Intent intent) {
        URI uri = UriUtils.ToJavaUri(intent.getData());
        String action = intent.getAction();

        IntentFilter intentFilter = new IntentFilter(action, uri);

        Collection<IntentHandler> intentHandlers = intentHandlersMap.get(intentFilter);

        for(IntentHandler intentHandler : intentHandlers){
            intentHandler.onReceive(intent, context, uri);
        }
    }

    /* Setters */
    public void RegisterIntentHandler(@NotNull IntentFilter intentFilter, @NotNull IntentHandler intentHandler){
        intentHandlersMap.put(intentFilter, intentHandler);
    }

    public void RemoveIntentHandler(@NotNull IntentFilter intentFilter, @NotNull IntentHandler intentHandler){
        intentHandlersMap.remove(intentFilter,  intentHandler);
    }
}
