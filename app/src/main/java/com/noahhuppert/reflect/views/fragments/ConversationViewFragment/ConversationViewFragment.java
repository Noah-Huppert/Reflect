package com.noahhuppert.reflect.views.fragments.ConversationViewFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviders;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;

public class ConversationViewFragment extends Fragment {
    public static final String EXTRA_THREAD_ID = "thread_id";
    public static final String EXTRA_SENDER_NAME = "sender_name";
    public static final String EXTRA_SENDER_NUMBER = "sender_number";

    private String threadId;
    private String senderName;
    private String senderNumber;

    private RecyclerView messagesList;
    private ConversationViewAdapter conversationViewAdapter;

    private EditText newMessageBody;
    private Button sendNewMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversation_view, container, false);

        threadId = getArguments().getString(EXTRA_THREAD_ID);
        senderName = getArguments().getString(EXTRA_SENDER_NAME);
        senderNumber = getArguments().getString(EXTRA_SENDER_NUMBER);

        messagesList = (RecyclerView) rootView.findViewById(R.id.fragment_conversation_view_list_messages);

        messagesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        conversationViewAdapter = new ConversationViewAdapter(getActivity(), threadId, senderName, messagesList);

        messagesList.setAdapter(conversationViewAdapter);

        /*newMessageBody = (EditText) rootView.findViewById(R.id.fragment_conversation_view_new_message_body);
        sendNewMessage = (Button) rootView.findViewById(R.id.fragment_conversation_view_new_message_send);

        sendNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DD", senderNumber);
                ReflectMessage reflectMessage = new ReflectMessage();
                reflectMessage.setProtocol(CommunicationType.SMS);
                reflectMessage.setReceiverUri(
                        new Uri.Builder()
                                .scheme(SmsMessagingProvider.SMS_URI_SCHEME)
                                .authority(senderNumber)
                                .build()
                );
                reflectMessage.setBody(newMessageBody.getText().toString());

                MessagingProviders.getMessagingProvider(CommunicationType.SMS).createMessage(reflectMessage,
                        getActivity(),
                        null);
            }
        });*/

        return rootView;
    }
}
