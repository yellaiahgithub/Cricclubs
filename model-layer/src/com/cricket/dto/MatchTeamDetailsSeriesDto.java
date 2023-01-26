package com.cricket.dto;

public class MatchTeamDetailsSeriesDto {
	
	private long 	id;	
	private long 	matchTeamId;	
	private long 	playerId;
	private String 	role;
	private String 	category;	
	private int		roleCount;
	private int		matchId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMatchTeamId() {
		return matchTeamId;
	}
	public void setMatchTeamId(long matchTeamId) {
		this.matchTeamId = matchTeamId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getRoleCount() {
		return roleCount;
	}
	public void setRoleCount(int roleCount) {
		this.roleCount = roleCount;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
		
}
