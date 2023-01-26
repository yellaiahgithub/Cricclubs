package com.cricket.beans;

import java.util.List;

public class ParScoreData {
	
	private List<ParScoreBall> balls;
	private String team1Name;
	private String team2Name;
	
	public List<ParScoreBall> getBalls() {
		return balls;
	}
	public void setBalls(List<ParScoreBall> balls) {
		this.balls = balls;
	}
	public String getTeam1Name() {
		return team1Name;
	}
	public void setTeam1Name(String team1Name) {
		this.team1Name = team1Name;
	}
	public String getTeam2Name() {
		return team2Name;
	}
	public void setTeam2Name(String team2Name) {
		this.team2Name = team2Name;
	}
}
