package com.pinomg.determinator.api;

import android.content.Context;
import android.util.Log;

import com.pinomg.determinator.Friend;
import com.pinomg.determinator.Poll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by patrik on 2015-05-11.
 */
public class ApiHandler {
    private static final String BASE_URL = "http://192.168.0.11/pinomg/";

    private static final String ENDPOINT_FRIEND = "friend/";
    private static final String ENDPOINT_LOGIN = "login/";
    private static final String ENDPOINT_USER = "user/";
    private static final String ENDPOINT_POLL = "poll/";
    private static final String ENDPOINT_ANSWER = "answer/";

    private Context context;

    public ApiHandler(Context context){
        this.context = context;
    }

    public List<Friend> getFriends(String user) {
        //Initiating and building urls.
        Log.e("Initiating", "Get friends");

        String urls[] = {"GET", BASE_URL + ENDPOINT_FRIEND + user};

        return (LinkedList<Friend>) apiListCall(urls, "friends");
    }

    public List<Poll> getPolls(String user){
        Log.e("Initiating", "Get friends");

        String urls[] = {"GET", BASE_URL + ENDPOINT_POLL + user};

        return (LinkedList<Poll>) apiListCall(urls, "polls");
    }

    public boolean login(String username, String password) throws ApiErrorException {
        //Initiating and building urls.
        Log.e("Initiating", "Login " + username + password);

        String urls[] = {"POST", BASE_URL + ENDPOINT_LOGIN, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    public boolean postAnswer(int poll_id, String username, int answer) throws ApiErrorException {
        //Init
        Log.e("Initiating", "postAnswer");

        String urls[] = {"POST", BASE_URL + ENDPOINT_ANSWER + poll_id, "username=" + username + "&answer=" + answer};

        return apiCall(urls);
    }

    public boolean createUser(String username, String password) throws ApiErrorException {
        //Initiating and building urls.
        Log.e("Initiating", "Creating user");

        String urls[] = {"POST", BASE_URL + ENDPOINT_USER, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    public List<?> apiListCall(String[] urls, String item){
        //Response array
        List<?> listItems = null;

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

    private List<Friend> doFriends(JSONArray json_list) throws JSONException {
        List<Friend> listItems = new LinkedList<>();

        for (int i = 0; i < json_list.length(); i++) {
            String friend = json_list.getString(i);
            listItems.add(new Friend(friend, 123)); //Remove id from here. It should not exist!
        }

        return listItems;
    }

    private List<Poll> doPolls(JSONArray json_list) throws JSONException {
        List<Poll> listItems = new LinkedList<>();

        for ( int i = 0; i < json_list.length(); i++ ){
            JSONObject poll_json = json_list.getJSONObject(i);
            Poll poll = Poll.serialize(poll_json);

            listItems.add(poll);
        }

        return listItems;
    }


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
}
