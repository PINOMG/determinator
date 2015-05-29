package com.pinomg.determinator.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pinomg.determinator.R;
import com.pinomg.determinator.helpers.Session;
import com.pinomg.determinator.net.ApiErrorException;
import com.pinomg.determinator.net.ApiHandler;

/**
 * A login screen that offers login via username/password.
 *
 * When finished sends the user to MainActivity.
 */
public class LoginActivity extends Activity{

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    protected ApiHandler apiHandler;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SessionManagement
        session = new Session(getApplicationContext());
        apiHandler = new ApiHandler(getBaseContext());

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mCreateAccountButton = (Button) findViewById(R.id.create_account_button);
        mCreateAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        if (fieldsValid()) {

            // Store values at the time of the login attempt.
            String username = mUsernameView.getText().toString();
            String password = mPasswordView.getText().toString();

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt. Will need login call to be run on
            // other thread than main to spin properly.
            showProgress(true);

            boolean success = false;
            try {
                success = apiHandler.login(username, password);
            } catch (ApiErrorException e) {
                e.printStackTrace();
            }
            showProgress(false);

            if (success) {
                session.createLoginSession(username);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
    }

    public void createAccount() {
        if(fieldsValid()) {
            showProgress(true);

            String[] params = {
                    mUsernameView.getText().toString(),
                    mPasswordView.getText().toString()
                };
            new createAccountTask().execute(params);
        }
    }

    private class createAccountTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

            Log.d("CREATE", "Start asynctask");

            String username = params[0];
            String password = params[1];

            boolean success = false;
            try {
                success = apiHandler.createUser(username, password);
                Log.d("CREATE", "Success! " + success);
            } catch(ApiErrorException e) {
                Log.d("CREATE", "FAIL: " + e.getMessage());
                success = false;
            } finally {
                Log.d("CREATE", "finally");
            }

            if(success) {
                return username;
            } else {
                return null;
            }
        }

        protected void onPostExecute(String username) {
            showProgress(false);

            if(username != null) {
                session.createLoginSession(username);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Account creation failed! Please try again!",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if input fields are valid
     */
    private boolean fieldsValid() {

        boolean valid = true;
        View focusView = null;

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if ( TextUtils.isEmpty(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            valid = false;
        }

        // If not valid, sets focus
        if(!valid) {
            focusView.requestFocus();
        }

        return valid;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}



