package com.S2013.dcc.helpers;

import java.util.HashMap;
import java.util.List;

/**
 * This class is used to store static objects that are used across multiple
 * classes/fragments.By storing the objects here, they can be called by "Storage.get...()".
 *
 * @author Brandon Harmon
 */
public class Storage {

    /*The User object, stores bio and credential information*/
	private static User user = null;
    /*This is a static list of the users that is stored after the initial user list
    is downloaded*/
    private static List<User> memberList;
    /*This is a static list of the news items that is set after the page is loaded
    the first time*/
    private static List<News> newsList;
    /*This stores the users that have been created into a hashmap based on their UID.
    This is used to ensure that the user object has not yet been created in another
    area of the application*/
    private static HashMap<Integer, User> userCache = new HashMap<Integer, User>();
    /*Used to identify if the menu is hidden*/
    public static Boolean menuHidden = true;

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
        Storage.memberList = memberList;
    }

    /*Returns the List<E> of news that represents the news items*/
    public static List<News> getNewsList() {
        return newsList;
    }

    /*Sets the List<E> of news that represents the news items*/
    public static void setNewsList(List<News> newsList) {
        Storage.newsList = newsList;
    }

    /*Sets the user as the main user*/
    public static void setUser(User u){
		if(user == null) user = u;
	}

    /*Returns the main user object*/
	public static User getUser(){
		return user;
    }
}
