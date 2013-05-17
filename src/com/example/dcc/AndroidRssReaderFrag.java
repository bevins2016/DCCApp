package com.example.dcc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dcc.helpers.User;

public class AndroidRssReaderFrag extends ListFragment {
    //Create buttons Globally so they are available to all methods
    public static RSSFeed myRssFeed = null;
    static WebView webView;
    private User user;
    Activity activity;
    TextView feedTitle;
    TextView feedDescribtion;
    TextView feedPubdate;
    TextView feedLink;
    TextView textEmpty;
    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_webviewer,
                container, false);
        activity = getActivity();
        feedTitle = (TextView)view.findViewById(R.id.feedtitle);
        feedDescribtion = (TextView)view.findViewById(R.id.feeddescribtion);
        feedPubdate = (TextView)view.findViewById(R.id.feedpubdate);
        feedLink = (TextView)view.findViewById(R.id.feedlink);
        textEmpty = (TextView)view.findViewById(android.R.id.empty);
        user = (User)getActivity().getIntent().getSerializableExtra("user");
        new MyTask().execute();

        //this portion of the onCreate is dedicated to running the webView
        //another webpage: http://creflodollarministries.org/
        //main webpage: http://www.virtualDiscoveryCenter.net
        webView  = (WebView) view.findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://www.virtualDiscoveryCenter.net");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Uri data = getIntent().getData(); Don't know what this is doing

        if (savedInstanceState == null) {
            String url = activity.getIntent().getDataString();
            // do something with this URL.
            webView.loadUrl(url);
        }
return view;
    }
    private class MyTask extends AsyncTask<Void, Void, Void>{
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

                feedTitle.setText(myRssFeed.getTitle());
                feedDescribtion.setText(myRssFeed.getDescription());
                feedPubdate.setText(myRssFeed.getPubdate());
                feedLink.setText(myRssFeed.getLink());

                ArrayAdapter<RSSItem> adapter =
                        new ArrayAdapter<RSSItem>(activity,
                                android.R.layout.simple_list_item_1,myRssFeed.getList());
                setListAdapter(adapter);

            }else{


                textEmpty.setText("No Feed Found!");
            }

            super.onPostExecute(result);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Uri feedUri = Uri.parse(myRssFeed.getItem(position).getLink());
        //Intent myIntent = new Intent(Intent.ACTION_VIEW, feedUri);
        //startActivity(myIntent);
        String url = myRssFeed.getItem(position).getLink();
        webView.loadUrl(url);
    }


}