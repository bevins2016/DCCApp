package com.example.dcc.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.util.Log;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285360029686080283L;
	private String handle;
	private String name;
	public DCCCookieStore cookieJar;
	public String cookies;
	
	public User(){
		this.name = null;
		this.cookieJar = new DCCCookieStore();
		this.handle = null;
		this.cookies = null;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}


	public DCCCookieStore getCookieJar() {
		Log.e("Cookie", cookieJar.getCookies().size()+"");
		return cookieJar;
	}

	public void setCookieJar(DCCCookieStore cj) {
		Log.e("Cookie", "Setting it "+cj.getCookies().size());
		List<Cookie> al = cj.getCookies();
		for(Cookie c: al){
			cookieJar.addCookie(c);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
