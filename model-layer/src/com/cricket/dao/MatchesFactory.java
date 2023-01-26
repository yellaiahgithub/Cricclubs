package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.DeletedMatchDto;
import com.cricket.dto.LiveScoreActionsDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.Pair;
import com.cricket.dto.insights.BowlingOverComparisonDTO;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.utility.ApplicationConstants;
import com.cricket.utility.CommonUtility;
import com.football.dto.PlayerMatchDtoFB;
import com.google.gson.Gson;

public class MatchesFactory {
	
	private static MatchDAO matchDao = null;
	
	private static MatchDAO getDaoInstance(){
		if(matchDao == null){
			matchDao = new MatchDAO();
		}
		return matchDao;
	}

	public static MatchDto getMatch(long matchId,int clubId) throws Exception{
		MatchDto match = getDaoInstance().getMatchByMatchId(matchId,clubId);
		if(match != null && match.getLastUpdatedBy() != 0){
			String userFullName = UserFactory.getUserFullNameById(match.getLastUpdatedBy());			
			if(!CommonUtility.isNullOrEmpty(userFullName)){
				match.setLastUpdatedByName(userFullName);
			}
		}
		return match;
	}
	
	public static List<MatchDto> getClubAllLiveMatches(int clubId , int limit) throws Exception{
		return getDaoInstance().getClubAllLiveMatches(clubId,limit);
	}
	
	public static List<MatchDto> getAllMatches(int limit,int clubId) throws Exception{
		return getDaoInstance().getMatches(0,limit,clubId,0,0,0,null,0, "",0, null, null,0);
	}
	
	public static List<MatchDto> getAllMatches(String date,int clubId) throws Exception{
		return getDaoInstance().getMatches(0,0,clubId,0,0,0,date,0, "",0,null,null,0);
	}
	
	public static List<MatchDto> getMatchesByTeamandDates(int teamId,int leagueId,int clubId, String internalClubId, 
			String fromDate, String toDate,int year) throws Exception{
		return getDaoInstance().getMatches(0,0, clubId, leagueId, teamId,0,null,0, internalClubId,0, fromDate, toDate,year);
	}
	
	public static List<MatchDto> getMatchesByTeam(int teamId,int leagueId,int clubId, String internalClubId) throws Exception{
		return getDaoInstance().getMatches(0,0, clubId, leagueId, teamId,0,null,0, internalClubId,0, null, null,0);
	}
	
	public static List<MatchDto> getAllMatchesAPI(int teamId,int leagueId,int clubId, String internalClubId, int limit, int skip) throws Exception{
		return getDaoInstance().getMatches(skip, limit, clubId, leagueId, teamId,0,null,0, internalClubId, 0, null, null,0);
	}
	
	public static List<LiveScoreActionsDto> liveScoreActionsData(int matchId,int clubId) throws Exception{
		return getDaoInstance().liveScoreActionsData(matchId,clubId);
	}
	public static List<MatchDto> getMatchesByStatus(int teamId,int leagueId,int clubId, String internalClubId, 
			int limit, int skip, int status) throws Exception{
		return getDaoInstance().getMatches(skip, limit, clubId, leagueId, teamId,0,null,0, internalClubId, status, null, null,0);
	}
	
	public static List<MatchDto> getMatchesByGround(int groundId,int clubId) throws Exception{
		return getDaoInstance().getMatches(0, 0, clubId, 0, 0,0,null,groundId, "",0,null,null,0);
	}

	public static List<MatchDto> getMatchesByLeagueandDates(int leagueId,int clubId, String internalClubId, 
			String fromDate, String toDate, int year) throws Exception{
		return getDaoInstance().getMatches(0, 0, clubId, leagueId, 0,0,null,0, "",0,fromDate, toDate,year);
	}
	
	public static List<MatchDto> getMatchesByLeague(int leagueId,int clubId, String internalClubId) throws Exception{
		return getDaoInstance().getMatches(0, 0, clubId, leagueId, 0,0,null,0, "",0,null, null,0);
	}
	
