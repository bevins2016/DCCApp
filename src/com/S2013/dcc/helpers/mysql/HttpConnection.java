package com.S2013.dcc.helpers.mysql;

import com.S2013.dcc.Utility;
import com.S2013.dcc.helpers.Storage;
import com.S2013.dcc.helpers.User;
import com.S2013.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.S2013.dcc.helpers.hacks.DCCCookieStore;
import com.S2013.dcc.helpers.hacks.DCCRedirectHandler;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app. This class should only be called by a ASYNCTASK
 */
public class HttpConnection {

    private static final String HOST = "www.virtualdiscoverycenter.net";
    /**
     * Parses the URI of a HOST Site (ie everything following the '.net') and returns a JSoup document
     * @param page url of the page to contact
     * @return Page reformated to a document standard;
     */
    public static synchronized Document getParseToXML(String page){
        try {

            User user = Storage.getUser();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(page);

            //Cookie hack that allows for lowered standards
            client.setCookieStore(new DCCCookieStore());
            client.getCookieSpecs().register("easy", getCookieSpec());
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

            //Set the headers
            get.setHeader("Cookie", user.getCookies());

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
            Utility.logError(e.getMessage());
        } catch (ClientProtocolException e) {
            Utility.logError(e.getMessage());
        } catch (IOException e) {
            Utility.logError(e.getMessage());
        }

        return null;
    }

    /**
     * This method takes in parameters and returns the uri's response of the page
     * specified in the first param
     *
     * @param page URL
     * @param nvp name value paris to insert into the header
     * @param holdFirst boolean to not accept redirects
     * @return HTTPResponse
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
            if (Storage.getUser().getCookies()!=null)post.setHeader("Cookie",
                    Storage.getUser().getCookies());

            // This will stop redirects if activated
            if (holdFirst)
                client.setRedirectHandler(new DCCRedirectHandler());

            //Execute the post and return the response
            return client.execute(new HttpHost(HOST), post);

        } catch (UnsupportedEncodingException e) {
            Utility.logError(e.getMessage());
        } catch (ClientProtocolException e) {
            Utility.logError(e.getMessage());
        } catch (IOException e) {
            Utility.logError(e.getMessage());
        }
        return null;
    }

    /**
     * Authenticates the user and stores cookies.
     *
     * @param log user name
     * @param pwd password
     * @return Returns the cookies received from the wordpress site
     */
    public static synchronized boolean login(String log, String pwd) {

        User user = Storage.getUser();

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
     * @return a easily manipulated Cookie Factory
     */
    private static CookieSpecFactory getCookieSpec() {
        return new DCCCookieSpecFactory();
    }

    /**
     * Sends the action item to the ai script in the server
     * @param results name value pairs to insert into the header
     */
    public static synchronized void sendActionItem(List<NameValuePair> results){
        postResponse("/wp-content/plugins/buddypress/bp-themes/bp-default/ai-submit.php", results, false);
    }
}

