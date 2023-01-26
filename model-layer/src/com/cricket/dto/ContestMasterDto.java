package com.cricket.dto;

import java.util.Date;

public class ContestMasterDto {
	
	private int 	id;			
	private String 	fantasyCurrency;		
	private String 	contestType;	
	private int 	noOfParticipants;
	private int 	minimumParticipants;
	private int		noOfWinners;	
	private float 	entryFee;	
	private float 	totalPrizeAmount;	
	private int		totalCoins;
	private int 	maxEntries;	
	private int		status;
	private Date 	createdAt;
	private Date 	updatedAt;
	private long 	userId;
	private int  	noOfUsersJoined;
	private int  	noOfUserEntries;
	private float 	maxBonusAmount;
	private int   maxBnousPercent;	
	private int   autoCreateContest;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public float getEntryFee() {
		return entryFee;
	}
	public void setEntryFee(float entryFee) {
		this.entryFee = entryFee;
	}
	public float getTotalPrizeAmount() {
		return totalPrizeAmount;
	}
	public void setTotalPrizeAmount(float totalPrizeAmount) {
		this.totalPrizeAmount = totalPrizeAmount;
	}
	public int getMaxEntries() {
		return maxEntries;
	}
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	public String getContestType() {
		return contestType;
	}
	public void setContestType(String contestType) {
		this.contestType = contestType;
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
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public boolean isUserJoined() {
		return this.noOfUserEntries > 0;
	}		
	public float getMaxBonusAmount() {
		return maxBonusAmount;
	}
	public void setMaxBonusAmount(float maxBonusAmount) {
		this.maxBonusAmount = maxBonusAmount;
	}
	public int getMaxBnousPercent() {
		return maxBnousPercent;
	}
	public void setMaxBnousPercent(int maxBnousPercent) {
		this.maxBnousPercent = maxBnousPercent;
	}
	public int getMinimumParticipants() {
		return minimumParticipants;
	}
	public void setMinimumParticipants(int minimumParticipants) {
		this.minimumParticipants = minimumParticipants;
	}
	public int getAutoCreateContest() {
		return autoCreateContest;
	}
	public void setAutoCreateContest(int autoCreateContest) {
		this.autoCreateContest = autoCreateContest;
	}
	public String getFantasyCurrency() {
		return fantasyCurrency;
	}
	public void setFantasyCurrency(String fantasyCurrency) {
		this.fantasyCurrency = fantasyCurrency;
	}
	public ContestMasterDto() {
		super();
	}
	
	public ContestMasterDto(int id, String fantasyCurrency, String contestType, int noOfParticipants,
			int minimumParticipants,
			/* int noOfWinners, */ float entryFee,
			/* float totalPrizeAmount, */ int maxEntries, 
			float maxBonusAmount, int autoCreateContest) {
		super();
		this.id = id;
		this.fantasyCurrency = fantasyCurrency;
		this.contestType = contestType;
		this.noOfParticipants = noOfParticipants;
		this.minimumParticipants = minimumParticipants;
		//this.noOfWinners = noOfWinners;
		this.entryFee = entryFee;
		//this.totalPrizeAmount = totalPrizeAmount;
		this.maxEntries = maxEntries;
		this.maxBonusAmount = maxBonusAmount;
		this.autoCreateContest = autoCreateContest;
	}
	public int getTotalCoins() {
		return totalCoins;
	}
	public void setTotalCoins(int totalCoins) {
		this.totalCoins = totalCoins;
	}
	
}
