/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dcc.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

public class EditStudents extends Activity implements OnItemClickListener {

	private ProgressDialog mProgressDialog;
	private ProgressDialog nProgressDialog;

	private AbsListView studentList;
	private ArrayAdapter<String> studentlistadapter;
	private ArrayList<String> students = new ArrayList<String>();

	private final String studentfile = Environment
			.getExternalStorageDirectory().getPath() + "/dcc/studentlist.txt";
	private final String teamfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/teamlist.txt";
	private final String studentListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keyfile.txt";
	private final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";
	private String editUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/edit.php";

	private String studentId = "";
	private String FirstName = "";
	private String LastName = "";
	private String eMail = "";
	private String project1 = "";
	private String project2 = "";

	private EditText Fname;
	private EditText Lname;
	private EditText Email;

	private Spinner FirstProject;
	private Spinner SecondProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_students);

		/* Assign variable to layout items */
		studentList = (ListView) findViewById(R.id.editStudents_list);

		/* Progress dialog for download async task */
		mProgressDialog = new ProgressDialog(EditStudents.this);
		mProgressDialog.setMessage("Please wait while we get things ready");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/* Progress dialog for update async task */
		nProgressDialog = new ProgressDialog(EditStudents.this);
		nProgressDialog.setMessage("Updating...");
		nProgressDialog.setIndeterminate(false);
		nProgressDialog.setMax(100);
		nProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		/* Populate Lists */
		createLists();

		/* Execute DownloadTask to get the files to populate the lists */
		DownloadTask downloadtask = new DownloadTask();
		downloadtask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_students, menu);
		return true;
	}

	/*
	 * When a student is selected, get their data, 
	 * format it, and bring up the edit dialog
	 */
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String[] student = studentList.getItemAtPosition(position).toString()
				.split(" ");
		studentId = Integer.toString(Integer.parseInt(student[0]) - 1);
		FirstName = student[2];
		LastName = student[1];
		eMail = student[3];
		project1 = student[4];
		project2 = student[5];

		editStudentDialog();
	}

	/* Get text entered into EditText */
	private String getText(EditText edittext) {
		return edittext.getText().toString();
	}

	/* Set up ListViews */
	private void createLists() {
		studentlistadapter = new ArrayAdapter<String>(this,
				R.layout.simple_row, students);
		studentlistadapter.setNotifyOnChange(true);
		if (!studentlistadapter.isEmpty()) {
			studentlistadapter.clear();
		}
		studentList.setOnItemClickListener(this);
		studentList.setAdapter(studentlistadapter);
	}

	/* Populate/Re-populate ListViews */
	private void populateLists(String file) {
		String add = "";

		/*
		 * Student numbers are not stored in the keyfile, so this counter is
		 * used to add them to the list
		 */
		int StudentNumber = 0;

		/* Set up lists */
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/* Clear anything currently in the view */
		studentlistadapter.clear();

		/* Read in data to populate lists */
		try {
			bufferedreader.readLine();// skip first line it is \n
			while ((add = bufferedreader.readLine()) != null) {
				StudentNumber++;
				studentlistadapter.add(Integer.toString(StudentNumber) + " "
						+ add);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Alert Dialog used to edit student info */
	private void editStudentDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.edit_student_popup, null);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

		Fname = (EditText) popup.findViewById(R.id.Fname_text);
		Lname = (EditText) popup.findViewById(R.id.Lname_text);
		Email = (EditText) popup.findViewById(R.id.Email_text);
		FirstProject = (Spinner) popup.findViewById(R.id.first_proj);
		SecondProject = (Spinner) popup.findViewById(R.id.second_proj);
		
		Fname.setText(FirstName);
		Lname.setText(LastName);
		Email.setText(eMail);

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

		alertBuilder
				.setCancelable(true)
				.setTitle("Edit Student Info:")
				.setView(popup)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								FirstName = getText(Fname);
								LastName = getText(Lname);
								eMail = getText(Email);
								project1 = FirstProject.getSelectedItem()
										.toString();
								project2 = SecondProject.getSelectedItem()
										.toString();
								EditUpdate updater = new EditUpdate();
								updater.execute();
							}
						});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	/* Async task to download student and team lists */
	public class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(studentListUrl);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(studentfile);

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
			try {
				URL url = new URL(teamListUrl);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
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

			/* Repopulate Lists with updated data */
			populateLists(studentfile);
			studentlistadapter.notifyDataSetChanged();
		}
	}

	/* Async Task to send update data to the server */
	public class EditUpdate extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", studentId));
				nameValuePairs.add(new BasicNameValuePair("firstname",
						FirstName));
				nameValuePairs
						.add(new BasicNameValuePair("lastname", LastName));
				nameValuePairs.add(new BasicNameValuePair("email", eMail));
				nameValuePairs.add(new BasicNameValuePair("firstproject",
						project1));
				nameValuePairs.add(new BasicNameValuePair("secondproject",
						project2));
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(editUrl);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				response.getEntity();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			nProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			nProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			nProgressDialog.setProgress(0);
			nProgressDialog.dismiss();

			/* Execute DownloadTask to get the files to populate the lists */
			DownloadTask downloadtask = new DownloadTask();
			downloadtask.execute();

			/* Update complete toast */
			Toast.makeText(getApplicationContext(), "Update Complete!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onBackPressed() {
		finish();
	}
}
