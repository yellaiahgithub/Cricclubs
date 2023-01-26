package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.ClubDto;
import com.cricket.dto.FantasyTeamsDto;
import com.cricket.dto.TeamDto;
import com.cricket.utility.CommonUtility;

public class FantasyTeamsFactory {
	
	private static FantasyTeamsDAO fantasyTeamsDAO = null;

	private static FantasyTeamsDAO getDaoInstance() {
		if (fantasyTeamsDAO == null) {
			fantasyTeamsDAO = new FantasyTeamsDAO();
		}
		return fantasyTeamsDAO;
	}

	public static List<FantasyTeamsDto> getFantasyTeams(FantasyTeamsDto teamDto) throws Exception {
		
		List<FantasyTeamsDto> ftDtos = getDaoInstance().getFantasyTeams(teamDto);
		if(!CommonUtility.isListNullEmpty(ftDtos)) {
			for(FantasyTeamsDto ftDto : ftDtos) {
				int clubId = ftDto.getCcClubId();
				ClubDto club = ClubFactory.getClub(clubId);
				ftDto.setClubName(club.getName());
			}
		}
		return ftDtos;
	}
	
	public static FantasyTeamsDto getFantasyTeamById(int teamId) throws Exception {
		
		FantasyTeamsDto teamDto = new FantasyTeamsDto();
		if(teamId>0) {
			teamDto.setId(teamId);
		}
		List<FantasyTeamsDto> teamsList = getDaoInstance().getFantasyTeams(teamDto);
		if(!CommonUtility.isListNullEmpty(teamsList)) {
			FantasyTeamsDto ftDto = teamsList.get(0);
			int clubId = ftDto.getCcClubId();
			ClubDto club = ClubFactory.getClub(clubId);
			ftDto.setClubName(club.getName());
			return ftDto;
		}
		return null;
	}
	
	public static FantasyTeamsDto getFantasyTeamByCCTeamId(int ccTeamId, int ccClubId) throws Exception {
		
		FantasyTeamsDto teamDto = new FantasyTeamsDto();
		if(ccTeamId>0) {
			teamDto.setCcTeamId(ccTeamId);
		}
		if(ccClubId>0) {
			teamDto.setCcClubId(ccClubId);
		}
		List<FantasyTeamsDto> teamsList = getDaoInstance().getFantasyTeams(teamDto);
		
		if(!CommonUtility.isListNullEmpty(teamsList)) {
			FantasyTeamsDto ftDto = teamsList.get(0);
			int clubId = ftDto.getCcClubId();
			ClubDto club = ClubFactory.getClub(clubId);
			ftDto.setClubName(club.getName());
			return ftDto;
		}
		return null;
	}
	
	public static void updateFantasyTeam(FantasyTeamsDto teamDto) throws Exception {		
		getDaoInstance().updateFantasyTeam(teamDto);			
	}

	public static void updateTeamLogoFilePath(String logoFilePath, int teamId) throws Exception {
		getDaoInstance().updateTeamLogoFilePath(logoFilePath, teamId);
		
	}
	
	public static Map<Integer,String> getTeamNameMap(int tournamentId) throws Exception {
		return getDaoInstance().getTeamNameMap(tournamentId);
	}
	
	public static Map<Integer,String> getTeamLogoPathMap(int ccTeam1Id, int ccTeam2Id,String imagePrefix) throws Exception {
		return getDaoInstance().getTeamLogoPathMap(ccTeam1Id, ccTeam2Id,imagePrefix);
	}
	
	public static int insertFantasyTeam(int clubId, int tournamentId, TeamDto teamDto) throws Exception{
		return getDaoInstance().insertFantasyTeam(clubId,tournamentId,teamDto);
	}
	
}
