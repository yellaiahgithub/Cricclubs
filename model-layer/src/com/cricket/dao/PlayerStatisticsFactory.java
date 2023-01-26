package com.cricket.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.PlayerAllIncidentStatsFBDto;
import com.cricket.dto.PlayerBatBowlStatsDto;
import com.cricket.dto.PlayerBattingDto;
import com.cricket.dto.PlayerBowlingDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerFieldingDto;
import com.cricket.dto.PlayerMatchBattingDto;
import com.cricket.dto.PlayerMatchBowlingDto;
import com.cricket.dto.PlayerMatchFieldingDto;
import com.cricket.dto.PlayerStatisticSummaryFBDto;
import com.cricket.dto.PlayerStatisticsFBDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.statistics.PlayerRankingStatisticDto;
import com.cricket.dto.statistics.PlayerStatisticSummaryDto;
import com.cricket.utility.CommonUtility;
import com.football.dto.PlayerMatchRecordsDtoFB;

public class PlayerStatisticsFactory {
	
	private static PlayerStatisticsDAO statisticsDao = null;
	
	private static PlayerStatisticsDAO getDaoInstance(){
		if(statisticsDao == null){
			statisticsDao = new PlayerStatisticsDAO();
		}
		return statisticsDao;
	}

	
	public static List<BattingDto> getPlayersBattingByMatchIdTeamId(long matchId, long teamId,int clubId) throws Exception{
		return getDaoInstance().getPlayersBattingByMatchIdTeamId(matchId,teamId,clubId);
	}
	
