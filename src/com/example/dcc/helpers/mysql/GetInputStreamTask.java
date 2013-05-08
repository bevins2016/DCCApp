package com.example.dcc.helpers.mysql;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class GetInputStreamTask extends AsyncTask<String, Void, InputStream>{

	@Override
	public InputStream doInBackground(String... url) {
		Log.i("Remindo-getis", "howdy");
		try{
			Log.i("Remindo-getis", url[0]);
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url[0]);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return entity.getContent();
		}catch(Exception e){
			return null;
		}
	}
	
	
}
