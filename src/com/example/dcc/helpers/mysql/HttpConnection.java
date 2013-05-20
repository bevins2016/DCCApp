package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.example.dcc.helpers.*;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.example.dcc.helpers.hacks.DCCCookieStore;
import com.example.dcc.helpers.hacks.DCCRedirectHandler;

public class HttpConnection {

    private static final String HOST = "www.virtualdiscoverycenter.net";
    private static final String REFERER = "http://www.virtualdiscoverycenter.net/intern/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0";
    private static final String LOG = "Dcc.HttpConnection";

    //private static final String LOG = "Dcc.HttpConnection";

    public static synchronized Document getParseToXML(String page){
        try {

            User user = ObjectStorage.getUser();

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(page);

            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            get.setHeader("Cookie", user.getCookies());
            get.setHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(new HttpHost(HOST), get);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                total.append(line);

            Document doc = Jsoup.parse(total.toString());

            return doc;
        } catch (UnsupportedEncodingException e) {
            Log.e("Requesting Page", e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
            Log.e("Requesting Page", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e("Requesting Page", e.getLocalizedMessage());
        }

        return null;
    }

/*
    private static synchronized org.w3c.dom.Document getXMLfromURL(String uri){
        try{
            User user = ObjectStorage.getUser();

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(uri);
            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");


            get.setHeader("Cookie", user.getCookies());
            get.setHeader("User-Agent", USER_AGENT);

            HttpResponse response =  client.execute(new HttpHost(HOST), get);
            String xml = EntityUtils.toString(response.getEntity());
            org.w3c.dom.Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();


            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);


            return doc;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

*/

    private static synchronized HttpResponse postResponse(String page, List<NameValuePair> nvp, boolean holdFirst){

        try {

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(page);

            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            if (nvp != null) post.setEntity(new UrlEncodedFormEntity(nvp));
            post.setHeader(new BasicHeader("Referer", REFERER));
            post.setHeader("User-Agent", USER_AGENT);

            // This will stop redirects if activated
            if (holdFirst)
                client.setRedirectHandler(new DCCRedirectHandler());

            HttpResponse response = client.execute(new HttpHost(HOST), post);

            return response;

        } catch (UnsupportedEncodingException e) {
            Log.e("Requesting Page", e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
            Log.e("Requesting Page", e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Requesting Page", e.getLocalizedMessage());
        }
        Log.e("asdf", "returning null post response");
        return null;
    }

    /**
     * Authenticates the user and stores cookies.
     *
     * @param log
     * @param pwd
     * @return Returns the cookies received from the wordpress site
     * @throws URISyntaxException
     */
    public static synchronized boolean login(String log, String pwd) {

        User user = ObjectStorage.getUser();

        String page = "/wp-login.php";

        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("rememberme", "forever"));
        nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
        nvp.add(new BasicNameValuePair("log", log));
        nvp.add(new BasicNameValuePair("pwd", pwd));
        nvp.add(new BasicNameValuePair("submit", ""));

        HttpResponse response = postResponse(page, nvp, true);

        Header[] cookies = (Header[]) response.getHeaders("Set-Cookie");

        StringBuilder sb = new StringBuilder();

        // Add all the cookies to the cookie string
        for (int h = 0; h < cookies.length; h++) {
            Header c = cookies[h];
            if(c.getValue().startsWith("wordpress_test") || c.getValue().startsWith("wordpress_logged")){
                sb.append(c.getValue()+";");
            }
        }
        user.setCookie(sb.toString());
        try {
            ObjectStorage.setUser(user);

            MySQLQuery.validateUser("/DCC/validateUser.php", log);
            MySQLQuery.updateFriends("/DCC/updateFriends.php");

            Log.e("Login", user.getName()+"\n"+user.getHandle()+"\n"+user.getCookies());
            return true;
        } catch (Exception e) {
            Log.e("Error", "no clue");
            return false;
        }
    }

    public static List<User> getMembers(){
        User user = ObjectStorage.getUser();
        Document doc = getParseToXML("/members/");

        Element main = doc.getElementById("members-list");
        Elements members = main.getElementsByTag("li");

        List<User> memList = MySQLQuery.getAllMembers("/DCC/getAllUsers.php");
        return memList;
    }

    /**
     * Method used to bypass some cookie security features.
     *
     * @return
     */
    private static CookieSpecFactory getCookieSpec() {
        return new DCCCookieSpecFactory();
    }


    public static synchronized List<News> getNews(){
        List<News> newsPaper = MySQLQuery.getNews("/DCC/getNews.php");
        return newsPaper;
    }
}

