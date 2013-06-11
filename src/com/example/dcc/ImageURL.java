package com.example.dcc;

import com.example.dcc.helpers.mysql.HttpConnection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ImageURL {
	
	public String url;

	public ImageURL(String page) {
		url = page;
	}
	
	public String getURLForImage(){
		Document document = HttpConnection.getParseToXML(url);
		Element image = document.select("img").first();
		return url = image.absUrl("src");
	}
}
