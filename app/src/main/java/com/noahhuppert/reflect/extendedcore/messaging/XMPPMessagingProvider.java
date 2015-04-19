package com.noahhuppert.reflect.extendedcore.messaging;

import com.noahhuppert.reflect.core.messaging.MessagingProvider;

import java.util.List;

public class XMPPMessagingProvider extends MessagingProvider {
    @Override
    public List<String> fetchConversations() {
        return null;
    }

    @Override
    public List<String> fetchConversationMessages(String conversationId) {
        return null;
    }

    @Override
    public String fetchMessage(String providerMessageId) {
        return null;
    }

    @Override
    public String sendMessage(String conversationId, String contactId, String content) {
        return null;
    }
}
