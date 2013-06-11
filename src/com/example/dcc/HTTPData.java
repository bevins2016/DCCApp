package com.example.dcc;

import com.example.dcc.helpers.mysql.HttpConnection;

import org.jsoup.nodes.Document;


public class HTTPData {
	private String currentURL;
	Document newXML;
	
	public HTTPData(String currentURL){
		this.currentURL = currentURL;

		newXML = HttpConnection.getParseToXML(currentURL);
	}
}
