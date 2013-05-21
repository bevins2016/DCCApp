package com.example.dcc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
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

public class ActionItemFrag extends ListFragment implements OnClickListener {
    // Create buttons Globally so they are available to all methods
    public static RSSFeed myRssFeed = null;
    private Button btnEmail = null;
    static WebView webView;

    String currentItem;
    TextView thisAction;
    int currentLocation;

    Activity activity;

    View view;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.action_item_fragment,
                container, false);
        activity = getActivity();

        btnEmail = (Button) view.findViewById(R.id.email_button1);
        btnEmail.setOnClickListener(this);
        thisAction = (TextView) view.findViewById(R.id.actionView);

        new MyTask().execute();

        // this portion of the onCreate is dedicated to running the webView
        // another webpage: http://creflodollarministries.org/
        // main webpage: http://www.virtualDiscoveryCenter.net
        webView = (WebView) view.findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.virtualDiscoveryCenter.net");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Uri data = getIntent().getData();

        if (savedInstanceState == null) {
            String url = activity.getIntent().getDataString();
            // do something with this URL.
            webView.loadUrl(url);
        }
        return view;
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
                TextView feedTitle = (TextView) view.findViewById(R.id.feedtitle);
                TextView feedDescribtion = (TextView) view.findViewById(R.id.feeddescribtion);
                TextView feedPubdate = (TextView) view.findViewById(R.id.feedpubdate);
                TextView feedLink = (TextView) view.findViewById(R.id.feedlink);
                //set the textviews to the info on the Rss feed located in RSSFeed
                feedTitle.setText(myRssFeed.getTitle());
                feedDescribtion.setText(myRssFeed.getDescription());
                feedPubdate.setText(myRssFeed.getPubdate());
                feedLink.setText(myRssFeed.getLink());

                //adapt the listview to the array.
                ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(
                        activity,
                        android.R.layout.simple_list_item_1,
                        myRssFeed.getList());
                setListAdapter(adapter);

                thisAction.setText(myRssFeed.getItem(0).getTitle());

            } else {

                //if the server is down or Rss is completely empty
                TextView textEmpty = (TextView) view.findViewById(android.R.id.empty);
                textEmpty.setText("No Feed Found!");
            }

            super.onPostExecute(result);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

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
            case R.id.email_button1:
                // Calling sendEmail from the activity class
                sendEmail(v);
                break;
        }
    }



}