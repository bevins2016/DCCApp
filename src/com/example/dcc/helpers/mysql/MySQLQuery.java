package com.example.dcc.helpers.mysql;

import android.os.AsyncTask;
import android.util.Log;
import com.example.dcc.helpers.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Harmon on 5/17/13.
 */
public class MySQLQuery {
    private synchronized static InputStream getInputStream(String url){
        try{
            return new GetInputStreamTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url).get();
        }catch(Exception e){
            e.printStackTrace();
            Log.e("MySQLConnection", url);
            return null;
        }
    }

    public synchronized static void updateFriends(String url){
        try{
            StringBuilder sb = new StringBuilder();
            InputStream is = getInputStream(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb.append(reader.readLine() + "\n");

            String line="";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            String result=sb.toString();

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++){
                JSONObject json_data = jArray.getJSONObject(i);
            }

        }catch(Exception e){

        }
    }

    public synchronized static User validateUser(String url, String login){
        try{
            StringBuilder sb = new StringBuilder();
            InputStream is = getInputStream(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb.append(reader.readLine() + "\n");

            String line="";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            Log.e("asdfas", sb.toString());
            is.close();
            String result=sb.toString();

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++){
                JSONObject json_data = jArray.getJSONObject(i);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
