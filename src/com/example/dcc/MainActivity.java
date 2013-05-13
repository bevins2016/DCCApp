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
import android.widget.TextView;
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

		user = (User)this.getIntent().getSerializableExtra("user");
		
		//These are the buttons on the left side of the screen.
		//The have been initialized in order.
		newsB = (Button) findViewById(R.id.news);
		calB = (Button) findViewById(R.id.calendar);
		photoB = (Button) findViewById(R.id.photo);
		reportB = (Button) findViewById(R.id.report);
		actionB = (Button) findViewById(R.id.action);
		directoryB = (Button) findViewById(R.id.directory);
		searchB = (Button) findViewById(R.id.search);
		//Here the listener for each button that allows actions is set.
		newsB.setOnClickListener(this);
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
		case R.id.news:
			Intent i = new Intent(this, AndroidRssReader.class);
			i.putExtra("user", user);
			startActivity(i);
			break;
		case R.id.calendar:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case R.id.photo:
			startActivity(new Intent(this, CustomizedListView.class));
			break;
		case R.id.report:
			startActivity(new Intent(this, EReportLauncher.class));
			finish();
			break;
		case R.id.search:
			startActivity(new Intent(this, LaunchActivity.class));
			finish();
			break;
		case R.id.action:
			startActivity(new Intent(this, ActionItem.class));
			finish();
			break;
		case R.id.directory:
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
