package com.noahhuppert.reflect.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;

/**
 * Class for determining feature availability like Telephony
 */
public class TelephonyUtils {
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

    public static boolean IsDefaultSmsApp(Context context){
        if(!HasTelephony(context)){
            return false;
        }

        String applicationPackage = context.getPackageName();
        String defaultSmsAppPackage = Telephony.Sms.getDefaultSmsPackage(context);

        return applicationPackage.equals(defaultSmsAppPackage);
    }

    public static TelephonyManager GetTelephonyManager(Context context) throws NoTelephonyManagerException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(telephonyManager == null || !HasTelephony(context)){
            throw new NoTelephonyManagerException("The device does not have a Telephony capability", "telephonyManager is null");
        }

        return telephonyManager;
    }
}
