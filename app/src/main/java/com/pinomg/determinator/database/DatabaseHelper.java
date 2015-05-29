package com.pinomg.determinator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The helper class for the database. This class can be used to
 * cache data from the API.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Determinator.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PollsTable.onCreate(db);
    }

    // Simply delete the old db and then creates a new one
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PollsTable.onUpgrade(db, oldVersion, newVersion);
    }
}
