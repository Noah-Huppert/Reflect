package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.noahhuppert.reflect.exceptions.InvalidIdException;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;

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
    public static String[] ResolveSmsMessage(String unknownMessageId, Context context) throws InvalidIdException{
        //Get Unknown message
        String[] getUnknownSmsMessageProjection = {
                COLUMNS._ID,
                COLUMNS.SMS_NUMBER,
                COLUMNS.CONTENT
        };
        String getUnknownSmsMessageQuery = COLUMNS._ID + " = ?";
        String[] getUnknownSmsMessageQueryArgs = {unknownMessageId};

        SQLiteDatabase unknownMessagesTable;

        synchronized (context){
            unknownMessagesTable = new UnknownSmsMessagesTable(context).getReadableDatabase();
        }

        Cursor unknownMessageQueryCursor = unknownMessagesTable.query(TABLE_NAME,
                getUnknownSmsMessageProjection,
                getUnknownSmsMessageQuery,
                getUnknownSmsMessageQueryArgs,
                null, null, null);

        if(unknownMessageQueryCursor.getCount() == 0){
            throw new InvalidIdException("The provided unknown message id does not point to any message", unknownMessageId);
        }

        unknownMessageQueryCursor.moveToFirst();

        String unknownMessageSenderPhoneNumber = unknownMessageQueryCursor.getString(1);
        String unknownMessageBody = unknownMessageQueryCursor.getString(2);

        unknownMessageQueryCursor.close();
        unknownMessagesTable.close();

        //Query SMS tables
        String[] getSmsMessageProjection = {
                BaseColumns._ID,
                Telephony.TextBasedSmsColumns.THREAD_ID
        };
        String getSmsMessageQuery = Telephony.TextBasedSmsColumns.ADDRESS + " = ? AND " +
                Telephony.TextBasedSmsColumns.BODY + " = ?";
        String[] getSmsMessageQueryArgs = {
                unknownMessageSenderPhoneNumber,
                unknownMessageBody
        };

        Cursor smsMessageQueryCursor;

        synchronized (context){
            smsMessageQueryCursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                    getSmsMessageProjection,
                    getSmsMessageQuery,
                    getSmsMessageQueryArgs,
                    null);
        }

        if(smsMessageQueryCursor.getCount() == 0){
            throw new InvalidIdException("The provided unknown message content and sender do not point to any message",
                    "Sender: \"" + unknownMessageSenderPhoneNumber + "\" Content: \"" + unknownMessageBody + "\"");
        }

        smsMessageQueryCursor.moveToFirst();

        String smsMessageId = smsMessageQueryCursor.getString(0);
        String smsThreadId = smsMessageQueryCursor.getString(1);

        return new String[]{smsMessageId, smsThreadId};
    }
}