	public static Map<String, Integer> getPlayerIdListForLeagueMatches(int clubId, String matchIdsStr) throws Exception{
		return getDaoInstance().getPlayerIdListForLeagueMatches(clubId, matchIdsStr);
	}
	
	public static List<MatchDto> getLastWeekMatchesByLeague(int leagueId,int clubId) throws Exception{
		return getDaoInstance().getMatches(0, 0, clubId, leagueId, 0,7,null,0, "",0, null, null,0);
	}

	public static List<MatchDto> getLastWeekMatches(int clubId) throws Exception{
		return getDaoInstance().getMatches(0, 0, clubId, 0, 0,6,null,0, "",0, null, null,0);
	}

	public static List<MatchDto> getAllMatchs(int clubId) throws Exception{
		return getDaoInstance().getMatches(0, 0,clubId,0,0,0,null,0, "",0, null, null,0);
	}
	
	public static List<MatchDto> getMatcheBasicDetails(int clubId) throws Exception{
		return getDaoInstance().getMatcheBasicDetails(clubId);
	}
	
	public static Integer getFootBallMatchCountByPlayerId(int playerId, List<Integer> clubIds) throws Exception{
		return getDaoInstance().getFootBallMatchCountByPlayerId(playerId, clubIds);
	}

	public static int createMatch(MatchDto dto,int clubId) throws Exception{
		 return createMatch(dto,clubId,null);
	}
	
	public static int createMatch(MatchDto dto,int clubId,Map<Integer,Integer> PlayersBattingPosition) throws Exception{
		if(dto.getTeamOne() ==0 || dto.getTeamTwo() == 0){			
			throw new CCException("Both teams are required to create a match.", CCErrorConstant.BOTH_TEAM_NAMES_REQUIRED);
		}
		swapTeamsBasedOnToss(dto);
		return getDaoInstance().insertMatch(dto,clubId,PlayersBattingPosition);
	}
	
	
	public static void restoreMatch(MatchDto dto,int clubId) throws Exception{
		getDaoInstance().restoreMatch(dto,clubId);
	}

	public static void updateMatch(MatchDto dto,int clubId) throws Exception{
		swapTeamsBasedOnToss(dto);
		getDaoInstance().updateMatch(dto,clubId);
	}
	public static void deleteMatch(long matchId,int clubId, int userId) throws Exception{
		getDaoInstance().deleteMatch(matchId,clubId, userId);
		FixturesFactory.deleteMatch((new Long(matchId)).intValue(), clubId);
		ScoringFactory.deleteSummaryIReportingDB(clubId,CommonUtility.longToInt(matchId));
	}

	public static void auditDeleteMatch(long matchId, int clubId, int userId) throws Exception {

		DeletedMatchDto auditMatch = new DeletedMatchDto();
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);

		auditMatch.setMatch(match);
		auditMatch.setFixture(FixturesFactory.getFixtureForMatch((int) matchId, clubId));
		auditMatch.setInnings(ScoringFactory.getInningsListForMatch((int) matchId, clubId));

		String matchJson = new Gson().toJson(auditMatch);

		String query = "INSERT INTO audit_records (record_type, audit_type, record_id, requested_by, data, created_date) "
				+ " values('" + ApplicationConstants.RECORDS_TYPE_DELETE_SCORE_CARD + "','"
				+ ApplicationConstants.AUDIT_TYPE_DELETE + "', '" + matchId + "','" + userId + "','" + matchJson + "',now())";

