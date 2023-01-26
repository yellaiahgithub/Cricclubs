package com.cricket.dto;

import java.util.Map;

public class FantasyScoreCardDto {

	private int    id;
	private int 	clubId;
	private int 	ccMatchId;	
	private int 	ccFixtureId;
	private int 	fantasyMatchId;
	private String 	scoreCard;
	private Map<Integer,String> playerImagePathMap;
	private Map<Integer,String> teamLogoPathMap;
	private Map<Integer,Integer> playerIdMap;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public int getCcMatchId() {
		return ccMatchId;
	}
	public void setCcMatchId(int ccMatchId) {
		this.ccMatchId = ccMatchId;
	}
	public int getCcFixtureId() {
		return ccFixtureId;
	}
	public void setCcFixtureId(int ccFixtureId) {
		this.ccFixtureId = ccFixtureId;
	}
	public int getFantasyMatchId() {
		return fantasyMatchId;
	}
	public void setFantasyMatchId(int fantasyMatchId) {
		this.fantasyMatchId = fantasyMatchId;
	}
	public String getScoreCard() {
		return scoreCard;
	}
	public void setScoreCard(String scoreCard) {
		this.scoreCard = scoreCard;
	}
	public Map<Integer, String> getPlayerImagePathMap() {
		return playerImagePathMap;
	}
	public void setPlayerImagePathMap(Map<Integer, String> playerImagePathMap) {
		this.playerImagePathMap = playerImagePathMap;
	}
	public Map<Integer, String> getTeamLogoPathMap() {
		return teamLogoPathMap;
	}
	public void setTeamLogoPathMap(Map<Integer, String> teamLogoPathMap) {
		this.teamLogoPathMap = teamLogoPathMap;
	}
	public Map<Integer, Integer> getPlayerIdMap() {
		return playerIdMap;
	}
	public void setPlayerIdMap(Map<Integer, Integer> playerIdMap) {
		this.playerIdMap = playerIdMap;
	}
	
}
