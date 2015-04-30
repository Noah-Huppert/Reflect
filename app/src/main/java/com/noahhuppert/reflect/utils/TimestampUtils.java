package com.noahhuppert.reflect.utils;

import java.sql.Timestamp;

/**
 * A utility class for the Timestamp object
 */
public class TimestampUtils {
    public static Timestamp FromLong(long timestampLong){
        return new Timestamp(timestampLong);
    }
}
