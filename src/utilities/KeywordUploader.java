/**********************************************************************
 * Director's Command Center
 * 
 * @author Chris Crowell <crowelch@mail.uc.edu>
 * @version 1.0
 * 
 * YATE Spring 2013
 *********************************************************************/

package utilities;

/******************************
 * KeywordUploader.java
 * 
 * Uploads keywords.txt to the server
 * 
 * Uploads the contents of keywords.txt to:
 * storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keywords.txt
 * This is accomplished through and HTTP GET request using comma
 * Separated values (csv)
 *****************************/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Environment;

public class KeywordUploader extends AsyncTask<String, Void, String> {

	String info = "";
	int count = 0;

	@Override
	protected String doInBackground(String... params) {
		
		String url = "http://storage.virtualdiscoverycenter.net/projectmorpheus/dcc/keywords.php?keywords=";
		String word = "";
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Environment.getExternalStorageDirectory()
							.getPath() + "/dcc/keywords.txt")));
			while ((word = bufferedreader.readLine()) != null) {
				/* replace spaces with pluses */
				if(word.contains(" ")) {
					word = word.replace(" ", "+");
				}
				/* append to url */
				if(count == 0) {
					url += word;
				} else {
					url += "," + word;
				}
				count++;
			}
			HttpGet httpget = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = null;
			HttpEntity Entity = null;
			response = httpclient.execute(httpget);
			Entity = response.getEntity();
			info = EntityUtils.toString(Entity);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
