package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IncomingMessagesTable extends SQLiteOpenHelper {
    //TODO Figure out how to properly create and access instance of this table, look at TextSecure
    private static final int SCHEMA_VERSION = 1;
    public static final String TABLE_NAME = "incoming_messages";

    private static final String TABLE_QUERY_CREATE =
                                "CREATE TABLE ? (" +
                                        "? INTEGER AUTOINCREMENT," +
                                        "? TEXT," +
                                        "? TEXT," +
                                        "? INTEGER" +
                                ");";
    private static final String[] TABLE_QUERY_CREATE_ARGS = {
            TABLE_NAME,
            COLUMNS._ID,
            COLUMNS.SENDER_PHONE_NUMBER,
            COLUMNS.MESSAGE_BODY,
            COLUMNS.NOTIFICATION_ID
    };

    public interface COLUMNS {
        String _ID = "_id";
        String SENDER_PHONE_NUMBER = "sender_phone_number";
        String MESSAGE_BODY = "body";
        String NOTIFICATION_ID = "notification_id";
    }

    private static IncomingMessagesTable ourInstance;

    public static IncomingMessagesTable getInstance(Context context){
        if(ourInstance == null) {
            ourInstance = new IncomingMessagesTable(context);
        }

        return ourInstance;
    }

    private IncomingMessagesTable(Context context) {
        super(context, TABLE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_QUERY_CREATE, TABLE_QUERY_CREATE_ARGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
