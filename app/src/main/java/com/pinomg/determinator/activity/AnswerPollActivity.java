package com.pinomg.determinator.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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


    /**
     * Sending users answer to the server
     * @param answer identificator for chosen answer
     */
    public void answerQuestion(final int answer) {

        // Kick off AsyncTask to answer question
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean success = false;
                try {
                    ApiHandler apiHandler = new ApiHandler(getBaseContext());
                    success = apiHandler.postAnswer(poll.getId(), session.getLoggedInUsername(), answer);
                } catch (ApiErrorException e) {
                    e.printStackTrace();
                }
                return success;
            }

            protected void onPostExecute(Boolean success) {
                if(success) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't send answer to server!", Toast.LENGTH_LONG).show();
                }
            }

        }.execute();

    }

    // OnClick metod for selecting answer one
    public void answerOne(View view){
        answerQuestion(1);
    }

    // OnClick metod for selecting answer two
    public void answerTwo(View view){
        answerQuestion(2);
    }


}
