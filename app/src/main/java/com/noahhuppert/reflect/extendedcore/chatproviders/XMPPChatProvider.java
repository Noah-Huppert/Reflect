package com.noahhuppert.reflect.extendedcore.chatproviders;

import com.noahhuppert.reflect.core.ChatProvider;

import java.util.List;

public class XMPPChatProvider extends ChatProvider {
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
