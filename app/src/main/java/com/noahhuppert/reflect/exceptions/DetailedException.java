package com.noahhuppert.reflect.exceptions;

/**
 * An exception that provides context as to why the exception was thrown. This is done by showing
 * the user the input that caused the exception
 */
public class DetailedException extends Exception {
    /**
     * The reason that the exception was thrown
     */
    private final String reason;

    /**
     * The input that caused the exception to be thrown
     */
    private final String input;

    /**
     * Creates a DetailedException and assembles the {@link com.noahhuppert.reflect.exceptions.DetailedException#reason}
     * and {@link com.noahhuppert.reflect.exceptions.DetailedException#input} into a readable form
     * @param reason {@link com.noahhuppert.reflect.exceptions.DetailedException#reason}
     * @param input {@link com.noahhuppert.reflect.exceptions.DetailedException#input}
     */
    public DetailedException(String reason, String input){
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
