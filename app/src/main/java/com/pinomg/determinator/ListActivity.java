package com.pinomg.determinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.pinomg.determinator.api.ApiConnector;
import com.pinomg.determinator.api.ApiErrorException;
import com.pinomg.determinator.api.ApiHandler;
import com.pinomg.determinator.database.DataApi;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ListActivity extends Activity {

    private int CREATE_POLL_REQUEST = 0;

    private DataApi api;
    private ApiHandler apiHandler;

    public List<Poll> questionList; // Creates a list to store questions
    private CustomAdapter adapter;

    // SessionManagement class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        session = new SessionManagement(getApplicationContext());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity if he is not
         * logged in
         * */
        session.checkLogin();

        this.api = new DataApi(getBaseContext());
        this.apiHandler = new ApiHandler(getBaseContext());

        final ListView questionView = (ListView) findViewById(R.id.questionView);
        questionView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Poll poll = (Poll) parent.getAdapter().getItem(position);

                if(poll.getStatus() == poll.STATUS_FINISHED) {
                    Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                    intent.putExtra("POLL", poll);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return false;
                } else {
                    Intent intent = new Intent(getBaseContext(), AnswerQuestionActivity.class);
                    intent.putExtra("POLL", poll);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return false;
                }
            }
        });

        questionList = apiHandler.getPolls("Martin");

        adapter = new CustomAdapter(this, questionList);

        questionView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        questionList.clear();
        List<Poll> allPolls = apiHandler.getPolls("Martin");
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
        switch (id) {
            case R.id.log_out:
                session.logoutUser();
                return true;
            case R.id.refresh:
                Log.d("Ey", "Init");

                try {
                    boolean trigg = apiHandler.login("Martin","123");
                } catch (ApiErrorException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createExampleList() {

    }

    public void goToCreateQuestionActivity(View view) {
        Intent intent = new Intent(this, CreatePollActivity.class);
        startActivityForResult(intent, CREATE_POLL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CREATE_POLL_REQUEST) {
            if(resultCode == RESULT_OK) {
                // Try to send poll to server
                Poll poll = (Poll) data.getSerializableExtra("CREATED_POLL");
                Toast.makeText(getBaseContext(), poll.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
