package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noahhuppert.reflect.database.UnknownSmsMessagesTable;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckForIncomingSmsMessagesRunnable extends ResultHandlerThread<String[][]> {
    private Context context;

    public CheckForIncomingSmsMessagesRunnable(Context context, ThreadResultHandler<String[][]> threadResultHandler) {
        super(threadResultHandler);
        this.context = context;
    }

    @Override
    protected String[][] execute() throws Exception {
        //Get sms messages
        String[] getUnknownMessagesProjection = {UnknownSmsMessagesTable.COLUMNS._ID};
        String getUnknownMessagesQuery = UnknownSmsMessagesTable.COLUMNS.STATE + " = ?";
        String[] getUnknownMessagesQueryArgs = {UnknownSmsMessagesTable.MESSAGE_STATES.INCOMING_RECEIVED};

        SQLiteDatabase unknownMessagesDatabase;

        synchronized (context){
            unknownMessagesDatabase = new UnknownSmsMessagesTable(context).getReadableDatabase();
        }

        Cursor getUnknownMessagesCursor = unknownMessagesDatabase.query(UnknownSmsMessagesTable.TABLE_NAME,
                getUnknownMessagesProjection,
                getUnknownMessagesQuery,
                getUnknownMessagesQueryArgs,
                null, null, null);

        if(getUnknownMessagesCursor.getCount() == 0){
            return new String[0][2];
        }

        String[] unknownMessageIds = new String[getUnknownMessagesCursor.getCount()];

        for(int i = 0; i < getUnknownMessagesCursor.getCount(); i++){
            getUnknownMessagesCursor.moveToPosition(i);
            unknownMessageIds[i] = getUnknownMessagesCursor.getString(0);
        }

        getUnknownMessagesCursor.close();
        unknownMessagesDatabase.close();

        //Resolve sms messages
        List<String[]> smsMessagesData = new ArrayList<>();

        synchronized (context){
            for(int i = 0; i < unknownMessageIds.length; i++){
                String[][] resolvedSmsMessagesData = UnknownSmsMessagesTable.ResolveSmsMessage(unknownMessageIds[i], context);

                for(int ii = 0; ii < resolvedSmsMessagesData.length; ii++){
                    smsMessagesData.add(resolvedSmsMessagesData[ii]);
                }
            }
        }

        String[][] smsMessagesDataArray = new String[smsMessagesData.size()][2];

        for(int i = 0; i < smsMessagesData.size(); i++){
            smsMessagesDataArray[i] = smsMessagesData.get(i);
        }

        return smsMessagesDataArray;
    }
}
