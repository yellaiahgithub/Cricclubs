package com.cricket.dto;

import java.io.Serializable;

public class ScorerDto implements Serializable {

	private static final long serialVersionUID = 8032519750630898370L;
	
	UserDto user;
	String session;
	
	public UserDto getUser() {
		return user;
	}
	
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	public String getSession() {
		return session;
	}
	
	public void setSession(String session) {
		this.session = session;
	}

}
