package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.fragments.FirstTimeSetupFragment;
import com.noahhuppert.reflect.settings.Settings;
import com.noahhuppert.reflect.utils.FragmentUtils;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_main);

        //Set main fragment content
        //TODO: Continue working on https://developer.android.com/training/implementing-navigation/nav-drawer.html
        FrameLayout mainContent = (FrameLayout) findViewById(R.id.activity_main_content);

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
