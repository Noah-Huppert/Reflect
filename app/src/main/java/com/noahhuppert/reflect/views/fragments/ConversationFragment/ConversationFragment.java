package com.noahhuppert.reflect.views.fragments.ConversationFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.threads.GetConversationRunnable;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.threading.MainThreadPool;

public class ConversationFragment extends Fragment {
    private static final String TAG = ConversationFragment.class.getSimpleName();

    public static final String ARGUMENT_CONVERSATION_COMMUNICATION_TYPE = "CONVERSATION_COMMUNICATION_TYPE";
    public static final String ARGUMENT_CONVERSATION_ID = "CONVERSATION_ID";

    private @CommunicationType String communicationType;
    private Conversation conversation;
    private String[] messageIds;

    private static class FragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == HandlerMessagePayload.CONVERSATION) {
                Log.d(TAG, "CONVERSATION => " + ((Conversation) msg.obj).toString());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        // Handle bundle arguments
        Bundle arguments = getArguments();

        @CommunicationType String communicationType = arguments.getString(ARGUMENT_CONVERSATION_COMMUNICATION_TYPE);
        String conversationId = arguments.getString(ARGUMENT_CONVERSATION_ID);

        if(communicationType == null || conversationId == null) {
            throw new WTFException("\"" + ARGUMENT_CONVERSATION_COMMUNICATION_TYPE + "\" and \"" + ARGUMENT_CONVERSATION_ID + "\" bundle arguments must be set", arguments != null ? arguments.toString() : "null");
        }

        FragmentHandler fragmentHandler = new FragmentHandler();

        MainThreadPool.getThreadPoolExecutor().execute(new GetConversationRunnable(fragmentHandler, getContext(), communicationType, conversationId));

        return rootView;
    }
}
