package com.cricket.cricstaz;

public class CSMatch {

	private String League;
	private String AutoAddPlayers;
	private String AutoAddOthers;
	private String ExternalMatchID;
	private String MatchType;
	private CSDate DatePlayed;
	private String Ground;
	private String Grade;
	private String HomeTeam;
	private String AwayTeam;
	private String HomeClub;
	private String AwayClub;
	private CSUserInfo HomeCaptain;
	private CSUserInfo AwayCaptain;
	private CSUserInfo HomeKeeper;
	private CSUserInfo AwayKeeper;
	private CSUserInfo Umpire1;
	private CSUserInfo Umpire2;
	private CSUserInfo Umpire3;
	private CSUserInfo Scorer1;
	private CSUserInfo Scorer2;
	private CSUserInfo Scorer3;
	private String Comment;
	private String Round;
	private String Final;
	private String GrandFinal;
	private String Toss;
	private String Result;
	private String SubResult;
	private String Margin;
	private String HomePoints;
	private String AwayPoints;
	private CSInnings Innings1;
	private CSInnings Innings2;
	private CSInnings Innings3;
	private CSInnings Innings4;
	
	public CSMatch() {
		super();
	}

	public CSMatch(String league, String autoAddPlayers, String autoAddOthers, String externalMatchID, String matchType,
			CSDate datePlayed, String ground, String grade, String homeTeam, String awayTeam, String homeClub,
			String awayClub, CSUserInfo homeCaptain, CSUserInfo awayCaptain, CSUserInfo homeKeeper,
			CSUserInfo awayKeeper, CSUserInfo umpire1, CSUserInfo umpire2, CSUserInfo umpire3, CSUserInfo scorer1,
			CSUserInfo scorer2, CSUserInfo scorer3, String comment, String round, String final1, String grandFinal,
			String toss, String result, String subResult, String margin, String homePoints, String awayPoints,
			CSInnings innings1, CSInnings innings2, CSInnings innings3, CSInnings innings4) {
		super();
		League = league;
		AutoAddPlayers = autoAddPlayers;
		AutoAddOthers = autoAddOthers;
		ExternalMatchID = externalMatchID;
		MatchType = matchType;
		DatePlayed = datePlayed;
		Ground = ground;
		Grade = grade;
		HomeTeam = homeTeam;
		AwayTeam = awayTeam;
		HomeClub = homeClub;
		AwayClub = awayClub;
		HomeCaptain = homeCaptain;
		AwayCaptain = awayCaptain;
		HomeKeeper = homeKeeper;
		AwayKeeper = awayKeeper;
		Umpire1 = umpire1;
		Umpire2 = umpire2;
		Umpire3 = umpire3;
		Scorer1 = scorer1;
		Scorer2 = scorer2;
		Scorer3 = scorer3;
		Comment = comment;
		Round = round;
		Final = final1;
		GrandFinal = grandFinal;
		Toss = toss;
		Result = result;
		SubResult = subResult;
		Margin = margin;
		HomePoints = homePoints;
		AwayPoints = awayPoints;
		Innings1 = innings1;
		Innings2 = innings2;
		Innings3 = innings3;
		Innings4 = innings4;
	}

	public String getLeague() {
		return League;
	}

	public void setLeague(String league) {
		League = league;
	}

	public String getAutoAddPlayers() {
		return AutoAddPlayers;
	}

	public void setAutoAddPlayers(String autoAddPlayers) {
		AutoAddPlayers = autoAddPlayers;
	}

	public String getAutoAddOthers() {
		return AutoAddOthers;
	}

	public void setAutoAddOthers(String autoAddOthers) {
		AutoAddOthers = autoAddOthers;
	}

	public String getExternalMatchID() {
		return ExternalMatchID;
	}

	public void setExternalMatchID(String externalMatchID) {
		ExternalMatchID = externalMatchID;
	}

	public String getMatchType() {
		return MatchType;
	}

	public void setMatchType(String matchType) {
		MatchType = matchType;
	}

	public CSDate getDatePlayed() {
		return DatePlayed;
	}

	public void setDatePlayed(CSDate datePlayed) {
		DatePlayed = datePlayed;
	}

