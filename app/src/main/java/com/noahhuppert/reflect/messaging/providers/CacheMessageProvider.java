package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.noahhuppert.reflect.caches.ContactLruCache;
import com.noahhuppert.reflect.caches.ConversationLruCache;
import com.noahhuppert.reflect.caches.ConversationMessageIdsLruCache;
import com.noahhuppert.reflect.caches.MessageLruCache;
import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.models.Message;

public class CacheMessageProvider implements MessagingProvider {
    private static final String TAG = CacheMessageProvider.class.getSimpleName();

    private ContactLruCache contactLruCache;
    private ConversationLruCache conversationLruCache;
    private MessageLruCache messageLruCache;
    private ConversationMessageIdsLruCache conversationMessageIdsLruCache;
    private String[] conversationIds;

    private MessagingProvider provider;

    public CacheMessageProvider(MessagingProvider provider) {
        this.provider = provider;

        this.contactLruCache = new ContactLruCache();
        this.conversationLruCache = new ConversationLruCache();
        this.messageLruCache = new MessageLruCache();
        this.conversationMessageIdsLruCache = new ConversationMessageIdsLruCache();
    }

    @NonNull
    @Override
    public String[] getConversationIds(@NonNull Context context) {
        if(conversationIds == null) {
            conversationIds = provider.getConversationIds(context);
        }

        return conversationIds;
    }

    @Nullable
    @Override
    public Conversation getConversation(@NonNull Context context, @NonNull String conversationId) {
        if(conversationLruCache.get(conversationId) == null) {
            conversationLruCache.put(conversationId, provider.getConversation(context, conversationId));
        }

        return conversationLruCache.get(conversationId);
    }

    @NonNull
    @Override
    public Contact getContactFromUri(@NonNull Context context, @NonNull Uri uri) {
        if(contactLruCache.get(uri) == null) {
            contactLruCache.put(uri, provider.getContactFromUri(context, uri));;
        }

        return contactLruCache.get(uri);
    }

    @NonNull
    @Override
    public String[] getConversationMessageIds(@NonNull Context context, @NonNull String conversationId) {
        if(conversationMessageIdsLruCache.get(conversationId) == null) {
            conversationMessageIdsLruCache.put(conversationId, provider.getConversationMessageIds(context, conversationId));
        }

        return conversationMessageIdsLruCache.get(conversationId);
    }

    @Nullable
    @Override
    public Message getMessage(@NonNull Context context, @NonNull String messageId) {
        if(messageLruCache.get(messageId) == null) {
            messageLruCache.put(messageId, provider.getMessage(context, messageId));
        }

        return messageLruCache.get(messageId);
    }
}
