package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
