package com.noahhuppert.reflect.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;

/**
 * Class for determining feature availability like Telephony
 */
public class TelephonyUtils {
    public static final String TAG = TelephonyUtils.class.getSimpleName();

    /**
     * Determines whether or not the device has Telephony capability
     *
     * This function is simply a wrapper of {@link TelephonyUtils#GetTelephonyManager(Context)}
     * that returns a boolean instead of throwing a exception. This is useful for conditionals.
     *
     * @param context The context to get the package manager from
     * @return If the device has Telephony capability
     */
    public static boolean HasTelephony(Context context){
        try{
            GetTelephonyManager(context);
        } catch (NoTelephonyManagerException e){
            return false;
        }

        return true;
    }

    /**
     * Determines whether or not Reflect is the default SMS app
     * @param context The context used to get the {@link TelephonyManager}
     * @return If Reflect is the default sms app
     */
    public static boolean IsDefaultSmsApp(Context context){
        if(!HasTelephony(context)){
            return false;
        }

        String applicationPackage = context.getPackageName();
        String defaultSmsAppPackage = Telephony.Sms.getDefaultSmsPackage(context);

        return applicationPackage.equals(defaultSmsAppPackage);
    }

    /**
     * Launches the intent to set Reflect as the default SMS app
     * @param context The context used to get the {@link TelephonyManager}
     */
    public static void SetAsDefaultSmsApp(Context context){
        if(!IsDefaultSmsApp(context)){
            Intent makeDefaultSmsAppIntent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            makeDefaultSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,  context.getPackageName());

            context.startActivity(makeDefaultSmsAppIntent);
        }
    }

    /**
     * Gets the {@link TelephonyManager}
     * @param context The context used to get the {@link TelephonyManager}
     * @return {@link TelephonyManager}
     * @throws NoTelephonyManagerException If no {@link TelephonyManager} is available
     */
    public static TelephonyManager GetTelephonyManager(Context context) throws NoTelephonyManagerException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(telephonyManager == null || telephonyManager == null || telephonyManager.getLine1Number() == null){
            throw new NoTelephonyManagerException("The device does not have a Telephony capability", "telephonyManager is null");
        }

        return telephonyManager;
    }
}
