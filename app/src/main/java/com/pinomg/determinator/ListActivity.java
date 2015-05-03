package com.pinomg.determinator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;


import java.util.ArrayList;


public class ListActivity extends Activity {

    public ArrayList<Poll> questionList = new ArrayList<Poll>(); // Creates a list to store questions
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final ListView questionView = (ListView) findViewById(R.id.questionView);

        questionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poll poll = (Poll) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getBaseContext(), AnswerQuestionActivity.class);
                intent.putExtra("POLL", poll);
                startActivity(intent);
            }
        });

        createExampleList();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionList);
        questionView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        questionList.clear();
        createExampleList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void createExampleList() {

        // Fetches all questions from db,
        DbHelper dbh      = new DbHelper(getBaseContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor cursor = db.query(
                DbContract.PollEntry.TABLE_NAME,
                DbContract.PollEntry.ALL_COLUMNS,
                null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Poll p = new Poll(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));

            questionList.add(p);
            cursor.moveToNext();
        }
        cursor.close();

    }

    public void goToCreateQuestionActivity(View view) {
        Intent intent = new Intent(this, CreatePollActivity.class);
        startActivity(intent);
    }

}
