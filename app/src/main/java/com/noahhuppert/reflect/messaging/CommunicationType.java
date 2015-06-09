package com.noahhuppert.reflect.messaging;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Type of communication, either SMS or XMPP. Used to determine contact type and preferred
 * communication method.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        CommunicationType.XMPP,
        CommunicationType.SMS
})
public @interface CommunicationType{
    String XMPP = "XMPP";
    String SMS = "SMS";
}