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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

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
	 */
	public synchronized CookieStore login(String log, String pwd){
		try{
			HttpClient client = new DefaultHttpClient();
			CookieStore cookieJar = new BasicCookieStore();
			
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
			
			for(int i = 0; i < response.getAllHeaders().length; i++){
				Header temp = response.getAllHeaders()[i];
				Log.i(this.getClass().getName(), temp.getName()+":"+temp.getValue());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = br.readLine();
			while(br!=null){
				Log.i(this.getClass().toString(), line);
				line = br.readLine();
			}
			Header[] cookie = (Header[]) response.getHeaders("Set-Cookie");
			for(int h=0; h<cookie.length; h++){
				Header temp = cookie[h];
				cookieJar.addCookie(new BasicClientCookie(temp.getName(), temp.getValue()));
			}

			if(cookieJar.getCookies().size() > 0 && response.getStatusLine().getStatusCode() == 302) return cookieJar;
			throw new IOException("No Cookies");
		}catch(IOException e){
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
			return null;
		}
	}
}
