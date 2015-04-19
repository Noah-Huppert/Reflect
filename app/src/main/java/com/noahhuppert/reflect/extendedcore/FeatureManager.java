package com.noahhuppert.reflect.extendedcore;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Class for determining feature availability like Telephony
 */
public class FeatureManager {
    /**
     * Determines weather or not the device has Telephony capability
     * @param context The context to get the package manager from
     * @return If the device has Telephony capability
     */
    public static boolean HasTelephony(Context context){
        PackageManager packageManager = context.getPackageManager();

        if(packageManager == null){
            return false;
        }

        return packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }
}
