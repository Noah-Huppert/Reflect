package com.noahhuppert.reflect.exceptions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        Errors.ERROR_OK
})
public @interface Errors {
    int ERROR_OK = 0;
}
