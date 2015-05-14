package com.noahhuppert.reflect.intents;

import android.content.Context;
import android.content.Intent;

import java.net.URI;

public interface IntentHandler {
    void onReceive(Intent intent, Context context, URI uri);
}
