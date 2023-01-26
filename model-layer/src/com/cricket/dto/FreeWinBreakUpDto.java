package com.cricket.dto;

public class FreeWinBreakUpDto {
	
	private int 	id;	
	private int 	contestId;	
	private int 	startingRank;	
	private int 	endingRank;	
	private float 	amount;
	private String  rankRange;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	public int getStartingRank() {
		return startingRank;
	}
	public void setStartingRank(int startingRank) {
		this.startingRank = startingRank;
	}
	public int getEndingRank() {
		return endingRank;
	}
	public void setEndingRank(int endingRank) {
		this.endingRank = endingRank;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getRankRange() {
		return rankRange;
	}
	public void setRankRange(String rankRange) {
		this.rankRange = rankRange;
	}	
	
}
