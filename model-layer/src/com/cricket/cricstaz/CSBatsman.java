package com.cricket.cricstaz;

public class CSBatsman {
	private String Firstname;
	private String Lastname;
	private String DOB;
	private String Howout;
	private CSUserInfo Fielder;
	private CSUserInfo Bowler;
	private String Score;
	private String Fours;
	private String Sixes;
	private String Balls;
	private String Mins;
	private String MVP;
	
	public CSBatsman() {
		super();
	}
	
	public CSBatsman(String firstname, String lastname, String dOB, String howout, CSUserInfo fielder,
			CSUserInfo bowler, String score, String fours, String sixes, String balls, String mins, String mVP) {
		super();
		Firstname = firstname;
		Lastname = lastname;
		DOB = dOB;
		Howout = howout;
		Fielder = fielder;
		Bowler = bowler;
		Score = score;
		Fours = fours;
		Sixes = sixes;
		Balls = balls;
		Mins = mins;
		MVP = mVP;
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

	public String getHowout() {
		return Howout;
	}

	public void setHowout(String howout) {
		Howout = howout;
	}

	public CSUserInfo getFielder() {
		return Fielder;
	}

	public void setFielder(CSUserInfo fielder) {
		Fielder = fielder;
	}

	public CSUserInfo getBowler() {
		return Bowler;
	}

	public void setBowler(CSUserInfo bowler) {
		Bowler = bowler;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

	public String getFours() {
		return Fours;
	}

	public void setFours(String fours) {
		Fours = fours;
	}

	public String getSixes() {
		return Sixes;
	}

	public void setSixes(String sixes) {
		Sixes = sixes;
	}

	public String getBalls() {
		return Balls;
	}

	public void setBalls(String balls) {
		Balls = balls;
	}

	public String getMins() {
		return Mins;
	}

	public void setMins(String mins) {
		Mins = mins;
	}

	public String getMVP() {
		return MVP;
	}

	public void setMVP(String mVP) {
		MVP = mVP;
	}
	
}
