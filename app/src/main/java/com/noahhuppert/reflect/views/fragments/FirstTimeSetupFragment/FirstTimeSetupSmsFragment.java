package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class FirstTimeSetupSmsFragment extends Fragment {
    private static final String TAG = FirstTimeSetupSmsFragment.class.getSimpleName();

    private FirstTimeSetupPageSwitcher pageSwitcher;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_first_time_setup_sms, container, false);

        //TODO Detect when phone doesn't have Telephony and disable this page

        Button yesButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_yes);
        Button noButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_no);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Make default");

                TelephonyUtils.SetAsDefaultSmsApp(getActivity());
                //TODO only switch page after set default dialog is complete
                pageSwitcher.switchPage(pageSwitcher.getCurrentPageIndex() + 1);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageSwitcher.switchPage(pageSwitcher.getCurrentPageIndex() + 1);
            }
        });


        //Attach FirstTimeSetupPageSwitcher
        try{
            pageSwitcher = (FirstTimeSetupPageSwitcher) getParentFragment();
        } catch(ClassCastException e){
            throw new ClassCastException(getParentFragment().getClass().getSimpleName() + " must implement " + FirstTimeSetupPageSwitcher.class.getSimpleName());
        }

        //setCorrectSmsPrompt();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCorrectSmsPrompt();
    }

    private void setCorrectSmsPrompt(){
        if(TelephonyUtils.IsDefaultSmsApp(getActivity())){
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_not_default).setVisibility(View.GONE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_is_default).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_not_default).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_is_default).setVisibility(View.GONE);
        }
    }
}
