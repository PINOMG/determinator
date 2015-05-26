package com.pinomg.determinator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pinomg.determinator.model.Poll;
import com.pinomg.determinator.R;
import com.pinomg.determinator.helpers.Session;
import com.pinomg.determinator.net.ApiErrorException;
import com.pinomg.determinator.net.ApiHandler;

/**
 * The activity that is used when answering a Poll.
 * This activity gets the serialized poll from MainActivity,
 * and when the answer is sent to the api the activity finishes.
 */

public class AnswerPollActivity extends Activity {

    private Poll poll;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        session = new Session(getApplicationContext());

        TextView questionText = (TextView) findViewById(R.id.question_text);
        Button btnAltOne = (Button) findViewById(R.id.btn_alt_one);
        Button btnAltTwo = (Button) findViewById(R.id.btn_alt_two);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            poll = (Poll) extras.getSerializable("POLL");
            questionText.setText(poll.getQuestion());
            btnAltOne.setText(poll.getAlternativeOne());
            btnAltTwo.setText(poll.getAlternativeTwo());

        } else {
            Toast.makeText(getBaseContext(), "Error in loading question!", Toast.LENGTH_LONG).show();
        }
    }


    public void answerQuestion(int answer) {
        ApiHandler apiHandler = new ApiHandler(getBaseContext());

        try {
            apiHandler.postAnswer(poll.getId(), session.getLoggedInUsername(), answer);
        } catch (ApiErrorException e) {
            e.printStackTrace();
        }

        finish();
    }

    public void answerOne(View view){
        answerQuestion(1);
    }

    public void answerTwo(View view){
        answerQuestion(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_question, menu);
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
