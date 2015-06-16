package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.Telephony;
import android.util.Log;

import com.noahhuppert.reflect.exceptions.InvalidIdException;

public class UnknownSmsMessagesTable extends SQLiteOpenHelper {
    private static final int SCHEMA_VERSION = 1;
    public static final String TABLE_NAME = "unknown_sms_messages";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMNS._ID + " TEXT PRIMARY KEY NOT NULL, " +
                    COLUMNS.SMS_NUMBER + " TEXT NOT NULL, " +
                    COLUMNS.CONTENT + " TEXT NOT NULL, " +
                    COLUMNS.STATE + " INTEGER NOT NULL, " +
                    COLUMNS.NOTIFICATION_ID + " INTEGER NOT NULL" +
            ");";

    public interface COLUMNS{
        String _ID = "_id";
        String SMS_NUMBER = "sms_number";
        String CONTENT = "content";
        String STATE = "state";
        String NOTIFICATION_ID = "notification_id";
    }

    public interface MESSAGE_STATES{
        String INCOMING_RECEIVED = "incoming_received";
        String OUTGOING_SENT = "outgoing_sent";
    }

    public UnknownSmsMessagesTable(Context context){
        super(context, TABLE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /* Actions */
    /**
     * Queryies the system SMS tables to try and resolve a unknown message
     * @param unknownMessageId The unknown message id to resolve
     * @param context The context used to get databases
     * @return A String array, the first item is the message id, the second item is the thread id
     * @throws InvalidIdException If the unknown message id is invalid or no sms messages exist with
     * the unknown message content
     */
    public static String[][] ResolveSmsMessage(String unknownMessageId, Context context) throws InvalidIdException{
        //Get Unknown message
        SQLiteDatabase unknownMessagesDatabase;

        synchronized (context){
            unknownMessagesDatabase = new UnknownSmsMessagesTable(context).getReadableDatabase();
        }

        String[] getUnknownMessageProjection = {COLUMNS.CONTENT, COLUMNS.SMS_NUMBER};
        String getUnknownMessageQuery = COLUMNS._ID + " = ?";
        String[] getUnknownMessageQueryArgs = {unknownMessageId};

        Cursor getUnknownMessageCursor = unknownMessagesDatabase.query(UnknownSmsMessagesTable.TABLE_NAME,
                getUnknownMessageProjection,
                getUnknownMessageQuery,
                getUnknownMessageQueryArgs,
                null, null, null);

        if(getUnknownMessageCursor.getCount() == 0){
            throw new InvalidIdException("The provided unknown message id does not exist", unknownMessageId);
        }

        getUnknownMessageCursor.moveToFirst();

        String unknownMessageContent = getUnknownMessageCursor.getString(0);
        String unknownMessageSmsNumber = getUnknownMessageCursor.getString(1);

        getUnknownMessageCursor.close();
        unknownMessagesDatabase.close();

        //Query SMS database
        String[] getSmsMessagesProjection = {BaseColumns._ID, Telephony.TextBasedSmsColumns.THREAD_ID};
        String getSmsMessagesQuery = Telephony.TextBasedSmsColumns.ADDRESS + " = ? AND " +
                                    Telephony.TextBasedSmsColumns.BODY + " = ?";
        String[] getSmsMessagesQueryArgs = {unknownMessageSmsNumber, unknownMessageContent};

        Cursor getSmsMessagesCursor;

        synchronized (context){
            getSmsMessagesCursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                    getSmsMessagesProjection,
                    getSmsMessagesQuery,
                    getSmsMessagesQueryArgs,
                    null);
        }

        Log.d("OMG", getSmsMessagesCursor.toString());

        if(getSmsMessagesCursor.getCount() == 0){
            throw new InvalidIdException("The retrieved unknown sms message does match a stored sms message",
                    "\"Sender Phone Number: \"" + unknownMessageSmsNumber + "\" Content: \"" + unknownMessageContent + "\"");
        }

        String[][] smsMessagesData = new String[getSmsMessagesCursor.getCount()][2];

        for(int i = 0; i < getSmsMessagesCursor.getCount(); i++){
            getSmsMessagesCursor.moveToPosition(i);
            smsMessagesData[i][0] = getSmsMessagesCursor.getString(0);
            smsMessagesData[i][1] = getSmsMessagesCursor.getString(1);
        }

        getSmsMessagesCursor.close();

        return smsMessagesData;
    }
}