	public String getGround() {
		return Ground;
	}

	public void setGround(String ground) {
		Ground = ground;
	}

	public String getGrade() {
		return Grade;
	}

	public void setGrade(String grade) {
		Grade = grade;
	}

	public String getHomeTeam() {
		return HomeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		HomeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return AwayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		AwayTeam = awayTeam;
	}

	public String getHomeClub() {
		return HomeClub;
	}

	public void setHomeClub(String homeClub) {
		HomeClub = homeClub;
	}

	public String getAwayClub() {
		return AwayClub;
	}

	public void setAwayClub(String awayClub) {
		AwayClub = awayClub;
	}

	public CSUserInfo getHomeCaptain() {
		return HomeCaptain;
	}

	public void setHomeCaptain(CSUserInfo homeCaptain) {
		HomeCaptain = homeCaptain;
	}

	public CSUserInfo getAwayCaptain() {
		return AwayCaptain;
	}

	public void setAwayCaptain(CSUserInfo awayCaptain) {
		AwayCaptain = awayCaptain;
	}

	public CSUserInfo getHomeKeeper() {
		return HomeKeeper;
	}

	public void setHomeKeeper(CSUserInfo homeKeeper) {
		HomeKeeper = homeKeeper;
	}

	public CSUserInfo getAwayKeeper() {
		return AwayKeeper;
	}

	public void setAwayKeeper(CSUserInfo awayKeeper) {
		AwayKeeper = awayKeeper;
	}

	public CSUserInfo getUmpire1() {
		return Umpire1;
	}

	public void setUmpire1(CSUserInfo umpire1) {
		Umpire1 = umpire1;
	}

	public CSUserInfo getUmpire2() {
		return Umpire2;
	}

	public void setUmpire2(CSUserInfo umpire2) {
		Umpire2 = umpire2;
	}

	public CSUserInfo getUmpire3() {
		return Umpire3;
	}

	public void setUmpire3(CSUserInfo umpire3) {
		Umpire3 = umpire3;
	}

	public CSUserInfo getScorer1() {
		return Scorer1;
	}

	public void setScorer1(CSUserInfo scorer1) {
		Scorer1 = scorer1;
	}

	public CSUserInfo getScorer2() {
		return Scorer2;
	}

	public void setScorer2(CSUserInfo scorer2) {
		Scorer2 = scorer2;
	}

	public CSUserInfo getScorer3() {
		return Scorer3;
	}

	public void setScorer3(CSUserInfo scorer3) {
		Scorer3 = scorer3;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getRound() {
		return Round;
	}

	public void setRound(String round) {
		Round = round;
	}

	public String getFinal() {
		return Final;
	}

	public void setFinal(String final1) {
		Final = final1;
	}

	public String getGrandFinal() {
		return GrandFinal;
	}

	public void setGrandFinal(String grandFinal) {
		GrandFinal = grandFinal;
	}

	public String getToss() {
		return Toss;
	}

	public void setToss(String toss) {
		Toss = toss;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getSubResult() {
		return SubResult;
	}

	public void setSubResult(String subResult) {
		SubResult = subResult;
	}

	public String getMargin() {
		return Margin;
	}

	public void setMargin(String margin) {
		Margin = margin;
	}

	public String getHomePoints() {
		return HomePoints;
	}

	public void setHomePoints(String homePoints) {
		HomePoints = homePoints;
	}

	public String getAwayPoints() {
		return AwayPoints;
	}

	public void setAwayPoints(String awayPoints) {
		AwayPoints = awayPoints;
	}

	public CSInnings getInnings1() {
		return Innings1;
	}

	public void setInnings1(CSInnings innings1) {
		Innings1 = innings1;
	}

	public CSInnings getInnings2() {
		return Innings2;
	}

	public void setInnings2(CSInnings innings2) {
		Innings2 = innings2;
	}

	public CSInnings getInnings3() {
		return Innings3;
	}

	public void setInnings3(CSInnings innings3) {
		Innings3 = innings3;
	}

	public CSInnings getInnings4() {
		return Innings4;
	}

	public void setInnings4(CSInnings innings4) {
		Innings4 = innings4;
	}
	
}
