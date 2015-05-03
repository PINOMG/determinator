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

import java.util.ArrayList;
import java.util.Iterator;

public class AddAnswerersActivity extends Activity {


    public ArrayList<Friend> friendList = new ArrayList<Friend>(); //Creates a list to store friends.
    private ArrayAdapter adapter;
    private ArrayList<Friend> checkedFriends = new ArrayList<Friend>();
    private SparseBooleanArray checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answerers);

        final ListView friendView = (ListView) findViewById(R.id.friendView);
        friendView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        createExampleFriendList();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, friendList);
        friendView.setAdapter(adapter);

        checked = friendView.getCheckedItemPositions();

        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(checked.get(i)){ //Add to checkedFriends
                    checkedFriends.add(friendList.get(i));
                } else { // Remove from checkedFriends
                    checkedFriends.remove(friendList.get(i));
                }
            }
        });






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

    public void goToListActivity(View view) {

        for(Iterator<Friend> i = checkedFriends.iterator(); i.hasNext(); ) {
            Friend f = i.next();
            Log.v("", f.toString());
        }
    }

    public void createExampleFriendList() {
        Friend friend1 = new Friend("Rasmus", 123);
        Friend friend2 = new Friend("Karin", 456);
        Friend friend3 = new Friend("Bubba", 789);

        friendList.add(friend1);
        friendList.add(friend2);
        friendList.add(friend3);
    }
}
