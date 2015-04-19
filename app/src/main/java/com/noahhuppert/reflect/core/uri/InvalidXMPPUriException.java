package com.noahhuppert.reflect.core.uri;

/**
 * Exception signifying that the provided Uri is not a valid XMPP Uri
 */
public class InvalidXMPPUriException extends Exception {
    public InvalidXMPPUriException(String detailMessage) {
        super(detailMessage);
    }
}
