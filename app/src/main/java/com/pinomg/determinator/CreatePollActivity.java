package com.pinomg.determinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class CreatePollActivity extends Activity {

    private EditText questionField, alternativeOne, alternativeTwo;
    private Button createButton;
    private Poll poll;

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



    public void goToAddAnswerersActivity(View view) {

        if(poll == null) {
            // Creates new poll
            Poll poll = new Poll(questionField.getText().toString(), alternativeOne.getText().toString(), alternativeTwo.getText().toString());
        } else {
            // Changes poll if we come back from addAnswerers
            poll.setQuestion(questionField.getText().toString());
            poll.setAlternativeOne(alternativeOne.getText().toString());
            poll.setAlternativeTwo(alternativeTwo.getText().toString());
        }

        // Check that the user has given all required input
        Boolean valid = true;
        if(questionField.getText().toString().length() == 0) {
            questionField.setError("You must define a question!");
            valid = false;
        }
        if(alternativeOne.getText().toString().length() == 0) {
            alternativeOne.setError("You must give an answer!");
            valid = false;
        }
        if(alternativeTwo.getText().toString().length() == 0) {
            alternativeTwo.setError("You must give an answer!");
            valid = false;
        }

        if(valid) {
            Intent intent = new Intent(this, AddAnswerersActivity.class);
            intent.putExtra("POLL", poll);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        } else {
            Bundle extras = data.getExtras();
            poll = (Poll) extras.getSerializable("POLL");
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
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
