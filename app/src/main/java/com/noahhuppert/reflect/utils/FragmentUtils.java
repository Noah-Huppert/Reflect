package com.noahhuppert.reflect.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * A utility class for switching and setting up fragments
 */
public class FragmentUtils {
    public static void SetFragment(Fragment fragment, int frameLayoutId, FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .add(frameLayoutId, fragment)
                .commit();
    }
}
