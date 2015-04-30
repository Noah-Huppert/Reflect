package com.noahhuppert.reflect.exceptions;

public class NoTelephonyManagerException extends DetailedException {
    public NoTelephonyManagerException(String reason, String input) {
        super(reason, input);
    }
}
