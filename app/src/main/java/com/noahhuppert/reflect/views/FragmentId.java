package com.noahhuppert.reflect.views;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A interface that holds the ids of Fragments that can be switched to
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        FragmentId.CONVERSATIONS_LIST,
        FragmentId.DEBUG_SEND
})
public @interface FragmentId{
    int CONVERSATIONS_LIST = 0;
    int DEBUG_SEND = 1;
}
