package com.noahhuppert.reflect.views.fragments.ConversationsListFragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.util.SimpleArrayMap;
import android.text.format.DateUtils;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class GetConversationsForAdapterRunnable extends ResultHandlerThread<String[][]> {
    private final Context context;

    public GetConversationsForAdapterRunnable(Context context, ThreadResultHandler<String[][]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected String[][] execute() throws Exception {
        //Get conversation snippets
        String[] getConversationsProjection = {
                Telephony.TextBasedSmsColumns.THREAD_ID,
                Telephony.Sms.Conversations.SNIPPET
        };
        String getConversationsOrderBy = Telephony.TextBasedSmsColumns.THREAD_ID + " DESC";

        Cursor getConversationsCursor;

        synchronized (context){
            getConversationsCursor = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI,
                    getConversationsProjection,
                    null, null,
                    getConversationsOrderBy);
        }

        SimpleArrayMap<String, String[]> conversations = new SimpleArrayMap<>();

        for(int i = 0; i < getConversationsCursor.getCount(); i ++){
            getConversationsCursor.moveToPosition(i);
            conversations.put(getConversationsCursor.getString(0), new String[5]);
            conversations.get(getConversationsCursor.getString(0))[ConversationsListAdapter.CONVERSATION_SNIPPET_INDEX] = getConversationsCursor.getString(1);
            conversations.get(getConversationsCursor.getString(0))[ConversationsListAdapter.CONVERSATION_THREAD_ID_INDEX] = getConversationsCursor.getString(0);
        }

        getConversationsCursor.close();

        //Get timestamp
        String[] getTimestampProjection = {Telephony.TextBasedSmsColumns.DATE, Telephony.TextBasedSmsColumns.ADDRESS, Telephony.TextBasedSmsColumns.THREAD_ID};
        String getTimestampOrderBy = Telephony.TextBasedSmsColumns.THREAD_ID + ", " + BaseColumns._ID + " DESC";

        for(int i = 0; i < conversations.size(); i++){
            Cursor cursor;

            String query = Telephony.TextBasedSmsColumns.THREAD_ID + " = ? AND " + Telephony.TextBasedSmsColumns.ADDRESS + " != ?";
            String[] queryArgs;

            synchronized (context) {
                queryArgs = new String[]{conversations.keyAt(i), TelephonyUtils.GetTelephonyManager(context).getLine1Number()};
            }

            synchronized (context){
                cursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                        getTimestampProjection,
                        query,
                        queryArgs,
                        getTimestampOrderBy);
            }

            if(cursor.getCount() != 0) {
                cursor.moveToFirst();

                conversations.get(cursor.getString(2))[ConversationsListAdapter.CONVERSATION_TIMESTAMP_INDEX] = DateUtils.getRelativeTimeSpanString(cursor.getLong(0), System.currentTimeMillis(), 0) + "";
                conversations.get(cursor.getString(2))[ConversationsListAdapter.CONVERSATION_CONTACT_NAME_INDEX] = cursor.getString(1);
                conversations.get(cursor.getString(2))[ConversationsListAdapter.CONVERSATION_CONTACT_NUMBER_INDEX] = cursor.getString(1);
            }

            cursor.close();
        }

        //Get contact name
        String[] getContactNameProjection = {ContactsContract.Contacts.DISPLAY_NAME};

        for(int i = 0; i < conversations.size(); i++){
            Uri queryUri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
                    .appendEncodedPath(conversations.valueAt(i)[ConversationsListAdapter.CONVERSATION_CONTACT_NAME_INDEX])
                    .build();

            Cursor cursor;

            synchronized (context){
                cursor = context.getContentResolver().query(queryUri,
                        getContactNameProjection,
                        null, null, null);
            }

            if(cursor.getCount() != 0) {
                cursor.moveToFirst();

                conversations.valueAt(i)[ConversationsListAdapter.CONVERSATION_CONTACT_NAME_INDEX] = cursor.getString(0);
            }

            cursor.close();
        }

        String[][] conversationArray = new String[conversations.size()][4];

        for(int i = 0; i < conversations.size(); i++){
            conversationArray[i] = conversations.valueAt(i);
        }

        return conversationArray;
    }
}
