package com.pinomg.determinator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pinomg.determinator.database.DataApi;
import com.pinomg.determinator.database.DatabaseHelper;
import com.pinomg.determinator.database.PollsTable;


public class CreatePollActivity extends Activity {

    private EditText questionField, alternativeOne, alternativeTwo;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        // Connecting layout with logic
        questionField = (EditText) findViewById(R.id.questionField);
        alternativeOne = (EditText) findViewById(R.id.alternativeOne);
        alternativeTwo = (EditText) findViewById(R.id.alternativeTwo);
        createButton = (Button) findViewById(R.id.createButton);
    }

    public void goToAddAnswerersActivity() {
        Poll poll = new Poll(questionField.getText().toString(), alternativeOne.getText().toString(), alternativeTwo.getText().toString(), null);

        // Check that the user has given all required input
        Boolean valid = true;
        if (questionField.getText().toString().length() == 0) {
            questionField.setError("You must define a question!");
            valid = false;
        }
        if (alternativeOne.getText().toString().length() == 0) {
            alternativeOne.setError("You must give an answer!");
            valid = false;
        }
        if (alternativeTwo.getText().toString().length() == 0) {
            alternativeTwo.setError("You must give an answer!");
            valid = false;
        }

        if (valid) {
            Intent intent = new Intent(this, AddAnswerersActivity.class);
            intent.putExtra("POLL", poll);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_poll, menu);
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

}
