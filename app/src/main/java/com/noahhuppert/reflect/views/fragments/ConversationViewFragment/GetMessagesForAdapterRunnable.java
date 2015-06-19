package com.noahhuppert.reflect.views.fragments.ConversationViewFragment;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class GetMessagesForAdapterRunnable extends ResultHandlerThread<String[][]> {
    private final Context context;
    private final String threadId;

    public GetMessagesForAdapterRunnable(Context context, String threadId, ThreadResultHandler<String[][]> threadResultHandler){
        super(threadResultHandler);
        this.context = context;
        this.threadId = threadId;
    }

    @Override
    protected String[][] execute() throws Exception {
        String[] getMessagesProjection = {Telephony.TextBasedSmsColumns.BODY, Telephony.TextBasedSmsColumns.TYPE};
        String getMessagesQuery = Telephony.TextBasedSmsColumns.THREAD_ID + " = ?";
        String[] getMessagesQueryArgs;

        synchronized (threadId){
            getMessagesQueryArgs = new String[]{threadId};
        }

        String getMessagesOrderBy = Telephony.TextBasedSmsColumns.DATE_SENT + " ASC";

        Cursor cursor;

        String ourNumber;

        synchronized (context){
            cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                    getMessagesProjection,
                    getMessagesQuery,
                    getMessagesQueryArgs,
                    getMessagesOrderBy);

            ourNumber = TelephonyUtils.GetTelephonyManager(context).getLine1Number();
        }

        String[][] conversations = new String[cursor.getCount()][3];

        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            conversations[i][ConversationViewAdapter.MESSAGE_BODY_INDEX] = cursor.getString(0);

            if(cursor.getInt(1) == Telephony.Sms.MESSAGE_TYPE_SENT) {
                conversations[i][ConversationViewAdapter.MESSAGE_OURS_INDEX] = ConversationViewAdapter.MESSAGE_OURS_TRUE_VALUE;
            } else {
                conversations[i][ConversationViewAdapter.MESSAGE_OURS_INDEX] = ConversationViewAdapter.MESSAGE_OURS_FALSE_VALUE;
            }
        }

        cursor.close();

        return conversations;
    }
}
