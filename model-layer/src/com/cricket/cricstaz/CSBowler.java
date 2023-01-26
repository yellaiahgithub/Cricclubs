package com.cricket.cricstaz;

public class CSBowler {
	private String Firstname;
	private String Lastname;
	private String DOB;
	private String Overs;
	private String Maidens;
	private String Wickets;
	private String RunsA;
	private String Wides;
	private String Noballs;
	
	public CSBowler() {
		super();
	}
	
	public CSBowler(String firstname, String lastname, String dOB, String overs, String maidens, String wickets,
			String runsA, String wides, String noballs) {
		super();
		Firstname = firstname;
		Lastname = lastname;
		DOB = dOB;
		Overs = overs;
		Maidens = maidens;
		Wickets = wickets;
		RunsA = runsA;
		Wides = wides;
		Noballs = noballs;
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

	public String getOvers() {
		return Overs;
	}

	public void setOvers(String overs) {
		Overs = overs;
	}

	public String getMaidens() {
		return Maidens;
	}

	public void setMaidens(String maidens) {
		Maidens = maidens;
	}

	public String getWickets() {
		return Wickets;
	}

	public void setWickets(String wickets) {
		Wickets = wickets;
	}

	public String getRunsA() {
		return RunsA;
	}

	public void setRunsA(String runsA) {
		RunsA = runsA;
	}

	public String getWides() {
		return Wides;
	}

	public void setWides(String wides) {
		Wides = wides;
	}

	public String getNoballs() {
		return Noballs;
	}

	public void setNoballs(String noballs) {
		Noballs = noballs;
	}
	
}
