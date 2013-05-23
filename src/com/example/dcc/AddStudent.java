package com.example.dcc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;

/**
 * AddStudent allows for a student to
 * add themselves to the database
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */

public class AddStudent extends Activity implements OnClickListener {

	private ProgressDialog mProgressDialog;

	private String project1 = "";
	private String project2 = "";
	private String studentEmail = "";
	private String url = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/add.php";
	
	private static final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";
	private static final String teamfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/teamlist.txt";

	private EditText Fname;
	private EditText Lname;
	private EditText Email;

	private Spinner FirstProject;
	private Spinner SecondProject;

	private Button AddButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_student);

		/* Progress dialog for download async task */
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Please wait while we get things ready");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		Fname = (EditText) findViewById(R.id.Fname);
		Lname = (EditText) findViewById(R.id.Lname);
		Email = (EditText) findViewById(R.id.email);
		FirstProject = (Spinner) findViewById(R.id.first_proj);
		SecondProject = (Spinner) findViewById(R.id.second_proj);
		AddButton = (Button) findViewById(R.id.addStudentButton);
		AddButton.setOnClickListener(this);

		/*
		 * If Student Info is filled out, populate fields with data from
		 * InternalStorage.txt
		 */
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/enotebook/InternalStorage.txt");
		if (file.exists()) {

			BufferedReader bufferedreader = null;
			try {
				bufferedreader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bufferedreader.readLine(); // Title (skip)
				bufferedreader.readLine(); // Student ID (xxx)
				bufferedreader.readLine(); // Director email
				bufferedreader.readLine(); // Team lead email
				studentEmail = bufferedreader.readLine(); // Student email
				String[] name = bufferedreader.readLine().split(" "); // Student
																		// name(first
																		// last)
				bufferedreader.readLine(); // Program Name(ex. Y2013)
				project1 = bufferedreader.readLine(); // Project 1
				project2 = bufferedreader.readLine(); // Project 2

				Fname.setText(name[0]);
				Lname.setText(name[1]);
				Email.setText(studentEmail);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/* Fill spinners with project data */
		setupSpinners();
	}

	@Override
	public void onClick(View arg0) {
		Updater add = new Updater();
		finish();
		add.execute();
	}

	/* Populate spinners, if teams are in the file, set spinners to those values */
	private void setupSpinners() {

		String add = "";
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(teamfile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item);

		adapter.setDropDownViewResource(R.layout.spinner_item);

		/* Read in data to populate spinners */
		try {
			while ((add = bufferedreader.readLine()) != null) {
				adapter.add(add);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FirstProject.setAdapter(adapter);
		SecondProject.setAdapter(adapter);

		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).toString().matches(project1)) {
				FirstProject.setSelection(i);
			}
			if (adapter.getItem(i).toString().matches(project2)) {
				SecondProject.setSelection(i);
			}
		}

	}

	private String getEditText(EditText edittext) {
		return edittext.getText().toString();
	}

	/* Async task to download team list */
	public class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(teamListUrl);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				@SuppressWarnings("resource")
				OutputStream output = new FileOutputStream(teamfile);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressDialog.setProgress(0);
			mProgressDialog.dismiss();
		}
	}

	/* Async Task to send update data to the server */
	public class Updater extends AsyncTask<Void, Integer, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("firstname",
						getEditText(Fname)));
				nameValuePairs.add(new BasicNameValuePair("lastname",
						getEditText(Lname)));
				nameValuePairs.add(new BasicNameValuePair("email",
						getEditText(Email)));
				nameValuePairs.add(new BasicNameValuePair("firstproject",
						FirstProject.getSelectedItem().toString()));
				nameValuePairs.add(new BasicNameValuePair("secondproject",
						SecondProject.getSelectedItem().toString()));
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				response.getEntity();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			/* Update complete toast */
			Toast.makeText(getApplicationContext(), "Student added!",
					Toast.LENGTH_SHORT).show();
		}
	}
}
