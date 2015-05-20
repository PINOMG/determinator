package com.pinomg.determinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pinomg.determinator.api.ApiErrorException;
import com.pinomg.determinator.api.ApiHandler;


public class AnswerPollActivity extends Activity {

    private Poll poll;
    private TextView questionText;
    private Button btnAltOne, btnAltTwo;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        session = new SessionManagement(getApplicationContext());

        questionText = (TextView) findViewById(R.id.question_text);
        btnAltOne = (Button) findViewById(R.id.btn_alt_one);
        btnAltTwo = (Button) findViewById(R.id.btn_alt_two);

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
