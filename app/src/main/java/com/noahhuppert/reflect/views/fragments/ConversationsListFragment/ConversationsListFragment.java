package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.databinding.DataBindingUtil;
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
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.util.Arrays;

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

        GetSmsConversationsThread getSmsConversationsThread = new GetSmsConversationsThread(getActivity(), new ThreadResultHandler<Conversation[]>() {
            @Override
            public void onDone(final Conversation[] data) {

                for(int i = 0; i < data.length; i ++){
                    Log.d(TAG, data[i].toString());
                }

                conversationListAdapter.conversations = data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conversationListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "Exception when getting sms conversations", exception);
            }
        });

        MainThreadPool.getInstance().getPool().submit(getSmsConversationsThread);

        conversationListAdapter = new ConversationListAdapter(new Conversation[0], getActivity());

        conversationsList.setAdapter(conversationListAdapter);

        return rootView;
    }
}
