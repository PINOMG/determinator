package com.pinomg.determinator.api;

import android.content.Context;
import android.util.Log;

import com.pinomg.determinator.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by patrik on 2015-05-11.
 */
public class ApiHandler {
    private static final String BASE_URL = "http://192.168.1.4/pinomg/";

    private static final String ENDPOINT_FRIEND = "friend/";
    private static final String ENDPOINT_LOGIN = "login/";
    private static final String ENDPOINT_USER = "user/";
    private static final String ENDPOINT_POLL = "poll/";

    private Context context;

    public ApiHandler(Context context){
        this.context = context;
    }

    public ArrayList<Friend> getFriends(String user) {
        //Initiating and building urls.
        Log.e("Initiating", "Get friends");
        String urls[] = {"GET", BASE_URL + ENDPOINT_FRIEND + user};


    }

    public boolean login(String username, String password){
        //Initiating and building urls.
        Log.e("Initiating", "Login");


        String urls[] = {"POST", BASE_URL + ENDPOINT_LOGIN, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    public boolean createUser(String username, String password){
        //Initiating and building urls.
        Log.e("Initiating", "Creating user");


        String urls[] = {"POST", BASE_URL + ENDPOINT_USER, "username=" + username + "&password=" + password};

        return apiCall(urls);
    }

    public ArrayList<?> apiListCall(String[] urls){
        //Response array
        ArrayList<?> listItems = null;

        //Do the call
        JSONObject response;
        try {
            response = new ApiConnector(context).execute(urls).get();

            if( response != null){
                JSONObject data = response.getJSONObject("data");
                JSONArray json_list = data.getJSONArray("items");

                listItems = doFriends(json_list);
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

    public ArrayList<Friend> doFriends(JSONArray json_list) throws JSONException {
        ArrayList<Friend> listItems = new ArrayList<Friend>();

        for (int i = 0; i < json_list.length(); i++) {
            String friend = json_list.getString(i);
            listItems.add(new Friend(friend, 123)); //Remove id from here. It should not exist!
        }

    }

    public boolean apiCall(String[] urls){ //Used for all simple calls. Those who doesn't expect a response
        JSONObject response;
        String message = "";

        try { // Try do the API call. Exceptions cast here is not "manageable". (Error on server side etc.)
            response = new ApiConnector(context).execute(urls).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        try {
            if (response != null) {
                JSONObject error = response.getJSONObject("error"); //This will throw an exception if error does NOT exist.

                if (error != null) { // Here we check if there is an authentication error or if params are on wrong form, which will always be true.
                    message = error.getString("message");
                    int code = error.getInt("code");
                    throw new ApiErrorException(message, code); 
                }
                return false; //This should never happen, since error can't be null ??
            } else {
                return false;
            }
        } catch (JSONException e) { // Error did NOT exist.
            JSONObject data = null;

            try { //Try to parse response.
                data = response.getJSONObject("data");
                return data != null; // This will always return true.
            } catch (JSONException e1) { //Something wrong with the response, we can't guarantee that user is authenticated.
                e1.printStackTrace();
                return false;
            }
        } catch (ApiErrorException e) { // We did not authenticate. Notify that to user.
            Log.d("Error", e.getMessage() + " " + e.getCode());
            return false;
        }
    }
}
