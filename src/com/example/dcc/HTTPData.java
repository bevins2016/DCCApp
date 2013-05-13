package com.example.dcc;

import org.jsoup.nodes.Document;

<<<<<<< HEAD
import com.example.dcc.helpers.mysql.HttpConnection;
=======
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
>>>>>>> 9d58e4d63de3012ddb2c4c585a1fce626e77294d

public class HTTPData {
	private String currentURL;
	Document newXML;
	
	public HTTPData(String currentURL){
		this.currentURL = currentURL;

		newXML = HttpConnection.getParseToXML(currentURL);
	}
}
