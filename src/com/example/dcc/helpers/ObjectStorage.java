package com.example.dcc.helpers;

import android.app.Fragment;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * This class is used to store static objects that are used across multiple
 * classes/fragments.By storing the objects here, they can be called by "ObjectStorage.get...()".
 *
 * @author Brandon Harmon
 */
public class ObjectStorage {

    /*The User object, stores bio and credential information*/
	private static User user = null;
    /*Used to store user objects by their uid number, which is uniquely assigned
    to each user during registration*/
    private static HashMap<Integer, Fragment> fragment;
    /*This is a static list of the users that is stored after the initial user list
    is downloaded*/
    private static List<User> memberList;
    /*This is a static list of the news items that is set after the page is loaded
    the first time*/
    private static List<News> newsList;
    /*This is a static list of the action items that is set after the page is
    loaded for the first time*/
    private static List<ActionItem> actionItems;
    /*This stores the users that have been created into a hashmap based on their UID.
    This is used to ensure that the user object has not yet been created in another
    area of the application*/
    private static HashMap<Integer, User> userCache = new HashMap<Integer, User>();
    /*Used to identify if the menu is hidden*/
    public static Boolean menuHidden = true;
    /*This is used to pass the navigation bar between fragments in order to alter the
    appearance based on clickss*/
    private static View menuFrame;

    /*sets the action items*/
    public static void setActionItems(List<ActionItem> ais){
        actionItems = ais;
    }

    /*Returns a List<E> of Action Item objects*/
    public static List<ActionItem> getActionItems(){
        return actionItems;
    }

    /*Returns a view that represents the navigation menu*/
    public static View getMenuFrame() {
        return menuFrame;
    }

    /*Sets a view as the navigation menu*/
    public static void setMenuFrame(View menu) {
        menuFrame = menu;
    }

    /*Returns the user based on the ID number parameter*/
    public static User getUser(int user){
        return userCache.get(user);
    }

    /*Returns true if the user exists inside of the table*/
    public static Boolean hasUser(int user){
        return userCache.containsKey(user);
    }

    /*Sets the user object as the main user*/
    public static void setUser(int id, User user){
        userCache.put(id, user);
    }

    /*Returns a List<E> of users that represent the directory of members*/
    public static List<User> getMemberList() {
        return memberList;
    }

    /*Sets the List<E> of users that represent the directory of members*/
    public static void setMemberList(List<User> memberList) {
        ObjectStorage.memberList = memberList;
    }

    /*Returns the List<E> of news that represents the news items*/
    public static List<News> getNewsList() {
        return newsList;
    }

    /*Sets the List<E> of news that represents the news items*/
    public static void setNewsList(List<News> newsList) {
        ObjectStorage.newsList = newsList;
    }

    /*Sets the user as the main user*/
    public static void setUser(User u){
		if(user == null) user = u;
	}

    /*Returns the main user object*/
	public static User getUser(){
		return user;
	}

    /*Set the hashmap for fragments*/
    public static void setHashMap(HashMap<Integer, Fragment> hm){
        if(fragment == null) fragment = hm;
    }

    /*Returns the hashmap of fragments*/
    public static HashMap<Integer, Fragment> getHashMap(){
        return fragment;
    }

    /*Returns the fragment inside of the hashmap identified by the key*/
    public static Fragment getFragment(int key){
        return fragment.get(key);
    }

    /*Sets a fragment inside of the hashmap by the key*/
    public static void setFragment(int key, Fragment frag){
        fragment.put(key,frag);
    }
}
