package com.cricket.scheduler;

import java.util.Map;


public class FixtureMetric {

	int homeMatch;
	int awayMatch;
	int totalMatch;
	Map<Integer, Integer> matchForDay;
	
	public int getHomeMatch() {
		return homeMatch;
	}
	public void setHomeMatch(int homeMatch) {
		this.homeMatch = homeMatch;
	}
	public int getAwayMatch() {
		return awayMatch;
	}
	public void setAwayMatch(int awayMatch) {
		this.awayMatch = awayMatch;
	}
	public int getTotalMatch() {
		return totalMatch;
	}
	public void setTotalMatch(int totalMatch) {
		this.totalMatch = totalMatch;
	}
	public Map<Integer, Integer> getMatchForDay() {
		return matchForDay;
	}
	public void setMatchForDay(Map<Integer, Integer> matchForDay) {
		this.matchForDay = matchForDay;
	}
	@Override
	public String toString() {
		return "FixtureMetric [homeMatch=" + homeMatch + ", awayMatch="
				+ awayMatch + ", totalMatch=" + totalMatch + ", matchForDay="
				+ matchForDay + "]";
	}
}