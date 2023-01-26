package com.cricket.scheduler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SchedulerConfigParams {

	private String fixtureType;
	private Date matchStart;
	private List<Integer> daysOnPlaying;
	private List<String> matchTime;
	private int gamePerTeamPerWeek;
	private int maxGamesPerWeek;
	private boolean isHomeAwayRule;
	private List<String> groundIds;
	private List<String> seriesExclusionStartDate;
	private List<String> seriesExclusionEndDate;
	private List<String> groundExcluded;
	private List<String> groundExclusionStartDate;
	private List<String> groundExclusionEndDate;
	
	private List<String> teamExcluded;
	private List<String> teamExclusionStartDate;
	private List<String> teamExclusionEndDate;
	
	
	public List<String> getTeamExcluded() {
		return teamExcluded;
	}
	public void setTeamExcluded(List<String> teamExcluded) {
		this.teamExcluded = teamExcluded;
	}
	public List<String> getTeamExclusionStartDate() {
		return teamExclusionStartDate;
	}
	public void setTeamExclusionStartDate(List<String> teamExclusionStartDate) {
		this.teamExclusionStartDate = teamExclusionStartDate;
	}
	public List<String> getTeamExclusionEndDate() {
		return teamExclusionEndDate;
	}
	public void setTeamExclusionEndDate(List<String> teamExclusionEndDate) {
		this.teamExclusionEndDate = teamExclusionEndDate;
	}
	public int getMaxGamesPerWeek() {
		return maxGamesPerWeek;
	}
	public void setMaxGamesPerWeek(int maxGamesPerWeek) {
		this.maxGamesPerWeek = maxGamesPerWeek;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public Date getMatchStart() {
		return matchStart;
	}
	public void setMatchStart(Date matchStart) {
		this.matchStart = matchStart;
	}
	public List<Integer> getDaysOnPlaying() {
		return daysOnPlaying;
	}
	public void setDaysOnPlaying(List<Integer> daysOnPlaying) {
		this.daysOnPlaying = daysOnPlaying;
	}
	public List<String> getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(List<String> matchTime) {
		this.matchTime = matchTime;
	}
	public int getGamePerTeamPerWeek() {
		return gamePerTeamPerWeek;
	}
	public void setGamePerTeamPerWeek(int gamePerTeamPerWeek) {
		this.gamePerTeamPerWeek = gamePerTeamPerWeek;
	}
	public boolean isHomeAwayRule() {
		return isHomeAwayRule;
	}
	public void setHomeAwayRule(boolean isHomeAwayRule) {
		this.isHomeAwayRule = isHomeAwayRule;
	}
	public List<String> getGroundIds() {
		return groundIds;
	}
	public void setGroundIds(List<String> groundIds) {
		this.groundIds = groundIds;
	}
	public List<String> getSeriesExclusionStartDate() {
		return seriesExclusionStartDate;
	}
	public void setSeriesExclusionStartDate(List<String> seriesExclusionStartDate) {
		this.seriesExclusionStartDate = seriesExclusionStartDate;
	}
	public List<String> getSeriesExclusionEndDate() {
		return seriesExclusionEndDate;
	}
	public void setSeriesExclusionEndDate(List<String> seriesExclusionEndDate) {
		this.seriesExclusionEndDate = seriesExclusionEndDate;
	}
	public List<String> getGroundExcluded() {
		return groundExcluded;
	}
	public void setGroundExcluded(List<String> groundExcluded) {
		this.groundExcluded = groundExcluded;
	}
	public List<String> getGroundExclusionStartDate() {
		return groundExclusionStartDate;
	}
	public void setGroundExclusionStartDate(List<String> groundExclusionStartDate) {
		this.groundExclusionStartDate = groundExclusionStartDate;
	}
	public List<String> getGroundExclusionEndDate() {
		return groundExclusionEndDate;
	}
	public void setGroundExclusionEndDate(List<String> groundExclusionEndDate) {
		this.groundExclusionEndDate = groundExclusionEndDate;
	}
	public SchedulerConfigParams(String fixtureType, Date matchStart,
			List<Integer> daysOnPlaying, List<String> matchTime,
			int gamePerTeamPerWeek, int maxGamesPerWeek, boolean isHomeAwayRule,
			List<String> groundIds, List<String> seriesExclusionStartDate,
			List<String> seriesExclusionEndDate, List<String> groundExcluded,
			List<String> groundExclusionStartDate,
			List<String> groundExclusionEndDate,
			List<String> teamExcluded,
			List<String> teamExclusionStartDate,
			List<String> teamExclusionEndDate) {
		super();
		this.fixtureType = fixtureType;
		this.matchStart = matchStart;
		this.daysOnPlaying = daysOnPlaying;
		this.matchTime = matchTime;
		this.gamePerTeamPerWeek = gamePerTeamPerWeek;
		this.maxGamesPerWeek = maxGamesPerWeek;
		this.isHomeAwayRule = isHomeAwayRule;
		this.groundIds = groundIds;
		this.seriesExclusionStartDate = seriesExclusionStartDate;
		this.seriesExclusionEndDate = seriesExclusionEndDate;
		this.groundExcluded = groundExcluded;
		this.groundExclusionStartDate = groundExclusionStartDate;
		this.groundExclusionEndDate = groundExclusionEndDate;
		this.teamExcluded = teamExcluded;
		this.teamExclusionStartDate = teamExclusionStartDate;
		this.teamExclusionEndDate = teamExclusionEndDate;
	}
	public SchedulerConfigParams() {
		this.fixtureType = "RR";
		this.matchStart = new Date();
		this.daysOnPlaying = Arrays.asList(1);
		this.matchTime = Arrays.asList("08:00 AM");;
		this.gamePerTeamPerWeek = 1;
		this.isHomeAwayRule = false;
	}
	
	public String getGroundIdsString(){
		StringBuffer buffer = new StringBuffer("");
		
		if(this.groundIds != null && !this.groundIds.isEmpty()){
			for(String s : this.groundIds){
				buffer.append(s);
				buffer.append(",");
			}
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}
}
