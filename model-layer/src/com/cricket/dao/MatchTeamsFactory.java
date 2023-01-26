package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.MatchTeamsDto;
import com.cricket.utility.CommonUtility;

public class MatchTeamsFactory {

	private static MatchTeamsDAO matchTeamsDAO = null;

	private static MatchTeamsDAO getDaoInstance() {
		if (matchTeamsDAO == null) {
			matchTeamsDAO = new MatchTeamsDAO();
		}
		return matchTeamsDAO;
	}
	
	public static MatchTeamsDto getMatchTeamDtoById(long matchTeamId) throws Exception {
		
		List<MatchTeamsDto> matchTeamsList = getDaoInstance().getMatchTeamDtos(matchTeamId,0,0);
		if(!CommonUtility.isListNullEmpty(matchTeamsList)) {
			return matchTeamsList.get(0);
		}
		return null;		
	}
	
	public static List<MatchTeamsDto> getMatchTeamDtosByUserAndMatchId(long userId, int matchId) throws Exception {
		return getDaoInstance().getMatchTeamDtos(0,userId,matchId);		
	}

	public static MatchTeamsDto getUserMatchTeamById(long matchTeamId) throws Exception {
		return getDaoInstance().getUserMatchTeamById(matchTeamId);		
	}
	
	public static long insertMatchTeam(MatchTeamsDto mtDto) throws Exception {
		return getDaoInstance().insertMatchTeam(mtDto);		
	}
	
	public static void updateMatchTeam(long matchTeamId, String teamName) throws Exception {
		getDaoInstance().updateMatchTeam(matchTeamId,teamName);		
	}
	
	public static void updatMatchTeamNoOfTransfers(long matchTeamId, int noOfTransfers) throws Exception {
		getDaoInstance().updatMatchTeamNoOfTransfers(matchTeamId,noOfTransfers);		
	}
	
	public static List<MatchTeamsDto> getUserTeams(long matchId, long userId, long userTeamid, int mathStatus, int seriesId) throws Exception {
		return getDaoInstance().getUserTeams(matchId,userId,userTeamid,mathStatus,seriesId);		
	}
	
	public static List<MatchTeamsDto> getUserTeamsOfContest(long matchId, long userId, long userTeamid, long contestId, int matchStatus, int seriesId) throws Exception {
		return getDaoInstance().getUserTeamsOfContest(matchId,userId,userTeamid,contestId, matchStatus,seriesId);		
	}
	
	public static List<String> getUserTeamNameOfTheMatch(long matchId, long userId, long matchTeamId) throws Exception {
		return getDaoInstance().getUserTeamNameOfTheMatch(matchId,userId,matchTeamId);		
	}
	
	public static int getNoOfUserTeamsForMatch(long matchId, long userId) throws Exception {
		return getDaoInstance().getNoOfUserTeamsForMatch(matchId,userId);		
	}
	
	public static int getNoOfTeamsForMatch(long matchId) throws Exception {
		return getDaoInstance().getNoOfUserTeamsForMatch(matchId,0);		
	}
		
	public static List<Integer> getPlayerIdsOfTeam(long userTeamId) throws Exception {
		return getDaoInstance().getPlayerIdsOfTeam(userTeamId);		
	}
	
	public static Map<Integer,String> getPlayerIdRoleMapOfTeam(long userTeamId) throws Exception {
		return getDaoInstance().getPlayerIdRoleMapOfTeam(userTeamId);		
	}
	
	public static void deleteMatchTeam(long matchTeamId) throws Exception {
		getDaoInstance().deleteMatchTeam(matchTeamId);		
	}
	
	public static Map<Long,String> getAllUserTeamIdNameMap(long fantasyMatchId) throws Exception {
		return getDaoInstance().getAllUserTeamIdNameMap(fantasyMatchId);		
	}
	
	public static Map<Long, Integer> getFantasyPlayerIdPointsMap(int fantasyMatchId) throws Exception {
		return getDaoInstance().getFantasyPlayerIdPointsMap(fantasyMatchId);		
	}
	
}
