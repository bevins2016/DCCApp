package com.example.dcc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dcc.helpers.ActionItemData;
import com.example.dcc.helpers.ObjectStorage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * eDaily Activity Allows user to create eDailys and send them to the specified
 * email addresses in "InternalStorage" file
 * 
 * @author Ashutosh Gupta
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */
public class EDailyActivity extends Activity implements OnClickListener {

	private EditText todayTF; // Today's Accomplishments Text Field
    private ArrayAdapter<String> adapter;
	private Button sendButton; // Send Button
	//http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/save.php
	private String url = "http://www.virtualdiscoverycenter.net/wp-content/plugins/buddypress/bp-themes/bp-default/eDaily.php";//http://www.facebook.com/l.php?u=http%3A%2F%2Fwww.virtualdiscoverycenter.net%2Fwp-content%2Fplugins%2Fbuddypress%2Fbp-themes%2Fbp-default%2FeDaily.php&h=3AQH7TTNw
    String first = "";
    String last = "";
    String name = "";
    ActionItemData data;
    private EditText issues;
    private EditText dependability;
    private EditText reliability;
    private EditText hours;

	/**
	 * Attaches all variables to corresponding .xml widgets and sets up the
	 * listener (this class)
	 * 
	 * @param savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edailygui);

		todayTF = (EditText) findViewById(R.id.todayTF);
		sendButton = (Button) findViewById(R.id.sendButton);
        hours = (EditText) findViewById(R.id.hours);
		sendButton.setOnClickListener(this);

        name = ObjectStorage.getUser().getName();

        data = new ActionItemData(name, "004");

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			//Intent intent = new Intent(this, SetDefaults.class);
			//startActivity(intent);
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
			finish();
		}
	}

	private String getEditText(EditText edittext) {
		return edittext.getText().toString();
	}

	/* Send eDaily to the server */
	public class Uploader extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			/* Send to server */
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
                nameValuePairs.add(new BasicNameValuePair("reportdate", data.getDate()));

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
			Toast.makeText(getApplicationContext(), "Report Sent!",
					Toast.LENGTH_SHORT).show();
		}

	}


	public void onBackPressed() {
		finish();
	}
}
