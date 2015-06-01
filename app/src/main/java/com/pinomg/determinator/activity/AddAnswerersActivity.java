package com.pinomg.determinator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pinomg.determinator.net.ApiHandler;
import com.pinomg.determinator.model.Poll;
import com.pinomg.determinator.R;
import com.pinomg.determinator.model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity used when selecting which users to send a poll to.
 *
 * The activity gets a serialized poll from CreatePollActivity,
 * modifies it and upon finish sends it back to CreatePollActivity.
 *
 */
public class AddAnswerersActivity extends Activity {

    private LinkedList<User> userList = new LinkedList<>(); //Creates a list to store users.
    private LinkedList<User> checkedUsers = new LinkedList<>();

    private SparseBooleanArray checked;
    private TextView receiversText;
    private Poll poll;
    private ListView userView;
    private ProgressBar loader;

    private ArrayAdapter<User> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_add_answerers);

        userView = (ListView) findViewById(R.id.userView);
        userView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        loader = (ProgressBar) findViewById(R.id.progressBar);

        //Connect the users to the view
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, userList);
        userView.setAdapter(usersAdapter);

        //Get the id to the text field that should show an inline list of checked users.
        receiversText = (TextView) findViewById(R.id.receivers);

        //Set the SparseBooleanArray so the checklist can work properly
        checked = userView.getCheckedItemPositions();

        //Get the poll from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            this.poll = (Poll) extras.getSerializable("POLL");

            if(this.poll != null) {
                if( poll.getAnswerers() != null)
                    checkedUsers = (LinkedList<User>)poll.getAnswerers();
                else
                    checkedUsers = new LinkedList<>();
            }
        } else {
            Toast.makeText(getBaseContext(), "Error in loading question!", Toast.LENGTH_LONG).show();
        }

        //Load the users from the API
        loadUsers();

        // The click listener when checking / un-checking a user.
        userView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checked.get(i)) { //Add to checkedUsers
                    checkedUsers.add(userList.get(i));

                } else { // Remove from checkedUsers
                    checkedUsers.remove(userList.get(i));
                }

                // Always update the inline list afterwards.
                showCheckedUsers();
            }
        });
    }

    //Attaches the checked users if back button is pressed. This will return us to the q activity.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        this.poll.setAnswerers(checkedUsers);
        intent.putExtra("POLL", poll);
        setResult(RESULT_CANCELED, intent);

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //Attach users to the poll and send it to the Q activity, which will send it via the API handler.
    public void sendPoll(View view) {
        if (!checkedUsers.isEmpty()) {
            this.poll.setAnswerers(checkedUsers);
            Intent intent = new Intent();
            intent.putExtra("CREATED_POLL", poll);
            setResult(RESULT_OK, intent);
            finish();
        } else { // Error handling if no users are checked.
            Toast.makeText(getBaseContext(), "You need to select one or more users!", Toast.LENGTH_LONG).show();
        }
    }

    //Populate string showed in the TextView receivers from checkedUsers here
    public void showCheckedUsers() {
        String receivers = "";
        for( User u: checkedUsers){
            receivers += u + " ";
        }

        receiversText.setText(receivers);
    }

    //Match the already checked users attached in the
    //poll with the corresponding users in the ListView
    //and check the boxes for the identified matches.
    public void checkUsers() {
        int index = 0;
        for( User i : userList){
            for ( User j : checkedUsers){
                if( i.equals(j) )
                    userView.setItemChecked(index, true);

            }
            index++;
        }
    }

    private void showLoader(boolean show){
        userView.setVisibility(show ? View.GONE : View.VISIBLE);
        loader.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Loads users from the server
    public void loadUsers() {
        showLoader(true);

        // Kicks off an AsyncTask that loads alla users from server
        new AsyncTask<String, Void, List<User>>(){
            @Override
            protected List<User> doInBackground(String... strings) {


                ApiHandler apiHandler = new ApiHandler(getApplicationContext());
                return apiHandler.getUsers();
            }

            @Override
            protected void onPostExecute(List<User> userList){
                AddAnswerersActivity.this.userList.clear();
                for(User u: userList){
                    AddAnswerersActivity.this.userList.add(u);
                }

                usersAdapter.notifyDataSetChanged();

                // Check the users from previous activity in the list
                checkUsers();

                // Print their usernames as well in the inline list.
                showCheckedUsers();

                showLoader(false);


            }
        }.execute();
    }
}
