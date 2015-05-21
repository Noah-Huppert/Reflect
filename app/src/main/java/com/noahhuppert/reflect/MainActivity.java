package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;
import com.noahhuppert.reflect.views.fragments.NavigationDrawerFragment;
import com.noahhuppert.reflect.settings.Settings;
import com.noahhuppert.reflect.utils.FragmentUtils;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //Main UI Elements
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_main);

        //Set main fragment content
        //TODO: Continue working on https://developer.android.com/training/implementing-navigation/nav-drawer.html
        //TODO: Set Home as dawer toggler
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open_description,
                R.string.navigation_drawer_close_description);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);


        FragmentUtils.SetFragment(new NavigationDrawerFragment(), R.id.activity_main_navigation_drawer, getSupportFragmentManager());

        if(Settings.getInstance().getBoolean(Settings.KEY_FIRST_TIME_SETUP_COMPLETE, getBaseContext())){

        } else {
            FragmentUtils.SetFragment(new FirstTimeSetupFragment(), R.id.activity_main_content, getSupportFragmentManager());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
