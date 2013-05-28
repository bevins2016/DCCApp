package com.example.dcc.helpers;

import java.io.Serializable;
//TODO: delete, replace with user
public class Friend implements Serializable{
	private String imgURL;
	private String page;
	private String name;
	private String handle;
	
	public Friend(){
		this.imgURL = null;
		this.page = null;
		this.name = null;
		this.handle = null;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
}
