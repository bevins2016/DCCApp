package com.example.dcc.helpers;

import java.io.Serializable;

import org.apache.http.client.CookieStore;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285360029686080283L;
	private String handle;
	private String name;
	private DCCCookieStore cookieJar;
	
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


	public DCCCookieStore getCookieJar() {
		return cookieJar;
	}

	public void setCookieJar(DCCCookieStore cookieJar) {
		this.cookieJar = cookieJar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
