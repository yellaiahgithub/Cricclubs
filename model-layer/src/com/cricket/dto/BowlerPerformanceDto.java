
package com.cricket.dto;

public class BowlerPerformanceDto {
	
	private int noOfOvers;
	private int runs;
	private int wickets;
	private int extras;
	private String overs;
	private float economy;
	
	public int getNoOfOvers() {
		return noOfOvers;
	}
	public void setNoOfOvers(int noOfOvers) {
		this.noOfOvers = noOfOvers;
	}
	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}
	public int getWickets() {
		return wickets;
	}
	public void setWickets(int wickets) {
		this.wickets = wickets;
	}
	public int getExtras() {
		return extras;
	}
	public void setExtras(int extras) {
		this.extras = extras;
	}
	public String getOvers() {
		return overs;
	}
	public void setOvers(String overs) {
		this.overs = overs;
	}
	public float getEconomy() {
		return economy;
	}
	public void setEconomy(float economy) {
		this.economy = economy;
	}
	
}
