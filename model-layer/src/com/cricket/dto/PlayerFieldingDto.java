/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;



/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerFieldingDto {
	private int playerID;
	private String firstName;
	private String lastName;
	private int totalMatches;
	private int totalInngs;
	private int catches;
	private int wkcatches;
	private int direct;
	private int indirect;
	private int stumpings;
	private int total;
	private int teamId;
	private String teamName;
	private String matchType;
	private String teamlogo_file_path;
	private String profilepic_file_path;
	private String playingRole = "";

		
	public String getPlayingRole() {
		return playingRole;
	}
	
	public void setPlayingRole(String playingRole) {
		this.playingRole = playingRole;
	}
	public int getTotalMatches() {
		return totalMatches;
	}


	public void setTotalMatches(int totalMatches) {
		this.totalMatches = totalMatches;
	}


	public int getTotalInngs() {
		return totalInngs;
	}


	public void setTotalInngs(int totalInngs) {
		this.totalInngs = totalInngs;
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


	public int getPlayerID() {
		return playerID;
	}


	public void setPlayerID(int playerID) {
		this.playerID = playerID;
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


	public int getCatches() {
		return catches;
	}


	public void setCatches(int catches) {
		this.catches = catches;
	}


	public int getDirect() {
		return direct;
	}


	public void setDirect(int direct) {
		this.direct = direct;
	}


	public int getIndirect() {
		return indirect;
	}


	public void setIndirect(int indirect) {
		this.indirect = indirect;
	}


	public int getStumpings() {
		return stumpings;
	}


	public void setStumpings(int stumpings) {
		this.stumpings = stumpings;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public String getMatchType() {
		return matchType;
	}


	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}


	public int getPoints() {
		int points = 0;
		points += (catches*10) + (stumpings*20) + (indirect *10) + (direct * 20);
		return points;
	}
	
	public int getFieldingPoints(int clubId) {
		if(clubId == 501){
			return 0;
		} else if(clubId == 8936) {
			return (catches*10) + (stumpings*10) + (indirect *10) + (direct* 10);
		} else if(clubId == 17117 || clubId == 6265) {
			return (catches*3) + (stumpings*3) + (indirect *3) + (direct* 5);
		} else if(clubId == 19694) {
			return (catches*5) + (stumpings*20);
		} else{
			return this.getPoints();
		}
	}


	public int getWkcatches() {
		return wkcatches;
	}


	public void setWkcatches(int wkcatches) {
		this.wkcatches = wkcatches;
	}


	public String getTeamName() {
		return teamName;
	}


	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}


	public int getTeamId() {
		return teamId;
	}


	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public int getFielderCatches() {
		return this.catches - this.wkcatches;
	}

}
