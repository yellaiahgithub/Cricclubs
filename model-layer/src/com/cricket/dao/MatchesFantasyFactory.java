package com.cricket.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cricket.dto.ContestWinningSummaryDto;
import com.cricket.dto.MatchesFantasysDto;
import com.cricket.dto.PlayerFantasyPoints;
import com.cricket.dto.PlayerFantasyPointsFB;
import com.cricket.utility.CommonUtility;

public class MatchesFantasyFactory {
	
	public static final int UPCOMING = 1;
	public static final int INPROGRESS = 2;	
	public static final int COMPLETED = 3;
	public static final int ARCHIVED = 4;
	public static final String MATCH_STATUS_UPDATED = "match_status_updated";
	public static final String CONTEST_STATUS_UPDATED = "contest_status_updated";

	private static MatchesFantasyDAO matchesFantasyDAO = null;

	private static MatchesFantasyDAO getDaoInstance() {
		if (matchesFantasyDAO == null) {
			matchesFantasyDAO = new MatchesFantasyDAO();
		}
		return matchesFantasyDAO;
	}

	public static List<MatchesFantasysDto> getFantasyMatchesNew( int sportId, String sportCategory, List<String> seriesNames, 
			List<String> teamNames, int status, long userId, 
			int myContests, String fantasyCurrency, int limit, int skip ) throws Exception {
		
		return getDaoInstance().getFantasyMatchesNew( sportId, sportCategory, seriesNames, teamNames, status, 0, 
				userId, myContests, fantasyCurrency, limit, skip);
	}
	
	public static List<MatchesFantasysDto> getFantasyMatchesMX(int status, int limit, int skip) throws Exception {		
		return getDaoInstance().getFantasyMatchesMX(1, 0, 0);
	}
	
	public static List<MatchesFantasysDto> getUserCompletedMatches(long userId, int sportId, int limit, int skip) throws Exception {		
		return getDaoInstance().getUserCompletedMatches(userId, sportId, limit, skip);
	}
	
	public static List<MatchesFantasysDto> getFantasyMatchesNew( int sportId, String sportCategory, List<String> seriesNames, 
			List<String> teamNames, int status, long userId, 
			int myContests, String fantasyCurrency) throws Exception {
		return getDaoInstance().getFantasyMatchesNew( sportId, sportCategory, seriesNames, teamNames, status, 0, 
				userId, myContests, fantasyCurrency, 0, 0);
	}
	
	public static List<Integer> getUpcomingMatchIds() throws Exception {
		return getDaoInstance().getUpcomingMatchIds();
	}
	
	public static List<MatchesFantasysDto> getFantasyMatches(int ccClubId,String sportCategory, String seriesName, 
			String teamName, int status, String fromDate, String toDate  ) throws Exception {
		return getDaoInstance().getFantasyMatches(0, sportCategory, seriesName, teamName, status,0,0,ccClubId,0,fromDate, toDate);
	}
	
	public static Map<Integer, String> getFantasyMatchesForUpdatingUserBalance() throws Exception {
		return getDaoInstance().getFantasyMatchesForUpdatingUserBalance();
	}
	
	public static String updateUserCashBalanceForMatch(int matchid) throws Exception {
		return getDaoInstance().updateUserCashBalanceForMatch(matchid);
	}
	
	public static String updateUserCoinsBalanceForMatch(int matchid) throws Exception {
		return getDaoInstance().updateUserCoinsBalanceForMatch(matchid);
	}
	
	public static List<Integer> getAllFantasyCCFixtureIds(int clubId) throws Exception {
		return getDaoInstance().getAllFantasyCCFixtureIds(clubId);
	}
	
	public static MatchesFantasysDto getFantasyMatchById(int matchId) throws Exception {
		
		List<MatchesFantasysDto> matches =  getDaoInstance().getFantasyMatches(0,null,null,null,0,matchId,0,0,0,null,null);
		 
		if(!CommonUtility.isListNullEmpty(matches)) {
			return matches.get(0);
		}
		return null;
	}
	
	public static void archiveFantasyMatchesData(int status, int archiveStatus, int noOfDays) throws Exception {		
		 getDaoInstance().archiveFantasyMatchesData(status, archiveStatus, noOfDays);		 
	}
	
	public static int getFirstLastMatchIdOfTournament(int tournamentId, String firstOrLast) throws Exception {		
		return  getDaoInstance().getFirstLastMatchIdOfTournament(tournamentId, firstOrLast);		 
	}
	
	public static int getLatestMatchIdScoredForTournament(int tournamentId) throws Exception {		
		return  getDaoInstance().getLatestMatchIdScoredForTournament(tournamentId);		 
	}
	