		getDaoInstance().insertAuditRecrords(matchId, clubId, userId, matchJson, query);
		getDaoInstance().backUpBallsData(matchId, clubId);

	}
	public static void saveMatchStatistics(MatchDto dto,int clubId) throws Exception{
		getDaoInstance().saveMatchStatistics(dto,clubId);
	}
	
	public static void saveMatchStatisticsFB(MatchDto dto,int clubId) throws Exception{
		getDaoInstance().saveMatchStatisticsFB(dto,clubId);
	}
	
	public static void lockMatch(long matchID, int clubId) throws Exception {
		getDaoInstance().updateLock(1, (int) matchID, clubId);
	}
	
	public static void updateFootBallMatchGoals(int t1Goals, int t2Goals, int matchId, int clubId) throws Exception {
		getDaoInstance().updateFootBallMatchGoals(t1Goals, t2Goals, matchId, clubId);
		
	}

	public static void unLockMatch(long matchID, int clubId) throws Exception {
		getDaoInstance().updateLock(0, (int) matchID, clubId);
	}
	
	public static void lockMatchByLeague(int leagueId, int clubId) throws Exception {
		getDaoInstance().updateLockByLeague(1, leagueId, clubId);
	}
	
	public static void unLockMatchByLeague(int leagueId, int clubId) throws Exception {
		getDaoInstance().updateLockByLeague(0,  leagueId, clubId);
	}

	public static void swapTeamsBasedOnToss(MatchDto dto) {
		if(dto.getBattingFirst() != dto.getTeamOne()){
			//swap teams
			int tempTeamOne = dto.getTeamOne();
			dto.setTeamOne(dto.getTeamTwo());
			dto.setTeamTwo(tempTeamOne);
			
			//swap teamNames
			String tempTeamOneName = dto.getTeamOneName();
			dto.setTeamOneName(dto.getTeamTwoName());
			dto.setTeamTwoName(tempTeamOneName);

			//swap captains
			int tempTeamOneCaptain = dto.getTeamOneCaptain();
			dto.setTeamOneCaptain(dto.getTeamTwoCaptain());
			dto.setTeamTwoCaptain(tempTeamOneCaptain);
			
			//swap vice captains.
			int tempTeamOneViceCaptain = dto.getTeamOneViceCaptain();
			dto.setTeamOneViceCaptain(dto.getTeamTwoViceCaptain());
			dto.setTeamTwoViceCaptain(tempTeamOneViceCaptain);
			
			//swap players
			List<Integer> players1 = dto.getPlayers1();
			dto.setPlayers1(dto.getPlayers2());
			dto.setPlayers2(players1);

		}
		
	}


	public static Map<String, String> getMatchesByScorer(int userID, int clubId) throws Exception {
		return getDaoInstance().getMatchesByScorer(userID,clubId);
	}
	
	public static List<MatchDto> getMatchesByUmpire(int userID, int clubId) throws Exception {
		return getDaoInstance().getMatchesByUmpire(0, 0, clubId, 0, 0,0,null,userID, "",0);
	}
	
	public static Map<Integer, Integer> getMatchScorerMap(int clubId) throws Exception {
		return getDaoInstance().getMatchScorerMap(clubId);
	}
	
	public static Map<Integer, String> getMatchScorerNameMap(int clubId) throws Exception {
		return getDaoInstance().getMatchScorerNameMap(clubId);
	}
	
	public static List<Integer> getPlayersOfMatches(List<Integer> matchIds, int clubId) throws Exception {
		return getDaoInstance().getPlayersOfMatches(matchIds,clubId);
	}


	public static void updateResOverDls(MatchDto matchDto, int clubId) throws Exception {
		 getDaoInstance().updateResOverDls(matchDto,clubId);
	}


	public static void updateMatchR1AvailableAndT2Target(MatchDto matchDto,
			int clubId) throws Exception {
		 getDaoInstance().updateMatchR1AvailableAndT2Target(matchDto,clubId);
		
	}


	public static void updateResOverTarget(MatchDto matchDto, int clubId) throws Exception {
		 getDaoInstance().updateResOverTarget(matchDto,clubId);
	}


	public static List<MatchDto> getPlayerMatches(int playerId) throws Exception {
		return getDaoInstance().getPlayerMatches(playerId);
	}

	public static List<MatchDto> getPlayerMatchesNew(int playerId, List<Integer> clubIds) throws Exception {
		return getDaoInstance().getPlayerMatchesNew(playerId, clubIds);
	}
	
	public static List<PlayerMatchDtoFB> getPlayerMatchesFB(int playerId, List<Integer> clubIds) throws Exception {
		return getDaoInstance().getPlayerMatchesFB(playerId, clubIds);
	}
	
	public static List<MatchDto> getMatchesForPlayerAsUmpire(int userId, List<Integer> clubIds) throws Exception {
		return getDaoInstance().getMatchesForPlayerAsUmpire(userId, clubIds);
	}
	
	public static List<MatchDto> getMatchesForPlayerAsScorer(int userId, List<Integer> clubIds) throws Exception {
		return getDaoInstance().getMatchesForPlayerAsScorer(userId, clubIds);
	}
	
	//Users with whom he/she played most number of matches (from the teams that he is part of or played against).
	public static List<Integer> getListOfPlayerIdsByMostNoOfMatches(int playerId, List<Integer> clubIds, int limit) throws Exception {
		return getDaoInstance().getListOfPlayerIdsByMostNoOfMatches(playerId, clubIds, limit);
	}

	public static void updateMatchLiveScoreURL(long matchId, int clubId, String liveSteamURL) throws Exception {
		 getDaoInstance().updateMatchLiveScoreURL(matchId,clubId, liveSteamURL, false);
	}
	
	public static void updateMatchLiveScoreURL(long matchId, int clubId, String liveSteamURL, boolean isMultipleStreams) throws Exception {
		 getDaoInstance().updateMatchLiveScoreURL(matchId,clubId, liveSteamURL, isMultipleStreams);
	}
	
	public static void deleteMatchLiveScoreLink(long matchId, int fixtureId, int clubId) throws Exception {
		 getDaoInstance().deleteMatchLiveScoreLink(matchId, fixtureId, clubId);
	}
	
	public static List<MatchDto> getPreviousEncounters(int clubId , int team1Id, int team2Id, String fixtureDate) throws Exception{
		return getDaoInstance().getPreviousEncounters(clubId, team1Id, team2Id, fixtureDate);
	}
	
	public static List<String> getRecentFormForTeam(int clubId , int teamId, String fixtureDate, int limt) throws Exception {
		return getDaoInstance().getRecentFormForTeam(clubId, teamId, fixtureDate, limt);
	}

	public static Map<Integer, Integer> getInningsInAMatch(int clubId, int matchId) throws Exception {
		return getDaoInstance().getInningsInAMatch(clubId, matchId);
	}

	public static Map<Integer, int[]> getBattingRunsBreakdownForGame(int clubId, int gameId) throws Exception {
		return getDaoInstance().getBattingRunsBreakdownForGame(clubId, gameId);
	}

	public static Map<String, List<Integer>> getRunsInGroupOfOvers(int clubId, int matchId) throws Exception {
		return getDaoInstance().getRunsInGroupOfOvers(clubId, matchId);
	}

	public static Map<String, int[]> getBowlingOutTypesForGame(int clubId, int matchId) throws Exception {
		return getDaoInstance().getBowlingOutTypesForGame(clubId, matchId);
	}

	public static List<List<BowlingOverComparisonDTO>> getBowlingOverByOverComparison(int clubId, int matchId) throws Exception {
		return getDaoInstance().getBowlingOverByOverComparison(clubId, matchId);
	}

	public static Map<Integer, double[]> getBattingBoundryPercentagePerBall(int clubId, int matchId) throws Exception {
		return getDaoInstance().getBattingBoundryPercentagePerBall(clubId, matchId);
	}
	
	public static void auditCreateMatch(int clubId, int fixtureId, int matchId, String matchInfo, int createdBy) throws Exception{
		getDaoInstance().auditCreateMatch(clubId, fixtureId, matchId, matchInfo, createdBy);
	}
	
	public static String getCreateMatchJson(int clubId, int fixtureId) throws Exception{
		return getDaoInstance().getCreateMatchJson(clubId, fixtureId);
	}
	
	public static Pair getLiveMatchesofTCSCC(boolean test) throws Exception{
		return getDaoInstance().getLiveMatchesofTCSCC(test);
	}
	
	public static void  insertOutPlayerForMatchFB(int clubId,int matchId,int playerId ,int teamId) throws Exception{
		getDaoInstance().insertOutPlayerForMatchFB(clubId,matchId, playerId, teamId);
	}
	
	public static void  updateSubstitutePlayerForMatchFB(int clubId,int matchId,int playerId) throws Exception{
		getDaoInstance().updateSubstitutePlayerForMatchFB(clubId,matchId, playerId);
	}
}
