package com.noahhuppert.reflect.exceptions;

/**
 * An exception thrown when the device does not have a TelephonyManager
 */
public class NoTelephonyManagerException extends DetailedException {
    public NoTelephonyManagerException(String reason, String input) {
        super(reason, input);
    }
}
