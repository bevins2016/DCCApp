package com.example.dcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dcc.helpers.ObjectStorage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * eDaily frag Allows user to create eDailys and send them to the specified
 * email addresses in "InternalStorage" file
 *
 * @author Ashutosh Gupta
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */
public class EDailyActivityFrag extends Fragment implements OnClickListener {

    private EditText todayTF, datefor; // Today's Accomplishments Text Field
    private Button sendButton, saveButton; // Send Button
    private String url = "/wp-content/plugins/buddypress/bp-themes/bp-default/eDaily.php";
    private EditText hours;
    Activity activity;
    private Context context;
    private boolean hasCache;
    private File file;
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
        datefor = (EditText) view.findViewById(R.id.datefor);

        checkForCache();
        //set the date to the standard format that is used on the website.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        datefor.setText(sdf.format(new Date()));
        sendButton = (Button) view.findViewById(R.id.sendButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        hours = (EditText) view.findViewById(R.id.hours);
        sendButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        return view;
    }

    private void loadCachedText(){
        try {
            BufferedReader bin = new BufferedReader(new FileReader(file));
            String date = bin.readLine();
            String hour = bin.readLine();
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = bin.readLine()) != null){
                sb.append(text);
            }

            datefor.setText(date);
            hours.setText(hour);
            todayTF.setText(sb.toString());

            bin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToCache(){
        try {
            BufferedWriter bout = new BufferedWriter(new FileWriter(file));
            bout.write(datefor.getText().toString()+"\n");
            bout.write(hours.getText().toString()+"\n");
            bout.write(todayTF.getText().toString());
            bout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void checkForCache(){
        file = new File(context.getFilesDir(), "EDAILY_CACHE");
        hasCache = file.exists();
        if(hasCache){

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            loadCachedText();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            file.delete();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Load From Saved File?\n(Selecting \"NO\" Deletes Cached File)").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.context = activity;
    }

    /**
     * Gets student ID, calls Async task to send the file to the server
     */
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sendButton:
                Uploader uploader = new Uploader();
                //start async task
                uploader.execute(url);
                break;
            case R.id.saveButton:
                saveToCache();
        }
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
                nameValuePairs.add(new BasicNameValuePair("Project", ObjectStorage.getUser().getProject()));
                nameValuePairs.add(new BasicNameValuePair("edaily-content",
                        getEditText(todayTF)));
                nameValuePairs.add(new BasicNameValuePair("Hours", getEditText(hours)));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                nameValuePairs.add(new BasicNameValuePair("reportdate",getEditText(datefor)));
                //start http client and send the namevaluepairs
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
            file.delete();
        }

    }

}
