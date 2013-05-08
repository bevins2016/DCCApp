package com.example.dcc;

import java.util.ArrayList;
import java.util.HashMap;

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

public class CustomizedListView extends Activity {
	ProgressDialog mProgressDialog;
	// All static variables
	static final String URL = "http://www.virtualdiscoverycenter.net/feed/"; //www.virtualdiscoverycenter.net/media/               
	// XML node keys
	static final String KEY_SONG = "item"; // parent node
	static final String KEY_ID = "link";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "dc:creator";
	static final String KEY_DURATION = "description";
	static final String KEY_THUMB_URL = "src";

	LazyAdapter adapter;
	NodeList nl;
	// Document doc;
	// String xml;
	XMLParser parser;
	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String,String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_listview);

		ListView list = (ListView) findViewById(R.id.list);
		
		adapter = new LazyAdapter(this, songsList);
		
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
				// String xml = parser.getXmlFromUrl(URL); // getting XML from
				// URL
				// Document doc = parser.getDomElement(xml); // getting DOM
				// element
				urlX = parser.getXmlFromUrl(URL); // getting XML from URL
				utility.setXml(urlX);

			} catch (Exception e1) {
				e1.printStackTrace();
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
			Document doc = parser.getDomElement(utility.getXml()); // getting
																	// DOM
																	// element

			nl = doc.getElementsByTagName(KEY_SONG);

			

			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				map.put(KEY_ID, parser.getValue(e, KEY_ID));
				map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
				map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
				map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
				map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

				// adding HashList to ArrayList
				songsList.add(map);
				adapter.notifyDataSetChanged();
			}
		
		}
	}

}