/* Created on Oct 9, 2019 */
package com.cricket.dto;

import java.util.Date;

public class AuditPlayerMergeDto {
	
	private int id;
	private int primaryClubId;
	private int secondaryClubId;
	private int primaryPlayerId;
	private int secondaryPlayerId;
	private int primaryUserId;
	private int secondaryUserId;
	private int primaryUmpireId;
	private int secondaryUmpireId;
	private String userClubIds;
	private String playerClubIds;
	private String userRoles;
	private String teamIds;
	private String internalClubIds;
	private String internalClubAdmins1;
	private String internalClubAdmins2;
	private String matchIds;
	private String commentIds;
	private Date mergeDateTtime;
	private int mergeBy;
	private int mergeStatus;
	private int unMergeStatus;
	private Date unMergeDateTtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrimaryClubId() {
		return primaryClubId;
	}
	public void setPrimaryClubId(int primaryClubId) {
		this.primaryClubId = primaryClubId;
	}
	public int getSecondaryClubId() {
		return secondaryClubId;
	}
	public void setSecondaryClubId(int secondaryClubId) {
		this.secondaryClubId = secondaryClubId;
	}
	public int getPrimaryPlayerId() {
		return primaryPlayerId;
	}
	public void setPrimaryPlayerId(int primaryPlayerId) {
		this.primaryPlayerId = primaryPlayerId;
	}
	public int getSecondaryPlayerId() {
		return secondaryPlayerId;
	}
	public void setSecondaryPlayerId(int secondaryPlayerId) {
		this.secondaryPlayerId = secondaryPlayerId;
	}
	public int getPrimaryUserId() {
		return primaryUserId;
	}
	public void setPrimaryUserId(int primaryUserId) {
		this.primaryUserId = primaryUserId;
	}
	public int getSecondaryUserId() {
		return secondaryUserId;
	}
	public void setSecondaryUserId(int secondaryUserId) {
		this.secondaryUserId = secondaryUserId;
	}
	public int getPrimaryUmpireId() {
		return primaryUmpireId;
	}
	public void setPrimaryUmpireId(int primaryUmpireId) {
		this.primaryUmpireId = primaryUmpireId;
	}
	public int getSecondaryUmpireId() {
		return secondaryUmpireId;
	}
	public void setSecondaryUmpireId(int secondaryUmpireId) {
		this.secondaryUmpireId = secondaryUmpireId;
	}
	public String getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
	public String getUserClubIds() {
		return userClubIds;
	}
	public void setUserClubIds(String userClubIds) {
		this.userClubIds = userClubIds;
	}
	public String getPlayerClubIds() {
		return playerClubIds;
	}
	public void setPlayerClubIds(String playerClubIds) {
		this.playerClubIds = playerClubIds;
	}
	public String getInternalClubIds() {
		return internalClubIds;
	}
	public void setInternalClubIds(String internalClubIds) {
		this.internalClubIds = internalClubIds;
	}
	public String getInternalClubAdmins1() {
		return internalClubAdmins1;
	}
	public void setInternalClubAdmins1(String internalClubAdmins1) {
		this.internalClubAdmins1 = internalClubAdmins1;
	}
	public String getInternalClubAdmins2() {
		return internalClubAdmins2;
	}
	public void setInternalClubAdmins2(String internalClubAdmins2) {
		this.internalClubAdmins2 = internalClubAdmins2;
	}
	public String getTeamIds() {
		return teamIds;
	}
	public void setTeamIds(String teamIds) {
		this.teamIds = teamIds;
	}	
	public String getMatchIds() {
		return matchIds;
	}
	public void setMatchIds(String matchIds) {
		this.matchIds = matchIds;
	}
	public String getCommentIds() {
		return commentIds;
	}
	public void setCommentIds(String commentIds) {
		this.commentIds = commentIds;
	}
	public Date getMergeDateTtime() {
		return mergeDateTtime;
	}
	public void setMergeDateTtime(Date mergeDateTtime) {
		this.mergeDateTtime = mergeDateTtime;
	}
	public int getMergeBy() {
		return mergeBy;
	}
	public void setMergeBy(int mergeBy) {
		this.mergeBy = mergeBy;
	}
	public int getMergeStatus() {
		return mergeStatus;
	}
	public void setMergeStatus(int mergeStatus) {
		this.mergeStatus = mergeStatus;
	}
	public int getUnMergeStatus() {
		return unMergeStatus;
	}
	public void setUnMergeStatus(int unMergeStatus) {
		this.unMergeStatus = unMergeStatus;
	}
	public Date getUnMergeDateTtime() {
		return unMergeDateTtime;
	}
	public void setUnMergeDateTtime(Date unMergeDateTtime) {
		this.unMergeDateTtime = unMergeDateTtime;
	}
	public boolean isMergeComplete() {
		if(this.mergeStatus == 1)
			return true;
		else
			return false;		
	}
	public boolean isUnMergeComplete() {
		if(this.unMergeStatus == 1)
			return true;
		else
			return false;		
	}
	
	
}
