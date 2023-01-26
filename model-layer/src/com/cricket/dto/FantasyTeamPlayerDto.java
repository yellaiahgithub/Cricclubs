package com.cricket.dto;

public class FantasyTeamPlayerDto {
	
	private long 	playerId;		
	private int 	teamId;
	private long 	ccPlayerId;		
	private int 	ccTeamId;
	private float	playerCredits;
	private int		playerPoints;
	private int 	ccClubId;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public long getCcPlayerId() {
		return ccPlayerId;
	}
	public void setCcPlayerId(long ccPlayerId) {
		this.ccPlayerId = ccPlayerId;
	}
	public int getCcTeamId() {
		return ccTeamId;
	}
	public void setCcTeamId(int ccTeamId) {
		this.ccTeamId = ccTeamId;
	}
	public float getPlayerCredits() {
		return playerCredits;
	}
	public void setPlayerCredits(float playerCredits) {
		this.playerCredits = playerCredits;
	}
	public int getPlayerPoints() {
		return playerPoints;
	}
	public void setPlayerPoints(int playerPoints) {
		this.playerPoints = playerPoints;
	}
	public int getCcClubId() {
		return ccClubId;
	}
	public void setCcClubId(int ccClubId) {
		this.ccClubId = ccClubId;
	}
	
}
