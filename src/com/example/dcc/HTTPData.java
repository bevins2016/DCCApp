package com.example.dcc;

import org.jsoup.nodes.Document;

import com.example.dcc.helpers.mysql.HttpConnection;

public class HTTPData {
	private String currentURL;
	Document newXML;
	
	public HTTPData(String currentURL){
		this.currentURL = currentURL;

		newXML = HttpConnection.getParseToXML(currentURL);
	}
}
