package com.noahhuppert.reflect.exceptions;

/**
 * A "What a Terrible Failure" exception. Used in cases where a condition should never happen
 */
public class WTFException extends DetailedRuntimeException {
    public WTFException(String reason, String input) {
        super(reason, input);
    }
}
