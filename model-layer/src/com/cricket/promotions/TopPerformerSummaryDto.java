/*
 * Created on Aug 2, 2021
 */
package com.cricket.promotions;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MadhuKiran K Rajuladevi
 * 
 *         Top Performance Summary DTO
 */
public class TopPerformerSummaryDto implements Serializable {

	private static final long serialVersionUID = -1106426335176562657L;
	
	private int playerId;
	private int teamId;
	private int seriesId;
	private int matchId;
	private int clubId;
	private String matchType;
	private Date match_date;
	private String matchDateStr;
	private String clubName;
	private String seriesName;
	private String matchTypeName; 
	private int total_points;
	  
	private int battingPoints;
	private int runsScored;
	private int ballsFaced;
	private int fours;
	private int sixers;
	  
	private int bowlingPoints;
	private int ballsBowled;
	private int runsGiven;
	private int wickets;
	private int maidens;
	  
	private int fielding_points;
	private int direct_ro;
	private int catches;
	private int stumpings;
	  
	private boolean isManOfTheMatch;
	private String t1_name;
	private String t2_name;	
	private String playerName;
	private float batting_strike_rate;
	private String oversBowled;
	private String country;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(int seriesId) {
		this.seriesId = seriesId;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public Date getMatch_date() {
		return match_date;
	}

	public void setMatch_date(Date match_date) {
		this.match_date = match_date;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getMatchTypeName() {
		return matchTypeName;
	}

	public void setMatchTypeName(String matchTypeName) {
		this.matchTypeName = matchTypeName;
	}

	public int getTotal_points() {
		return total_points;
	}

	public void setTotal_points(int total_points) {
		this.total_points = total_points;
	}

	public int getBattingPoints() {
		return battingPoints;
	}

	public void setBattingPoints(int battingPoints) {
		this.battingPoints = battingPoints;
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

	public int getBowlingPoints() {
		return bowlingPoints;
	}

	public void setBowlingPoints(int bowlingPoints) {
		this.bowlingPoints = bowlingPoints;
	}

	public int getBallsBowled() {
		return ballsBowled;
	}

	public void setBallsBowled(int ballsBowled) {
		this.ballsBowled = ballsBowled;
	}

	public int getRunsGiven() {
		return runsGiven;
	}

	public void setRunsGiven(int runsGiven) {
		this.runsGiven = runsGiven;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getMaidens() {
		return maidens;
	}

	public void setMaidens(int maidens) {
		this.maidens = maidens;
	}

	public int getFielding_points() {
		return fielding_points;
	}

	public void setFielding_points(int fielding_points) {
		this.fielding_points = fielding_points;
	}

	public int getDirect_ro() {
		return direct_ro;
	}

	public void setDirect_ro(int direct_ro) {
		this.direct_ro = direct_ro;
	}

	public int getCatches() {
		return catches;
	}

	public void setCatches(int catches) {
		this.catches = catches;
	}

	public int getStumpings() {
		return stumpings;
	}

	public void setStumpings(int stumpings) {
		this.stumpings = stumpings;
	}

	public boolean isManOfTheMatch() {
		return isManOfTheMatch;
	}

	public void setManOfTheMatch(boolean isManOfTheMatch) {
		this.isManOfTheMatch = isManOfTheMatch;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getT1_name() {
		return t1_name;
	}

	public void setT1_name(String t1_name) {
		this.t1_name = t1_name;
	}

	public String getT2_name() {
		return t2_name;
	}

	public void setT2_name(String t2_name) {
		this.t2_name = t2_name;
	}

	public float getBatting_strike_rate() {
		return batting_strike_rate;
	}

	public void setBatting_strike_rate(float batting_strike_rate) {
		this.batting_strike_rate = batting_strike_rate;
	}

	public String getOversBowled() {
		return oversBowled;
	}

	public void setOversBowled(String overs_bowled) {
		this.oversBowled = overs_bowled;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMatchDateStr() {
		return matchDateStr;
	}

	public void setMatchDateStr(String matchDateStr) {
		this.matchDateStr = matchDateStr;
	}

	
}
