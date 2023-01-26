package com.cricket.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;

import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.Pair;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.TeamDetailsDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.UserDto;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.utility.CommonUtility;
import com.google.common.collect.ListMultimap;

public class TeamFactory {

	private static TeamDAO teamDao = null;

	private static TeamDAO getDaoInstance() {
		if (teamDao == null) {
			teamDao = new TeamDAO();
		}
		return teamDao;
	}

	public static List<TeamDto> getPointsTable(String leagueName, int clubId) throws Exception {
		return getDaoInstance().getPointsTable("l", leagueName, clubId);
	}

	public static List<TeamDto> getPointsTableForSuperLeague(String leagueName, int clubId) throws Exception {
		return getDaoInstance().getPointsTable("sl", leagueName, clubId);
	}
	
	public static List<TeamDto> getPointsTablefor100BallLeague(String leagueName, int clubId,int ballsPerOver) throws Exception {
		return getDaoInstance().getPointsTablefor100BallLeague("l", leagueName, clubId,  ballsPerOver);
	}
	
	public static List<TeamDto> getPointsTableFB(String leagueName, int clubId) throws Exception {
		return getDaoInstance().getPointsTableFB("l", leagueName, clubId);
	}

	public static List<TeamDto> getPointsTablefor100BallSuperLeague(String leagueName, int clubId,int ballsPerOver) throws Exception {
		return getDaoInstance().getPointsTablefor100BallLeague("sl", leagueName, clubId,  ballsPerOver);
	}

	public static List<TeamDto> getTeams(TeamDto teamConstraint, int clubId) throws Exception {
		return getDaoInstance().getTeams(teamConstraint, clubId);
	}

	public static int insertTeam(TeamDto team, int clubId) throws Exception {
		return getDaoInstance().insertTeam(team, clubId);
	}

	public static void updateTeam(TeamDto team, int clubId) throws Exception {
		getDaoInstance().updateTeam(team, clubId);
	}
	public static void addPlayerToTeam(TeamDto team, int clubId) throws Exception {
		getDaoInstance().addPlayerToTeam(team, clubId);
	}
	
	public static int deletePlayerFromTeam(int teamId, List<Integer> playerIds, int clubId) throws Exception {
		return getDaoInstance().deletePlayerFromTeam(teamId, playerIds, clubId);
	}
	
	public static int deletePlayerFromTeam(int teamId, List<Integer> playerIds, int clubId, String deletedBy) throws Exception {
		return getDaoInstance().deletePlayerFromTeam(teamId, playerIds, clubId, deletedBy);
	}
	
	public static int deletePlayerFromTeamNew(int teamId, Integer playerId, int clubId, String deletedBy) throws Exception {
		return getDaoInstance().deletePlayerFromTeamNew(teamId, playerId, clubId, deletedBy);
	}

	public static int deleteTeam(int teamId, String userId, int clubId) throws Exception {
		int matches = getDaoInstance().getMatchCountForTeam(teamId, clubId);
		if (matches > 0) {
			return matches;
		} else {
			getDaoInstance().deleteTeam(teamId, userId, clubId);
			return 0;
		}

	}

	public static List<TeamDto> getTeams(String league, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setLeague(league);
		return getDaoInstance().getTeams(team, clubId);

	}
	public static List<TeamDto> getTeamsByInternalClub(String league, int clubId, List<Integer> internalClubIds) throws Exception {
		TeamDto team = new TeamDto();
		team.setLeague(league);
		return getDaoInstance().getTeamsByInternalClub(team, clubId, internalClubIds);

	}
	public static List<TeamDto> getTeamsByLeagueAndInternalClub(int league,int internalClubId, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setLeague(league+"");
		team.setClubId(internalClubId);
		return getDaoInstance().getTeams(team, clubId);

	}
	
	public static List<TeamDto> getTeamsByInternalClub(int internalClubId, int clubId) throws Exception {
        TeamDto team = new TeamDto();
        team.setClubId(internalClubId);
        return getDaoInstance().getTeams(team, clubId);

    }

	public static Map<Integer, Integer> getTeamCounts(int leagueId, int clubId) throws Exception {
		return getDaoInstance().getTeamCounts(leagueId, clubId);

	}

	public static TeamDto getTeamByPlayerId(int playerId, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setPlayerId(playerId);
		List<TeamDto> teams = getDaoInstance().getTeams(team, clubId);
		if (teams != null && !teams.isEmpty()) {
			return (TeamDto) teams.get(0);
		}
		return null;
	}
	
