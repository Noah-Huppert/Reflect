package com.noahhuppert.reflect.exceptions;

/**
 * A exception thrown when the provided id for the messaging resource is invalid
 */
public class InvalidIdException extends DetailedException {
    public InvalidIdException(String reason, String input) {
        super(reason, input);
    }
}
