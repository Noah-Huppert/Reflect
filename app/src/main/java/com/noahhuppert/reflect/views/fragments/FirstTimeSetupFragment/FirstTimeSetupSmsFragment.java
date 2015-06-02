package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noahhuppert.reflect.R;

public class FirstTimeSetupSmsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup_sms, container, false);

        Button yesButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_yes);

        return rootView;
    }
}
