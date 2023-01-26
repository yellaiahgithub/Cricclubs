/*
 * Created on Apr 9, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.io.Serializable;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FixturesAuditDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6267030249989697391L;
	private int fixtureId;
	private int teamOne;
	private int teamTwo;
	private String teamOneName;
	private String teamTwoName;
	private String date;
	private String time;
	private String matchType;
	private String location = "";
	private String locationMap = "";
	private int leagueId;
	private String leagueName;
	private String comment;
	private int limit;
	private int matchID;
	private String inValidReason;
	private String umpire1;
	private String umpire2;
	private boolean matchLocked;
	private String t1Description;
	private String t2Description;
	private int groundId;
	private int umpire1TeamId;
	private int umpire2TeamId;
	private int umpire1Id;
	private int umpire2Id;
	private String scorerName;
	private int scorerId;
	private int paperScorerId;
	private String t1_logo_file_path;
	private String t2_logo_file_path;
	private String seriesType;
	private String umpire1Name = "";
	private String umpire2Name = "";
	private boolean isMatchComplete;
	private String changeDate;
	private String changeType;
	private int changeBy;
	private int changeId;
	
	public FixturesAuditDto() {
		super();
	}
	
	public void setChangeBy(int i) {
		changeBy = i;
	}
	
	public int getChangeBy() {
		return changeBy;
	}
	
	public void setChangeId(int i) {
		changeId = i;
	}
	
	public int getChangeId() {
		return changeId;
	}
	
	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
	public String getChangeDate() {
		return changeDate;
	}
	
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	
	public boolean isMatchComplete() {
		return isMatchComplete;
	}
	public void setMatchComplete(boolean isMatchComplete) {
		this.isMatchComplete = isMatchComplete;
	}

	public String getUmpire1Name() {
		return umpire1Name;
	}

	public void setUmpire1Name(String umpire1Name) {
		this.umpire1Name = umpire1Name;
	}

	public String getUmpire2Name() {
		return umpire2Name;
	}

	public void setUmpire2Name(String umpire2Name) {
		this.umpire2Name = umpire2Name;
	}

	public String getSeriesType() {
		return seriesType;
	}

	public void setSeriesType(String seriesType) {
		this.seriesType = seriesType;
	}

	public String getT1_logo_file_path() {
		return t1_logo_file_path;
	}

	public void setT1_logo_file_path(String t1_logo_file_path) {
		this.t1_logo_file_path = t1_logo_file_path;
	}

	public String getT2_logo_file_path() {
		return t2_logo_file_path;
	}

	public void setT2_logo_file_path(String t2_logo_file_path) {
		this.t2_logo_file_path = t2_logo_file_path;
	}

	/**
	 * @return
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return
	 */
	public int getFixtureId() {
		return fixtureId;
	}

	/**
	 * @return
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @return
	 */
	public String getMatchType() {
		return matchType;
	}

	/**
	 * @return
	 */
	public int getTeamOne() {
		return teamOne;
	}

	/**
	 * @return
	 */
	public String getTeamOneName() {
		return teamOneName;
	}

	/**
	 * @return
	 */
	public int getTeamTwo() {
		return teamTwo;
	}

	/**
	 * @return
	 */
	public String getTeamTwoName() {
		return teamTwoName;
	}

	/**
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param string
	 */
	public void setDate(String string) {
		date = string;
	}

	/**
	 * @param i
	 */
	public void setFixtureId(int i) {
		fixtureId = i;
	}

	/**
	 * @param i
	 */
	public void setLimit(int i) {
		limit = i;
	}

	/**
	 * @param string
	 */
	public void setMatchType(String string) {
		matchType = string;
	}

	/**
	 * @param i
	 */
	public void setTeamOne(int i) {
		teamOne = i;
	}

	/**
	 * @param string
	 */
	public void setTeamOneName(String string) {
		teamOneName = string;
	}

	/**
	 * @param i
	 */
	public void setTeamTwo(int i) {
		teamTwo = i;
	}

	/**
	 * @param string
	 */
	public void setTeamTwoName(String string) {
		teamTwoName = string;
	}

	/**
	 * @param string
	 */
	public void setTime(String string) {
		time = string;
	}

	/**
	 * @return
	 */
	public String getLocation() {
		return location==null?"":location;
	}

	public void setLocation(String string) {
		location = string;
	}

	public String getLocationMap() {
		return locationMap;
	}

	public void setLocationMap(String locationMap) {
		this.locationMap = locationMap;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	
	public String getMatchTypeShortString() {
			return this.matchType.toUpperCase();
	}
	
	public String getComment() {
		return comment==null?"":comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getMatchID() {
		return matchID;
	}

	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}

	public String getInValidReason() {
		return inValidReason;
	}

	public void setInValidReason(String inValidReason) {
		this.inValidReason = inValidReason;
	}

	public String getUmpire1() {
		return umpire1 == null?"":umpire1;
	}

	public void setUmpire1(String umpire1) {
		this.umpire1 = umpire1;
	}

	public String getUmpire2() {
		return umpire2==null?"":umpire2;
	}

	public void setUmpire2(String umpire2) {
		this.umpire2 = umpire2;
	}

	public boolean isMatchLocked() {
		return matchLocked;
	}

	public void setMatchLocked(boolean matchLocked) {
		this.matchLocked = matchLocked;
	}

	public String getT1Description() {
		return t1Description;
	}

	public void setT1Description(String t1Description) {
		this.t1Description = t1Description;
	}

	public String getT2Description() {
		return t2Description;
	}

	public void setT2Description(String t2Description) {
		this.t2Description = t2Description;
	}

	public int getGroundId() {
		return groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}

	public int getUmpire1TeamId() {
		return umpire1TeamId;
	}

	public void setUmpire1TeamId(int umpire1TeamId) {
		this.umpire1TeamId = umpire1TeamId;
	}

	public int getUmpire2TeamId() {
		return umpire2TeamId;
	}

	public void setUmpire2TeamId(int umpire2TeamId) {
		this.umpire2TeamId = umpire2TeamId;
	}

	public int getUmpire1Id() {
		return umpire1Id;
	}

	public void setUmpire1Id(int umpire1Id) {
		this.umpire1Id = umpire1Id;
	}

	public int getUmpire2Id() {
		return umpire2Id;
	}

	public void setUmpire2Id(int umpire2Id) {
		this.umpire2Id = umpire2Id;
	}

	public String derivedUmpire1(){
		if(this.umpire1Id > 0){
			return "u-"+umpire1Id;
		}else if(this.umpire1TeamId > 0){
			return "t-"+umpire1TeamId;
		}else {
			return getUmpire1();
		}
	}
	public String derivedUmpire2(){
		if(this.umpire2Id > 0){
			return "u-"+umpire2Id;
		}else if(this.umpire2TeamId > 0){
			return "t-"+umpire2TeamId;
		}else {
			return getUmpire2();
		}
	}
	
	public String derivedUmpire1N(){
		if(this.umpire1Id > 0){
			return "u-"+umpire1Id;
		}else if(this.umpire1TeamId > 0){
			return "t-"+umpire1TeamId;
		}else {
			return "";
		}
	}
	public String derivedUmpire2N(){
		if(this.umpire2Id > 0){
			return "u-"+umpire2Id;
		}else if(this.umpire2TeamId > 0){
			return "t-"+umpire2TeamId;
		}else {
			return "";
		}
	}

	public String getScorerName() {
		return scorerName == null?"":scorerName;
	}

	public void setScorerName(String scorerName) {
		this.scorerName = scorerName;
	}

	public int getScorerId() {
		return scorerId;
	}

	public void setScorerId(int scorerId) {
		this.scorerId = scorerId;
	}
	public int getPaperScorerId() {
		return paperScorerId;
	}

	public void setPaperScorerId(int paperScorerId) {
		this.paperScorerId = paperScorerId;
	}

	@Override
	public String toString() {
		return "FixtureDto [fixtureId=" + fixtureId + ", matchID=" + matchID
				+ ", umpire1ID=" + umpire1Id + ", umpire2ID=" + umpire2Id + "]";
	}
}
