package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.views.FragmentId;
import com.noahhuppert.reflect.views.FragmentSwitcher;

public class FirstTimeSetupXMPPFragment extends Fragment {
    private FragmentSwitcher fragmentSwitcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup_xmpp, container, false);

        Button finishButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_xmpp_finish_button);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSwitcher.switchFragment(FragmentId.CONVERSATIONS_LIST);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            fragmentSwitcher = (FragmentSwitcher) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.getClass().getSimpleName() + " must implement " + FragmentSwitcher.class.getSimpleName());
        }
    }
}
