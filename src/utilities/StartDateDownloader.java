/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package utilities;

/**********************************************************************
 * StartDateDownloader.java
 * 
 * Gets the start date of the program from the server
 *********************************************************************/

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StartDateDownloader extends AsyncTask<String, Void, String> {

	String info = "";

	@Override
	protected String doInBackground(String... params) {
		HttpGet httpget = new HttpGet(
				"http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/startdate.txt");
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpEntity Entity = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Entity = response.getEntity();
			info = EntityUtils.toString(Entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

}
