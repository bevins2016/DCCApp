package com.example.dcc.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.example.dcc.helpers.mysql.GetInputStreamTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Used to store data that represents users of VDC
 */
public class User implements Serializable{

    //Serial number
    private static final long serialVersionUID = -5285360029686080283L;
    //String that define the object
    private String handle,email,name,cookies,project,project2,imageURL;
    //ID number of the user
    private int ID;

    public User(){
    }

    public void setImageURL(String url){
        this.imageURL = url;
    }

    public String getCookies(){
        return cookies;
    }

    public void setCookie(String cookie){
        this.cookies = cookie;
    }
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {

        try{
            String uri = "/DCC/getUserGravitar.php?email=" + email;
            if(BitmapCache.getBitmap(uri) != null) return BitmapCache.getBitmap(uri);
            Log.e("Fetching Image ",name+"---"+uri);
            StringBuilder sb = new StringBuilder();
            InputStream in = new GetInputStreamTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri).get();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);

            String s;
            while((s = reader.readLine())!=null){
                sb.append(s);
            }
            setImageURL(sb.toString());
            in.close();


            Bitmap image =  new GetImageTask().execute().get();

            BitmapCache.addBitmap(uri, image);

            return image;

        }catch(Exception e){
            Log.e("EH", e.getLocalizedMessage());
        }
        return null;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProject2() {
        return project2;
    }

    public void setProject2(String project2) {
        this.project2 = project2;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(name);
        sb.append("<br/>").append(project);
        return sb.toString();
    }


    public class GetImageTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap image;
            try{
                URL url = new URL(imageURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
               image = BitmapFactory.decodeStream(conn.getInputStream());
                conn.disconnect();
            }catch(Exception e){
                return null;
            }
            return image;
        }
    }
}
