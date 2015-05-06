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


import com.pinomg.determinator.database.DataApi;
import com.pinomg.determinator.database.DatabaseHelper;
import com.pinomg.determinator.database.PollsTable;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends Activity {

    private DataApi api;

    public List<Poll> questionList; // Creates a list to store questions
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.api = new DataApi(getBaseContext());

        final ListView questionView = (ListView) findViewById(R.id.questionView);
        questionView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Poll poll = (Poll) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getBaseContext(), AnswerQuestionActivity.class);
                intent.putExtra("POLL", poll);
                startActivity(intent);
                overridePendingTransition(0,0);
                return false;
            }
        });

        questionList = api.getAllPolls();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionList);
        questionView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        questionList.clear();
        List<Poll> allPolls = api.getAllPolls();
        for(Poll p : allPolls) {
            questionList.add(p);
        }

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

    }

    public void goToCreateQuestionActivity(View view) {
        Intent intent = new Intent(this, CreatePollActivity.class);
        startActivity(intent);
    }

}
