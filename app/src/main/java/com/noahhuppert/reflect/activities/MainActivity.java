package com.noahhuppert.reflect.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.BuildConfig;
import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.utils.TelephonyUtils;
import com.noahhuppert.reflect.views.FragmentId;
import com.noahhuppert.reflect.views.FragmentSwitcher;
import com.noahhuppert.reflect.views.fragments.ConversationsListFragment;
import com.noahhuppert.reflect.views.fragments.DebugSendFragment;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements FragmentSwitcher {
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

        //Setup main content
        if(savedInstanceState == null) {
            switchFragment(FragmentId.CONVERSATIONS_LIST);
        }

        TelephonyUtils.SetAsDefaultSmsApp(this);
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

        if (item.getItemId() == R.id.main_menu_debug_send){
            switchFragment(FragmentId.DEBUG_SEND);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void switchFragment(@FragmentId int fragmentId) {
        Fragment fragment = null;

        if(fragmentId == FragmentId.CONVERSATIONS_LIST) {
            fragment = new ConversationsListFragment();
        }else if(fragmentId == FragmentId.DEBUG_SEND){
            fragment = new DebugSendFragment();
        }

        if(fragment == null){
            throw new WTFException("A fragment instance should be created for supplied fragmentId", fragmentId + "");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
    }
}
