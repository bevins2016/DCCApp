package com.example.dcc.helpers;

public class ObjectStorage {

	public static User user = null;

	public static void setUser(User u){
		if(user == null) user = u;
	}

	public static User getUser(){
		return user;
	}
}
