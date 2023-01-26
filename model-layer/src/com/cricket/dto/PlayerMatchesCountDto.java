/*
 * Created on Apr 9, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

/**
 * @author Prudhvi
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerMatchesCountDto {

	private int playerId;
	private String firstName;
	private String lastName;
	private String clubName;
	private String homeTeamName;
	private String homeLeagueName;
	private String teamName;
	private String leagueName;
	private int count;
	
	
	
	@Override
	public String toString() {
		return "PlayerMatchesCountDto [playerId=" + playerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", clubName=" + clubName + ", homeTeamName=" + homeTeamName + ", homeLeagueName=" + homeLeagueName
				+ ", teamName=" + teamName + ", leagueName=" + leagueName + ", count=" + count + "]";
	}
	
	
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
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getHomeTeamName() {
		return homeTeamName;
	}
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	public String getHomeLeagueName() {
		return homeLeagueName;
	}
	public void setHomeLeagueName(String homeLeagueName) {
		this.homeLeagueName = homeLeagueName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
