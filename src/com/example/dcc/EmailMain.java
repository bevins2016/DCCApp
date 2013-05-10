package com.example.dcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmailMain extends Activity implements OnClickListener {
	private Button btnEmail = null;

	// Create buttons Globally so they are available to all methods
	Button newsB;
	Button loginB;
	Button calB;
	Button photoB;
	Button reportB;
	Button actionB;
	Button directoryB;
	Button searchB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);

		// These are the buttons on the left side of the screen.
		// The have been initialized in order.
		btnEmail = (Button) findViewById(R.id.email_button);
		newsB = (Button) findViewById(R.id.button1);
		loginB = (Button) findViewById(R.id.button2);
		calB = (Button) findViewById(R.id.button3);
		photoB = (Button) findViewById(R.id.button5);
		reportB = (Button) findViewById(R.id.button6);
		actionB = (Button) findViewById(R.id.button7);
		directoryB = (Button) findViewById(R.id.button8);
		searchB = (Button) findViewById(R.id.search);

		// Here the listener for each button that allows actions is set.
		newsB.setOnClickListener(this);
		loginB.setOnClickListener(this);
		calB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		btnEmail.setOnClickListener(this);
		searchB.setOnClickListener(this);
	}

	public void sendEmail(View v) {
		// The following code is the implementation of Email client
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("text/plain");
		String[] address = { "bevins2012@hotmail.com" };

		i.putExtra(android.content.Intent.EXTRA_EMAIL, address);
		i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Email client");
		i.putExtra(android.content.Intent.EXTRA_TEXT,
				"Android Development Tutorial to send mail from code");
		startActivityForResult((Intent.createChooser(i, "Email")), 1);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button1:
			startActivity(new Intent(this, AndroidRssReader.class));
			break;
		case R.id.button2:
			startActivity(new Intent(this, MainActivity.class));
			finish();
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
			break;
		case R.id.button7:
			startActivity(new Intent(this, ActionItem.class));
			break;
		case R.id.button8:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case R.id.email_button:
			// Calling sendEmail from the activity class
			sendEmail(v);
			break;
		case R.id.search:
			startActivity(new Intent(this, LaunchActivity.class));
			break;
		}
	}
}