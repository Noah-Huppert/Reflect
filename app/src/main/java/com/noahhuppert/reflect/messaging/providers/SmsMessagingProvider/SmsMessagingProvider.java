package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.noahhuppert.reflect.exceptions.Errors;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessageType;
import com.noahhuppert.reflect.messaging.models.Contact;
import com.noahhuppert.reflect.messaging.models.Conversation;
import com.noahhuppert.reflect.messaging.models.Message;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * A Messaging Provider that uses Sms as its messaging resource
 */
public class SmsMessagingProvider implements MessagingProvider {
    private static final String TAG = SmsMessagingProvider.class.getSimpleName();

    private static final int snippetLimit = 45;

    private static class TempMessageId implements Comparable<TempMessageId> {
        public String id;
        public long date;

        public TempMessageId(String id, long date) {
            this.id = id;
            this.date = date;
        }

        @Override
        public int compareTo(TempMessageId another) {
            if(this.date > another.date) {
                return -1;
            } else if(this.date < another.date) {
                return 1;
            } else {
                return 0;
            }
        }
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

        if(getConversationIdsCursor == null) {
            return new String[0];
        }

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

        if(getSnippetCursor == null) {
            return null;
        }

        if(getSnippetCursor.getCount() == 0){
            getSnippetCursor.close();
            return null;
        }

        getSnippetCursor.moveToFirst();

        Conversation conversation = new Conversation();
        conversation.id = threadId;
        conversation.communicationType = CommunicationType.SMS;

        StringBuilder snippetBuilder = new StringBuilder();
        String[] snippetParts = getSnippetCursor.getString(0).split(" ");

        for(int i = 0; i < snippetParts.length && snippetBuilder.toString().length() + snippetParts[i].length() < snippetLimit; i++){
            snippetBuilder.append(snippetParts[i]);

            if(snippetBuilder.toString().length() + 1 < snippetLimit) {
                snippetBuilder.append(" ");
            }
        }

        conversation.snippet = snippetBuilder.toString();

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

        getRecentMessageCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                getRecentMessageProjection,
                getRecentMessageQuery,
                getRecentMessageQueryArgs,
                getRecentMessageOrderBy);

        if(getRecentMessageCursor == null) {
            return null;
        }

        if(getRecentMessageCursor.getCount() == 0){
            getRecentMessageCursor.close();
            return null;
        }

        getRecentMessageCursor.moveToFirst();

        conversation.lastMessageTimestamp = new Timestamp(getRecentMessageCursor.getLong(1));

        conversation.contacts = new Contact[1];
        conversation.contacts[0] = getContactFromUri(context, Contact.BuildSmsUri(getRecentMessageCursor.getString(0)));

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

        Cursor getContactCursor = context.getContentResolver().query(getContactUri,
                    getContactProjection,
                    null, null, null);

        Contact contact = new Contact();
        contact.uri = uri;
        contact.communicationType = CommunicationType.SMS;

        if(getContactCursor == null) {
            return contact;
        }

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
    private @Nullable Uri getContactPhotoUriFromPhotoId(@NonNull final Context context, String photoId){
        String[] getContactPhotoProjection = {ContactsContract.Data.PHOTO_THUMBNAIL_URI};
        String getContactPhotoQuery = ContactsContract.Data.PHOTO_ID + " = ?";
        String[] getContactPhotoQueryArgs = {photoId};

        Cursor getContactPhotoCursor = getContactPhotoCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    getContactPhotoProjection,
                    getContactPhotoQuery,
                    getContactPhotoQueryArgs,
                    null);

        if(getContactPhotoCursor == null) {
            return null;
        }

        if(getContactPhotoCursor.getCount() == 0){
            getContactPhotoCursor.close();
            return null;
        }

        getContactPhotoCursor.moveToFirst();

        Uri contactPhotoUri = Uri.parse(getContactPhotoCursor.getString(0));

        getContactPhotoCursor.close();

