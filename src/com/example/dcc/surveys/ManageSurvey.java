/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.surveys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dcc.LaunchActivityFrag;
import com.example.dcc.R;

@SuppressLint("SetJavaScriptEnabled")
public class ManageSurvey extends Activity {
	
	private WebView ManageSurvey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_survey);

		/* Assign the WebView to the corresponding layout item */
		ManageSurvey = (WebView) findViewById(R.id.managesurvey);
		
		/* 
		 * WebViewClient prevents the website from trying to 
		 * open a page in a browser application, instead of within the app
		 */
		ManageSurvey.setWebViewClient(new WebViewClient());
		
		/* JavaScript is required for this website, so we enable it */
		ManageSurvey.getSettings().setJavaScriptEnabled(true);
		
		/* Loads the website */
		ManageSurvey.loadUrl("http://discoverylabsurvey.appspot.com");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_survey, menu);
		return true;
	}
	
	/* Intent is here because otherwise onBackPressed exits the app */
	public void onBackPressed() {
		Intent intent = new Intent(ManageSurvey.this, LaunchActivityFrag.class);
		finish();
		startActivity(intent);
	}
}