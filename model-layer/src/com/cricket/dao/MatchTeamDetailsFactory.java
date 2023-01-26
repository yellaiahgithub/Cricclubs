package com.cricket.dao;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.cricket.dto.MatchTeamDetailsDto;

public class MatchTeamDetailsFactory {

	private static MatchTeamDetailsDAO matchTeamDetailsDAO = null;

	private static MatchTeamDetailsDAO getDaoInstance() {
		if (matchTeamDetailsDAO == null) {
			matchTeamDetailsDAO = new MatchTeamDetailsDAO();
		}
		return matchTeamDetailsDAO;
	}

	public static List<MatchTeamDetailsDto> getMatchTeamDetails(long matchTeamDetailId) throws Exception {
		return getDaoInstance().getMatchTeamDetails(matchTeamDetailId);		
	}
	
	public static void deleteAndInsertMatchTeamDetails(List<MatchTeamDetailsDto> mtDetails, long matchTeamId) throws Exception {
		getDaoInstance().insertMatchTeamDetails(mtDetails,matchTeamId);		
	}
	
	public static void insertMatchTeamDetails(List<MatchTeamDetailsDto> mtDetails) throws Exception {
		getDaoInstance().insertMatchTeamDetails(mtDetails,0);		
	}
	
	public static int deleteUserTeam(long userTeamId) throws Exception {
		return getDaoInstance().deleteUserTeam(userTeamId);		
	}
	
	public static SortedMap<String,Integer> getMatchTeamPlayerRoleMap(int matchId) throws Exception {
		return getDaoInstance().getMatchTeamPlayerRoleMap(matchId);		
	}
	
	public static List<Long> getUserTeamPlayersById(long matchTeamId) throws Exception {
		return getDaoInstance().getUserTeamPlayersById(matchTeamId);		
	}
	
	public static Map<String,Integer> getTournamentMatchTeamPlayerRoleMap(int tournamentId) throws Exception {
		return getDaoInstance().getTournamentMatchTeamPlayerRoleMap(tournamentId);		
	}
	
	public static Map<Long,List<Long>> getAllUserTeamIdPlayerListMap(int fantasyMatchId) throws Exception {
		return getDaoInstance().getAllUserTeamIdPlayerListMap(fantasyMatchId);		
	}
	
	public static Map<Long,Integer> getMatchTeamIdPointsMap(int fantasyMatchId) throws Exception {
		return getDaoInstance().getMatchTeamIdPointsMap(fantasyMatchId);		
	}
	
}
