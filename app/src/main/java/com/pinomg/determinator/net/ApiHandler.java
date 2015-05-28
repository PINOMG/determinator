package com.pinomg.determinator.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pinomg.determinator.model.User;
import com.pinomg.determinator.model.Poll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * The ApiHandler used for all requests to the API.
 */
public class ApiHandler {

    private static final String LOG_TAG = "ApiHandler";

    private static final String BASE_URL = "http://79.99.3.112/pinomg/";
    private static final String ENDPOINT_FRIEND = BASE_URL + "friend/";
    private static final String ENDPOINT_LOGIN  = BASE_URL + "login/";
    private static final String ENDPOINT_USER   = BASE_URL + "user/";
    private static final String ENDPOINT_POLL   = BASE_URL + "poll/";
    private static final String ENDPOINT_ANSWER = BASE_URL + "answer/";

    private Context context;

    public ApiHandler(Context context){
        this.context = context;
    }

    /**
     * Gets all polls for logged in user from the server
     * @param username The username the polls belong to
     * @param respListener The callback to use on success
     * @param errListener The callback to use on failure
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
                                    pollsFromJson(response.getJSONObject("data").getJSONArray("items"))
                                );
                            } catch (Exception e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }

                    }
                }, errListener);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
     * Method to return the friends belonging to the user
     * @param user The username of the user
     * @return List of friends (users)
     */
    public List<User> getFriends(String user) {
        String urls[] = {"GET", ENDPOINT_FRIEND + user};

        return (LinkedList<User>) apiListCall(urls, "users");
    }

    /**
     * Method to return all users on the server. Used when using the application in
     * smaller scale and the friend functionality isn't added yet.
     * @return List of all users in the application
     */
    public List<User> getUsers(){
        String urls[] = {"GET", ENDPOINT_USER};

        return (LinkedList<User>) apiListCall(urls, "users");
    }

    /**
     * Attempt a login
     * @param username Username
     * @param password Password
     * @return True if credentials fit, else false
     * @throws ApiErrorException
     */
    public boolean login(String username, String password) throws ApiErrorException {
        String urls[] = {"POST", ENDPOINT_LOGIN, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    /**
     * Send the answer of poll.
     * @param poll_id The server id of Poll
     * @param username The user's username
     * @param answer The user's answer to the poll. 1 or 2.
     * @return True if it was a success, else false
     * @throws ApiErrorException
     */
    public boolean postAnswer(int poll_id, String username, int answer) throws ApiErrorException {
        String urls[] = {"POST", ENDPOINT_ANSWER + poll_id, "username=" + username + "&answer=" + answer};

        return apiCall(urls);
    }

    /**
     * Used when creating a new user
     * @param username The wanted username
     * @param password The wanted password
     * @return True if success, else false.
     * @throws ApiErrorException
     */
    public boolean createUser(String username, String password) throws ApiErrorException {
        String urls[] = {"POST", ENDPOINT_USER, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    /**
     * Method to use when asking a new Poll
     * @param poll The Poll object.
     * @param username The user's username
     * @return True on success, else false.
     * @throws ApiErrorException
     */
    public boolean createPoll(Poll poll, String username) throws ApiErrorException {

        // Create a Json array from the list of usernames in answerers.
        JSONArray receivers = new JSONArray();
        for( User i : poll.getAnswerers() ){
            receivers.put(i.toString());
        }

        String data =   "question=" + poll.getQuestion() +
                        "&alternative_one=" + poll.getAlternativeOne() +
                        "&alternative_two=" + poll.getAlternativeTwo() +
                        "&receivers=" + receivers +
                        "&username=" + username;

        String urls[] = {"POST", ENDPOINT_POLL, data};
        return apiCall(urls);

    }

    /**
     * Used when returning a list from the server
     * @param urls The params used
     * @param item The type requested from the server. Tells the method how to parse the response
     * @return A list of the requested types.
     */
    public List<?> apiListCall(String[] urls, String item){
        //Response array
        List<?> listItems = new LinkedList<>();

        //Do the call
        JSONObject response;
        try {
            response = new ApiConnector(context).execute(urls).get();

            // Parse the response
            if( response != null){
                if (response.has("error")){ // If there is an error.
                    JSONObject error = response.getJSONObject("error");

                    String message = error.getString("message");
                    int code = error.getInt("code");
                    throw new ApiErrorException(message, code);
                }

                JSONObject data = response.getJSONObject("data");
                JSONArray json_list = data.getJSONArray("items");

                switch (item){
                    case "users":
                        listItems = usersFromJson(json_list);
                        break;
                    default:
                        Log.e("Create list", "Case not found");
                        listItems = null;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listItems;
    }

    /**
     * Parse users from a list of json
     * @param json_list JSONArray of usernames
     * @return List of users
     * @throws JSONException
     */
    private List<User> usersFromJson(JSONArray json_list) throws JSONException {
        List<User> listItems = new LinkedList<>();

        for (int i = 0; i < json_list.length(); i++) {
            String friend = json_list.getString(i);
            listItems.add(new User(friend)); //Remove id from here. It should not exist!
        }

        return listItems;
    }

    /**
     * Building a List of Polls from JSONArray
     * @param json_list JSONArray of poll objects
     * @return List of poll objects
     * @throws JSONException
     */
    public List<Poll> pollsFromJson(JSONArray json_list) throws JSONException {
        List<Poll> listItems = new LinkedList<>();

        for ( int i = 0; i < json_list.length(); i++ ){
            JSONObject poll_json = json_list.getJSONObject(i);

            // Parse the Poll from the json object.
            Poll poll = jsonToPoll(poll_json);
            listItems.add(poll);
        }

        return listItems;
    }

    /**
     * Simple apiCall. Not expecting a list response from server, just an error or success.
     * @param urls url object used
     * @return true or false, depending on success or error
     * @throws ApiErrorException
     */
    public boolean apiCall(String[] urls) throws ApiErrorException { //Used for all simple calls. Those who doesn't expect a response
        JSONObject response;
        String message;

        try {
            response = new ApiConnector(context).execute(urls).get();

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
            } else { // This cannot happen happened.
                return false;
            }
        } catch (JSONException e) { // Parse errors. Probably because of server errors.
            return false;
        } catch (Exception e){ // Not managable errors.
            e.printStackTrace();
            return false;
        }
    }

    //How to "un"-serialize a poll from json-code
    private static Poll jsonToPoll(JSONObject json) throws JSONException {
        String alternative_one = json.getString("alternative_one");
        String alternative_two = json.getString("alternative_two");

        String question = json.getString("question");
        int id = json.getInt("id");
        int result = json.getInt("result");
        int answer = json.getInt("answer");

        return new Poll(id, question, alternative_one, alternative_two, result, answer);

    }
}
