package com.noahhuppert.reflect.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noahhuppert.reflect.R;

/**
 * Created by Noah Huppert on 3/12/2015.
 */
public class SetupProvidersFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup_providers, container, false);

        ActionBarActivity activity = (ActionBarActivity) getActivity();

        if(activity.getSupportActionBar() != null){
            activity.getSupportActionBar().hide();
        }

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        ActionBarActivity activity = (ActionBarActivity) getActivity();

        if(activity.getSupportActionBar() != null){
            activity.getSupportActionBar().show();
        }
    }
}
