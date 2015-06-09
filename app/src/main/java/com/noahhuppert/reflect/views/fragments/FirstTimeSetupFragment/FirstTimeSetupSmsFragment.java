package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.providers.MessagingProviders;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class FirstTimeSetupSmsFragment extends Fragment {
    private static final String TAG = FirstTimeSetupSmsFragment.class.getSimpleName();

    private FirstTimeSetupPageSwitcher pageSwitcher;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_first_time_setup_sms, container, false);

        Button notDefaultYesButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_yes);
        Button notDefaultNoButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_navigation_button_no);
        Button isDefaultNextButton = (Button) rootView.findViewById(R.id.fragment_first_time_setup_is_default_next_button);
        Button noTelephonyNextButton = (Button) rootView.findViewById(R.id.fragment_First_time_setup_no_telephony_next_button);

        notDefaultYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyUtils.SetAsDefaultSmsApp(getActivity());
            }
        });

        View.OnClickListener nextPageOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageSwitcher.switchPage(pageSwitcher.getCurrentPageIndex() + 1);
            }
        };

        notDefaultNoButton.setOnClickListener(nextPageOnClickListener);

        isDefaultNextButton.setOnClickListener(nextPageOnClickListener);

        noTelephonyNextButton.setOnClickListener(nextPageOnClickListener);

        //Attach FirstTimeSetupPageSwitcher
        try{
            pageSwitcher = (FirstTimeSetupPageSwitcher) getParentFragment();
        } catch(ClassCastException e){
            throw new ClassCastException(getParentFragment().getClass().getSimpleName() + " must implement " + FirstTimeSetupPageSwitcher.class.getSimpleName());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!TelephonyUtils.HasTelephony(getActivity())){
            rootView.findViewById(R.id.fragment_first_time_setup_no_telephony).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_not_default).setVisibility(View.GONE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_is_default).setVisibility(View.GONE);
        } else if(!TelephonyUtils.IsDefaultSmsApp(getActivity())){
            rootView.findViewById(R.id.fragment_first_time_setup_no_telephony).setVisibility(View.GONE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_not_default).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_is_default).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.fragment_first_time_setup_no_telephony).setVisibility(View.GONE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_not_default).setVisibility(View.GONE);
            rootView.findViewById(R.id.fragment_first_time_setup_sms_promp_view_is_default).setVisibility(View.VISIBLE);
        }
    }
}
