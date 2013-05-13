package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

import com.example.dcc.helpers.DCCCookieStore;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.example.dcc.helpers.hacks.DCCRedirectHandler;

public class HttpConnection{

	private static final String HOST = "www.virtualdiscoverycenter.net";
	private static final String REFERER = "http://www.virtualdiscoverycenter.net/intern/";
	private static final String LOG = "Dcc.HttpConnection";


	private static synchronized Document getParseToXML(String page){
		try {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(page);
			get.setHeader("Cookie", ObjectStorage.getUser().cookies);
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
						
			HttpResponse response =  client.execute(new HttpHost(HOST), get);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while((line = in.readLine()) != null) total.append(line);
			
			
			Document doc = Jsoup.parse(total.toString());
			
			
			return doc;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	private static synchronized Document postParseToXML(String page, List<NameValuePair> nvp){
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(page);
			post.setHeader("Cookie",ObjectStorage.getUser().cookies);
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
						
			HttpResponse response =  client.execute(new HttpHost(HOST), post);
			
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.setHeader(new BasicHeader("Referer", REFERER));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while((line = in.readLine()) != null) total.append(line);
			
			Document doc = Jsoup.parse(total.toString());
			
			return doc;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * Authenticates the user and stores cookies.
	 * @param log
	 * @param pwd
	 * @return Returns the cookies received from the wordpress site
	 * @throws URISyntaxException 
	 */
	public static synchronized boolean login(User user, String log, String pwd){
		try{
			//Basic settings
			DefaultHttpClient client = new DefaultHttpClient();

			//Build the header
			HttpPost post = new HttpPost("/wp-login.php");

			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("rememberme", "forever"));
			nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
			nvp.add(new BasicNameValuePair("log", log));
			nvp.add(new BasicNameValuePair("pwd", pwd));
			nvp.add(new BasicNameValuePair("submit", ""));

			//Add everything to the header
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.setHeader(new BasicHeader("Referer", REFERER));

			//Hack to keep from forwarding to the next page
			//This will ensure we grab the header with the cookies we need
			client.setRedirectHandler(new DCCRedirectHandler());

			//Send the request and parse the cookies
			HttpResponse response = client.execute(new HttpHost(HOST), post);
			Header[] cookies = (Header[]) response.getHeaders("Set-Cookie");

			StringBuilder sb = new StringBuilder();
			//Add all the cookies to the cookie jar
			for(int h=0; h<cookies.length; h++){
				Header c = cookies[h];
				if(c.getName().startsWith("wordpress_logged_in")||
						c.getName().startsWith("wordpress_test")){
					sb.append(c.getName()+"="+c.getValue()+";");
				}
			}

			//Forward ourself to the intended forward site, this will enable us to get the user's data.
			HttpGet get = new HttpGet();
			get.setURI(new URI("/intern/"));
			
			user.cookies = sb.toString();
			
			get.setHeader("Cookie", sb.toString());
			response = client.execute(new HttpHost(HOST), get);

			buildUser(user, response);	

			return true;
		}catch(IOException e){
			Log.e(LOG, e.getLocalizedMessage());
		} catch (URISyntaxException e) {
			Log.e(LOG, e.getLocalizedMessage());
		}

		return false;
	}


	/**
	 * Takes an input stream and parses all relevant data from the HTML file into the user object.
	 * 
	 * @param u
	 * @param response
	 */
	private static synchronized void buildUser(User user, HttpResponse response) {
		try {
			//Get the site from an input stream
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while((line = in.readLine()) != null) total.append(line);

			//Look for the span that contains user's name
			Document doc = Jsoup.parse(total.toString());
			Element span = doc.getElementById("ffl-logged-in-user");

			user.setName(span.text());
		} catch (IllegalStateException e) {
			Log.e(LOG, e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(LOG, e.getLocalizedMessage());
		}

	}

	/**
	 * Used to gather a list of friends of the user
	 * @param cookieStore
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getFriends(CookieStore cookieStore) throws ClientProtocolException, IOException{
		@SuppressWarnings("unused")
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("/members/brandoncharmon/friends/");
		StringBuilder cookies = new StringBuilder();
		for(Cookie c : cookieStore.getCookies()){
			cookies.append(c.getName()+"="+c.getValue()+";");
		}
		httpGet.setHeader("Cookie",cookies.toString());
	}

	/**
	 * Method used to bypass some cookie security features.
	 * @return
	 */
	private static CookieSpecFactory getCookieSpec(){
		return new DCCCookieSpecFactory();
	}
}
