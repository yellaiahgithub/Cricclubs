package com.cricket.dto;

public class PlayerStatisticsFBDto {
	
	private int playerId;
	private String firstName;
	private String lastName;
	private int teamId;
	private String teamName;
	private String teamCode;
	private int seriesId;
	private String seriesName;
	private int matches;
	private String matchType;
	private String matchyear;
	private String seriesType;
	private int goalsScored;
	private int assists;
	private int goalsScoredPenalty;
	private int goalsScoredFreeKick;
	private int yellowCards;
	private int redCards;
	private int offside;
	private int points;
	private int shotsOnTargets;
	private int tackles;
	private int interceptions;
	private int goalsSaved;
	private String teamlogo_file_path;
	private String profilepic_file_path;
	private String playingRole = "";
	private int clubId;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public int getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(int seriesId) {
		this.seriesId = seriesId;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public int getMatches() {
		return matches;
	}
	public void setMatches(int matches) {
		this.matches = matches;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getMatchyear() {
		return matchyear;
	}
	public void setMatchyear(String matchyear) {
		this.matchyear = matchyear;
	}
	public String getSeriesType() {
		return seriesType;
	}
	public void setSeriesType(String seriesType) {
		this.seriesType = seriesType;
	}
	public int getGoalsScored() {
		return goalsScored;
	}
	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getYellowCards() {
		return yellowCards;
	}
	public void setYellowCards(int yellowCards) {
		this.yellowCards = yellowCards;
	}
	public int getRedCards() {
		return redCards;
	}
	public void setRedCards(int redCards) {
		this.redCards = redCards;
	}

	public int getPoints() {
		points = 0;		
		if (goalsScored > 0) {
			points += goalsScored;
		}
		if (assists > 0) {
			points += assists;
		}		
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getTeamlogo_file_path() {
		return teamlogo_file_path;
	}
	public void setTeamlogo_file_path(String teamlogo_file_path) {
		this.teamlogo_file_path = teamlogo_file_path;
	}
	public String getProfilepic_file_path() {
		return profilepic_file_path;
	}
	public void setProfilepic_file_path(String profilepic_file_path) {
		this.profilepic_file_path = profilepic_file_path;
	}
	public String getPlayingRole() {
		return playingRole;
	}
	public void setPlayingRole(String playingRole) {
		this.playingRole = playingRole;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getPlayerName() {
		return this.firstName+" "+this.lastName;
	}
	public int getShotsOnTargets() {
		return shotsOnTargets;
	}
	public void setShotsOnTargets(int shotsOnTargets) {
		this.shotsOnTargets = shotsOnTargets;
	}
	public int getTackles() {
		return tackles;
	}
	public void setTackles(int tackles) {
		this.tackles = tackles;
	}
	public int getInterceptions() {
		return interceptions;
	}
	public void setInterceptions(int interceptions) {
		this.interceptions = interceptions;
	}
	public int getGoalsSaved() {
		return goalsSaved;
	}
	public void setGoalsSaved(int goalsSaved) {
		this.goalsSaved = goalsSaved;
	}
	public int getGoalsScoredPenalty() {
		return goalsScoredPenalty;
	}
	public void setGoalsScoredPenalty(int goalsScoredPenalty) {
		this.goalsScoredPenalty = goalsScoredPenalty;
	}
	public int getGoalsScoredFreeKick() {
		return goalsScoredFreeKick;
	}
	public void setGoalsScoredFreeKick(int goalsScoredFreeKick) {
		this.goalsScoredFreeKick = goalsScoredFreeKick;
	}
	public int getOffside() {
		return offside;
	}
	public void setOffside(int offside) {
		this.offside = offside;
	}
}
