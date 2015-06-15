package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noahhuppert.reflect.database.UnknownSmsMessagesTable;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class CheckForIncomingSmsMessagesRunnable extends ResultHandlerThread<String[][]> {
    private Context context;

    public CheckForIncomingSmsMessagesRunnable(Context context, ThreadResultHandler<String[][]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected String[][] execute() throws Exception {
        String[] getUnknownIncomingMessageProjection = {
                UnknownSmsMessagesTable.COLUMNS._ID
        };
        String getUnknownIncomingMessageQuery = UnknownSmsMessagesTable.COLUMNS.STATE + " = ?";
        String[] getUnknownIncomingMessagesQueryArgs = {UnknownSmsMessagesTable.MESSAGE_STATES.INCOMING_RECEIVED};

        SQLiteDatabase unknownMessagesDb;

        synchronized (context){
            unknownMessagesDb = new UnknownSmsMessagesTable(context).getReadableDatabase();
        }

        Cursor getUnknownMessagesCursor = unknownMessagesDb.query(UnknownSmsMessagesTable.TABLE_NAME,
                getUnknownIncomingMessageProjection,
                getUnknownIncomingMessageQuery,
                getUnknownIncomingMessagesQueryArgs,
                null, null, null);

        if(getUnknownMessagesCursor.getCount() == 0){
            return new String[0][];
        }

        String[] unknownMessageIds = new String[getUnknownMessagesCursor.getCount()];

        for(int i = 0; i < getUnknownMessagesCursor.getCount(); i++){
            getUnknownMessagesCursor.moveToPosition(i);

            unknownMessageIds[i] = getUnknownMessagesCursor.getString(0);
        }

        String[][] incomingMessageData = new String[unknownMessageIds.length][2];

        synchronized (context) {
            for (int i = 0; i < unknownMessageIds.length; i++) {
                incomingMessageData[i] = UnknownSmsMessagesTable.ResolveSmsMessage(
                        unknownMessageIds[i],
                        context
                );
            }
        }

        return incomingMessageData;
    }
}
