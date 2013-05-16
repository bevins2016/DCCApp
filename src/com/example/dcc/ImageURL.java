package com.example.dcc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.dcc.helpers.mysql.HttpConnection;

public class ImageURL {
	
	public String url;

	public ImageURL(String page) {
		url = page;
	}
	
	public String getURLForImage(){
		Document document = HttpConnection.getParseToXML(null, url);
		Element image = document.select("img").first();
		return url = image.absUrl("src");
	}
}