	public static TeamDto getTeamByPlayerIdOman(int playerId, int clubId) throws Exception {		
		List<TeamDto> teams = getDaoInstance().getTeamByPlayerIdOman(playerId, clubId);
		if (teams != null && !teams.isEmpty()) {
			return (TeamDto) teams.get(0);
		}
		return null;
	}

	public static List<Integer> getTeamIdsByPlayerId(int playerId, int clubId) throws Exception {
		List<Integer> teamIds = new ArrayList<Integer>();
		TeamDto team = new TeamDto();
		team.setPlayerId(playerId);
		List<TeamDto> teams = getDaoInstance().getTeams(team, clubId);
		if (teams != null && !teams.isEmpty()) {
			for(TeamDto team1:teams){
				teamIds.add(team1.getTeamID());
			}
		}
		return teamIds;
	}
	public static List<String> getPlayerTeamClubsList(int playerId, int clubId) throws Exception {		
		return getDaoInstance().getPlayerTeamInternalClubsList(playerId,clubId);
	}

	public static TeamDto getTeamByTeamId(int teamId, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setTeamID(teamId);
		List<TeamDto> teams = getDaoInstance().getTeams(team, clubId);
		if (teams != null && !teams.isEmpty()) {
			return (TeamDto) teams.get(0);
		}
		return null;
	}
	public static TeamDto getTeamByTeamIdForPreview(int teamId, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setTeamID(teamId);
		List<TeamDto> teams = getDaoInstance().getTeams(team, clubId, true);
		if (teams != null && !teams.isEmpty()) {
			return (TeamDto) teams.get(0);
		}
		return null;
	}
	public static TeamDto getTeamByTeamIdFromAll(int teamId, int clubId) throws Exception {
		TeamDto team = new TeamDto();
		team.setTeamID(teamId);
		List<TeamDto> teams = getDaoInstance().getFromAllTeams(team, clubId);
		if (teams != null && !teams.isEmpty()) {
			return (TeamDto) teams.get(0);
		}
		return null;
	}
	

	public static void addPlayerToTeam(int playerId, int teamId, int clubId, boolean isSecondary, String userId) throws Exception {
		
		if (!"sync".equalsIgnoreCase(userId)) {
			TeamDto team = TeamFactory.getTeamByTeamId(teamId, clubId);
			LeagueDto league = LeagueFactory.getLeague(CommonUtility.stringToInt(team.getLeague()), clubId);
			if (league.getTeamLimit() > 0) {
				Map<Integer, Integer> teamsCount = TeamFactory.getTeamCounts(league.getLeagueId(), clubId);
				int currentCount = 0;
				if (teamsCount.get(teamId) != null) {
					currentCount = teamsCount.get(teamId);
				}
				if (currentCount >= league.getTeamLimit()) {
					throw new CCException("Exceeding the team limit. Currrent Count:" + currentCount + " Team Limit:"+ league.getTeamLimit(), CCErrorConstant.EXCEEDING_TEAM_LIMIT);
				}
			}
		}
		
		getDaoInstance().insertPlayerToTeam(teamId, playerId, clubId, isSecondary ? "1" : "", userId);
	}
	
	public static void addPlayerToTeamNew(int playerId, int teamId, int clubId, String userId) throws Exception {
		
			TeamDto team = TeamFactory.getTeamByTeamId(teamId, clubId);
			LeagueDto league = LeagueFactory.getLeague(CommonUtility.stringToInt(team.getLeague()), clubId);
			if (league != null && league.getTeamLimit() > 0) {
				Map<Integer, Integer> teamsCount = TeamFactory.getTeamCounts(league.getLeagueId(), clubId);
				int currentCount = 0;
				if (teamsCount.get(teamId) != null) {
					currentCount = teamsCount.get(teamId);
				}
				if (currentCount >= league.getTeamLimit()) {
					throw new CCException("Exceeding the team limit. Currrent Count:" + currentCount + " Team Limit:"+ league.getTeamLimit(), CCErrorConstant.EXCEEDING_TEAM_LIMIT);
				}
			}
		getDaoInstance().insertPlayerToTeamNew(teamId, playerId, clubId, userId);
	}
	
	public static void addPlayerToOmanTeam(int playerId, int teamId, int clubId, boolean isSecondary, String userId) throws Exception {
		getDaoInstance().insertPlayerToTeam(teamId, playerId, clubId, isSecondary ? "1" : "", userId);
	}

