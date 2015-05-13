package com.noahhuppert.reflect.exceptions;

/**
 * The same as a {@link DetailedException} but in the form of a RuntimeException
 */
public abstract class DetailedRuntimeException extends RuntimeException {
    /**
     * The reason that the exception was thrown
     */
    private final String reason;

    /**
     * The input that caused the exception to be thrown
     */
    private final String input;

    /**
     * Creates a DetailedRuntimeException and assembles the {@link com.noahhuppert.reflect.exceptions.DetailedRuntimeException#reason}
     * and {@link com.noahhuppert.reflect.exceptions.DetailedRuntimeException#input} into a readable form
     * @param reason {@link com.noahhuppert.reflect.exceptions.DetailedRuntimeException#reason}
     * @param input {@link com.noahhuppert.reflect.exceptions.DetailedRuntimeException#input}
     */
    public DetailedRuntimeException(String reason, String input){
        super(reason + ": " + input);
        this.reason = reason;
        this.input = input;
    }

    /* Getters */
    public String getReason() {
        return reason;
    }

    public String getInput() {
        return input;
    }
}
