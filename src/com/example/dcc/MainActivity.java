//TODO change button ids to more descriptive names.

package com.example.dcc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;

public class MainActivity extends Activity implements OnClickListener {

	//Create buttons Globally so they are available to all methods
	private Button newsB;
	private Button calB;
	private Button photoB;
	private Button reportB;
	private Button actionB;
	private Button directoryB;
	private Button searchB;
	
	private ImageView userIcon;

	private Context context;

	public String userText;
	public String passwordText;

	public User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = ObjectStorage.getUser();
		
		//These are the buttons on the left side of the screen.
		//The have been initialized in order.
		newsB = (Button) findViewById(R.id.news);
		calB = (Button) findViewById(R.id.calendar);
		photoB = (Button) findViewById(R.id.photo);
		reportB = (Button) findViewById(R.id.report);
		actionB = (Button) findViewById(R.id.action);
		directoryB = (Button) findViewById(R.id.directory);
		searchB = (Button) findViewById(R.id.search);
		userIcon = (ImageView) findViewById(R.id.usericon);
		
		//Here the listener for each button that allows actions is set.
		newsB.setOnClickListener(this);
		calB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		searchB.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		//this switch listens for any and all click actions in the app
		//each case is a button in the menu.
		switch (v.getId()) {
		case R.id.news:
			startActivity(new Intent(this, AndroidRssReader.class));
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
			break;
		case R.id.directory:
			startActivity(new Intent(this, MainActivity.class));
			finish();
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
	/*
	 * 
	 * 
	 * 
	 * 
	 * newsB loginB calB mailB photoB reportB actionB
	 */

}
