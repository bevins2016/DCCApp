package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app. This class should only be called by a ASYNCTASK
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.util.Log;

import com.example.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.example.dcc.helpers.hacks.DCCCookieStore;
import com.example.dcc.helpers.hacks.DCCRedirectHandler;

public class HttpConnection {

    private static final String HOST = "www.virtualdiscoverycenter.net";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0";
    private static final String LOG = "dcc.HttpConn";
    /**
     * Parses the URI of a HOST Site (ie everything following the '.net') and returns a JSoup document
     * @param page
     * @return
     */
    public static synchronized Document getParseToXML(String page){
        try {

            User user = ObjectStorage.getUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(page);

            //Cookie hack that allows for lowered standards
            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            //Set the headers
            get.setHeader("Cookie", user.getCookies());
            get.setHeader("User-Agent", USER_AGENT);

            //Execute the request and get back the response
            HttpResponse response = client.execute(new HttpHost(HOST), get);

            //Parse the response into JSoup
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                total.append(line);
            return Jsoup.parse(total.toString());
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG, e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
            Log.e(LOG, e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(LOG, e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * This method takes in parameters and returns the uri's response of the page
     * specified in the first param
     *
     * @param page
     * @param nvp
     * @param holdFirst
     * @return
     */
    public static synchronized HttpResponse postResponse(String page,
                                                          List<NameValuePair> nvp,
                                                          boolean holdFirst){

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(page);

            //Lower the standards of the cookie
            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            //Set Name Value Pairs if needed
            if (nvp != null) post.setEntity(new UrlEncodedFormEntity(nvp));

            //Set headers (Cookies are set if they are not null)
            post.setHeader("User-Agent", USER_AGENT);
            if (ObjectStorage.getUser().getCookies()!=null)post.setHeader("Cookie",
                    ObjectStorage.getUser().getCookies());

            // This will stop redirects if activated
            if (holdFirst)
                client.setRedirectHandler(new DCCRedirectHandler());

            //Execute the post and return the response
            return client.execute(new HttpHost(HOST), post);

        } catch (UnsupportedEncodingException e) {
            Log.e(LOG, e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
            Log.e(LOG, e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(LOG, e.getLocalizedMessage());
        }
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

        //URL of the login verification site
        String page = "/wp-login.php";

        //Set the name value pairs needed for authentication
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("rememberme", "forever"));
        nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
        nvp.add(new BasicNameValuePair("log", log));
        nvp.add(new BasicNameValuePair("pwd", pwd));
        nvp.add(new BasicNameValuePair("submit", ""));

        //Post the response (Needs to stop the transaction after the first response
        //in order to grab the cookies.
        HttpResponse response = postResponse(page, nvp, true);

        //Get the cookies from the response header
        Header[] cookies = response.getHeaders("Set-Cookie");


        // Add all the cookies to the cookie string
        StringBuilder sb = new StringBuilder();
        for (Header c : cookies) {
            if (c.getValue().startsWith("wordpress_test") || c.getValue().startsWith("wordpress_logged"))
                sb.append(c.getValue()).append(";");
        }

        //Set the current user's cookie as the string.
        user.setCookie(sb.toString());

        //Get more data for the user
        MySQLQuery.validateUser("/DCC/validateUser.php");
        return true;
    }

    /**
     * Method used to bypass some cookie security features.
     *
     * @return
     */
    private static CookieSpecFactory getCookieSpec() {
        return new DCCCookieSpecFactory();
    }

    /**
     * Sends the action item to the ai script in the server
     * @param results
     */
    public static synchronized void sendActionItem(List<NameValuePair> results){
        postResponse("/wp-content/plugins/buddypress/bp-themes/bp-default/ai-submit.php", results, false);
    }
}

