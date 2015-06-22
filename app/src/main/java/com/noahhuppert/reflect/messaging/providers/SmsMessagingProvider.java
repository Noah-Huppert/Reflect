package com.noahhuppert.reflect.messaging.providers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;

import java.sql.Timestamp;

/**
 * A Messaging Provider that uses Sms as its messaging resource
 */
public class SmsMessagingProvider implements MessagingProvider {
    private static final String TAG = SmsMessagingProvider.class.getSimpleName();

    public static final String SMS_CONTACT_URI_SCHEME = "sms";

    @Override
    public Conversation[] getConversations(@NonNull final Context context) {
        //Get Conversation.id and Conversation.snippet
        String[] getConversationsProjection = {
                Telephony.Sms.Conversations.THREAD_ID,
                Telephony.Sms.Conversations.SNIPPET
        };
        String getConversationsOrderBy = Telephony.Sms.Conversations.THREAD_ID + " DESC";

        Cursor getConversationsCursor;

        synchronized (context){
            getConversationsCursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                    getConversationsProjection,
                    null, null,
                    getConversationsOrderBy);
        }

        Conversation[] conversations = new Conversation[getConversationsCursor.getCount()];

        if(conversations.length == 0){
            getConversationsCursor.close();
            return conversations;
        }

        for(int i = 0; i < conversations.length; i++){
            getConversationsCursor.moveToPosition(i);

            conversations[i] = new Conversation();
            conversations[i].id = getConversationsCursor.getString(0);
            conversations[i].snippet = getConversationsCursor.getString(1);
            conversations[i].communicationType = CommunicationType.SMS;
        }

        getConversationsCursor.close();

        //Get Conversation.contactUris
        String[] getOneReceivedConversationMessageProjection = {Telephony.TextBasedSmsColumns.ADDRESS};
        String getOneReceivedConversationMessageQuery = Telephony.Sms.Inbox.THREAD_ID + " = ?";
        String[] getOneReceivedConversationMessageQueryArgs = new String[1];
        String getOneReceivedConversationMessageOrderBy = Telephony.Sms.Inbox.DATE + " DESC LIMIT 1";

        Cursor getOneReceivedConversationMessageCursor;
        Uri.Builder contactUriBuilder = new Uri.Builder();

        for(int i = 0; i < conversations.length; i++) {
            getOneReceivedConversationMessageQueryArgs[0] = conversations[i].id;

            synchronized (context) {
                getOneReceivedConversationMessageCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                        getOneReceivedConversationMessageProjection,
                        getOneReceivedConversationMessageQuery,
                        getOneReceivedConversationMessageQueryArgs,
                        getOneReceivedConversationMessageOrderBy);
            }

            if(getOneReceivedConversationMessageCursor.getCount() == 0){
                getOneReceivedConversationMessageCursor.close();
                Log.e("Every SMS conversation should have at least one message", "THREAD_ID => " + conversations[i].id);
                continue;
            } else if(getOneReceivedConversationMessageCursor.getCount() > 1){
                Log.e(TAG, "Query to retrieve ONE conversation message is returning more than one message (" +
                        "THREAD_ID => " + conversations[i].id +
                        " Count => " + getOneReceivedConversationMessageCursor.getCount() +
                ")");
            }

            getOneReceivedConversationMessageCursor.moveToFirst();

            conversations[i].contacts = new Contact[1];
            conversations[i].contacts[0] = new Contact();
            conversations[i].contacts[0].uri = contactUriBuilder
                    .scheme(SMS_CONTACT_URI_SCHEME)
                    .authority(getOneReceivedConversationMessageCursor.getString(0))
                    .build();

            getOneReceivedConversationMessageCursor.close();
        }

        //Get Conversation.contactNames
        for(int i = 0; i < conversations.length; i++){
            conversations[i].contacts[0] = getContactFromUri(context, conversations[i].contacts[0].uri);
        }

        //Get Conversation.lastMessageTimestamp
        String[] getLastConversationMessageProjection = {Telephony.TextBasedSmsColumns.DATE};
        String getLastConversationMessageQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
        String[] getLastConversationMessageQueryArgs = new String[1];
        String getLastConversationMessageOrderBy = Telephony.TextBasedSmsColumns.DATE + " DESC LIMIT 1";

        Cursor getLastConversationMessageCursor;

        for(int i = 0; i < conversations.length; i++){
            getLastConversationMessageQueryArgs[0] = conversations[i].id;

            synchronized (context){
                getLastConversationMessageCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                        getLastConversationMessageProjection,
                        getLastConversationMessageQuery,
                        getLastConversationMessageQueryArgs,
                        null);
            }

            if(getLastConversationMessageCursor.getCount() == 0){
                getLastConversationMessageCursor.close();
                throw new WTFException("Every SMS conversation should have at least one message", "THREAD_ID => " + conversations[i].id);
            } else if(getLastConversationMessageCursor.getCount() > 1){
                /*Log.e(TAG, "Query to retrieve ONE conversation message is returning more than one message (" +
                        "THREAD_ID => " + conversations[i].id +
                        " Count => " + getLastConversationMessageCursor.getCount() +
                        ")");*/
            }

            getLastConversationMessageCursor.moveToFirst();

            conversations[i].lastMessageTimestamp = new Timestamp(getLastConversationMessageCursor.getLong(0));

            getLastConversationMessageCursor.close();
        }

        return conversations;
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

        Cursor getContactCursor;

        synchronized (context){
            getContactCursor = context.getContentResolver().query(getContactUri,
                    getContactProjection,
                    null, null, null);
        }

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

        Cursor getContactPhotoCursor;

        synchronized (context){
            getContactPhotoCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    getContactPhotoProjection,
                    getContactPhotoQuery,
                    getContactPhotoQueryArgs,
                    null);
        }

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
}
