package com.cricket.beans;


public class UploadedTeamBean {
	private String teamName;
	private int group;
	private String inValidReason;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getInValidReason() {
		return inValidReason;
	}
	public void setInValidReason(String inValidReason) {
		this.inValidReason = inValidReason;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
}
