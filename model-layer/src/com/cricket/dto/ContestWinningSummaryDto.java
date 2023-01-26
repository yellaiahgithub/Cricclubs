package com.cricket.dto;

public class ContestWinningSummaryDto {	
	
	private int 	matchId;	
	private String  matchTitle;
	private int		masterContestId;	
	private String 	contestType;
	private int 	noOfContests;
	private int 	noOfParticipants;
	private int 	noOfUsersJoined;
	private float 	totalPrizeAmount;
	private int		totalPrizeCoins;
	private float 	totalMoneyDistributed;
	private float 	totalCoinsDistributed;	
	private int  	noOfActaulUsers;
	private int 	contestId;
	private int 	noOfWinners;
	private int     noOfUsersGotMoney;
	
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
	public String getContestType() {
		return contestType;
	}
	public void setContestType(String contestType) {
		this.contestType = contestType;
	}
	public int getNoOfContests() {
		return noOfContests;
	}
	public void setNoOfContests(int noOfContests) {
		this.noOfContests = noOfContests;
	}
	public int getNoOfParticipants() {
		return noOfParticipants;
	}
	public void setNoOfParticipants(int noOfParticipants) {
		this.noOfParticipants = noOfParticipants;
	}
	public int getNoOfUsersJoined() {
		return noOfUsersJoined;
	}
	public void setNoOfUsersJoined(int noOfUsersJoined) {
		this.noOfUsersJoined = noOfUsersJoined;
	}
	public float getTotalPrizeAmount() {
		return totalPrizeAmount;
	}
	public void setTotalPrizeAmount(float totalPrizeAmount) {
		this.totalPrizeAmount = totalPrizeAmount;
	}
	public float getTotalMoneyDistributed() {
		return totalMoneyDistributed;
	}
	public void setTotalMoneyDistributed(float totalMoneyDistributed) {
		this.totalMoneyDistributed = totalMoneyDistributed;
	}
	public int getNoOfActaulUsers() {
		return noOfActaulUsers;
	}
	public void setNoOfActaulUsers(int noOfActaulUsers) {
		this.noOfActaulUsers = noOfActaulUsers;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	public int getNoOfWinners() {
		return noOfWinners;
	}
	public void setNoOfWinners(int noOfWinners) {
		this.noOfWinners = noOfWinners;
	}
	public int getNoOfUsersGotMoney() {
		return noOfUsersGotMoney;
	}
	public void setNoOfUsersGotMoney(int noOfUsersGotMoney) {
		this.noOfUsersGotMoney = noOfUsersGotMoney;
	}
	public String getMatchTitle() {
		return matchTitle;
	}
	public void setMatchTitle(String matchTitle) {
		this.matchTitle = matchTitle;
	}
	public int getTotalPrizeCoins() {
		return totalPrizeCoins;
	}
	public void setTotalPrizeCoins(int totalPrizeCoins) {
		this.totalPrizeCoins = totalPrizeCoins;
	}
	public float getTotalCoinsDistributed() {
		return totalCoinsDistributed;
	}
	public void setTotalCoinsDistributed(float totalCoinsDistributed) {
		this.totalCoinsDistributed = totalCoinsDistributed;
	}
}