	public static void addCaptainToTeam(int playerId, int teamId, int clubId) throws Exception {
		getDaoInstance().addCaptainToTeam(playerId, teamId, clubId);
	}

	public static void addViceCaptainToTeam(int playerId, int teamId, int clubId) throws Exception {
		getDaoInstance().addViceCaptainToTeam(playerId, teamId, clubId);
	}
	
	public static void addWicketKeeperToTeam(int playerId, int teamId, int clubId) throws Exception {
		getDaoInstance().addWicketKeeperToTeam(playerId, teamId, clubId);
	}

	public static void lockTeam(int teamId, int clubId) throws Exception {
		getDaoInstance().updateLock(1, teamId,0, clubId);
	}

	public static void unLockTeam(int teamId, int clubId) throws Exception {
		getDaoInstance().updateLock(0, teamId,0, clubId);
	}

	public static List<TeamDto> getAllTeams(int clubId) throws Exception {
		return getDaoInstance().getTeams(new TeamDto(), clubId);
	}
	

	public static List<TeamDto> getAllTeamsPendingApproval(int clubId) throws Exception {
		return getDaoInstance().getAllTeamsPendingApproval(clubId);
	}
	
	public static TeamDto getTeamPendingApprovalById(int clubId, int teamId) throws Exception {
		List<TeamDto> teams = getDaoInstance().getAllTeamsPendingApproval(clubId);
		if(teams != null && !teams.isEmpty()){
			for(TeamDto team : teams) {
				if(team.getTeamID() == teamId) {
					return team;
				}
			}
		}
		return null;
	}
	
	public static Map<Integer,String> getGroupNames(int leagueId, int clubId) throws Exception {
		return getDaoInstance().getGroupNames(leagueId+"", clubId);
	}
	public static Map<Integer,String> getGroupNames(String leagueId, int clubId) throws Exception {
		return getDaoInstance().getGroupNames(leagueId, clubId);
	}
	public static void addGroupName(int leagueId,  int groupId, String groupName, int clubId) throws Exception {
		getDaoInstance().addGroupName(leagueId, groupId, groupName, clubId);
	}
	public static void deleteGroupName(int leagueId,  int groupId, int clubId) throws Exception {
		getDaoInstance().deleteGroupName(leagueId, groupId, clubId);
	}

	public static void lockAllTeams(int league, int clubId) throws Exception {
		getDaoInstance().updateLock(1, 0,league, clubId);
		
	}

	public static void unlockAllTeams(int league, int clubId) throws Exception {
		getDaoInstance().updateLock(0,0, league, clubId);
		
	}
	
	public static void updateTeamLogoFilePath(String logo_file_path,int teamId, int clubId) throws Exception {
		getDaoInstance().updateTeamLogoFilePath(logo_file_path,teamId, clubId);
		
	}
	
	public static void updateTeamBackGroundImagePath(String backGroundImagePath, int teamId, int clubId, int userId) throws Exception {
		getDaoInstance().updateTeamBackGroundImagePath(backGroundImagePath, teamId, clubId, userId);
		
	}
	
	public static List<Integer> getDistinctTeams(int clubId) throws Exception{
		return getDaoInstance().getDistinctTeams(clubId);
	}

	public static List<TeamDto> getTeamsByQuery(String qStr, String league, int clubId) throws Exception {
		return getDaoInstance().getTeamsByQuery(qStr, league, clubId);
	}

	public static int updateTeamName(TeamDto teamDto, int clubId) throws Exception {
		return getDaoInstance().updateTeamName(teamDto, clubId);
	}

	public static int updateInternalClubId(int teamId, int internalClubId, int clubId) throws Exception {
		return getDaoInstance().updateInternalClubId(teamId, internalClubId, clubId);
	}
	public static int approveTeam(int teamId, String userId, int clubId) throws Exception {
		return getDaoInstance().approveTeam(teamId, userId, clubId);
	}

	public static void addUserToTeamOfficial(int clubIdParam, int teamId, int userId, String teamOfficialRole) throws Exception {
		getDaoInstance().addUserToTeamOfficial(clubIdParam, teamId, userId, teamOfficialRole);
	}
	
	public static void insertTeamOfficilaByCopyTeamId(int clubId, int copyTeamId, int newTeamId) throws Exception {
		getDaoInstance().insertTeamOfficilaByCopyTeamId(clubId, copyTeamId, newTeamId);
	}

