package com.noahhuppert.reflect.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
    protected int fragmentLayoutId;

    public BaseFragment(int fragmentLayoutId) {
        this.fragmentLayoutId = fragmentLayoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(fragmentLayoutId, container, false);

        return rootView;
    }
}