	public static void updateManOfTheMatch(int matchId, int player_id,int clubId) throws Exception{
		getDaoInstance().updateManOfTheMatch(matchId,player_id,clubId);
	}
	public static List<PlayerBatBowlStatsDto> getInternalClubPlayersBatBowlStats(int clubId, int internalClubId) throws Exception{
		return getDaoInstance().getInternalClubPlayersBatBowlStats(clubId, internalClubId);
	}
	public static List<PlayerStatisticSummaryDto> getAllPlayersMatchStatistics(int matchId, int clubId) throws Exception{
		return getDaoInstance().getAllPlayerMatchStatistics(matchId,clubId);
	}
	public static List<PlayerStatisticSummaryDto> getPlayerStatsForMOMForTestMatch(int matchId, int clubId) throws Exception{
		return getDaoInstance().getPlayerStatsForMOMForTestMatch(matchId,clubId);
	}
	public static List<PlayerBattingDto> getAllPlayersBattingStatistics(int teamId, long playerId, String matchType,String league,int limit,int clubId, String seriesType, String internalClubId, String year) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatistics(teamId, playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, year, null,0);
	}
	public static List<PlayerStatisticsFBDto> getAllPlayersLastWeekStatisticsFB(String matchType,String league,int clubId) throws Exception{
		
		return getDaoInstance().getPlayersStatisticsFB(clubId,0,0,0,league,matchType,7,null,null,null,0,0);
	}
	
	public static List<PlayerStatisticsFBDto> getPlayersStatisticsFB(int clubId, long playerId, int matchId, int teamId, String seriesIdStr, 
			String matchType, int days, String year, String fixtureDate, String groupBy, int limit, int skip) throws Exception{
		return getDaoInstance().getPlayersStatisticsFB(clubId, playerId, matchId, teamId, seriesIdStr, matchType, days, year, fixtureDate, groupBy, limit, skip);
	}
	
	public static List<PlayerAllIncidentStatsFBDto> getPlayerAllIncidentStatsFB(int playerId, List<Integer> playerClubIds) throws Exception{
		return getDaoInstance().getPlayerAllIncidentStatsFB(playerId, playerClubIds);
	}
	
	public static List<PlayerBattingDto> getAllPlayersBattingStatistics(int teamId, long playerId, String matchType,String league,int limit,int clubId, String seriesType, String internalClubId, int skip) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatistics(teamId, playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, null, null, skip);
	}
	
	public static List<PlayerBattingDto> getAllPlayersBattingStatisticsBeforeFixtureDate(int teamId, long playerId, String matchType,String league,int limit,int clubId, String seriesType, String internalClubId, String fixtureDate) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatistics(teamId, playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, null, fixtureDate,0);
	}
	
	public static List<PlayerBattingDto> getAllPlayersLastWeekBattingStatistics(String matchType,String league,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatistics(0, 0,matchType,league,0,clubId,7, null, "", null,null,0);
	}

	public static List<PlayerBattingDto> getAllPlayersLast30BattingStatistics(int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatistics(0, 0,null,"",0,clubId,60, null, "", null, null,0);
	}

	public static List<PlayerBattingDto> getAllPlayersBattingStatisticsLite(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBattingStatisticsLite(playerId,matchType,league,limit,clubId);
	}

	public static List<PlayerDto> getAllPlayersMOMStatistics(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersMOMStatistics(playerId,matchType,league,limit,clubId,0);
	}

	public static List<PlayerDto> getAllPlayersLastWeekMOMStatistics(String matchType,String league,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersMOMStatistics(0,matchType,league,0,clubId,7);
	}

	public static List<PlayerMatchBattingDto> getPlayerMatchBattingStatistics(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		return getDaoInstance().getPlayerMatchBattingStatistics(playerId,matchType,league,limit,clubId);
	}

	public static List<PlayerBowlingDto> getAllPlayersBowlingStatistics(int teamId,long playerId, String matchType,String league,int limit,int clubId, String seriesType, String internalClubId) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatistics(teamId,playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, null, null,0);
	}
	
	public static List<PlayerBowlingDto> getAllPlayersBowlingStatisticsBeforeFixtureDate(int teamId,long playerId, String matchType,String league,
			int limit,int clubId, String seriesType, String internalClubId, String fixtureDate) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatistics(teamId,playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, null, fixtureDate,0);
	}

	public static List<PlayerBowlingDto> getAllPlayersBowlingStatistics(int teamId,long playerId, String matchType,String league,int limit,
			int clubId, String seriesType, String internalClubId, String year, int skip) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatistics(teamId,playerId,matchType,league,limit,clubId,0, seriesType, internalClubId, year, null, skip);
	}
	
	public static List<PlayerBowlingDto> getAllPlayersLastWeekBowlingStatistics(String matchType,String league,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatistics(0,0,matchType,league,0,clubId,7,null,"", null, null,0);
	}

	public static List<PlayerBowlingDto> getAllPlayersLast30BowlingStatistics(int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatistics(0,0,null,"",0,clubId,60,null,"", null, null,0);
	}

	public static List<PlayerBowlingDto> getAllPlayersBowlingStatisticsLite(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersBowlingStatisticsLite(playerId,matchType,league,limit,clubId);
	}

	public static List<PlayerMatchBowlingDto> getPlayerMatchBowlingStatistics(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		return getDaoInstance().getPlayerMatchBowlingStatistics(playerId,matchType,league,limit,clubId);
	}
	
	public static List<PlayerMatchFieldingDto> getPlayerMatchFieldingStatistics(long playerId, String matchType, int clubId, int limit, int skip) throws Exception{
		return getDaoInstance().getPlayerMatchFieldingStatistics(playerId, matchType, null, limit, clubId);
	}

	public static List<PlayerFieldingDto> getAllPlayersFieldingStatistics(int teamId,long playerId, String matchType,String league, 
			int clubId, String seriesType, int limit, int skip) throws Exception{
		return getDaoInstance().getAllPlayersFieldingStatistics(teamId,playerId,matchType,league,clubId,0, seriesType, null, null, limit, skip);
	}
	public static List<PlayerFieldingDto> getAllPlayersFieldingStatistics(int teamId,long playerId, String matchType,String league,int clubId, String seriesType, String internalClubId, String year) throws Exception{
		return getDaoInstance().getAllPlayersFieldingStatistics(teamId,playerId,matchType,league,clubId,0, seriesType, internalClubId, year,0,0);
	}
	public static List<PlayerFieldingDto> getAllPlayersFieldingStatistics(int teamId,long playerId, String matchType,String league,int clubId, String seriesType, String internalClubId) throws Exception{
		return getDaoInstance().getAllPlayersFieldingStatistics(teamId,playerId,matchType,league,clubId,0, seriesType, internalClubId, null,0,0);
	}
	public static List<PlayerFieldingDto> getAllPlayersLastWeekFieldingStatistics(String matchType,String league,int clubId) throws Exception{
		return getDaoInstance().getAllPlayersFieldingStatistics(0,0,matchType,league,clubId,7, null, null, null,0,0);
	}

	public static List<BowlingDto> getPlayersBowlingByMatchIdTeamId(long matchId, long teamId,int clubId) throws Exception{
		return getDaoInstance().getPlayersBowlingByMatchIdTeamId(matchId,teamId,clubId);
	}
	
	public static void deleteAllBattingrecords(long matchId,int clubId) throws Exception{
		getDaoInstance().deleteAllBattingrecords(matchId,clubId);
	}

	public static void deleteAllBowlingrecords(long matchId,int clubId) throws Exception{
		getDaoInstance().deleteAllBowlingrecords(matchId,clubId);
	}

	public static void saveBattingRecords(List<BattingDto> battingRecords,int clubId) throws Exception{
		getDaoInstance().saveBattingRecords(battingRecords,clubId);
	}

	public static void saveBowlingRecords(List<BowlingDto> bowlingRecords,int clubId) throws Exception{
		getDaoInstance().saveBowlingRecords(bowlingRecords,clubId);
	}
	public static List<PlayerBattingDto> getPlayersBattingStatistics(long playerId, String matchType, int clubId) throws Exception{
		return PlayerStatisticsFactory.getPlayersBattingStatistics(playerId, matchType, 0, clubId);
	}

	public static List<PlayerBattingDto> getPlayersBattingStatistics(long playerId, String matchType,int leagueId, int clubId) throws Exception{
		
		List<PlayerBattingDto> players = getDaoInstance().getAllPlayersBattingStatistics(0,playerId, "l".equals(matchType)? null : matchType, leagueId==0?null:leagueId+"",0,clubId,0, null, "", null, null,0);
		
		Map<String, Integer> playerCorrections = null;
		
		if(leagueId == 0) {
			playerCorrections = PlayerFactory.getPlayerCorrections(playerId, clubId);
		}
		
		if(!CommonUtility.isListNullEmpty(players) || playerCorrections != null){
			
			PlayerBattingDto playerBattingDto = new PlayerBattingDto();
			if(players != null && !players.isEmpty()){
				playerBattingDto = players.get(0);
			}else {
				players = new ArrayList<PlayerBattingDto>();
				players.add(playerBattingDto);
			}
			
			if("l".equals(matchType)){
				
				if(playerCorrections != null && !playerCorrections.isEmpty()){
					playerBattingDto.setMatches(playerBattingDto.getMatches() + playerCorrections.get("matches"));
					playerBattingDto.setInnings(playerBattingDto.getInnings() + playerCorrections.get("bat_innings"));
					playerBattingDto.setNotOuts(playerBattingDto.getNotOuts() + playerCorrections.get("bat_no"));
					playerBattingDto.setBallsFaced(playerBattingDto.getBallsFaced() + playerCorrections.get("bat_balls"));
					playerBattingDto.setRunsScored(playerBattingDto.getRunsScored() + playerCorrections.get("bat_runs"));
					playerBattingDto.setHighestScore(playerBattingDto.getHighestScore() > playerCorrections.get("bat_hs")?playerBattingDto.getHighestScore():playerCorrections.get("bat_hs"));
					playerBattingDto.setHundreds(playerBattingDto.getHundreds() + playerCorrections.get("bat_100"));
					playerBattingDto.setFifties(playerBattingDto.getFifties() + playerCorrections.get("bat_50"));
					playerBattingDto.setTwentyFives(playerBattingDto.getTwentyFives() + playerCorrections.get("bat_25"));
					playerBattingDto.setFours(playerBattingDto.getFours() + playerCorrections.get("bat_4"));
					playerBattingDto.setSixers(playerBattingDto.getSixers() + playerCorrections.get("bat_6"));
					playerBattingDto.setDucks(playerBattingDto.getDucks() + playerCorrections.get("bat_0"));
					
				}
			}else if("p".equals(matchType) && players.size()>1){
				for(int i=1;i<players.size();i++){
					PlayerBattingDto bat = players.get(i);
					playerBattingDto.setMatches(playerBattingDto.getMatches() + bat.getMatches());
					playerBattingDto.setInnings(playerBattingDto.getInnings() + bat.getInnings());
					playerBattingDto.setNotOuts(playerBattingDto.getNotOuts() + bat.getNotOuts());
					playerBattingDto.setBallsFaced(playerBattingDto.getBallsFaced() + bat.getBallsFaced());
					playerBattingDto.setRunsScored(playerBattingDto.getRunsScored() + bat.getRunsScored());
					playerBattingDto.setHighestScore(playerBattingDto.getHighestScore() > bat.getHighestScore()?playerBattingDto.getHighestScore():bat.getHighestScore());
					playerBattingDto.setHundreds(playerBattingDto.getHundreds() + bat.getHundreds());
					playerBattingDto.setFifties(playerBattingDto.getFifties() + bat.getFifties());
					playerBattingDto.setTwentyFives(playerBattingDto.getTwentyFives() + bat.getTwentyFives());
					playerBattingDto.setFours(playerBattingDto.getFours() + bat.getFours());
					playerBattingDto.setSixers(playerBattingDto.getSixers() + bat.getSixers());
					playerBattingDto.setDucks(playerBattingDto.getDucks() + bat.getDucks());
				}
				players.subList(1, players.size()).clear();
			}
			return players;
		}
		return null;
	}
	public static List<PlayerBattingDto> getPlayersBattingStatisticsGroupBySeries(long playerId, int clubId, String seriesType) throws Exception{
		List<PlayerBattingDto> batting =  getDaoInstance().getAllPlayersBattingStatisticsGroupBy(playerId, "l", clubId, null, "series", seriesType);
		if(batting != null && !batting.isEmpty()){
			ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
			for(PlayerBattingDto player:batting){
				player.setLeagueName(club.getLeagueName(player.getLeagueId()));
			}
		}
		return batting;
	}
	
	public static List<PlayerBowlingDto> getPlayersBowlingStatisticsGroupBySeries(long playerId, int clubId, String seriesType) throws Exception{
		List<PlayerBowlingDto> bowling =  getDaoInstance().getAllPlayersBowlingStatisticsGroupBy(playerId, "l", clubId, null, "series", seriesType);
		if(bowling != null && !bowling.isEmpty()){
			ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
			for(PlayerBowlingDto player:bowling){
				player.setLeagueName(club.getLeagueName(player.getLeagueId()));
			}
		}
		return bowling;
	}
	public static List<PlayerBattingDto> getPlayersBattingStatisticsGroupByYear(long playerId, int clubId, String seriesType) throws Exception{
		List<PlayerBattingDto> batting =  getDaoInstance().getAllPlayersBattingStatisticsGroupBy(playerId, "l", clubId, null, "year", seriesType);
		return batting;
	}
	
	public static List<PlayerBowlingDto> getPlayersBowlingStatisticsGroupByYear(long playerId, int clubId, String seriesType) throws Exception{
		List<PlayerBowlingDto> bowling =  getDaoInstance().getAllPlayersBowlingStatisticsGroupBy(playerId, "l", clubId, null, "year", seriesType);
		
		return bowling;
	}
	public static List<PlayerBowlingDto> getPlayersBowlingStatistics(long playerId, String matchType, int clubId) throws Exception{
		return PlayerStatisticsFactory.getPlayersBowlingStatistics(playerId, matchType, 0, clubId);
	}
	public static List<PlayerBowlingDto> getPlayersBowlingStatistics(long playerId, String matchType,int leagueId, int clubId) throws Exception{
		List<PlayerBowlingDto> players = getDaoInstance().getAllPlayersBowlingStatistics(0,playerId, "l".equals(matchType) ? null : matchType, leagueId==0?null:leagueId+"",0,clubId,0,null,"", null, null,0);
		Map<String, Integer> playerCorrections = null;
		
		if(leagueId == 0) {
			playerCorrections = PlayerFactory.getPlayerCorrections(playerId, clubId);
		}
		
		//if(players != null && !players.isEmpty()){
		if(!CommonUtility.isListNullEmpty(players) || playerCorrections != null){
			
			PlayerBowlingDto bowling = new PlayerBowlingDto();
			if(players != null && !players.isEmpty()){
				bowling = players.get(0);
			}else {
				players = new ArrayList<PlayerBowlingDto>();
				players.add(bowling);
			}
			
			bowling.setCatches(getDaoInstance().getPlayerCatches(playerId,matchType,clubId,players.get(0).getSeriesType()));
			if("l".equals(matchType)){
				if(playerCorrections != null && !playerCorrections.isEmpty()){
					bowling.setMatches(bowling.getMatches() + playerCorrections.get("matches"));
					bowling.setInnings(bowling.getInnings() + playerCorrections.get("ball_innings"));
					bowling.setBalls(bowling.getBalls() + playerCorrections.get("ball_balls"));
					bowling.setRuns(bowling.getRuns() + playerCorrections.get("ball_runs"));
					bowling.setWickets(bowling.getWickets() + playerCorrections.get("ball_wickets"));
					bowling.setFourWickets(bowling.getFourWickets() + playerCorrections.get("ball_4w"));
					bowling.setFiveWickets(bowling.getFiveWickets() + playerCorrections.get("ball_5w"));
					
				}
			}else if("p".equals(matchType) && players.size()>1){
				for(int i=1;i<players.size();i++){
					PlayerBowlingDto bowl = players.get(i);
					bowling.setMatches(bowling.getMatches() + bowl.getMatches());
					bowling.setInnings(bowling.getInnings() + bowl.getInnings());
					bowling.setBalls(bowling.getBalls() + bowl.getBalls());
					bowling.setRuns(bowling.getRuns() + bowl.getRuns());
					bowling.setWickets(bowling.getWickets() + bowl.getWickets());
					bowling.setFourWickets(bowling.getFourWickets() + bowl.getFourWickets());
					bowling.setFiveWickets(bowling.getFiveWickets() + bowl.getFiveWickets());
				}
				players.subList(1, players.size()).clear();
			}
			return players;
		}
		return null;
	}


	public static Map<String, Integer> getPlayerBowlingOutType(int playerId, int clubId) throws Exception {
		return getDaoInstance().getPlayerBowlingOutType(playerId,clubId);
	}

	// TODO Need to create New PlayerStatisticSummaryDao
	public static void deleteAllPlayerSumary(int matchId, int clubId) throws Exception {
		getDaoInstance().deleteAllPlayerSumary(matchId,clubId);
	}
	
	public static void deleteAllPlayerSumaryFB(int matchId, int clubId) throws Exception {
		getDaoInstance().deleteAllPlayerSumaryFB(matchId,clubId);
	}

	public static List<PlayerRankingStatisticDto> getAllPlayersRankingsStatistics(String teamId,String playerId, String matchType,String league,int clubId, String internalClubId, int year) throws Exception{
		return getDaoInstance().getAllPlayersRankingsStatistics(teamId,playerId,matchType,league,clubId,internalClubId,year );
	}
	public static void saveAllPlayerSumary(List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos, int clubId) throws Exception {
		getDaoInstance().saveAllPlayerSumary(playerStatisticsSummaryDtos,clubId);
	}
	
	public static void saveAllPlayerSumaryFB(List<PlayerStatisticSummaryFBDto> playerStatisticsSummaryDtos, int clubId) throws Exception {
		getDaoInstance().saveAllPlayerSumaryFB(playerStatisticsSummaryDtos,clubId);
	}

	public static void deleteAllPlayerSumary(int clubId) throws Exception {
		// TODO Auto-generated method stub
		getDaoInstance().deleteAllPlayerSumary(clubId);
	}
	
	public static void deletePlayerSumaryForMatch(int clubId, int matchId) throws Exception {
		getDaoInstance().deletePlayerSumaryForMatch(clubId, matchId);
	}
	
	public static List<PlayerBattingDto> getAssociationPlayersBattingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		return getDaoInstance().getAssociationPlayersBattingStatistics(clubs, fromDate, toDate, seriesType, category, clubSeriesMap, limit == 0?10:limit);
	}
	public static List<PlayerBowlingDto> getAssociationPlayersBowlingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		return getDaoInstance().getAssociationPlayersBowlingStatistics(clubs, fromDate, toDate, seriesType, category, clubSeriesMap, limit == 0?10:limit);
	}
	public static List<PlayerRankingStatisticDto> getAssociationPlayersRankingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		return getDaoInstance().getAssociationPlayersRankingStatistics(clubs, fromDate, toDate, seriesType, category, clubSeriesMap, limit == 0?10:limit);
	}
	
	public static Map<String,Integer> getAssociationmetrics(List<ClubDtoLite> clubs, String seriesType, String category, Map<Integer,String> clubSeriesMap,String fromDate, String toDate) throws Exception{
		return getDaoInstance().getAssociationMetrics(clubs, seriesType, category, clubSeriesMap,fromDate,toDate);
	}
	
	public static Map<String,Integer> getAssociationmetrics2(List<ClubDtoLite> clubs, String seriesType, String category, Map<Integer,String> clubSeriesMap,String fromDate, String toDate) throws Exception{
		return getDaoInstance().getAssociationMetrics2(clubs, seriesType, category, clubSeriesMap,fromDate,toDate);
	}
	
	public static List<PlayerMatchRecordsDtoFB> getPlayerMatchStatsFB(int clubId, String seriesIdStr, int year, int playerId, int matchId, int limit, int skip) throws Exception{
		return getDaoInstance().getPlayerMatchStatsFB(clubId, seriesIdStr, year, playerId, matchId, limit, skip);
	}
	
}
