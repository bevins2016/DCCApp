package com.example.dcc;

import android.app.Activity;
import android.os.Bundle;

public class WebViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			String url = getIntent().getDataString();
			// do something with this URL.
			AndroidRssReader.webView.loadUrl(url);
		}
	}
}
