package com.cricket.dto;

public class MatchResultsByGroundDto {

	private int matchesWonBattingFirst;
	private int matchesWonBattingSecond;
	private int avgScoreBattingFirst;
	private int avgScoreBattingSecond;
	private int totalMatches;
	private String location;
	private String address;
	
	public int getMatchesWonBattingFirst() {
		return matchesWonBattingFirst;
	}
	public void setMatchesWonBattingFirst(int matchesWonBattingFirst) {
		this.matchesWonBattingFirst = matchesWonBattingFirst;
	}
	public int getMatchesWonBattingSecond() {
		return matchesWonBattingSecond;
	}
	public void setMatchesWonBattingSecond(int matchesWonBattingSecond) {
		this.matchesWonBattingSecond = matchesWonBattingSecond;
	}
	public int getAvgScoreBattingFirst() {
		return avgScoreBattingFirst;
	}
	public void setAvgScoreBattingFirst(int avgScoreBattingFirst) {
		this.avgScoreBattingFirst = avgScoreBattingFirst;
	}
	public int getAvgScoreBattingSecond() {
		return avgScoreBattingSecond;
	}
	public void setAvgScoreBattingSecond(int avgScoreBattingSecond) {
		this.avgScoreBattingSecond = avgScoreBattingSecond;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getTotalMatches() {
		return totalMatches;
	}
	public void setTotalMatches(int totalMatches) {
		this.totalMatches = totalMatches;
	}
	
}
