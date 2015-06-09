package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noahhuppert.reflect.R;

public class FirstTimeSetupGoogleFragment extends Fragment {
    private FirstTimeSetupPageSwitcher firstTimeSetupPageSwitcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup_google, container, false);

        Button nextButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_google_next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTimeSetupPageSwitcher.switchPage(firstTimeSetupPageSwitcher.getCurrentPageIndex() + 1);
            }
        });

        try{
            firstTimeSetupPageSwitcher = (FirstTimeSetupPageSwitcher) getParentFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(getParentFragment().getClass().getSimpleName() + " must implement " + FirstTimeSetupPageSwitcher.class.getSimpleName());
        }

        return rootView;
    }
}
