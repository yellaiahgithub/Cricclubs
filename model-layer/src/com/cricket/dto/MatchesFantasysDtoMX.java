package com.cricket.dto;

import java.util.Date;
import java.util.List;

public class MatchesFantasysDtoMX {	
	
	private int 	matchId;
	private String 	matchName;
	private String 	seriesName;
	private Date 	matchDate;
	private String 	matchTime;
	List<ContestDtoMX> contestList;
	
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public Date getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public List<ContestDtoMX> getContestList() {
		return contestList;
	}
	public void setContestList(List<ContestDtoMX> contestList) {
		this.contestList = contestList;
	}
	
}
