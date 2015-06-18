package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IncomingSmsNotificationIdsTable extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "incoming_sms_notification_ids";
    private static final int SCHEMA_VERSION = 1;

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMNS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNS.SENDER + " TEXT NOT NULL, " +
            COLUMNS.NOTIFICATION_ID + " INTEGER NOT NULL);";


    public interface COLUMNS{
        String _ID = "_id";
        String SENDER = "sender";
        String NOTIFICATION_ID = "notification_id";
    }

    public IncomingSmsNotificationIdsTable(Context context) {
        super(context, TABLE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
