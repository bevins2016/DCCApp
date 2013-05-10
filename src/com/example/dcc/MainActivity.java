//TODO change button ids to more descriptive names.

package com.example.dcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.HttpConnection;

public class MainActivity extends Activity implements OnClickListener {

	final private static int DIALOG_LOGIN = 1; 

	//Create buttons Globally so they are available to all methods
	private Button newsB;
	private Button loginB;
	private Button calB;
	private Button photoB;
	private Button reportB;
	private Button actionB;
	private Button directoryB;
	private Button searchB;

	private Context context;
	private LogInTask logTask;

	public String userText,passwordText;

	public User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//These are the buttons on the left side of the screen.
		//The have been initialized in order.
		newsB = (Button) findViewById(R.id.button1);
		loginB = (Button) findViewById(R.id.button2);
		calB = (Button) findViewById(R.id.button3);
		photoB = (Button) findViewById(R.id.button5);
		reportB = (Button) findViewById(R.id.button6);
		actionB = (Button) findViewById(R.id.button7);
		directoryB = (Button) findViewById(R.id.button8);
		searchB = (Button) findViewById(R.id.search);

		//Here the listener for each button that allows actions is set.
		newsB.setOnClickListener(this);
		loginB.setOnClickListener(this);
		calB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		searchB.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	public void onClick(View v) {
		//this switch listens for any and all click actions in the app
		//each case is a button in the menu.
		switch (v.getId()) {
		case R.id.button1:
			Intent i = new Intent(this, AndroidRssReader.class);
			i.putExtra("user", user);
			startActivity(i);
			break;
		case R.id.button2:
			showDialog(1);
			break;
		case R.id.button3:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case R.id.button5:
			startActivity(new Intent(this, CustomizedListView.class));
			break;
		case R.id.button6:
			startActivity(new Intent(this, EReportLauncher.class));
			finish();
			break;
		case R.id.search:
			startActivity(new Intent(this, LaunchActivity.class));
			finish();
			break;
		case R.id.button7:
			startActivity(new Intent(this, ActionItem.class));
			finish();
			break;
		case R.id.button8:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		}
	}

	/**
	 * This method is used to display the dialog that accepts the user's
	 * name and password and starts the authentication task.
	 */
	protected Dialog onCreateDialog(int id){
		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.dialog_login, null);
			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle("DCC Login").setView(dialogview);
			dialogDetails = dialogbuilder.create();
		}
		return dialogDetails;
	}

	/**
	 * This class is used to build the dialog box prior to being set to visible.
	 * All click listeners in the dialog is limited to this method(does not use this as
	 * the listener). Ignores the int 'id'
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {

		//Create dialog
		final AlertDialog alertDialog = (AlertDialog) dialog;
		Button loginbutton = (Button) alertDialog.findViewById(R.id.btn_login);
		Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);

		//Get input fields
		final EditText user = (EditText) alertDialog.findViewById(R.id.login_text);
		final EditText pwd = (EditText) alertDialog.findViewById(R.id.password);

		//Set the listener for the login action
		loginbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userText = user.getText().toString();
				passwordText = pwd.getText().toString();

				logTask = null;
				attemptLogin();
				alertDialog.dismiss();
			}
		});
		
		//Set the lisener for the cancel action
		cancelbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (logTask != null) {
			return;
		}

		boolean cancel = false;
		@SuppressWarnings("unused")
		View focusView = null;

		try{
			// Check for a valid password.
			if (TextUtils.isEmpty(passwordText)) {
				Toast.makeText(context, "Password is Empty", Toast.LENGTH_LONG);
				cancel = true;
			} else if (passwordText.length() < 4) {
				Toast.makeText(context, "Password is to short", Toast.LENGTH_LONG);
				cancel = true;
			}

			// Check for a valid email address.
			if (TextUtils.isEmpty(userText)) {
				Toast.makeText(context, "User Name is Empty", Toast.LENGTH_LONG);;
				cancel = true;
			}

		}catch(Exception e){
			cancel = true;
		}
		
		//If the task has successfully passed the inspection move to login task
		if (!cancel){
			logTask = new LogInTask();
			logTask.execute((Void) null);
		}
	}


	public class LogInTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			HttpConnection hc = new HttpConnection();
			try {
				//This logs the user in.
				hc.login(user, userText, passwordText);
				//hc.getFriends(user.getCookieJar());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
		}

		@Override
		protected void onCancelled() {
		}
	}

	/*
	 *
	 *
	 *
	 *
	 * newsB loginB calB mailB photoB reportB actionB
	 */
	/*
	 * 
	 * 
	 * 
	 * 
	 * newsB loginB calB mailB photoB reportB actionB
	 */

}
