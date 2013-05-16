package com.example.dcc.helpers;

import android.app.Fragment;

import java.util.HashMap;

public class ObjectStorage {

	private static User user = null;
    private static HashMap<Integer, Fragment> fragment;

    public static void setUser(User u){
		if(user == null) user = u;
	}

	public static User getUser(){
		return user;
	}

    public static void setHashMap(HashMap<Integer, Fragment> hm){
        if(fragment == null) fragment = hm;
    }

    public static HashMap<Integer, Fragment> getHashMap(){
        return fragment;
    }

    public static Fragment getFragment(int key){
        return fragment.get(key);
    }

    public static void setFragment(int key, Fragment frag){
        fragment.put(key,frag);
    }
}
