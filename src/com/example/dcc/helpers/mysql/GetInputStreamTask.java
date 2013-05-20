package com.example.dcc.helpers.mysql;

import java.io.InputStream;

import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.example.dcc.helpers.hacks.DCCCookieStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Harmon on 5/17/13.
 */
public class GetInputStreamTask extends AsyncTask<String, Void, InputStream>{

    private static final String HOST = "www.virtualdiscoverycenter.net";

    @Override
    public InputStream doInBackground(String... url) {
        User user = ObjectStorage.getUser();
        try{
            Log.i("getis", url[0]);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url[0]);

            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            httpPost.setHeader("Cookie", user.getCookies());
           // httpPost.setHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(new HttpHost(HOST), httpPost);
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Method used to bypass some cookie security features.
     *
     * @return
     */
    private static CookieSpecFactory getCookieSpec() {
        return new DCCCookieSpecFactory();
    }
}