        return contactPhotoUri;
    }

    @NonNull
    @Override
    public String[] getConversationMessageIds(@NonNull Context context, @NonNull String conversationId) {
        String[] getConversationMessageIdsProjection = {
                BaseColumns._ID,
                Telephony.TextBasedSmsColumns.DATE
        };
        String getConversationMessageIdsQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
        String[] getConversationMessageIdsQueryArgs = {conversationId};
        String getConversationMessageIdsOrderBy = Telephony.TextBasedSmsColumns.DATE + " DESC";

        Cursor getConversationMessageIdsCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                getConversationMessageIdsProjection,
                getConversationMessageIdsQuery,
                getConversationMessageIdsQueryArgs,
                getConversationMessageIdsOrderBy);

        if(getConversationMessageIdsCursor == null) {
            return new String[0];
        }

        if(getConversationMessageIdsCursor.getCount() == 0) {
            getConversationMessageIdsCursor.close();
            return new String[0];
        }

        TempMessageId[] tempMessageIds = new TempMessageId[getConversationMessageIdsCursor.getCount()];

        for(int i = 0; i < getConversationMessageIdsCursor.getCount(); i++) {
            getConversationMessageIdsCursor.moveToPosition(i);

            tempMessageIds[i] = new TempMessageId(
                            getConversationMessageIdsCursor.getString(0),
                            getConversationMessageIdsCursor.getLong(1)
                    );
        }

        Arrays.sort(tempMessageIds);

        String[] messageIds = new String[tempMessageIds.length];

        for(int i = 0; i < tempMessageIds.length; i++) {
            messageIds[i] = tempMessageIds[i].id;
        }

        getConversationMessageIdsCursor.close();

        return messageIds;
    }

    @Nullable
    @Override
    public Message getMessage(@NonNull Context context, @NonNull String messageId) {
        /**
         * Message.id                = BaseColumns._ID                         = DB[0]
         * Message.otherPartyUris    = Telephony.TextBasedSmsColumns.ADDRESS   = DB[1]
         * Message.messageType       = Telephony.TextBasedSmsColumns.TYPE      = DB[2]
         * Message.error             = Int to bool                             = DB[3]
         * Message.communicationType = CommunicationType.SMS
         * Message.sentTimestamp     = Telephony.TextBasedSmsColumns.DATE_SENT = DB[4]
         * Message.receivedTimestamp = Telephony.TextBasedSmsColumns.DATE      = DB[5]
         * Message.body              = Telephony.TextBasedSmsColumns.BODY      = DB[6]
         * Message.read              = Telephony.TextBasedSmsColumns.READ      = DB[7]
         */
        String[] getMessageProjection = {
                BaseColumns._ID,
                Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.TYPE,
                Telephony.TextBasedSmsColumns.ERROR_CODE,
                Telephony.TextBasedSmsColumns.DATE_SENT,
                Telephony.TextBasedSmsColumns.DATE,
                Telephony.TextBasedSmsColumns.BODY,
                Telephony.TextBasedSmsColumns.READ
        };
        String getMessageQuery = BaseColumns._ID + " = ?";
        String[] getMessageQueryArgs = {messageId};

        Cursor getMessageCursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                getMessageProjection,
                getMessageQuery,
                getMessageQueryArgs,
                null);

        if(getMessageCursor == null) {
            return null;
        }

        if(getMessageCursor.getCount() == 0) {
            getMessageCursor.close();
            return null;
        }

        getMessageCursor.moveToPosition(0);

        Message message = new Message();

        message.id = getMessageCursor.getString(0);
        message.otherPartyUris = new Uri[]{
                Contact.BuildSmsUri(getMessageCursor.getString(1))
        };

        int type = getMessageCursor.getInt(2);

        if(type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_DRAFT) {
            message.messageType = MessageType.DRAFT;
        } else if(type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX || type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_FAILED || type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED) {
            message.messageType = MessageType.SENDING;
        } else if(type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX) {
            message.messageType = MessageType.RECEIVED;
        } else if(type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT) {
            message.messageType = MessageType.SENT;
        }

        if(getMessageCursor.getInt(3) == 0) {
            message.error = Errors.OK;
        } else {
            message.error = Errors.FAILED;
        }

        message.communicationType = CommunicationType.SMS;
        message.sentTimestamp = new Timestamp(getMessageCursor.getLong(4));
        message.receivedTimestamp = new Timestamp(getMessageCursor.getLong(5));
        message.body = getMessageCursor.getString(6);
        message.read = getMessageCursor.getInt(7) == 1;

        getMessageCursor.close();

        return message;
    }
}
