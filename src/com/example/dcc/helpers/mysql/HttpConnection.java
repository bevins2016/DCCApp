package com.example.dcc.helpers.mysql;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import android.util.Log;

public class HttpConnection{

	private static final String HOST = "www.virtualdiscoverycenter.net";

	public synchronized CookieStore login(String log, String pwd){
		try{

			HttpClient client = new DefaultHttpClient();
			CookieStore cookieJar = new BasicCookieStore();
			
			HttpParams hp = new BasicHttpParams();
			hp.setParameter("rememberme", "forever");
			hp.setParameter("redirect_to", "%2Fintern%2F");
			hp.setParameter("log", log);
			hp.setParameter("pwd", pwd);

			HttpGet get = new HttpGet(HOST);
			get.setParams(hp);

			BasicHttpContext bhc = new BasicHttpContext();
			bhc.setAttribute(ClientContext.COOKIE_STORE, cookieJar);

			HttpResponse response = client.execute(get, bhc);

			Header[] cookie = (Header[]) response.getHeaders("Set-Cookie");
			for(int h=0; h<cookie.length; h++){
				cookieJar.addCookie(new BasicClientCookie(cookie[h].getName(), cookie[h].getValue()));
			}

			if(cookieJar.getCookies().size() > 0) return cookieJar;
			else return null;
			
		}catch(IOException e){
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
			return null;
		}
	}
}
