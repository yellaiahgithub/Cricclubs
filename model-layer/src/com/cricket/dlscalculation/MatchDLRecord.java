package com.cricket.dlscalculation;

public class MatchDLRecord {

	private int id;
	private int matchID;
	private int inningNum;
	private double maxOver;
	private double oversPlayed;
	private double oversLost;
	private double oversAtStartOfInning;
	private double oversRevised;
	private int wicketsLost;
	private int runs;
	private int runsDls;
	private double resTotal;
	private double resLost;
	private double resAvailable;
	private boolean isInteruption;
	
	public boolean isInteruption() {
		return isInteruption;
	}
	public void setInteruption(boolean isInteruption) {
		this.isInteruption = isInteruption;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMatchID() {
		return matchID;
	}
	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}
	public int getInningNum() {
		return inningNum;
	}
	public void setInningNum(int inningNum) {
		this.inningNum = inningNum;
	}
	public double getMaxOver() {
		return maxOver;
	}
	public void setMaxOver(double maxOver) {
		this.maxOver = maxOver;
	}
	public double getOversPlayed() {
		return oversPlayed;
	}
	public void setOversPlayed(double oversPlayed) {
		this.oversPlayed = oversPlayed;
	}
	public double getOversLost() {
		return oversLost;
	}
	public void setOversLost(double oversLost) {
		this.oversLost = oversLost;
	}
	public double getOversAtStartOfInning() {
		return oversAtStartOfInning;
	}
	public void setOversAtStartOfInning(double oversAtStartOfInning) {
		this.oversAtStartOfInning = oversAtStartOfInning;
	}
	public double getOversRevised() {
		return oversRevised;
	}
	public void setOversRevised(double oversRevised) {
		this.oversRevised = oversRevised;
	}
	public int getWicketsLost() {
		return wicketsLost;
	}
	public void setWicketsLost(int wicketsLost) {
		this.wicketsLost = wicketsLost;
	}
	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}
	public int getRunsDls() {
		return runsDls;
	}
	public void setRunsDls(int runsDls) {
		this.runsDls = runsDls;
	}
	public double getResTotal() {
		return resTotal;
	}
	public void setResTotal(double resTotal) {
		this.resTotal = resTotal;
	}
	public double getResLost() {
		return resLost;
	}
	public void setResLost(double resLost) {
		this.resLost = resLost;
	}
	public double getResAvailable() {
		return resAvailable;
	}
	public void setResAvailable(double resAvailable) {
		this.resAvailable = resAvailable;
	}
	
}