	public static List<UserDto> getTeamOfficialByTeamId(int teamId, int clubId) throws Exception {
		return getDaoInstance().getTeamOfficialByTeamId(teamId, clubId);
	}

	public static void removeUserToTeamOfficial(int clubIdParam, int teamId, int userId) throws Exception {
		getDaoInstance().removeUserToTeamOfficial(clubIdParam, teamId, userId);
	}
	
	public static int deletePlayerFromAllTeam(int playerId, int clubId, String userId) throws Exception {
		return getDaoInstance().deletePlayerFromAllTeam(playerId, clubId, userId);
	}

	public static List<String> getTeamOfficialsEmailByTeamIds(List<String> teamList, int clubId) throws Exception {
		return getDaoInstance().getTeamOfficialsEmailByTeamIds(teamList, clubId);
	}

	public static TeamDto getTeamByCustomteamIdAndLeagueId(int groupId, int leagueId, int clubId) throws Exception {
		return getDaoInstance().getTeamByCustomteamIdAndLeagueId(groupId, leagueId, clubId);
	}
	
	public static List<Integer> getTeamPlayer(int teamId, int clubId) throws Exception {
		return getDaoInstance().getTeamPlayer(teamId, clubId);
	}
	
	public static List<PlayerDto> getTeamPlayersList(int teamId, int clubId) throws Exception {
		return getDaoInstance().getTeamPlayersList(teamId, clubId);
	}

	public static List<TeamDto> getTeamsByName(String teamName, int clubId) throws Exception {
		return getDaoInstance().getTeamsByName(teamName, clubId);
	}
	
	public static List<TeamDto> getTeamsByNameSearch(String teamName, int clubId) throws Exception {
		return getDaoInstance().getTeamsByNameSearch(teamName, clubId);
	}
	
	public static List<TeamDto> getPlayerAllTeams(int playerId, List<Integer> clubIds, int limit) throws Exception {
		return getDaoInstance().getPlayerAllTeams(playerId, clubIds, limit);
	}
	
	public static List<TeamDto> getPlayerAllCurrentTeams(int playerId, List<Integer> clubIds, int limit) throws Exception {
		return getDaoInstance().getPlayerAllCurrentTeams(playerId, clubIds, limit);
	}

	public static Map<Integer, Integer> getPlayerTeamIdMap(int clubId) throws Exception {
		return getDaoInstance().getPlayerTeamIdMap(clubId);
	}
	
	public static Map<Integer, Integer> getTeamInternalClubIdMap(int clubId) throws Exception {
		return getDaoInstance().getTeamInternalClubIdMap(clubId);
	}
	
	public static List<Integer> getTeamUserIds(int teamId, int clubId) throws Exception {
		return getDaoInstance().getTeamUserIds(teamId, clubId);
	}

	public static List<Integer> getTeamOfficialTeamIds(int userId, int clubId) throws Exception {
		return getDaoInstance().getTeamOfficialTeamIds(userId, clubId);
	}

	public static TeamDetailsDto getTeamDetailsByTeamName(String teamName, int clubId) throws Exception {
		return getDaoInstance().getTeamDetailsByTeamName(teamName, clubId);
	}

	public static void saveTeamDetails(TeamDetailsDto teamDetailsDto, int clubId) throws Exception {
		getDaoInstance().saveTeamDetails(teamDetailsDto, clubId);
	}

	public static TeamDetailsDto getTeamDetailsByTeamId(String teamId, int clubId) throws Exception {
		return getDaoInstance().getTeamDetailsByTeamId(teamId, clubId);
	}

	public static void updateTeamName(int clubId, String apiTeamName, List<Integer> teamIdList) throws Exception {
		getDaoInstance().getTeamDetailsByTeamId(clubId, apiTeamName, teamIdList);
	}

	public static void removeUnSyncPlayersFromTeam(List<Integer> dbPlayerIds, int teamId) throws Exception {
		getDaoInstance().removeUnSyncPlayersFromTeam(dbPlayerIds, teamId);
	}

	public static int getTeamIdFromTeamDetails(int teamId,int clubId) throws Exception{
		return getDaoInstance().getTeamIdFromTeamDetails(teamId,clubId);
	}

	public static void updateteamDetailsByCaptainName(int teamId, int clubId, String captainName) throws Exception{
		getDaoInstance().updateteamDetailsByCaptainName(teamId, clubId,captainName);
		
	}
	public static Map<Integer, String> getCoaches(int clubId) throws Exception {
		return getDaoInstance().getCoaches(clubId);

	}

}
