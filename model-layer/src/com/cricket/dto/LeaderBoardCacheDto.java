package com.cricket.dto;

public class LeaderBoardCacheDto {	
	private int 	contestId;
	private long	userId;
	private int 	rank;
	private int   	earnedPoints;
	private String  displayName;
	private String  profileImagePath;
	private String  teamName;
	private long	teamId;
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getEarnedPoints() {
		return earnedPoints;
	}
	public void setEarnedPoints(int earnedPoints) {
		this.earnedPoints = earnedPoints;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
