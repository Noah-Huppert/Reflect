package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.caches.CircleTileDrawableLruCache;
import com.noahhuppert.reflect.messaging.models.Conversation;


public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {
    public Conversation[] conversations;
    private final Context context;

    public ConversationListAdapter(Conversation[] conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView contactsAvatar;
        public TextView contactsName;

        public ViewHolder(View itemView) {
            super(itemView);

            contactsAvatar = (ImageView) itemView.findViewById(R.id.contacts_avatar);
            contactsName = (TextView) itemView.findViewById(R.id.contacts_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_conversation, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String contactsName = "";

        if(conversations[i].contacts.length == 1){
            contactsName = conversations[i].contacts[0].getNonNullName();
            Drawable contactDrawable;

            synchronized (context){
                contactDrawable = conversations[i].contacts[0].getNonNullAvatarDrawable(context);
            }

            //TODO Find out why conversations[i].contacts is changing every line !!!!!!

            if (!conversations[i].contacts[0].willReturnCircleTileWithLetter()) {
                Log.d("TAG", conversations[i].contacts[0].getNonNullName() + " => " + conversations[i].contacts[0].willReturnCircleTileWithLetter() + " => " + conversations[i].contacts[0].name);
                viewHolder.contactsAvatar.setImageResource(R.drawable.ic_person_white_48dp);
                viewHolder.contactsAvatar.setPadding(20, 20, 20, 20);
                viewHolder.contactsAvatar.setBackground(contactDrawable);
            } else {
                viewHolder.contactsAvatar.setImageDrawable(contactDrawable);
            }
        } else if (conversations[i].contacts.length > 1){
            for(int contactIndex = 0; contactIndex < conversations[i].contacts.length; contactIndex++){
                contactsName += conversations[i].contacts[contactIndex].getNonNullName();

                if(contactIndex != conversations[i].contacts.length - 1){
                    contactsName += ", ";
                }
            }

            Drawable contactDrawable = CircleTileDrawableLruCache.getInstance().get(contactsName, "");

            viewHolder.contactsAvatar.setImageResource(R.drawable.ic_people_white_48dp);
            viewHolder.contactsAvatar.setPadding(20, 20, 20, 20);
            viewHolder.contactsAvatar.setBackground(contactDrawable);
        }

        viewHolder.contactsName.setText(contactsName);
    }

    @Override
    public int getItemCount() {
        return conversations.length;
    }
}
