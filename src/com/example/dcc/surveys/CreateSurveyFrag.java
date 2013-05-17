/**********************************************************************
 * Director's Command Center
 *
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 *
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.surveys;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dcc.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CreateSurveyFrag extends Fragment {

    private WebView CreateSurvey;
    Activity activity;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_survey,
                container, false);
        activity = getActivity();

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
        return view;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.create_survey, menu);
        return true;
    }

    public void onBackPressed() {
        activity.finish();
    }
}
