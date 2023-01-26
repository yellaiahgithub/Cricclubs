/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerMatchBowlingDto {
	
	private int playerID;
	private int runs;
	private int balls;
	private String firstName;
	private String lastName;
	private int maidens;
	private int wickets;
	private int wides;
	private int noBalls;
	private int hattricks;
	private int dotBalls;
	private int matchId;
	private String matchDate;
	private int teamId;
	private String teamName;
	private int againstId;
	private String againstName;
	private String againstCode;
	private int winnerId;
	private String winnerName;
	private String winnerCode;
	private String matchType;
	private int points;
	private String t1_logo_file_path;
	private String t2_logo_file_path;
	private String profilepic_file_path;
	
	

	public String getAgainstCode() {
		return againstCode;
	}

	public void setAgainstCode(String againstCode) {
		this.againstCode = againstCode;
	}

	public String getWinnerCode() {
		return winnerCode;
	}

	public void setWinnerCode(String winnerCode) {
		this.winnerCode = winnerCode;
	}

	public String getProfilepic_file_path() {
		return profilepic_file_path;
	}

	public void setProfilepic_file_path(String profilepic_file_path) {
		this.profilepic_file_path = profilepic_file_path;
	}

	public String getT1_logo_file_path() {
		return t1_logo_file_path;
	}

	public void setT1_logo_file_path(String t1_logo_file_path) {
		this.t1_logo_file_path = t1_logo_file_path;
	}

	public String getT2_logo_file_path() {
		return t2_logo_file_path;
	}

	public void setT2_logo_file_path(String t2_logo_file_path) {
		this.t2_logo_file_path = t2_logo_file_path;
	}

	
	

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getBalls() {
		return balls;
	}

	public void setBalls(int balls) {
		this.balls = balls;
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

	public int getMaidens() {
		return maidens;
	}

	public void setMaidens(int maidens) {
		this.maidens = maidens;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getWides() {
		return wides;
	}

	public void setWides(int wides) {
		this.wides = wides;
	}

	public int getNoBalls() {
		return noBalls;
	}

	public void setNoBalls(int noBalls) {
		this.noBalls = noBalls;
	}
	
	public int getHattricks() {
		return hattricks;
	}

	public void setHattricks(int hattricks) {
		this.hattricks = hattricks;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getAgainstId() {
		return againstId;
	}

	public void setAgainstId(int againstId) {
		this.againstId = againstId;
	}

	public String getAgainstName() {
		return againstName;
	}

	public void setAgainstName(String againstName) {
		this.againstName = againstName;
	}

	public int getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(int winnerId) {
		this.winnerId = winnerId;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getStrikeRate(){
		
		double sr = 0;
		if(this.wickets != 0){
			sr = (double)this.balls/this.wickets;
		}
		return CommonUtility.Round(sr,1);
	}

	public String getEconomy(){
		
		double sr = 0;
		if(this.balls != 0){
			sr = (double)this.runs/this.balls;
		}
		return CommonUtility.Round(sr*6,2);
	}
	
	public String getEconomy(int ballToOver){
		
		double sr = 0;
		if(this.balls != 0){
			sr = (double)this.runs/this.balls;
		}
		return CommonUtility.Round(sr*ballToOver,2);
	}

	public int getDotBalls() {
		return dotBalls;
	}

	public void setDotBalls(int dotBalls) {
		this.dotBalls = dotBalls;
	}

}
