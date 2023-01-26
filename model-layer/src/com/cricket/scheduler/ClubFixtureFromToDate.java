package com.cricket.scheduler;

import java.util.Date;

public class ClubFixtureFromToDate {

	private int clubId;
	private int clubId1;
	private int clubId2;
	private String fixtureDate;
	private String fixtureTime;
	private Date fromDateTime;
	private Date toDateTime;
	private int maxOvers;
	
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public int getClubId1() {
		return clubId1;
	}
	public void setClubId1(int clubId1) {
		this.clubId1 = clubId1;
	}
	public int getClubId2() {
		return clubId2;
	}
	public void setClubId2(int clubId2) {
		this.clubId2 = clubId2;
	}
	public String getFixtureDate() {
		return fixtureDate;
	}
	public void setFixtureDate(String fixtureDate) {
		this.fixtureDate = fixtureDate;
	}
	public String getFixtureTime() {
		return fixtureTime;
	}
	public void setFixtureTime(String fixtureTime) {
		this.fixtureTime = fixtureTime;
	}
	public Date getFromDateTime() {
		return fromDateTime;
	}
	public void setFromDateTime(Date fromDateTime) {
		this.fromDateTime = fromDateTime;
	}
	public Date getToDateTime() {
		return toDateTime;
	}
	public void setToDateTime(Date toDateTime) {
		this.toDateTime = toDateTime;
	}
	public int getMaxOvers() {
		return maxOvers;
	}
	public void setMaxOvers(int maxOvers) {
		this.maxOvers = maxOvers;
	}
	
}
