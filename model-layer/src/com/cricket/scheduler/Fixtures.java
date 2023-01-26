package com.cricket.scheduler;

import java.util.Date;

public class Fixtures {

	private Team team1;
	private Team team2;
	private Ground ground;
	private Date gameDate;

	public Fixtures() {}
	
	public Fixtures(Team team1, Team team2) {
		super();
		this.team1 = team1;
		this.team2 = team2;		
	}
	
	public Fixtures(Team team1, Team team2, Ground ground) {
		super();
		this.team1 = team1;
		this.team2 = team2;
		this.ground = ground;
	}
	
	
	
	public Fixtures(Team team1, Team team2, Ground ground, Date gameDate) {
		super();
		this.team1 = team1;
		this.team2 = team2;
		this.ground = ground;
		this.gameDate = gameDate;
	}

	public Team getTeam1() {
		return team1;
	}
	public void setTeam1(Team team1) {
		this.team1 = team1;
	}
	public Team getTeam2() {
		return team2;
	}
	public void setTeam2(Team team2) {
		this.team2 = team2;
	}
	public Ground getGround() {
		return ground;
	}
	public void setGround(Ground ground) {
		this.ground = ground;
	}	
	
	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	@Override
	public String toString() {
		return  team1 + " VS " + team2 + " AT  " + ground + " On " + gameDate + "]";
	}

	
	
}
