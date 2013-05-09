package com.example.dcc.helpers.mysql;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;


import android.util.Log;

public class HttpConnection{

	private static final String HOST = "www.virtualdiscoverycenter.net";

	HttpClient client;
	CookieStore cookieJar;

	public HttpConnection(){
		this.client = new DefaultHttpClient();
		this.cookieJar = new BasicCookieStore();
	}
	
	public boolean login(String log, String pwd){
		try{

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
        
		}catch(IOException e){
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
			return false;
		}
		return false;
	}
}
