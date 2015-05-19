package com.noahhuppert.reflect.views;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.noahhuppert.reflect.R;

public class ActionBarDrawerToggle extends android.support.v7.app.ActionBarDrawerToggle {
    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
        super(activity, drawerLayout, toolbar, R.string.navigation_drawer_open_description, R.string.navigation_drawer_close_description);
    }
}
