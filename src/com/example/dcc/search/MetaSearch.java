/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.search;

/**********************************************************************
 * MetaSearch.java
 * 
 * Universal search activity. Allows for searching by:
 * Report type, student, team, keywords, and date range
 * 
 * Defaults are entire program(start date to current date),
 * all report types(0:57's, eReports, eDailies) and no other parameters
 * 
 * Also has a button to view todays reports, currently with all report
 * types and no parameters, but could be easily changed to allow parameters
 *********************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.dcc.LaunchActivityFrag;
import utilities.KeywordUploader;
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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.example.dcc.R;

public class MetaSearch extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private Button MetaKeywordButton;
	private Button MetaSearchButton;
	private Button MetaTodayButton;

	private AbsListView keywordList;
	private AbsListView studentList;
	private AbsListView teamList;

	private ArrayAdapter<String> keywordlistadapter;
	private ArrayAdapter<String> studentlistadapter;
	private ArrayAdapter<String> teamlistadapter;
	private ArrayAdapter<String> deletekeywordadapter;

	private ArrayList<String> Keywords = new ArrayList<String>();
	private ArrayList<String> Students = new ArrayList<String>();
	private ArrayList<String> Teams = new ArrayList<String>();

	final String keywordfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/keywords.txt";
	final String teamfile = Environment.getExternalStorageDirectory().getPath()
			+ "/dcc/teamlist.txt";
	final String studentfile = Environment.getExternalStorageDirectory()
			.getPath() + "/dcc/studentlist.txt";

	private String ReportType057 = "";
	private String ReportTypeEreport = "";
	private String ReportTypeEdaily = "";
	private String SearchKeywords = "&keywords=";
	private String SearchStudents = "id=";
	private String SearchTeams = "";
	private String SearchTeams2 = "";

	private static String SearchStartDate = "&startdate=";
	private static String SearchEndDate = "&enddate=";

	public static String SearchUrl = "";

	private final String studentListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keyfile.txt";
	private final String teamListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/projects.txt";
	private final String keywordListUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keywords.txt";

	private int StudentNumber = 0;

	private EditText newWord;

	private TextView emptyList;

	private static TextView startText;
	private static TextView endText;

	private CheckBox fiftysevenCheck;
	private CheckBox ereportCheck;
	private CheckBox edailyCheck;

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metasearch);

		/* Attach variables to their layout counterparts */
		keywordList = (ListView) findViewById(R.id.metakeywordlist);
		studentList = (ListView) findViewById(R.id.metastudentlist);
		teamList = (ListView) findViewById(R.id.metateamlist);
		startText = (TextView) findViewById(R.id.startdateTextView);
		endText = (TextView) findViewById(R.id.enddateTextView);
		fiftysevenCheck = (CheckBox) findViewById(R.id.fiftysevencheckBox);
		ereportCheck = (CheckBox) findViewById(R.id.ereportcheckBox);
		edailyCheck = (CheckBox) findViewById(R.id.edailycheckBox);

		/* Set text to be shown if keyword list is empty */
		emptyList = (TextView) findViewById(R.id.metaemptykeytext);
		keywordList.setEmptyView(emptyList);

		/* Set up progress dialog for Async Task */
		mProgressDialog = new ProgressDialog(MetaSearch.this);
		mProgressDialog.setMessage("Please wait while we get things ready");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/*
		 * Create and execute Async Task to get student and projects lists from
		 * the server
		 */
		DownloadTask downloadtask = new DownloadTask();
		downloadtask.execute(studentListUrl, studentfile, teamListUrl,
				teamfile, keywordListUrl, keywordfile);

		/* Get start date and end date from LaunchActivity */
		SearchStartDate = "&startdate=" + LaunchActivityFrag.startdate;
		SearchEndDate = "&enddate=" + LaunchActivityFrag.today;
		SearchUrl = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/found.php?";

		/* Set Report type checkboxes to checked by default */
		fiftysevenCheck.setChecked(true);
		ereportCheck.setChecked(true);
		edailyCheck.setChecked(true);

		/* Set the text with date values */
		startText.setText("Current Start Date: " + LaunchActivityFrag.startdate);
		endText.setText("Current End Date: " + LaunchActivityFrag.today);

		/* Set up buttons */
		MetaKeywordButton = (Button) findViewById(R.id.meta_add_remv_key_btn);
		MetaKeywordButton.setOnClickListener(this);
		MetaSearchButton = (Button) findViewById(R.id.metasearchbtn);
		MetaSearchButton.setOnClickListener(this);
		MetaTodayButton = (Button) findViewById(R.id.meta_today_button);
		MetaTodayButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meta_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.manage_keywords:
			/* Create popup dialog to manage keywords */
			manageKeywordsPopup(false);
			return true;
		}
		return false;
	}

	
	/* Change checked state if clicked */
	public void onCheckedChanged(CompoundButton button, boolean checked) {
		if (checked) {
			if (button.getId() == R.id.ereportcheckBox)
				ereportCheck.setChecked(false);
			else if (button.getId() == R.id.fiftysevencheckBox)
				fiftysevenCheck.setChecked(false);
			else if (button.getId() == R.id.edailycheckBox)
				edailyCheck.setChecked(false);
		} else {
			if (button.getId() == R.id.ereportcheckBox)
				ereportCheck.setChecked(true);
			else if (button.getId() == R.id.fiftysevencheckBox)
				fiftysevenCheck.setChecked(true);
			else if (button.getId() == R.id.edailycheckBox)
				edailyCheck.setChecked(true);
		}

	}

	
	/* Make the Buttons work */
	public void onClick(View v) {
		Intent intent = new Intent();
		if (v.getId() == R.id.meta_add_remv_key_btn)
			keywordPopup();// start alertdialog
		else if (v.getId() == R.id.fiftysevencheckBox) {
			if (fiftysevenCheck.isChecked())
				fiftysevenCheck.setChecked(false);
			else
				fiftysevenCheck.setChecked(true);
		} else if (v.getId() == R.id.edailycheckBox) {
			if (edailyCheck.isChecked())
				edailyCheck.setChecked(false);
			else
				edailyCheck.setChecked(true);
		} else if (v.getId() == R.id.ereportcheckBox) {
			if (ereportCheck.isChecked())
				ereportCheck.setChecked(false);
			else
				ereportCheck.setChecked(true);
		} else if (v.getId() == R.id.meta_today_button) {
			/* Set dates to today and reports to all */
			/*
			 * This could easily be changed to accept parameters as
			 * metasearchbtn does below
			 */
			SearchStartDate = "&startdate=" + LaunchActivityFrag.today;
			SearchEndDate = "&enddate=" + LaunchActivityFrag.today;
			ReportTypeEreport = "&num1=1";
			ReportType057 = "&num2=3";
			ReportTypeEdaily = "&num3=5";
			SearchUrl += ReportTypeEreport + ReportType057 + ReportTypeEdaily
					+ SearchStartDate + SearchEndDate;
			intent.setClass(MetaSearch.this, SearchResults.class);
			startActivity(intent);
		} else if (v.getId() == R.id.metasearchbtn) {
			getCheckedItems();
			getReportTypes();
			/* build URL */
			SearchUrl += SearchStudents + ReportTypeEreport + ReportType057
					+ ReportTypeEdaily + SearchStartDate + SearchEndDate
					+ SearchTeams + SearchTeams2 + SearchKeywords;
			Toast.makeText(getApplicationContext(), SearchUrl,
					Toast.LENGTH_LONG).show();
			intent.setClass(MetaSearch.this, SearchResults.class);
			startActivity(intent);
			finish();
		}
	}

	/*
	 * Adds new word to the list and keywords.txt file; called from
	 * keywordPopup()
	 */
	private void addWord(String keyword) {
		String path = Environment.getExternalStorageDirectory().getPath();
		File f = new File(path + "/dcc");
		File check = new File(f + "/keywords.txt");
		if (!f.exists()) {
			f.mkdir();
		}
		if (!check.exists()) {
			try {
				check.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(check)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String empty = "";
		try {
			empty = bufferedreader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (empty == "Nothing here yet!" + "\n" + "Press Add a Keyword to add words to the list") {
			try {
				FileWriter filewriter = new FileWriter(check);
				filewriter.append(keyword);
				filewriter.append("\n");
				filewriter.flush();
				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter filewriter = new FileWriter(check, true);
			filewriter.append(keyword);
			filewriter.append("\n");
			filewriter.flush();
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		keywordlistadapter
				.remove("Nothing here yet! Press Add a Keyword to add words to the list");
		keywordlistadapter.add(keyword);
		
		/* Send keyword file to server */
		KeywordUploader keyworduploader = new KeywordUploader();
		keyworduploader.execute();
	}

	/*
	 * Creates a popup dialog using alertbuilder to add keywords to the
	 * keywords.txt file and list
	 */
	private void keywordPopup() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.keyword_popup, null);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder
				.setCancelable(true)
				.setTitle("Add a new Keyword")
				.setView(popup)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})

				.setNeutralButton("Add & Add Another",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								newWord = (EditText) popup
										.findViewById(R.id.addwordtxt);
								addWord(newWord.getText().toString());
								keywordPopup();
							}
						})
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								newWord = (EditText) popup
										.findViewById(R.id.addwordtxt);
								if (newWord.getText().toString() != "") {
									addWord(newWord.getText().toString());
								}
							}
						});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	/* Popup dialog used to manage (delete) keywords */
	private void manageKeywordsPopup(final boolean allChecked) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.manage_keywords, null);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

		final AbsListView deleteKeywords = (ListView) popup
				.findViewById(R.id.delete_keywords);

		String add = "";
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(keywordfile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		deletekeywordadapter = new ArrayAdapter<String>(this,
				R.layout.simple_checked_row, Keywords);
		deleteKeywords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		deleteKeywords.setAdapter(deletekeywordadapter);
		deletekeywordadapter.clear();
		try {
			while ((add = bufferedreader.readLine()) != null) {
				deletekeywordadapter.add(add);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (allChecked) {
			for (int i = 0; i < deleteKeywords.getCount(); i++) {
				deleteKeywords.setItemChecked(i, true);
			}
		}

		alertBuilder
				.setCancelable(true)
				.setTitle("Delete Keywords")
				.setView(popup)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
						/* 
						 * Pressing a button automatically closes the dialog,
						 * so reopen the dialog with all items checked
						 */
				.setNeutralButton("Select All",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								/* Set all items to checked */
								manageKeywordsPopup(true);
							}
						})
				.setPositiveButton("Delete Selected",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								/*
								 * Run through the list, if an item is not
								 * checked write to to the file
								 */
								SparseBooleanArray checked = deleteKeywords
										.getCheckedItemPositions();
								File f = new File(keywordfile);
								if (f.exists()) {
									f.delete();
								}
								for (int i = 0; i < deleteKeywords.getCount(); i++) {
									if (!checked.get(i)) {
										try {
											FileWriter filewriter = new FileWriter(
													keywordfile, true);
											filewriter.append(deleteKeywords
													.getItemAtPosition(i)
													.toString());
											filewriter.append("\n");
											filewriter.flush();
											filewriter.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
								if (allChecked) {
									if (f.exists()) {
										f.delete();
									}
								}
								/* update list in metasearch */
								populateList(keywordfile, "keywordID");
								/* Send updated list to server */
								KeywordUploader keyworduploader = new KeywordUploader();
								keyworduploader.execute();
							}
						});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	/* Populate lists with data from file teamlist, studentlist, and keywords */
	private void populateList(String file, String id) {
		String add = "";
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (id == "keywordID") {
			keywordlistadapter = new ArrayAdapter<String>(this,
					R.layout.simple_checked_row, Keywords);
			keywordList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			keywordList.setAdapter(keywordlistadapter);
			keywordlistadapter.clear();
		} else if (id == "studentID") {
			studentlistadapter = new ArrayAdapter<String>(this,
					R.layout.simple_checked_row, Students);
			studentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			studentList.setAdapter(studentlistadapter);
		} else if (id == "teamID") {
			teamlistadapter = new ArrayAdapter<String>(this,
					R.layout.simple_checked_row, Teams);
			teamList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			teamList.setAdapter(teamlistadapter);
		}
		if (id == "studentID") {
			try {
				/* skip first line it is \n */
				bufferedreader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f = new File(keywordfile);
		try {
			if (id == "keywordID" && f.exists()) {
				while ((add = bufferedreader.readLine()) != null) {
					if (id == "keywordID")
						keywordlistadapter.add(add);
				}
			} else if (id == "teamID" || id == "studentID") {
				while ((add = bufferedreader.readLine()) != null) {
					if (id == "studentID") {
						StudentNumber++;
						add = Integer.toString(StudentNumber) + " " + add;
						studentlistadapter.add(add);
					} else if (id == "teamID")
						teamlistadapter.add(add);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Get which report types are checked for the search */
	private void getReportTypes() {
		if (fiftysevenCheck.isChecked()) {
			ReportType057 = "&num2=3";
		} else {
			ReportType057 = "";
		}
		if (ereportCheck.isChecked()) {
			ReportTypeEreport = "&num1=1";
		} else {
			ReportTypeEreport = "";
		}
		if (edailyCheck.isChecked()) {
			ReportTypeEdaily = "&num3=5";
		} else {
			ReportTypeEdaily = "";
		}
	}

	/* Get which students/teams/keywords were selected for the search */
	private void getCheckedItems() {
		SparseBooleanArray checked = keywordList.getCheckedItemPositions();
		for (int i = 0; i < keywordList.getCount(); i++) {
			if (checked.get(i)) {
				if (SearchKeywords == "&keywords=") {
					if (((String) keywordList.getItemAtPosition(i))
							.contains(" ")) {
						String replacer = (String) keywordList
								.getItemAtPosition(i);
						replacer = replacer.replace(" ", "_");
						SearchKeywords += replacer;
					} else {
						SearchKeywords += (String) keywordList
								.getItemAtPosition(i);
					}
				} else {
					SearchKeywords += "%2C";
					if (((String) keywordList.getItemAtPosition(i))
							.contains(" ")) {
						String replacer = (String) keywordList
								.getItemAtPosition(i);
						replacer = replacer.replace(" ", "_");
						SearchKeywords += replacer;
					} else {
						SearchKeywords += (String) keywordList
								.getItemAtPosition(i);
					}
				}
			}
		}
		checked = studentList.getCheckedItemPositions();
		for (int i = 0; i < studentList.getCount(); i++) {
			if (checked.get(i)) {
				String FullLine = (String) studentList.getItemAtPosition(i);
				String[] getID = FullLine.split(" ");
				if (SearchStudents == "id=") {
					SearchStudents += getID[0];
				} else {
					SearchStudents += "%2C" + getID[0];
				}
			}
		}
		checked = teamList.getCheckedItemPositions();
		for (int i = 0; i < teamList.getCount(); i++) {
			if (checked.get(i)) {// add 2nd team, pass same team name
				SearchTeams += "&firstprojectlist%5B%5D="
						+ (String) teamList.getItemAtPosition(i);
				SearchTeams2 += "&secondprojectlist%5B%5D="
						+ (String) teamList.getItemAtPosition(i);
			}
		}
	}

	/*
	 * Method called from Change Start Date button
	 * android:onClick="showDatePickerDialog"
	 */
	public void showDatePickerDialog(View v) {
		if (v.getId() == R.id.metastartdate) {
			DialogFragment newFragment = new setStartDate();
			newFragment.show(getFragmentManager(), "datePicker");
		}
		if (v.getId() == R.id.metaenddate) {
			DialogFragment newFragment2 = new setEndDate();
			newFragment2.show(getFragmentManager(), "datePicker");
		}
	}

	/* Set up DatePicker for end date and Handle received data */
	public static class setEndDate extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			monthOfYear += 1;// month starts at 0
			SearchEndDate = "&enddate=";
			SearchEndDate += Integer.toString(year) + "-";
			if (monthOfYear < 10) {
				SearchEndDate += "0" + Integer.toString(monthOfYear) + "-";
			} else {
				SearchEndDate += Integer.toString(monthOfYear) + "-";
			}
			if (dayOfMonth < 10) {
				SearchEndDate += "0" + Integer.toString(dayOfMonth);
			} else {
				SearchEndDate += Integer.toString(dayOfMonth);
			}

			endText.setText("Current End Date: "
					+ SearchEndDate.substring(9, 19));
			Toast.makeText(getActivity(), SearchEndDate, Toast.LENGTH_SHORT)
					.show();
		}

	}

	/* Set up DatePicker for start date and Handle received data */
	public static class setStartDate extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int year = Integer.parseInt(LaunchActivityFrag.startdate
					.substring(0, 4));
			int month = Integer.parseInt(LaunchActivityFrag.startdate.substring(5,
					7)) - 1;// - 1 to conform to months starting at 0
			int day = Integer.parseInt(LaunchActivityFrag.startdate
					.substring(8, 10));

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			monthOfYear += 1;// month starts at 0
			SearchStartDate = "&startdate=";
			SearchStartDate += Integer.toString(year) + "-";
			if (monthOfYear < 10) {
				SearchStartDate += "0" + Integer.toString(monthOfYear) + "-";
			} else {
				SearchStartDate += Integer.toString(monthOfYear) + "-";
			}
			if (dayOfMonth < 10) {
				SearchStartDate += "0" + Integer.toString(dayOfMonth);
			} else {
				SearchStartDate += Integer.toString(dayOfMonth);
			}
			startText.setText("Current Start Date: "
					+ SearchStartDate.substring(11, 21));
			Toast.makeText(getActivity(), SearchStartDate, Toast.LENGTH_SHORT)
					.show();
		}

	}

	/*
	 * Async Task to download studentlist.txt and teamlist.txt, then call
	 * populateList for each list
	 */
	public class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			/* Download students list */
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
			/* Download projects list */
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
			/* Download keywords list */
			try {
				URL url = new URL(params[4]);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(params[5]);

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
			populateList(studentfile, "studentID");
			populateList(teamfile, "teamID");
			populateList(keywordfile, "keywordID");
		}

	}

	public void onBackPressed() {
		/* Send keyword file to server */
		KeywordUploader keyworduploader = new KeywordUploader();
		keyworduploader.execute();
		finish();
	}
}
