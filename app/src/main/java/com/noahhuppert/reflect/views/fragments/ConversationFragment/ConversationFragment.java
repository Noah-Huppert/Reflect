package com.noahhuppert.reflect.views.fragments.ConversationFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.caches.ConversationLruCache;
import com.noahhuppert.reflect.exceptions.DetailedRuntimeException;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;

public class ConversationFragment extends Fragment {
    private static final String TAG = ConversationFragment.class.getSimpleName();

    public static final String ARGUMENT_CONVERSATION_COMMUNICATION_TYPE = "CONVERSATION_COMMUNICATION_TYPE";
    public static final String ARGUMENT_CONVERSATION_ID = "CONVERSATION_ID";

    private Conversation conversation;
    private String[] messageIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        Bundle arguments = getArguments();

        if(arguments == null || !arguments.containsKey(ARGUMENT_CONVERSATION_COMMUNICATION_TYPE) || !arguments.containsKey(ARGUMENT_CONVERSATION_ID)) {
            // TODO Handle null or missing arguments, maybe display error toast
            throw new WTFException("Arguments can not be null or missing for ConversationFragment", "" + arguments);
        } else {
            String argumentCommunicationType = arguments.getString(ARGUMENT_CONVERSATION_COMMUNICATION_TYPE);
            @CommunicationType String communicationType;

            if(CommunicationType.SMS.equals(argumentCommunicationType)) {
                communicationType = CommunicationType.SMS;
            } else if(CommunicationType.XMPP.equals(argumentCommunicationType)) {
                communicationType = CommunicationType.XMPP;
            } else {
                throw new WTFException("Unhandled communication type in ConversationFragment", argumentCommunicationType);
            }

            ConversationLruCache.ConversationKey conversationKey = new ConversationLruCache.ConversationKey(
                    arguments.getString(ARGUMENT_CONVERSATION_ID),
                    communicationType,
                    getContext()
            );
            conversation = ConversationLruCache.getInstance().get(conversationKey);
        }

        return rootView;
    }
}
