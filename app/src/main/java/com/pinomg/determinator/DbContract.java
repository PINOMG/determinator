package com.pinomg.determinator;

import android.provider.BaseColumns;

/**
 * Contract class for database schema, specifies layout of schema
 *
 * Created by Olle on 26/04/15.
 */
public final class DbContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Determinator.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class
    public DbContract() {}


    // Contract for a poll entry in db
    public static abstract class PollEntry implements BaseColumns {
        public static final String TABLE_NAME = "polls";
        public static final String COLUMN_NAME_POLL_ID = "id";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_ALTERNATIVE_ONE = "alternative_one";
        public static final String COLUMN_NAME_ALTERNATIVE_TWO = "alternative_two";


        public static final String CREATE_TABLE =
                "CREATE TABLE " + PollEntry.TABLE_NAME + " (" +
                        PollEntry._ID + " INTEGER PRIMARY KEY," +
                        PollEntry.COLUMN_NAME_POLL_ID + TEXT_TYPE + COMMA_SEP +
                        PollEntry.COLUMN_NAME_QUESTION + TEXT_TYPE + COMMA_SEP +
                        PollEntry.COLUMN_NAME_ALTERNATIVE_ONE + TEXT_TYPE + COMMA_SEP +
                        PollEntry.COLUMN_NAME_ALTERNATIVE_TWO + TEXT_TYPE +
                        " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + PollEntry.TABLE_NAME;
    }

}
