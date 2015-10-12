package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.threads.GetConversationIdsRunnable;
import com.noahhuppert.reflect.messaging.providers.threads.GetConversationRunnable;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.views.FragmentId;
import com.noahhuppert.reflect.views.FragmentSwitcher;
import com.noahhuppert.reflect.views.RecyclerView.RecyclerViewOnItemClickListener;
import com.noahhuppert.reflect.views.fragments.ConversationFragment.ConversationFragment;

import java.util.Arrays;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    private FragmentSwitcher fragmentSwitcher;

    private RecyclerView conversationsList;
    private ConversationListAdapter conversationListAdapter;

    private static class FragmentHandler extends Handler {
        private Context context;
        private ConversationListAdapter conversationListAdapter;

        public FragmentHandler(Context context, ConversationListAdapter conversationListAdapter) {
            this.context = context;
            this.conversationListAdapter = conversationListAdapter;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == HandlerMessagePayload.CONVERSATION_IDS) {
                conversationListAdapter.conversationIds = (String[]) msg.obj;
                conversationListAdapter.notifyItemRangeChanged(0, conversationListAdapter.conversationIds.length);

                for(int i = 0; i < conversationListAdapter.conversationIds.length; i++) {
                    MainThreadPool.getThreadPoolExecutor().execute(new GetConversationRunnable(this, context, CommunicationType.SMS, conversationListAdapter.conversationIds[i]));
                }
            } else if(msg.what == HandlerMessagePayload.CONVERSATION) {
                Conversation conversation = (Conversation) msg.obj;
                conversationListAdapter.conversationsCache.put(conversation.id, conversation);
                conversationListAdapter.notifyItemChanged(Arrays.asList(conversationListAdapter.conversationIds).indexOf(conversation.id));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        // Conversation Recycler View
        conversationsList = (RecyclerView) rootView.findViewById(R.id.conversations_list);

        conversationsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        conversationListAdapter = new ConversationListAdapter(new String[0], getActivity());
        conversationsList.setAdapter(conversationListAdapter);
        conversationsList.setItemAnimator(new SlideInLeftAnimator());

        conversationListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, int index) {
                Bundle arguments = new Bundle();

                arguments.putString(ConversationFragment.ARGUMENT_CONVERSATION_COMMUNICATION_TYPE, CommunicationType.SMS);
                arguments.putString(ConversationFragment.ARGUMENT_CONVERSATION_ID, conversationListAdapter.conversationIds[index]);

                fragmentSwitcher.switchFragment(FragmentId.CONVERSATION, arguments);
            }
        });

        // Fetch data
        FragmentHandler fragmentHandler = new FragmentHandler(getContext(), conversationListAdapter);

        MainThreadPool.getThreadPoolExecutor().execute(new GetConversationIdsRunnable(fragmentHandler, getContext(), CommunicationType.SMS));

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentSwitcher = (FragmentSwitcher) context;
        } catch(ClassCastException e) {
            Log.e(TAG, context.getClass().getSimpleName() + " must implement " + FragmentSwitcher.class.getSimpleName());
        }
    }
}
