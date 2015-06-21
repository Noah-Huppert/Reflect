package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.noahhuppert.reflect.messaging.models.Conversation;

/**
 * A Messaging Provider is a class that performs operations on a messaging resource(Like SMS or XMPP)
 * to get data like conversations, or individual messages
 */
public interface MessagingProvider {
    /**
     * Retrieves a list of conversations from the messaging resource
     * @param context The application context used to perform operations
     * @return All conversations available from the messaging resource
     */
    @WorkerThread
    Conversation[] getConversations(@NonNull Context context);

    @WorkerThread
    @NonNull String getContactDisplayNameForUri(@NonNull Context context, @NonNull Uri contactUri);
}
