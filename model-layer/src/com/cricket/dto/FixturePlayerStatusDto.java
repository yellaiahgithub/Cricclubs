package com.cricket.dto;

import java.util.Date;

public class FixturePlayerStatusDto {

	private int fixtureId;
	private int playerId;
	private int teamId;
	private String statusCode;
	private String statusDesc;
	private Date createdDate;
	private Date lastUpdated;
	
	public FixturePlayerStatusDto(int fixtureId, int playerId, int teamId, String playerAvailabilityStatus) {
		setFixtureId(fixtureId);
		setPlayerId(playerId);
		setStatusCode(playerAvailabilityStatus);
		setTeamId(teamId);
	}
	
	public FixturePlayerStatusDto() {
		// TODO Auto-generated constructor stub
	}
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
		
		switch(this.statusCode) {
			case "A":
				statusDesc = "Available";
				break;
			case "NA":
				statusDesc = "Not Available";
				break;
			case "NS":
				statusDesc = "Not Sure";
				break;
			default:
				statusDesc = "Decision Pending";
				break;
		}
		
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}
