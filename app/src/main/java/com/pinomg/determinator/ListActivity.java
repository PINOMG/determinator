package com.pinomg.determinator;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pinomg.determinator.api.ApiErrorException;
import com.pinomg.determinator.api.ApiHandler;
import com.pinomg.determinator.database.DataApi;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class ListActivity extends Activity {

    private static String LOG_TAG = "ListActivity";

    private int CREATE_POLL_REQUEST = 0;

    private DataApi api;
    private ApiHandler apiHandler;

    public List<Poll> pollList; // Creates a list to store polls
    private CustomAdapter adapter; // The custom adapter, grouping polls.

    private SwipeRefreshLayout mySwipeRefreshLayout;

    private RequestQueue queue;

    // SessionManagement class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        queue = Volley.newRequestQueue(this);

        session = new SessionManagement(getApplicationContext());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity if he is not
         * logged in
         * */
        session.checkLogin();

        this.api = new DataApi(getBaseContext());
        this.apiHandler = new ApiHandler(getBaseContext());

        final ListView pollView = (ListView) findViewById(R.id.pollView);

        pollView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getAdapter().getItemViewType(position) == CustomAdapter.TYPE_POLL) {
                    Poll poll = (Poll) parent.getAdapter().getItem(position);

                    if (poll.getStatus() == poll.STATUS_FINISHED) {
                        Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                        intent.putExtra("POLL", poll);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return false;
                    } else if (poll.getStatus() == poll.STATUS_PENDING) {
                        Intent intent = new Intent(getBaseContext(), AnswerPollActivity.class);
                        intent.putExtra("POLL", poll);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return false;
                    }
                    return false;
                } else {
                    return false;
                }
            }
        });

        pollList = new LinkedList<>();
        adapter = new CustomAdapter(this, pollList);
        pollView.setAdapter(adapter);


        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        updatePollListView();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySwipeRefreshLayout.setRefreshing(true);
        updatePollListView();
    }

    private void updatePollListView() {

        if(session.isLoggedIn()) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://95.80.41.105:8004/determinator_server/poll/" + session.getLoggedInUsername(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(LOG_TAG, "Response: " + response.toString());
                            if (response.has("data")) {

                                try {
                                    pollList.clear();
                                    List<Poll> allPolls = apiHandler.doPolls(response.getJSONObject("data").getJSONArray("items"));
                                    for (Poll p : allPolls) {
                                        pollList.add(p);
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Log.e(LOG_TAG, e.getMessage());
                                } finally {
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, "Error: " + error.toString());
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    });
            queue.add(jsObjRequest);
        } else {
            mySwipeRefreshLayout.setRefreshing(false);
        }
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
        switch (id) {
            case R.id.log_out:
                session.logoutUser();
                return true;
            case R.id.refresh:
                this.onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void goToCreatePollActivity(View view) {
        Intent intent = new Intent(this, CreatePollActivity.class);
        startActivityForResult(intent, CREATE_POLL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CREATE_POLL_REQUEST) {
            if(resultCode == RESULT_OK) {
                // Try to send poll to server
                Poll poll = (Poll) data.getSerializableExtra("CREATED_POLL");
                try {
                    apiHandler.createPoll(poll, session);
                } catch (ApiErrorException e) {
                    Log.d(e.getMessage(), null);
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
