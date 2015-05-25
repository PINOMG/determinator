package com.pinomg.determinator.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrik on 2015-05-11.
 */

public class ApiConnector extends AsyncTask<String, Boolean, JSONObject> {
    private Context context;
    private List<Exception> errors = new ArrayList<Exception>();

    public ApiConnector(Context context){
        this.context = context;
    }


     /*
    input urls:
    url[0] is method
    url[1] is url
    url[2] is arguments (optional)
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            // Check the network status of the device.
            networkStatus();

            return request(params);
        } catch (IOException e) {
            errors.add(e);
            return null;
        } catch (NoConnectionException e){
            errors.add(e);
            return null;
        } catch (JSONException e) {
            errors.add(e);
            return null;
        }
    }

    // onPostExecute displays the results of the call
    @Override
    protected void onPostExecute(JSONObject result) {
        for(Exception e : errors){
            if(e instanceof NoConnectionException)
                showToast(e.getMessage());

            Log.e("Connect error:", e.getMessage());
        }

        if( result != null) {
            Log.d("Final:", result.toString());
        }
    }

    private void networkStatus() throws NoConnectionException {
        // Gets the URL from the UI:s text field.
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            throw new NoConnectionException("Check your internet connection.");
        }
    }

    private JSONObject request(String[] params) throws IOException, JSONException {
        InputStream is = null;

        // Restriction of length of the api call.
        int len = 5000;

        // Start the connection
        try {
            URL url = new URL(params[1]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000 /* milliseconds */);
            conn.setConnectTimeout(5500 /* milliseconds */);
            conn.setRequestMethod(params[0]);
            conn.setDoInput(true);
            conn.setRequestProperty("charset", "utf-8");

            if(params[0].equals( "POST" )) //Change content-type header if sending a POST request.
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if(params.length > 2){ //There is arguments
                String urlParameters  = params[2];
                byte[] postData       = urlParameters.getBytes( Charset.forName("UTF-8"));
                int    postDataLength = postData.length;
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

                try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                    wr.write( postData );
                }
            }

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            Log.d("ApiConnector", "The response is: " + response);
            is = conn.getInputStream();

            // Turn the response into a JSON object.
            JSONTokener token = new JSONTokener(readIt(is, len));
            JSONObject obj = new JSONObject(token);

            return obj;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Error handling when no connection.
    private void showToast(String s){
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this.context, s, duration);
        toast.show();
    }
}