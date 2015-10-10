package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.models.Message;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Timestamp;

/**
 * A Messaging Provider that uses Sms as its messaging resource
 */
public class SmsMessagingProvider implements MessagingProvider {
    private static final String TAG = SmsMessagingProvider.class.getSimpleName();

    private static final int snippetLimit = 35;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            HandlerMessagePayload.CONVERSATION_IDS
    })
    public @interface HandlerMessagePayload{
        int CONVERSATION_IDS = 0;
        int CONVERSATION = 1;
    }

    @NonNull
    @Override
    public String[] getConversationIds(@NonNull final Context context) {
        //Get conversation ids
        String[] getConversationIdsProjection = {Telephony.Sms.Conversations.THREAD_ID};
        String getConversationIdsOrderBy = Telephony.Sms.Conversations.DATE + " DESC";

        Cursor getConversationIdsCursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                    getConversationIdsProjection,
                    null, null,
                    getConversationIdsOrderBy);

        if(getConversationIdsCursor.getCount() == 0){
            getConversationIdsCursor.close();
            return new String[0];
        }

        String[] conversationIds = new String[getConversationIdsCursor.getCount()];

        for(int i = 0; i < getConversationIdsCursor.getCount(); i++){
            getConversationIdsCursor.moveToPosition(i);
            conversationIds[i] = getConversationIdsCursor.getString(0);
        }

        getConversationIdsCursor.close();

        return conversationIds;
    }

    @Override
    public @Nullable Conversation getConversation(@NonNull final Context context, @NonNull String threadId) {
        //Get Conversation.snippet
        String[] getSnippetProjection = {Telephony.Sms.Conversations.SNIPPET};
        String getSnippetQuery = Telephony.Sms.Conversations.THREAD_ID + " = ?";
        String[] getSnippetQueryArgs = {threadId};

        Cursor getSnippetCursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                    getSnippetProjection,
                    getSnippetQuery,
                    getSnippetQueryArgs,
                    null);

        if(getSnippetCursor.getCount() == 0){
            getSnippetCursor.close();
            return null;
        }

        getSnippetCursor.moveToFirst();

        Conversation conversation = new Conversation();
        conversation.id = threadId;
        conversation.communicationType = CommunicationType.SMS;

        conversation.snippet = "";
        String[] snippetParts = getSnippetCursor.getString(0).split(" ");

        for(int i = 0; i < snippetParts.length && conversation.snippet.length() < snippetLimit; i++){
            conversation.snippet += snippetParts[i] + " ";
        }

        getSnippetCursor.close();

        //Get Conversation.lastMessageTimestamp and Conversation.contacts
        String[] getRecentMessageProjection = {
                Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.DATE
        };
        String getRecentMessageQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
        String[] getRecentMessageQueryArgs = {threadId};
        String getRecentMessageOrderBy = Telephony.TextBasedSmsColumns.DATE + " DESC LIMIT 1";

        Cursor getRecentMessageCursor;

        //synchronized (context){
            getRecentMessageCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                    getRecentMessageProjection,
                    getRecentMessageQuery,
                    getRecentMessageQueryArgs,
                    getRecentMessageOrderBy);
        //}

        if(getRecentMessageCursor.getCount() == 0){
            getRecentMessageCursor.close();
            return null;
        }

        getRecentMessageCursor.moveToFirst();

        conversation.lastMessageTimestamp = new Timestamp(getRecentMessageCursor.getLong(1));

        conversation.contacts = new Contact[1];
        conversation.contacts[0] = getContactFromUri(context,
                new Uri.Builder()
                    .scheme(Contact.SMS_SCHEME)
                    .authority(getRecentMessageCursor.getString(0))
                    .build()
        );

        getRecentMessageCursor.close();

        return conversation;
    }

    @Override
    public @NonNull Contact getContactFromUri(@NonNull final Context context, @NonNull Uri uri) {
        String[] getContactProjection = {
                ContactsContract.PhoneLookup.LOOKUP_KEY,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.PHOTO_ID,
                ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
        };
        Uri getContactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(uri.getAuthority()));

        Cursor getContactCursor = getContactCursor = context.getContentResolver().query(getContactUri,
                    getContactProjection,
                    null, null, null);

        Contact contact = new Contact();
        contact.uri = uri;
        contact.communicationType = CommunicationType.SMS;

        if(getContactCursor.getCount() == 0){
            getContactCursor.close();
            return contact;
        }

        getContactCursor.moveToFirst();

        contact.id = getContactCursor.getString(0);
        contact.name = getContactCursor.getString(1);

        if(getContactCursor.getString(2) != null){
            contact.avatarUri = getContactPhotoUriFromPhotoId(context, getContactCursor.getString(2));
        } else if(getContactCursor.getString(3) != null){
            contact.avatarUri = Uri.parse(getContactCursor.getString(3));
        }

        getContactCursor.close();

        return contact;
    }

    @WorkerThread
    public @Nullable Uri getContactPhotoUriFromPhotoId(@NonNull final Context context, String photoId){
        String[] getContactPhotoProjection = {ContactsContract.Data.PHOTO_THUMBNAIL_URI};
        String getContactPhotoQuery = ContactsContract.Data.PHOTO_ID + " = ?";
        String[] getContactPhotoQueryArgs = {photoId};

        Cursor getContactPhotoCursor = getContactPhotoCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    getContactPhotoProjection,
                    getContactPhotoQuery,
                    getContactPhotoQueryArgs,
                    null);

        if(getContactPhotoCursor.getCount() == 0){
            getContactPhotoCursor.close();
            return null;
        }

        getContactPhotoCursor.moveToFirst();

        try {
            return Uri.parse(getContactPhotoCursor.getString(0));
        } finally {
            getContactPhotoCursor.close();
        }
    }

    @NonNull
    @Override
    public String[] getConversationMessageIds(@NonNull Context context, @NonNull String conversationId) {
        // TODO Get conversation message ids query, uri => Sms.Messages.ContentUri, projection => [_ID], query => THREAD_ID = conversationId
        return new String[0];
    }

    @NonNull
    @Override
    public Message getMessage(@NonNull Context context, @NonNull String messageId) {
        // TODO Get message query
        return null;
    }
}
