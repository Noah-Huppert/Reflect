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
import com.noahhuppert.reflect.intents.LocalBroadcaster;
import com.noahhuppert.reflect.utils.FragmentUtils;
import com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment.FirstTimeSetupFragment;

import javax.validation.constraints.NotNull;

/**
 * A class to manage the lifecycle of fragments
 */
public class FragmentSetter extends LocalBroadcaster {
    private static final String ACTION_SET_FRAGMENT = "com.noahhuppert.reflect.intent.actions.SET_FRAGMENT";
    private static final String INTENT_EXTRA_FRAGMENT = "fragment";

    private FragmentManager fragmentManager;

    private static FragmentSetter ourInstance = new FragmentSetter();

    public static FragmentSetter getInstance(){
        return ourInstance;
    }

    private FragmentSetter(){
        super(new IntentFilter(ACTION_SET_FRAGMENT));
    }

    /* Actions */

    @Override
    public void onBroadcast(@NotNull Context context, @NotNull Intent intent) {
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

    public void register(@NotNull Context context, @NotNull FragmentManager fragmentManager) {
        register(context);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void unregister(@NotNull Context context) {
        super.unregister(context);
        fragmentManager = null;
    }

    public void setFragment(RegisteredFragment fragment, Context context){
        //TODO use LocalBroadcaster.sendBroadcast
        Intent intent = new Intent(ACTION_SET_FRAGMENT);
        intent.putExtra(INTENT_EXTRA_FRAGMENT, fragment.toString());

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public enum RegisteredFragment {
        FIRST_TIME_SETUP;

        private Fragment fragmentInstance;

        RegisteredFragment(){
            fragmentInstance = null;
        }

        /* Actions */
        public static Fragment GetFragmentInstance(RegisteredFragment fragment){
            //Save memory by having saved instance
            if(fragment.getFragmentInstance() == null) {
                if (fragment == FIRST_TIME_SETUP) {
                    fragment.setFragmentInstance(new FirstTimeSetupFragment());
                }
            }

            return fragment.getFragmentInstance();
        }

        /* Getters */
        public Fragment getFragmentInstance() {
            return fragmentInstance;
        }

        /* Setters */
        public void setFragmentInstance(Fragment fragmentInstance) {
            this.fragmentInstance = fragmentInstance;
        }
    }
}
