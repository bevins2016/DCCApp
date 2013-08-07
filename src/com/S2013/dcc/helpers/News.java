package com.S2013.dcc.helpers;

import java.io.Serializable;

/**
 * This represents a news or post item which is pulled from the VDC site.
 *
 * @author Brandon Harmon
 */
public class News implements Serializable{

    /*Title of the article*/
	private String title;
    /*Date of publication*/
	private String pubdate;
    /*Message in the article*/
	private String text;
    /*User that created the article*/
	private User publisher;

    /**
     * Constructor of the News object.
     */
	public News(){
		this.title = null;

		this.pubdate = null;
		this.text = null;
		this.publisher = null;
	}

    /*Returns the title of the article*/
	public String getTitle() {
		return title;
	}

    /*Sets the title of the article*/
	public void setTitle(String title) {
		this.title = title;
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
	public User getPublisher() {
		return publisher;
	}

    /*Sets the name of the author*/
	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

    /*Prints pertinent details aobut the article(In HTML format)*/
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<b><u>").append(title).append("</u></b><br/>");
		sb.append("<b>Date:</b> ").append(pubdate).append("&nbsp&nbsp&nbsp&nbsp");
		sb.append("<b>By:</b> ").append(publisher);
        if(text.length()>140) sb.append("<br/>").append(text.substring(0, 139));
		else sb.append("<br/>").append(text);
		
		return sb.toString();
	}
}

