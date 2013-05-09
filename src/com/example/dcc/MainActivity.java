package com.example.dcc;

import com.example.dcc.helpers.mysql.PreparedStatements;
import com.example.dcc.helpers.mysql.PreparedStatements.SQL_COMMANDS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	final private static int DIALOG_LOGIN = 1; 
	
	//Create buttons Globally so they are available to all methods
	private Button newsB;
	private Button loginB;
	private Button calB;
	private Button mailB;
	private Button photoB;
	private Button reportB;
	private Button actionB;
	private Button directoryB;
	
	private Context context;
	private PreparedStatements sqlStatement;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;
		sqlStatement = new PreparedStatements(this.openOrCreateDatabase("dcc_settings", MODE_PRIVATE, null));
		sqlStatement.runStatement(SQL_COMMANDS.createTable);
		
		//These are the buttons on the left side of the screen.
		//The have been initialized in order.
		newsB = (Button) findViewById(R.id.button1);
		loginB = (Button) findViewById(R.id.button2);
		calB = (Button) findViewById(R.id.button3);
		mailB = (Button) findViewById(R.id.button4);
		photoB = (Button) findViewById(R.id.button5);
		reportB = (Button) findViewById(R.id.button6);
		actionB = (Button) findViewById(R.id.button7);
		directoryB = (Button) findViewById(R.id.button8);

		//Here the listener for each button that allows actions is set.
		newsB.setOnClickListener(this);
		loginB.setOnClickListener(this);
		calB.setOnClickListener(this);
		mailB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
	}

	public void onClick(View v) {
		//this switch listens for any and all click actions in the app
		//each case is a button in the menu.
		switch (v.getId()) {
		case R.id.button1:
			startActivity(new Intent(this, AndroidRssReader.class));
			break;
		case R.id.button2:
			showDialog(DIALOG_LOGIN, null);
			break;
		case R.id.button3:
			startActivity(new Intent(this, MainActivity.class));
			break;
		case R.id.button4:
			startActivity(new Intent(this, EmailMain.class));
			break;
		case R.id.button5:
			startActivity(new Intent(this, CustomizedListView.class));
			break;
		case R.id.button6:
			startActivity(new Intent(this, LaunchActivity.class));
			break;
		case R.id.button7:
			startActivity(new Intent(this, MainActivity.class));
			break;
		case R.id.button8:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		}
	}

	protected Dialog onCreateDialog(int id){
		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.dialog_login, null);

			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle("Login");
			dialogbuilder.setView(dialogview);
			dialogDetails = dialogbuilder.create();

			break;
		}

		return dialogDetails;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case DIALOG_LOGIN:
			final AlertDialog alertDialog = (AlertDialog) dialog;
			Button loginbutton = (Button) alertDialog
					.findViewById(R.id.btn_login);
			Button cancelbutton = (Button) alertDialog
					.findViewById(R.id.btn_cancel);
			final EditText userName = (EditText) alertDialog
					.findViewById(R.id.login_text);
			final EditText password = (EditText) alertDialog
					.findViewById(R.id.password);

			loginbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					Toast.makeText(
							context,
							"User Name : " + userName.getText().toString()
							+ "  Password : "
							+ password.getText().toString(),
							Toast.LENGTH_LONG).show();
				}
			});

			cancelbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		}
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * newsB loginB calB mailB photoB reportB actionB
	 */

}
