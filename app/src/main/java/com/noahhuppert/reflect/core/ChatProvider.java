package com.noahhuppert.reflect.core;

import java.util.List;

/**
 * An abstract class that handles getting messages from messaging providers
 */
public abstract class ChatProvider {
    /**
     * Retrieves conversations from source and makes sure they are stored in the database
     * @return List of conversation ids that are stored in the database
     */
    public abstract List<String> fetchConversations();

    /**
     * Retrieves messages from source and makes sure they are stored in the database
     * @param conversationId The id of the conversation to get messages for
     * @return List of message ids that are stored in the database
     */
    public abstract List<String> fetchConversationMessages(String conversationId);

    /**
     * Retrieves a message from the provider and saves it in the database
     * @param providerMessageId The id of the message on the provider side
     * @return The id of the message in the database
     */
    public abstract String fetchMessage(String providerMessageId);

    /**
     * Sends a message through the source
     * @param conversationId The id of the conversation that the message should belong to
     * @param contactId The id of the contact to send it to
     * @param content The content of the message
     * @return The id of the newly created message
     */
    public abstract String sendMessage(String conversationId, String contactId, String content);
}
