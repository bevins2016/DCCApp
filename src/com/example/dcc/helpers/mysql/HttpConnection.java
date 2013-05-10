package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all http connections. It will do this through static methods in order to increase
 * protability between classes within this app.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.dcc.helpers.User;

import android.util.Log;
import android.util.Xml.Encoding;

public class HttpConnection{

	private static final String HOST = "www.virtualdiscoverycenter.net";
	private static final String REFERER = "http://www.virtualdiscoverycenter.net/intern/";


	/**
	 * Authenticates the user and returns 
	 * @param log
	 * @param pwd
	 * @return
	 * @throws URISyntaxException 
	 */
	public synchronized CookieStore login(User u, String log, String pwd) throws URISyntaxException{
		try{
			DefaultHttpClient client = new DefaultHttpClient();
			CookieStore cookieJar = new BasicCookieStore();
			client.setCookieStore(cookieJar);
			client.getCookieSpecs().register("easy", getCookieSpec());

			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("rememberme", "forever"));
			nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
			nvp.add(new BasicNameValuePair("log", log));
			nvp.add(new BasicNameValuePair("pwd", pwd));
			//nvp.add(new BasicNameValuePair("submit", ""));

			
			HttpPost post = new HttpPost("/wp-login.php");
			post.setEntity(new UrlEncodedFormEntity(nvp));
			post.setHeader(new BasicHeader("Referer", REFERER));
			
			BasicHttpContext bhc = new BasicHttpContext();
			bhc.setAttribute(ClientContext.COOKIE_STORE, cookieJar);

			HttpResponse response = client.execute(new HttpHost(HOST), post);
			
			buildUser(u, response);
			Header[] cookie = (Header[]) response.getHeaders("Set-Cookie");
			for(int h=0; h<cookie.length; h++){
				Header temp = cookie[h];
				cookieJar.addCookie(new BasicClientCookie(temp.getName(), temp.getValue()));
			}

			
			return cookieJar;
		}catch(IOException e){
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
			return null;
		}
	}
	
	private void buildUser(User u, HttpResponse response) {
		try {
			//Get the site
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while((line = in.readLine()) != null) total.append(line);

			Document doc = Jsoup.parse(total.toString());
			Element span = doc.getElementById("ffl-logged-in-user");
			u.setName(span.text());
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getFriends(CookieStore cj) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("/members/brandoncharmon/friends/");
		StringBuilder cookies = new StringBuilder();
		for(Cookie c : cj.getCookies()){
			cookies.append(c.getName()+"="+c.getValue()+";");
		}
		httpGet.setHeader("Cookie",cookies.toString());
		HttpResponse hr = client.execute(new HttpHost(HOST), httpGet);
	}
	
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
