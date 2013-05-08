package com.example.dcc.helpers.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.dcc.helpers.User;

import android.os.AsyncTask;
import android.util.Log;

public class MySQLConnection {

	private static final String MYSQL_URL = "http://virtualdiscoverycenter.net/app/";
	
	/**
	 * Enumerate the tasks
	 * @author Harmon
	 *
	 */
	private static enum task{
		//Add any new tasks here
		GET_USERS("something.php");
		
		private final String address;
		task(String address){
			this.address = MYSQL_URL+address;
		}
		
		public String toString(){
			return address;
		}
	}
	
	/**
	 * Gets the input stream from a separate task.
	 * @param url
	 * @return
	 */
	private synchronized static InputStream getInputStream(String url){
		try{
			return new GetInputStreamTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url).get();
		}catch(Exception e){
			Log.e("MySQLConnection Failed", e.getLocalizedMessage()+"\n"+url);
			return null;
		}
	}

	/**
	 * Returns a list of users
	 * @return
	 */
	public synchronized static ArrayList<User> getUsers() {
		/*String result="";
		ArrayList<String> temp = new ArrayList<String>();
		try{
			StringBuilder sb = new StringBuilder();
			String url = task.GET_USERS.address;
			InputStream is = getInputStream(url);

			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line="";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result=sb.toString();
			if(result.startsWith("null")) return temp;
			JSONArray jArray = new JSONArray(result);
			for(int i=0; i<jArray.length(); i++){
				JSONObject json_data = jArray.getJSONObject(i);
				temp.add(json_data.getString("FriendID"));
				Log.e("Added", json_data.getString("FriendID"));
			}
			return temp;
		}catch(Exception e){
			Log.e("Get Friends", "Error parsing data "+e.toString()+" "+result);
			return null;
		}*/
		return null;
	}
	
}
