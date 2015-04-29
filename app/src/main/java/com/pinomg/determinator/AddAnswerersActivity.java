package com.pinomg.determinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AddAnswerersActivity extends Activity {


    public ArrayList<Friend> friendList = new ArrayList<Friend>(); //Creates a list to store friends.
    private ArrayAdapter adapter;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answerers);

        final ListView friendView = (ListView) findViewById(R.id.friendView);

        createExampleFriendList();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, friendList);
        friendView.setAdapter(adapter);

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

    public void createExampleFriendList() {
        Friend friend1 = new Friend("Rasmus", 123);
        Friend friend2 = new Friend("Karin", 456);
        Friend friend3 = new Friend("Bubba", 789);

        friendList.add(friend1);
        friendList.add(friend2);
        friendList.add(friend3);
    }
}
