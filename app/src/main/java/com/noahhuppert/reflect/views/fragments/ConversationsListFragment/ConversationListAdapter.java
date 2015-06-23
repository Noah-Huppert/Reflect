package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.caches.CircleTileDrawableLruCache;
import com.noahhuppert.reflect.caches.ContactAvatarLruCache;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;

import java.sql.Timestamp;


public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {
    private static final String TAG = ConversationListAdapter.class.getSimpleName();

    public String[] conversationIds;
    private final Context context;
    public SimpleArrayMap<String, Conversation> conversationsCache = new SimpleArrayMap<>();

    public ConversationListAdapter(String[] conversationIds, Context context) {
        this.conversationIds = conversationIds;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView contactsAvatar;
        public TextView contactsName;
        public TextView conversationSnippet;
        public TextView conversationLastMessageTimestamp;

        private @AvatarLayoutType int avatarLayoutType;

        public ViewHolder(View itemView) {
            super(itemView);

            contactsAvatar = (ImageView) itemView.findViewById(R.id.contacts_avatar);
            contactsName = (TextView) itemView.findViewById(R.id.contacts_name);
            conversationSnippet = (TextView) itemView.findViewById(R.id.conversation_snippet);
            conversationLastMessageTimestamp = (TextView) itemView.findViewById(R.id.conversation_last_message_timestamp);
        }

        public void switchToAvatarLayoutType(@AvatarLayoutType int avatarLayoutType) {
            if(this.avatarLayoutType == avatarLayoutType){
                return;
            }

            this.avatarLayoutType = avatarLayoutType;

            if(avatarLayoutType == AvatarLayoutType.NO_PADDING){
                contactsAvatar.setBackground(null);
                contactsAvatar.setPadding(0, 0, 0, 0);
            } else if(avatarLayoutType == AvatarLayoutType.PADDING){
                contactsAvatar.setPadding(20, 20, 20, 20);
            }
        }

        @IntDef({
                AvatarLayoutType.NO_PADDING,
                AvatarLayoutType.PADDING
        })
        public @interface AvatarLayoutType {
            int NO_PADDING = 0;
            int PADDING = 1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_conversation, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        String contactsName = "";

        Conversation conversation;

        if(conversationsCache.containsKey(conversationIds[i])){
            conversation = conversationsCache.get(conversationIds[i]);
        } else {
            conversation = new Conversation();
            conversation.lastMessageTimestamp = new Timestamp(System.currentTimeMillis());
            conversation.communicationType = CommunicationType.SMS;
            conversation.contacts = new Contact[1];
            conversation.contacts[0] = new Contact();
            conversation.contacts[0].name = "Loading";
            conversation.snippet = "Loading";

            conversationsCache.put(conversationIds[i], conversation);
        }

        String contactNames = "";
        for(int ci = 0; ci < conversation.contacts.length; ci++){
            contactNames += conversation.contacts[ci].getNonNullName();

            if(ci != conversation.contacts.length - 1){
                contactNames += ", ";
            }
        }
        viewHolder.contactsName.setText(contactNames);

        if(conversation.contacts.length > 1 || !Character.isLetter(contactNames.charAt(0))){
            viewHolder.contactsAvatar.setBackground(CircleTileDrawableLruCache.getInstance().get(contactNames, ""));

            viewHolder.switchToAvatarLayoutType(ViewHolder.AvatarLayoutType.PADDING);

            if(conversation.contacts.length > 1) {
                viewHolder.contactsAvatar.setImageResource(R.drawable.ic_people_white_48dp);
            } else {
                viewHolder.contactsAvatar.setImageResource(R.drawable.ic_person_white_48dp);
            }
        } else if(conversation.contacts[0].avatarUri != null){
            viewHolder.switchToAvatarLayoutType(ViewHolder.AvatarLayoutType.NO_PADDING);

            viewHolder.contactsAvatar.setImageDrawable(ContactAvatarLruCache.getInstance().get(conversation.contacts[0].avatarUri, context));
        }else{
            viewHolder.switchToAvatarLayoutType(ViewHolder.AvatarLayoutType.NO_PADDING);

            viewHolder.contactsAvatar.setImageDrawable(CircleTileDrawableLruCache.getInstance().get(contactNames, contactNames.charAt(0) + ""));
        }

        viewHolder.conversationSnippet.setText(conversation.snippet);
        viewHolder.conversationLastMessageTimestamp.setText(DateUtils.getRelativeTimeSpanString(conversation.lastMessageTimestamp.getTime(), System.currentTimeMillis(), 0));
    }

    @Override
    public int getItemCount() {
        return conversationIds.length;
    }
}
