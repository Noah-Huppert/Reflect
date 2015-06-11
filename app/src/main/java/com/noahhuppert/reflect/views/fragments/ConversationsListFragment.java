package com.noahhuppert.reflect.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviders;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        TextView phoneNumber = (TextView) rootView.findViewById(R.id.fragment_conversations_list_phone_number);
        final EditText receiverNumber = (EditText) rootView.findViewById(R.id.fragment_conversations_list_receiver);
        final EditText text = (EditText) rootView.findViewById(R.id.fragment_conversations_list_text);
        Button send = (Button) rootView.findViewById(R.id.fragment_conversations_list_send);

        try {
            String phoneNumberText = TelephonyUtils.GetTelephonyManager(getActivity()).getLine1Number();
            phoneNumber.setText(phoneNumberText);

            String modPhoneNumberText = phoneNumberText.substring(0, phoneNumberText.length() - 1);

            if(phoneNumberText.endsWith("4")){
                modPhoneNumberText += "6";
            } else {
                modPhoneNumberText += "4";
            }

            receiverNumber.setText(modPhoneNumberText);
        } catch (NoTelephonyManagerException e){
            phoneNumber.setText("Error");
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflectMessage reflectMessage = new ReflectMessage();
                reflectMessage.setProtocol(CommunicationType.SMS);
                reflectMessage.setReceiverUri(
                        new Uri.Builder()
                                .scheme(SmsMessagingProvider.SMS_URI_SCHEME)
                                .authority(receiverNumber.getText().toString())
                                .build()
                );
                reflectMessage.setBody(text.getText().toString());

                MessagingProviders.getMessagingProvider(CommunicationType.SMS).createMessage(reflectMessage,
                        getActivity(),
                        null);
            }
        });

        return rootView;
    }
}
