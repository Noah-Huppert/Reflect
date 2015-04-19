package com.noahhuppert.reflect.core.uri;

/**
 * Class for holding phone numbers. Main job is to normalize phone numbers
 */
public class SMSReflectUri {
    private String formattedNumber;

    public SMSReflectUri(SMSReflectUri smsReflectUri){
        formattedNumber = smsReflectUri.getFormattedNumber();
    }

    /* Actions */
    public static SMSReflectUri Parse(String number) {
        return null;
    }

    /* Getters */
    public String getFormattedNumber() {
        return formattedNumber;
    }
}
