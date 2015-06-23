package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsGetConversationIdsRunnable;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsGetConversationRunnable;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    private RecyclerView conversationsList;
    private ConversationListAdapter conversationListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        conversationsList = (RecyclerView) rootView.findViewById(R.id.conversations_list);

        conversationsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        SmsGetConversationIdsRunnable smsGetConversationIdsRunnable = new SmsGetConversationIdsRunnable(getActivity(), new ThreadResultHandler<String[]>() {
            @Override
            public void onDone(final String[] conversationIds) {
                conversationListAdapter.conversationIds = conversationIds;

                for(int i = 0; i < conversationIds.length; i++){
                    conversationListAdapter.conversationsCache.put(conversationIds[i], new SmsMessagingProvider().getConversation(getActivity(), conversationIds[i]));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conversationListAdapter.notifyDataSetChanged();
                        }
                    });
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conversationListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "Failed to fetch SMS conversation ids", exception);
            }
        });
        MainThreadPool.getInstance().getPool().submit(smsGetConversationIdsRunnable);

        conversationListAdapter = new ConversationListAdapter(new String[0], getActivity());

        conversationsList.setAdapter(conversationListAdapter);

        return rootView;
    }
}
