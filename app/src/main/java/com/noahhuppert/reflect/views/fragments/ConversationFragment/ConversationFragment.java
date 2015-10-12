package com.noahhuppert.reflect.views.fragments.ConversationFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;

public class ConversationFragment extends Fragment {
    private static final String TAG = ConversationFragment.class.getSimpleName();

    public static final String ARGUMENT_CONVERSATION_COMMUNICATION_TYPE = "CONVERSATION_COMMUNICATION_TYPE";
    public static final String ARGUMENT_CONVERSATION_ID = "CONVERSATION_ID";

    private @CommunicationType String communicationType;
    private Conversation conversation;
    private String[] messageIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        Bundle arguments = getArguments();

        return rootView;
    }
}
