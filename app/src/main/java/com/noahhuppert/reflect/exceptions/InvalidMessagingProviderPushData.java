package com.noahhuppert.reflect.exceptions;

/**
 * A exception thrown when the data for a {@link com.noahhuppert.reflect.messaging.providers.MessagingProvider}
 * push method does not contain valid data
 */
public class InvalidMessagingProviderPushData extends DetailedException {
    public InvalidMessagingProviderPushData(String reason, String input) {
        super(reason, input);
    }
}
