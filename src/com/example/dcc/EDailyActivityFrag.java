package com.example.dcc;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Fragment;
import android.view.*;
import android.widget.*;
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
    private ArrayAdapter<String> adapter;
    private Button sendButton; // Send Button
    //http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/save.php
    private String url = "http://www.virtualdiscoverycenter.net/wp-content/plugins/buddypress/bp-themes/bp-default/eDaily.php";//http://www.facebook.com/l.php?u=http%3A%2F%2Fwww.virtualdiscoverycenter.net%2Fwp-content%2Fplugins%2Fbuddypress%2Fbp-themes%2Fbp-default%2FeDaily.php&h=3AQH7TTNw
    private String popupText = "Reports can not be submitted until sudent info is set!\n\nFill this out now?";
    String first = "";
    String last = "";
    String name = "";
    //ActionItemData data;
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

        //data = new ActionItemData(name, "004");

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

        File f = new File(Environment.getExternalStorageDirectory()
                + "/enotebook/InternalStorage.txt");
        if (!f.exists()) {
            setDefaultsPopup();
        }
        return view;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(activity, SetDefaults.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Gets student ID, calls Async task to send the file to the server
     */
    public void onClick(View v) {
        File f = new File(Environment.getExternalStorageDirectory() + "/enotebook/InternalStorage.txt");
        if(f.exists()) {
            String studentID = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(
                        "sdcard/eNotebook/InternalStorage.txt")));
                br.readLine(); // Skip Title
                studentID = br.readLine(); // Get Student ID#
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uploader uploader = new Uploader();
            uploader.execute(url, studentID);
            activity.finish();
        }
        else {
            setDefaultsPopup();
        }
    }

    private String getEditText(EditText edittext) {
        return edittext.getText().toString();
    }

    /*
     * Creates a popup dialog using alertbuilder to ask the user to fill out
     * their information in SetDefaults.txt if InternalStorage.txt does not exit
     */
    private void setDefaultsPopup() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View popup = factory.inflate(R.layout.default_popup, null);
        TextView textview = (TextView) popup.findViewById(R.id.defaultPopupText);
        textview.setText(popupText);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setCancelable(true)
                .setTitle("Student Info")
                .setView(popup)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(activity,
                                        SetDefaults.class);
                                startActivity(intent);
                            }
                        });

        AlertDialog alertdialog = alertBuilder.create();
        alertdialog.show();
    }

    /* Send eDaily to the server */
    public class Uploader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

			/* Send to server */
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//				nameValuePairs.add(new BasicNameValuePair("id", params[1]));
//				nameValuePairs.add(new BasicNameValuePair("type", "edaily"));
//				nameValuePairs.add(new BasicNameValuePair("edaily",
//						getEditText(todayTF)));


                nameValuePairs.add(new BasicNameValuePair("ID", params[1]));
                nameValuePairs.add(new BasicNameValuePair("First", first));
                nameValuePairs.add(new BasicNameValuePair("Last", last));
                nameValuePairs.add(new BasicNameValuePair("Project", SetDefaults.project1));
                nameValuePairs.add(new BasicNameValuePair("edaily-content",
                        getEditText(todayTF)));
                nameValuePairs.add(new BasicNameValuePair("Hours", getEditText(hours)));
                nameValuePairs.add(new BasicNameValuePair("Issues", getEditText(issues)));
                nameValuePairs.add(new BasicNameValuePair("Dependability", getEditText(dependability)));
                nameValuePairs.add(new BasicNameValuePair("Reliability", getEditText(reliability)));
              //  nameValuePairs.add(new BasicNameValuePair("reportdate", data.getDate()));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
			
			/* Write to local file */
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMddyy", Locale.US);
                BufferedReader br = new BufferedReader(new FileReader(new File(
                        "sdcard/eNotebook/InternalStorage.txt")));
                br.readLine(); // Skip Title
                String studentID = br.readLine(); // Get Student ID#
                br.close();
                String filename = "/enotebook/edailys/" + studentID + "_" + sdf.format(new Date()) + "_edaily.txt";
                FileWriter internal = new FileWriter(Environment.getExternalStorageDirectory()
                        + filename);
                internal.append(getEditText(todayTF));
                internal.flush();
                internal.close();
            } catch (IOException e) {
                e.printStackTrace();
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

//    /* Populate spinners, if teams are in the file, set spinners to those values */
//    private void setupSpinners() {
//
//        String add = "";
//        BufferedReader bufferedreader = null;
//        adapter = new ArrayAdapter<String>(getApplicationContext(),
//                R.layout.spinner_item);
//
//        adapter.setDropDownViewResource(R.layout.spinner_item);
//
//           for(int i = 1; i < 10; i++){
//               adapter.add("" + i);
//           }
//
//
//
//        issues.setAdapter(adapter);
//        dependability.setAdapter(adapter);
//        reliability.setAdapter(adapter);
//
////        for (int i = 0; i < adapter.getCount(); i++) {
////            if (adapter.getItem(i).toString().matches(project1)) {
////                FirstProject.setSelection(i);
////            }
////            if (adapter.getItem(i).toString().matches(project2)) {
////                SecondProject.setSelection(i);
////            }
////        }
//    }

    public void onBackPressed() {
        activity.finish();
    }
}
