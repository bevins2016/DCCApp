package com.example.dcc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Set Defaults Activity Uses internal storage to store key strings for the
 * application's use. These strings include the director's email, the team
 * lead's email, and the student's number
 * 
 * @author Ashutosh Gupta
 * @author Chris Crowell <crowelch@mail.uc.edu>
 */
public class SetDefaults extends Activity implements OnClickListener {
	private EditText directorEmailTF; // Director's Email Address Text Field
	private EditText teamLeadEmailTF; // Team Lead's Email Address Text Field
	private EditText studentEmailTF; // Student's Email Address Text Field
	private EditText studentNumberTF; // Student Number Text Field
	private EditText studentNameTF; // Student Name Text Field
	private EditText programTF; // Program Name & Year (ie Y2013)

	private Button doneButton; // Done Button

	private FileOutputStream fos; // File Output Stream that accesses the
									// "internalStorage"
	private ProgressDialog mProgressDialog;

	private ArrayAdapter<String> adapter;

	private Spinner FirstProject;
	private Spinner SecondProject;

	private String project1 = "";
	private String project2 = "";

	private final String teamfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/teamlist.txt";
	private final String studentfile = Environment
			.getExternalStorageDirectory().getPath() + "/dcc/studentlist.txt";
	private final String studentListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keyfile.txt";
	private final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";

	/**
	 * Attaches corresponding variables to corresponding widgets in .xml file
	 * and start up the file stream for the storage of the key strings
	 * 
	 * @param savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setdefault);

		/* Progress dialog for download async task */
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Please wait while we get things ready");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/* Attach instance variables to corresponding .xml widgets */
		directorEmailTF = (EditText) findViewById(R.id.directorEmailTF);
		teamLeadEmailTF = (EditText) findViewById(R.id.teamLeadTF);
		studentEmailTF = (EditText) findViewById(R.id.studentemailTF);
		studentNumberTF = (EditText) findViewById(R.id.studentNumber);
		studentNameTF = (EditText) findViewById(R.id.studentName);
		programTF = (EditText) findViewById(R.id.programName);
		FirstProject = (Spinner) findViewById(R.id.project1);
		SecondProject = (Spinner) findViewById(R.id.project2);
		doneButton = (Button) findViewById(R.id.doneButton);
		doneButton.setOnClickListener(this);

		/*
		 * If the internal storage file already exists, populate the widgetswith
		 * its data
		 */
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					Environment.getExternalStorageDirectory()
							+ "/enotebook/InternalStorage.txt")));
			br.readLine(); // Skip Title
			studentNumberTF.setText(br.readLine());
			/*
			 * Check if there is an email address if not, do nothing, as Dr.
			 * Williams' email is the default set in the layout
			 */
			String dirEmail = br.readLine();
			if (!dirEmail.matches("")) {
				directorEmailTF.setText(dirEmail);
			}
			teamLeadEmailTF.setText(br.readLine());
			studentEmailTF.setText(br.readLine());
			studentNameTF.setText(br.readLine());
			programTF.setText(br.readLine());
			project1 = br.readLine();
			project2 = br.readLine();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
		// Create the File Output Stream for "internalStorage"
		try {
			fos = openFileOutput("InternalStorage", Context.MODE_PRIVATE);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Set spinner adapters */
		setupSpinners();
	}

	/**
	 * When the "done" button is clicked, this method writes to the
	 * "internalStorage" file and starts up the eLabBook Activity
	 */
	public void onClick(View v) {
		if (studentNumberTF.getText().toString().matches("")
				|| studentNameTF.getText().toString().matches("")
				|| studentEmailTF.getText().toString().matches("")){
			emptyPopup();
		} else {
			FileWriter internal = null;
			try {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/enotebook/");
				f.mkdir();
				internal = new FileWriter(Environment.getExternalStorageDirectory()
						+ "/enotebook/InternalStorage.txt");
				internal.append("Internal Storage Data:\n"
						+ studentNumberTF.getText() + "\n"
						+ directorEmailTF.getText() + "\n"
						+ teamLeadEmailTF.getText() + "\n"
						+ studentEmailTF.getText() + "\n" + studentNameTF.getText()
						+ "\n" + programTF.getText() + "\n"
						+ FirstProject.getSelectedItem().toString() + "\n"
						+ SecondProject.getSelectedItem().toString() + "\n");
				internal.flush();
				internal.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finish();
		}
	}

	/*
	 * Creates a popup dialog using alertbuilder to ask the user to fill out
	 * their information in SetDefaults.txt if InternalStorage.txt does not exit
	 */
	private void emptyPopup() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.default_popup, null);
		TextView textview = (TextView) popup.findViewById(R.id.defaultPopupText);
		textview.setText("You must set at least Student ID Number, Student Name, and Student Email");
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder
				.setCancelable(true)
				.setTitle("Student Info")
				.setView(popup)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	/* Populate spinners, if teams are in the file, set spinners to those values */
	private void setupSpinners() {

		String add = "";
		BufferedReader bufferedreader = null;
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item);

		adapter.setDropDownViewResource(R.layout.spinner_item);

		/* Read in data to populate spinners */
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(teamfile)));
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

	public void onBackPressed() {
		finish();
	}
}
