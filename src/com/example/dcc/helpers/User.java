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
	private List<Friend> friends;


	public User(){
		this.name = "";
		this.cookies = "";
		this.image = null;
		this.friends = new ArrayList<Friend>();
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
		String url = "";
		switch(link){
		case PROFILE:
			url = "/intern/"; break;
		case MESSAGES:
			url = base+"messages/"; break;
		case MEDIA:
			url = base+"media/"; break;
		case FRIENDS:
			url = base+"friends/"; break;
		case GROUPS:
			url = base+"groups/"; break;
		case MEMBERS:
			url = "/members/"; break;
		case SETTINGS:
			url = "/settings/"; break;
		default:
			url = "/intern/"; break;
		}
		Log.e("Link", url);
		return url;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void addFriend(Friend f) {
		friends.add(f);
	}
}
