package com.cricket.dto;

import java.sql.Date;

public class FixtureLiveDto {

	private int fixtureId;
	private String fixtureDate;
	private String fixtureTime;
	private int matchId;
	private String matchType;
	private int teamOne;
	private String teamOneName;
	private int teamOneCaptain;
	private int teamOneClubId;
	private int teamTwo;
	private String teamTwoName;
	private int teamTwoCaptain;
	private int teamTwoClubId;
	private int maxOvers;
	private int isLeagueClubStructureEnabled;
	private int seriesId;
	private boolean isFantasy;
	
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}
	public int getTeamOne() {
		return teamOne;
	}
	public void setTeamOne(int teamOne) {
		this.teamOne = teamOne;
	}
	public String getTeamOneName() {
		return teamOneName;
	}
	public void setTeamOneName(String teamOneName) {
		this.teamOneName = teamOneName;
	}
	public int getTeamOneCaptain() {
		return teamOneCaptain;
	}
	public void setTeamOneCaptain(int teamOneCaptain) {
		this.teamOneCaptain = teamOneCaptain;
	}
	public int getTeamTwo() {
		return teamTwo;
	}
	public void setTeamTwo(int teamTwo) {
		this.teamTwo = teamTwo;
	}
	public String getTeamTwoName() {
		return teamTwoName;
	}
	public void setTeamTwoName(String teamTwoName) {
		this.teamTwoName = teamTwoName;
	}
	public int getTeamTwoCaptain() {
		return teamTwoCaptain;
	}
	public void setTeamTwoCaptain(int teamTwoCaptain) {
		this.teamTwoCaptain = teamTwoCaptain;
	}
	public int getMaxOvers() {
		return maxOvers;
	}
	public void setMaxOvers(int maxOvers) {
		this.maxOvers = maxOvers;
	}
	public int getIsLeagueClubStructureEnabled() {
		return isLeagueClubStructureEnabled;
	}
	public void setIsLeagueClubStructureEnabled(int isLeagueClubStructureEnabled) {
		this.isLeagueClubStructureEnabled = isLeagueClubStructureEnabled;
	}
	public int getTeamOneClubId() {
		return teamOneClubId;
	}
	public void setTeamOneClubId(int teamOneClubId) {
		this.teamOneClubId = teamOneClubId;
	}
	public int getTeamTwoClubId() {
		return teamTwoClubId;
	}
	public void setTeamTwoClubId(int teamTwoClubId) {
		this.teamTwoClubId = teamTwoClubId;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getFixtureDate() {
		return fixtureDate;
	}
	public void setFixtureDate(String fixtureDate) {
		this.fixtureDate = fixtureDate;
	}
	public String getFixtureTime() {
		return fixtureTime;
	}
	public void setFixtureTime(String fixtureTime) {
		this.fixtureTime = fixtureTime;
	}
	public int getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(int seriesId) {
		this.seriesId = seriesId;
	}
	public boolean isFantasy() {
		return isFantasy;
	}
	public void setFantasy(boolean isFantasy) {
		this.isFantasy = isFantasy;
	}
	
}
