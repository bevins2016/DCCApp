package com.example.dcc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ActionItem extends ListActivity implements OnClickListener {
	// Create buttons Globally so they are available to all methods
	public static RSSFeed myRssFeed = null;
	private Button btnEmail = null;
	static WebView webView;
	Button newsB;
	Button loginB;
	Button calB;
	Button mailB;
	Button photoB;
	Button reportB;
	Button actionB;
	Button directoryB;

	String currentItem;
	TextView thisAction;
	int currentLocation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_item);

		btnEmail = (Button) findViewById(R.id.email_button1);
		newsB = (Button) findViewById(R.id.button1);
		loginB = (Button) findViewById(R.id.button2);
		calB = (Button) findViewById(R.id.button3);
		mailB = (Button) findViewById(R.id.button4);
		photoB = (Button) findViewById(R.id.button5);
		reportB = (Button) findViewById(R.id.button6);
		actionB = (Button) findViewById(R.id.button7);
		directoryB = (Button) findViewById(R.id.button8);
		thisAction = (TextView) findViewById(R.id.actionView);

		newsB.setOnClickListener(this);
		loginB.setOnClickListener(this);
		calB.setOnClickListener(this);
		mailB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		btnEmail.setOnClickListener(this);

		new MyTask().execute();

		// this portion of the onCreate is dedicated to running the webView
		// another webpage: http://creflodollarministries.org/
		// main webpage: http://www.virtualDiscoveryCenter.net
		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://www.virtualDiscoveryCenter.net");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		Uri data = getIntent().getData();

		if (savedInstanceState == null) {
			String url = getIntent().getDataString();
			// do something with this URL.
			webView.loadUrl(url);
		}

	}

	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				URL rssUrl = new URL(
						"http://www.virtualdiscoverycenter.net/feed/");// http://www.virtualdiscoverycenter.net/feed/
				SAXParserFactory mySAXParserFactory = SAXParserFactory
						.newInstance();
				SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
				XMLReader myXMLReader = mySAXParser.getXMLReader();
				RSSHandler myRSSHandler = new RSSHandler();
				myXMLReader.setContentHandler(myRSSHandler);
				InputSource myInputSource = new InputSource(rssUrl.openStream());
				myXMLReader.parse(myInputSource);

				myRssFeed = myRSSHandler.getFeed();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (myRssFeed != null) {
				TextView feedTitle = (TextView) findViewById(R.id.feedtitle);
				TextView feedDescribtion = (TextView) findViewById(R.id.feeddescribtion);
				TextView feedPubdate = (TextView) findViewById(R.id.feedpubdate);
				TextView feedLink = (TextView) findViewById(R.id.feedlink);
				feedTitle.setText(myRssFeed.getTitle());
				feedDescribtion.setText(myRssFeed.getDescription());
				feedPubdate.setText(myRssFeed.getPubdate());
				feedLink.setText(myRssFeed.getLink());

				ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1,
						myRssFeed.getList());
				setListAdapter(adapter);

				thisAction.setText(myRssFeed.getItem(0).getTitle());

			} else {

				TextView textEmpty = (TextView) findViewById(android.R.id.empty);
				textEmpty.setText("No Feed Found!");
			}

			super.onPostExecute(result);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// Uri feedUri = Uri.parse(myRssFeed.getItem(position).getLink());
		// Intent myIntent = new Intent(Intent.ACTION_VIEW, feedUri);
		// startActivity(myIntent);
		String url = myRssFeed.getItem(position).getLink();
		currentItem = myRssFeed.getItem(position).getTitle();
		currentLocation = position;
		thisAction.setText(currentItem);
		webView.loadUrl(url);
	}
	
	public void sendEmail(View v) {
		// The following code is the implementation of Email client
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("text/plain");
		String[] address = { "bevins2012@hotmail.com" };

		i.putExtra(android.content.Intent.EXTRA_EMAIL, address);
		i.putExtra(android.content.Intent.EXTRA_SUBJECT, myRssFeed.getItem(currentLocation).getDescription());
		i.putExtra(android.content.Intent.EXTRA_TEXT,
				"Action Item from DCC app.");
		startActivityForResult((Intent.createChooser(i, "Email")), 1);
	}

	public void onClick(View v) {
		// this switch listens for any and all click actions in the app
		// each case is a button in the menu.
		switch (v.getId()) {
		case R.id.button1:
			startActivity(new Intent(this, AndroidRssReader.class));
			break;
		case R.id.button2:
			startActivity(new Intent(this, MainActivity.class));
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
			startActivity(new Intent(this, ActionItem.class));
			break;
		case R.id.button8:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case R.id.email_button1:
			// Calling sendEmail from the activity class
			sendEmail(v);
			break;
		}
	}

	

}