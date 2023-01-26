package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.MatchTeamDetailsDto;
import com.cricket.dto.MatchTeamDetailsSeriesDto;

public class MatchTeamDetailsSeriesFactory {

	private static MatchTeamDetailsSeriesDAO matchTeamDetailsSeriesDAO = null;

	private static MatchTeamDetailsSeriesDAO getDaoInstance() {
		if (matchTeamDetailsSeriesDAO == null) {
			matchTeamDetailsSeriesDAO = new MatchTeamDetailsSeriesDAO();
		}
		return matchTeamDetailsSeriesDAO;
	}

	public static List<MatchTeamDetailsSeriesDto> getMatchTeamDetailsSeries(long matchTeamDetailId) throws Exception {
		return getDaoInstance().getMatchTeamDetailsSeries(matchTeamDetailId);		
	}
	
	public static void insertMatchTeamDetailsSeries(List<MatchTeamDetailsSeriesDto> mtDetails) throws Exception {
		getDaoInstance().insertMatchTeamDetailsSeries(mtDetails);		
	}
	
	public static void addSeriesContestUserTeamDetails(int matchId, int dreamTeamsMatchId, int dynamicTeamsMatchId) throws Exception {
		getDaoInstance().addSeriesContestUserTeamDetails(matchId, dreamTeamsMatchId, dynamicTeamsMatchId);		
	}
	
	public static void deleteUserTeam(long userTeamId) throws Exception {
		getDaoInstance().deleteUserTeam(userTeamId);		
	}
	
	public static Map<String,Integer> getMatchTeamPlayerRoleMap(int matchId) throws Exception {
		return getDaoInstance().getMatchTeamPlayerRoleMap(matchId);		
	}
	
	public static int verifyFantasyMatchExistsOrNot(int matchId) throws Exception {
		return getDaoInstance().verifyFantasyMatchExistsOrNot(matchId);		
	}
	
	public static Map<String,Integer> getTournamentMatchTeamPlayerRoleMap(int tournamentId) throws Exception {
		return getDaoInstance().getTournamentMatchTeamPlayerRoleMap(tournamentId);		
	}
	
}
