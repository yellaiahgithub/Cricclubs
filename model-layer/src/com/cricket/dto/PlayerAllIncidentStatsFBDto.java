package com.cricket.dto;

public class PlayerAllIncidentStatsFBDto {
	
	private String incidentType;
	private int noOfTimes;
	private String playerType;
	private int clubId;
	
	public String getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	public String getPlayerType() {
		return playerType;
	}
	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public int getNoOfTimes() {
		return noOfTimes;
	}
	public void setNoOfTimes(int noOfTimes) {
		this.noOfTimes = noOfTimes;
	}
}
