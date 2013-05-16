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
	private Button newsB;
	private Button loginB;
	private Button calB;
	private Button photoB;
	private Button reportB;
	private Button actionB;
	private Button directoryB;
	private Button searchB;

	String currentItem;
	TextView thisAction;
	int currentLocation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_item);


		newsB = (Button) findViewById(R.id.news);
		calB = (Button) findViewById(R.id.calendar);
		photoB = (Button) findViewById(R.id.photo);
		reportB = (Button) findViewById(R.id.report);
		actionB = (Button) findViewById(R.id.action);
		directoryB = (Button) findViewById(R.id.directory);
		thisAction = (TextView) findViewById(R.id.actionView);
		searchB = (Button) findViewById(R.id.search);

		newsB.setOnClickListener(this);
		calB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		searchB.setOnClickListener(this);

		new MyTask().execute();

		// this portion of the onCreate is dedicated to running the webView
		// another webpage: http://creflodollarministries.org/
		// main webpage: http://www.virtualDiscoveryCenter.net
		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://www.virtualDiscoveryCenter.net");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		//Uri data = getIntent().getData();

		if (savedInstanceState == null) {
			String url = getIntent().getDataString();
			// do something with this URL.
			webView.loadUrl(url);
		}

	}
	
	
// use Async task to make network calls to avoid NetworkOnMainThreadException
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				URL rssUrl = new URL(//set URL
						"http://www.virtualdiscoverycenter.net/feed/");// http://www.virtualdiscoverycenter.net/feed/
				//instantiate network objects and parsers
				SAXParserFactory mySAXParserFactory = SAXParserFactory
						.newInstance();
				SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
				XMLReader myXMLReader = mySAXParser.getXMLReader();
				RSSHandler myRSSHandler = new RSSHandler();
				myXMLReader.setContentHandler(myRSSHandler);
				InputSource myInputSource = new InputSource(rssUrl.openStream());
				myXMLReader.parse(myInputSource);

				myRssFeed = myRSSHandler.getFeed();
				//catch potential exceptions
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				//return null because a String return is required but uneeded
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (myRssFeed != null) {
				//instanciate the textviews in the scrollview
				TextView feedTitle = (TextView) findViewById(R.id.feedtitle);
				TextView feedDescribtion = (TextView) findViewById(R.id.feeddescribtion);
				TextView feedPubdate = (TextView) findViewById(R.id.feedpubdate);
				TextView feedLink = (TextView) findViewById(R.id.feedlink);
				//set the textviews to the info on the Rss feed located in RSSFeed
				feedTitle.setText(myRssFeed.getTitle());
				feedDescribtion.setText(myRssFeed.getDescription());
				feedPubdate.setText(myRssFeed.getPubdate());
				feedLink.setText(myRssFeed.getLink());

				//adapt the listview to the array.
				ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1,
						myRssFeed.getList());
				setListAdapter(adapter);

				thisAction.setText(myRssFeed.getItem(0).getTitle());

			} else {

				//if the server is down or Rss is completely empty
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
		//this will change what happens when you pick and item on the list
		String url = myRssFeed.getItem(position).getLink();
		currentItem = myRssFeed.getItem(position).getTitle();
		currentLocation = position;
		thisAction.setText(currentItem);
		//loads the url into the webview of the selcted Rss item
		webView.loadUrl(url);
	}
	
	public void sendEmail(View v) {
		// The following code is the implementation of Email client
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("text/plain");
		//email to send to 
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
		case R.id.news:
			Intent i = new Intent(this, AndroidRssReader.class);
			startActivity(i);
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
		case R.id.action:
			startActivity(new Intent(this, ActionItem.class));
			finish();
			break;
		case R.id.directory:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case R.id.email_button1:
			// Calling sendEmail from the activity class
			sendEmail(v);
			break;
		case R.id.search:
			startActivity(new Intent(this, LaunchActivity.class));
			finish();
			break;
		}
	}

	

}