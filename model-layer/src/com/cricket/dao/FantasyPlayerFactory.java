package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.FantasyPlayerDto;
import com.cricket.utility.CommonUtility;

public class FantasyPlayerFactory {

	private static FantasyPlayerDAO fantasyPlayerDAO = null;

	private static FantasyPlayerDAO getDaoInstance() {
		if (fantasyPlayerDAO == null) {
			fantasyPlayerDAO = new FantasyPlayerDAO();
		}
		return fantasyPlayerDAO;
	}

	public static List<FantasyPlayerDto> getPlayersOfTeam(int teamId) throws Exception {
		return getDaoInstance().getPlayersOfTeam(teamId);		
	}
	
	public static List<FantasyPlayerDto> getPlayersOfTeam1AndTeam2(int team1Id, int team2Id) throws Exception {
		return getDaoInstance().getPlayersOfTeam1AndTeam2(team1Id, team2Id);		
	}
	
	public static List<FantasyPlayerDto> getPlayersOfTournamnet(int tournamentId) throws Exception {
		return getDaoInstance().getPlayersOfTournamnet(tournamentId);		
	}
	
	public static Map<Integer,String> getPlayerImagePathMap(int fantasyMatchId, String imagePrefix) throws Exception {
		return getDaoInstance().getPlayerImagePathMap(fantasyMatchId,imagePrefix);		
	}
	
	public static Map<Integer,Integer> getPlayerIdMap(int fantasyMatchId) throws Exception {
		return getDaoInstance().getPlayerIdMap(fantasyMatchId);		
	}
	
	public static Map<Integer,String> getPlayerCategoryMapForMatch(int fantasyMatchId) throws Exception {
		return getDaoInstance().getPlayerCategoryMapForMatch(fantasyMatchId);		
	}
	
	public static List<FantasyPlayerDto> getFantasyPlayers(int ccClubId, int seriesTypeId, 
			int tournamentId, int teamId) throws Exception {
		return getDaoInstance().getFantasyPlayers(ccClubId,seriesTypeId,tournamentId,teamId,0);		
	}
	
	public static FantasyPlayerDto getFantasyPlayerById(int playerId) throws Exception {
		List<FantasyPlayerDto>  playersList = getDaoInstance().getFantasyPlayers(0,0,0,0,playerId);
		if(!CommonUtility.isListNullEmpty(playersList)) {
			return playersList.get(0);
		}
		return null;
	}
	
	public static int checkFantasyPlayerExistsByCCPlayerId(int ccPlayerId) throws Exception {
		return getDaoInstance().checkFantasyPlayerExistsByCCPlayerId(ccPlayerId);
	}
	
	public static void insertFantasyPlayersCopyMatches(int clubId, int teamOne, int teamTwo) throws Exception{
		getDaoInstance().insertFantasyPlayersCopyMatches(clubId, teamOne, teamTwo);
	}
	
	public static int copyPlayersToFantasy(List<FantasyPlayerDto> playersList) throws Exception{
		return getDaoInstance().copyPlayersToFantasy(playersList);
	}
	
	public static List<Integer> getExistingFantasyCCPlayerIds(String ccPlayerIdsStr) throws Exception{
		return getDaoInstance().getExistingFantasyCCPlayerIds(ccPlayerIdsStr);
	}
	
	public static Map<Integer, Integer> getFantasyPlayerIdsMap(String ccPlayerIdsStr) throws Exception{
		return getDaoInstance().getFantasyPlayerIdsMap(ccPlayerIdsStr);
	}

	public static void updateFantasyPlayerCredits(int playerId, int teamId, float playerCredits) throws Exception {
		getDaoInstance().updateFantasyPlayerCredits(playerId, teamId, playerCredits);
		
	}
	
	public static int updateFantasyPlayerCredits(String[] playerIds, String[] teamIds, String[] playerCredits) throws Exception {
		return getDaoInstance().updateFantasyPlayerCredits(playerIds, teamIds, playerCredits);
		
	}

	public static int updateFantasyPlayerInfo(FantasyPlayerDto fpdto) throws Exception {
		return getDaoInstance().updateFantasyPlayerInfo(fpdto);
	}
	public static int updateFantasyPlayerProfileImage(int id, String imagePath) throws Exception {
		return getDaoInstance().updateFantasyPlayerProfileImage(id, imagePath);
	}

	public static int insertFantasyPlayer(FantasyPlayerDto fpdto) throws Exception {
		return getDaoInstance().insertFantasyPlayer(fpdto);
	}

	public static int addPlayerToFantasyTeam(int ccClubId, int ccTeamId, int ccPlayerId, int fantasyTeamId,
			int fantasyplayerId, float playerCredits) throws Exception {
		return getDaoInstance().addPlayerToFantasyTeam(ccClubId, ccTeamId, ccPlayerId, fantasyTeamId, fantasyplayerId, playerCredits);
	}

	public static int bulkUpdateFantasyPlayerInfo(List<FantasyPlayerDto> players) throws Exception {
		return getDaoInstance().bulkUpdateFantasyPlayerInfo(players);
	}
	
}
