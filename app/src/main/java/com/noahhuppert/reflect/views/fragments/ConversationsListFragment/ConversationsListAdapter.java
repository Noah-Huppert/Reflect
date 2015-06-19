package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.util.List;

public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListAdapter.ConversationsListViewHolder> {
    private static final String TAG = ConversationsListAdapter.class.getSimpleName();

    public static final int CONVERSATION_CONTACT_NAME_INDEX = 0;
    public static final int CONVERSATION_CONTACT_NUMBER_INDEX = 1;
    public static final int CONVERSATION_THREAD_ID_INDEX = 2;
    public static final int CONVERSATION_SNIPPET_INDEX = 3;
    public static final int CONVERSATION_TIMESTAMP_INDEX = 4;

    private String[][] conversations;
    private OnClickListener onClickListener;

    interface OnClickListener{
        void onClick(int index);
    }

    public static final class ConversationsListViewHolder extends RecyclerView.ViewHolder{
        public TextView contactName;
        public TextView conversationSnippet;
        public TextView conversationTimestamp;

        public ConversationsListViewHolder(View itemView) {
            super(itemView);

            contactName = (TextView) itemView.findViewById(R.id.fragment_conversations_list_view_holder_contact_name);
            conversationSnippet = (TextView) itemView.findViewById(R.id.fragment_conversations_list_view_holder_snippet);
            conversationTimestamp = (TextView) itemView.findViewById(R.id.fragment_conversations_list_view_holder_timestamp);
        }
    }

    public ConversationsListAdapter(final Activity activity, OnClickListener onClickListener){
        conversations = new String[0][5];
        this.onClickListener = onClickListener;

        GetConversationsForAdapterRunnable getConversationsForAdapterRunnable = new GetConversationsForAdapterRunnable(activity, new ThreadResultHandler<String[][]>() {
            @Override
            public void onDone(String[][] data) {
                conversations = data;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "Exception fetching conversations", exception);
            }
        });

        MainThreadPool.getInstance().getPool().submit(getConversationsForAdapterRunnable);
    }

    public ConversationsListAdapter(String[][] conversations, OnClickListener onClickListener){
        this.conversations = conversations;
        this.onClickListener = onClickListener;
    }

    @Override
    public ConversationsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_conversations_list_view_holder, viewGroup, false);

        final int index = i;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(index);
                }
            }
        });

        return new ConversationsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationsListViewHolder conversationsListViewHolder, int i) {
        conversationsListViewHolder.contactName.setText(conversations[i][CONVERSATION_CONTACT_NAME_INDEX]);
        conversationsListViewHolder.conversationSnippet.setText(conversations[i][CONVERSATION_SNIPPET_INDEX]);
        conversationsListViewHolder.conversationTimestamp.setText(conversations[i][CONVERSATION_TIMESTAMP_INDEX]);
    }

    @Override
    public int getItemCount() {
        return conversations.length;
    }

    public String[] getConversation(int index){
        return conversations[index];
    }
}
