package com.pinomg.determinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AnswerQuestionActivity extends Activity {

    private Poll poll;
    private TextView questionText;
    private Button btnAltOne, btnAltTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        questionText = (TextView) findViewById(R.id.question_text);
        btnAltOne = (Button) findViewById(R.id.btn_alt_one);
        btnAltTwo = (Button) findViewById(R.id.btn_alt_two);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            poll = (Poll) extras.getSerializable("POLL");
            questionText.setText(poll.question);
            btnAltOne.setText(poll.alternativeOne);
            btnAltTwo.setText(poll.alternativeTwo);
        } else {
            Toast.makeText(getBaseContext(), "Error in loading question!", Toast.LENGTH_LONG).show();
        }
    }

    // TODO: Handle answer
    public void answerQuestion(View view) {
        finish();
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
