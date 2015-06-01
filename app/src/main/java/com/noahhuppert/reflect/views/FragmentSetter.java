package com.noahhuppert.reflect.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.utils.FragmentUtils;
import com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment.FirstTimeSetupFragment;

/**
 * A class to manage the lifecycle of fragments
 */
public class FragmentSetter {
    private static final String TAG = FragmentSetter.class.getSimpleName();

    private static final String ACTION_SET_FRAGMENT = "com.noahhuppert.reflect.intent.actions.SET_FRAGMENT";
    private static final String INTENT_EXTRA_FRAGMENT = "fragment";

    private BroadcastReceiver broadcastReceiver;
    private FragmentManager fragmentManager;

    private static FragmentSetter ourInstance = new FragmentSetter();

    public static FragmentSetter getInstance(){
        return ourInstance;
    }

    private FragmentSetter(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String registeredFragmentId = intent.getStringExtra(INTENT_EXTRA_FRAGMENT);

                if(registeredFragmentId == null){
                    throw new WTFException("Cannot set fragment if extra \"" + INTENT_EXTRA_FRAGMENT + "\" is not set", null);
                }

                RegisteredFragment registeredFragment = RegisteredFragment.valueOf(registeredFragmentId);
                Fragment fragment = RegisteredFragment.GetFragmentInstance(registeredFragment);

                if(registeredFragment == null){
                    throw new WTFException("Unknown fragment id of \"" +  registeredFragment + "\"", null);
                }

                if(fragmentManager == null){
                    throw new WTFException("FragmentManager is not set, call FragmentSetter.register first", null);
                }

                FragmentUtils.SetFragment(fragment, R.id.activity_main_content, fragmentManager);
            }
        };
    }

    /* Actions */
    public void register(Context context, FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver,
                new IntentFilter(ACTION_SET_FRAGMENT));
    }

    public void unregister(Context context){
        fragmentManager = null;
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    public void setFragment(RegisteredFragment fragment, Context context){
        Intent intent = new Intent(ACTION_SET_FRAGMENT);
        intent.putExtra(INTENT_EXTRA_FRAGMENT, fragment.toString());

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public enum RegisteredFragment {
        FIRST_TIME_SETUP;

        public static Fragment GetFragmentInstance(RegisteredFragment fragment){
            if(fragment == FIRST_TIME_SETUP){
                return new FirstTimeSetupFragment();
            }

            return null;
        }
    }
}
