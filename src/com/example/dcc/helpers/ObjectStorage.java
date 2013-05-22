package com.example.dcc.helpers;

import android.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectStorage {

	private static User user = null;
    private static HashMap<Integer, Fragment> fragment;
    private static List<String> memberActivity = new ArrayList<String>();
    private static List<User> memberList;
    private static List<News> newsList;
    private static HashMap<Integer, User> userCache = new HashMap<Integer, User>();
    public static Boolean menuHidden = true;
    private static View menuFrame;

    public static View getMenuFrame() {
        return menuFrame;
    }

    public static void setMenuFrame(View menu) {
        menuFrame = menu;
    }




    public static User getUser(int user){
        return userCache.get(user);
    }

    public static Boolean hasUser(int user){
        return userCache.containsKey(user);
    }

    public static void setUser(int id, User user){
        userCache.put(id, user);
    }
    public static List<User> getMemberList() {
        return memberList;
    }

    public static void setMemberList(List<User> memberList) {
        ObjectStorage.memberList = memberList;
    }

    public static List<News> getNewsList() {
        return newsList;
    }

    public static void setNewsList(List<News> newsList) {
        ObjectStorage.newsList = newsList;
    }



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
