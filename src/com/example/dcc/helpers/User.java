package com.example.dcc.helpers;

import org.apache.http.client.CookieStore;

public class User {

	private String handle;
	private String name;
	private CookieStore cookieJar;
	
	public User(){
		this.setHandle(null);
		this.setName(null);
		this.setCookieJar(null);
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}


	public CookieStore getCookieJar() {
		return cookieJar;
	}

	public void setCookieJar(CookieStore cookieJar) {
		this.cookieJar = cookieJar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
