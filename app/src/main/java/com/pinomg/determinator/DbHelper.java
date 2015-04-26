package com.pinomg.determinator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Olle on 26/04/15.
 */
public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.PollEntry.CREATE_TABLE);
    }

    // Simply delete the old db and then creates a new one
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.PollEntry.DELETE_TABLE);
        onCreate(db);
    }
}
