package com.cricket.cricstaz;

public class CSFOW {

	private String Score;
	private String Firstname;
	private String Lastname;
	private String DOB;
	
	public CSFOW() {
		super();
	}
	
	public CSFOW(String score, String firstname, String lastname, String dOB) {
		super();
		Score = score;
		Firstname = firstname;
		Lastname = lastname;
		DOB = dOB;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getFirstname() {
		return Firstname;
	}

	public void setFirstname(String firstname) {
		Firstname = firstname;
	}

	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		Lastname = lastname;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}
	
}
