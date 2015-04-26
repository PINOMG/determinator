package com.pinomg.determinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ArrayAdapter;


import java.util.ArrayList;


public class ListActivity extends Activity {

    public ArrayList<Question> questionList = new ArrayList<Question>(); // Creates a list to store questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final ListView questionView = (ListView) findViewById(R.id.questionView);

        createExampleList();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionList);
        questionView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

    private void createExampleList() {
        // Creates example questions and adds them to questionList
        Question exampleQuestion1 = new Question();
        exampleQuestion1.question = "Kårlunch eller Sushi?";

        Question exampleQuestion2 = new Question();
        exampleQuestion2.question = "En runda till eller gå hem?";

        questionList.add(exampleQuestion1);
        questionList.add(exampleQuestion2);
    }

}
