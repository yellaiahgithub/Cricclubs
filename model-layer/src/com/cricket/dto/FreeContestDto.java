package com.cricket.dto;

import java.util.Date;

public class FreeContestDto {
	
	private int 	id;	
	private int 	matchId;		
	private String 	contestName;	
	private int 	noOfParticipants;
	private int		noOfWinners;	
	private int 	entryPoints;		
	private int 	totalPrizePoints;	
	private int 	maxEntries;	
	private int		status;
	private int 	allowMultiEntry;
	private String 	type;
	private Date 	createdAt;
	private Date 	updatedAt;
	private long 	userId;
	private int  	noOfUsersJoined;
	private int     noOfUserEntries;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	public int getNoOfParticipants() {
		return noOfParticipants;
	}
	public void setNoOfParticipants(int noOfParticipants) {
		this.noOfParticipants = noOfParticipants;
	}
	public int getNoOfWinners() {
		return noOfWinners;
	}
	public void setNoOfWinners(int noOfWinners) {
		this.noOfWinners = noOfWinners;
	}
	public int getEntryPoints() {
		return entryPoints;
	}
	public void setEntryPoints(int entryPoints) {
		this.entryPoints = entryPoints;
	}	
	public int getMaxEntries() {
		return maxEntries;
	}
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getNoOfUsersJoined() {
		return noOfUsersJoined;
	}
	public void setNoOfUsersJoined(int noOfUsersJoined) {
		this.noOfUsersJoined = noOfUsersJoined;
	}
	public int getNoOfUserEntries() {
		return noOfUserEntries;
	}
	public void setNoOfUserEntries(int noOfUserEntries) {
		this.noOfUserEntries = noOfUserEntries;
	}
	public int getAllowMultiEntry() {
		return allowMultiEntry;
	}
	public void setAllowMultiEntry(int allowMultiEntry) {
		this.allowMultiEntry = allowMultiEntry;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTotalPrizePoints() {
		return totalPrizePoints;
	}
	public void setTotalPrizePoints(int totalPrizePoints) {
		this.totalPrizePoints = totalPrizePoints;
	}	
	
	
}
