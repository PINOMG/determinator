package com.pinomg.determinator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.pinomg.determinator.model.Poll;
import com.pinomg.determinator.R;

/**
 * Activity for viewing a result of a poll
 */
public class ResultActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Connecting layout with logic
        TextView questionText = (TextView) findViewById(R.id.question);
        TextView answerText = (TextView) findViewById(R.id.answer);

        // Fetches poll to be viewed
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Poll poll = (Poll) extras.getSerializable("POLL");
            questionText.setText(poll.getQuestion());
            answerText.setText(poll.getResultText());

        }
    }
}
