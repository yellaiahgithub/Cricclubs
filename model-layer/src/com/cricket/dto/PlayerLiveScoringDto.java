package com.cricket.dto;

public class PlayerLiveScoringDto {	
	
	private long   newPlayerId;
	private String firstName;
	private String lastName;
	private String email;	
	private String phoneNumber;
	private String jerseyNumber;
	
	public long getNewPlayerId() {
		return newPlayerId;
	}
	public void setNewPlayerId(long newPlayerId) {
		this.newPlayerId = newPlayerId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getJerseyNumber() {
		return jerseyNumber;
	}
	public void setJerseyNumber(String jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}	
	
}
