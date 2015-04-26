package com.pinomg.determinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateQuestionActivity extends Activity {

    private EditText questionField, answer1, answer2;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        // Connecting layout with logic
        questionField = (EditText) findViewById(R.id.questionField);
        answer1 = (EditText) findViewById(R.id.answer1);
        answer2 = (EditText) findViewById(R.id.answer2);
        createButton = (Button) findViewById(R.id.createButton);
    }

    public void sendQuestion(View v) {
        Question question = new Question(questionField.getText().toString(), answer1.getText().toString(), answer2.getText().toString());

        // Check that the user has given all required input
        if( questionField.getText().toString().length() == 0)
            questionField.setError("You must define a question!");
        if (answer1.getText().toString().length() == 0)
            answer1.setError("You must give an answer!");
        if( answer2.getText().toString().length() == 0)
            answer2.setError("You must give an answer!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_question, menu);
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
