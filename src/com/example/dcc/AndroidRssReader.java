package com.example.dcc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dcc.helpers.User;

public class AndroidRssReader extends ListActivity{
	//Create buttons Globally so they are available to all methods
	public static RSSFeed myRssFeed = null;
	static WebView webView;
	private User user;
	private Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_webviewer);
		context = this;
		user = (User)getIntent().getSerializableExtra("user");
		new MyTask().execute();

		//this portion of the onCreate is dedicated to running the webView
		//another webpage: http://creflodollarministries.org/
		//main webpage: http://www.virtualDiscoveryCenter.net
		webView  = (WebView) findViewById(R.id.webView1);

		webView.setWebViewClient(new WebViewClient());
		Map<String, String> m = new HashMap<String, String>();
		m.put("Cookie", user.cookies);
		Log.i("cookiesasd", user.cookies);

		webView.loadUrl("http://www.virtualDiscoveryCenter.net", m);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		//Uri data = getIntent().getData(); Don't know what this is doing

		if (savedInstanceState == null) {
			String url = getIntent().getDataString();
			// do something with this URL.
			webView.loadUrl(url);
		}

	}
	private class MyTask extends AsyncTask<Void, Void, Void>{


		//CookieManager cm;
		//String cookie;
		
		protected void onPreExecute(){
			/*CookieSyncManager.createInstance(context);
			CookieStore cs = user.getCookieJar();
			
			for(int i=0; i < cs.getCookies().size(); i++){
				Cookie c = cs.getCookies().get(i);
				String cookie = c.getName()+"="+c.getValue()+"; domain="+c.getDomain()+"; path="+c.getPath();
				cm.setCookie(c.getDomain(), cookie);
				CookieSyncManager.getInstance().sync();
			}*/
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				URL rssUrl = new URL("http://www.virtualdiscoverycenter.net/feed/");//http://www.virtualdiscoverycenter.net/feed/
				SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
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
			if (myRssFeed!=null)
			{
				TextView feedTitle = (TextView)findViewById(R.id.feedtitle);
				TextView feedDescribtion = (TextView)findViewById(R.id.feeddescribtion);
				TextView feedPubdate = (TextView)findViewById(R.id.feedpubdate);
				TextView feedLink = (TextView)findViewById(R.id.feedlink);
				feedTitle.setText(myRssFeed.getTitle());
				feedDescribtion.setText(myRssFeed.getDescription());
				feedPubdate.setText(myRssFeed.getPubdate());
				feedLink.setText(myRssFeed.getLink());

				ArrayAdapter<RSSItem> adapter =
						new ArrayAdapter<RSSItem>(getApplicationContext(),
								android.R.layout.simple_list_item_1,myRssFeed.getList());
				setListAdapter(adapter);	

			}else{

				TextView textEmpty = (TextView)findViewById(android.R.id.empty);
				textEmpty.setText("No Feed Found!");
			}

			super.onPostExecute(result);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//Uri feedUri = Uri.parse(myRssFeed.getItem(position).getLink());
		//Intent myIntent = new Intent(Intent.ACTION_VIEW, feedUri);
		//startActivity(myIntent);
		String url = myRssFeed.getItem(position).getLink();
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("Cookie", user.cookies);
		Log.i("cookies", user.cookies);
		
		webView.loadUrl(url, m);
	}


}