package com.noahhuppert.reflect.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        return rootView;
    }
}
