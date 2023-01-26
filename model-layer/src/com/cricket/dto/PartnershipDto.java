package com.cricket.dto;

public class PartnershipDto {
	private int outPlayerId;
	private short outPlayerScore;
	private short outPlayerBalls;

	private int otherPlayerId;
	private short otherPlayerScore;
	private short otherPlayerBalls;

	private short teamTotal;
	private short teamBalls;
	private short partnershipTotalRuns;
	private short partnershipTotalBalls;
	
	private short overNumber;
	private byte ballNumber;
	private boolean isRetired;
	private boolean isOut = true;
	private String profilepic_file_path;
	
	
	public String getProfilepic_file_path() {
		return profilepic_file_path;
	}
	public void setProfilepic_file_path(String profilepic_file_path) {
		this.profilepic_file_path = profilepic_file_path;
	}
	public int getOutPlayerId() {
		return outPlayerId;
	}
	public void setOutPlayerId(int outPlayerId) {
		this.outPlayerId = outPlayerId;
	}
	public int getOtherPlayerId() {
		return otherPlayerId;
	}
	public void setOtherPlayerId(int otherPlayerId) {
		this.otherPlayerId = otherPlayerId;
	}
	public short getTeamTotal() {
		return teamTotal;
	}
	public void setTeamTotal(short teamTotal) {
		this.teamTotal = teamTotal;
	}
	public short getOverNumber() {
		return overNumber;
	}
	public void setOverNumber(short overNumber) {
		this.overNumber = overNumber;
	}
	public byte getBallNumber() {
		return ballNumber;
	}
	public void setBallNumber(byte ballNumber) {
		this.ballNumber = ballNumber;
	}
	public short getOutPlayerScore() {
		return outPlayerScore;
	}
	public void setOutPlayerScore(short outPlayerScore) {
		this.outPlayerScore = outPlayerScore;
	}
	public short getOutPlayerBalls() {
		return outPlayerBalls;
	}
	public void setOutPlayerBalls(short outPlayerBalls) {
		this.outPlayerBalls = outPlayerBalls;
	}
	public short getOtherPlayerScore() {
		return otherPlayerScore;
	}
	public void setOtherPlayerScore(short otherPlayerScore) {
		this.otherPlayerScore = otherPlayerScore;
	}
	public short getOtherPlayerBalls() {
		return otherPlayerBalls;
	}
	public void setOtherPlayerBalls(short otherPlayerBalls) {
		this.otherPlayerBalls = otherPlayerBalls;
	}
	public boolean isRetired() {
		return isRetired;
	}
	public void setRetired(boolean isRetired) {
		this.isRetired = isRetired;
	}
	public boolean isOut() {
		return isOut;
	}
	public void setOut(boolean isOut) {
		this.isOut = isOut;
	}
	public short getPartnershipTotalRuns() {
		return partnershipTotalRuns;
	}
	public void setPartnershipTotalRuns(short partnershipTotalRuns) {
		this.partnershipTotalRuns = partnershipTotalRuns;
	}
	public short getPartnershipTotalBalls() {
		return partnershipTotalBalls;
	}
	public void setPartnershipTotalBalls(short partnershipTotalBalls) {
		this.partnershipTotalBalls = partnershipTotalBalls;
	}
	public short getTeamBalls() {
		return teamBalls;
	}
	public void setTeamBalls(short teamBalls) {
		this.teamBalls = teamBalls;
	}
}
