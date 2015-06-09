package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.messaging.models.ReflectConversation;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProvider;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsMessagingProvider implements MessagingProvider {
    public static final String SMS_URI_SCHEME = "sms";
    /**
     * A list of columns that should be retrieve by DB when getting a message
     *
     * Note:
     *  If this array is every changed you must change {@link SmsGetMessageRunnable#reflectMessageFromCursor(Cursor)}
     *  because it is dependant on the order of this array
     */
    public static final String[] SMS_MESSAGE_PROJECTION = {
            BaseColumns._ID,//ReflectMessage.id
            Telephony.TextBasedSmsColumns.ADDRESS,//ReflectMessage.receiverUri
            Telephony.TextBasedSmsColumns.BODY,//ReflectMessage.body
            Telephony.TextBasedSmsColumns.DATE_SENT,//ReflectMessage.sentTimestamp
            Telephony.TextBasedSmsColumns.DATE,//ReflectMessage.receivedTimestamp
            Telephony.TextBasedSmsColumns.READ,//ReflectMessage.read
            Telephony.TextBasedSmsColumns.SEEN//ReflectMessage.seen
    };

    /**
     * A list of columns that should be retrieved by DB when getting a conversation
     */
    public static final String[] SMS_CONVERSATION_PROJECTION = {
            Telephony.TextBasedSmsColumns.THREAD_ID,//ReflectConversation.id
            Telephony.Sms.Conversations.SNIPPET//ReflectConversation.snippet
    };

    /**
     * A list of columns that should be retrieved by DB when getting a conversation
     */
    public static final String[] SMS_CONTACT_PROJECTION = {
            ContactsContract.Contacts.LOOKUP_KEY,//ReflectContact.id
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,//ReflectContact.displayName
            ContactsContract.Contacts.PHOTO_URI//ReflectContact.avatarUri
    };

    @Override
    public void getMessage(String id, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        long messageId = Long.parseLong(id);
        SmsGetMessageRunnable smsGetMessageRunnable = new SmsGetMessageRunnable(messageId, context, threadResultHandler);

        MainThreadPool.getInstance().getPool().submit(smsGetMessageRunnable);
    }

    @Override
    public void getConversation(String id, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) {
        long conversationId = Long.parseLong(id);
        SmsGetConversationRunnable smsGetConversationRunnable = new SmsGetConversationRunnable(conversationId, context, threadResultHandler);

        MainThreadPool.getInstance().getPool().submit(smsGetConversationRunnable);
    }

    @Override
    public void getContact(String id, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
        long contactId = Long.parseLong(id);
        SmsGetContactRunnable smsGetContactRunnable = new SmsGetContactRunnable(contactId, context, threadResultHandler);

        MainThreadPool.getInstance().getPool().submit(smsGetContactRunnable);
    }

    @Override
    public void getConversationIds(Context context, ThreadResultHandler<String> threadResultHandler) {
        //TODO Implement listing of conversation ids
    }

    @Override
    public void createMessage(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) throws InvalidMessagingProviderPushData {
        //TODO Re-Implement sending of messages
    }

    @Override
    public void createConversation(ReflectConversation reflectConversation, Context context, ThreadResultHandler<ReflectConversation> threadResultHandler) throws InvalidMessagingProviderPushData {

    }

    @Override
    public void createContact(ReflectContact reflectContact, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) throws InvalidMessagingProviderPushData {

    }
}
