package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony;
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
import com.noahhuppert.reflect.messaging.providers.MessagingProviderCache;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsGetConversationIdsRunnable;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsGetConversationRunnable;
import com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider.SmsMessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.views.RecyclerView.RecyclerViewOnItemClickListener;

import java.util.Arrays;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    private RecyclerView conversationsList;
    private ConversationListAdapter conversationListAdapter;

    private Handler conversationsHandler;

    private static class ConversationsHandler extends Handler{
        private final Context context;
        private ConversationListAdapter conversationListAdapter;

        public ConversationsHandler(Context context, ConversationListAdapter conversationListAdapter) {
            super(Looper.getMainLooper());
            this.conversationListAdapter = conversationListAdapter;
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == SmsMessagingProvider.HandlerMessagePayload.CONVERSATION_IDS){
                String[] conversationIds = (String[]) msg.obj;
                conversationListAdapter.conversationIds = conversationIds;
                conversationListAdapter.notifyItemRangeChanged(0, conversationIds.length);

                for(int i = 0; i < conversationIds.length; i++){
                    if(!conversationListAdapter.conversationsCache.containsKey(conversationIds[i])) {
                        SmsGetConversationRunnable smsGetConversationRunnable = new SmsGetConversationRunnable(context, this, conversationIds[i]);
                        MainThreadPool.getThreadPoolExecutor().execute(smsGetConversationRunnable);
                    }
                }
            } else if(msg.what == SmsMessagingProvider.HandlerMessagePayload.CONVERSATION){
                Conversation conversation = (Conversation) msg.obj;
                conversationListAdapter.conversationsCache.put(conversation.id, conversation);
                conversationListAdapter.notifyItemChanged(Arrays.asList(conversationListAdapter.conversationIds).indexOf(conversation.id));
            } else {
                msg.sendToTarget();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        conversationsList = (RecyclerView) rootView.findViewById(R.id.conversations_list);

        conversationsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        conversationListAdapter = new ConversationListAdapter(new String[0], getActivity());
        conversationsList.setAdapter(conversationListAdapter);
        conversationsList.setItemAnimator(new SlideInLeftAnimator());

        conversationListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder, int index) {
                // TODO Handle conversation list item click
                String[] messageIds = MessagingProviderCache.get(SmsMessagingProvider.class).getConversationMessageIds(getContext(), conversationListAdapter.conversationIds[index]);
                MessagingProviderCache.get(SmsMessagingProvider.class).getMessage(getContext(), messageIds[1]);
            }
        });

        conversationsHandler = new ConversationsHandler(getActivity(), conversationListAdapter);

        MainThreadPool.getThreadPoolExecutor().execute(new SmsGetConversationIdsRunnable(getActivity(), conversationsHandler));

        return rootView;
    }
}
