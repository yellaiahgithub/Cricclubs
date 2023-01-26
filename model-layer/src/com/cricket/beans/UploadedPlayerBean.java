package com.cricket.beans;


public class UploadedPlayerBean {
	private int ccPlayerId;
	private String firstName;
	private String lastName;
	private String teamName;
	private String dob;
	private int teamId;
	private int internalClubId;
	private String internalClubName="";
	private String email;
	private String role;
	private String dateOfBirth;
	private String customId="";
	private boolean captain;
	private boolean viceCaptain;
	private String inValidReason;
	private boolean wicketKeeper;
	private String usaca;
	
	
	public int getCcPlayerId() {
		return ccPlayerId;
	}
	public void setCcPlayerId(int ccPlayerId) {
		this.ccPlayerId = ccPlayerId;
	}
	public String getUsaca() {
		return usaca;
	}
	public void setUsaca(String usaca) {
		this.usaca = usaca;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getInternalClubName() {
		return internalClubName;
	}
	public void setInternalClubName(String internalClubName) {
		this.internalClubName = internalClubName;
	}
	public int getInternalClubId() {
		return internalClubId;
	}
	public void setInternalClubId(int internalClubId) {
		this.internalClubId = internalClubId;
	}
	public boolean isWicketKeeper() {
		return wicketKeeper;
	}
	public void setWicketKeeper(boolean wicketKeeper) {
		this.wicketKeeper = wicketKeeper;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInValidReason() {
		return inValidReason;
	}
	public void setInValidReason(String inValidReason) {
		this.inValidReason = inValidReason;
	}
	public String getRole() {
		return role;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isCaptain() {
		return captain;
	}
	public void setCaptain(boolean captain) {
		this.captain = captain;
	}
	public boolean isViceCaptain() {
		return viceCaptain;
	}
	public void setViceCaptain(boolean viceCaptain) {
		this.viceCaptain = viceCaptain;
	}
	public String getCustomId() {
		return customId;
	}
	public void setCustomId(String customId) {
		this.customId = customId;
	}
}
