package com.pinomg.determinator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinomg.determinator.net.ApiHandler;
import com.pinomg.determinator.model.Poll;
import com.pinomg.determinator.R;
import com.pinomg.determinator.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity used when selecting which users to send a poll to.
 *
 * The activity gets a serialized poll from CreatePollActivity,
 * modifies it and upon finish sends it back to CreatePollActivity.
 *
 */
public class AddAnswerersActivity extends Activity {

    private LinkedList<User> friendList = new LinkedList<>(); //Creates a list to store friends.
    private LinkedList<User> checkedFriends = new LinkedList<>();

    private SparseBooleanArray checked;
    private TextView receiversText;
    private Poll poll;
    private ListView friendView;

    private ArrayAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answerers);

        friendView = (ListView) findViewById(R.id.friendView);
        friendView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //Connect the friends to the view
        friendsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, friendList);
        friendView.setAdapter(friendsAdapter);

        //Get the id to the text field that should show an inline list of checked users.
        receiversText = (TextView) findViewById(R.id.receivers);

        //Set the SparseBooleanArray so the checklist can work properly
        checked = friendView.getCheckedItemPositions();

        //Get the poll from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            this.poll = (Poll) extras.getSerializable("POLL");

            if(this.poll != null) {
                if( poll.getAnswerers() != null)
                    checkedFriends = (LinkedList<User>)poll.getAnswerers();
                else
                    checkedFriends = new LinkedList<>();
            }
        } else {
            Toast.makeText(getBaseContext(), "Error in loading question!", Toast.LENGTH_LONG).show();
        }

        //Load the friends from the API
        loadFriends();

        // The click listener when checking / un-checking a friend.
        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(checked.get(i)) { //Add to checkedFriends
                checkedFriends.add(friendList.get(i));

            } else{ // Remove from checkedFriends
                checkedFriends.remove(friendList.get(i));
            }

            // Always update the inline list afterwards.
            showCheckedFriends();
            }
        });
    }

    //Attaches the checked friends if back button is pressed. This will return us to the q activity.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        this.poll.setAnswerers(checkedFriends);
        intent.putExtra("POLL", poll);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    //Attach friends to the poll and send it to the Q activity, which will send it via the API handler.
    public void sendPoll(View view) {
        if (!checkedFriends.isEmpty()) {
            this.poll.setAnswerers(checkedFriends);
            Intent intent = new Intent();
            intent.putExtra("CREATED_POLL", poll);
            setResult(RESULT_OK, intent);
            finish();
        } else { // Error handling if no users are checked.
            Toast.makeText(getBaseContext(), "Haven't you got any friends, or?", Toast.LENGTH_LONG).show();
        }
    }

    //Populate string showed in the TextView receivers from checkedFriends here
    public void showCheckedFriends () {
        String receivers = "";
        for( User u: checkedFriends){
            receivers += u + " ";
        }

        receiversText.setText(receivers);
    }

    //Match the already checked friends attached in the
    //poll with the corresponding friends in the ListView
    //and check the boxes for the identified matches.
    public void checkFriends() {
        int index = 0;
        for( User i : friendList ){
            for ( User j : checkedFriends ){
                if( i.equals(j) )
                    friendView.setItemChecked(index, true);

            }
            index++;
        }
    }

    public void loadFriends() {
        new AsyncTask<String, Void, List<User>>(){
            @Override
            protected List<User> doInBackground(String... strings) {
                ApiHandler apiHandler = new ApiHandler(getApplicationContext());
                return apiHandler.getUsers();
            }

            @Override
            protected void onPostExecute(List<User> userList){
                friendList.clear();
                for(User u: userList){
                    friendList.add(u);
                }

                friendsAdapter.notifyDataSetChanged();

                // Check the friends from previous activity in the list
                checkFriends();

                // Print their usernames as well in the inline list.
                showCheckedFriends();

            }
        }.execute();
    }
}
