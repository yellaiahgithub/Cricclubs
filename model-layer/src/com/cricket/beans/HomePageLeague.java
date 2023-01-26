package com.cricket.beans;

import java.util.List;
import java.util.Map;

import com.cricket.dto.PlayerBattingDto;
import com.cricket.dto.PlayerBowlingDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.SeriesNumbersBean;
import com.cricket.dto.TeamDto;

public class HomePageLeague {
	private int leagueId;
	private int clubId;// TODo - For SCC and multiple club data at new embeded page.
	private String leagueName;
	private List<PlayerBattingDto> battingRecords;
	private List<PlayerBowlingDto> bowlingRecords;
	private List<PlayerDto> playerPoints;
	private List<PlayerBattingDto> weeklyBattingRecords;
	private List<PlayerBowlingDto> weeklyBowlingRecords;
	private List<PlayerDto> weeklyPlayerPoints;
	private SeriesNumbersBean seriesInNumbers;
	private List<TeamDto> teams;
	private List<TeamDto> removedTeams;
	private Map<Integer,String> groupNames;
	private int groups;

	
	
	public List<TeamDto> getRemovedTeams() {
		return removedTeams;
	}

	public void setRemovedTeams(List<TeamDto> removedTeams) {
		this.removedTeams = removedTeams;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public List<PlayerBattingDto> getBattingRecords() {
		return battingRecords;
	}

	public void setBattingRecords(List<PlayerBattingDto> battingRecords) {
		this.battingRecords = battingRecords;
	}

	public List<PlayerBowlingDto> getBowlingRecords() {
		return bowlingRecords;
	}

	public void setBowlingRecords(List<PlayerBowlingDto> bowlingRecords) {
		this.bowlingRecords = bowlingRecords;
	}

	public List<PlayerDto> getPlayerPoints() {
		return playerPoints;
	}

	public void setPlayerPoints(List<PlayerDto> playerPoints) {
		this.playerPoints = playerPoints;
	}

	public SeriesNumbersBean getSeriesInNumbers() {
		return seriesInNumbers;
	}

	public void setSeriesInNumbers(SeriesNumbersBean seriesInNumbers) {
		this.seriesInNumbers = seriesInNumbers;
	}

	public List<TeamDto> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamDto> teams) {
		this.teams = teams;
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

	public List<PlayerBattingDto> getWeeklyBattingRecords() {
		return weeklyBattingRecords;
	}

	public void setWeeklyBattingRecords(List<PlayerBattingDto> weeklyBattingRecords) {
		this.weeklyBattingRecords = weeklyBattingRecords;
	}

	public List<PlayerBowlingDto> getWeeklyBowlingRecords() {
		return weeklyBowlingRecords;
	}

	public void setWeeklyBowlingRecords(List<PlayerBowlingDto> weeklyBowlingRecords) {
		this.weeklyBowlingRecords = weeklyBowlingRecords;
	}

	public List<PlayerDto> getWeeklyPlayerPoints() {
		return weeklyPlayerPoints;
	}

	public void setWeeklyPlayerPoints(List<PlayerDto> weeklyPlayerPoints) {
		this.weeklyPlayerPoints = weeklyPlayerPoints;
	}
}
