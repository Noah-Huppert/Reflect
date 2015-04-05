package com.noahhuppert.reflect.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * A custodial class for holding static information about the DBFlow database
 */
@Database(name = ReflectDatabase.NAME, version = ReflectDatabase.VERSION)
public class ReflectDatabase {
    /**
     * Database name
     */
    public static final String NAME = "ReflectDatabase";

    /**
     * Database schema version
     */
    public static final int VERSION = 1;
}
