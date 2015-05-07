package com.noahhuppert.reflect.utils;

/**
 * A class providing helpers for using enums
 */
public class EnumUtils {
    /**
     * Turns an enum into a lowercase string. Used mainly for putting enums in the messaging URi
     * scheme
     * @param e The enum to turn into a lowercase string
     * @return The provided enum in the form of a lowercase string.
     */
    public static String PartEnumToString(Enum e){
        return e.toString().toLowerCase();
    }
}
