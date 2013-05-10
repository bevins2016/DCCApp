package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.example.dcc.helpers.User;

public class HttpConnection{

	private static final String HOST = "www.virtualdiscoverycenter.net";
	private static final String REFERER = "http://www.virtualdiscoverycenter.net/intern/";


	/**
	 * Authenticates the user and stores cookies.
	 * @param log
	 * @param pwd
	 * @return Returns the cookies received from the wordpress site
	 * @throws URISyntaxException 
	 */
	public synchronized void login(User user, String log, String pwd){
		try{
			//Basic settings
			DefaultHttpClient client = new DefaultHttpClient();
			DCCCookieStore cookieJar = user.cookieJar;
			client.setCookieStore(cookieJar);
			
			//Used to bypass some typical security settings
			client.getCookieSpecs().register("easy", getCookieSpec());

			//Build the header
			HttpPost post = new HttpPost("/wp-login.php");
			
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("rememberme", "forever"));
			nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
			nvp.add(new BasicNameValuePair("log", log));
			nvp.add(new BasicNameValuePair("pwd", pwd));
			nvp.add(new BasicNameValuePair("submit", "\n\n"));
			
			//Add everything to the header
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.setHeader(new BasicHeader("Referer", REFERER));

			//Hack to keep from forwarding to the next page
			//This will ensure we grab the header with the cookies we need
			client.setRedirectHandler(new RedirectHandler() {
				@Override
				public boolean isRedirectRequested(HttpResponse response,
						HttpContext context) {
					return false;
				}
				@Override
				public URI getLocationURI(HttpResponse response, HttpContext context)
						throws ProtocolException {
					return null;
				}
			});

			//Send the request and parse the cookies
			HttpResponse response = client.execute(new HttpHost(HOST), post);
			Header[] cookies = (Header[]) response.getHeaders("Set-Cookie");

			//Add all the cookies to the cookie jar
			for(int h=0; h<cookies.length; h++){
				Header temp = cookies[h];
				cookieJar.addCookie(new BasicClientCookie(temp.getName(), temp.getValue()));
			}

			//Forward ourself to the intended forward site, this will enable us to get the user's data.
			HttpGet get = new HttpGet();
			get.setURI(new URI("/intern/"));
			StringBuilder sb = new StringBuilder();
			//Re-add the cookies to the request
			for(int i=0; i < cookieJar.getCookies().size(); i++){
				Cookie c = cookieJar.getCookies().get(i);
				if(c.getName().startsWith("wordpress_logged_in")||c.getName().startsWith("wordpress_test"))
				sb.append(c.getName()+"="+c.getValue()+";");
			}
			
			user.cookies = sb.toString();
			Log.i("Cookies", sb.toString()+cookieJar.getCookies().size());
			get.setHeader("Cookie", sb.toString());
			response = client.execute(new HttpHost(HOST), get);
			
			buildUser(user, response);
			//user.setCookieJar(cookieJar);
			
		}catch(IOException e){
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
		} catch (URISyntaxException e) {
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
		}
	}

	
	/**
	 * Takes an input stream and parses all relevant data from the HTML file into the user object.
	 * 
	 * @param u
	 * @param response
	 */
	private void buildUser(User user, HttpResponse response) {
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
			Log.i(this.getClass().getName(), span.text());
		} catch (IllegalStateException e) {
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
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
		//HttpResponse hr = client.execute(new HttpHost(HOST), httpGet);
	}

	/**
	 * Method used to bypass some cookie security features.
	 * @return
	 */
	private CookieSpecFactory getCookieSpec(){
		return new CookieSpecFactory() {

			@Override
			public CookieSpec newInstance(HttpParams params) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
					}
				};
			}
		};
	}
}
