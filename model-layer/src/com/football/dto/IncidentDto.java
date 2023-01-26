package com.football.dto;

import java.util.Date;

import com.cricket.utility.CommonUtility;

public class IncidentDto {

	private int incidentId;
	private int	matchId;
	private String minsOfGame;
	private String additionalMins;
	private String incidentType;
	private int player1Id;
	private int player2Id;
	private int	team1Goals;
	private int	team2Goals;
	private String comment;
	private String timeSaved;
	private Date createdTime;
	private Date updatedTime;
	private long clientId;
	private String incidentImg;
	private String player1Name;
	private String player2Name;
	private int player1TeamId;
	private int player2TeamId;
	private String player1TeamName;
	private String player2TeamName;	
	private String player1TeamLogoPath;
	private String player2TeamLogoPath;	
	
	public int getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(int incidentId) {
		this.incidentId = incidentId;
	}
	public String getMinsOfGame() {
		return minsOfGame;
	}
	public void setMinsOfGame(String minsOfGame) {
		this.minsOfGame = minsOfGame;
	}
	public String getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	public int getPlayer1Id() {
		return player1Id;
	}
	public void setPlayer1Id(int player1Id) {
		this.player1Id = player1Id;
	}
	public int getPlayer2Id() {
		return player2Id;
	}
	public void setPlayer2Id(int player2Id) {
		this.player2Id = player2Id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getAdditionalMins() {
		return additionalMins;
	}
	public void setAdditionalMins(String additionalMins) {
		this.additionalMins = additionalMins;
	}
	public int getTeam1Goals() {
		return team1Goals;
	}
	public void setTeam1Goals(int team1Goals) {
		this.team1Goals = team1Goals;
	}
	public int getTeam2Goals() {
		return team2Goals;
	}
	public void setTeam2Goals(int team2Goals) {
		this.team2Goals = team2Goals;
	}
	public String getTimeSaved() {
		return timeSaved;
	}
	public void setTimeSaved(String timeSaved) {
		this.timeSaved = timeSaved;
	}
	public String getIncidentImg() {
		return incidentImg;
	}
	public void setIncidentImg(String incidentImg) {
		this.incidentImg = incidentImg;
	}
	
	public String getPlayer1Name() {
		return player1Name;
	}
	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}
	public String getPlayer2Name() {
		return player2Name;
	}
	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}
	public int getPlayer1TeamId() {
		return player1TeamId;
	}
	public void setPlayer1TeamId(int player1TeamId) {
		this.player1TeamId = player1TeamId;
	}
	public int getPlayer2TeamId() {
		return player2TeamId;
	}
	public void setPlayer2TeamId(int player2TeamId) {
		this.player2TeamId = player2TeamId;
	}
	public String getPlayer1TeamName() {
		return player1TeamName;
	}
	public void setPlayer1TeamName(String player1TeamName) {
		this.player1TeamName = player1TeamName;
	}
	public String getPlayer2TeamName() {
		return player2TeamName;
	}
	public void setPlayer2TeamName(String player2TeamName) {
		this.player2TeamName = player2TeamName;
	}
	public String getIncidentImageLink() {
		String imgName="";
		switch(this.getIncidentType()) {
		
		case "Corner"           :  			imgName="Corner";
											break;
		case "Goal Scored"      :  		
		case "Goal Scored - Free kick":	
		case "Goal Scored - Penalty kick": 
		case "Own Goal": 	
											imgName="Goal_Scored";
											break;	
		case "Shot on target"   : 		    imgName="Shot_on_Target";
											break;					
		case "Tackle"           :  			imgName="Tackle";
											break;	
		case "Foul - Free kick" :
		case "Foul - Penalty kick":         imgName="Foul";
											break;	
		case "Interception"     : 			imgName="Interception";
											break;	
		case "Offside"           :  		imgName="Offside";
											break;
		case "Red card"         :  			imgName="Red_Card";
											break;	
		case "Substitute"       : 	    	imgName="Substitute";
											break;					
		case "Yellow card"      : 		 	imgName="Yellow_Card";
											break;	
		case "Goal saved"       : 
		case "Goal saved - Penalty":        imgName="Goal_Saved";
											break;	
		
		
		}
		if(!CommonUtility.isNullOrEmptyOrNULL(imgName)) {
		return "/documentsRep/Football-Icons/"+imgName+".png";
		}else {
			return null;
		}
		
	}
	public String getPlayer1TeamLogoPath() {
		return player1TeamLogoPath;
	}
	public void setPlayer1TeamLogoPath(String player1TeamLogoPath) {
		this.player1TeamLogoPath = player1TeamLogoPath;
	}
	public String getPlayer2TeamLogoPath() {
		return player2TeamLogoPath;
	}
	public void setPlayer2TeamLogoPath(String player2TeamLogoPath) {
		this.player2TeamLogoPath = player2TeamLogoPath;
	}
}
