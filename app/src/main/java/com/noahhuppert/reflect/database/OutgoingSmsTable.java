package com.noahhuppert.reflect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class OutgoingSmsTable extends SQLiteOpenHelper {
    private static final int SCHEMA_VERSION = 1;
    public static final String TABLE_NAME = "outgoing_sms_messages";

    private static final String TABLE_QUERY_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMNS._ID + " TEXT PRIMARY KEY NOT NULL, ";

    public interface COLUMNS{
        String _ID = "_id";
    }

    public OutgoingSmsTable(Context context) {
        super(context, TABLE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
