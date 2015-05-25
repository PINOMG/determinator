package com.pinomg.determinator.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pinomg.determinator.model.User;
import com.pinomg.determinator.model.Poll;
import com.pinomg.determinator.helpers.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by patrik on 2015-05-11.
 */
public class ApiHandler {

    private static final String LOG_TAG = "ApiHandler";

    // private static final String BASE_URL = "http://95.80.41.105:8004/determinator_server/";
    private static final String BASE_URL = "http://192.168.43.8/pinomg/";
    // private static final String BASE_URL = "http://129.16.207.255/pinomg/";
    private static final String ENDPOINT_FRIEND = BASE_URL + "friend/";
    private static final String ENDPOINT_LOGIN  = BASE_URL + "login/";
    private static final String ENDPOINT_USER   = BASE_URL + "user/";
    public static final String ENDPOINT_POLL   = BASE_URL + "poll/";
    private static final String ENDPOINT_ANSWER = BASE_URL + "answer/";

    private Context context;

    public ApiHandler(Context context){
        this.context = context;
    }

    /**
     * Gets all polls for logged in user from the server
     * @param username
     * @param respListener
     * @param errListener
     */
    public void getPolls(String username, final Response.Listener<List<Poll>> respListener, final Response.ErrorListener errListener) {

        // Creates a basic json request via volley and parse the result before
        // calling the callbacks.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                ENDPOINT_POLL + username,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("data")) {
                            try {
                                respListener.onResponse(
                                    doPolls(response.getJSONObject("data").getJSONArray("items"))
                                );
                            } catch (Exception e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }

                    }
                }, errListener);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    public List<User> getFriends(String user) {
        //Initiating and building urls.
        Log.e("Initiating", "Get friends");

        String urls[] = {"GET", ENDPOINT_FRIEND + user};

        return (LinkedList<User>) apiListCall(urls, "friends");
    }

    public List<User> getUsers(){
        String urls[] = {"GET", ENDPOINT_USER};

        return (LinkedList<User>) apiListCall(urls, "friends");
    }

    // Returns the polls asked to the user
    public List<Poll> getPolls(String user){
        Log.e("Initiating", "Get friends");

        String urls[] = {"GET", ENDPOINT_POLL + user};

        return (LinkedList<Poll>) apiListCall(urls, "polls");
    }

    // Attempt a login.
    public boolean login(String username, String password) throws ApiErrorException {
        //Initiating and building urls.
        Log.e("Initiating", "Login " + username + password);

        String urls[] = {"POST", ENDPOINT_LOGIN, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    // Send the answer of poll.
    public boolean postAnswer(int poll_id, String username, int answer) throws ApiErrorException {
        //Init
        Log.e("Initiating", "postAnswer");

        String urls[] = {"POST", ENDPOINT_ANSWER + poll_id, "username=" + username + "&answer=" + answer};

        return apiCall(urls);
    }

    // Create a new user
    public boolean createUser(String username, String password) throws ApiErrorException {
        //Initiating and building urls.
        Log.e("Initiating", "Creating user");

        String urls[] = {"POST", ENDPOINT_USER, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    //Create a new poll
    public boolean createPoll(Poll poll, Session session) throws ApiErrorException {
        Log.e("Initiating", "Creating poll");

        // This should be implemented when friend function is working properly, not when username's are hard coded.
        // String receivers = "[" + '"' + session.getLoggedInUsername() + '"' + "," ;

        String receivers = "[";

        for (Iterator<User> i = poll.getAnswerers().iterator(); i.hasNext(); ) {
            User f = i.next();
            receivers += '"' + f.toString() + '"';
            if (i.hasNext())
                receivers += ',';
            else
                receivers += ']';
        }

        String data =   "question=" + poll.getQuestion() +
                        "&alternative_one=" + poll.getAlternativeOne() +
                        "&alternative_two=" + poll.getAlternativeTwo() +
                        "&receivers=" + receivers +
                        "&username=" + session.getLoggedInUsername();

        Log.e("Post data", data);

        String urls[] = {"POST", ENDPOINT_POLL, data};
        return apiCall(urls);

    }

    // Used when returning a list from the server
    public List<?> apiListCall(String[] urls, String item){
        //Response array
        List<?> listItems = new LinkedList<>();

        //Do the call
        JSONObject response;
        try {
            response = new ApiConnector(context).execute(urls).get();

            if( response != null){
                JSONObject data = response.getJSONObject("data");
                JSONArray json_list = data.getJSONArray("items");

                switch (item){
                    case "friends":
                        listItems = doFriends(json_list);
                        break;
                    case "polls":
                        listItems = doPolls(json_list);
                        break;
                    default:
                        Log.d("Create list", "Case not found");
                        listItems = null;
                        break;
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listItems;
    }


    private List<User> doFriends(JSONArray json_list) throws JSONException {
        List<User> listItems = new LinkedList<>();

        for (int i = 0; i < json_list.length(); i++) {
            String friend = json_list.getString(i);
            listItems.add(new User(friend)); //Remove id from here. It should not exist!
        }

        return listItems;
    }

    // Building a List of Polls from JSONArray
    public List<Poll> doPolls(JSONArray json_list) throws JSONException {
        List<Poll> listItems = new LinkedList<>();

        for ( int i = 0; i < json_list.length(); i++ ){
            JSONObject poll_json = json_list.getJSONObject(i);
            Poll poll = jsonToPoll(poll_json);
            listItems.add(poll);
        }

        return listItems;
    }

    //Simple apiCall. Not expecting a list response from server, just an error or success.
    public boolean apiCall(String[] urls) throws ApiErrorException { //Used for all simple calls. Those who doesn't expect a response
        JSONObject response;
        String message = "";

        try {
            Log.d("apicall", "before resp");
            response = new ApiConnector(context).execute(urls).get();
            Log.d("apicall", "after resp");

            if (response != null) {
                if (response.has("error")){ // If there is an error.
                    JSONObject error = response.getJSONObject("error");

                    message = error.getString("message");
                    int code = error.getInt("code");
                    throw new ApiErrorException(message, code);
                } else {
                    JSONObject data = response.getJSONObject("data");
                    return data != null; // This will always return true.
                }
            } else { // If something FUBAR happened.
                return false;
            }
        } catch (JSONException e) { // Parse errors. Probably because server errors.
            Log.d("App error", "Error parsing JSON");
            return false;
        } catch (Exception e){ // Not managaeble errors.
            e.printStackTrace();
            return false;
        }
    }

    //How to "un"-serialize an object from json-code
    private static Poll jsonToPoll(JSONObject json) throws JSONException {

        String alternative_one = json.getString("alternative_one");
        String alternative_two = json.getString("alternative_two");

        String question = json.getString("question");
        int id = json.getInt("id");
        int result = json.getInt("result");
        int answer = json.getInt("answer");

        Poll p = new Poll(id, question, alternative_one, alternative_two, result, answer);

        Log.d("Poll:", "Created poll " + id);
        return p;

    }
}
