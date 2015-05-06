package com.pinomg.determinator.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by olle on 03/05/15.
 */
public class PollsTable {
    public static final String TABLE_POLLS = "polls";
    public static final String COLUMN_POLL_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ALTERNATIVE_ONE = "alternative_one";
    public static final String COLUMN_ALTERNATIVE_TWO = "alternative_two";

    public static final String CREATE_POLLS_TABLE =
            "CREATE TABLE " + TABLE_POLLS + " (" +
            COLUMN_POLL_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_QUESTION + " TEXT, " +
            COLUMN_ALTERNATIVE_ONE + " TEXT, " +
            COLUMN_ALTERNATIVE_TWO + " TEXT" +
            " )";

    public static final String DELETE_POLLS_TABLE = "DROP TABLE IF EXISTS " + TABLE_POLLS;

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POLLS_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_POLLS_TABLE);
        onCreate(db);
    }
}
