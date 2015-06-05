package com.noahhuppert.reflect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.settings.Settings;
import com.noahhuppert.reflect.utils.FragmentUtils;
import com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment.FirstTimeSetupFragment;
import com.noahhuppert.reflect.views.fragments.NavigationDrawerFragment;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //Main UI Elements
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_main);

        //Setup navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open_description,
                R.string.navigation_drawer_close_description);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentUtils.SetFragment(new NavigationDrawerFragment(), R.id.activity_main_navigation_drawer, getSupportFragmentManager());

        //Setup main content
        if(savedInstanceState == null) {
            if (Settings.getInstance().getBoolean(Settings.KEY_FIRST_TIME_SETUP_COMPLETE, this)) {

            } else {
                FirstTimeSetupFragment firstTimeSetupFragment = new FirstTimeSetupFragment();
                firstTimeSetupFragment.setArguments(savedInstanceState);

                getSupportFragmentManager().beginTransaction().add(R.id.activity_main_content, firstTimeSetupFragment).commit();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}