	public static int getSeriesLevelMatchIdOfTournament(int tournamentId, int isTeamEditable) throws Exception {		
		return  getDaoInstance().getSeriesLevelMatchIdOfTournament(tournamentId,isTeamEditable);		 
	}
	
	public static MatchesFantasysDto getFantasyMatchByFixtureId(int ccFixtureId, int ccClubId) throws Exception {
		
		List<MatchesFantasysDto> matches =  getDaoInstance().getFantasyMatches(0,null,null,null,0,0,ccFixtureId,ccClubId,0,null,null);
		 
		if(!CommonUtility.isListNullEmpty(matches)) {
			return matches.get(0);
		}
		return null;
	}
	
	public static int getFantasyMatchStatusByContestIdOrInviteCode(int contestId, String inviteCode) throws Exception {		
		return getDaoInstance().getFantasyMatchStatusByContestIdOrInviteCode(contestId, inviteCode);
	}
	
	public static List<String> getSeriesNames(String seriesType, int Status, int sportId) throws Exception {
		return getDaoInstance().getSeriesNames(seriesType,Status,sportId, 0);
	}
	
	public static List<String> getSeriesNames(String seriesType, int Status, int sportId, int clubId) throws Exception {
		return getDaoInstance().getSeriesNames(seriesType,Status,sportId, clubId);
	}
	
	public static List<Integer> getFantasyMatchPlayerIds(int fantasyMatchId) throws Exception {
		return getDaoInstance().getFantasyMatchPlayerIds(fantasyMatchId);
	}
	
	public static List<Integer> getTournamentMatchPlayerIds(int tournamentId) throws Exception {
		return getDaoInstance().getTournamentMatchPlayerIds(tournamentId);
	}
	
	public static Map<String, Integer> getSportsMap() throws Exception {
		return getDaoInstance().getSportsMap();
	}
	public static List<String> getLeagueNames(int sportId) throws Exception {
		return getDaoInstance().getLeagueNames(sportId);
	}
	
	public static Map<Integer,String> getLeagueNameMap(int sportId) throws Exception {
		return getDaoInstance().getLeagueNameMap(sportId);
	}
	
	public static List<String> getSeriesTypes(int sportId) throws Exception {
		return getDaoInstance().getSeriesTypes(sportId);
	}
	
	public static Map<Integer,String> getSeriesTypeMap(int sportId) throws Exception {
		return getDaoInstance().getSeriesTypeMap(sportId);
	}
	
	public static Map<Integer, List<Integer>> getSeriesMasterContestIdsMap() throws Exception {
		return getDaoInstance().getSeriesMasterContestIdsMap();
	}
	
	public static int getSeriesTypeIdByName(int sportId, String name) throws Exception {
		return getDaoInstance().getSeriesTypeId(sportId,name);
	}
	
	public static List<String> getMatchTitles(String contestType, int contestStatus) throws Exception {
		return getDaoInstance().getMatchTitles(contestType,contestStatus);
	}
	
	public static List<String> getTeamNames(int Status, int sportId, String seriesName) throws Exception {
		return getDaoInstance().getTeamNames(Status,sportId,seriesName);
	}
	
	public static List<String> getTeamNamesWeb(int Status, int sportId, String seriesName) throws Exception {
		return getDaoInstance().getTeamNamesWeb(Status,sportId,seriesName);
	}
	
	public static Map<Integer,Integer> getMatchPlayerSeriesPointsMap(MatchesFantasysDto match) throws Exception {
		return getDaoInstance().getMatchPlayerSeriesPointsMap(match);
	}
	
	public static Map<Integer,Integer> getPlayersTournamentPointsMap(int tournamentId) throws Exception {
		return getDaoInstance().getPlayersTournamentPointsMap(tournamentId);
	}
	
	public static int copyMatchesToFantasy(List<MatchesFantasysDto> matchesList) throws Exception{
		return getDaoInstance().copyMatchesToFantasy(matchesList);
	}
	
	public static void insertFantasyTeamPlayers(int clubId, int teamId) throws Exception{
		getDaoInstance().insertFantasyTeamPlayers(clubId, teamId);
	}
	
	public static int checkCCMatchExistsInFantasy(int clubId, int seriesId, int ccMatchId) throws Exception{
		return getDaoInstance().checkCCMatchExistsInFantasy(clubId, seriesId, ccMatchId);
	}
	
	public static void updateFantasyMatch(MatchesFantasysDto fantasyMatch) throws Exception {
		getDaoInstance().updateFantasyMatch(fantasyMatch);
	}
	
	public static void updateFantasyMatchTeamDetails(String teamName, String teamCode, int fantasyTeamId) throws Exception {
		getDaoInstance().updateFantasyMatchTeamDetails(teamName,teamCode,fantasyTeamId );
	}
	
