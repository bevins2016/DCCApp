/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.search;

import com.example.dcc.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SearchResults extends Activity {
	private WebView Results;
	private String website = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Results = new WebView(this);
		setContentView(Results);
		website = MetaSearch.SearchUrl;
		
		/* 
		 * WebViewClient prevents the website from trying to 
		 * open a page in a browser application, instead of within the app
		 */
		Results.setWebViewClient(new WebViewClient());

		/* JavaScript is required for this website, so we enable it */
		Results.setWebChromeClient(new WebChromeClient());
		
		Results.loadUrl(website);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.temp_search, menu);
		return true;
	}

	public void onBackPressed() {
		Intent intent = new Intent(SearchResults.this, MetaSearch.class);
		finish();
		startActivity(intent);
	}
}
