package com.example.dcc.helpers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.example.dcc.R;
import com.example.dcc.fragment.NewsDetailFragment;

import java.io.Serializable;

/**
 * This represents a news or post item which is pulled from the VDC site.
 *
 * @author Brandon Harmon
 */
public class News implements Serializable{

    /*Title of the article*/
	private String title;
    /*Link to the article*/
	private String link;
    /*Link to the comments*/
	private String comments;
    /*Date of publication*/
	private String pubdate;
    /*Message in the article*/
	private String text;
    /*User that created the article*/
	private String publisher;
    /*Category of the article*/
	private String category;
    /*ID of the article*/
	private String id;

    /**
     * Constructor of the News object.
     */
	public News(){
		this.title = null;
		this.link = null;
		this.comments = null;
		this.pubdate = null;
		this.text = null;
		this.publisher = null;
		this.category = null;
        this.id = null;
	}

    /*Returns the title of the article*/
	public String getTitle() {
		return title;
	}

    /*Sets the title of the article*/
	public void setTitle(String title) {
		this.title = title;
	}

    /*Returns the link to the article*/
	public String getLink() {
		return link;
	}

    /*Sets the link to the article*/
	public void setLink(String link) {
		this.link = link;
	}

    /*Returns the comments link of this article*/
	public String getComments() {
		return comments;
	}

    /*Sets the comments link of this article*/
	public void setComments(String comments) {
		this.comments = comments;
	}

    /*Returns the publication date/time of this article*/
	public String getPubdate() {
		return pubdate;
	}

    /*Sets the publication date/time of this article*/
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

    /*Returns the message inside of this article*/
	public String getText() {
		return text;
	}

    /*Sets the message inside of this article*/
	public void setText(String text) {
		this.text = text;
	}

    /*Returns the name of the author*/
	public String getPublisher() {
		return publisher;
	}

    /*Sets the name of the author*/
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

    /*Returns the category of the article*/
	public String getCategory() {
		return category;
	}

    /*Sets the category of the article*/
	public void setCategory(String category) {
		this.category = category;
	}

    /*Sends details about this object to the logging statement*/
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

    /*Prints pertinent details aobut the article(In HTML format)*/
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<b><u>"+title+"</u></b><br/>");
		sb.append("<b>Date:</b> "+pubdate+"&nbsp&nbsp&nbsp&nbsp");
		sb.append("<b>By:</b> "+publisher);
        if(text.length()>140) sb.append("<br/>"+text.substring(0,139));
		else sb.append("<br/>"+text);
		
		return sb.toString();
	}

    /*Sets the id of the article*/
    public void setID(String id) {
        this.id = id;
    }

    /*Gets the id of the article*/
    public String getID(){
        return id;
    }
}

