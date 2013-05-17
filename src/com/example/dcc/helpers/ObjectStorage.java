package com.example.dcc.helpers;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectStorage {

	private static User user = null;
    private static HashMap<Integer, Fragment> fragment;
    private static List<String> memberActivity = new ArrayList<String>();

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


    public static List<String> getMemberActivity() {
        return memberActivity;
    }

    public static void setMemberActivity(List<String> memberActivity) {
        ObjectStorage.memberActivity = memberActivity;
    }

}
