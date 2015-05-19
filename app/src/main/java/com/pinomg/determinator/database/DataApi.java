package com.pinomg.determinator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pinomg.determinator.Poll;

import java.util.LinkedList;
import java.util.List;

/**
 * Api for data handling in determinator
 * @author Olle Lindeman
 */
public class DataApi {

    private DatabaseHelper dbh;

    public DataApi(Context context) {
        this.dbh = new DatabaseHelper(context);
    }

    /**
     * Inserts new poll into database
     * @param poll poll to be inserted
     * @return the id of the inserted poll
     */
    public long addPoll(Poll poll) {
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PollsTable.COLUMN_QUESTION, poll.question);
        values.put(PollsTable.COLUMN_ALTERNATIVE_ONE, poll.alternativeOne);
        values.put(PollsTable.COLUMN_ALTERNATIVE_TWO, poll.alternativeTwo);

        return db.insert(PollsTable.TABLE_POLLS, null, values);
    }

    /**
     * Updates Poll in database
     * @param poll Poll to be updated
     * @return Number of rows affected
     */
    public int updatePoll(Poll poll) {
        ContentValues values = new ContentValues();
        values.put(PollsTable.COLUMN_QUESTION, poll.question);
        values.put(PollsTable.COLUMN_ALTERNATIVE_ONE, poll.alternativeOne);
        values.put(PollsTable.COLUMN_ALTERNATIVE_TWO, poll.alternativeTwo);

        String where = PollsTable.COLUMN_POLL_ID + "=" + poll.id;
        SQLiteDatabase db = dbh.getWritableDatabase();
        return db.update(PollsTable.TABLE_POLLS, values, where, null);
    }

    /**
     * Returns all Polls from database
     * @return all Polls from database
     */
    public List<Poll> getAllPolls() {
        List<Poll> polls = new LinkedList<Poll>();
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                PollsTable.COLUMN_POLL_ID,
                PollsTable.COLUMN_QUESTION,
                PollsTable.COLUMN_ALTERNATIVE_ONE,
                PollsTable.COLUMN_ALTERNATIVE_TWO
        };

        Cursor cursor = db.query(
                PollsTable.TABLE_POLLS,
                projection,
                null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Poll p = new Poll(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    null);

            polls.add(p);
            cursor.moveToNext();
        }
        cursor.close();

        return polls;
    }
}
