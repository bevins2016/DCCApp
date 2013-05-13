package com.example.dcc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.example.dcc.helpers.User;
import com.example.dcc.helpers.hacks.DCCCookieStore;

public class HTTPData {
	private String currentURL;
	
	public HTTPData(String currentURL, User user){
//		this.currentURL = currentURL;
//		
//		//Basic settings
//		DefaultHttpClient client = new DefaultHttpClient();
//		DCCCookieStore cookieJar = user.cookieJar;
//		client.setCookieStore(cookieJar);
//		
//		//Used to bypass some typical security settings
//		client.getCookieSpecs().register("easy", getCookieSpec());
//
//
//
//		//Send the request and parse the cookies
//		HttpResponse response = client.execute(new HttpHost(HOST), post);
//		Header[] cookies = (Header[]) response.getHeaders("Set-Cookie");
//
//		//Add all the cookies to the cookie jar
//		for(int h=0; h<cookies.length; h++){
//			Header temp = cookies[h];
//			cookieJar.addCookie(new BasicClientCookie(temp.getName(), temp.getValue()));
//		}
//
//		//Forward ourself to the intended forward site, this will enable us to get the user's data.
//		HttpGet get = new HttpGet();
//		get.setURI(new URI("/intern/"));
//		StringBuilder sb = new StringBuilder();
//		//Re-add the cookies to the request
//		for(int i=0; i < cookieJar.getCookies().size(); i++){
//			Cookie c = cookieJar.getCookies().get(i);
//			if(c.getName().startsWith("wordpress_logged_in")||c.getName().startsWith("wordpress_test"))
//			sb.append(c.getName()+"="+c.getValue()+";");
//		}
//		
//		user.cookies = sb.toString();
//		Log.i("Cookies", sb.toString()+cookieJar.getCookies().size());
//		get.setHeader("Cookie", sb.toString());
//		response = client.execute(new HttpHost(HOST), get);
//		
//		buildUser(user, response);	
//		
//		return true;
//	}catch(IOException e){
//		Log.e(LOG, e.getLocalizedMessage());
//	} catch (URISyntaxException e) {
//		Log.e(LOG, e.getLocalizedMessage());
//	}

	}
}
