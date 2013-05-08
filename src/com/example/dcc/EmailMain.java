package com.example.dcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmailMain extends Activity implements OnClickListener {
	private Button btnEmail = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		
		btnEmail = (Button) findViewById(R.id.email_button);
		
		btnEmail.setOnClickListener(this);
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
	    if (v == btnEmail) {
	        //Calling sendEmail  from the activity class
	        sendEmail(v);
	    }
	}
}