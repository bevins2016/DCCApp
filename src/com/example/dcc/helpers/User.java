package com.example.dcc.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import com.example.dcc.helpers.hacks.DCCCookieStore;

import android.graphics.Bitmap;
import android.util.Log;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285360029686080283L;
	private static final String LOG = "User Object";
	private String handle;
	private String name;
	public String cookies;
	private Bitmap image;
	public List<DccCookie> cook;

	public User(){
		this.name = "";
		this.handle = "";
		this.cookies = "";
		this.image = null;
		this.cook = new LinkedList<DccCookie>();
	}
	
	public void displayData(){
		Log.i(LOG, name);
		Log.i(LOG, handle);
		Log.i(LOG, cookies);
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL(Links link){
		String base = "/members/"+handle+"/";
		switch(link){
		case PROFILE:
			return "/intern/";
		case MESSAGES:
			return base+"messages/";
		case MEDIA:
			return base+"media/";
		case FRIENDS:
			return base+"friends/";
		case GROUPS:
			return base+"groups/";
		case MEMBERS:
			return "/members/";
		case SETTINGS:
			return "/settings/";
		default:
			return "/intern/";
		}
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}
