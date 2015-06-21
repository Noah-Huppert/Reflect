package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.databinding.ViewHolderConversationBinding;
import com.noahhuppert.reflect.messaging.models.Conversation;

import java.util.Arrays;


public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {
    private Conversation[] conversations;

    public ConversationListAdapter(Conversation[] conversations) {
        this.conversations = conversations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolderConversationBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ViewHolderConversationBinding.bind(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_conversation, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.binding.setConversation(conversations[i]);
    }

    @Override
    public int getItemCount() {
        return conversations.length;
    }
}