	public static void updateFantasyMatchTeamLogoPath(String teamLogoPath, int fantasyTeamId) throws Exception {
		getDaoInstance().updateFantasyMatchTeamLogoPath(teamLogoPath,fantasyTeamId);
	}
	
	public static void updateFantasyMatchResult(int fantasyMatchId,String result) throws Exception {
		getDaoInstance().updateFantasyMatchResult(fantasyMatchId,result);
	}
	
	public static void updateSeriesMatchTeamImagePaths(int tournamentId,String imagePath) throws Exception {
		getDaoInstance().updateSeriesMatchTeamImagePaths(tournamentId,imagePath);
	}
	
	public static void updateFantasyMatchCCMatchId(int fantasyMatchId,int ccMatchId) throws Exception {
		getDaoInstance().updateFantasyMatchCCMatchId(fantasyMatchId,ccMatchId);
	}
	
	public static void updateFantasyMatchStatusAndCCMatchId(int fantasyMatchId, int ccMatchId, int status) throws Exception {
		getDaoInstance().updateFantasyMatchStatusAndCCMatchId(fantasyMatchId, ccMatchId, status);
	}
	
	public static void updateFantasyMatchStatus(int fantasyMatchId,int status) throws Exception {
		getDaoInstance().updateFantasyMatchStatus(fantasyMatchId,status);
	}
	
	public static void updateSeriesLevelMatchesStatus(int tournamentId, int status) throws Exception {
		getDaoInstance().updateSeriesLevelMatchesStatus(tournamentId, status);
	}
	
	public static void updateMatchPlayingXISelectedStatus(int clubId, int ccFixtureId) throws Exception {
		getDaoInstance().updateMatchPlayingXISelectedStatus(clubId, ccFixtureId);
	}
	
	public static int checkMatchHasContest(int matchId) throws Exception{
		return getDaoInstance().checkMatchHasContest(matchId);
	}
	
	public static int checkMatchHasUserTeam(int matchId) throws Exception{
		return getDaoInstance().checkMatchHasUserTeam(matchId);
	}
	
	public static void deleteFantasyMatch(int matchId) throws Exception {
		getDaoInstance().deleteFantasyMatch(matchId);
	}

	public static void deleteFantasyMatchPlayers(int fantasyMatchId) throws Exception {
		getDaoInstance().deleteFantasyMatchPlayers(fantasyMatchId);
	}

	public static int insertFantasyMatchPlayers(Collection<PlayerFantasyPoints> playerPoints, int ccMatchId,
			int fantasyMatchId) throws Exception {
		return getDaoInstance().insertFantasyMatchPlayers(playerPoints,ccMatchId,fantasyMatchId);
	}
	
	public static int insertFantasyMatchPlayersFB(Collection<PlayerFantasyPointsFB> playerPoints, int ccMatchId,
			int fantasyMatchId) throws Exception {
		return getDaoInstance().insertFantasyMatchPlayersFB(playerPoints,ccMatchId,fantasyMatchId);
	}
	
	public static  String updateTeamPointsAndLeaderBoard(int matchId) throws Exception {
		return getDaoInstance().updateTeamPointsAndLeaderBoard(matchId);
	}
	
	public static String updateDreamTeamsMatchPointsAndLeaderBoard(int matchId, int tournamentId) throws Exception {
		return getDaoInstance().updateDreamTeamsMatchPointsAndLeaderBoard(matchId, tournamentId);
	}
	
	public static String updateDynamicTeamsMatchPointsAndLeaderBoard(int matchId, int tournamentId) throws Exception {
		return getDaoInstance().updateDynamicTeamsMatchPointsAndLeaderBoard(matchId, tournamentId);
	}
	
	public static void abadonFantasyMatch(int matchId) throws Exception {
		getDaoInstance().abadonFantasyMatch(matchId);
	}
	
	public static List<ContestWinningSummaryDto> getSummaryOfContestWinningsForMatch(int matchId) throws Exception {		
		return  getDaoInstance().getSummaryOfContestWinningsForMatch(matchId);		 
	}
	
	public static List<ContestWinningSummaryDto> getContestIssueWithLeaderBoardForMatch(int matchId) throws Exception {		
		return  getDaoInstance().getContestIssueWithLeaderBoardForMatch(matchId);		 
	}
	
	public static List<String> getFantasyMatchRunningStatus(int fantasyMatchId) throws Exception {		
		return  getDaoInstance().getFantasyMatchRunningStatus(fantasyMatchId);		 
	}
	
	public static void addFantasyMatchRunningStatus(int fantasyMatchId, String status) throws Exception {
		getDaoInstance().addFantasyMatchRunningStatus(fantasyMatchId, status);
	}
	
	public static Map<Integer, String> getIdTitleMapForUpcomingAndInpogressMatches() throws Exception {
		return getDaoInstance().getIdTitleMapForUpcomingAndInpogressMatches();
	}
}
