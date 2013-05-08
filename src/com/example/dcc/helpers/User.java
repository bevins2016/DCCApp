package com.example.dcc.helpers;

public class User {

	private String handle;
	private String email;
	private String firstName;
	private String lastName;
	
	public User(){
		this.setHandle(null);
		this.setEmail(null);
		this.setFirstName(null);
		this.setLastName(null);
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
