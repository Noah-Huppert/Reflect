package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.threading.StreamingResultHandler;

/**
 * A Messaging Provider is a class that performs operations on a messaging resource(Like SMS or XMPP)
 * to get data like conversationIds, or individual messages
 */
public interface MessagingProvider {
    /**
     * Retrieves a list of conversation ids, sorted from most to least recent
     * @param context Application context
     * @return An array of conversation ids
     */
    @WorkerThread
    @NonNull String[] getConversationIds(@NonNull final Context context);

    /**
     * Retrieves a single conversation which is specified by the {@code conversationId}
     * @param context Application context
     * @param conversationId The id of the conversation to get
     * @return The specified conversation
     */
    @WorkerThread
    @Nullable Conversation getConversation(@NonNull final Context context, @NonNull String conversationId);

    /**
     * Retrieves a contact
     * @param context Application context
     * @param uri Uri of the contact
     * @return The specified contact
     */
    @WorkerThread
    @NonNull Contact getContactFromUri(@NonNull final Context context, @NonNull Uri uri);
}
