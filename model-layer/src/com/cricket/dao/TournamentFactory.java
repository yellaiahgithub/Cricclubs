package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.TournamentDto;
import com.cricket.utility.CommonUtility;

public class TournamentFactory {

	private static TournamentDAO tournamentDAO = null;

	private static TournamentDAO getDaoInstance() {
		if (tournamentDAO == null) {
			tournamentDAO = new TournamentDAO();
		}
		return tournamentDAO;
	}
	
	public static int insertTournament(TournamentDto tDto) throws Exception {
		return getDaoInstance().insertTournament(tDto);		
	}
	

	public static List<TournamentDto> getTournaments(int sportId, List<String> seriesNames, int status) throws Exception {
		return getDaoInstance().getTournaments(sportId,seriesNames,status,0,0,0);		
	}
	
	public static List<TournamentDto> getTournamentsForAdmin(int seriesTypeId, int tournamentId, int clubId) 
			throws Exception {
		return getDaoInstance().getTournamentsForAdmin(seriesTypeId, tournamentId, clubId);		
	}
	
	public static TournamentDto getLatestTournament(int sportId) throws Exception {
		return getDaoInstance().getLatestTournament(sportId);		
	}
	
	public static TournamentDto getTournamentById(int tournamentId) throws Exception {
		
		List<TournamentDto> tournaments =  getDaoInstance().getTournaments(0,null,0,tournamentId,0,0);
		 
		if(!CommonUtility.isListNullEmpty(tournaments)) {
			return tournaments.get(0);
		}
		return null;
	}
	public static TournamentDto getTournamentByCCSeriesId(int ccClubId, int ccSeriesId) throws Exception {
		
		List<TournamentDto> tournaments =  getDaoInstance().getTournaments(0,null,0,0,ccClubId,ccSeriesId);
		 
		if(!CommonUtility.isListNullEmpty(tournaments)) {
			return tournaments.get(0);
		}
		return null;
	}
	
	public static Map<Integer,String> getTournamentNameMap(int sportId, int sportCategoryId, int ccClubId) throws Exception {
		return getDaoInstance().getTournamentNameMap(sportId,sportCategoryId,ccClubId);
	}
	
	public static void updateFantasyTournamentLogoUrl(int tournamentId, String s3ImagePath) throws Exception {
		getDaoInstance().updateFantasyTournamentLogoUrl(tournamentId, s3ImagePath);
	}

	public static void updateTournamentDetails(int tournamentId, String tournamentName) throws Exception {
		getDaoInstance().updateTournamentDetails(tournamentId, tournamentName);
	}
	
	public static void eanbleTournamentStartTransfersCount(int tournamentId) throws Exception {
		getDaoInstance().eanbleTournamentStartTransfersCount(tournamentId);
	}
	
}
