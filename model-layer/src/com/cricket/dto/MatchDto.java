/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.MatchDLRecordsFactory;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.utility.CommonLogic;
import com.cricket.utility.CommonUtility;


/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MatchDto implements Serializable {
	
	public static final String ABANDONE_TYPE_ABANDONED = "abandoned";
	public static final String ABANDONE_TYPE_SUPEROVER = "superover";
	public static final String ABANDONE_TYPE_FOREFEIT = "forefeit";
	public static final String ABANDONE_TYPE_DRAW = "draw";
	
	private static final long serialVersionUID = -8414886581160270474L;
	private int clubID;
	private String clubName;
	private int matchID;
	private int teamOne;
	private String teamOneName;
	private String teamOneCode;
	private int teamOneCaptain;
	private int teamOneViceCaptain;
	private int teamTwo;
	private String teamTwoName;
	private String teamTwoCode;
	private int teamTwoCaptain;
	private int teamTwoViceCaptain;
	private int tossWon;
	private int battingFirst;
	private List<Integer> players1;
	private List<Integer> players2;
	private List<PlayerLiveScoringDto> newPlayers1;
	private List<PlayerLiveScoringDto> newPlayers2;
	private int overs;
	private int winner;
	private String location;
	private String matchDate;
	private int t1total;
	private int t2total;
	private int t1balls;
	private int t2balls;
	private int t1wickets;
	private int t2wickets;
	private int t1_1total;
	private int t2_1total;
	private int t1_1balls;
	private int t2_1balls;
	private int t1_1wickets;
	private int t2_1wickets;
	private int t1_2total;
	private int t2_2total;
	private int t1_2balls;
	private int t2_2balls;
	private int t1_2wickets;
	private int t2_2wickets;
	private int hasInnings;
	private int t1penalty;
	private int t2penalty;
	private int t1byes;
	private int t2byes;
	private int t1lbyes;
	private int t2lbyes;
	private int t1Wides;
	private int t2Wides;
	private int t1noballs;
	private int t2noballs;
	private int t1hattricks;
	private int t2hattricks;
	private int t1_1penalty;
	private int t2_1penalty;
	private int t1_1byes;
	private int t2_1byes;
	private int t1_1lbyes;
	private int t2_1lbyes;
	private int t1_1Wides;
	private int t2_1Wides;
	private int t1_1noballs;
	private int t2_1noballs;
	private int t1_1hattricks;
	private int t2_1hattricks;
	private int t1_2penalty;
	private int t2_2penalty;
	private int t1_2byes;
	private int t2_2byes;
	private int t1_2lbyes;
	private int t2_2lbyes;
	private int t1_2Wides;
	private int t2_2Wides;
	private int t1_2noballs;
	private int t2_2noballs;
	private int t1_2hattricks;
	private int t2_2hattricks;
	private int manOfTheMatch;
	private String matchType;
	private int isComplete;
	private int leagueId;
	private String comment = "";
	private int isAbandoned;
	private int isLocked;
	private String notificationSent = "";
	private String abandoneType = "";
	private int scorer;
	private String umpire1 = "";
	private String umpire2 = "";
	private boolean isUmpire1Report;
	private boolean isUmpire2Report;
	private String umpire1UserId;
	private String umpire2UserId;
	private String fow1 = "";
	private String fow2 = "";
	private String fow1_2 = "";
	private String fow2_2 = "";
	private Date lastUpdatedDate;
	private int lastUpdatedBy;
	private String lastUpdatedByName;
	private int maximumPlayers;
	private String liveURL = "";
	private String result;
	private String timeSinceLastUpdate;
	private String t1_logo_file_path;
	private String t2_logo_file_path;
	private String live_streaming_link;
	private int isFollowon;
	private String seriesType;
	private int isTrump;
	private int t2Target;
	private boolean isDls;
	private double r1ResAvailable;
	private int t2RevisedOvers;
	private float t2RevisedOversF;
	int newT1Total = 0;
	float newOvers = 0;
	private String startTime;
	private String endTime;
	private String seriesName;
	private boolean isMatchSummary;

	private String lastWicket;
	private String runsEquation;
	private String runRate;
	private String status="";
	private String isFollowonStr="";
	String leadTrailByEquation = "";
	private int currentInnings;
	private int superOverCurrentInnings;
	private String srcSite;
	private int	srcLeagueId;
	private long srcMatchId;
	private int t1PlayerSize;
	private int t2PlayerSize;
	
	public String getRunRate() {
		return runRate;
	}

	public void setRunRate(String runRate) {
		this.runRate = runRate;
	}

	public float getT2RevisedOversF() {
		return t2RevisedOversF;
	}

	public void setT2RevisedOversF(float t2RevisedOversF) {
		this.t2RevisedOversF = t2RevisedOversF;
		setT2RevisedOvers(CommonUtility.oversToBalls(t2RevisedOversF+""));
	}

	public String getRunsEquation() {		
		return runsEquation;
	}
	
	public void setRunsEquation(String runsEquation) {
		this.runsEquation = runsEquation;
	}
		
	public String getLastWicket() {
		return lastWicket;
	}

	public void setLastWicket(String lastWicket) {
		this.lastWicket = lastWicket;
	}
	
	public boolean isMatchSummary() {
		return isMatchSummary;
	}

	public void setMatchSummary(boolean isMatchSummary) {
		this.isMatchSummary = isMatchSummary;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	
	public float getT2RevisedOvers() {
		return CommonUtility.stringToFloat(CommonUtility.ballsToOvers(t2RevisedOvers));
	}

	public void setT2RevisedOvers(int t2RevisedOvers) {
		this.t2RevisedOvers = t2RevisedOvers;
	}

	public double getR1ResAvailable() {
		return r1ResAvailable;
	}

	public void setR1ResAvailable(double r1ResAvailable) {
		this.r1ResAvailable = r1ResAvailable;
	}

	public int getT2Target() {
		return t2Target;
	}

	public void setT2Target(int t2Target) {
		this.t2Target = t2Target;
	}

	public boolean isDls() {
		return isDls;
	}

	public void setDls(boolean isDls) {
		this.isDls = isDls;
	}

	public String getSeriesType() {
		return seriesType;
	}

	public void setSeriesType(String seriesType) {
		this.seriesType = seriesType;
	}

	public boolean isUmpire1Report() {
		return isUmpire1Report;
	}

	public void setUmpire1Report(boolean isUmpire1Report) {
		this.isUmpire1Report = isUmpire1Report;
	}

	public boolean isUmpire2Report() {
		return isUmpire2Report;
	}

	public String getUmpire1UserId() {
		return umpire1UserId;
	}

	public void setUmpire1UserId(String umpire1UserId) {
		this.umpire1UserId = umpire1UserId;
	}

	public String getUmpire2UserId() {
		return umpire2UserId;
	}

	public void setUmpire2UserId(String umpire2UserId) {
		this.umpire2UserId = umpire2UserId;
	}

	public void setUmpire2Report(boolean isUmpire2Report) {
		this.isUmpire2Report = isUmpire2Report;
	}

	public int getIsFollowon() {
		return isFollowon;
	}

	public void setIsFollowon(int isFollowon) {
		this.isFollowon = isFollowon;
	}

	public int getClubID() {
		return clubID;
	}

	public void setClubID(int clubID) {
		this.clubID = clubID;
	}

	public String getTeamOneCode() {
		return teamOneCode;
	}

	public void setTeamOneCode(String teamOneCode) {
		this.teamOneCode = teamOneCode;
	}

	public String getTeamTwoCode() {
		return teamTwoCode;
	}

	public void setTeamTwoCode(String teamTwoCode) {
		this.teamTwoCode = teamTwoCode;
	}

	public String getLive_streaming_link() {
		return live_streaming_link;
	}

	public void setLive_streaming_link(String live_streaming_link) {
		this.live_streaming_link = live_streaming_link;
	}

	public String getT1_logo_file_path() {
		return t1_logo_file_path;
	}

	public void setT1_logo_file_path(String t1_logo_file_path) {
		this.t1_logo_file_path = t1_logo_file_path;
	}

	public String getT2_logo_file_path() {
		return t2_logo_file_path;
	}

	public void setT2_logo_file_path(String t2_logo_file_path) {
		this.t2_logo_file_path = t2_logo_file_path;
	}

	public String getUmpire2() {
		return umpire2 == null ? "" : umpire2;
	}

	public void setUmpire2(String umpire2) {
		this.umpire2 = umpire2;
	}

	public String getUmpire1() {
		return umpire1 == null ? "" : umpire1;
	}

	public void setUmpire1(String umpire1) {
		this.umpire1 = umpire1;
	}

	public String getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(String notificationSent) {
		this.notificationSent = notificationSent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getIsAbandoned() {
		return isAbandoned;
	}

	public void setIsAbandoned(int isAbandoned) {
		this.isAbandoned = isAbandoned;
	}

	public int getHasInnings() {
		return hasInnings;
	}

	public void setHasInnings(int hasInnings) {
		this.hasInnings = hasInnings;
	}

	public int getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	/**
	 * @return
	 */
	public int getBattingFirst() {
		if (this.battingFirst == 0) {
			return this.teamOne;
		} else {
			return this.battingFirst;
		}
	}

	/**
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return
	 */
	public int getMatchID() {
		return matchID;
	}

	/**
	 * @return
	 */
	public int getOvers() {
		return overs;
	}

	/**
	 * @return
	 */
	public int getTeamOne() {
		return teamOne;
	}

	/**
	 * @return
	 */
	public int getTeamTwo() {
		return teamTwo;
	}

	/**
	 * @return
	 */
	public int getTossWon() {
		return tossWon;
	}

	/**
	 * @return
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * @param l
	 */
	public void setBattingFirst(int l) {
		battingFirst = l;
	}

	/**
	 * @param string
	 */
	public void setLocation(String string) {
		location = string;
	}

	/**
	 * @param l
	 */
	public void setMatchID(int l) {
		matchID = l;
	}

	/**
	 * @param i
	 */
	public void setOvers(int i) {
		overs = i;
	}

	/**
	 * @param l
	 */
	public void setTeamOne(int l) {
		teamOne = l;
	}

	/**
	 * @param l
	 */
	public void setTeamTwo(int l) {
		teamTwo = l;
	}

	/**
	 * @param l
	 */
	public void setTossWon(int l) {
		tossWon = l;
	}

	/**
	 * @param l
	 */
	public void setWinner(int l) {
		winner = l;
	}

	/**
	 * @return
	 */
	public int getTeamOneCaptain() {
		return teamOneCaptain;
	}

	/**
	 * @return
	 */
	public int getTeamOneViceCaptain() {
		return teamOneViceCaptain;
	}

	/**
	 * @return
	 */
	public int getTeamTwoCaptain() {
		return teamTwoCaptain;
	}

	/**
	 * @return
	 */
	public int getTeamTwoViceCaptain() {
		return teamTwoViceCaptain;
	}

	/**
	 * @param l
	 */
	public void setTeamOneCaptain(int l) {
		teamOneCaptain = l;
	}

	/**
	 * @param l
	 */
	public void setTeamOneViceCaptain(int l) {
		teamOneViceCaptain = l;
	}

	/**
	 * @param l
	 */
	public void setTeamTwoCaptain(int l) {
		teamTwoCaptain = l;
	}

	/**
	 * @param l
	 */
	public void setTeamTwoViceCaptain(int l) {
		teamTwoViceCaptain = l;
	}

	/**
	 * @return
	 */
	public String getMatchDate() {
		return matchDate;
	}

	/**
	 * @param string
	 */
	public void setMatchDate(String string) {
		matchDate = string;
	}

	/**
	 * @return
	 */
	public int getT1balls() {
		return t1balls;
	}

	/**
	 * @return
	 */
	public int getT1byes() {
		return t1byes;
	}

	/**
	 * @return
	 */
	public int getT1lbyes() {
		return t1lbyes;
	}

	/**
	 * @return
	 */
	public int getT1total() {
		return t1total;
	}

	/**
	 * @return
	 */
	public int getT1wickets() {
		return t1wickets;
	}

	/**
	 * @return
	 */
	public int getT2balls() {
		return t2balls;
	}

	/**
	 * @return
	 */
	public String getT1overs() {
		
		int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t1balls != 0) {
			overs = "" + (int) this.t1balls / ballsInOver;
			overs += "." + (this.t1balls % ballsInOver);
		}
		return overs;
	}

	/**
	 * @return
	 */
	public String getT2overs() {
		
		int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t2balls != 0) {
			overs = "" + (int) this.t2balls / ballsInOver;
			overs += "." + (this.t2balls % ballsInOver);
		}
		return overs;
	}

	/**
	 * @return
	 */
	public String getT1_1overs() {
int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t1_1balls != 0) {
			overs = "" + (int) this.t1_1balls / ballsInOver;
			overs += "." + (this.t1_1balls % ballsInOver);
		}
		return overs;
	}

	/**
	 * @return
	 */
	public String getT2_1overs() {
int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t2_1balls != 0) {
			overs = "" + (int) this.t2_1balls / ballsInOver;
			overs += "." + (this.t2_1balls % ballsInOver);
		}
		return overs;
	}

	/**
	 * @return
	 */
	public String getT1_2overs() {
int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t1_2balls != 0) {
			overs = "" + (int) this.t1_2balls / ballsInOver;
			overs += "." + (this.t1_2balls % ballsInOver);
		}
		return overs;
	}

	/**
	 * @return
	 */
	public String getT2_2overs() {
int ballsInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())) {
			ballsInOver = 5;
		}
		
		String overs = "0.0";
		if (this.t2_2balls != 0) {
			overs = "" + (int) this.t2_2balls / ballsInOver;
			overs += "." + (this.t2_2balls % ballsInOver);
		}
		return overs;
	}
	public String getRunsNeededEquation() {
		String str = "";
		
		int totalBallInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())){
			totalBallInOver = 5;
		}
		
		float t2TargetOvers = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2RevisedOvers() : this.getOvers();
		int t2TargetRuns = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2Target() : this.getT1total();	
		
		int runs = t2TargetRuns - this.getT2total();	
		
		if(!this.isDls()) {
			runs = runs+1;
		}		
		
		int ballsRemaining = CommonUtility.oversToBalls(t2TargetOvers+"", totalBallInOver)- this.getT2balls();
		String overs = CommonUtility.ballsToOvers(ballsRemaining, totalBallInOver);			
		
		int wickets = this.getT2TotalWickets() - this.getT2wickets();
		
		String rrr = CommonUtility.calculateRunRate(runs,(double)(ballsRemaining)/totalBallInOver);	
		str = runs + " runs needed in ";
		if("100b".equalsIgnoreCase(this.getSeriesType())){
			str += + ballsRemaining +" balls with " + wickets + " wickets remaining. RRR: "+ rrr;
		}else {
			str += overs + " overs "+ "(" + ballsRemaining +" balls) with " + wickets + " wickets remaining. RRR: "+ rrr;
		}
		return str;
	}
	
	public String getRunRateForMatch() {
		String str = "";
		
		int totalBallInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())){
			totalBallInOver = 5;
		}
		
		float t2TargetOvers = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2RevisedOvers() : this.getOvers();
		int t2TargetRuns = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2Target() : this.getT1total();	
		
		int runs = t2TargetRuns - this.getT2total();	
		
		if(!this.isDls()) {
			runs = runs+1;
		}		
		
		int ballsRemaining = CommonUtility.oversToBalls(t2TargetOvers+"", totalBallInOver)- this.getT2balls();
		
		String rrr = CommonUtility.calculateRunRate(runs,(double)(ballsRemaining)/totalBallInOver);			
		return rrr;
	}
	
	public String getRunsNeededEquation(OverDto over) {
		
		String str = "";
		float t2TargetOvers = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2RevisedOvers() : this.getOvers();
		int t2TargetRuns = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2Target() : this.getT1total();			
	
		if (over != null) {			
			int runs = t2TargetRuns - over.getMatchTotal();		
			if(!this.isDls()) {
				runs = runs+1;
			}		
			float overs = t2TargetOvers - over.getOverNumber()-1;
			int balls = CommonUtility.oversToBalls(overs+"");
			int wickets = this.getT2TotalWickets() - over.getMatchWickets();
			String rrr = CommonUtility.calculateRunRate(runs,(double)(balls)/6);			
			str = runs + " runs needed in " + overs + " overs "+ "(" + balls +" balls) with " + wickets + " wickets remaining. RRR: "+ rrr;		
		}
			return str;		
	}
	public String getRunsNeededEquation(OverDto over, String seriesType) {
		
		String str = "";
		float t2TargetOvers = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2RevisedOvers() : this.getOvers();
		int t2TargetRuns = this.isDls() && this.getT2RevisedOvers() > 0 ? this.getT2Target() : this.getT1total();			
	
		if (over != null) {			
			int runs = t2TargetRuns - over.getMatchTotal();		
			if(!this.isDls()) {
				runs = runs+1;
			}		
			float overs = t2TargetOvers - over.getOverNumber()-1;
			int balls = CommonUtility.oversToBalls(overs+"");
			int wickets = this.getT2TotalWickets() - over.getMatchWickets();
			String rrr = CommonUtility.calculateRunRate(runs,(double)(balls)/6);	
			if("100b".equals(seriesType)) {
				str = runs + " runs needed off "+ CommonUtility.oversToBalls(overs+"."+balls, 5) +" balls";		
			}else {
				str = runs + " runs needed in " + overs + " overs "+ "(" + balls +" balls) with " + wickets + " wickets remaining. RRR: "+ rrr;
			}
		}
			return str;		
	}
	/**
	 * @return
	 */
	public int getT2byes() {
		return t2byes;
	}

	/**
	 * @return
	 */
	public int getT2lbyes() {
		return t2lbyes;
	}

	/**
	 * @return
	 */
	public int getT2total() {
		return t2total;
	}

	/**
	 * @return
	 */
	public int getT2wickets() {
		return t2wickets;
	}

	/**
	 * @param l
	 */
	public void setT1balls(int l) {
		t1balls = l;
	}

	/**
	 * @param l
	 */
	public void setT1byes(int l) {
		t1byes = l;
	}

	/**
	 * @param l
	 */
	public void setT1lbyes(int l) {
		t1lbyes = l;
	}

	/**
	 * @param l
	 */
	public void setT1total(int l) {
		t1total = l;
	}

	/**
	 * @param l
	 */
	public void setT1wickets(int l) {
		t1wickets = l;
	}

	/**
	 * @param l
	 */
	public void setT2balls(int l) {
		t2balls = l;
	}

	/**
	 * @param l
	 */
	public void setT2byes(int l) {
		t2byes = l;
	}

	/**
	 * @param l
	 */
	public void setT2lbyes(int l) {
		t2lbyes = l;
	}

	/**
	 * @param l
	 */
	public void setT2total(int l) {
		t2total = l;
	}

	/**
	 * @param l
	 */
	public void setT2wickets(int l) {
		t2wickets = l;
	}

	public String getTeamScoresString() {
		int totalBallInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())){
			totalBallInOver = 5;
		}
		
		
		String result = "";
		if ("Test".equalsIgnoreCase(this.getSeriesType())) {
			if (this.teamOne == this.battingFirst) {
				result = this.teamOneName + ": " + (this.t1_1total) + "/" + (this.t1_1wickets) + " - "
						+ (this.t1_2total) + "/" + (this.t1_2wickets);
				if ((this.t2balls) > 0) {
					result += "<BR />" + this.teamTwoName + ": " + (this.t2_1total) + "/" + (this.t2_1wickets) + " - "
							+ (this.t2_2total) + "/" + (this.t2_2wickets);
				}
			} else {
				result = this.teamTwoName + ": " + (this.t1_1total) + "/" + (this.t1_1wickets) + " - "
						+ (this.t1_2total) + "/" + (this.t1_2wickets);
				if ((this.t2balls) > 0) {
					result += "<BR />" + this.teamOneName + ": " + (this.t2total) + "/" + (this.t2wickets) + " - "
							+ (this.t2total) + "/" + (this.t2wickets);
				}
			}
			if (this.isLiveMatch() && !this.isInProgress()) {
				result += "<img alt='Ball By Ball Coverage' title='Ball By Ball Coverage' src='"
						+ CommonUtility.getCDN() + "/images/bowler.png' />";
			}
			if (this.isLiveMatch() && this.isInProgress()) {
				result += "&nbsp;&nbsp;<span class=\"label label-success\">Live</span>";
			}
		} else {

			if (this.teamOne == this.battingFirst) {
				result = this.teamOneName + ": " + (this.t1total) + "/" + (this.t1wickets) + "("
						+ CommonUtility.ballsToOvers((int) (this.t1balls), totalBallInOver) + ")";
				if ((this.t2balls) > 0) {
					result += "<BR />" + this.teamTwoName + ": " + (this.t2total) + "/" + (this.t2wickets) + "("
							+ CommonUtility.ballsToOvers((int) (this.t2balls), totalBallInOver) + ")";
				}
			} else {
				result = this.teamTwoName + ": " + (this.t1total) + "/" + (this.t1wickets) + "("
						+ CommonUtility.ballsToOvers((int) (this.t1balls), totalBallInOver) + ")  ";
				if ((this.t2balls) > 0) {
					result += "<BR />" + this.teamOneName + ": " + (this.t2total) + "/" + (this.t2wickets) + "("
							+ CommonUtility.ballsToOvers((int) (this.t2balls), totalBallInOver) + ")";
				}
			}
			if (this.isLiveMatch() && !this.isInProgress()) {
				result += "<img alt='Ball By Ball Coverage' title='Ball By Ball Coverage' src='"
						+ CommonUtility.getCDN() + "/images/bowler.png' />";
			}
			if (this.isLiveMatch() && this.isInProgress()) {
				result += "&nbsp;&nbsp;<span class=\"label label-success\">Live</span>";
			}

		}
		return result;
	}

	public String getResult() {
		return this.getResult(false);
	}

	public String getResultForTitle() {
		return this.getResult(true);
	}

	public String getResultFB()
	{
		String result = this.teamOneName+" "+this.t1total+" - "+this.t2total+" "+this.teamTwoName;		
		if(this.winner == this.teamTwo){
			result = this.teamTwoName+" "+this.t2total+" - "+this.t1total+" "+this.teamOneName;
		}
		return result;
	}
	
	
	private String getResult(boolean shortTeamName) {

		String result = "";
		
		int totalBallInOver = 6;
		
		if("100b".equalsIgnoreCase(this.getSeriesType())){
			totalBallInOver = 5;
		}
		
		if ("Test".equalsIgnoreCase(this.seriesType)) {

			if (!CommonUtility.isNullOrEmpty(abandoneType)) {
				if (ABANDONE_TYPE_ABANDONED.equals(abandoneType)) {
					result += "Abandoned.";
				} else if (ABANDONE_TYPE_FOREFEIT.equals(abandoneType)) {
					result += "Forfeited.";
				} else if (ABANDONE_TYPE_SUPEROVER.equals(abandoneType)) {
					result += "Super Over.";
				} else if (ABANDONE_TYPE_DRAW.equals(abandoneType)) {
					result += "Draw.";
				} else if (winner == 0) {
					result += "No Result";
				}
				if (winner != 0) {
					if (this.winner == this.teamOne) {
						result += " Winner: "
								+ (shortTeamName ? CommonUtility.trimTeamName(teamOneName, this.teamOneCode)
										: teamOneName);
					} else if (this.winner == this.teamTwo) {
						result += " Winner: "
								+ (shortTeamName ? CommonUtility.trimTeamName(teamTwoName, this.teamTwoCode)
										: teamTwoName);
					}
				}
			} else {

				if (this.winner != 0 && this.isComplete == 1) {
					if (this.winner == this.teamOne) {
						result += (shortTeamName ? CommonUtility.trimTeamName(teamOneName, this.teamOneCode)
								: teamOneName) + " won by ";
					} else {
						result += (shortTeamName ? CommonUtility.trimTeamName(teamTwoName, this.teamTwoCode)
								: teamTwoName) + " won by ";
					}
					if (this.getIsFollowon() == 1) {
						if (this.winner == this.battingFirst) {
							if (this.maximumPlayers > 0 && this.maximumPlayers < 11) {
								if (this.t1_2balls > 0 || this.t1_2total > 0) {
									result += (this.maximumPlayers - (this.t1_2wickets) - 1) + " Wkt(s)";
								} else {
									result += "Innings and " + (this.t1total - t2total) + " Run(s)";
								}
							} else {
								if (this.t1_2balls > 0 || this.t1_2total > 0) {
									result += ((10 - (this.t1_2wickets))) + " Wkt(s)";
								} else {
									result += "Innings and " + ((this.t1total - t2total)) + " Run(s)";
								}
								// result += (10 - (this.t1_2wickets)) + " Wkt(s)";
							}
						} else {
							// TODO check for second team.
							result += ((this.t2total) - (this.t1total)) + " Run(s)";
						}
					} else {
						if (this.winner == this.battingFirst) {
							result += ((this.t1total) - (this.t2total)) + " Run(s)";
						} else {
							if (this.t2_2balls > 0 && this.t2_2total > 0) {
								if (this.maximumPlayers > 0 && this.maximumPlayers < 11) {
									result += (this.maximumPlayers - (this.t2_2wickets) - 1) + " Wkt(s)";
								} else {
									result += ((10 - (this.t2_2wickets))) + " Wkt(s)";
								}
							} else {
								result += "Innings and " + (t2total - t1total) + "Run(s)";
							}
						}
					}

				} else if (this.winner == 0 && this.isComplete == 1) {
					if (this.t1total == this.t2total) {
						result += "Match Tie";
					} else {
						result += "Match Draw";
					}
				} else if (shortTeamName) {
					if (this.teamOne == this.battingFirst) {
						if ((this.t2balls) > 0) {
							result = CommonUtility.trimTeamName(this.teamTwoName, this.teamTwoCode) + " "
									+ (this.t2_1total) + "/" + (this.t2_1wickets) + " & " + (this.t2_2total) + "/"
									+ (this.t2_2wickets);
							/*
							 * + " (" + CommonUtility .ballsToOvers((int) this.t2balls) + "/" + this.overs +
							 * " ov) ";
							 */
						}
						result += "  " + CommonUtility.trimTeamName(this.teamOneName, this.teamOneCode) + " "
								+ (this.t1_1total) + "/" + (this.t1_1wickets) + " & " + (this.t1_2total) + "/"
								+ (this.t1_2wickets);
						/*
						 * + " (" + CommonUtility.ballsToOvers((int) this.t1balls) + "/" + this.overs +
						 * " ov) ";
						 */
					} else {
						if ((this.t2balls) > 0) {
							result = CommonUtility.trimTeamName(this.teamOneName, this.teamOneCode) + " "
									+ (this.t2_1total) + "/" + (this.t2_1wickets) + " & " + (this.t2_2total) + "/"
									+ (this.t2_2wickets);
							/*
							 * + " (" + CommonUtility .ballsToOvers((int) this.t2balls) + "/" + this.overs +
							 * " ov) ";
							 */
						}
						result += CommonUtility.trimTeamName(this.teamTwoName, this.teamTwoCode) + " "
								+ (this.t1_1total) + "/" + (this.t1_1wickets) + " & " + (this.t1_2total) + "/"
								+ (this.t1_2wickets);
						/*
						 * + " (" + CommonUtility.ballsToOvers((int) this.t1balls) + "/" + this.overs +
						 * " ov)  ";
						 */
					}

				} else {
					if (this.teamOne == this.battingFirst) {
						result = this.teamOneName + ": " + (this.t1_1total) + "/" + (this.t1_1wickets) + " & " +(this.t1_2total) + "/" + (this.t1_2wickets);
						if ((this.t2balls) > 0) {
							result += " | " +this.teamTwoName + ": " + (this.t2_1total) + "/" + (this.t2_1wickets) + " & " + (this.t2_2total) + "/" + (this.t2_2wickets);
						}
					} else {
						result = this.teamTwoName + ": " + (this.t1_1total) + "/" + (this.t1_1wickets) + " & " +(this.t1_2total) + "/" + (this.t1_2wickets);;
						if ((this.t2balls) > 0) {
							result += " | " +this.teamOneName + ": " + (this.t2_1total) + "/" + (this.t2_1wickets) + " & " + (this.t2_2total) + "/" + (this.t2_2wickets);
						}
					}
				}
			}
			// For Other then test match.
		} else {
			float t2Overs = this.overs;
			if(this.getT2RevisedOvers() > 0) {
				t2Overs = this.getT2RevisedOvers();
			}
			if (!CommonUtility.isNullOrEmpty(abandoneType)) {
				if (ABANDONE_TYPE_ABANDONED.equals(abandoneType)) {
					result += "Abandoned.";
				} else if (ABANDONE_TYPE_FOREFEIT.equals(abandoneType)) {
					result += "Forfeited.";
				} else if (ABANDONE_TYPE_SUPEROVER.equals(abandoneType)) {
					result += "Super Over.";
				} else if (ABANDONE_TYPE_DRAW.equals(abandoneType)) {
					result += "Draw.";
				} else if (winner == 0) {
					result += "No Result";
				}
				if (winner != 0) {
					if (this.winner == this.teamOne) {
						result += " Winner: "
								+ (shortTeamName ? CommonUtility.trimTeamName(teamOneName, this.teamOneCode)
										: teamOneName);
					} else if (this.winner == this.teamTwo) {
						result += " Winner: "
								+ (shortTeamName ? CommonUtility.trimTeamName(teamTwoName, this.teamTwoCode)
										: teamTwoName);
					}
				}
			} else {

				if (this.winner != 0 && this.isComplete == 1) {
					if (this.winner == this.teamOne) {
						result += (shortTeamName ? CommonUtility.trimTeamName(teamOneName, this.teamOneCode)
								: teamOneName) + " won by ";
					} else {
						result += (shortTeamName ? CommonUtility.trimTeamName(teamTwoName, this.teamTwoCode)
								: teamTwoName) + " won by ";
					}
					if (this.winner == this.battingFirst) {
						if (this.isDls) {
							result += ((this.t2Target -1) - (this.t2total)) + " Run(s)";
						} else {
							result += ((this.t1total) - (this.t2total)) + " Run(s)";
						}
					} else {
						
						if(this.isDls && this.getT2total() > this.getT2Target() ) {
							float t2oversAtLastInteruption = 0;
							try {
								t2oversAtLastInteruption = MatchDLRecordsFactory.getT2BallsAtLastInterruption(this.matchID, this.clubID);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							int t2BallsAtLastInteruption = 0;
							if(t2oversAtLastInteruption>0) {
								t2BallsAtLastInteruption = CommonUtility.oversToBalls(t2oversAtLastInteruption+"", 6);
							}
							if(t2BallsAtLastInteruption>0){
								if(this.t2balls == t2BallsAtLastInteruption){
									result += ((this.t2total) - (this.t2Target -1)) + " Run(s)";
								}else if(this.t2balls>t2BallsAtLastInteruption) {
									result += (10 - (this.t2wickets)) + " Wkt(s)";
								}
							}else {
								if(this.t2balls==this.t2RevisedOvers){		
									result += ((this.t2total) - (this.t2Target -1)) + " Run(s)";
								}else {
									result += (10 - (this.t2wickets)) + " Wkt(s)";
								}
							}
							/*
							if(t2BallsAtLastInteruption>0 && this.t2balls>t2BallsAtLastInteruption){
								result += (10 - (this.t2wickets)) + " Wkt(s)";
							}else if(t2BallsAtLastInteruption==0 && this.t2balls==this.t2RevisedOvers){		
								result += ((this.t2total) - (this.t2Target -1)) + " Run(s)";
							}else {
								result += (10 - (this.t2wickets)) + " Wkt(s)";
							}*/
						} else {						
							if (this.maximumPlayers > 0 && this.maximumPlayers < 11) {
								result += (this.maximumPlayers - (this.t2wickets) - 1) + " Wkt(s)";
							} else {
								result += (10 - (this.t2wickets)) + " Wkt(s)";
							}
						}
					}
				} else if (this.winner == 0 && this.isComplete == 1) {
					result += "It is a Tie";
				} else if (shortTeamName) {
					
					if (this.teamOne == this.battingFirst) {
						if ((this.t2balls) > 0) {
							result = CommonUtility.trimTeamName(this.teamTwoName, this.teamTwoCode) + " " + (this.t2total) + "/" + (this.t2wickets);
							if(!"100b".equalsIgnoreCase(this.getSeriesType())){
								result += " ("	+ CommonUtility.ballsToOvers((int) this.t2balls, totalBallInOver) + "/" + t2Overs + " ov) ";
							}else {
								result += " ("	+ this.t2balls + " b) ";
							}
						}
						result += CommonUtility.trimTeamName(this.teamOneName, this.teamOneCode) + " " + (this.t1total)	+ "/" + (this.t1wickets);
						if(!"100b".equalsIgnoreCase(this.getSeriesType())){
							result += " (" + CommonUtility.ballsToOvers((int) this.t1balls, totalBallInOver) + "/"+ this.overs + " ov) ";
						}else {
							result += " (" + this.t1balls + " b) ";
						}
					} else {
						if ((this.t2balls) > 0) {
							result = CommonUtility.trimTeamName(this.teamOneName, this.teamOneCode) + " "+ (this.t2total) + "/" + (this.t2wickets);
							if(!"100b".equalsIgnoreCase(this.getSeriesType())){
								result += " (" + CommonUtility.ballsToOvers((int) this.t2balls, totalBallInOver) + "/" + t2Overs + " ov) ";
							}else {
								result += " (" + this.t2balls + " b) ";
							}
						}
						result += CommonUtility.trimTeamName(this.teamTwoName, this.teamTwoCode) + " " + (this.t1total)	+ "/" + (this.t1wickets);
						if(!"100b".equalsIgnoreCase(this.getSeriesType())){
							result += " (" + CommonUtility.ballsToOvers((int) this.t1balls, totalBallInOver) + "/"	+ this.overs + " ov)  ";
						}else {
							result += " (" + this.t1balls + " b)  ";
						}
					}

				} else {
					if (this.teamOne == this.battingFirst) {
						result = this.teamOneName + ": " + (this.t1total) + "/" + (this.t1wickets);
						if(!"100b".equalsIgnoreCase(this.getSeriesType())){
							result += " ("+ CommonUtility.ballsToOvers((int) (this.t1balls), totalBallInOver) + "/" + this.overs + " ov)  ";
						}else {
							result += " ("+ this.t1balls + " b)  ";
						}
						if ((this.t2balls) > 0) {
							result += " <br/> " + this.teamTwoName + ": " + (this.t2total) + "/" + (this.t2wickets);
							if(!"100b".equalsIgnoreCase(this.getSeriesType())){
								result += " ("+ CommonUtility.ballsToOvers((int) (this.t2balls), totalBallInOver) + "/" + (int)t2Overs + " ov)";
							}else {
								result += " ("+ this.t2balls + " b)";
							}
						}
					} else {
						result = this.teamTwoName + ": " + (this.t1total) + "/" + (this.t1wickets);
						if(!"100b".equalsIgnoreCase(this.getSeriesType())){
							result += "(" + CommonUtility.ballsToOvers((int) (this.t1balls), totalBallInOver) + "/" + this.overs + " ov)  ";
						}else {
							result += "(" + this.t1balls + " b)  ";
						}
						if ((this.t2balls) > 0) {
							result += this.teamOneName + ": " + (this.t2total) + "/" + (this.t2wickets);
							if(!"100b".equalsIgnoreCase(this.getSeriesType())){
								result += "("+ CommonUtility.ballsToOvers((int) (this.t2balls), totalBallInOver) + "/" + (int) t2Overs + " ov)";
							}else {
								result += "("+this.t2balls + " b)";
							}
						}
					}
				}
			}
		}
		if(this.isDls) {
			result += " (D/L)";
		}
		return result;
	}

	

	public String getResultMargin() {
		String result = "";
		if (!CommonUtility.isNullOrEmpty(abandoneType)) {
			if (ABANDONE_TYPE_ABANDONED.equals(abandoneType)) {
				result += "Abandoned.";
			} else if (ABANDONE_TYPE_FOREFEIT.equals(abandoneType)) {
				result += "Forfeited.";
			} else if (ABANDONE_TYPE_SUPEROVER.equals(abandoneType)) {
				result += "Super Over.";
			} else if (ABANDONE_TYPE_DRAW.equals(abandoneType)) {
				result += "Draw.";
			} else if (winner == 0) {
				result += "No Result";
			}
			
		} else {
			
			if (this.winner != 0 && this.isComplete == 1) {
				if (this.winner == this.battingFirst) {
					if (this.isDls) {
						result += ((this.t2Target) - (this.t2total)) + " Run(s)";
					} else {
						result += ((this.t1total) - (this.t2total)) + " Run(s)";
					}
				} else {
					if (this.maximumPlayers > 0 && this.maximumPlayers < 11) {
						result += (this.maximumPlayers - (this.t2wickets) - 1) + " Wkt(s)";
					} else {
						result += (10 - (this.t2wickets)) + " Wkt(s)";
					}
				}
			} else if (this.winner == 0 && this.isComplete == 1) {
				result += "It is a Tie";
			} 
		}
		
		return result;
	}
	
	public String getFirstBattingTeamName() {
		if (this.teamOne == this.battingFirst) {
			return this.teamOneName;
		}

		return this.teamTwoName;
	}

	public String getSecondBattingTeamName() {
		if (this.teamOne == this.battingFirst) {
			return this.teamTwoName;
		}

		return this.teamOneName;

	}

	public String getFirstBattingTeamNameEscapChar() {
		if (this.teamOne == this.battingFirst) {
			return this.teamOneName.replaceAll("'", "\\\\\'");
		}

		return this.teamTwoName.replaceAll("'", "\\\\\'");
	}

	public String getSecondBattingTeamNameEscapChar() {
		if (this.teamOne == this.battingFirst) {
			return this.teamTwoName.replaceAll("'", "\\\\\'");
		}

		return this.teamOneName.replaceAll("'", "\\\\\'");

	}

	/**
	 * @return
	 */
	public String getTeamOneName() {
		return teamOneName;
	}

	/**
	 * @return
	 */
	public String getTeamTwoName() {
		return teamTwoName;
	}

	/**
	 * @param string
	 */
	public void setTeamOneName(String string) {
		teamOneName = string;
	}

	/**
	 * @param string
	 */
	public void setTeamTwoName(String string) {
		teamTwoName = string;
	}

	public String toString() {
		String str = "\nMatchDto: ";
		str += "\nthis.getMatchID(): " + this.getMatchID();
		str += "\nthis.getMatchDate(): " + this.getMatchDate();
		str += "\nthis.getTeamOne(): " + this.getTeamOne();
		str += "\nthis.getTeamOne(): " + this.getTeamOne();
		str += "\nthis.getTeamOneName(): " + this.getTeamOneName();
		str += "\nthis.getTeamTwo(): " + this.getTeamTwo();
		str += "\nthis.getTeamTwoName(): " + this.getTeamTwoName();
		return str;

	}

	/*
	 * public String getMatchTypeString() { String matchTypeStr = ""; if (matchType
	 * != null && matchType.equals("l")) { matchTypeStr = "L"; } else if (matchType
	 * != null && matchType.equals("p")) { matchTypeStr = "P"; } else if (matchType
	 * != null && matchType.equals("q")) { matchTypeStr = "QF"; } else if (matchType
	 * != null && matchType.equals("s")) { matchTypeStr = "SF"; } else if (matchType
	 * != null && matchType.equals("sl")) { matchTypeStr = "SL"; } else if
	 * (matchType != null && matchType.equals("f")) { matchTypeStr = "F"; } else if
	 * (matchType != null && matchType.equals("e")) { matchTypeStr = "E"; } else if
	 * (matchType != null && matchType.equals("ql")) { matchTypeStr = "Q"; } else if
	 * (matchType != null && matchType.equals("3p")) { matchTypeStr = "3P"; } return
	 * matchTypeStr; }
	 * 
	 * public String getMatchTypeFullString() { String matchTypeStr = ""; if
	 * (matchType != null && matchType.equals("l")) { matchTypeStr = "League"; }
	 * else if (matchType != null && matchType.equals("p")) { matchTypeStr =
	 * "Practice"; } else if (matchType != null && matchType.equals("q")) {
	 * matchTypeStr = "Quarter Final"; } else if (matchType != null &&
	 * matchType.equals("s")) { matchTypeStr = "Semi Final"; } else if (matchType !=
	 * null && matchType.equals("sl")) { matchTypeStr = "Super League"; } else if
	 * (matchType != null && matchType.equals("f")) { matchTypeStr = "Final"; } else
	 * if (matchType != null && matchType.equals("e")) { matchTypeStr =
	 * "Eliminator"; } else if (matchType != null && matchType.equals("ql")) {
	 * matchTypeStr = "Qualifier"; } else if (matchType != null &&
	 * matchType.equals("3p")) { matchTypeStr = "3rd Position"; } return
	 * matchTypeStr; }
	 */

	/**
	 * @return
	 */
	public int getManOfTheMatch() {
		return manOfTheMatch;
	}

	/**
	 * @param l
	 */
	public void setManOfTheMatch(int l) {
		manOfTheMatch = l;
	}

	public List<Integer> getAllPlayersForTheMatch() {
		if (players1 != null && players2 != null) {

			List<Integer> allPlayers = new ArrayList<Integer>();
			allPlayers.addAll(players1);
			allPlayers.addAll(players2);
			return allPlayers;
		}
		return null;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public boolean isT1Trump() {
		if (this.isTrump == 0 || this.isTrump == this.getTeamOne()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isT2Trump() {
		if (this.isTrump == 0 || this.isTrump == this.getTeamTwo()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEligibleForBonusPoint() {
		if (this.isComplete == 1 && this.overs > 0 && this.t2balls > 0) {
			double winnerRunrate = 0;
			double looserRunrate = 0;
			
			if(this.isDls && this.getT2Target() > 0) {
				this.newT1Total = this.getT2Target();			
			}else {
				this.newT1Total = this.getT1total();
			}
			
			if(this.isDls && this.getT2RevisedOvers() > 0) {
				this.newOvers = this.getT2RevisedOvers();			
			}else {
				this.newOvers = this.getOvers();
			}
			
			if (this.winner == this.battingFirst) {
					winnerRunrate = (double) this.newT1Total / (this.newOvers);
					looserRunrate = (double) this.t2total / (this.newOvers);				
			} else {
					winnerRunrate = (double) this.t2total / ((double) this.t2balls / 6);
					looserRunrate = (double) this.newT1Total / (this.newOvers);						
			}
			if (winnerRunrate >= (looserRunrate * 1.25)) {
				return true;
			}
		}
		return false;
	}
	public boolean isEligibleForBonusPointDCL() {
		if (this.isComplete == 1 && this.overs > 0 && this.t2balls > 0) {
			double winnerRunrate = 0;
			double looserRunrate = 0;
			
			if(this.isDls && this.getT2Target() > 0) {
				this.newT1Total = this.getT2Target();			
			}else {
				this.newT1Total = this.getT1total();
			}
			
			if(this.isDls && this.getT2RevisedOvers() > 0) {
				this.newOvers = this.getT2RevisedOvers();			
			}else {
				this.newOvers = this.getOvers();
			}
			
			if (this.winner == this.battingFirst) {
					winnerRunrate = (double) this.newT1Total / (this.newOvers);
					looserRunrate = (double) this.t2total / (this.newOvers);				
			} else {
					winnerRunrate = (double) this.t2total / ((double) this.t2balls / 6);
					looserRunrate = (double) this.newT1Total / (this.newOvers);						
			}
			if ((winnerRunrate - looserRunrate)/winnerRunrate >= 0.15) {
				return true;
			}
		}
		return false;
	}
	public boolean isEligibleForBonusPointForHampton() {
		if (this.isComplete == 1 && this.overs > 0 && this.t2balls > 0) {
			double winnerRunrate = 0;
			double looserRunrate = 0;
			if (this.winner == this.battingFirst) {
				winnerRunrate = (double) this.t1total / (this.overs);
				looserRunrate = (double) this.t2total / (this.overs);
				if (winnerRunrate >= (looserRunrate * 1.25)) {
					return true;
				}
			} else {
				if (t2balls < 79) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean isEligibleForSpecialBonusPoint() {
		if ("l".equals(this.getMatchType()) && this.isComplete == 1 && this.overs > 0 && this.t2balls > 0
				&& this.isAbandoned != 1) {
			return true;
		}
		return false;
	}

	public int getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(int isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isUserEligibleForScoring(UserDto user, List<TeamDto> teams) {
		if (this.isLocked == 1) {
			return false;
		}
		int playerId = user.getPlayerID();
		int userId = user.getUserID();
		int umpire1UsrId=0;
		int umpire2UsrId=0;
		
		if(!CommonUtility.isNullOrEmpty(this.umpire1UserId))
		{
			 umpire1UsrId=CommonUtility.stringToInt(this.umpire1UserId.replaceAll("[u-]", ""));
		}
		
		if(!CommonUtility.isNullOrEmpty(this.umpire2UserId))
		{
			 umpire2UsrId=CommonUtility.stringToInt(this.umpire2UserId.replaceAll("[u-]", ""));
		}		
		
		if ((playerId >0 && (this.teamOneCaptain == playerId || this.teamTwoCaptain == playerId || this.teamOneViceCaptain == playerId
				|| this.teamTwoViceCaptain == playerId))
				|| CommonLogic.isUserCaptainOfaTeam(teams, this.teamOne, this.teamTwo, user) || (userId == umpire1UsrId) ||(userId == umpire2UsrId)) {
			return true;
		}
		return false;
	}
	
	public boolean canUserCreateCaptainReport(UserDto user, List<TeamDto> teams) {

		int playerId = user.getPlayerID();
		int userId = user.getUserID();
		int umpire1UsrId = 0;
		int umpire2UsrId = 0;

		if (!CommonUtility.isNullOrEmpty(this.umpire1UserId)) {
			umpire1UsrId = CommonUtility.stringToInt(this.umpire1UserId.replaceAll("[u-]", ""));
		}

		if (!CommonUtility.isNullOrEmpty(this.umpire2UserId)) {
			umpire2UsrId = CommonUtility.stringToInt(this.umpire2UserId.replaceAll("[u-]", ""));
		}

		if (user.isCaptain()
				|| (playerId > 0 && (this.teamOneCaptain == playerId || this.teamTwoCaptain == playerId
						|| this.teamOneViceCaptain == playerId || this.teamTwoViceCaptain == playerId))
				|| CommonLogic.isUserCaptainOfaTeam(teams, this.teamOne, this.teamTwo, user) || (userId == umpire1UsrId)
				|| (userId == umpire2UsrId)) {
			return true;
		}
		return false;
	}

	public int getScorer() {
		return scorer;
	}

	public void setScorer(int scorer) {
		this.scorer = scorer;
	}

	public String getScorerStr() {
		if (scorer == 0) {
			return "";
		}
		return scorer + "";
	}

	public void setScorerStr(String scorer) {
		if (scorer != null && !"".equals(scorer)) {
			this.scorer = Integer.parseInt(scorer.trim());
		}
	}

	public boolean isInProgress() {
		if (this.isComplete != 1) {
			return true;
		}
		return false;
	}

	public boolean isLiveMatch() {
		if (this.scorer != 0 || this.isComplete != 1 ) {
			return true;
		}
		return false;
	}

	public List<Integer> getPlayers1() {
		if (players1 == null) {
			players1 = new ArrayList<Integer>();
		}
		return players1;
	}

	public void setPlayers1(List<Integer> players1) {
		this.players1 = players1;
	}

	public List<Integer> getPlayers2() {
		if (players2 == null) {
			players2 = new ArrayList<Integer>();
		}
		return players2;
	}

	public void setPlayers2(List<Integer> players2) {
		this.players2 = players2;
	}

	public int getBattingSecond() {
		if (this.battingFirst == this.teamOne) {
			return this.teamTwo;
		} else {
			return this.teamOne;
		}
	}

	public boolean isSecondInningsStarted() {
		return this.t2balls > 0 || this.t2total > 0;
	}

	public boolean isThirdInningsStarted() {
		
		if(this.getIsComplete() == 1) {
			if (this.getIsFollowon() <= 0) {
				return this.t1_2balls > 0 || this.t1_2total > 0;
			} else {
				return this.t2_2balls > 0 || this.t2_2total > 0;
			}
		}else {
			if(this.getCurrentInnings() >= 3) {
				return true;
			}else {
				return false;
			}
		}
	}

	public boolean isFourthInningsStarted() {
		
		if(this.getIsComplete() == 1) {
			if (this.getIsFollowon() <= 0) {
				return this.t2_2balls > 0 || this.t2_2total > 0;
			} else {
				return this.t1_2balls > 0 || this.t1_2total > 0;
			}
		}else {
			if(this.getCurrentInnings() >= 4) {
				return true;
			}else {
				return false;
			}
		}
	}

	public boolean isFirstInningsStarted() {
		return this.t1balls > 0 || this.t1total > 0;
	}

	public int getT2TotalWickets() {
		if (players2 != null  && !players2.isEmpty() && players2.size() < 11) {
			return players2.size() - 1;
		}
		return 10;
	}

	public int getT1TotalWickets() {
		if (players1 != null && players1.size() < 11) {
			return players1.size() - 1;
		}
		return 10;
	}

	public int getT1TotalRuns() {
		return this.t1_1total + this.t1_2total;
	}

	public int getT2TotalRuns() {
		return this.t2_1total + this.t2_2total;
	}

	public String getAbandoneType() {
		return abandoneType;
	}

	public void setAbandoneType(String abandoneType) {
		this.abandoneType = abandoneType;
	}

	public int getTeam1Points(LeagueDto league) {
		if (this.winner == 0) {
			if (this.getIsAbandoned() == 1) {
				return league.getAbandonedPoints();
			} else {
				return league.getTiePoints();
			}
		}
		if (!CommonLogic.customBonusPointLeagues.contains(league.getClubId())) {
			return (this.teamOne == getWinner())
					? league.getWinPoints() + (isEligibleForBonusPoint() ? league.getBonusPoints() : 0)
					: 0;
		} else {
			return ((this.teamOne == getWinner()) ? league.getWinPoints() : 0) + this.getTeam1BonusPoints(league);
		}
	}
	
	public int getTeam2Points(LeagueDto league) {
		if (this.winner == 0) {
			if (this.getIsAbandoned() == 1) {
				return league.getAbandonedPoints();
			} else {
				return league.getTiePoints();
			}
		}
		if (!CommonLogic.customBonusPointLeagues.contains(league.getClubId())) {
			return (this.teamTwo == getWinner())
					? league.getWinPoints() + (isEligibleForBonusPoint() ? league.getBonusPoints() : 0)
					: 0;
		} else {
			return ((this.teamTwo == getWinner()) ? league.getWinPoints() : 0) + this.getTeam2BonusPoints(league);
		}
	}
	
	public int getTeam1PointsFB(LeagueDto league) {
		if (this.winner == 0) {
			if (this.getIsAbandoned() == 1) {
				return league.getAbandonedPoints();
			} else {
				return league.getTiePoints();
			}
		}
		return (this.teamOne == getWinner() ? league.getWinPoints() : 0 );
	}
	
	public int getTeam2PointsFB(LeagueDto league) {
		if (this.winner == 0) {
			if (this.getIsAbandoned() == 1) {
				return league.getAbandonedPoints();
			} else {
				return league.getTiePoints();
			}
		}
		return (this.teamTwo == getWinner() ? league.getWinPoints() : 0 );
	}

	public int getTeam1BonusPoints(LeagueDto league) {
		int bonus = 0;
		
		if(this.isDls && this.getT2Target() > 0) {
			this.newT1Total = this.getT2Target();			
		}else {
			this.newT1Total = this.getT1total();
		}
		
		if(this.isDls && this.getT2RevisedOvers() > 0) {
			this.newOvers = this.getT2RevisedOvers();			
		}else {
			this.newOvers = this.getOvers();
		}
		
		// Custom Logic for JSCC 32995
		bonus = getTitanT1BonusPoints(league, bonus);
		
		// Custom Logic for JSCC 17156
		bonus = getJSCCT1BonusPoints(league, bonus);
		
		// custom logic for MYCSCA
		bonus = getMYCST1BonusPoints(league, bonus);


		// custom logic for OCL
		bonus = getOCLT1BonusPoints(league, bonus);

		// custom logic for Maryland
		bonus = getMYCT1BonusPoints(league, bonus);

		// custom logic for Guyana
		bonus = getGYNT1BonusPoints(league, bonus);

		// custom logic for NTCA
		bonus = getNTCAT1BonusPoints(league, bonus);

		// custom logic for OMSCC
		bonus = getOMSCCT1BonusPoints(league, bonus);

		// custom logic for Sacramento && UCCL
		bonus = getSacramentoAndUCCLT1BounsPoints(league, bonus);

		// custom logic for CSCA
		bonus = getCSCAT1BonusPoints(league, bonus);

		// custom logic for Origin Cricket Cup
		bonus = getOCCT1BonusPoints(league, bonus);

		// custom logic for NWCL
		bonus = getNWCLT1BonusPoints(league, bonus);

		// custom logic for Scarborough Team One
		bonus = getSCRBT1BonusPoints(league, bonus);

		// custom logic for Spirit of Cricket
		 //commented as part of LM-747 
		// bonus = getSOCT1BonusPoints(league, bonus);

		// custom logic for Team One Singapore Cricket Association
		bonus = getSGPT1BonusPoints(league, bonus);

		// custom logic for Yorker Sports
		bonus = getYSLT1BonusPoints(league, bonus);

		// custom logic for DCL
		bonus = getDCL1BonusPoints(league, bonus);
		
		// custom logic for VCASA
		bonus = getVCASAT1BonusPoints(league, bonus);
		
		bonus=getCustomBonusPointsT1(league,bonus);

		return bonus;
	}
	
	public double getTeam1BonusPointsInDouble(LeagueDto league) {
		double bonus=0.0;
		
		//custom logic for Spirit of Cricket-4108
		bonus=getBattingBonusPointsT1(league,bonus);
		bonus=getBowllingBonusPointsT1(league,bonus);
		
		
		return bonus;
	}
	private int getCustomBonusPointsT1(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0
				&& (league.getClubId() == 18896 || league.getClubId() == 19350 || league.getClubId() == 27942)) {

			if (isEligibleForSpecialBonusPoint()) {
				if (this.teamOne == this.winner) {
					if (this.getT2total() < (this.getT1total() * 0.8)) {
						bonus += 1;
					}
				}
				double T2overs = Double.parseDouble(CommonUtility.ballsToOvers(this.getT2balls()));
				if (this.teamOne != this.winner) {
					if (T2overs > this.getOvers() * 0.8) {
						bonus += 1;
					}
				}
			}
		}
		return bonus;
	}
	
	private int getVCASAT1BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 22082) {
			
			if (isEligibleForSpecialBonusPoint()) {	
				if (this.getWinner() == this.teamOne) {	
					if (this.getT2total() <= (this.getT1total()*70/100)) {
						bonus += 2;
					} else if (this.getT2total() <= (this.getT1total()*80/100)) {
						bonus += 1;
					}
				}
			}
		}
		return bonus;
	}

	private int getSOCT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getMaxOvers() == 20 && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {

			int points = 0;

			if (this.getWinner() == this.getTeamOne()) {
				if (isEligibleForBonusPoint()) {
					points += 1;
					if (isT1Trump()) {
						points += 2;
					}
				} else {
					if (isT1Trump()) {
						points = points - 2;
					}
				}
			}
			if (this.getWinner() == this.getTeamTwo()) {
				if (isT1Trump()) {
					points = points - 2;
				}
				if (!isEligibleForBonusPoint()) {
					points += 1;
				}
			}
			bonus += points;
		}
		return bonus;
	}

	private int getYSLT1BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4263 && this.isComplete == 1
				&& "l".equals(this.getMatchType())) {

			// if tie add 1 extra point
			if (this.t1total > 0 && this.t1total == this.t2total) {
				return 1;
			} else if (ABANDONE_TYPE_FOREFEIT.equals(this.abandoneType) && this.teamOne == this.winner) {
				return 2;
			} else if (this.isAbandoned != 1 && this.winner > 0) {
				if (this.getTeamOne() == this.getWinner()) { // if team one is
																// winner
					if (this.getT2total() <= ((double) this.newT1Total * 0.5)) {
						return 2;
					}
				} else {
					if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.9)) {
						return 0;
					}
				}
			}
		}
		return bonus;
	}

	private int getSGPT1BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 7683) {

			int points = 0;
			
			if (isEligibleForSpecialBonusPoint()) {

				if (this.getWinner() == this.getTeamOne()) {
					if (this.getT2total() < ((double) this.newT1Total * 0.5)) {
						points = 2;
					}
				}
				if (this.getWinner() == this.getTeamTwo()) {
					if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers + "") * 0.9)) {
						points = 1;
					}
				}
			} else if (this.getWinner() == this.getTeamOne() && this.getIsAbandoned() == 1 
					&& this.getAbandoneType().equalsIgnoreCase("forefeit")) {
				points = league.getBonusPoints();				
			}	
			bonus = points;			
		}
		return bonus;
	}

	private int getSCRBT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getMaxOvers() == 20 && league.getBonusPoints() > 0 && league.getClubId() == 1835
				&& isEligibleForSpecialBonusPoint()) {
			int points = 0;
			if (league.getSeriesType() == "Youth" || league.getSeriesType() == "Women") {

				if (this.getT1total() >= 150 && this.getT1total() < 175) {
					points += 1;
				}
				if (this.getT1total() >= 175 && this.getT1total() < 199) {
					points += 2;
				}
				if (this.getT1total() >= 199) {
					points += 3;
				}
				if (this.getT2wickets() == (getT2PlayersCount() - 1)) {
					points += 1;
					if (this.getT2total() <= 60) {
						points += 1;
					}
				}
			} else {
				if (this.getT1total() >= 250) {
					points += 1;
				}
				if (this.getT2wickets() == (getT2PlayersCount() - 1)) {
					points += 1;
				}
			}
			bonus += points;
		}
		return bonus;
	}

	private int getNWCLT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 232
				&& isEligibleForSpecialBonusPoint()) {

			if (this.getTeamTwo() == this.getWinner()) {
				int points = 0;
				if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.9)) {
					points = 5;
				} else if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.8)) {
					points = 4;
				} else if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.7)) {
					points = 3;
				} else if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.6)) {
					points = 2;
				} else if (this.getT2balls() >= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.5)) {
					points = 1;
				}
				bonus += points;
				bonus += (this.getT2wickets() / 2);
			}
		}
		return bonus;
	}

	private int getOCCT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 7126
				&& isEligibleForSpecialBonusPoint()) {

			if (this.getWinner() == this.getTeamOne() && this.getT2balls() <= 90) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getCSCAT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 3067
				&& isEligibleForSpecialBonusPoint()) {
			if ((this.getT1total() >= 125)) {
				bonus += 1;
			}

			if ((this.getT2wickets() >= 10)) {
				bonus += 1;
			}

			if (this.getWinner() == this.getTeamOne() && this.getT1balls() <= 60) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getDCL1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 560
				&& isEligibleForBonusPointDCL()) {

			if (this.getWinner() == this.getTeamOne()) {
				return league.getBonusPoints();
			}else {
				return 0;
			}
		}
		
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 560
				&& !isEligibleForBonusPointDCL()) {

			if (this.getWinner() == this.getTeamTwo()) {
				return league.getBonusPoints();
			}else {
				return 0;
			}
		}
		return bonus;
	}

	private int getDCL2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 560
				&& isEligibleForBonusPointDCL()) {

			if (this.getWinner() == this.getTeamTwo()) {
				return league.getBonusPoints();
			}else {
				return 0;
			}
		}
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 560
				&& !isEligibleForBonusPointDCL()) {

			if (this.getWinner() == this.getTeamOne()) {
				return league.getBonusPoints();
			}else {
				return 0;
			}
		}
		return bonus;
	}

	private int getSacramentoAndUCCLT1BounsPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 2453 ||  league.getClubId() == 30340
				&& isEligibleForSpecialBonusPoint()) {

			if ((this.getT2wickets() >= 10)) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getOMSCCT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 556
				&& isEligibleForSpecialBonusPoint()) {
			if ((this.getT1total() >= 150)) {
				bonus += 1;
			}

			if ((this.getT2wickets() >= 11)) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getMYCT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 2313
				&& isEligibleForSpecialBonusPoint()) {

			if ((this.getT1total() >= 50 && this.getT1total() <= 74)) {
				bonus += 1;
			}
			if ((this.getT1total() >= 75 && this.getT1total() <= 99)) {
				bonus += 2;
			}
			if ((this.getT1total() >= 100 && this.getT1total() <= 124)) {
				bonus += 3;
			}
			if ((this.getT1total() >= 125)) {
				bonus += 4;
			}
			if ((this.getT2wickets() >= 3 && this.getT2wickets() <= 4)) {
				bonus += 1;
			}
			if ((this.getT2wickets() >= 5 && this.getT2wickets() <= 6)) {
				bonus += 2;
			}
			if (this.getT2wickets() >= 7 && (this.getT2wickets() < (this.getT2PlayersCount() - 1))
					&& this.getT2wickets() != 10) {
				bonus += 3;
			} 
			if (this.getT2wickets() >= (this.getT2PlayersCount() - 1) || this.getT2wickets() == 10) {
				bonus += 4;
			}			
			if (this.teamOne == this.winner && this.getT1total() == 0 && this.getT2total() == 0) {
				bonus += 8;
			}
		}
		return bonus;
	}

	private int getGYNT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 9093
				&& isEligibleForSpecialBonusPoint()) {

			if (this.teamOne == this.winner) {

				if ((this.getT1total() >= 200 && this.getT1total() <= 249)) {
					bonus += 1;
				}
				if ((this.getT1total() >= 250 && this.getT1total() <= 299)) {
					bonus += 2;
				}
				if ((this.getT1total() >= 300 && this.getT1total() <= 349)) {
					bonus += 3;
				}
				if ((this.getT1total() >= 350 && this.getT1total() <= 399)) {
					bonus += 4;
				}
				if ((this.getT1total() >= 400)) {
					bonus += 5;
				}
				if ((this.getT2wickets() >= 3 && this.getT2wickets() <= 5)) {
					bonus += 1;
				}
				if ((this.getT2wickets() >= 6 && this.getT2wickets() <= 8)) {
					bonus += 2;
				}
				if ((this.getT2wickets() >= 9 && this.getT2wickets() <= 10)) {
					bonus += 3;
				}
			}
		}
		return bonus;
	}

	private int getOCLT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 116
				&& league.getSeriesType().equalsIgnoreCase("One Day") && isEligibleForSpecialBonusPoint()) {

			if (this.teamTwo == this.winner) {

				if (this.getT2balls() >= 162) {
					bonus += 5;
				} else if (this.getT2balls() >= 144) {
					bonus += 4;
				} else if (this.getT2balls() >= 126) {
					bonus += 3;
				} else if (this.getT2balls() >= 108) {
					bonus += 2;
				} else if (this.getT2balls() >= 90) {
					bonus += 1;
				}

				if (this.t2wickets >= 10) {
					bonus += 5;
				} else if (this.t2wickets >= 8) {
					bonus += 4;
				} else if (this.t2wickets >= 6) {
					bonus += 3;
				} else if (this.t2wickets >= 4) {
					bonus += 2;
				} else if (this.t2wickets >= 2) {
					bonus += 1;
				}
			}
		}
		return bonus;
	}

	private int getNTCAT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 343
				&& isEligibleForSpecialBonusPoint()) {
			if (((double) this.getT1total() / (double) this.getOvers() >= 7)) {
				bonus += 1;
			}

			if ((this.getT2wickets() >= 10)) {
				bonus += 1;
			}
		}
		return bonus;
	}
	
	private double getBattingBonusPointsT1(LeagueDto league, double bonus) {
		// TODO Auto-generated method stub
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {
			if (this.getT1total() > 75 && this.getT1total() <= 100) {
				bonus += 0.5;
			} else if (this.getT1total() >= 101 && this.getT1total() <= 125) {
				bonus += 1;
			} else if (this.getT1total() >= 126 && this.getT1total() <= 140) {
				bonus += 1.5;
			} else if (this.getT1total() > 140) {
				bonus += 2;
			}
		}
		return bonus;
	}

	private double getBowllingBonusPointsT1(LeagueDto league, double bonus) {
		// TODO Auto-generated method stub
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {
			if (this.getT2PlayerSize() == 11) {
				if (this.getT2wickets() == 5) {
					bonus +=  0.5;
				} else if (this.getT2wickets() == 6) {
					bonus += 1;
				} else if (this.getT2wickets() == 7) {
					bonus +=1;
				} else if (this.getT2wickets() == 8) {
					bonus +=1.5;
				} else if (this.getT2wickets() == 9) {
					bonus += 1.5;
				} else if (this.getT2wickets() == 10) {
					bonus +=2;
				}

			} if (this.getT2PlayerSize() == 10) {
				if (this.getT2wickets() == 5) {
					bonus +=  0.5;
				} else if (this.getT2wickets() == 6) {
					bonus += 1;
				} else if (this.getT2wickets() == 7) {
					bonus +=1;
				} else if (this.getT2wickets() == 8) {
					bonus += 1.5;
				} else if (this.getT2wickets() == 9) {
					bonus +=2;
				} 

			} if (this.getT2PlayerSize() == 9) {
				if (this.getT2wickets() == 5) {
					bonus +=  0.5;
				} else if (this.getT2wickets() == 6) {
					bonus += 1;
				} else if (this.getT2wickets() == 7) {
					bonus +=1.5;
				} else if (this.getT2wickets() == 8) {
					bonus +=2;
				} 
			}
		}
		return bonus;
	}

	private int getT2PlayersCount() {
		if (this.players2 == null || this.players2.isEmpty()) {
			return 11;
		}
		return this.players2.size();
	}

	public int getTeam2BonusPoints(LeagueDto league) {
		
		int bonus = 0;		
		
		if(this.isDls && this.getT2Target() > 0) {
			this.newT1Total = this.getT2Target();
		}else {
			this.newT1Total = this.getT1total();
		}
		
		if(this.isDls && this.getT2RevisedOvers() > 0) {
			this.newOvers = this.getT2RevisedOvers();			
		}else {
			this.newOvers = this.getOvers();
		}
		
		// Custom Logic for JSCC 32995
		bonus = getTitanT2BonusPoints(league, bonus);
		
		// Custom Logic for JSCC 17156
		bonus = getJSCCT2BonusPoints(league, bonus);
				
		// custom logic for MYCSCA
		bonus = getMYCST2BonusPoints(league, bonus);
		
		// custom logic for OCL
		bonus = getOCLT2BonusPoints(league, bonus);

		// custom logic for Maryland
		bonus = getMYCT2BonusPoints(league, bonus);

		// custom logic for Guyana
		bonus = getGYNT2BonusPoints(league, bonus);

		// custom logic for NTCA
		bonus = getNTCAT2BonusPoints(league, bonus);

		// custom logic for OMSCC
		bonus = getOMSCCT2BonusPoints(league, bonus);

		// custom logic for Sacramento && UCCL
		bonus = getSacramentoAndUCCLT2BounsPoints(league, bonus);

		// custom logic for CSCA
		bonus = getCSCAT2BonusPoints(league, bonus);

		// custom logic for Origin Cricket Cup
		bonus = getOCCT2BonusPoints(league, bonus);

		// custom logic for NWCL
		bonus = getNWCLT2BonusPoints(league, bonus);

		// custom logic for Scarborough Team Two
		bonus = getSCRBT2BonusPoints(league, bonus);

		// custom logic for Spirit of Cricket Team Two
		//commented as part of LM-747
		//bonus = getSOCT2BonusPoints(league, bonus);

		// custom logic for Team Two Singapore Cricket Association
		bonus = getSGPT2BonusPoints(league, bonus);

		// custom logic for Yorker Sports
		bonus = getYSLT2BonusPoints(league, bonus);
		
		// custom logic for DCL
		bonus = getDCL2BonusPoints(league, bonus);

		//custom logic for VCASA
		bonus = getVCASAT2BonusPoints(league, bonus);
		

		bonus=getCustomBonusPointsT2(league,bonus);
		
		return bonus;
	}
	
	public double getTeam2BonusPointsInDouble(LeagueDto league) {
		
		double bonus=0.0;
		
		//custom logic for Spirit of Cricket-4108
		bonus=getBattingBonusPointsT2(league,bonus);
		bonus=getBowllingBonusPointsT2(league,bonus);
		bonus=getSOCBonusPointsT2(league,bonus);
		
		return bonus;
	}
	
	private int getCustomBonusPointsT2(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0
				&& (league.getClubId() == 18896 || league.getClubId() == 19350 || league.getClubId() == 27942)) {

			if (isEligibleForSpecialBonusPoint()) {
				double T2overs = Double.parseDouble(CommonUtility.ballsToOvers(this.getT2balls()));
				if (this.teamTwo == this.winner) {
					if(!(league.getClubId() == 18896) && (T2overs <= this.getOvers() * 0.7)) {
						bonus += 2;
					} else if (T2overs <= this.getOvers() * 0.8) {
						bonus += 1;
					}
				}
				if (this.teamTwo != this.winner) {
					if (this.getT2total() >= (this.getT1total() * 0.8)) {
						bonus += 1;
					}
				}
			}
		}
		return bonus;

	}
	

	private int getVCASAT2BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 22082) {
			
			if (isEligibleForSpecialBonusPoint()) {	
				
				if (this.getWinner() == this.teamTwo) {	
					int ballsInOver = 6;
					if("100b".equalsIgnoreCase(this.getSeriesType())){
						ballsInOver = 5;						
					}
					if (this.getT2balls() <= (this.getOvers()*ballsInOver*70/100)) {
						bonus += 2;
					} else if (this.getT2balls() <= (this.getOvers()*ballsInOver*80/100)) {
						bonus += 1;
					}
				}
			}
		}
		return bonus;
	}
	
	private int getTitanT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 32995 && isEligibleForSpecialBonusPoint()) {
			if (this.teamOne == this.winner) {
				if (this.isDls) {
					if (this.t2total>(this.t2Target-(this.t2Target*5/100))) {
						bonus += 1;
					}
				} else if (this.t2total>(this.t1total-(this.t1total*5/100))) {
					bonus += 1;
				}
			}			
			if (this.teamTwo == this.winner) {
				if (this.isDls) {
					if (this.t2balls<(this.t2RevisedOvers-(this.t2RevisedOvers*20/100))) {
						bonus += 1;
					}
				}else if(this.t2balls<(this.overs*6-(this.overs*6*20/100))) {
					bonus += 1;
				}
			}
		}
		return bonus;
	}
	 

	private int getJSCCT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 17156 && isEligibleForSpecialBonusPoint()) {
			if (this.teamOne == this.winner) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getSGPT2BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 7683 ) {
			
			int points = 0;
			
			if(isEligibleForSpecialBonusPoint()) {
				if (this.getWinner() == this.getTeamTwo()) {
					if (this.getT2balls() < ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.5)) {
						points = 2;					
					}
				}
				if (this.getWinner() == this.getTeamOne()) {
					if (this.getT2total() >= ((double) this.newT1Total * 0.9)) {
						points = 1;
					}
				}				
			}else if (this.getWinner() == this.getTeamTwo() && this.getIsAbandoned() == 1 
					&& this.getAbandoneType().equalsIgnoreCase("forefeit")) {
				points = league.getBonusPoints();				
			}	
			bonus = points;
		}
		return bonus;
	}

	private int getYSLT2BonusPoints(LeagueDto league, int bonus) {

		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4263 && this.isComplete == 1
				&& "l".equals(this.getMatchType())) {

			// if tie add 1 extra point
			if (this.t2total > 0 && this.t1total == this.t2total) {
				return 1;
			} else if (ABANDONE_TYPE_FOREFEIT.equals(this.abandoneType) && this.teamTwo == this.winner) {
				return 2;
			} else if (this.isAbandoned != 1 && this.winner > 0) {
				if (this.getTeamTwo() == this.getWinner()) { // if team two is
																// winner
					if (this.getT2balls() <= ((double) CommonUtility.oversToBalls(this.newOvers+"") * 0.5)) {
						return 2;
					}
				} else {
					if (this.getT2total() >= ((double) this.newT1Total * 0.9)) {
						return 0;
					}
				}
			}

		}
		return bonus;
	}

	private int getSCRBT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getMaxOvers() == 20 && league.getBonusPoints() > 0 && league.getClubId() == 1835
				&& isEligibleForSpecialBonusPoint()) {

			int points = 0;

			if (league.getSeriesType() == "Youth" || league.getSeriesType() == "Women") {

				if (this.getT2total() >= 150 && this.getT2total() < 175) {
					points += 1;
				}
				if (this.getT2total() >= 175 && this.getT2total() < 199) {
					points += 2;
				}
				if (this.getT2total() >= 199) {
					points += 3;
				}
				if (this.getT1wickets() == (getT1PlayersCount() - 1)) {
					points += 1;
					if (this.getT1total() <= 60) {
						points += 1;
					}
				}
			} else {
				if (this.getT2total() >= 250) {
					points += 1;
				}
				if (this.getT1wickets() == (getT1PlayersCount() - 1)) {
					points += 1;
				}
			}
			bonus += points;
		}

		return bonus;
	}

	private int getSOCT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getMaxOvers() == 20 && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {

			int points = 0;

			if (this.getWinner() == this.getTeamTwo()) {
				if (isEligibleForBonusPoint()) {
					points += 1;
					if (isT2Trump()) {
						points += 2;
					}
				} else {
					if (isT2Trump()) {
						points = points - 2;
					}
				}
			}
			if (this.getWinner() == this.getTeamOne()) {
				if (isT2Trump()) {
					points = points - 2;
				}
				if (!isEligibleForBonusPoint()) {
					points += 1;
				}
			}
			bonus += points;
		}
		return bonus;
	}

	private int getNWCLT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 232
				&& isEligibleForSpecialBonusPoint()) {

			if (this.getTeamOne() == this.getWinner()) {
				int points = 0;
				if (this.getT2total() >= ((double) this.newT1Total * 0.9)) {
					points = 5;
				} else if (this.getT2total() >= ((double) this.newT1Total * 0.8)) {
					points = 4;
				} else if (this.getT2total() >= ((double) this.newT1Total * 0.7)) {
					points = 3;
				} else if (this.getT2total() >= ((double) this.newT1Total * 0.6)) {
					points = 2;
				} else if (this.getT2total() >= ((double) this.newT1Total * 0.5)) {
					points = 1;
				}
				bonus += points;
				bonus += (this.getT1wickets() / 2);
			}
		}
		return bonus;
	}

	private int getOCCT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 7126
				&& isEligibleForSpecialBonusPoint()) {

			if (this.getWinner() == this.getTeamTwo() && this.getT2balls() <= 90) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getCSCAT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 3067
				&& isEligibleForSpecialBonusPoint()) {
			if ((this.getT2total() >= 125)) {
				bonus += 1;
			}

			if ((this.getT1wickets() >= 10)) {
				bonus += 1;
			}

			if (this.getWinner() == this.getTeamTwo() && this.getT2balls() <= 60) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getSacramentoAndUCCLT2BounsPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 2453 || league.getClubId() == 30340
				&& isEligibleForSpecialBonusPoint()) {

			if ((this.getT1wickets() >= 10)) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getOMSCCT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 556
				&& isEligibleForSpecialBonusPoint()) {
			if ((this.getT2total() >= 150)) {
				bonus += 1;
			}

			if ((this.getT1wickets() >= 11)) {
				bonus += 1;
			}
		}
		return bonus;
	}

	private int getGYNT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 9093
				&& isEligibleForSpecialBonusPoint()) {

			if (this.teamTwo == this.winner) {

				if ((this.getT2total() >= 200 && this.getT2total() <= 249)) {
					bonus += 1;
				}
				if ((this.getT2total() >= 250 && this.getT2total() <= 299)) {
					bonus += 2;
				}
				if ((this.getT2total() >= 300 && this.getT2total() <= 349)) {
					bonus += 3;
				}
				if ((this.getT2total() >= 350 && this.getT2total() <= 399)) {
					bonus += 4;
				}
				if ((this.getT2total() >= 400)) {
					bonus += 5;
				}
				if ((this.getT1wickets() >= 3 && this.getT1wickets() <= 5)) {
					bonus += 1;
				}
				if ((this.getT1wickets() >= 6 && this.getT1wickets() <= 8)) {
					bonus += 2;
				}
				if ((this.getT1wickets() >= 9 && this.getT1wickets() <= 10)) {
					bonus += 3;
				}
			}
		}
		return bonus;
	}

	private int getOCLT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 116
				&& league.getSeriesType().equalsIgnoreCase("One Day") && isEligibleForSpecialBonusPoint()) {

			if (this.teamOne == this.winner) {

				if (this.getT2total() >= ((double) this.newT1Total * 9 / 10)) {
					bonus += 5;
				} else if (this.getT2total() >= ((double) this.newT1Total * 4 / 5)) {
					bonus += 4;
				} else if (this.getT2total() >= ((double) this.newT1Total * 7 / 10)) {
					bonus += 3;
				} else if (this.getT2total() >= ((double) this.newT1Total * 3 / 5)) {
					bonus += 2;
				} else if (this.getT2total() >= ((double) this.newT1Total / 2)) {
					bonus += 1;
				}
				
				if (this.t1wickets >= 10) {
					bonus += 5;
				} else if (this.t1wickets >= 8) {
					bonus += 4;
				} else if (this.t1wickets >= 6) {
					bonus += 3;
				} else if (this.t1wickets >= 4) {
					bonus += 2;
				} else if (this.t1wickets >= 2) {
					bonus += 1;
				}
			}
		}
		return bonus;
	}
	
	private int getTitanT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 32995
				&& isEligibleForSpecialBonusPoint()) {
			
			if (this.teamTwo == this.winner) {
				if (this.isDls) {
					if (this.t2balls>(this.t2RevisedOvers-(this.t2RevisedOvers*5/100))) {
						bonus += 1;
					}
				}else if(this.t2balls>(this.overs*6-(this.overs*6*5/100))) {
					bonus += 1;
				}
			}
			if (this.teamOne == this.winner) {
				if (this.isDls) {
					if (this.t2total<(this.t2Target-(this.t2Target*20/100))) {
						bonus += 1;
					}
				}else if(this.t2total<(this.t1total-(this.t1total*20/100))) {
					bonus += 1;
				}
			}
		}
		return bonus;
	}
	
	private int getJSCCT1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 17156 && isEligibleForSpecialBonusPoint()) {
			if (this.teamTwo == this.winner) {
				bonus += 1;
			}
		}
		return bonus;
	}
	
	private int getMYCST1BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 10268 && isEligibleForSpecialBonusPoint()) {
			if (this.teamOne == this.winner && this.t1balls < 60) {
				bonus += 1;
			}
			if (this.getT1total()>125 ) {
				bonus += 1;
			 }
			if (this.getT2wickets() == (this.getT2PlayersCount()-1)) {
				bonus += 1;
			 }			
		}
		return bonus;
	}
	private int getMYCST2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 10268 && isEligibleForSpecialBonusPoint()) {
			if (this.teamTwo == this.winner &&   this.t2balls < 60) {
				bonus += 1;
			}
			if (this.getT2total()>125 ) {
				bonus += 1;
			 }
			if (this.getT1wickets() == (this.getT1PlayersCount()-1)) {
				bonus += 1;
			 }			
		}
		return bonus;
	}

	private int getMYCT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 2313) {

			if ((this.getT2total() >= 50 && this.getT2total() <= 74)) {
				bonus += 1;
			}
			if ((this.getT2total() >= 75 && this.getT2total() <= 99)) {
				bonus += 2;
			}
			if ((this.getT2total() >= 100 && this.getT2total() <= 124)) {
				bonus += 3;
			}
			if ((this.getT2total() >= 125)) {
				bonus += 4;
			}
			if ((this.getT1wickets() >= 3 && this.getT1wickets() <= 4)) {
				bonus += 1;
			}
			if ((this.getT1wickets() >= 5 && this.getT1wickets() <= 6)) {
				bonus += 2;
			}
			if (this.getT1wickets() >= 7 && (this.getT1wickets() < (this.getT1PlayersCount() - 1))
					&& this.getT1wickets() != 10) {
				bonus += 3;
			} 
			if (this.getT1wickets() >= (this.getT1PlayersCount() - 1) || this.getT1wickets() == 10) {
				bonus += 4;
			}
			if (this.teamTwo == this.winner && this.getT1total() < 50) {

				if (this.getT2wickets() <= 2) {
					bonus += 2;
				}
				if (this.getT1wickets() >= 3) {
					bonus += 1;
				}
			}
			if (this.teamTwo == this.winner && this.getT1total() == 0 && this.getT2total() == 0) {
				bonus += 8;
			}
		}
		return bonus;
	}

	private int getNTCAT2BonusPoints(LeagueDto league, int bonus) {
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 343
				&& isEligibleForSpecialBonusPoint()) {
			int balls = (this.getWinner() == this.getTeamTwo()) ? this.getT2balls() : (this.getOvers()*6);
			if (((((double) this.getT2total() / balls)) * 6) >= 7) {
				bonus += 1;
			}
			if ((this.getT1wickets() >= 10)) {
				bonus += 1;
			}
		}
		return bonus;
	}
	
	private double getBattingBonusPointsT2(LeagueDto league, double bonus) {
		// TODO Auto-generated method stub
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {
			if (this.getT2total() > 75 && this.getT2total() <= 100) {
				bonus +=  0.5;
			} else if (this.getT2total() >= 101 && this.getT2total() <= 125) {
				bonus += 1;
			} else if (this.getT2total() >= 126 && this.getT2total() <= 140) {
				bonus += 1.5;
			} else if (this.getT2total() > 140) {
				bonus += 2;
			}
		}
		return bonus;
	}

	private double getBowllingBonusPointsT2(LeagueDto league, double bonus) {
		// TODO Auto-generated method stub
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {
			if (this.getT1PlayerSize() == 11) {
				if (this.getT1wickets() == 5) {
					bonus += 0.5;
				} else if (this.getT1wickets() == 6) {
					bonus += 1;
				} else if (this.getT1wickets() == 7) {
					bonus +=1;
				} else if (this.getT1wickets() == 8) {
					bonus += 1.5;
				} else if (this.getT1wickets() == 9) {
					bonus += 1.5;
				} else if (this.getT1wickets() == 10) {
					bonus +=2;
				}

			} if (this.getT1PlayerSize() == 10) {
				if (this.getT1wickets() == 5) {
					bonus += 0.5;
				} else if (this.getT1wickets() == 6) {
					bonus += 1;
				} else if (this.getT1wickets() == 7) {
					bonus +=1;
				} else if (this.getT1wickets() == 8) {
					bonus += 1.5;
				} else if (this.getT1wickets() == 9) {
					bonus +=2;
				} 

			} if (this.getT1PlayerSize() == 9) {
				if (this.getT1wickets() == 5) {
					bonus += 0.5;
				} else if (this.getT1wickets() == 6) {
					bonus += 1;
				} else if (this.getT1wickets() == 7) {
					bonus += 1.5;
				} else if (this.getT1wickets() == 8) {
					bonus +=2;
				} 
			}
		}
		return bonus;
	}
	
	private double getSOCBonusPointsT2(LeagueDto league, double bonus) {
		// TODO Auto-generated method stub
		if (league != null && league.getBonusPoints() > 0 && league.getClubId() == 4108
				&& isEligibleForSpecialBonusPoint()) {
			
			if( (this.isDls?   this.getT2Target()<76 : this.getT1total()+1<76) && this.getWinner()== this.getTeamTwo()) {
				if(this.getT2PlayerSize() == 11 && this.getT2wickets()<=5) {
					bonus += 1;
				} else if((this.getT2PlayerSize() == 10 || this.getT2PlayerSize() == 9) && this.getT2wickets()<=4) {
					bonus += 1;
				}
			}
		}
		return bonus;
	}
	private int getT1PlayersCount() {
		if (this.players1 == null || this.players1.isEmpty()) {
			return 11;
		}
		return this.players1.size();
	}

	public String getFow1() {
		return fow1 == null ? "" : fow1;
	}

	public void setFow1(String fow1) {
		this.fow1 = fow1;
	}

	public String getFow2() {
		return fow2 == null ? "" : fow2;
	}

	public void setFow2(String fow2) {
		this.fow2 = fow2;
	}

	public int getT1penalty() {
		return t1penalty;
	}

	public void setT1penalty(int t1penalty) {
		this.t1penalty = t1penalty;
	}

	public int getT2penalty() {
		return t2penalty;
	}

	public void setT2penalty(int t2penalty) {
		this.t2penalty = t2penalty;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(int lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

	public int getMaximumPlayers() {
		return maximumPlayers;
	}

	public void setMaximumPlayers(int maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}

	public String matchLink() {

		return matchLink(false);
	}

	public String matchLink(boolean iDeviceFlag) {

		if (iDeviceFlag) {

			return "match.xhtml";
		}

		if (isLiveMatch() && isInProgress()) {

			return "externalView.xhtml";
		} else {

			return "match.xhtml";
		}
	}

	public String getLiveURL() {
		return liveURL;
	}

	public void setLiveURL(String liveURL) {
		this.liveURL = liveURL;
	}

	public int getT1Wides() {
		return t1Wides;
	}

	public void setT1Wides(int t1Wides) {
		this.t1Wides = t1Wides;
	}

	public int getT2Wides() {
		return t2Wides;
	}

	public void setT2Wides(int t2Wides) {
		this.t2Wides = t2Wides;
	}

	public int getT1noballs() {
		return t1noballs;
	}

	public void setT1noballs(int t1noballs) {
		this.t1noballs = t1noballs;
	}

	public int getT2noballs() {
		return t2noballs;
	}

	public void setT2noballs(int t2noballs) {
		this.t2noballs = t2noballs;
	}

	public int getT1hattricks() {
		return t1hattricks;
	}

	public void setT1hattricks(int t1hattricks) {
		this.t1hattricks = t1hattricks;
	}

	public int getT2hattricks() {
		return t2hattricks;
	}

	public void setT2hattricks(int t2hattricks) {
		this.t2hattricks = t2hattricks;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTimeSinceLastUpdate() {
		return this.timeSinceLastUpdate;
	}

	public void setTimeSinceLastUpdate(String timeSinceLastUpdate) {
		this.timeSinceLastUpdate = timeSinceLastUpdate;
	}

	public int getT1_2total() {
		return t1_2total;
	}

	public void setT1_2total(int t1_2total) {
		this.t1_2total = t1_2total;
	}

	public int getT2_2total() {
		return t2_2total;
	}

	public void setT2_2total(int t2_2total) {
		this.t2_2total = t2_2total;
	}

	public int getT1_2balls() {
		return t1_2balls;
	}

	public void setT1_2balls(int t1_2balls) {
		this.t1_2balls = t1_2balls;
	}

	public int getT2_2balls() {
		return t2_2balls;
	}

	public void setT2_2balls(int t2_2balls) {
		this.t2_2balls = t2_2balls;
	}

	public int getT1_2wickets() {
		return t1_2wickets;
	}

	public void setT1_2wickets(int t1_2wickets) {
		this.t1_2wickets = t1_2wickets;
	}

	public int getT2_2wickets() {
		return t2_2wickets;
	}

	public void setT2_2wickets(int t2_2wickets) {
		this.t2_2wickets = t2_2wickets;
	}

	public int getT1_2penalty() {
		return t1_2penalty;
	}

	public void setT1_2penalty(int t1_2penalty) {
		this.t1_2penalty = t1_2penalty;
	}

	public int getT2_2penalty() {
		return t2_2penalty;
	}

	public void setT2_2penalty(int t2_2penalty) {
		this.t2_2penalty = t2_2penalty;
	}

	public int getT1_2byes() {
		return t1_2byes;
	}

	public void setT1_2byes(int t1_2byes) {
		this.t1_2byes = t1_2byes;
	}

	public int getT2_2byes() {
		return t2_2byes;
	}

	public void setT2_2byes(int t2_2byes) {
		this.t2_2byes = t2_2byes;
	}

	public int getT1_2lbyes() {
		return t1_2lbyes;
	}

	public void setT1_2lbyes(int t1_2lbyes) {
		this.t1_2lbyes = t1_2lbyes;
	}

	public int getT2_2lbyes() {
		return t2_2lbyes;
	}

	public void setT2_2lbyes(int t2_2lbyes) {
		this.t2_2lbyes = t2_2lbyes;
	}

	public String getFow1_2() {
		return fow1_2;
	}

	public void setFow1_2(String fow1_2) {
		this.fow1_2 = fow1_2;
	}

	public String getFow2_2() {
		return fow2_2;
	}

	public void setFow2_2(String fow2_2) {
		this.fow2_2 = fow2_2;
	}

	public int getT1_1total() {
		return t1_1total;
	}

	public void setT1_1total(int t1_1total) {
		this.t1_1total = t1_1total;
	}

	public int getT2_1total() {
		return t2_1total;
	}

	public void setT2_1total(int t2_1total) {
		this.t2_1total = t2_1total;
	}

	public int getT1_1balls() {
		return t1_1balls;
	}

	public void setT1_1balls(int t1_1balls) {
		this.t1_1balls = t1_1balls;
	}

	public int getT2_1balls() {
		return t2_1balls;
	}

	public void setT2_1balls(int t2_1balls) {
		this.t2_1balls = t2_1balls;
	}

	public int getT1_1wickets() {
		return t1_1wickets;
	}

	public void setT1_1wickets(int t1_1wickets) {
		this.t1_1wickets = t1_1wickets;
	}

	public int getT2_1wickets() {
		return t2_1wickets;
	}

	public void setT2_1wickets(int t2_1wickets) {
		this.t2_1wickets = t2_1wickets;
	}

	public int getT1_1penalty() {
		return t1_1penalty;
	}

	public void setT1_1penalty(int t1_1penalty) {
		this.t1_1penalty = t1_1penalty;
	}

	public int getT2_1penalty() {
		return t2_1penalty;
	}

	public void setT2_1penalty(int t2_1penalty) {
		this.t2_1penalty = t2_1penalty;
	}

	public int getT1_1byes() {
		return t1_1byes;
	}

	public void setT1_1byes(int t1_1byes) {
		this.t1_1byes = t1_1byes;
	}

	public int getT2_1byes() {
		return t2_1byes;
	}

	public void setT2_1byes(int t2_1byes) {
		this.t2_1byes = t2_1byes;
	}

	public int getT1_1lbyes() {
		return t1_1lbyes;
	}

	public void setT1_1lbyes(int t1_1lbyes) {
		this.t1_1lbyes = t1_1lbyes;
	}

	public int getT2_1lbyes() {
		return t2_1lbyes;
	}

	public void setT2_1lbyes(int t2_1lbyes) {
		this.t2_1lbyes = t2_1lbyes;
	}

	public int getT1_1Wides() {
		return t1_1Wides;
	}

	public void setT1_1Wides(int t1_1Wides) {
		this.t1_1Wides = t1_1Wides;
	}

	public int getT2_1Wides() {
		return t2_1Wides;
	}

	public void setT2_1Wides(int t2_1Wides) {
		this.t2_1Wides = t2_1Wides;
	}

	public int getT1_1noballs() {
		return t1_1noballs;
	}

	public void setT1_1noballs(int t1_1noballs) {
		this.t1_1noballs = t1_1noballs;
	}

	public int getT2_1noballs() {
		return t2_1noballs;
	}

	public void setT2_1noballs(int t2_1noballs) {
		this.t2_1noballs = t2_1noballs;
	}

	public int getT1_1hattricks() {
		return t1_1hattricks;
	}

	public void setT1_1hattricks(int t1_1hattricks) {
		this.t1_1hattricks = t1_1hattricks;
	}

	public int getT2_1hattricks() {
		return t2_1hattricks;
	}

	public void setT2_1hattricks(int t2_1hattricks) {
		this.t2_1hattricks = t2_1hattricks;
	}

	public int getT1_2Wides() {
		return t1_2Wides;
	}

	public void setT1_2Wides(int t1_2Wides) {
		this.t1_2Wides = t1_2Wides;
	}

	public int getT2_2Wides() {
		return t2_2Wides;
	}

	public void setT2_2Wides(int t2_2Wides) {
		this.t2_2Wides = t2_2Wides;
	}

	public int getT1_2noballs() {
		return t1_2noballs;
	}

	public void setT1_2noballs(int t1_2noballs) {
		this.t1_2noballs = t1_2noballs;
	}

	public int getT2_2noballs() {
		return t2_2noballs;
	}

	public void setT2_2noballs(int t2_2noballs) {
		this.t2_2noballs = t2_2noballs;
	}

	public int getT1_2hattricks() {
		return t1_2hattricks;
	}

	public void setT1_2hattricks(int t1_2hattricks) {
		this.t1_2hattricks = t1_2hattricks;
	}

	public int getT2_2hattricks() {
		return t2_2hattricks;
	}

	public void setT2_2hattricks(int t2_2hattricks) {
		this.t2_2hattricks = t2_2hattricks;
	}

	public int getIsTrump() {
		return isTrump;
	}

	public void setIsTrump(int isTrump) {
		this.isTrump = isTrump;
	}


	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public List<PlayerLiveScoringDto> getNewPlayers1() {
		return newPlayers1;
	}

	public void setNewPlayers1(List<PlayerLiveScoringDto> newPlayers1) {
		this.newPlayers1 = newPlayers1;
	}

	public List<PlayerLiveScoringDto> getNewPlayers2() {
		return newPlayers2;
	}

	public void setNewPlayers2(List<PlayerLiveScoringDto> newPlayers2) {
		this.newPlayers2 = newPlayers2;
	}

	public String getStatus() {
		if(!CommonUtility.isNullOrEmptyOrNULL(status)) {
			return status;
		}else {
			return "";
		}
	}

	public void setStatus(String status) {
		if(!CommonUtility.isNullOrEmptyOrNULL(status)) {
			this.status = status;
		}else {
			this.status = "";
		}
	}

	public String getIsFollowonStr() {
		return isFollowonStr;
	}

	public void setIsFollowonStr(String isFollowonStr) {
		this.isFollowonStr = isFollowonStr;
	}

	/*
	 * public String getLeadTrailByEquation() {
	 * 
	 * if(this.isFollowon == 1) { if(this.t1_2balls>0) { int runsNeeded =
	 * (this.t2_1total+this.t2_2total)-(this.t1_1total+this.t1_2total);
	 * leadTrailByEquation = this.teamOneName+" need "+runsNeeded+" runs to win.";
	 * }else if(this.t2_2balls>0) { int runsDiff =
	 * (this.t2_1total+this.t2_2total)-(this.t1_1total); if(runsDiff>0) {
	 * leadTrailByEquation = this.teamTwoName+" lead by "+runsDiff+" runs."; }else {
	 * leadTrailByEquation = this.teamTwoName+" trail by "+runsDiff*-1+" runs."; }
	 * }else if(this.t2_1balls>0) { int runsDiff = this.t1_1total-this.t2_1total;
	 * if(runsDiff>0) { leadTrailByEquation =
	 * this.teamTwoName+" lead by "+runsDiff+" runs."; }else { leadTrailByEquation =
	 * this.teamTwoName+" trail by "+runsDiff*-1+" runs."; } } }else {
	 * if(this.t2_2balls>0) { int runsNeeded =
	 * (this.t1_1total+this.t1_2total)-(this.t2_1total+this.t2_2total);
	 * leadTrailByEquation = this.teamTwoName+" need "+runsNeeded+" runs to win.";
	 * }else if(this.t1_2balls>0) { int runsDiff =
	 * (this.t1_1total+this.t1_2total)-(this.t2_1total); if(runsDiff>0) {
	 * leadTrailByEquation = this.teamOneName+" lead by "+runsDiff+" runs."; }else {
	 * leadTrailByEquation = this.teamOneName+" trail by "+runsDiff*-1+" runs."; }
	 * }else if(this.t2_1balls>0) { int runsDiff = this.t2total-this.t1total;
	 * if(runsDiff>0) { leadTrailByEquation =
	 * this.teamTwoName+" lead by "+runsDiff+" runs."; }else { leadTrailByEquation =
	 * this.teamTwoName+" trail by "+runsDiff*-1+" runs."; } } } return
	 * leadTrailByEquation; }
	 */
	/**
	 * @return
	 */
	public String getLeadTrailByEquation() {
		
		if(this.seriesType.contains("Test") || this.seriesType.contains("2X")) {			
			if(this.currentInnings == 4) {
				if(this.isFollowon == 1) {
					int runsNeeded = (this.t2_1total+this.t2_2total)-(this.t1_1total+this.t1_2total);
					leadTrailByEquation = this.teamOneName+" need "+(runsNeeded+1)+" runs to win.";					
				}else {
					int runsNeeded = (this.t1_1total+this.t1_2total)-(this.t2_1total+this.t2_2total);
					leadTrailByEquation = this.teamTwoName+" need "+(runsNeeded+1)+" runs to win.";
				}				
			}else if(this.currentInnings == 3) {
				if(this.isFollowon == 1) {
					int runsDiff = (this.t2_1total+this.t2_2total)-this.t1_1total;
					if(runsDiff>=0) {
						leadTrailByEquation = this.teamTwoName+" lead by "+runsDiff+" runs.";
					}else {
						leadTrailByEquation = this.teamTwoName+" trail by "+runsDiff*-1+" runs.";
					}
				}else {
					int runsDiff = (this.t1_1total+this.t1_2total)-this.t2_1total;
					if(runsDiff>=0) {
						leadTrailByEquation = this.teamOneName+" lead by "+runsDiff+" runs.";
					}else {
						leadTrailByEquation = this.teamOneName+" trail by "+runsDiff*-1+" runs.";
					}
				}				
			}else if(this.currentInnings == 2) {
				int runsDiff = this.t2_1total-this.t1_1total;
				if(runsDiff>=0) {
					leadTrailByEquation = this.teamTwoName+" lead by "+runsDiff+" runs.";
				}else {
					leadTrailByEquation = this.teamTwoName+" trail by "+runsDiff*-1+" runs.";
				}
			}
		}
	return leadTrailByEquation;
}

	public void setLeadTrailByEquation(String leadTrailByEquation) {
		this.leadTrailByEquation = leadTrailByEquation;
	}

	public int getCurrentInnings() {
		return currentInnings;
	}

	public void setCurrentInnings(int currentInnings) {
		this.currentInnings = currentInnings;
	}

	public int getSuperOverCurrentInnings() {
		return superOverCurrentInnings;
	}

	public void setSuperOverCurrentInnings(int superOverCurrentInnings) {
		this.superOverCurrentInnings = superOverCurrentInnings;
	}

	public String getSrcSite() {
		return srcSite;
	}

	public void setSrcSite(String srcSite) {
		this.srcSite = srcSite;
	}

	public int getSrcLeagueId() {
		return srcLeagueId;
	}

	public void setSrcLeagueId(int srcLeagueId) {
		this.srcLeagueId = srcLeagueId;
	}

	public long getSrcMatchId() {
		return srcMatchId;
	}

	public void setSrcMatchId(long srcMatchId) {
		this.srcMatchId = srcMatchId;
	}

	public int getT1PlayerSize() {
		return t1PlayerSize;
	}

	public void setT1PlayerSize(int t1PlayerSize) {
		this.t1PlayerSize = t1PlayerSize;
	}

	public int getT2PlayerSize() {
		return t2PlayerSize;
	}

	public void setT2PlayerSize(int t2PlayerSize) {
		this.t2PlayerSize = t2PlayerSize;
	}

}
