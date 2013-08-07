package com.example.dcc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.HttpConnection;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private String mUser;
    private String mPassword;

    /* UI references. */
    private EditText mUserView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private Context context;
    boolean autoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;
        ObjectStorage.setUser(new User());

        /* Set up the login form. */
        mUser = "";
        mUserView = (EditText) findViewById(R.id.email);
        mUserView.setText(mUser);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent)
                    {
                        if (id == R.id.login || id == EditorInfo.IME_NULL)
                        {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        attemptLogin();
                    }
                });

        try{
            SharedPreferences sp1=this.getSharedPreferences("CurrentUser", MODE_PRIVATE);

            mUser =sp1.getString("UserName", null);
            mPassword = sp1.getString("PassWord", null);

            if(!sp1.getString("UserName", null).equals(""))
            {
                autoLogin = true;
                showProgress(true);
                attemptLogin();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin()
    {

        mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
        showProgress(true);

        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        if(!autoLogin){
            // Store values at the time of the login attempt.
            try{
                mUser = mUserView.getText().toString();
                mPassword = mPasswordView.getText().toString();
            }catch(NullPointerException e){
                mUser = mPassword = "";
            }
        } else {

            SharedPreferences sp1=this.getSharedPreferences("CurrentUser", MODE_PRIVATE);

            try{
                if(!sp1.getString("UserName", null).equals("")){
                    mUser =sp1.getString("UserName", null);
                    mPassword = sp1.getString("PassWord", null);
                }
            }catch(Exception e){
                autoLogin = false;
                attemptLogin();
            }
        }

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword))
        {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4)
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(mUser))
        {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }
        //Reset auto login
        autoLogin = false;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask();

            /*Attempt Login, If failure display a toast to alert the user to possible reason*/
            try {
                mAuthTask.execute();

                /*if(!status){
                    Toast.makeText(context, "Login Failure Please Verify Credentials and Try Again", Toast.LENGTH_LONG).show();
                }*/
/*
            } catch (InterruptedException e) {
                Toast.makeText(context, "Login Failure Please Try Again Later", Toast.LENGTH_LONG).show();
            } catch (ExecutionException e) {
                Toast.makeText(context, "Login Failure Please Try Again Later.", Toast.LENGTH_LONG).show();
            } catch (TimeoutException e) {
                /*Caused by a server timeout / slow internet */
              //  Toast.makeText(context, "Connection Timout", Toast.LENGTH_LONG).show();
            } catch (CancellationException e){
                /*Caused if there is no network*/
                Toast.makeText(context, "Network Failure Exception Please Check Settings And Try Again", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void showProgress(final boolean show) {
        Log.e("LoginActivity", "SHOW PROGRESS: "+show);
        int shortAnimTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mLoginStatusView.setVisibility(View.VISIBLE);
        mLoginStatusView.animate().setDuration(shortAnimTime)
                .alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginStatusView.setVisibility(show ? View.VISIBLE
                                : View.GONE);
                    }
                });

        mLoginFormView.setVisibility(View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime)
                .alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE
                                : View.VISIBLE);
                    }
                });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try{
                if (!HttpConnection.login(mUser, mPassword)){
                    cancel(true);
                    return false;
                }

            }catch(Exception e){
                cancel(true);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;


            if (success)
            {
                SharedPreferences mPreferences;
                mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.putString("UserName", mUser);
                editor.putString("PassWord", mPassword);
                editor.commit();
                showProgress(false);
                startActivity(new Intent(context, EasterEggs.class));
                finish();
            } else {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            autoLogin = false;
            mAuthTask = null;
            showProgress(false);
        }
    }
}
