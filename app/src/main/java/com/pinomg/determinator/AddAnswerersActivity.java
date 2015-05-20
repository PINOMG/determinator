package com.pinomg.determinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddAnswerersActivity extends Activity {

    public ArrayList<User> friendList = new ArrayList<User>(); //Creates a list to store friends.
    private ArrayAdapter friendsAdapter;
    private List<User> checkedFriends = new ArrayList<User>();
    private SparseBooleanArray checked;
    public TextView receiversText;
    private Poll poll;
    private ListView friendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answerers);

        friendView = (ListView) findViewById(R.id.friendView);
        friendView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        createExampleFriendList();

        friendsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, friendList);
        friendView.setAdapter(friendsAdapter);

        receiversText = (TextView) findViewById(R.id.receivers);

        checked = friendView.getCheckedItemPositions();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            poll = (Poll) extras.getSerializable("POLL");
            if(poll != null) {

                checkedFriends = poll.getAnswerers();
            }
        } else{
            Toast.makeText(getBaseContext(), "Error in loading question!", Toast.LENGTH_LONG).show();
        }

        checkFriends();
        showCheckedFriends();

        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(checked.get(i)) { //Add to checkedFriends
                    checkedFriends.add(friendList.get(i));

                } else{ // Remove from checkedFriends
                    checkedFriends.remove(friendList.get(i));
                }
                showCheckedFriends();
            }
        });
    }

    //Attaches the checked friends if back button is pressed.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        poll.setAnswerers(checkedFriends);
        intent.putExtra("POLL", poll);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_answerers, menu);
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

    //Attach friends to the poll and upload it to the database.
    public void sendPoll(View view) {
        if (!checkedFriends.isEmpty()) {
            poll.setAnswerers(checkedFriends);
            Intent intent = new Intent();
            intent.putExtra("CREATED_POLL", poll);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Haven't you got any friends, or?", Toast.LENGTH_LONG).show();
        }
    }

    //Populate string showed in the TextView receivers from checkedFriends here
    public void showCheckedFriends () {
        String receivers = "";
        for(Iterator<User> j = checkedFriends.iterator(); j.hasNext(); ) {
            User f = j.next();
            receivers += f.toString() + " ";
        }
        receiversText.setText(receivers);
    }

    //Match the already checked friends attached in the
    //poll with the corresponding friends in the ListView
    //and check the boxes for the identified matches.
    public void checkFriends() {
        for(Iterator<User> i = checkedFriends.iterator(); i.hasNext();) {
            int index = 0;
            User friend1 = i.next();
            for(Iterator<User> j = friendList.iterator(); j.hasNext();) {
                User friend2 = j.next();
                if(friend1.equals(friend2)) {
                    Log.e("Equals", "Equals!");
                    friendView.setItemChecked(index, true);
                }
                index++;
            }
        }
    }

    public void createExampleFriendList() {
        friendList.add(new User("Martin"));
        friendList.add(new User("Patrik"));
        friendList.add(new User("Ebba"));
        friendList.add(new User("Björn"));
        friendList.add(new User("Olle"));
        friendList.add(new User("Philip"));
    }
}
