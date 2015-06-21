package com.noahhuppert.reflect.exceptions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        Errors.OK,
        Errors.FAILED
})
public @interface Errors {
    int OK = 0;
    int FAILED  = 1;
}
