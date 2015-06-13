package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IncomingSmsTable extends SQLiteOpenHelper {
    private static final int SCHEMA_VERSION = 2;
    public static final String TABLE_NAME = "incoming_sms_messages";

    private static final String TABLE_QUERY_CREATE =
                                "CREATE TABLE " + TABLE_NAME + " (" +
                                        COLUMNS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        COLUMNS.SENDER_PHONE_NUMBER + " TEXT NOT NULL, " +
                                        COLUMNS.MESSAGE_BODY + " TEXT NOT NULL, " +
                                        COLUMNS.NOTIFICATION_ID + " INTEGER NOT NULL" +
                                ");";

    public interface COLUMNS {
        String _ID = "_id";
        String SENDER_PHONE_NUMBER = "sender_phone_number";
        String MESSAGE_BODY = "body";
        String NOTIFICATION_ID = "notification_id";
    }

    public IncomingSmsTable(Context context) {
        super(context, TABLE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 2){//Rename table to incoming_sms_messages
            db.execSQL("ALTER TABLE incoming_messages RENAME TO " + TABLE_NAME);
        }
    }
}
