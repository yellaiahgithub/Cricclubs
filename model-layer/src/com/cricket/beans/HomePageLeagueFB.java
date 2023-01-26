package com.cricket.beans;

import java.util.List;
import java.util.Map;

import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerStatisticsFBDto;
import com.cricket.dto.SeriesNumbersBeanFB;
import com.cricket.dto.TeamDto;

public class HomePageLeagueFB {
	
	private int leagueId;
	private int clubId;
	private String leagueName;
	private List<PlayerStatisticsFBDto> topGoalsScoredRecords;
	private List<PlayerStatisticsFBDto> topAssistsRescords;
	private List<PlayerDto> playerPoints;
	private List<PlayerStatisticsFBDto> weeklyTopGoalsScoredRecords;
	private List<PlayerStatisticsFBDto> weeklyTopAssistsRecords;
	private List<PlayerStatisticsFBDto> weeklyPlayerPoints;
	private SeriesNumbersBeanFB seriesInNumbers;
	private List<TeamDto> teams;
	private List<TeamDto> removedTeams;
	private Map<Integer,String> groupNames;
	private int groups;
	
	public int getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public List<PlayerStatisticsFBDto> getTopGoalsScoredRecords() {
		return topGoalsScoredRecords;
	}
	public void setTopGoalsScoredRecords(List<PlayerStatisticsFBDto> topGoalsScoredRecords) {
		this.topGoalsScoredRecords = topGoalsScoredRecords;
	}
	public List<PlayerStatisticsFBDto> getTopAssistsRescords() {
		return topAssistsRescords;
	}
	public void setTopAssistsRescords(List<PlayerStatisticsFBDto> topAssistsRescords) {
		this.topAssistsRescords = topAssistsRescords;
	}
	public List<PlayerDto> getPlayerPoints() {
		return playerPoints;
	}
	public void setPlayerPoints(List<PlayerDto> playerPoints) {
		this.playerPoints = playerPoints;
	}
	public List<PlayerStatisticsFBDto> getWeeklyTopGoalsScoredRecords() {
		return weeklyTopGoalsScoredRecords;
	}
	public void setWeeklyTopGoalsScoredRecords(List<PlayerStatisticsFBDto> weeklyTopGoalsScoredRecords) {
		this.weeklyTopGoalsScoredRecords = weeklyTopGoalsScoredRecords;
	}
	public List<PlayerStatisticsFBDto> getWeeklyTopAssistsRecords() {
		return weeklyTopAssistsRecords;
	}
	public void setWeeklyTopAssistsRecords(List<PlayerStatisticsFBDto> weeklyTopAssistsRecords) {
		this.weeklyTopAssistsRecords = weeklyTopAssistsRecords;
	}
	public List<PlayerStatisticsFBDto> getWeeklyPlayerPoints() {
		return weeklyPlayerPoints;
	}
	public void setWeeklyPlayerPoints(List<PlayerStatisticsFBDto> weeklyPlayerPoints) {
		this.weeklyPlayerPoints = weeklyPlayerPoints;
	}
	public SeriesNumbersBeanFB getSeriesInNumbers() {
		return seriesInNumbers;
	}
	public void setSeriesInNumbers(SeriesNumbersBeanFB seriesInNumbers) {
		this.seriesInNumbers = seriesInNumbers;
	}
	public List<TeamDto> getTeams() {
		return teams;
	}
	public void setTeams(List<TeamDto> teams) {
		this.teams = teams;
	}
	public List<TeamDto> getRemovedTeams() {
		return removedTeams;
	}
	public void setRemovedTeams(List<TeamDto> removedTeams) {
		this.removedTeams = removedTeams;
	}
	public Map<Integer, String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(Map<Integer, String> groupNames) {
		this.groupNames = groupNames;
	}
	public int getGroups() {
		return groups;
	}
	public void setGroups(int groups) {
		this.groups = groups;
	}
	
}
