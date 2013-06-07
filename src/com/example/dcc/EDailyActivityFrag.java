package com.example.dcc;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.dcc.helpers.ActionItemData;
import com.example.dcc.helpers.ObjectStorage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View.OnClickListener;

/**
 * eDaily Activity Allows user to create eDailys and send them to the specified
 * email addresses in "InternalStorage" file
 *
 * @author Ashutosh Gupta
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */
public class EDailyActivityFrag extends Fragment implements OnClickListener {

    private EditText todayTF; // Today's Accomplishments Text Field
    private Button sendButton; // Send Button
    private String url = "http://www.virtualdiscoverycenter.net/wp-content/plugins/buddypress/bp-themes/bp-default/eDaily.php";
    String first = "";
    String last = "";
    String name = "";
    private EditText issues;
    private EditText dependability;
    private EditText reliability;
    private EditText hours;
    Activity activity;

    /**
     * Attaches all variables to corresponding .xml widgets and sets up the
     * listener (this class)
     *
     * @param savedInstanceState
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edailygui,
                container, false);

        activity = getActivity();

        todayTF = (EditText) view.findViewById(R.id.todayTF);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        issues = (EditText) view.findViewById(R.id.issues);
        dependability = (EditText) view.findViewById(R.id.dependability);
        reliability = (EditText) view.findViewById(R.id.reliability);
        hours = (EditText) view.findViewById(R.id.hours);
        sendButton.setOnClickListener(this);

        name = ObjectStorage.getUser().getName();

        boolean helper = false;

        for(int i = 0; i < name.length(); i++){

            if(name.charAt(i) == ' '){
                helper = true;
                continue;
            }
            if (helper){
                last += name.charAt(i);
            } else{
                first += name.charAt(i);
            }
        }
        return view;
    }

    /**
     * Gets student ID, calls Async task to send the file to the server
     */
    public void onClick(View v) {
            Uploader uploader = new Uploader();
            uploader.execute(url);

    }

    private String getEditText(EditText edittext) {
        return edittext.getText().toString();
    }

    /* Send eDaily to the server */
    public class Uploader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("ID", "" + ObjectStorage.getUser().getID()));
                nameValuePairs.add(new BasicNameValuePair("First", first));
                nameValuePairs.add(new BasicNameValuePair("Last", last));
                nameValuePairs.add(new BasicNameValuePair("Project", ObjectStorage.getUser().getProject()));
                nameValuePairs.add(new BasicNameValuePair("edaily-content",
                        getEditText(todayTF)));
                nameValuePairs.add(new BasicNameValuePair("Hours", getEditText(hours)));
                nameValuePairs.add(new BasicNameValuePair("Issues", getEditText(issues)));
                nameValuePairs.add(new BasicNameValuePair("Dependability", getEditText(dependability)));
                nameValuePairs.add(new BasicNameValuePair("Reliability", getEditText(reliability)));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                nameValuePairs.add(new BasicNameValuePair("reportdate", sdf.format(new Date())));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                Log.e("log_tag", "finished sending" + response.getStatusLine());
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

			/* Update complete toast */
            Toast.makeText(activity, "Report Sent!",
                    Toast.LENGTH_SHORT).show();
        }

    }

//    public void onBackPressed() {
//        activity.finish();
//    }
}
