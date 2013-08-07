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
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dcc.R;

public class CreateSurvey extends Activity {

	private WebView CreateSurvey;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CreateSurvey = new WebView(this);
		setContentView(CreateSurvey);

		/* JavaScript is required for this website, so we enable it */
		CreateSurvey.getSettings().setJavaScriptEnabled(true);
		
		/* 
		 * WebViewClient prevents the website from trying to 
		 * open a page in a browser application, instead of within the app
		 */
		CreateSurvey.setWebViewClient(new WebViewClient());

		/* JavaScript is required for this website, so we enable it */
		CreateSurvey.setWebChromeClient(new WebChromeClient());
		CreateSurvey.getSettings().getJavaScriptCanOpenWindowsAutomatically();
		CreateSurvey.getSettings().getBuiltInZoomControls();
		CreateSurvey.getSettings().getDisplayZoomControls();
		CreateSurvey.getSettings().getSavePassword();
		
		CreateSurvey.loadUrl("http://build.opendatakit.org");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_survey, menu);
		return true;
	}
	
	public void onBackPressed() {
		finish();
	}
}
