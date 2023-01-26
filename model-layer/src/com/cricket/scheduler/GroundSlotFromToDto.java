package com.cricket.scheduler;

import java.util.Date;

public class GroundSlotFromToDto {

	private int groundId;
	private int maxOvers;
	private String fixtureDate;
	private String fixtureTime;
	private Date fromDateTime;
	private Date toDateTime;
	private int fixtureId;
	
	public int getGroundId() {
		return groundId;
	}
	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}
	public int getMaxOvers() {
		return maxOvers;
	}
	public void setMaxOvers(int maxOvers) {
		this.maxOvers = maxOvers;
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
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}
	
}
