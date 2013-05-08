/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.admin;

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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.dcc.R;

import utilities.StartDateDownloader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AdminActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private ProgressDialog mProgressDialog;
	private ProgressDialog nProgressDialog;

	private AbsListView studentList;
	private AbsListView teamList;

	private ArrayAdapter<String> studentlistadapter;
	private ArrayAdapter<String> teamlistadapter;

	private ArrayList<String> Students = new ArrayList<String>();
	private ArrayList<String> Teams = new ArrayList<String>();

	private ArrayList<String> students = new ArrayList<String>();
	private ArrayList<String> teams = new ArrayList<String>();

	private final String teamfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/teamlist.txt";
	private final String studentfile = Environment
			.getExternalStorageDirectory().getPath() + "/dcc/studentlist.txt";
	private final String studentListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keyfile.txt";
	private final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";

	private String AdminUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/admin.php?";
	private String teamDisplay = "";
	private String studentDisplay = "";
	private String startDate = setStartDate();
	
	private static String newstartDate = "";
	
	private Button editStudentsButton;
	private Button updateButton;

	private CheckBox sort;

	private EditText addTeam;

	private TextView conf;
	private TextView startText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);

		/* Progress dialog for update async task */
		nProgressDialog = new ProgressDialog(AdminActivity.this);
		nProgressDialog.setMessage("Updating...");
		nProgressDialog.setIndeterminate(false);
		nProgressDialog.setMax(100);
		nProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/* Progress dialog for download async task */
		mProgressDialog = new ProgressDialog(AdminActivity.this);
		mProgressDialog.setMessage("Please wait while we get things ready");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/* Execute DownloadTask to get the files to populate the lists */
		DownloadTask downloadtask = new DownloadTask();
		downloadtask
				.execute(studentListUrl, studentfile, teamListUrl, teamfile);

		/* Prevents the keyboard from showing on start */
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* Assign elements to the corresponding layout items */
		sort = (CheckBox) findViewById(R.id.sortcheckBox);
		studentList = (ListView) findViewById(R.id.admin_studentlist);
		teamList = (ListView) findViewById(R.id.admin_teamlist);
		startText = (TextView) findViewById(R.id.startdatetext);
		addTeam = (EditText) findViewById(R.id.addproject);
		updateButton = (Button) findViewById(R.id.admin_update_btn);
		editStudentsButton = (Button) findViewById(R.id.admin_editStudents_btn);

		/* Set listeners for Buttons */
		updateButton.setOnClickListener(this);
		editStudentsButton.setOnClickListener(this);

		/* Populate Lists */
		createLists();

		/* Set current start date above DatePicker button */
		startText.setText("Start date currently " + startDate);
	}

	/* Handles events from OnClickListeners */
	public void onClick(View v) {
		if (v.getId() == R.id.admin_update_btn) {
			confDialog();// show dialog to confirm the changes being made
		} else if (v.getId() == R.id.admin_editStudents_btn) {
			Intent intent = new Intent(AdminActivity.this, EditStudents.class);
			startActivity(intent);
		}
	}

	/*
	 * Set up and execute the Async Task which handle sending update data to the
	 * server
	 */
	@SuppressWarnings("unchecked")
	private void runUpdate() {
		Updater updater = new Updater();
		updater.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}

	/* Changes the state of CheckBox */
	public void onCheckedChanged(CompoundButton button, boolean checked) {
		if (sort.isChecked())
			sort.setChecked(false);
		else
			sort.setChecked(true);
	}

	/* Get text entered into EditText */
	private String getText(EditText edittext) {
		return edittext.getText().toString();
	}

	/* Get all items checked in ListViews */
	private void getCheckedItems() {
		SparseBooleanArray checked = teamList.getCheckedItemPositions();
		for (int i = 0; i < teamList.getCount(); i++) {
			if (checked.get(i)) {
				teamDisplay += (String) teamList.getItemAtPosition(i) + "\n";// for
																				// confDialog
				teams.add(teamList.getItemAtPosition(i).toString());
			}
		}
		checked = studentList.getCheckedItemPositions();
		for (int i = 0; i < studentList.getCount(); i++) {
			if (checked.get(i)) {
				String FullLine = (String) studentList.getItemAtPosition(i);
				String[] getID = FullLine.split(" ");
				studentDisplay += getID[1] + " " + getID[2] + "\n";// for
																	// confDialog
				String thing = "";
				for (int j = 1; j < getID.length; j++) {
					thing += getID[j] + " ";
				}
				students.add(thing);
			}
		}
	}

	/* Set up ListViews */
	private void createLists() {
		studentlistadapter = new ArrayAdapter<String>(this,
				R.layout.simple_checked_row, Students);
		studentlistadapter.setNotifyOnChange(true);
		if (!studentlistadapter.isEmpty()) {
			studentlistadapter.clear();
		}
		studentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		studentList.setAdapter(studentlistadapter);
		teamlistadapter = new ArrayAdapter<String>(this,
				R.layout.simple_checked_row, Teams);
		teamlistadapter.setNotifyOnChange(true);
		if (!teamlistadapter.isEmpty()) {
			teamlistadapter.clear();
		}
		teamList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		teamList.setAdapter(teamlistadapter);
	}

	/* Populate/Re-populate ListViews */
	private void populateLists(String file, String id) {
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

		/* Clear anything currently in the views */
		if (id == "teamID" && !teamlistadapter.isEmpty()) {
			teamlistadapter.clear();
		}
		if (id == "studentID" && !studentlistadapter.isEmpty()) {
			studentlistadapter.clear();
		}

		/* Read in data to populate lists */
		try {
			if (id.matches("studentID")) {
				/* Skip the first line as it is \n */
				bufferedreader.readLine();
			}
			while ((add = bufferedreader.readLine()) != null) {
				if (id == "studentID") {
					StudentNumber++;
					studentlistadapter.add(Integer.toString(StudentNumber)
							+ " " + add);
				} else if (id == "teamID")
					teamlistadapter.add(add);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Gather updates to be made */
	private String confMessage() {
		String message = "";
		getCheckedItems();

		/* Check if changes are to be made */
		/* If so, add them to the message */
		if (!newstartDate.matches("")) {
			message += "The start date will be changed to: " + newstartDate
					+ "\n\n";
		}
		if (!getText(addTeam).matches("")) {
			message += "The project: \"" + getText(addTeam)
					+ "\" will be added.\n\n";
		}
		if (!teams.isEmpty()) {
			message += "The following projects will be deleted:\n";
			message += teamDisplay + "\n";
		}
		if (!students.isEmpty()) {
			message += "The following students will be deleted:\n";
			message += studentDisplay + "\n\n";
		}
		if (sort.isChecked()) {
			message += "The Keyfile will be sorted!";
		}
		return message;
	}

	/* Alert Dialog used to confirm update actions */
	private void confDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.admin_conf_popup, null);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		conf = (TextView) popup.findViewById(R.id.admin_conf);
		conf.setText("");
		conf.setText(confMessage());
		alertBuilder
				.setCancelable(true)
				.setTitle("The following changes will be made:")
				.setView(popup)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								reset();
								dialog.cancel();
							}
						})
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								runUpdate();
							}
						});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	/* Reset all variables so they are not changed and to display new values */
	private void reset() {
		addTeam.setText("");
		conf.setText("");
		teamDisplay = "";
		studentDisplay = "";
		newstartDate = "";
		teams.clear();
		students.clear();
		startDate = setStartDate();
		startText.setText("Start date currently " + startDate);
	}

	/* Set the start date of the program by getting the data from the server */
	private String setStartDate() {
		String programStart = "";
		StartDateDownloader getDate = new StartDateDownloader();
		getDate.execute("");
		try {
			programStart = getDate.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return programStart;
	}

	/*
	 * Method called from Change Start Date button
	 * android:onClick="showDatePickerDialog"
	 */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new setStartDate();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	/* Set up DatePicker and Handle received data */
	public static class setStartDate extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		/* Get current date to display in DatePicker */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		/* When a date is selected, get it and format it to YYY-MM-DD */
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			monthOfYear += 1;// month starts at 0
			newstartDate = Integer.toString(year) + "-";
			if (monthOfYear < 10) {
				newstartDate += "0" + Integer.toString(monthOfYear) + "-";
			} else {
				newstartDate += Integer.toString(monthOfYear) + "-";
			}
			if (dayOfMonth < 10) {
				newstartDate += "0" + Integer.toString(dayOfMonth);
			} else {
				newstartDate += Integer.toString(dayOfMonth);
			}
		}
	}

	/* Async task to download student and team lists */
	public class DownloadTask extends AsyncTask<String, Integer, String> {
		File file, file2;

		@Override
		protected String doInBackground(String... params) {
			file = new File(params[1]);
			file2 = new File(params[3]);
			try {
				URL url = new URL(params[0]);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(params[1]);

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
				URL url = new URL(params[2]);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(params[3]);

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
			populateLists(studentfile, "studentID");
			populateLists(teamfile, "teamID");
			teamlistadapter.notifyDataSetChanged();
			studentlistadapter.notifyDataSetChanged();
		}
	}

	/* Async Task to send update data to the server */
	public class Updater extends AsyncTask<ArrayList<String>, Integer, String> {

		@Override
		protected String doInBackground(ArrayList<String>... params) {
			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("apw", "bobbill"));
				nameValuePairs.add(new BasicNameValuePair("startdate",
						newstartDate));
				nameValuePairs.add(new BasicNameValuePair("startdateadmin",
						"startdateadmin"));
				nameValuePairs.add(new BasicNameValuePair("addproject",
						getText(addTeam)));
				nameValuePairs.add(new BasicNameValuePair("addprojectadmin",
						"addprojectadmin"));
				if (sort.isChecked()) {
					nameValuePairs.add(new BasicNameValuePair("sort", "sort"));
					nameValuePairs.add(new BasicNameValuePair("sortadmin",
							"sortadmin"));
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AdminUrl);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				response.getEntity();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			/* Team and student removal must be handled one at a time */
			if (!students.isEmpty()) {
				for (int i = 0; i < students.size(); i++) {
					try {
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("apw",
								"bobbill"));
						nameValuePairs.add(new BasicNameValuePair(
								"removeperson", students.get(i)));
						nameValuePairs.add(new BasicNameValuePair(
								"removepersonadmin", "removepersonadmin"));
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(AdminUrl);
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						response.getEntity();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			/* Team and student removal must be handled one at a time */
			if (!teams.isEmpty()) {
				for (int i = 0; i < teams.size(); i++) {
					try {
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("apw",
								"bobbill"));
						nameValuePairs.add(new BasicNameValuePair(
								"removeproject", teams.get(i)));
						nameValuePairs.add(new BasicNameValuePair(
								"removeprojectadmin", "removeprojectadmin"));
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(AdminUrl);
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						response.getEntity();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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
			downloadtask.execute(studentListUrl, studentfile, teamListUrl,
					teamfile);

			/* Reset confirmation dialog data */
			reset();

			/* Update complete toast */
			Toast.makeText(getApplicationContext(), "Update Complete!",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * When returning from EditStudents Re-populate student list to reflect any
	 * changes made
	 */
	protected void onResume() {
		super.onResume();
		populateLists(studentfile, "studentID");
		studentlistadapter.notifyDataSetChanged();
	}

	public void onBackPressed() {
		finish();
	}
}
