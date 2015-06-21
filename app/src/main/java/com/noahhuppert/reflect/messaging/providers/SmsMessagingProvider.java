package com.noahhuppert.reflect.messaging.providers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;
import com.noahhuppert.reflect.exceptions.WTFException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.utils.TelephonyUtils;

import java.sql.Timestamp;

/**
 * A Messaging Provider that uses Sms as its messaging resource
 */
public class SmsMessagingProvider implements MessagingProvider {
    private static final String TAG = SmsMessagingProvider.class.getSimpleName();

    public static final String SMS_CONTACT_URI_SCHEME = "sms";

    @Override
    public Conversation[] getConversations(@NonNull Context context) {
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
                getOneReceivedConversationMessageCursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                        getOneReceivedConversationMessageProjection,
                        getOneReceivedConversationMessageQuery,
                        getOneReceivedConversationMessageQueryArgs,
                        getOneReceivedConversationMessageOrderBy);
            }

            if(getOneReceivedConversationMessageCursor.getCount() == 0){
                try {
                    throw new WTFException("Every SMS conversation should have at least one message", "THREAD_ID => " + conversations[i].id);
                } finally {
                    getOneReceivedConversationMessageCursor.close();
                }
            } else if(getOneReceivedConversationMessageCursor.getCount() > 1){
                Log.e(TAG, "Query to retrieve ONE conversation message is returning more than one message (" +
                        "THREAD_ID => " + conversations[i].id +
                        " Count => " + getOneReceivedConversationMessageCursor.getCount() +
                ")");
            }

            getOneReceivedConversationMessageCursor.moveToFirst();

            conversations[i].contactUris = new Uri[1];
            conversations[i].contactUris[0] = contactUriBuilder
                    .scheme(SMS_CONTACT_URI_SCHEME)
                    .authority(getOneReceivedConversationMessageCursor.getString(0))
                    .build();

            getOneReceivedConversationMessageCursor.close();
        }

        //Get Conversation.contactNames
        for(int i = 0; i < conversations.length; i++){
            conversations[i].contactNames = new String[1];
            conversations[i].contactNames[0] = getContactDisplayNameForUri(context, conversations[i].contactUris[0]);
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
                getLastConversationMessageCursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                        getLastConversationMessageProjection,
                        getLastConversationMessageQuery,
                        getLastConversationMessageQueryArgs,
                        null);
            }

            if(getLastConversationMessageCursor.getCount() == 0){
                getLastConversationMessageCursor.close();
                throw new WTFException("Every SMS conversation should have at least one message", "THREAD_ID => " + conversations[i].id);
            } else if(getLastConversationMessageCursor.getCount() > 1){
                Log.e(TAG, "Query to retrieve ONE conversation message is returning more than one message (" +
                        "THREAD_ID => " + conversations[i].id +
                        " Count => " + getLastConversationMessageCursor.getCount() +
                        ")");
            }

            getLastConversationMessageCursor.moveToFirst();

            conversations[i].lastMessageTimestamp = new Timestamp(getLastConversationMessageCursor.getLong(0));

            getLastConversationMessageCursor.close();
        }

        return conversations;
    }

    @Override
    public @NonNull String getContactDisplayNameForUri(@NonNull Context context, @NonNull Uri contactUri) {
        String[] getContactNameProjection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Uri getContactNameUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, contactUri.getAuthority());

        Cursor getContactNameCursor;

        synchronized (context){
            getContactNameCursor = context.getContentResolver().query(getContactNameUri,
                    getContactNameProjection,
                    null, null, null);
        }

        if(getContactNameCursor.getCount() == 0){
            getContactNameCursor.close();

            return PhoneNumberUtils.formatNumber(contactUri.getAuthority());
        }

        getContactNameCursor.moveToFirst();

        try {
            return getContactNameCursor.getString(0);
        } finally {
            getContactNameCursor.close();
        }
    }
}
