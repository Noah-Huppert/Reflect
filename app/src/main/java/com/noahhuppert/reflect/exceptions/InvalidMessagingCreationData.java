package com.noahhuppert.reflect.exceptions;

/**
 * A exception thrown when the data for a {@link com.noahhuppert.reflect.messaging.providers.MessagingProvider}
 * push method does not contain valid data
 */
public class InvalidMessagingCreationData extends DetailedException {
    public InvalidMessagingCreationData(String reason, String input) {
        super(reason, input);
    }
}
