package com.cricket.dto;

import java.util.List;

public class FantasyUserTeamCacheDto {
	
	private int 	points;
	List<Long>	playerList;
	
	public List<Long> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(List<Long> playerList) {
		this.playerList = playerList;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
}
