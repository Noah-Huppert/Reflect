package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noahhuppert.reflect.R;

public class FirstTimeSetupSmsFragment extends Fragment {
    private static final String TAG = FirstTimeSetupSmsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup_sms, container, false);

        Button yesButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_yes);
        Button noButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_no);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Make default");
                //TODO Use LocalBroadcaster to switch page with ACTION_FRAGMENT_FIRST_TIME_SETUP_SET_PAGE action
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Use LocalBroadcaster to switch page with ACTION_FRAGMENT_FIRST_TIME_SETUP_SET_PAGE action
            }
        });

        return rootView;
    }
}
