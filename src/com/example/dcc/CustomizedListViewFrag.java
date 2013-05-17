package com.example.dcc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CustomizedListViewFrag extends Fragment {
    ProgressDialog mProgressDialog;
    // All static variables
    static final String URL = "http://www.virtualdiscoverycenter.net/feed/"; // www.virtualdiscoverycenter.net/media/
    // http://www.virtualdiscoverycenter.net/feed/
    // XML node keys
    static final String KEY_SONG = "item"; // parent node
    static final String KEY_ID = "link";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "dc:creator";
    static final String KEY_DURATION = "description";
    static final String KEY_THUMB_URL = "src";
    String xml;
    String url;
    // The url of the website. This is just an example
    private static final String webSiteURL = "http://www.virtualdiscoverycenter.net/media/";

    LazyAdapter adapter;
    NodeList nl;
    XMLParser parser;
    ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    String src;
    Elements img;
    Elements elTest;
    Document documentO;
    org.jsoup.nodes.Document doc;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_listview,
                container, false);

        activity = getActivity();
        ListView list = (ListView) view.findViewById(R.id.list);

        adapter = new LazyAdapter(activity, songsList);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

		/* Progress dialog for download async task */
		/*
		 * mProgressDialog = new ProgressDialog(CustomizedListView.this);
		 * mProgressDialog.setMessage("Please wait while we get things ready");
		 * mProgressDialog.setIndeterminate(false); mProgressDialog.setMax(100);
		 * mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * 
		 * ArrayList<HashMap<String, String>> songsList = new
		 * ArrayList<HashMap<String, String>>();
		 * 
		 * XMLParser parser = new XMLParser(); String xml =
		 * parser.getXmlFromUrl(URL); // getting XML from URL Document doc =
		 * parser.getDomElement(xml); // getting DOM element
		 * 
		 * NodeList nl = doc.getElementsByTagName(KEY_SONG); // looping through
		 * all song nodes <song> for (int i = 0; i < nl.getLength(); i++) { //
		 * creating new HashMap HashMap<String, String> map = new
		 * HashMap<String, String>(); Element e = (Element) nl.item(i); //
		 * adding each child node to HashMap key => value map.put(KEY_ID,
		 * parser.getValue(e, KEY_ID)); map.put(KEY_TITLE, parser.getValue(e,
		 * KEY_TITLE)); map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
		 * map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
		 * map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
		 * 
		 * // adding HashList to ArrayList songsList.add(map); }
		 * 
		 * // list = (ListView) findViewById(R.id.list);
		 * 
		 * // Getting adapter by passing xml data ArrayList adapter = new
		 * LazyAdapter(this, songsList); list.setAdapter(adapter);
		 * 
		 * // Click event for single list row list.setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view, int
		 * position, long id) {
		 * 
		 * } });
		 */

        DownloadTask downloadtask = new DownloadTask();
        downloadtask.execute();
        return view;
    }

    /* Async task to download student and team lists */
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Utility utility;
        private String urlX;

        @Override
        protected String doInBackground(String... params) {
            utility = new Utility();
            try {

                parser = new XMLParser();
                String xml = parser.getXmlFromUrl(URL); // getting XML from
                // URL
                documentO = parser.getDomElement(xml); // getting DOM
                // element
                urlX = parser.getXmlFromUrl(URL); // getting XML from URL
                System.out.print(urlX);
                utility.setXml(urlX);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {

                // this is the link to the page that has the code
                // http://examples.javacodegeeks.com/enterprise-java/html/download-images-from-a-website-using-jsoup/

                // Connect to the website and get the html
                doc = Jsoup.connect(webSiteURL).get();

                // Get all elements with img tag ,
                img = doc.getElementsByTag("img");
                elTest = doc.getElementsByTag("p");

            } catch (IOException ex) {
                System.err.println("There was an error");
                Logger.getLogger(com.example.dcc.CustomizedListViewFrag.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // XMLParser parser = new XMLParser();
            // String xml = parser.getXmlFromUrl(URL); // getting XML from URL
            documentO = parser.getDomElement(utility.getXml()); // getting
            // DOM
            // element

            nl = documentO.getElementsByTagName("item");


            Elements body = doc.select("body");
            ArrayList<String> htmlPlainText = new ArrayList<String>();

            htmlPlainText.add(((org.jsoup.nodes.Document) doc).title());

            for(Iterator<org.jsoup.nodes.Element> it = body.iterator(); it.hasNext();)
            {
                org.jsoup.nodes.Element pBody = it.next();
                Elements pTag = ((org.jsoup.nodes.Element) pBody).getElementsByTag("p").parents();

                for(int pTagCount = 0; pTagCount < pTag.size(); pTagCount++)
                {
                    org.jsoup.nodes.Element p = pTag.get(pTagCount);
                    String pt = p.text();

                    if(pt.length() != 0)
                    {
                        htmlPlainText.add(pt);
                        pTagCount++;
                    }

                    pTag.parents().empty();

                }
            }


            ArrayList<String> imageList = new ArrayList<String>();
            ArrayList<String> descriptionList = new ArrayList<String>();
            String test = null;
            for (org.jsoup.nodes.Element el : img) {

                // for each element get the srs url
                src = el.absUrl("src");

                System.out.println("Image Found!");
                System.out.println("src attribute is : " + src);
                imageList.add(src);
            }
            for (org.jsoup.nodes.Element elTest : img) {

//				test = elTest.absUrl("");

                elTest.select("body > p");
                test = elTest.val();
                descriptionList.add(test);
            }

            for (int i = 0; i < nl.getLength(); i++) {
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);
                // adding each child node to HashMap key => value
                try {
                    map.put(KEY_ID, parser.getValue(e, KEY_ID));
                } catch (Exception e3) {
                }
                try {
                    map.put(KEY_TITLE, htmlPlainText.get(i+1));
                } catch (Exception e2) {

                }
                map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
                try {
                    map.put(KEY_THUMB_URL, imageList.get(i));

                } catch (Exception e1) {

                }

                // adding HashList to ArrayList
                songsList.add(map);
                adapter.notifyDataSetChanged();
            }

        }
    }

}