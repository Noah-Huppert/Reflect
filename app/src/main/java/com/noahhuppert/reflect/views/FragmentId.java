package com.noahhuppert.reflect.views;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A interface that holds the ids of Fragments that can be switched to
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        FragmentId.FIRST_TIME_SETUP,
        FragmentId.CONVERSATIONS_LIST
})
public @interface FragmentId{
    int FIRST_TIME_SETUP = 0;
    int CONVERSATIONS_LIST = 1;
}
