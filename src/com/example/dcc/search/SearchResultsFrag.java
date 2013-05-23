/**********************************************************************
 * Director's Command Center
 *
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 *
 * YATE Spring 2013
 *********************************************************************/

package com.example.dcc.search;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dcc.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.dcc.helpers.ObjectStorage;
import org.xmlpull.v1.XmlPullParser;

public class SearchResultsFrag extends Fragment {
    private WebView Results;
    private String website = "";
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        Results = new WebView(activity);
        View view = inflater.inflate((XmlPullParser) Results,
                container, false);
//        getActivity().setContentView(Results);
        website = MetaSearch.SearchUrl;
		
		/* 
		 * WebViewClient prevents the website from trying to 
		 * open a page in a browser application, instead of within the app
		 */
        Results.setWebViewClient(new WebViewClient());

		/* JavaScript is required for this website, so we enable it */
        Results.setWebChromeClient(new WebChromeClient());

        Results.loadUrl(website);
        return view;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.temp_search, menu);
        return true;
    }

    public void onBackPressed() {
//        Intent intent = new Intent(activity, MetaSearch.class);
//        activity.finish();
//        startActivity(intent);
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        Fragment newer = new MetaSearchFrag();
        ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

        transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
        transaction.commit();

    }
}
