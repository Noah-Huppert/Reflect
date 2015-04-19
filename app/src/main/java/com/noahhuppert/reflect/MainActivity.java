package com.noahhuppert.reflect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.noahhuppert.reflect.core.settings.UserSetting;
import com.noahhuppert.reflect.core.settings.UserSettings;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        UserSettings.SetDefaults();

        setContentView(R.layout.activity_main);

        UserSetting defaultIso = UserSettings.GetSettingSync(UserSettings.SETTING_DEFAULT_COUNTRY_CODE);
        System.out.println(defaultIso.getKey() + " => " + defaultIso.getValue() + " " + UserSettings.ValueAsBoolean(defaultIso.getValue()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
