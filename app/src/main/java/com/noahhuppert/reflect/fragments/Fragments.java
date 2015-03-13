package com.noahhuppert.reflect.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.noahhuppert.reflect.R;

/**
 * Created by Noah Huppert on 3/12/2015.
 */
public class Fragments {
    public static void Switch(FragmentManager fragmentManager, Fragment fragment){
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}
