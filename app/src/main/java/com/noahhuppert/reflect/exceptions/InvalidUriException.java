package com.noahhuppert.reflect.exceptions;

/**
 * An exception signifying that something is wrong with the URI provided
 */
public class InvalidUriException extends DetailedException {
    public InvalidUriException(String reason, String input) {
        super(reason, input);
    }
}
