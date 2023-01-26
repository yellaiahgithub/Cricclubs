package com.cricket.cricstaz;

public class CSUserInfo {

	private String Firstname;
	private String Lastname;
	private String DOB;
	
	
	public CSUserInfo() {
		super();
	}
	public CSUserInfo(String firstname, String lastname, String dOB) {
		super();
		Firstname = firstname;
		Lastname = lastname;
		DOB = dOB;
	}
	public CSUserInfo(String name) {
		if(name != null && !name.isEmpty()) {
			String names[] = name.split(" ");
			this.Firstname = names.length >= 1 ? names[0]:"";
			this.Lastname = names.length >= 2 ? names[1]:"";
			this.DOB = "";
		}
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
