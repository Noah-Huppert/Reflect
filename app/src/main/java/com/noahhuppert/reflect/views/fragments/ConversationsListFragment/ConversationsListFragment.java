package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.app.Activity;
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
import com.noahhuppert.reflect.views.FragmentId;
import com.noahhuppert.reflect.views.FragmentSwitcher;
import com.noahhuppert.reflect.views.fragments.ConversationViewFragment.ConversationViewAdapter;
import com.noahhuppert.reflect.views.fragments.ConversationViewFragment.ConversationViewFragment;

public class ConversationsListFragment extends Fragment {
    private static final String TAG = ConversationsListFragment.class.getSimpleName();

    private RecyclerView conversationList;
    private ConversationsListAdapter conversationsListAdapter;
    private FragmentSwitcher fragmentSwitcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);

        conversationList = (RecyclerView) rootView.findViewById(R.id.fragment_conversations_list_conversations);

        conversationList.setLayoutManager(new LinearLayoutManager(getActivity()));

        conversationsListAdapter = new ConversationsListAdapter(getActivity(), new ConversationsListAdapter.OnClickListener() {
            @Override
            public void onClick(int index) {
                Bundle conversationViewExtras = new Bundle();

                conversationViewExtras.putString(ConversationViewFragment.EXTRA_THREAD_ID, conversationsListAdapter.getConversation(index)[ConversationsListAdapter.CONVERSATION_THREAD_ID_INDEX]);
                conversationViewExtras.putString(ConversationViewFragment.EXTRA_SENDER_NAME, conversationsListAdapter.getConversation(index)[ConversationsListAdapter.CONVERSATION_CONTACT_NAME_INDEX]);
                conversationViewExtras.putString(ConversationViewFragment.EXTRA_SENDER_NAME, conversationsListAdapter.getConversation(index)[ConversationsListAdapter.CONVERSATION_CONTACT_NUMBER_INDEX]);


                fragmentSwitcher.switchFragment(FragmentId.CONVERSATION_VIEW, conversationViewExtras);
            }
        });

        conversationList.setAdapter(conversationsListAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            fragmentSwitcher = (FragmentSwitcher) activity;
        } catch (ClassCastException e){
            Log.e(TAG, activity.getClass().getSimpleName() + " must implement " + FragmentSwitcher.class.getSimpleName());
        }
    }
}
