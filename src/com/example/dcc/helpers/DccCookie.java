package com.example.dcc.helpers;

public class DccCookie {

//	private String name;
	private String value;
	
	public DccCookie(String value){
		//this.setName(name);
		this.setValue(value);
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
