package com.cricket.dto;

import com.cricket.utility.CommonUtility;

public class PlayerBatBowlStatsDto {
	
	private int playerId;
	private String playerName;
	private int matches;
	private int runsScored;
	private int ballsFaced;
	private int fours;
	private int sixers;
	private int inningsBatting;
	private int notOuts;
	private int highestScore;
	private int twentyFives;
	private int fifties;
	private int seventyFives;
	private int hundreds;	
	private int runsGiven;
	private int ballsBowled;	
	private int fourWickets;
	private int fiveWickets;	
	private int inningsBowling;
	private int maidens;
	private int wickets;
	private int wides;
	private int noBalls;
	private int hattricks;
	private int catches;
	private String economy;
	private String seriesName;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getRunsScored() {
		return runsScored;
	}
	public void setRunsScored(int runsScored) {
		this.runsScored = runsScored;
	}
	public int getBallsFaced() {
		return ballsFaced;
	}
	public void setBallsFaced(int ballsFaced) {
		this.ballsFaced = ballsFaced;
	}
	public int getFours() {
		return fours;
	}
	public void setFours(int fours) {
		this.fours = fours;
	}
	public int getSixers() {
		return sixers;
	}
	public void setSixers(int sixers) {
		this.sixers = sixers;
	}
	public int getInningsBatting() {
		return inningsBatting;
	}
	public void setInningsBatting(int inningsBatting) {
		this.inningsBatting = inningsBatting;
	}
	public int getNotOuts() {
		return notOuts;
	}
	public void setNotOuts(int notOuts) {
		this.notOuts = notOuts;
	}
	public int getHighestScore() {
		return highestScore;
	}
	public void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}
	public int getTwentyFives() {
		return twentyFives;
	}
	public void setTwentyFives(int twentyFives) {
		this.twentyFives = twentyFives;
	}
	public int getFifties() {
		return fifties;
	}
	public void setFifties(int fifties) {
		this.fifties = fifties;
	}
	public int getSeventyFives() {
		return seventyFives;
	}
	public void setSeventyFives(int seventyFives) {
		this.seventyFives = seventyFives;
	}
	public int getHundreds() {
		return hundreds;
	}
	public void setHundreds(int hundreds) {
		this.hundreds = hundreds;
	}
	public int getRunsGiven() {
		return runsGiven;
	}
	public void setRunsGiven(int runsGiven) {
		this.runsGiven = runsGiven;
	}
	public int getBallsBowled() {
		return ballsBowled;
	}
	public void setBallsBowled(int ballsBowled) {
		this.ballsBowled = ballsBowled;
	}
	public int getFourWickets() {
		return fourWickets;
	}
	public void setFourWickets(int fourWickets) {
		this.fourWickets = fourWickets;
	}
	public int getFiveWickets() {
		return fiveWickets;
	}
	public void setFiveWickets(int fiveWickets) {
		this.fiveWickets = fiveWickets;
	}
	public int getInningsBowling() {
		return inningsBowling;
	}
	public void setInningsBowling(int inningsBowling) {
		this.inningsBowling = inningsBowling;
	}
	public int getMaidens() {
		return maidens;
	}
	public void setMaidens(int maidens) {
		this.maidens = maidens;
	}
	public int getWickets() {
		return wickets;
	}
	public void setWickets(int wickets) {
		this.wickets = wickets;
	}
	public int getWides() {
		return wides;
	}
	public void setWides(int wides) {
		this.wides = wides;
	}
	public int getNoBalls() {
		return noBalls;
	}
	public void setNoBalls(int noBalls) {
		this.noBalls = noBalls;
	}
	public int getHattricks() {
		return hattricks;
	}
	public void setHattricks(int hattricks) {
		this.hattricks = hattricks;
	}
	public int getCatches() {
		return catches;
	}
	public void setCatches(int catches) {
		this.catches = catches;
	}
	public String getEconomy() {
		return economy;
	}
	public void setEconomy(String economy) {
		this.economy = economy;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public int getMatches() {
		return matches;
	}
	public void setMatches(int matches) {
		this.matches = matches;
	}	
	public String getBattingAverage(){
		
		double batAvg = 0;
		if((this.inningsBatting - this.notOuts) != 0){
			batAvg = (double)this.runsScored/(this.inningsBatting - this.notOuts);
		} else  if((this.inningsBatting == this.notOuts)){
			return "--";
		} else{
			return "0.00";
		}
		return CommonUtility.Round(batAvg,2);
	}
	
	public String getBattingStrikeRate(){
		
		double sr = 0;
		if(this.ballsFaced != 0){
			sr = (double)this.runsScored/this.ballsFaced;
		}
		return CommonUtility.Round(sr*100,2);
	}
	
	public String getBowlingEconomy(int ballsInOver){
		
		double sr = 0;
		if(this.ballsBowled != 0){
			sr = (double)this.runsGiven/this.ballsBowled;
		}
		return CommonUtility.Round(sr*ballsInOver,2);
	}
	public String getBowlingAverage(){
		
		double average = 0;
		if(this.wickets != 0){
			average = (double)this.runsGiven/this.wickets;
		}else{
			return "0";
		}
		return CommonUtility.Round(average,2);
	}
	
	public String getBowlingStrikeRate(){
		
		double sr = 0;
		if(this.wickets != 0){
			sr = (double)this.ballsBowled/this.wickets;
		}
		return CommonUtility.Round(sr,1);
	}
	
}
