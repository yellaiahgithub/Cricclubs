package com.cricket.promotions;

public class Scorecard {
	private String innings_number;
	private String batting_team_abbrev;
	private TeamScore team_1_score;
	private TeamScore team_2_score;
	
	public String getInnings_number() {
		return innings_number;
	}
	public void setInnings_number(String innings_number) {
		this.innings_number = innings_number;
	}
	public TeamScore getTeam_1_score() {
		return team_1_score;
	}
	public void setTeam_1_score(TeamScore team_1_score) {
		this.team_1_score = team_1_score;
	}
	public TeamScore getTeam_2_score() {
		return team_2_score;
	}
	public void setTeam_2_score(TeamScore team_2_score) {
		this.team_2_score = team_2_score;
	}
	public String getBatting_team_abbrev() {
		return batting_team_abbrev;
	}
	public void setBatting_team_abbrev(String batting_team_abbrev) {
		this.batting_team_abbrev = batting_team_abbrev;
	}
	
	
}
