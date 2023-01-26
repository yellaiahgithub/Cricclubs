/*
 * Created on Aug 30, 2021
 */
package com.cricket.promotions;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MadhuKiran K Rajuladevi
 * 
 *         Career Stats Summary DTO
 */
public class CareerStatsSummaryDto implements Serializable {

	private static final long serialVersionUID = -1106426335176562627L;

	private int playerId;
	private int clubId;
	private String clubName;
	private String playerName;
	private int totalMatches;
	private int totalBattingInnings;
	private int totalBowlingInnings;
	
	private int totalRunsScored;
	private int totalBallsFaced;
	private int highestRuns;
	private int totalHundreads;
	private int totalFifties;
	private int totalNotOuts;
	private int totalFours;
	private int totalSixers;
	  
	private int totalBallsBowled;
	private int totalRunsGiven;
	private int totalWickets;
	  
	private int totalCatches;
	private int totalMaidens;
	private int totalRunOuts;
	
	private Date lastUpdated;
	private String country;
	private String lastUpdatedStr;
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getTotalMatches() {
		return totalMatches;
	}
	public void setTotalMatches(int totalMatches) {
		this.totalMatches = totalMatches;
	}
	public int getTotalBattingInnings() {
		return totalBattingInnings;
	}
	public void setTotalBattingInnings(int totalBattingInnings) {
		this.totalBattingInnings = totalBattingInnings;
	}
	public int getTotalBowlingInnings() {
		return totalBowlingInnings;
	}
	public void setTotalBowlingInnings(int totalBowlingInnings) {
		this.totalBowlingInnings = totalBowlingInnings;
	}
	public int getTotalRunsScored() {
		return totalRunsScored;
	}
	public void setTotalRunsScored(int totalRunsScored) {
		this.totalRunsScored = totalRunsScored;
	}
	public int getTotalBallsFaced() {
		return totalBallsFaced;
	}
	public void setTotalBallsFaced(int totalBallsFaced) {
		this.totalBallsFaced = totalBallsFaced;
	}
	public int getHighestRuns() {
		return highestRuns;
	}
	public void setHighestRuns(int highestRuns) {
		this.highestRuns = highestRuns;
	}
	public int getTotalHundreads() {
		return totalHundreads;
	}
	public void setTotalHundreads(int totalHundreads) {
		this.totalHundreads = totalHundreads;
	}
	public int getTotalFifties() {
		return totalFifties;
	}
	public void setTotalFifties(int totalFifties) {
		this.totalFifties = totalFifties;
	}
	public int getTotalNotOuts() {
		return totalNotOuts;
	}
	public void setTotalNotOuts(int totalNotOuts) {
		this.totalNotOuts = totalNotOuts;
	}
	public int getTotalFours() {
		return totalFours;
	}
	public void setTotalFours(int totalFours) {
		this.totalFours = totalFours;
	}
	public int getTotalSixers() {
		return totalSixers;
	}
	public void setTotalSixers(int totalSixers) {
		this.totalSixers = totalSixers;
	}
	public int getTotalBallsBowled() {
		return totalBallsBowled;
	}
	public void setTotalBallsBowled(int totalBallsBowled) {
		this.totalBallsBowled = totalBallsBowled;
	}
	public int getTotalRunsGiven() {
		return totalRunsGiven;
	}
	public void setTotalRunsGiven(int totalRunsGiven) {
		this.totalRunsGiven = totalRunsGiven;
	}
	public int getTotalWickets() {
		return totalWickets;
	}
	public void setTotalWickets(int totalWickets) {
		this.totalWickets = totalWickets;
	}
	public int getTotalCatches() {
		return totalCatches;
	}
	public void setTotalCatches(int totalCatches) {
		this.totalCatches = totalCatches;
	}
	public int getTotalMaidens() {
		return totalMaidens;
	}
	public void setTotalMaidens(int totalMaidens) {
		this.totalMaidens = totalMaidens;
	}
	public int getTotalRunOuts() {
		return totalRunOuts;
	}
	public void setTotalRunOuts(int totalRunOuts) {
		this.totalRunOuts = totalRunOuts;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLastUpdatedStr() {
		return lastUpdatedStr;
	}
	public void setLastUpdatedStr(String lastUpdatedStr) {
		this.lastUpdatedStr = lastUpdatedStr;
	}
}
