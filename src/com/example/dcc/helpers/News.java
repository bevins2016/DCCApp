package com.example.dcc.helpers;

import android.util.Log;

public class News {

	private String title;
	private String link;
	private String comments;
	private String pubdate;
	private String text;
	private String publisher;
	private String category;
	
	public News(){
		this.title = null;
		this.link = null;
		this.comments = null;
		this.pubdate = null;
		this.text = null;
		this.publisher = null;
		this.category = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public void logNews(){
		StringBuilder sb = new StringBuilder();
		sb.append("\nTitle: "+title);
		sb.append("\nLink: "+link);
		sb.append("\nComments: "+comments);
		sb.append("\nDate: "+pubdate);
		sb.append("\nPublisher: "+publisher);
		sb.append("\nCategory"+category);
		sb.append("\nText"+text);
		
		Log.e("News: ", sb.toString());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<b><u>"+title+"</u></b><br/>");
		sb.append("Date: "+pubdate.substring(0, pubdate.indexOf("+")-1)+"&nbsp&nbsp&nbsp&nbsp");
		sb.append("<b>--By</b>"+publisher);
        if(text.length()>140) sb.append("<br/>"+text.substring(0,139));
		else sb.append("<br/>"+text);
		
		return sb.toString();
	}
}
