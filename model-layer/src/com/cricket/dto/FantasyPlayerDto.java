package com.cricket.dto;

public class FantasyPlayerDto {
	
	private int 	id;		
	private int		ccPlayerId;
	private String 	name;
	private String 	nickName;
	private String 	category;
	private String 	gender;
	private String  imagePath;
	private float	credits;
	private String 	teamName;
	private String 	seriesName;
	private int		playerPoints;
	private int 	teamId;
	private int 	tournamentId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public float getCredits() {
		return credits;
	}
	public void setCredits(float credits) {
		this.credits = credits;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public int getPlayerPoints() {
		return playerPoints;
	}
	public void setPlayerPoints(int playerPoints) {
		this.playerPoints = playerPoints;
	}
	public int getCcPlayerId() {
		return ccPlayerId;
	}
	public void setCcPlayerId(int ccPlayerId) {
		this.ccPlayerId = ccPlayerId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public int getTournamentId() {
		return tournamentId;
	}
	public void setTournamentId(int tournamentId) {
		this.tournamentId = tournamentId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}		
	
}
