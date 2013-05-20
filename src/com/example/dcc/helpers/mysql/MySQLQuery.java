package com.example.dcc.helpers.mysql;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dcc.helpers.Member;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public synchronized static List<News> getNews(String url){
        JSONArray jNews = (JSONArray)getArray(url);
        List<News> newsList = new ArrayList<News>();

        for(int i = 0; i < jNews.length(); i++){
            try {
                JSONObject temp = jNews.getJSONObject(i);
                News n = new News();

                int authorID = Integer.parseInt(temp.getString("post_author"));
                User u;
                if(ObjectStorage.hasUser(authorID)){
                    u = ObjectStorage.getUser(authorID);
                }else{
                    u = convertUser ((JSONObject)getArray("/DCC/getUserById.php?id="+authorID));
                    ObjectStorage.setUser(authorID, u);
                }
                n.setPublisher(u.getName());
                n.setPubdate(temp.getString("post_date"));
                n.setTitle(temp.getString("post_title"));
                n.setText(temp.getString("post_content"));
                n.setID(temp.getString("ID"));
                newsList.add(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        //Need to reverse the list for correct view
        Collections.reverse(newsList);

        return newsList;
    }

    private synchronized static User convertUser(JSONObject jUser){
        User u = new User();
        return u;
    }

    public synchronized static User validateUser(String url, String login){

        JSONObject jUser = null;
        jUser = (JSONObject)getArray(url);

        User user = ObjectStorage.getUser();

        try{
            user.setName(jUser.getString("display_name"));
            user.setEmail(jUser.getString("user_email"));
            user.setPhone(jUser.getString("phone"));
            user.setHandle(jUser.getString("user_login"));
            user.setProject(jUser.getString("project"));
            user.setProject2(jUser.getString("project2"));

            StringBuilder sb = new StringBuilder();
            InputStream in = getInputStream("/DCC/getGravitar.php");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);
            String s;
            while((s = reader.readLine())!=null){
                sb.append(s);
                Log.e("IMG", s);
            }
            user.setImage(sb.toString());
        }catch(Exception e){

        }

        return null;
    }

    private synchronized static Object getArray(String url){
        String result = "";
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
            result=sb.toString();

            Log.e("User", result);
            return new JSONObject(result);
        }catch(Exception e){
            Log.e("getArray", "Error fetching data from: "+url+"//"+e.getMessage());
            try {
                return new JSONArray(result);
            } catch (JSONException e1) {
                return null;
            }
        }
    }

    public synchronized static List<User> getAllMembers(String url){
        List<User> lMembers = new ArrayList<User>();
        JSONArray jMembers = (JSONArray)getArray(url);

        for(int i = 0 ; i < jMembers.length(); i++){
            try {
                JSONObject jMember = jMembers.getJSONObject(i);
                User m = new User();
                m = convertUser(jMember);
                int uid = Integer.parseInt(jMember.getString("ID"));
                if(!ObjectStorage.hasUser(uid)){
                    ObjectStorage.setUser(uid, m);
                    lMembers.add(m);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lMembers;
    }
}
