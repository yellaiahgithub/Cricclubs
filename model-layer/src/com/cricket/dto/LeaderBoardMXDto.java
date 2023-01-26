package com.cricket.dto;

public class LeaderBoardMXDto {	
	
	private String userId;
	private int matchId;
	private int	masterContestId;
	private String points;
	private String cash;
	private int rank;
	private int coins;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public int getMasterContestId() {
		return masterContestId;
	}
	public void setMasterContestId(int masterContestId) {
		this.masterContestId = masterContestId;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	
}
