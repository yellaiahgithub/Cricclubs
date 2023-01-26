package com.cricket.cricstaz;

public class CricStazCommonStatics {
	// Out Types
	/*
	  Howouts: 0=dnb, 1=not out, 2=bowled, 3=ct, 4=c&b, 5=ct behind, 6=lbw,
	  7=runout, 8=st, 9=hit wkt, 10=obstructed, 11=handled ball, 12=hit wkt,
	  20=retired, 21=absent, 22=retired out, 23=timed out
	 */
	final static String OT_DID_NOT_BAT = "0";
	final static String OT_NOT_OUT = "1";
	final static String OT_BOWLED = "2";
	final static String OT_CATCH = "3";
	final static String OT_CAUGHT_BOWLED = "4";
	final static String OT_CATCH_BEHIND = "5";
	final static String OT_LBW = "6";
	final static String OT_RUN_OUT = "7";
	final static String OT_STUMP = "8";
	final static String OT_HIT_WICKET = "9";
	final static String OT_OBSTRUCTED = "10";
	final static String OT_HANDLED_BALL = "11";
	final static String OT_HIT_WICKET_1 = "12";
	final static String OT_RETIRED = "20";
	final static String OT_ABSENT = "21";
	final static String OT_RETIRED_OUT = "22";
	final static String OT_TIMED_OUT = "23";
	
	// Match Types - Match types: 1=1 innings, 2=2 innings, 3=t20
	final static String MATCH_TYPE_1INNINGS = "1";
	final static String MATCH_TYPE_2INNINGS = "2";
	final static String MATCH_TYPE_T20 = "3";
	
	// Configurations 
	// AutoAddPlayers: 1=add new player records when they can't be matched, 0=don't add players
	final static String AUTO_ADD_PLAYERS_TRUE = "1";
	final static String AUTO_ADD_PLAYERS_FALSE = "0";
	final static String AUTO_ADD_OTHER_DETAILS_TRUE = "1";
	final static String AUTO_ADD_OTHER_DETAILS_FALSE = "0";
	// Toss: 0=Hometeam,1=Awayteam,2=unknown
	final static String TOSS_WIN_HOME_TEAM = "0";
	final static String TOSS_WIN_AWAY_TEAM = "1";
	final static String TOSS_WIN_UNKNOWN = "2";
	
	//Result: 1=Hometeam win, 2=Awayteam win, 3=draw, 4=no result, 5=Hometeam forfeit, 
	// 6=Awayteam forfeit, 7=in progress, 8=abandoned, 9=tied
	final static String MATCH_WIN_TYPE_HOME_TEAM = "1";
	final static String MATCH_WIN_TYPE_AWAY_TEAM = "2";
	final static String MATCH_WIN_TYPE_DRAW = "3";
	final static String MATCH_WIN_TYPE_NO_RESULT = "4";
	final static String MATCH_WIN_TYPE_HOMETEAM_FORFOIT = "5";
	final static String MATCH_WIN_TYPE_AWAY_FORFOIT = "6";
	final static String MATCH_WIN_TYPE_INPROGRESS = "7";
	final static String MATCH_WIN_TYPE_ABANDONED = "8";
	final static String MATCH_WIN_TYPE_TIED = "9";
	
	public static final String ABANDONE_TYPE_ABANDONED = "abandoned";
	public static final String ABANDONE_TYPE_SUPEROVER = "superover";
	public static final String ABANDONE_TYPE_FOREFEIT = "forefeit";
	public static final String ABANDONE_TYPE_DRAW = "draw";
}
