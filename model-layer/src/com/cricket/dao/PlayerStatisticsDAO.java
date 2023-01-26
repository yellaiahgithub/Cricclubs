/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.ClubDto;
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
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.cricket.utility.MatchUtility;
import com.football.dao.IncidentFactory;
import com.football.dto.PlayerMatchRecordsDtoFB;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerStatisticsDAO {

	private static Logger log = LoggerFactory.getLogger(PlayerStatisticsDAO.class);
	
	/**
	 * @param dto
	 * @return
	 */
	protected List<BattingDto> getPlayersBattingByMatchIdTeamId(long matchId, long teamId,int clubId) throws Exception{
		List<BattingDto> players = new ArrayList<BattingDto>();
		String query =
			"SELECT b.batting_id," +
			"mpt.match_id," +
			"mpt.player_id," +
			"mpt.team_id," +
			"p.f_name," +
			"p.l_name,runs_scored," +
			"p.nickname," +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"balls_faced," +
			"fours," +
			"sixers," +
			"how_out," +
//			"wicket_taker," +
			"wicket_taker1," +
			"wicket_taker2," +
			"b.subt_plyr_name," +
			"is_out," +
			"b.innings, " +
			"batting_style "+
			" FROM match_player_team mpt LEFT JOIN batting b on (b.player_id=mpt.player_id and b.match_id = mpt.match_id)," +
			"mcc.player p " +
			"where mpt.player_id = p.player_id";

		if(matchId != 0){
			query += " AND mpt.match_id = " + matchId;
		}
		
		if(teamId != 0){
			query += " AND mpt.team_id = " + teamId;
		}
		
		query += " order by -b.batting_id DESC,mpt.player_id";
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			BattingDto player = new BattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setMatchID(rs.getInt("match_id"));
			player.setTeamId(rs.getInt("team_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs_scored"));
			player.setNickName(rs.getString("nickname"));
			player.setBallsFaced(rs.getInt("balls_faced"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setHowOut(rs.getString("how_out"));
//			player.setWicketTaker(rs.getString("wicket_taker"));
			player.setWicketTaker1(rs.getString("wicket_taker1"));
			player.setWicketTaker2(rs.getString("wicket_taker2"));
			player.setIsOut(rs.getString("is_out"));
			player.setInnings((rs.getInt("innings")));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setSubtPlyrName(rs.getString("subt_plyr_name"));
			player.setBattingStyle(rs.getString("batting_style"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param days 
	 * @param internalClubId 
	 * @param dto
	 * @return
	 */
	protected List<PlayerBattingDto> getAllPlayersBattingStatistics(int teamId, long playerId, String matchType,String league,int limit,int clubId, int days, 
			String seriesType, String internalClubId, String year, String fixtureDate, int skip) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		List<PlayerBattingDto> players = new ArrayList<PlayerBattingDto>();
		String query =
				"select ps.player_id,  p.f_name,  p.l_name, p.playing_role, t.team_id, t.team_code, t.team_name, l.series_type, "+
				"IFNULL(t.logo_file_path, '') teamlogo_file_path, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+
				"sum(ps.runs_scored) as runs, sum(ps.balls_faced) as balls, sum(ps.fours) as fours, sum(ps.sixers) as sixers, sum(ps.batting_points) as points,"+
				"SUM(IF(ps.runs_scored > 24,  IF(ps.runs_scored < 50, 1, 0),  0)) twenty_five,"+
				"SUM(IF(ps.runs_scored > 74,  IF(ps.runs_scored < 100, 1, 0),  0)) seventy_five,"+
				"SUM(IF(ps.runs_scored > 49,  IF(ps.runs_scored < 75, 1, 0),  0)) fifty,"+
				"SUM(IF(ps.runs_scored > 99, 1, 0)) hundred, "+
				"SUM(IF(ps.runs_scored = 0, IF((ps.how_out is not null and trim(ps.how_out) <> '' and ps.how_out <> 'rt' ), 1, 0),  0)) duck, "+
				"SUM(IF(ps.how_out is null, 0, 1)) inns, count(distinct ps.match_id) as matches, "+
				"sum(IF((trim(ps.how_out) = '' or ps.how_out = 'rt'), 1, 0)) as no, max(runs_scored) as hs"+
				" from player_statistics_summary ps, mcc.player p, team t, league l where p.player_id = ps.player_id  "
				+ " and t.team_id = ps.team_id and l.league_id = ps.series_id ";
		
			String groupByCondition = " ";
			
			if(playerId != 0){
				query += " and ps.player_id = " + playerId ;
			}
			if(teamId != 0){
				query += " and ps.team_id = " + teamId ;
			}
			if(CommonUtility.stringToInt(year) > 0) {
				query += " and ps.match_date like '"+year+"%' " ;
			}
			
			if("other".equalsIgnoreCase(seriesType) ){
				query += " and (l.series_type = '' or l.series_type IS NULL) " ;
			} 
			else if(seriesType != null && !"".equals(seriesType.trim())){
				query += " and l.series_type like '" +seriesType  + "' " ;
			}
			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isOnlyDigitInString(league)){
				query += " and ps.series_id in (" + league + ") " ;
				groupByCondition = ",ps.team_id";				
			}
			
			if(days != 0){
				query += " and ps.match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
			}
			
			if(!CommonUtility.isNullOrEmpty(fixtureDate)) {
				query += " and ps.match_date < STR_TO_DATE('"+fixtureDate+"',  '%m/%d/%Y') ";
			}
			
			if(CommonUtility.stringToInt(internalClubId) > 0){
				query += " AND t.club_id = " + internalClubId ;
			}
			
				query += " and ps.match_type in ( " + (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po")? MatchUtility.MATCH_TYPE_PO : "'" +matchType + "'") + ") " +
				" group by ps.player_id " + groupByCondition + " , p.f_name, p.l_name ";
			
			if(playerId != 0){
				query += ", l.series_type  " ;
			}
			
			/*query += " having sum(b.balls_faced) >0 order by runs desc,balls  " ;*/
			query += " order by runs desc, balls  " ;
			
			if (limit > 0) {
				if(skip>0) {
					query += " LIMIT "+skip+","+ limit;
				}else {
					query += " LIMIT " + limit;
				}
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBattingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerBattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs"));
			player.setBallsFaced(rs.getInt("balls"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setTwentyFives(rs.getInt("twenty_five"));
			player.setSeventyFives(rs.getInt("seventy_five"));
			player.setFifties(rs.getInt("fifty"));
			player.setHundreds(rs.getInt("hundred"));
			player.setDucks(rs.getInt("duck"));
			player.setInnings(rs.getInt("inns"));
			player.setNotOuts(rs.getInt("no"));
			player.setHighestScore(rs.getInt("hs"));
			//player.setMatchType(rs.getString("match_type"));
			player.setSeriesType(rs.getString("series_type"));
			player.setSeriesType(player.getSeriesType()==null?null:player.getSeriesType().toString().trim().replace("One Day", "OneDay"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setPlayingRole(rs.getString("playing_role"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerMatchRecordsDtoFB> getPlayerMatchStatsFB(int clubId, String seriesIdStr, 
			int year, int playerId, int matchId, int limit, int skip) throws Exception{
		
		List<PlayerMatchRecordsDtoFB> players = new ArrayList<PlayerMatchRecordsDtoFB>();
		
		String query =  " select pst.player_id, p.f_name,  p.l_name, t.team_id, t.team_name, "
						+ "m.match_id, DATE_FORMAT(m.match_date,  '%m/%d/%Y') match_date, "
						+ "if(m.team_one = t.team_id, t2.team_id,t1.team_id) against_id," 
						+ "if(m.team_one = t.team_id, t2.team_name,t1.team_name) against_name,"
						+ "IF(m.winner = t1.team_id, t1.team_id, IF(m.winner = t2.team_id, t2.team_id,0)) winner_id, "
						+ "IF(m.winner = t1.team_id, t1.team_name,IF(m.winner = t2.team_id, t2.team_name,'No Winner')) winner_name, "
						+ " SUM(pst.goals_scored) goals_scored, SUM(assists) assists, SUM(goals_scored_penalty_kick) goals_scored_penalty,"
						+ "SUM(goals_scored_free_kick) goals_scored_free_kick,SUM(shot_on_target) shot_on_targets, SUM(tackles) tackles, "
						+ "SUM(interceptions) interceptions, SUM(goals_saved) goals_saved, SUM(yellow_cards) yellow_cards, "
						+ "SUM(red_cards) red_cards, SUM(offside) offside "		
						+ "from player_statistics_summary_fb pst, mcc.player p, team t, team t1, team t2, league l, matches m "
						+ "where pst.player_id = p.player_id AND pst.match_id = m.match_id AND t.team_id = pst.team_id "
						+ "AND l.league_id = t.league and m.team_one = t1.team_id and m.team_two = t2.team_id ";
			
			if(playerId>0){
				query += " and pst.player_id = " + playerId ;
			}
			if(matchId>0){
				query += " and m.matchId = " + matchId ;
			}
			if(year>0) {
				query += " and m.match_date like '"+year+"%' " ;
			}			
			if(!CommonUtility.isNullOrEmpty(seriesIdStr)){
				query += " and l.league_id in (" + seriesIdStr + ") " ;
			}
			query += " group by m.match_id ";			
			
			query += " order by m.match_id desc  " ;
			
			if (limit > 0) {
				if(skip>0) {
					query += " LIMIT "+skip+","+ limit;
				}else {
					query += " LIMIT " + limit;
				}
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while (rs.next()) {
			players.add(populatePlayerFootBallMatchRecordDto(rs));
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerStatisticsFBDto> getPlayersStatisticsFB(int clubId, long playerId, 
			int matchId, int teamId, String seriesIdStr, String matchType, int days, String year, 
			String fixtureDate, String groupBy, int limit, int skip) throws Exception {

		if (!MatchUtility.isValidateMatchType(matchType))
			return null;

		List<PlayerStatisticsFBDto> players = new ArrayList<PlayerStatisticsFBDto>();

		String query = "select pst.player_id, p.f_name, p.l_name, p.playing_role, p.profilepic_file_path, t.team_id, t.team_code, "
				+ "t.team_name, t.logo_file_path,count(DISTINCT(pst.match_id)) matches, pst.match_type, "
				+ "pst.match_date, YEAR(pst.match_date) match_year,"
				+ "l.league_id, l.league_name, SUM(pst.goals_scored) goals_scored, SUM(assists) assists, "
				+ "SUM(goals_scored_penalty_kick) goals_scored_penalty,SUM(goals_scored_free_kick) goals_scored_free_kick,"
				+ "SUM(shot_on_target) shot_on_targets, SUM(tackles) tackles, SUM(interceptions) interceptions, "
				+ "SUM(goals_saved) goals_saved, SUM(yellow_cards) yellow_cards, SUM(red_cards) red_cards, "
				+ "SUM(offside) offside from player_statistics_summary_fb pst, mcc.player p, team t, league l "
				+ "where pst.player_id = p.player_id AND pst.team_id = t.team_id AND t.league = l.league_id ";

		String groupByCondition = " ";

		if (playerId != 0) {
			query += " and pst.player_id = " + playerId;
		}
		if (teamId != 0) {
			query += " and pst.team_id = " + teamId;
		}
		if (CommonUtility.stringToInt(year) > 0) {
			query += " and pst.match_date like '" + year + "%' ";
		}
		if (!CommonUtility.isNullOrEmpty(seriesIdStr) && CommonUtility.isOnlyDigitInString(seriesIdStr)) {
			query += " and l.league_id in (" + seriesIdStr + ") ";
			groupByCondition = ",pst.team_id";
		}
		if (days != 0) {
			query += " and pst.match_date >= ( CURDATE() - INTERVAL " + days + " DAY ) ";
		}
		if (!CommonUtility.isNullOrEmpty(fixtureDate)) {
			query += " and pst..match_date < STR_TO_DATE('" + fixtureDate + "',  '%m/%d/%Y') ";
		}
		if (!CommonUtility.isNullOrEmpty(groupBy)) {
			groupByCondition = "," + groupBy;
		}
		query += " and pst.match_type in ( "
				+ (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL
						: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po") ? MatchUtility.MATCH_TYPE_PO
								: "'" + matchType + "'")+ ") ";

		query += " group by p.player_id " + groupByCondition + " , p.f_name, p.l_name order by goals_scored desc  ";

		if (limit > 0) {
			if (skip > 0) {
				query += " LIMIT " + skip + "," + limit;
			} else {
				query += " LIMIT " + limit;
			}
		}

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				players.add(populatePlayerFootBallStatDto(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	protected List<PlayerStatisticsFBDto> getPlayersPassingStatisticsFB(int teamId, long playerId, String matchType,String league,int limit,int clubId, int days, 
			String seriesType, String internalClubId, String year, String fixtureDate, int skip) throws Exception{
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		
		List<PlayerStatisticsFBDto> players = new ArrayList<PlayerStatisticsFBDto>();
		
		
			String query = " select mpt.player_id,  p.f_name,  p.l_name, t.team_id, t.team_code, t.team_name, "
				  + "count(DISTINCT(i.match_id)) matches, l.series_type, IFNULL(t.logo_file_path, '') teamlogo_file_path, "
				  + "IFNULL(p.profilepic_file_path, '') profilepic_file_path, "
				  + "0 goals_scored, SUM(IF(i.incident_type like '%Goal Scored%', 0, 1)) assists,"
				  + "0 shot_on_targets, 0 tackles, 0 interceptions, 0 goals_saved,0 yellow_cards, 0 red_cards "
				  + "from incidents i, match_player_team mpt, mcc.player p, team t, league l, matches m "
				  + "where p.player_id = i.player2_id  AND i.match_id = mpt.match_id "
				  + "AND mpt.player_id = i.player2_id AND mpt.player_id = p.player_id "
				  + "AND t.team_id = mpt.team_id AND l.league_id = t.league AND i.match_id = m.match_id ";			
			
			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isOnlyDigitInString(league)){
				query += " and l.league_id in (" + league + ") " ;
			}
			query += " and m.match_type in ( " + (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po")? MatchUtility.MATCH_TYPE_PO : "'" +matchType + "");
			
			query +=" group by player_id, f_name, l_name ";
			
			if (limit > 0) {
				if(skip>0) {
					query += " LIMIT "+skip+","+ limit;
				}else {
					query += " LIMIT " + limit;
				}
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerStatisticsFBDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			
			player = new PlayerStatisticsFBDto();
			
			player.setPlayerId(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setGoalsScored(rs.getInt("goals_scored"));
			player.setAssists(rs.getInt("assists"));
			player.setShotsOnTargets(rs.getInt("shot_on_targets"));
			player.setTackles(rs.getInt("tackles"));
			player.setInterceptions(rs.getInt("interceptions"));
			player.setGoalsSaved(rs.getInt("goals_saved"));
			player.setYellowCards(rs.getInt("yellow_cards"));
			player.setRedCards(rs.getInt("red_cards"));			
			player.setSeriesType(rs.getString("series_type"));
			player.setMatches(rs.getInt("matches"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setPlayingRole(rs.getString("playing_role"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerAllIncidentStatsFBDto> getPlayerAllIncidentStatsFB(int playerId, List<Integer> clubIds) throws Exception {
		
		List<PlayerAllIncidentStatsFBDto> playerStats = new ArrayList<PlayerAllIncidentStatsFBDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		query += "select incident_type, sum(no_of_times), player_type from ( ";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}			
				query += "SELECT incident_type, count(*) no_of_times, 'PLAYER1' as player_type "						
						+ "FROM "+clubSchema+".incidents WHERE player1_id = "+playerId						
						+ " GROUP BY player1_id, incident_type "						
						+ " UNION "
						+ "SELECT incident_type, count(*) no_of_times, 'PLAYER2' as player_type "						
						+ "FROM "+clubSchema+".incidents WHERE player2_id = "+playerId 
						+ " GROUP BY player2_id, incident_type ";
				
					query += " UNION ";
			}
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			query += " ) final_query group by incident_type ";
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					playerStats.add(populatePlayerAllIncidentStats(rs));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		return playerStats;
	}
	
	private PlayerAllIncidentStatsFBDto populatePlayerAllIncidentStats(ResultSet rs) throws SQLException {
		
		PlayerAllIncidentStatsFBDto dto = new PlayerAllIncidentStatsFBDto();
		
		dto.setIncidentType(rs.getString(1));
		dto.setNoOfTimes(rs.getInt(2));
		dto.setPlayerType(rs.getString(3));
		
		return dto;
	}

	protected List<PlayerBatBowlStatsDto> getInternalClubPlayersBatBowlStats(int clubId, int internalClubId) throws Exception{
		
		
		List<PlayerBatBowlStatsDto> players = new ArrayList<PlayerBatBowlStatsDto>();
		
		String query =
				"SELECT ps.player_id,  CONCAT(p.f_name,' ',p.l_name) player_name, l.league_name series_name, SUM(ps.runs_scored) AS runs_scored, " + 
						"SUM(ps.balls_faced) AS balls_faced, SUM(ps.fours) AS fours, SUM(ps.sixers) AS sixers, "+
						"SUM(IF(ps.runs_scored > 49,  IF(ps.runs_scored < 75, 1, 0),  0)) fifty, SUM(IF(ps.runs_scored > 99, 1, 0)) hundred," + 
						"SUM(IF((TRIM(ps.how_out) = '' OR ps.how_out = 'rt'), 1, 0)) AS Not_Outs, MAX(runs_scored) AS hs," + 
						"SUM(ps.runs_given) runs_given, SUM(ps.balls_bowled) balls_bowled, SUM(ps.catches + ps.catches_w) AS total_catches, " + 
						"SUM(ps.wickets) wickets, SUM(ps.maidens) maidens, SUM(ps.wides) wides, SUM(ps.no_balls) no_balls, SUM(ps.hatricks) hatricks, "+ 
						"SUM(IF(ps.wickets > 4, 1, 0)) five_wickets, SUM(IF(ps.how_out IS NULL, 0, 1)) innings_batting, "+
						"SUM(IF(ps.balls_bowled = 0, 0, 1)) innings_bowling, COUNT(DISTINCT ps.match_id) matches " + 
						"FROM player_statistics_summary ps, mcc.player p, club_player cp, league l " + 
						"WHERE  ps.player_id = p.player_id AND ps.player_id = cp.player_id AND ps.series_id = l.league_id "+
						 "AND p.player_id = cp.player_id AND cp.internal_club_id = 10 AND ps.match_type IN ( 'l','e','ql','sl','q','s','f','3p')  " + 
						"GROUP BY ps.player_id, p.f_name , p.l_name   ";
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBatBowlStatsDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			
			player = new PlayerBatBowlStatsDto();
			
			player.setPlayerId(rs.getInt("player_id"));
			player.setPlayerName(rs.getString("player_name"));
			player.setSeriesName(rs.getString("series_name"));
			player.setMatches(rs.getInt("matches"));
			player.setRunsScored(rs.getInt("runs_scored"));
			player.setBallsFaced(rs.getInt("balls_faced"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setFifties(rs.getInt("fifty"));
			player.setHundreds(rs.getInt("hundred"));
			player.setInningsBatting(rs.getInt("innings_batting"));
			player.setNotOuts(rs.getInt("Not_Outs"));
			player.setHighestScore(rs.getInt("hs"));			
			player.setRunsGiven(rs.getInt("runs_given"));
			player.setBallsBowled(rs.getInt("balls_bowled"));
			player.setFiveWickets(rs.getInt("five_wickets"));
			player.setWickets(rs.getInt("wickets"));
			player.setInningsBowling(rs.getInt("innings_bowling"));
			player.setCatches(rs.getInt("total_catches"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	
	
	protected List<PlayerBattingDto> getAllPlayersBattingStatisticsGroupBy(long playerId, String matchType,int clubId, String seriesType, String groupBy, String leagueType) throws Exception{
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		
		List<PlayerBattingDto> players = new ArrayList<PlayerBattingDto>();
		
		String query = "select ps.player_id,  ps.series_id as league, p.f_name,  p.l_name, t.team_id, t.team_code, t.team_name, l.series_type, "+
				"IFNULL(t.logo_file_path, '') teamlogo_file_path, IFNULL(p.profilepic_file_path, '') profilepic_file_path, YEAR(ps.match_date) matchyear, "+
				"sum(ps.runs_scored) as runs, sum(ps.balls_faced) as balls, sum(ps.fours) as fours, sum(ps.sixers) as sixers, sum(batting_points) as points,"+
				"SUM(IF(ps.runs_scored > 24,  IF(ps.runs_scored < 50, 1, 0),  0)) twenty_five,"+
				"SUM(IF(ps.runs_scored > 74,  IF(ps.runs_scored < 100, 1, 0),  0)) seventy_five,"+
				"SUM(IF(ps.runs_scored > 49,  IF(ps.runs_scored < 75, 1, 0),  0)) fifty,"+
				"SUM(IF(ps.runs_scored > 99, 1, 0)) hundred, "+
				"SUM(IF(ps.runs_scored = 0, IF((ps.how_out is not null and trim(ps.how_out) <> '' and ps.how_out <> 'rt' ), 1, 0),  0)) duck, "+
				"SUM(IF(ps.how_out is null, 0, 1)) inns, count(DISTINCT ps.match_id) as matches, "+
				"sum(IF((trim(ps.how_out) = '' or ps.how_out = 'rt' ), 1, 0)) as no, max(runs_scored) as hs"+
				" from player_statistics_summary ps, mcc.player p, team t, league l where p.player_id = ps.player_id  "
				+ " and t.team_id = ps.team_id and l.league_id = ps.series_id ";
			if(playerId != 0){
				query += " and ps.player_id = " + playerId ;
			}
			if(leagueType==null || leagueType.equalsIgnoreCase("null"))
				query += " and l.series_type IS NULL "  ;
			else
				query += " and upper(l.series_type) = '"+leagueType.trim().toUpperCase() +"' " ;
			if(groupBy.equalsIgnoreCase("year")){
				query += " and ps.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
				" group by ps.player_id,p.f_name,p.l_name, matchyear, l.series_type ";
			}
			else if(groupBy.equalsIgnoreCase("series")) {
				query += " and ps.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
				" group by ps.player_id,p.f_name,p.l_name,ps.series_id, l.series_type ";
			}
			if(playerId == 0){
				query += " having sum(ps.balls_faced) >0" ;
			}
			if(groupBy.equalsIgnoreCase("year")) {
				query += " order by matchyear desc";
			}
			else if(groupBy.equalsIgnoreCase("series")) {
				query += " order by length(league) desc, league desc";
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBattingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerBattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs"));
			player.setBallsFaced(rs.getInt("balls"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setTwentyFives(rs.getInt("twenty_five"));
			player.setSeventyFives(rs.getInt("seventy_five"));
			player.setSeriesType(rs.getString("series_type"));
			player.setSeriesType(player.getSeriesType()==null?null:player.getSeriesType().toString().trim().replace("One Day", "OneDay"));
			player.setFifties(rs.getInt("fifty"));
			player.setHundreds(rs.getInt("hundred"));
			player.setDucks(rs.getInt("duck"));
			player.setInnings(rs.getInt("inns"));
			player.setNotOuts(rs.getInt("no"));
			player.setHighestScore(rs.getInt("hs"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setLeagueId(rs.getInt("league"));
			player.setMatchYear(rs.getString("matchyear"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
		
	protected List<PlayerBowlingDto> getAllPlayersBowlingStatisticsGroupBy(long playerId, String matchType,int clubId, String seriesType, String groupBy, String leagueType) throws Exception{
		List<PlayerBowlingDto> players = new ArrayList<PlayerBowlingDto>();
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		
		StringBuffer query = new StringBuffer(
				"SELECT ps.player_id, p.f_name, p.l_name, ps.series_id as league, t.team_code, l.series_type, "+
				"IFNULL(t.logo_file_path, '') teamlogo_file_path, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+
				"YEAR(ps.match_date) matchyear, SUM(ps.runs_given) runs, SUM(ps.balls_bowled) balls, SUM(ps.wickets) wickets, "+
				"SUM(ps.maidens) maidens, SUM(ps.wides) wides, SUM(ps.bowling_points) points, SUM(ps.no_balls) no_balls,  "+
				"SUM(IF(ps.wickets = 4, 1, 0)) four_wickets, SUM(IF(ps.wickets > 4, 1, 0)) five_wickets, "+
				"SUM(IF(ps.balls_bowled = 0, 0, 1)) inns, count(DISTINCT ps.match_id) matches "+
				"FROM player_statistics_summary ps, mcc.player p, team t, league l  "+
				"WHERE ps.player_id = p.player_id and t.team_id = ps.team_id and l.league_id = ps.series_id  "
				);
			if(playerId != 0){
				query.append(" and ps.player_id = " + playerId) ;
			}

			if(leagueType==null || leagueType.equalsIgnoreCase("null"))
				query.append(" and l.series_type IS NULL " ) ;
			else
				query.append(" and upper(l.series_type) = '"+leagueType.trim().toUpperCase() +"' ") ;
			if(groupBy.equalsIgnoreCase("year")){
				query.append(" and ps.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
				" group by ps.player_id,p.f_name,p.l_name, matchyear, l.series_type  ");
			}
			else if(groupBy.equalsIgnoreCase("series")) {
				query.append(" and ps.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
				" group by ps.player_id,p.f_name,p.l_name,t.league,l.series_type  ");
			}
			if(playerId == 0){
				query.append( " having sum(balls) > 0") ;
			}
			if(groupBy.equalsIgnoreCase("year")) {
				query.append(" order by matchyear desc");
			}
			else if(groupBy.equalsIgnoreCase("series")) {
				query.append(" order by length(league) desc, league desc");
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBowlingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query.toString());
		while (rs.next()) {
			player = new PlayerBowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRuns(rs.getInt("runs"));
			player.setBalls(rs.getInt("balls"));
			player.setFourWickets(rs.getInt("four_wickets"));
			player.setFiveWickets(rs.getInt("five_wickets"));
			player.setWickets(rs.getInt("wickets"));
			player.setMaidens(rs.getInt("maidens"));
			player.setWides(rs.getInt("wides"));
			player.setInnings(rs.getInt("inns"));
			player.setNoBalls(rs.getInt("no_balls"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setLeagueId(rs.getInt("league"));
			player.setSeriesType(rs.getString("series_type"));
			player.setSeriesType(player.getSeriesType()==null?null:player.getSeriesType().toString().trim().replace("One Day", "OneDay"));
			player.setMatchYear(rs.getString("matchyear"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			
			
			if(groupBy.equalsIgnoreCase("year")) {
				player.setCatches(getPlayerCatches(playerId,matchType,clubId,groupBy,player.getMatchYear(),0,player.getSeriesType()));
			}
			else if(groupBy.equalsIgnoreCase("series")) {
				player.setCatches(getPlayerCatches(playerId,matchType,clubId,groupBy,"",player.getLeagueId(),player.getSeriesType()));
			}
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}	
	
	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerBattingDto> getAllPlayersBattingStatisticsLite(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;		
		List<PlayerBattingDto> players = new ArrayList<PlayerBattingDto>();
		String query =
			"SELECT p.player_id," +
			"p.f_name," +
			"p.l_name," +
			"t.team_id," +
			"t.team_name," +
			"t.team_code," +
			"IFNULL(t.logo_file_path,'') teamlogo_file_path, " +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"sum(b.runs_scored) runs," +
			"sum(b.balls_faced) balls," +
			"sum(b.fours) fours," +
			"sum(b.sixers) sixers," +
			"sum(b.points) points," +
			"sum(if(b.runs_scored>24,if(b.runs_scored<50,1,0),0)) twenty_five," +
			"sum(if(b.runs_scored>74,if(b.runs_scored<100,1,0),0)) seventy_five," +
			"sum(if(b.runs_scored>49,if(b.runs_scored<75,1,0),0)) fifty," +
			"sum(if(b.runs_scored>99,1,0)) hundred," +
			"sum(if(b.runs_scored=0,if(b.is_out = '0',0,1),0)) duck," +
			"sum(if(IFNULL(b.batting_id,0) = 0,0,1)) inns," +
			"sum(if(IFNULL(mpt.player_id,0) = 0,0,1)) matches," +
			"sum(if(b.is_out = 0,1,0)) no," +
			"max(b.runs_scored) hs" +
			" FROM mcc.player p JOIN match_player_team mpt on (mpt.player_id=p.player_id) JOIN batting b on (mpt.match_id = b.match_id and b.player_id=p.player_id) JOIN matches m on (m.match_id=mpt.match_id) JOIN team t on (t.team_id=mpt.team_id)	" +
			"where 1=1 " ;
			if(playerId != 0){
				query += " and p.player_id = " + playerId ;
			}
			if(league != null && !"".equals(league.trim()) && CommonUtility.isValidLeagueId(league)){
				query += " and t.league = '" + league + "'" ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED);
			}
			query += " and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
			"group by p.player_id,p.f_name,p.l_name having sum(b.balls_faced) >0 order by runs desc,balls";

			if(limit != 0){
				query += " LIMIT  " + limit ;
			}
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBattingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerBattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs"));
			player.setBallsFaced(rs.getInt("balls"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setTwentyFives(rs.getInt("twenty_five"));
			player.setSeventyFives(rs.getInt("seventy_five"));
			player.setFifties(rs.getInt("fifty"));
			player.setHundreds(rs.getInt("hundred"));
			player.setDucks(rs.getInt("duck"));
			player.setInnings(rs.getInt("inns"));
			player.setNotOuts(rs.getInt("no"));
			player.setHighestScore(rs.getInt("hs"));
			//player.setMatchType(rs.getString("match_type"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerDto> getAllPlayersMOMStatistics(long playerId, String matchType,String league,int limit,int clubId, int days) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query =
				"SELECT ps.player_id, p.l_name, p.f_name, p.playing_role , IFNULL(t.logo_file_path, '') teamlogo_file_path, "+
				"IFNULL(p.profilepic_file_path, '') profilepic_file_path, COUNT(*) count "+
				"FROM mcc.player p, player_statistics_summary ps, team t "+
				"WHERE p.player_id = ps.player_id and ps.team_id = t.team_id and  ps.man_of_the_match is true ";

			if(playerId != 0){
				query += " and ps.player_id = " + playerId ;
			}
			if(league != null && !"".equals(league.trim()) && CommonUtility.isOnlyDigitInString(league)){				
				query += " and ps.series_id in (" + league + ") " ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED); 
			}
			
			if(days != 0){
				query += " and ps.match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
			}

			
			query += " and ps.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
			"group by ps.player_id,p.f_name,p.l_name";

			if(limit != 0){
				query += " LIMIT  " + limit ;
			}
			
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setOtherPoints(rs.getInt("count")*50);
			player.setPlayingRole(rs.getString("playing_role"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	protected List<PlayerMatchBattingDto> getPlayerMatchBattingStatistics(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		List<PlayerMatchBattingDto> players = new ArrayList<PlayerMatchBattingDto>();
		String query =
			"SELECT p.player_id," +
			"p.f_name," +
			"p.l_name," +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"b.runs_scored runs," +
			"b.balls_faced balls," +
			"b.fours fours," +
			"b.sixers sixers," +
			"b.points," +
			"IFNULL(b.how_out,'') how_out," +
			"m.match_id," +
			"DATE_FORMAT(m.MATCH_DATE,  '%m/%d/%Y') MATCH_DATE,	" +
			"t.team_id," +
			"t.team_name," +
			"t.team_code," +
			"IFNULL(t1.logo_file_path,'') t1_logo_file_path, " +
			"IFNULL(t2.logo_file_path,'') t2_logo_file_path, " +
			"if(m.team_one = t.team_id, t2.team_id,t1.team_id) against_id," +
			"if(m.team_one = t.team_id, t2.team_name,t1.team_name) against_name," +
			// "if(m.winner = t1.team_id, t1.team_id,t2.team_id) winner_id," +
			" IF(m.winner = t1.team_id, t1.team_id, IF(m.winner = t2.team_id, t2.team_id,0)) winner_id, " +
			//"if(m.winner = t1.team_id, t1.team_name,t2.team_name) winner_name," +
			" IF(m.winner = t1.team_id, t1.team_name,IF(m.winner = t2.team_id, t2.team_name,'No Winner')) winner_name, " +
			"if(b.is_out = 0,1,0) no, " +
			"ifnull(b.runs_scored,-1) dnb, " +
			"b.batting_position " +
			" FROM mcc.player p LEFT JOIN match_player_team mpt on (mpt.player_id=p.player_id) " +
			" LEFT JOIN batting b on (mpt.match_id = b.match_id and b.player_id=p.player_id) " +
			" LEFT JOIN matches m on (m.match_id=mpt.match_id) " +
			" LEFT JOIN team t on (t.team_id=mpt.team_id) " +
			" LEFT JOIN team t1 on (t1.team_id=m.team_one) " +
			" LEFT JOIN team t2 on (t2.team_id=m.team_two) " +
			"where 1=1 " ;
			if(playerId != 0){
				query += " and p.player_id = " + playerId ;
			}
			if(league != null && !"".equals(league.trim()) && CommonUtility.isValidLeagueId(league)){
				query += " and t.league = '" + league + "'" ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED);
			}
			query += " and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " ;
			
			query += " ORDER BY m.MATCH_DATE DESC ";
			if(limit != 0){
				query += " LIMIT  " + limit ;
			}
			
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerMatchBattingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerMatchBattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs"));
			player.setBallsFaced(rs.getInt("balls"));
			player.setFours(rs.getInt("fours"));
			player.setSixers(rs.getInt("sixers"));
			player.setMatchId(rs.getInt("match_id"));
			player.setTeamId(rs.getInt("team_id"));
			player.setAgainstId(rs.getInt("against_id"));
			player.setNo(rs.getInt("no"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamCode(rs.getString("team_code"));
			player.setAgainstName(rs.getString("against_name"));
			player.setWinnerId(rs.getInt("winner_id"));
			player.setWinnerName(rs.getString("winner_name"));
			player.setMatchDate(rs.getString("match_date"));
			player.setPoints(rs.getInt("points"));
			player.setDidNotBat(rs.getInt("dnb"));
			player.setOutType(rs.getString("how_out"));
			player.setT1_logo_file_path(rs.getString("t1_logo_file_path"));
			player.setT2_logo_file_path(rs.getString("t2_logo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setBattingPosition(rs.getInt("batting_position"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerBowlingDto> getAllPlayersBowlingStatistics(int teamId,long playerId, String matchType,String league,int limit,int clubId,int days, 
			String seriesType, String internalClubId, String year, String fixtureDate, int skip) throws Exception{
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		
		List<PlayerBowlingDto> players = new ArrayList<PlayerBowlingDto>();

		String query =
				"SELECT ps.player_id, p.f_name, p.l_name, p.playing_role,  ps.team_id, t.team_name, t.team_code, l.series_type, IFNULL(t.logo_file_path, '') "
				+ "teamlogo_file_path, SUM(ps.dot_balls) AS dot_balls, "+
				"IFNULL(p.profilepic_file_path, '') profilepic_file_path, SUM(ps.runs_given) runs, SUM(ps.balls_bowled) balls, "+
				"   SUM(ps.catches + ps.catches_w) AS total_catches, "+
				"SUM(ps.wickets) wickets, SUM(ps.maidens) maidens, SUM(ps.wides) wides, SUM(ps.bowling_points) points, "+
				"SUM(ps.no_balls) no_balls, SUM(ps.hatricks) hatricks, SUM(IF(ps.wickets = 4, 1, 0)) four_wickets, "
				+ "SUM(IF(ps.wickets > 4, 1, 0)) five_wickets, "+
				"SUM(IF(ps.balls_bowled = 0, 0, 1)) inns, count(distinct ps.match_id) matches "+
				"FROM mcc.player p, player_statistics_summary ps , team t, league l "+
				"WHERE  ps.player_id = p.player_id and ps.team_id = t.team_id  and ps.series_id = l.league_id";
		
		String groupByCondition = " ";
		
			if(playerId != 0){
				query += " and ps.player_id = " + playerId ;
			}
			if(teamId != 0){
				query += " and ps.team_id = " + teamId ;
			}
			
			if(CommonUtility.stringToInt(year) > 0) {
				query += " and ps.match_date like '"+year+"%' " ;
			}
			
			if(seriesType != null && !"".equals(seriesType.trim()) && seriesType.trim().equalsIgnoreCase("other") ){
				query += " and (l.series_type = '' or l.series_type IS NULL) " ;
				
			} 
			else if(seriesType != null && !"".equals(seriesType.trim())){
				
				query += " and l.series_type like '" +seriesType  + "'  " ;
			}			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isOnlyDigitInString(league)){				
				query += " and ps.series_id in (" + league + ") " ;
				groupByCondition = ",ps.team_id";
			}			
			if(days != 0){
				query += " and ps.match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
			}
			if(!CommonUtility.isNullOrEmpty(fixtureDate)) {
				query += " and ps.match_date < STR_TO_DATE('"+fixtureDate+"',  '%m/%d/%Y') ";
			}
			if(CommonUtility.stringToInt(internalClubId) > 0){
				query += " AND t.club_id = " + internalClubId ;
			}			
			query += " and ps.match_type in ( " + (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po")? MatchUtility.MATCH_TYPE_PO: "'" +matchType + "'") + ") " +
					" GROUP BY ps.player_id " + groupByCondition + " , p.f_name , p.l_name  ";
			if(playerId != 0){
				query += ", l.series_type  " ;
			}
			if(playerId == 0){
				query += " HAVING SUM(ps.balls_bowled) > 0 " ;
			}
			query += " ORDER BY wickets DESC , (SUM(ps.runs_given) / SUM(ps.balls_bowled)) ASC ";

			if (limit > 0) {
				if(skip>0) {
					query += " LIMIT "+skip+","+ limit;
				}else {
					query += " LIMIT " + limit;
				}
			}

		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBowlingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerBowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRuns(rs.getInt("runs"));
			player.setBalls(rs.getInt("balls"));
			player.setFourWickets(rs.getInt("four_wickets"));
			player.setFiveWickets(rs.getInt("five_wickets"));
			player.setWickets(rs.getInt("wickets"));
			player.setMaidens(rs.getInt("maidens"));
			player.setWides(rs.getInt("wides"));
			player.setInnings(rs.getInt("inns"));
			player.setNoBalls(rs.getInt("no_balls"));
			player.setHattricks(rs.getInt("hatricks"));
//			player.setMatchType(rs.getString("match_type"));
			player.setSeriesType(rs.getString("series_type"));
			player.setSeriesType(player.getSeriesType()==null?null:player.getSeriesType().toString().trim().replace("One Day", "OneDay"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setCatches(rs.getInt("total_catches"));
			player.setPlayingRole(rs.getString("playing_role"));
			player.setDotBalls(rs.getInt("dot_balls"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	protected int getPlayerCatches(long playerId, String matchType, int clubId, String seriesType) throws Exception {
		if(!MatchUtility.isValidateMatchType(matchType)) return 0;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return 0;
		
		int catches = 0;
		String query = "select count(*) catches from batting b,matches m LEFT JOIN match_player_team mpt on (mpt.match_id=m.match_id) LEFT JOIN team t on (t.team_id=mpt.team_id)  LEFT JOIN league l on (t.league=l.league_id)" +
				"where m.match_id=b.match_id and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ")  and how_out in('ct','ctw','cw') and wicket_taker2 =" + playerId +" and l.series_type='"+seriesType+"' and mpt.player_id = "+playerId;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				catches = rs.getInt("catches");
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return catches;
	}
	
	protected int getPlayerCatches(long playerId, String matchType, int clubId, String groupBy, String stringValue, int intValue, String seriesType) throws Exception {
		if(!MatchUtility.isValidateMatchType(matchType)) return 0;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return 0;
		int catches = 0;
		String query = "";
		//String query = "select count(*) catches from batting b,matches m, match_player_team mpt, team t where (mpt.player_id="+playerId+") and (t.team_id=mpt.team_id) and m.match_id=b.match_id and m.match_type in ( " + (matchType.equals("l")?ApplicationConstants.MATCH_TYPE_ALL:"'" +matchType + "'") + ")  and how_out in('ct','ctw') and wicket_taker2 =" + playerId ;
				//query = query + "  ";
				if(groupBy.equalsIgnoreCase("year")) {
					//query = query + " and YEAR(m.match_date) = "+stringValue;
					query = "select count(*) catches from batting b, matches m LEFT JOIN match_player_team mpt on "
							+ " (mpt.match_id=m.match_id) LEFT JOIN team t on (t.team_id=mpt.team_id)  LEFT JOIN league l on (t.league=l.league_id)"  
					+ " where m.match_id = b.match_id "
					+ " and m.match_type in ("+MatchUtility.MATCH_TYPE_ALL+") and how_out in ('ct','cw','ctw') "
					+ " and wicket_taker2 = "+ playerId +" and year(m.match_date) = "+ stringValue +"  and l.series_type='"+seriesType
					+"' and mpt.player_id = "+playerId;
				}
				else if(groupBy.equalsIgnoreCase("series")) {
					//query = query + " and t.league =  "+intValue;
					query = "select count(*) catches from batting b, matches m LEFT JOIN match_player_team mpt on (mpt.match_id=m.match_id) "
							+ " LEFT JOIN team t on (t.team_id=mpt.team_id)  LEFT JOIN league l on (t.league=l.league_id)"
						+ " where m.match_id = b.match_id "
						+ " and m.match_type in ("+MatchUtility.MATCH_TYPE_ALL+") and how_out in ('ct','cw','ctw') "
						+ " and wicket_taker2 = "+ playerId +"  and l.series_type='"+seriesType+"' and mpt.player_id = "+playerId
						+" and m.match_id in "
						+ " ( "
						+ " select match_id from match_player_team " 
						+ " where team_id in (select team_id from team where league = "+intValue+" ) " 
						+ " and player_id = "+playerId+" "
						+" ) " ;
				}
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				catches = rs.getInt("catches");
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return catches;
	}
	
	protected List<PlayerBowlingDto> getAllPlayersBowlingStatisticsLite(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		List<PlayerBowlingDto> players = new ArrayList<PlayerBowlingDto>();

		String query =
			"SELECT p.player_id," +
			"p.f_name," +
			"p.l_name," +
			"t.team_id," +
			"t.team_name," +
			"t.team_code," +
			"IFNULL(t.logo_file_path,'') teamlogo_file_path, " +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"sum(b.runs) runs," +
			"sum(b.balls) balls," +
			"sum(b.wickets) wickets," +
			"sum(b.maidens) maidens," +
			"sum(b.wides) wides," +
			"sum(b.points) points," +
			"sum(b.no_balls) no_balls," +
			"sum(b.hatricks) hatricks," +
			"sum(b.dot_balls) dot_balls," +
			"sum(if(b.wickets=4,1,0)) four_wickets," +
			"sum(if(b.wickets>4,1,0)) five_wickets," +
			"sum(if(IFNULL(b.bowling_id,0) = 0,0,1)) inns," +
			"sum(if(IFNULL(mpt.player_id,0) = 0,0,1)) matches" +
			" FROM mcc.player p JOIN match_player_team mpt on (mpt.player_id=p.player_id) JOIN bowling b on (mpt.match_id = b.match_id and b.player_id=p.player_id) JOIN matches m on (m.match_id=mpt.match_id) JOIN team t on (t.team_id=mpt.team_id)" +
			"  where 1=1";
			if(playerId != 0){
				query += " and p.player_id = " + playerId ;
			}
			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isValidLeagueId(league)){
				query += " and t.league = '" + league + "'" ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED);
			}

			query += " and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " +
			" group by p.player_id,p.f_name,p.l_name ";
			if(playerId == 0){
				query += " having sum(balls) > 0" ;
			}
			query += " order by wickets desc, (sum(runs)/sum(balls)) asc";

			if(limit != 0){
				query += " LIMIT  " + limit ;
			}

		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerBowlingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerBowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRuns(rs.getInt("runs"));
			player.setBalls(rs.getInt("balls"));
			player.setFourWickets(rs.getInt("four_wickets"));
			player.setFiveWickets(rs.getInt("five_wickets"));
			player.setWickets(rs.getInt("wickets"));
			player.setMaidens(rs.getInt("maidens"));
			player.setWides(rs.getInt("wides"));
			player.setInnings(rs.getInt("inns"));
			player.setNoBalls(rs.getInt("no_balls"));
			player.setHattricks(rs.getInt("hatricks"));
			player.setDotBalls(rs.getInt("dot_balls"));
//			player.setMatchType(rs.getString("match_type"));
			player.setMatches(rs.getInt("matches"));
			player.setPoints(rs.getInt("points"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamCode(rs.getString("team_code"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	protected List<PlayerMatchBowlingDto> getPlayerMatchBowlingStatistics(long playerId, String matchType,String league,int limit,int clubId) throws Exception{
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		List<PlayerMatchBowlingDto> players = new ArrayList<PlayerMatchBowlingDto>();

		String query =
			"SELECT p.player_id," +
			"p.f_name," +
			"p.l_name," +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"b.runs runs," +
			"b.balls balls," +
			"b.wickets wickets," +
			"b.maidens maidens," +
			"b.wides wides," +
			"b.no_balls no_balls," +
			"b.hatricks hatricks," +
			"b.dot_balls dot_balls," +
			"b.points," +
			"m.match_id," +
			"DATE_FORMAT(m.MATCH_DATE,  '%m/%d/%Y') MATCH_DATE,	" +
			"t.team_id," +
			"t.team_name," +
			"t.team_code," +
			"IFNULL(t1.logo_file_path,'') t1_logo_file_path, " +
			"IFNULL(t2.logo_file_path,'') t2_logo_file_path, " +
			"if(m.team_one = t.team_id, t2.team_id,t1.team_id) against_id," +
			"if(m.team_one = t.team_id, t2.team_name,t1.team_name) against_name," +
			"if(m.team_one = t.team_id, t2.team_code,t1.team_code) against_code," +
			//"if(m.winner = t1.team_id, t1.team_id,t2.team_id) winner_id," +
			" IF(m.winner = t1.team_id, t1.team_id, IF(m.winner = t2.team_id, t2.team_id,0)) winner_id, "+
			//"if(m.winner = t1.team_id, t1.team_name,t2.team_name) winner_name, " +
			" IF(m.winner = t1.team_id, t1.team_name,IF(m.winner = t2.team_id, t2.team_name,'No Winner')) winner_name, "+
			//"if(m.winner = t1.team_id, t1.team_code,t2.team_code) winner_code " +
			" if(m.winner = t1.team_id, t1.team_code,if(m.winner = t2.team_id, t2.team_code,'NW')) winner_code " +
			" FROM mcc.player p LEFT JOIN match_player_team mpt on (mpt.player_id=p.player_id) " +
			" LEFT JOIN bowling b on (mpt.match_id = b.match_id and b.player_id=p.player_id) " +
			" LEFT JOIN matches m on (m.match_id=mpt.match_id) " +
			" LEFT JOIN team t on (t.team_id=mpt.team_id)" +
			" LEFT JOIN team t1 on (t1.team_id=m.team_one) " +
			" LEFT JOIN team t2 on (t2.team_id=m.team_two) " +
			"  where balls > 0 ";
			if(playerId != 0){
				query += " and p.player_id = " + playerId ;
			}
			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isValidLeagueId(league)){
				query += " and t.league = '" + league + "'" ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED);
			}

			query += " and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " ;

			if(limit != 0){
				query += " LIMIT  " + limit ;
			}

		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerMatchBowlingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			player = new PlayerMatchBowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRuns(rs.getInt("runs"));
			player.setBalls(rs.getInt("balls"));
			player.setWickets(rs.getInt("wickets"));
			player.setMaidens(rs.getInt("maidens"));
			player.setWides(rs.getInt("wides"));
			player.setNoBalls(rs.getInt("no_balls"));
			player.setHattricks(rs.getInt("hatricks"));
			player.setDotBalls(rs.getInt("dot_balls"));
			player.setMatchId(rs.getInt("match_id"));
			player.setTeamId(rs.getInt("team_id"));
			player.setAgainstId(rs.getInt("against_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setAgainstName(rs.getString("against_name"));
			player.setAgainstCode(rs.getString("against_code"));
			player.setWinnerId(rs.getInt("winner_id"));
			player.setWinnerName(rs.getString("winner_name"));
			player.setWinnerCode(rs.getString("winner_code"));
			player.setMatchDate(rs.getString("match_date"));
			player.setPoints(rs.getInt("points"));
			player.setT1_logo_file_path(rs.getString("t1_logo_file_path"));
			player.setT2_logo_file_path(rs.getString("t2_logo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
//			player.setMatchType(rs.getString("match_type"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerFieldingDto> getAllPlayersFieldingStatistics(int teamId, long playerId, String matchType,String leagueId,int clubId,int days, 
			String seriesType, String internalClubId, String year, int limit, int skip) throws Exception{
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		seriesType = seriesType==null?null:seriesType.toString().trim().replace("OneDay", "One Day");
		if(!CommonUtility.isValidateSeriesType(seriesType)) return null;
		
		List<PlayerFieldingDto> players = new ArrayList<PlayerFieldingDto>();
		
		String query =
				"SELECT ps.player_id, SUM(IF(ps.balls_faced = 0, 0, 1)) inns, count(distinct ps.match_id) matches , p.f_name, p.l_name, p.playing_role,  t.team_id, t.team_name,  "+
				"IFNULL(t.logo_file_path, '') logo_file_path, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+ 
				"sum(direct_ro) as direct, sum(indirect_ro) as indirect, sum(stumpings) as stumpings, sum(catches + catches_w) as catches,  "+
				"sum(catches_w) as catches_w, SUM(direct_ro + indirect_ro + stumpings + catches + catches_w) total "+
				"FROM mcc.player p, player_statistics_summary ps , team t, league l  "+
				"WHERE  ps.player_id = p.player_id and ps.team_id = t.team_id  and ps.series_id = l.league_id " ;
		
		String groupByCondition = " ";
		
		matchType = " and ps.match_type in ( " + (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po")? MatchUtility.MATCH_TYPE_PO : "'" +matchType + "'") + ") " ;
		String leagueConstraint = "";
		if(leagueId != null && !"".equals(leagueId.trim()) && CommonUtility.isOnlyDigitInString(leagueId)){
			//query += " and t.league = '" + league + "'" ;
			 leagueConstraint = " and ps.series_id in (" + leagueId + ") " ;
			 groupByCondition = ",ps.team_id";	
		}		
		String teamConstraint = "";
		if(teamId != 0){
			teamConstraint = " and ps.team_id = "+ teamId;
		}
		if(CommonUtility.stringToInt(internalClubId) > 0){
			query += " AND t.club_id = " + internalClubId ;
		}
		
		if(CommonUtility.stringToInt(year) > 0) {
			query += " and ps.match_date like '"+year+"%' " ;
		}
		
		String dateConstraint = "";
		String dateConstraint2 = "";
		if(days != 0){
			dateConstraint += " and ps.match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
			dateConstraint2 += " and ps.match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
		}

			if(playerId != 0){
				query += " and ps.player_id = " + playerId ;
			}
			
			query += matchType + leagueConstraint + teamConstraint + dateConstraint;
			
			query += " group by ps.player_id " + groupByCondition + " , p.profilepic_file_path," +
			" p.l_name," +
			" p.f_name " +
				/*" having total>0 " +*/
			" order by total desc,catches desc";
			
			if (limit > 0) {
				if(skip>0) {
					query += " LIMIT "+skip+","+ limit;
				}else {
					query += " LIMIT " + limit;
				}
			}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerFieldingDto player = null; 
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			player = new PlayerFieldingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setTotalMatches(rs.getInt("matches"));
			player.setTotalInngs(rs.getInt("inns"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setCatches(rs.getInt("catches"));
			player.setWkcatches(rs.getInt("catches_w"));
			player.setDirect(rs.getInt("direct"));
			player.setIndirect(rs.getInt("indirect"));
			player.setStumpings(rs.getInt("stumpings"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setTotal(rs.getInt("total"));
			player.setTeamlogo_file_path(rs.getString("logo_file_path"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setPlayingRole(rs.getString("playing_role"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerMatchFieldingDto> getPlayerMatchFieldingStatistics(long playerId, String matchType, String league, int limit, int clubId) throws Exception{
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		
		List<PlayerMatchFieldingDto> players = new ArrayList<PlayerMatchFieldingDto>();

		String query =
			"SELECT p.player_id," +
			"p.f_name," +
			"p.l_name," +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"pss.direct_ro as direct, "	+ 
			"pss.indirect_ro as indirect, "+ 
			"pss.stumpings as stumpings, "+ 
			"pss.catches as catches,"  + 
			"catches_w as catches_w,"+
			"pss.fielding_points," +
			"m.match_id," +
			"DATE_FORMAT(m.MATCH_DATE,  '%m/%d/%Y') MATCH_DATE,	" +
			"t.team_id," +
			"t.team_name," +
			"t.team_code," +
			"IFNULL(t1.logo_file_path,'') t1_logo_file_path, " +
			"IFNULL(t2.logo_file_path,'') t2_logo_file_path, " +
			"if(m.team_one = t.team_id, t2.team_id,t1.team_id) against_id," +
			"if(m.team_one = t.team_id, t2.team_name,t1.team_name) against_name," +
			"if(m.team_one = t.team_id, t2.team_code,t1.team_code) against_code," +
			" IF(m.winner = t1.team_id, t1.team_id, IF(m.winner = t2.team_id, t2.team_id,0)) winner_id, "+
			" IF(m.winner = t1.team_id, t1.team_name,IF(m.winner = t2.team_id, t2.team_name,'No Winner')) winner_name, "+
			" if(m.winner = t1.team_id, t1.team_code,if(m.winner = t2.team_id, t2.team_code,'NW')) winner_code " +
			" FROM mcc.player p LEFT JOIN match_player_team mpt on (mpt.player_id=p.player_id) " +
			" LEFT JOIN player_statistics_summary pss on (mpt.match_id = pss.match_id and pss.player_id=p.player_id) " +
			" LEFT JOIN matches m on (m.match_id=mpt.match_id) " +
			" LEFT JOIN team t on (t.team_id=mpt.team_id)" +
			" LEFT JOIN team t1 on (t1.team_id=m.team_one) " +
			" LEFT JOIN team t2 on (t2.team_id=m.team_two) " +
			"  where 1=1 ";
			if(playerId != 0){
				query += " and p.player_id = " + playerId ;
			}
			
			if(league != null && !"".equals(league.trim()) && CommonUtility.isValidLeagueId(league)){
				query += " and t.league = '" + league + "'" ;
			}else if(playerId == 0){
				throw new CCException("League name is required.", CCErrorConstant.LEAGUE_NAME_REQUIRED);
			}

			query += " and m.match_type in ( " + (matchType.equals("l")?MatchUtility.MATCH_TYPE_ALL:"'" +matchType + "'") + ") " ;

			if(limit != 0){
				query += " LIMIT  " + limit ;
			}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PlayerMatchFieldingDto player = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		while (rs.next()) {
			
			player = new PlayerMatchFieldingDto();
			
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setCatches(rs.getInt("catches"));
			player.setWkcatches(rs.getInt("catches_w"));
			player.setDirect(rs.getInt("direct"));
			player.setIndirect(rs.getInt("indirect"));
			player.setStumpings(rs.getInt("stumpings"));
			player.setMatchId(rs.getInt("match_id"));
			player.setTeamId(rs.getInt("team_id"));
			player.setAgainstId(rs.getInt("against_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setAgainstName(rs.getString("against_name"));
			player.setAgainstCode(rs.getString("against_code"));
			player.setWinnerId(rs.getInt("winner_id"));
			player.setWinnerName(rs.getString("winner_name"));
			player.setWinnerCode(rs.getString("winner_code"));
			player.setMatchDate(rs.getString("match_date"));
			
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param l
	 * @param m
	 * @return
	 */
	protected List<BowlingDto> getPlayersBowlingByMatchIdTeamId(long matchId, long teamId,int clubId) throws Exception {
		List<BowlingDto> players = new ArrayList<BowlingDto>();
		String query =
			"SELECT b.bowling_id," +
			"mpt.match_id," +
			"mpt.player_id," +
			"mpt.team_id," +
			"p.f_name," +
			"p.l_name," +
			"IFNULL(p.profilepic_file_path,'') profilepic_file_path, " +
			"balls," +
			"runs," +
			"wides," +
			"no_balls," +
			"wickets," +
			"maidens," +
			"hatricks, " +
			"dot_balls, " +
			"b.innings, " +
			"p.bowling_style "+
			" FROM match_player_team mpt LEFT JOIN bowling b on (b.player_id=mpt.player_id and b.match_id = mpt.match_id)," +
			"mcc.player p " +
			"where mpt.player_id = p.player_id";

			
		if(matchId != 0){
			query += " AND mpt.match_id = " + matchId;
		}
		
		if(teamId != 0){
			query += " AND mpt.team_id = " + teamId;
		}
		query += " order by b.bowling_id";

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getReadConnection(clubId);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while (rs.next()) {
			BowlingDto player = new BowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setMatchID(rs.getInt("match_id"));
			player.setTeamId(rs.getInt("team_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setBalls(rs.getInt("balls"));
			player.setRuns(rs.getInt("runs"));
			player.setWides(rs.getInt("wides"));
			player.setNoBalls(rs.getInt("no_balls"));
			player.setWickets(rs.getInt("wickets"));
			player.setMaidens(rs.getInt("maidens"));
			player.setHattricks(rs.getInt("hatricks"));
			player.setDotBalls(rs.getInt("dot_balls"));
			player.setInnings((rs.getInt("innings")));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setBowlingStyle(rs.getString("bowling_style"));
			players.add(player);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	/**
	 * @param l
	 */
	protected void deleteAllBattingrecords(long matchId,int clubId) throws Exception {
		
		String query = "delete from batting where match_id = " + matchId;
		Statement st = null;
		Connection conn = null;
		try {
		conn = DButility.getConnection(clubId);
		st = conn.createStatement();
		st.executeUpdate(query);
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}finally{
			st.close();
			DButility.closeConnection(conn);
		}
		
	}

	/**
	 * @param l
	 */
	protected void deleteAllBowlingrecords(long matchId,int clubId) throws Exception {
		
		String query = "delete from bowling where match_id = " + matchId;
		Statement st = null;
		Connection conn = null;
		try {
		conn = DButility.getConnection(clubId);
		st = conn.createStatement();
		st.executeUpdate(query);
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}finally{
			DButility.closeConnectionAndStatement(conn, st);
		}
		
	}

	/**
	 * @param battingRecords
	 */
	protected void saveBattingRecords(List<BattingDto> battingRecords,int clubId) throws Exception {
		String query = "insert into batting(match_id,player_id,runs_scored,balls_faced,fours,sixers,how_out,wicket_taker1,"
				+ "wicket_taker2,is_out,points,innings, subt_plyr_name,batting_position) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			
			for(int i=0;i<battingRecords.size();i++){
				BattingDto batting = (BattingDto) battingRecords.get(i);
				pst.setInt(1,batting.getMatchID());
				pst.setInt(2,batting.getPlayerID());
				pst.setInt(3,batting.getRunsScored());
				pst.setInt(4,batting.getBallsFaced());
				pst.setInt(5,batting.getFours());
				pst.setInt(6,batting.getSixers());
				pst.setString(7,batting.getHowOut());
				pst.setString(8,batting.getWicketTaker1());
				pst.setString(9,batting.getWicketTaker2());
				pst.setString(10,batting.getIsOut());
				pst.setInt(11, batting.getPoints(clubId));
				pst.setInt(12, batting.getInnings());
				pst.setString(13,batting.getSubtPlyrName());
				pst.setInt(14, batting.getBattingPosition());
				pst.addBatch();
			}
			pst.executeBatch();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
		
	}
	/**
	 * @param battingRecords
	 */
	protected void saveBowlingRecords(List<?> bowlingRecords,int clubId) throws Exception {
		String query = "insert into bowling(match_id,player_id,balls,runs,wides,no_balls,wickets,maidens,points,hatricks,innings,dot_balls) values (?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			
			for(int i=0;i<bowlingRecords.size();i++){
				BowlingDto bowling = (BowlingDto) bowlingRecords.get(i);
				pst.setInt(1,bowling.getMatchID());
				pst.setInt(2,bowling.getPlayerID());
				pst.setInt(3,bowling.getBalls());
				pst.setInt(4,bowling.getRuns());
				pst.setInt(5,bowling.getWides());
				pst.setInt(6,bowling.getNoBalls());
				pst.setInt(7,bowling.getWickets());
				pst.setInt(8,bowling.getMaidens());
				pst.setInt(9,bowling.getPoints(clubId));
				pst.setInt(10,bowling.getHattricks());
				pst.setInt(11,bowling.getInnings());
				pst.setInt(12,bowling.getDotBalls());
				pst.addBatch();
			}
			pst.executeBatch();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			pst.close();
			DButility.closeConnection(conn);
		}
		
	}

	public Map<String, Integer> getPlayerBowlingOutType(long playerId, int clubId) throws Exception {
		Map<String, Integer> outCounts = new HashMap<String, Integer>();
		String query = "select how_out, count(*) out_count from batting where wicket_taker1 = ? and how_out not in ('ro','rt') group by how_out";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setLong(1, playerId);
			rs = st.executeQuery();
			while (rs.next()) {
				outCounts.put(rs.getString("how_out"), rs.getInt("out_count"));
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return outCounts;
	}
	
	public void deleteAllPlayerSumary(int matchId, int clubId) throws Exception {

		String query =
				"delete from player_statistics_summary where match_id = " + matchId;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage()); // TODO Change to throw exception itself
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	public int deleteAllPlayerSumaryFB(int matchId, int clubId) throws Exception {

		String query = "delete from player_statistics_summary_fb where match_id = " + matchId;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		int deleteCount = 0;
		try {
			pst = conn.prepareStatement(query);
			
			deleteCount = pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage()); // TODO Change to throw exception itself
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
		return deleteCount;
	}
	
	public void updateManOfTheMatch(int matchId,int playerId, int clubId) throws Exception {

		String query1 = "update matches set man_of_the_match  = "+playerId +" where match_id = " + matchId;
		String query2 = "update player_statistics_summary set man_of_the_match = 1 where player_id = "+playerId +" and match_id = " + matchId;
		Connection conn = DButility.getConnection(clubId);		
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		try {
			pst1 = conn.prepareStatement(query1);
			pst1.executeUpdate();
			pst2 = conn.prepareStatement(query2);
			pst2.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage()); // TODO Change to throw exception itself
		} finally {
			DButility.closeStatement(pst2);
			DButility.closeConnectionAndStatement(conn, pst1);
			
		}		
	}

	public void saveAllPlayerSumary(List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos, int clubId) throws Exception {

		String query = "INSERT INTO player_statistics_summary(player_id,team_id,series_id,match_id,match_type,match_date,fours,sixers,how_out,wicket_taker1,"
				+ " wicket_taker2,runs_scored,balls_bowled,runs_given,wides,balls_faced,no_balls,dot_balls,wickets,maidens,hatricks,direct_ro,indirect_ro,"
				+ " catches,stumpings,catches_w,bowling_points,batting_points,fielding_points,man_of_the_match,last_updated)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now());";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			for(PlayerStatisticSummaryDto playerStatisticSummaryDto : playerStatisticsSummaryDtos){
				int i = 1;
				
				if("false".equalsIgnoreCase(playerStatisticSummaryDto.getHowOut())){playerStatisticSummaryDto.setHowOut(null);}	
				if(CommonUtility.isNullOrEmptyOrNULL(playerStatisticSummaryDto.getMatchType())){playerStatisticSummaryDto.setMatchType("l");}
				pst.setInt(i++,playerStatisticSummaryDto.getPlayerId());
				pst.setInt(i++,playerStatisticSummaryDto.getTeamId());
				pst.setInt(i++,playerStatisticSummaryDto.getSeriesId());
				pst.setInt(i++,playerStatisticSummaryDto.getMatchId());
				pst.setString(i++,playerStatisticSummaryDto.getMatchType());
				pst.setDate(i++,CommonUtility.utilToSqlDate(playerStatisticSummaryDto.getMatchDate()));
				pst.setInt(i++,playerStatisticSummaryDto.getFours());
				pst.setInt(i++,playerStatisticSummaryDto.getSixers());
				pst.setString(i++,playerStatisticSummaryDto.getHowOut());
				pst.setInt(i++,playerStatisticSummaryDto.getWicketTaker1());
				pst.setInt(i++,playerStatisticSummaryDto.getWicketTaker2());
				pst.setInt(i++,playerStatisticSummaryDto.getRunsScored());
				pst.setInt(i++,playerStatisticSummaryDto.getBallsBowled());
				pst.setInt(i++,playerStatisticSummaryDto.getRunsGiven());
				pst.setInt(i++,playerStatisticSummaryDto.getWides());
				pst.setInt(i++,playerStatisticSummaryDto.getBallsFaced());
				pst.setInt(i++,playerStatisticSummaryDto.getNoBalls());
				pst.setInt(i++, playerStatisticSummaryDto.getDotBalls());
				pst.setInt(i++,playerStatisticSummaryDto.getWickets());
				pst.setInt(i++,playerStatisticSummaryDto.getMaidens());
				pst.setInt(i++,playerStatisticSummaryDto.getHatricks());
				pst.setInt(i++,playerStatisticSummaryDto.getDirectRo());
				pst.setInt(i++,playerStatisticSummaryDto.getIndirectRo());
				pst.setInt(i++,playerStatisticSummaryDto.getCatches());
				pst.setInt(i++,playerStatisticSummaryDto.getStumpings());
				pst.setInt(i++,playerStatisticSummaryDto.getCatchesW());
				pst.setInt(i++,playerStatisticSummaryDto.getBowlingPoints());
				pst.setInt(i++,playerStatisticSummaryDto.getBattingPoints());
				pst.setInt(i++,playerStatisticSummaryDto.getFieldingPoints(clubId));
				pst.setBoolean(i++,playerStatisticSummaryDto.isManOfTheMatch());
				pst.addBatch();
			}
			pst.executeBatch();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	public void saveAllPlayerSumaryFB(List<PlayerStatisticSummaryFBDto> playerStatisticsSummaryDtos, int clubId) throws Exception {

		String query = "INSERT INTO player_statistics_summary_fb(player_id,series_id,team_id,match_id,match_type,match_date,goals_scored,assists,"
				+ "shot_on_target,goals_scored_penalty_kick,goals_scored_free_kick,tackles,interceptions,goals_saved,yellow_cards,red_cards,offside,"
				+ "points, man_of_the_match,last_updated) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now());";
		
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			
			for(PlayerStatisticSummaryFBDto psDto : playerStatisticsSummaryDtos){				
				int i = 1;
				
				if(CommonUtility.isNullOrEmptyOrNULL(psDto.getMatchType())){
					psDto.setMatchType("l");
				}
				
				pst.setInt(i++,psDto.getPlayerId());
				pst.setInt(i++,psDto.getSeriesId());
				pst.setInt(i++,psDto.getTeamId());
				pst.setInt(i++,psDto.getMatchId());
				pst.setString(i++,psDto.getMatchType());
				pst.setDate(i++,CommonUtility.utilToSqlDate(psDto.getMatchDate()));			
				pst.setInt(i++,psDto.getGoalsScored());
				pst.setInt(i++,psDto.getAssists());
				pst.setInt(i++,psDto.getShotsOnTargets());
				pst.setInt(i++,psDto.getGoalsScoredPenalty());
				pst.setInt(i++,psDto.getGoalsScoredFreeKick());
				pst.setInt(i++,psDto.getTackles());
				pst.setInt(i++,psDto.getInterceptions());
				pst.setInt(i++,psDto.getGoalsSaved());
				pst.setInt(i++,psDto.getYellowCards());
				pst.setInt(i++,psDto.getRedCards());
				pst.setInt(i++,psDto.getOffside());				
				pst.setInt(i++,psDto.getTotalPoints());				
				pst.setBoolean(i++,psDto.isManOfTheMatch());
				
				pst.addBatch();
			}
			pst.executeBatch();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	
	
	public List<PlayerStatisticSummaryDto> getAllPlayerStatisticsSummary(int clubId, PlayerStatisticSummaryDto playerStatisticSummaryDto) throws Exception {
		// TODO select Data from summary table.
		String query = "select pss.id, pss.player_id, pss.team_id, pss.series_id, pss.match_id, pss.match_type, pss.match_date, pss.fours, "
				+ " pss.sixers, pss.how_out, pss.wicket_taker1, pss.wicket_taker2, pss.runs_scored, balls_bowled, pss.runs_given, pss.wides, "
				+ " pss.balls_faced, pss.no_balls, pss.wickets, pss.maidens, pss.hatricks, pss.direct_ro, pss.indirect_ro, sum(pss.catches + pss.catches_w) as catches, "
				+ " pss.stumpings, pss.catches_w, bowling_points, pss.batting_points, pss.fielding_points, pss.last_updated, p.f_name, p.l_name, "
				+ " p.profilepic_file_path, t.team_name, t.logo_file_path from player_statistics_summary pss, mcc.player p, team t "
				+ " where pss.player_id = p.player_id and t.team_id = pss.team_id and pss.player_id = ?;"; 	
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerStatisticSummaryDto> playerStatisticSummaryDtos = new ArrayList<PlayerStatisticSummaryDto>();
		PlayerStatisticSummaryDto playerStatisticDto = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, playerStatisticSummaryDto.getPlayerId());
			rs = st.executeQuery();
			while (rs.next()) {
				playerStatisticDto = new PlayerStatisticSummaryDto();
				populatePlayerStatisticSummaryDto(playerStatisticDto, rs);
				playerStatisticSummaryDtos.add(playerStatisticDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerStatisticSummaryDtos;
		
	}
	
	public List<PlayerStatisticSummaryDto> getAllPlayerMatchStatistics(int matchId, int clubId) throws Exception {
		// TODO select Data from summary table.
		String query = "select pss.player_id,p.f_name,p.l_name, pss.match_id, pss.match_type, pss.match_date,pss.runs_scored, "				
				+ " pss.runs_given,pss.wickets, pss.balls_bowled,pss.balls_faced,(pss.catches + pss.catches_w) as catches, "
				+ " pss.bowling_points, pss.batting_points, pss.fielding_points, "
				+ " (pss.bowling_points+pss.batting_points+pss.fielding_points) as total_points, "
				+ " p.profilepic_file_path from player_statistics_summary pss, mcc.player p "
				+ " where pss.match_id = ? and pss.player_id = p.player_id order by total_points desc"; 	
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerStatisticSummaryDto> playerStatisticSummaryDtos = new ArrayList<PlayerStatisticSummaryDto>();
		PlayerStatisticSummaryDto playerStatisticDto = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();
			while (rs.next()) {
				playerStatisticDto = new PlayerStatisticSummaryDto();
				playerStatisticDto.setPlayerId(rs.getInt("player_id"));
				playerStatisticDto.setfName(rs.getString("f_name"));
				playerStatisticDto.setlName(rs.getString("l_name"));
				playerStatisticDto.setProfilePicURL(rs.getString("profilepic_file_path"));
				playerStatisticDto.setMatchId(rs.getInt("match_id"));
				playerStatisticDto.setMatchType(rs.getString("match_type"));
				playerStatisticDto.setMatchDate(rs.getDate("match_date"));
				playerStatisticDto.setRunsScored(rs.getInt("runs_scored"));
				playerStatisticDto.setRunsGiven(rs.getInt("runs_given"));
				playerStatisticDto.setWickets(rs.getInt("wickets"));
				playerStatisticDto.setBallsBowled(rs.getInt("balls_bowled"));
				playerStatisticDto.setBallsFaced(rs.getInt("balls_faced"));
				playerStatisticDto.setCatches(rs.getInt("catches"));
				playerStatisticDto.setBowlingPoints(rs.getInt("bowling_points"));
				playerStatisticDto.setBattingPoints(rs.getInt("batting_points"));
				playerStatisticDto.setFieldingPoints(rs.getInt("fielding_points"));				
				playerStatisticDto.setTotalPoints(rs.getInt("total_points"));
				playerStatisticSummaryDtos.add(playerStatisticDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerStatisticSummaryDtos;		
	}
	
	public List<PlayerStatisticSummaryDto> getPlayerStatsForMOMForTestMatch(int matchId, int clubId) throws Exception {
		
		String query = "SELECT pss.player_id,p.f_name,p.l_name, pss.match_id, pss.match_type, pss.match_date, "
				+ "SUM(pss.runs_scored) runs_scored, SUM(pss.runs_given) runs_given,SUM(pss.wickets) wickets, "
				+ "SUM(pss.balls_bowled) balls_bowled, SUM(pss.balls_faced) balls_faced, "
				+ "(SUM(pss.catches)+SUM(pss.catches_w)) AS catches, SUM(pss.bowling_points) bowling_points, "
				+ "SUM(pss.batting_points) batting_points, SUM(pss.fielding_points) fielding_points, " 
				+ "(SUM(pss.bowling_points)+SUM(pss.batting_points)+SUM(pss.fielding_points)) AS total_points, "
				+ "p.profilepic_file_path "
				+ "FROM player_statistics_summary pss, mcc.player p "
				+ "WHERE pss.match_id = ? AND pss.player_id = p.player_id "
				+ "GROUP BY pss.player_id ORDER BY total_points DESC"; 	
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerStatisticSummaryDto> playerStatisticSummaryDtos = new ArrayList<PlayerStatisticSummaryDto>();
		PlayerStatisticSummaryDto playerStatisticDto = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();
			while (rs.next()) {
				playerStatisticDto = new PlayerStatisticSummaryDto();
				playerStatisticDto.setPlayerId(rs.getInt("player_id"));
				playerStatisticDto.setfName(rs.getString("f_name"));
				playerStatisticDto.setlName(rs.getString("l_name"));
				playerStatisticDto.setProfilePicURL(rs.getString("profilepic_file_path"));
				playerStatisticDto.setMatchId(rs.getInt("match_id"));
				playerStatisticDto.setMatchType(rs.getString("match_type"));
				playerStatisticDto.setMatchDate(rs.getDate("match_date"));
				playerStatisticDto.setRunsScored(rs.getInt("runs_scored"));
				playerStatisticDto.setRunsGiven(rs.getInt("runs_given"));
				playerStatisticDto.setWickets(rs.getInt("wickets"));
				playerStatisticDto.setBallsBowled(rs.getInt("balls_bowled"));
				playerStatisticDto.setBallsFaced(rs.getInt("balls_faced"));
				playerStatisticDto.setCatches(rs.getInt("catches"));
				playerStatisticDto.setBowlingPoints(rs.getInt("bowling_points"));
				playerStatisticDto.setBattingPoints(rs.getInt("batting_points"));
				playerStatisticDto.setFieldingPoints(rs.getInt("fielding_points"));				
				playerStatisticDto.setTotalPoints(rs.getInt("total_points"));
				playerStatisticSummaryDtos.add(playerStatisticDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerStatisticSummaryDtos;		
	}

	private void populatePlayerStatisticSummaryDto(PlayerStatisticSummaryDto playerStatisticDto, ResultSet rs) throws SQLException {
		playerStatisticDto.setId(rs.getInt("id"));
		playerStatisticDto.setPlayerId(rs.getInt("player_id"));
		playerStatisticDto.setfName(rs.getString("f_name"));
		playerStatisticDto.setlName(rs.getString("l_name"));
		playerStatisticDto.setProfilePicURL(rs.getString("profilepic_file_path"));
		playerStatisticDto.setTeamName(rs.getString("team_name"));
		playerStatisticDto.setTeamLogoURL(rs.getString("logo_file_path"));
		playerStatisticDto.setTeamId(rs.getInt("team_id"));
		playerStatisticDto.setSeriesId(rs.getInt("series_id"));
		playerStatisticDto.setMatchId(rs.getInt("match_id"));
		playerStatisticDto.setMatchType(rs.getString("match_type"));
		playerStatisticDto.setMatchDate(rs.getDate("match_date"));
		playerStatisticDto.setFours(rs.getInt("fours"));
		playerStatisticDto.setSixers(rs.getInt("sixers"));
		playerStatisticDto.setHowOut(rs.getString("how_out"));
		playerStatisticDto.setWicketTaker1(rs.getInt("wicket_taker1"));
		playerStatisticDto.setWicketTaker2(rs.getInt("wicket_taker2"));
		playerStatisticDto.setRunsScored(rs.getInt("runs_scored"));
		playerStatisticDto.setBallsBowled(rs.getInt("balls_bowled"));
		playerStatisticDto.setRunsGiven(rs.getInt("runs_given"));
		playerStatisticDto.setWides(rs.getInt("wides"));
		playerStatisticDto.setBallsFaced(rs.getInt("balls_faced"));
		playerStatisticDto.setNoBalls(rs.getInt("no_balls"));
		playerStatisticDto.setWickets(rs.getInt("wickets"));
		playerStatisticDto.setMaidens(rs.getInt("maidens"));
		playerStatisticDto.setHatricks(rs.getInt("hatricks"));
		playerStatisticDto.setDirectRo(rs.getInt("direct_ro"));
		playerStatisticDto.setIndirectRo(rs.getInt("indirect_ro"));
		playerStatisticDto.setCatches(rs.getInt("catches"));
		playerStatisticDto.setStumpings(rs.getInt("stumpings"));
		playerStatisticDto.setCatches(rs.getInt("catches_w"));
		playerStatisticDto.setBowlingPoints(rs.getInt("bowling_points"));
		playerStatisticDto.setBattingPoints(rs.getInt("batting_points"));
		playerStatisticDto.setFieldingPoints(rs.getInt("fielding_points"));
		playerStatisticDto.setLast_updated(rs.getDate("last_updated"));
	}

	public List<PlayerRankingStatisticDto> getAllPlayersRankingsStatistics(String teamId,String playerId, String matchType,String league,int clubId, String internalClubId, int year) throws Exception {
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
				
		int momPoints = 50;
		if(clubId == 501 || clubId == 9262 || clubId == 17117 || clubId == 6265){
			momPoints = 0;
		}else if(clubId == 19694) {
			momPoints = 10;
		}
		// TODO select Data from summary table.
		
		if(!MatchUtility.isValidateMatchType(matchType)) return null;
		
		StringBuffer query = new StringBuffer(
				"SELECT ps.player_id, p.f_name, p.l_name, t.team_name, t.team_code, ps.series_id, ps.team_id, "+
				"IFNULL(t.logo_file_path, '') teamlogo_file_path, IFNULL(p.profilepic_file_path, '') profilepic_file_path,  "+
				"SUM(batting_points) AS batting_points, SUM(bowling_points) AS bowling_points,  "+
				"SUM(ps.fielding_points) AS fielding_points, "+
				"count(distinct ps.match_id) as matches, SUM(man_of_the_match) as mom, SUM(man_of_the_match * "+momPoints+") as others,  "+
				"SUM(bowling_points + batting_points + fielding_points + (man_of_the_match * "+momPoints+")) as total "+
				"FROM mcc.player p, player_statistics_summary ps, team t, league l  "+
				"WHERE ps.player_id = p.player_id AND ps.team_id = t.team_id AND ps.series_id = l.league_id  ");
		
		String groupByCondition = " ";
		
		// match type condition -- TODO for now its default.
		//query.append("AND ps.match_type IN ('l' , 'q', 's', 'e', 'ql', 'ql', 'sl', 'f')");
		
		//if(teamId >0) query.append(" and ps.team_id = ? ");
		if(teamId != null && !"".equals(teamId.trim()) && teamId.contains(",")){
			query.append(" and ps.team_id in (" + teamId + ") " );
		}
		else if(CommonUtility.stringToInt(teamId) > 0){
			query.append(" and ps.team_id = '" + teamId + "' ") ;
		}
		if(CommonUtility.stringToInt(internalClubId) > 0){
			query.append(" AND t.club_id = " + internalClubId );			
		}		
		if(league != null && !"".equals(league.trim()) && CommonUtility.isOnlyDigitInString(league)){
			query.append(" and ps.series_id in (" + league + ") " );
			//groupByCondition = ",ps.team_id";
		}		
		query.append(" and ps.match_type in ( " + (CommonUtility.isNullOrEmpty(matchType) ? MatchUtility.MATCH_TYPE_ALL: !CommonUtility.isNullOrEmpty(matchType) && matchType.equals("po")? MatchUtility.MATCH_TYPE_PO : "'" +matchType + "'") + ")");
		if(year>0 && CommonUtility.isNullOrEmptyOrNULL(league)) {
			query.append(" and YEAR(ps.match_date)="+year);
		}
		query.append(" GROUP BY ps.player_id " + groupByCondition + " , p.l_name , p.f_name ORDER BY total DESC ");
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerRankingStatisticDto> playerRankingStatisticDtos = new ArrayList<PlayerRankingStatisticDto>();
		PlayerRankingStatisticDto playerRankingStatisticDto = null;
		try {
			st = conn.prepareStatement(query.toString());
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				// TODO popuplate playerRankingSummary DTO
				playerRankingStatisticDto =  new PlayerRankingStatisticDto();
				playerRankingStatisticDto.setPlayerID(rs.getInt("player_id"));
				playerRankingStatisticDto.setFirstName(rs.getString("f_name"));
				playerRankingStatisticDto.setLastName(rs.getString("l_name"));
				playerRankingStatisticDto.setTeamId(rs.getInt("team_id"));
				playerRankingStatisticDto.setTeamName(rs.getString("team_name"));
				playerRankingStatisticDto.setTeamCode(rs.getString("team_code"));
				playerRankingStatisticDto.setLeagueId(rs.getInt("series_id"));
				playerRankingStatisticDto.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
				playerRankingStatisticDto.setProfilepic_file_path(rs.getString("profilepic_file_path"));
				playerRankingStatisticDto.setBattingPoints(rs.getInt("batting_points"));
				playerRankingStatisticDto.setBowlingPoints(rs.getInt("bowling_points"));
				playerRankingStatisticDto.setFieldingPoints(rs.getInt("fielding_points"));
				playerRankingStatisticDto.setMatchesPlayed(rs.getInt("matches"));
				playerRankingStatisticDto.setMom(rs.getInt("mom"));
				playerRankingStatisticDto.setOtherPoints(rs.getInt("others"));
				playerRankingStatisticDto.setTotal(rs.getInt("total"));
				
				playerRankingStatisticDtos.add(playerRankingStatisticDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		
		return playerRankingStatisticDtos;
		
	}

	public void deleteAllPlayerSumary(int clubId) throws Exception {


		String query = "delete from player_statistics_summary ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage()); // TODO Change to throw exception itself
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	public void deletePlayerSumaryForMatch(int clubId, int matchId) throws Exception {

		String query = "delete from player_statistics_summary where match_id = "+matchId;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage()); // TODO Change to throw exception itself
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected List<PlayerBattingDto> getAssociationPlayersBattingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		List<PlayerBattingDto> players = new ArrayList<PlayerBattingDto>();
		
		String query ="SELECT " + 
				"    player_id," + 
				"    f_name," + 
				"    l_name," + 
				"	profilepic_file_path," + 
				"    SUM(runs) runs," + 
				"    SUM(balls) balls," + 
				"    club_id," + 
				"	league_name from (" ;
		for(int i=0;i<clubs.size();i++) {
			query+=	"( select ps.player_id,  p.f_name,  p.l_name, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+
				"sum(ps.runs_scored) as runs, sum(ps.balls_faced) as balls, '"+clubs.get(i).getName()+"' league_name,"+clubs.get(i).getClubId()+" club_id"+
				" from club"+clubs.get(i).getClubId()+".player_statistics_summary ps, mcc.player p ";
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
				query += ", club"+clubs.get(i).getClubId()+".league l ";
			} 
			query += " where p.player_id = ps.player_id  ";
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
				query += " and ps.series_id = l.league_id ";
			}
			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and l.series_type = ? ";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = ? ";
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query += " and ps.series_id in ("+clubSeriesMap.get(clubs.get(i).getClubId())+") ";
			}
			
				if(CommonUtility.isNullOrEmpty(fromDate) && CommonUtility.isNullOrEmpty(toDate)) {
					query += " and ps.match_date >= ( CURDATE() - INTERVAL 365 DAY ) ";
				}else {
					if (!CommonUtility.isNullOrEmpty(fromDate)) {
						query += " and ps.match_date >= STR_TO_DATE(?,  '%m/%d/%Y') ";
					}
					if (!CommonUtility.isNullOrEmpty(toDate)) {
						query += " and ps.match_date <= STR_TO_DATE(?,  '%m/%d/%Y') ";
					}
				}				
			query += " group by ps.player_id order by runs desc, balls LIMIT " + limit + (i!=clubs.size()-1 ?") UNION ":") ");
		}
		query+= ") a GROUP BY player_id " + 
				"ORDER BY runs DESC , balls " + 
				"LIMIT " + limit;
			
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.prepareStatement(query);
		int index = 1;
		
		if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (!CommonUtility.isNullOrEmpty(seriesType)) {
					st.setString(index++, seriesType);
				}
				if (!CommonUtility.isNullOrEmpty(category)) {
					st.setString(index++, category);
				}				
			}
		}
		
		if(!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (fromDate != null && !"".equals(fromDate.trim())) {
					st.setString(index++, fromDate);
				}
				if (toDate != null && !"".equals(toDate.trim())) {
					st.setString(index++, toDate);
				}
			}
		}
		rs = st.executeQuery();
		
		while (rs.next()) {
			PlayerBattingDto player = new PlayerBattingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setRunsScored(rs.getInt("runs"));
			player.setBallsFaced(rs.getInt("balls"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setLeagueName(rs.getString("league_name"));
			player.setClubId(rs.getInt("club_id"));
			players.add(player);
		}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerBowlingDto> getAssociationPlayersBowlingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		List<PlayerBowlingDto> players = new ArrayList<PlayerBowlingDto>();
		
		String query ="SELECT " + 
				"    player_id," + 
				"    f_name," + 
				"    l_name," + 
				"	profilepic_file_path," + 
				"    SUM(wickets) wickets," + 
				"    club_id," + 
				"	league_name from (" ;
		for(int i=0;i<clubs.size();i++) {
			query+=	"( select ps.player_id,  p.f_name,  p.l_name, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+
				"SUM(ps.wickets) wickets, '"+clubs.get(i).getName()+"' league_name,"+clubs.get(i).getClubId()+" club_id"+
				" from club"+clubs.get(i).getClubId()+".player_statistics_summary ps, mcc.player p ";
			
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
				query += ", club"+clubs.get(i).getClubId()+".league l ";
			} 
			query += " where p.player_id = ps.player_id  ";
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
				query += " and ps.series_id = l.league_id ";
			}
			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and l.series_type = ? ";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = ? ";
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query += " and ps.series_id in ("+clubSeriesMap.get(clubs.get(i).getClubId())+") ";
			}
			
			if(CommonUtility.isNullOrEmpty(fromDate) && CommonUtility.isNullOrEmpty(toDate)) {
				query += " and ps.match_date >= ( CURDATE() - INTERVAL 365 DAY ) ";
			}else {
				if (!CommonUtility.isNullOrEmpty(fromDate)) {
					query += " and ps.match_date >= STR_TO_DATE(?,  '%m/%d/%Y') ";
				}
				if (!CommonUtility.isNullOrEmpty(toDate)) {
					query += " and ps.match_date <= STR_TO_DATE(?,  '%m/%d/%Y') ";
				}
			}	
			query += " group by ps.player_id order by wickets desc LIMIT " + limit + (i!=clubs.size()-1 ?") UNION ":") ");
		}
		query+= ") a GROUP BY player_id " + 
				"ORDER BY wickets DESC " + 
				"LIMIT " + limit;
			
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.prepareStatement(query);
		int index = 1;
		
		if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (!CommonUtility.isNullOrEmpty(seriesType)) {
					st.setString(index++, seriesType);
				}
				if (!CommonUtility.isNullOrEmpty(category)) {
					st.setString(index++, category);
				}
			}
		}
		
		if(!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (fromDate != null && !"".equals(fromDate.trim())) {
					st.setString(index++, fromDate);
				}
				if (toDate != null && !"".equals(toDate.trim())) {
					st.setString(index++, toDate);
				}
			}
		}
		rs = st.executeQuery();
		while (rs.next()) {
			PlayerBowlingDto player = new PlayerBowlingDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setWickets(rs.getInt("wickets"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setLeagueName(rs.getString("league_name"));
			player.setClubId(rs.getInt("club_id"));
			players.add(player);
		}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}
	
	protected List<PlayerRankingStatisticDto> getAssociationPlayersRankingStatistics(List<ClubDtoLite> clubs, String fromDate, String toDate, 
			String seriesType, String category, Map<Integer,String> clubSeriesMap, int limit) throws Exception{
		
		List<PlayerRankingStatisticDto> players = new ArrayList<PlayerRankingStatisticDto>();
		
		String query ="SELECT " + 
				"    player_id," + 
				"    f_name," + 
				"    l_name," + 
				"	profilepic_file_path," + 
				"    SUM(total) total," + 
				"    club_id," + 
				"	league_name from (" ;
		for(int i=0;i<clubs.size();i++) {
			query+=	"( select ps.player_id,  p.f_name,  p.l_name, IFNULL(p.profilepic_file_path, '') profilepic_file_path, "+
				"SUM(bowling_points + batting_points + fielding_points + (man_of_the_match * 50)) as total, '"+clubs.get(i).getName()+"' league_name, "+clubs.get(i).getClubId()+" club_id"+
				" from club"+clubs.get(i).getClubId()+".player_statistics_summary ps, mcc.player p ";
			
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
				query += ", club"+clubs.get(i).getClubId()+".league l ";
			} 
			query += " where p.player_id = ps.player_id  ";
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) ) {
				query += " and ps.series_id = l.league_id ";
			}
			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and l.series_type = ? ";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = ? ";
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query += " and ps.series_id in ("+clubSeriesMap.get(clubs.get(i).getClubId())+") ";
			}
			
			if(CommonUtility.isNullOrEmpty(fromDate) && CommonUtility.isNullOrEmpty(toDate)) {
				query += " and ps.match_date >= ( CURDATE() - INTERVAL 365 DAY ) ";
			}else {
				if (!CommonUtility.isNullOrEmpty(fromDate)) {
					query += " and ps.match_date >= STR_TO_DATE(?,  '%m/%d/%Y') ";
				}
				if (!CommonUtility.isNullOrEmpty(toDate)) {
					query += " and ps.match_date <= STR_TO_DATE(?,  '%m/%d/%Y') ";
				}
			}	
			query += " group by ps.player_id order by total desc LIMIT " + limit + (i!=clubs.size()-1 ?") UNION ":") ");
		}
		query+= ") a GROUP BY player_id " + 
				"ORDER BY total DESC " + 
				"LIMIT " + limit;
			
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.prepareStatement(query);
		int index = 1;
		
		if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (!CommonUtility.isNullOrEmpty(seriesType)) {
					st.setString(index++, seriesType);
				}
				if (!CommonUtility.isNullOrEmpty(category)) {
					st.setString(index++, category);
				}
			}
		}
		
		if(!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
			
			for(int i=0;i<clubs.size();i++) {
				
				if (fromDate != null && !"".equals(fromDate.trim())) {
					st.setString(index++, fromDate);
				}
				if (toDate != null && !"".equals(toDate.trim())) {
					st.setString(index++, toDate);
				}
			}
		}
		rs = st.executeQuery();
		while (rs.next()) {
			PlayerRankingStatisticDto player = new PlayerRankingStatisticDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setPoints(rs.getInt("total"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setLeagueName(rs.getString("league_name"));
			player.setClubId(rs.getInt("club_id"));
			players.add(player);
		}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return players;
	}

	protected Map<String, Integer> getAssociationMetrics(List<ClubDtoLite> clubs, String seriesType, String category, 
			Map<Integer,String> clubSeriesMap,String fromDate, String toDate) throws Exception {
		
		Map<String, Integer> metrics = new HashMap<String, Integer>();
		
		String query ="SELECT " + 
				"    sum(league) leagues," + 
				"    sum(grounds) grounds," + 
				"    sum(teams) teams," + 
				"    sum(players) players," + 
				"    sum(matches) matches," + 
				"    sum(umpires) umpires from (" ;
		for(int i=0;i<clubs.size();i++) {
			int clubId = clubs.get(i).getClubId();
			query+=	" (SELECT " + 
					"    (SELECT " + 
					"            COUNT(*)  FROM" + 
					"            club"+clubId+".league l where 1=1 ";
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			query+=	" ) league," + 
					"    (SELECT " + 
					"            COUNT(distinct(g.ground_id))" + 
					"        FROM" + 
					"            club"+clubId+".grounds g";
			
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)) {
				query+=	" , club"+clubId+".fixtures f, club"+clubId+".league l where g.ground_id = f.ground_id and f.league_id = l.league_id "; 
			}			
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			query+= " ) grounds," + 
					"    (SELECT " + 
					"            COUNT(DISTINCT team_name)" + 
					"        FROM" + 
					"            club"+clubId+".team ";
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)) {
				query+=	" t, club"+clubId+".league l where t.league = l.league_id "; 
			}			
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			
			query+=	" ) teams," + 
					"    (SELECT " + 
					"            COUNT(distinct(pc.player_id))" + 
					"        FROM" + 
					"            player_club pc ";
					if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)) {
						query+=	", club"+clubId+".team_player tp, club"+clubId+".team t, club"+clubId+".league l "
								+ " where pc.club_id = "+clubId + " and pc.player_id = tp.player_id "
								+ " and tp.team_id = t.team_id and t.league = l.league_id "; 
					}else {
						query+=	" WHERE pc.club_id = "+clubId;
					}
			
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			query+=	" ) players," + 
					"    (SELECT " + 
					"            COUNT(*)" + 
					"        FROM" + 
					"            club"+clubId+".matches ";
			
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)
					) {
				query+=	" m, club"+clubId+".fixtures f, club"+clubId+".league l where m.match_id = f.match_id and f.league_id = l.league_id ";
			} else if(!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
				query +=" where 1=1 ";
			}
			
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			if (!CommonUtility.isNullOrEmpty(fromDate)) {
				query += " and match_date >= STR_TO_DATE('"+fromDate+"',  '%m/%d/%Y')";
			}
			if (!CommonUtility.isNullOrEmpty(toDate)) {
				query += " and match_date <= STR_TO_DATE('"+toDate+"',  '%m/%d/%Y')";
			}
			
			query+= " ) matches," + 
					"    (SELECT COUNT(distinct(uc.umpire_id)) FROM  mcc.umpire_view uc ";
					
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)) {
				query+=	" , club"+clubId+".fixtures f, club"+clubId+".league l "; 
			}
			query+= " where uc.club_id = "+clubId;
			
			if(!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category) || (clubSeriesMap != null && clubSeriesMap.size()>0)) {
				query+=	" and (uc.user_id = f.umpire1_id or uc.user_id = f.umpire2_id ) and f.league_id = l.league_id "; 
			}
			
			if(!CommonUtility.isNullOrEmpty(seriesType)) {
				query+=	" and l.series_type = '"+ seriesType +"'"; 
			}
			if(!CommonUtility.isNullOrEmpty(category)) {
				query+=	" and l.category = '"+ category +"'"; 
			}
			if (clubSeriesMap != null && clubSeriesMap.size()>0) {
				query+=	" and l.league_id in ( "+clubSeriesMap.get(clubId)+")" ;
			}
			query+= " ) umpires "+ (i!=clubs.size()-1 ?")  UNION ":") ");
		}
		query+= " ) a ";
			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.createStatement();
		
		rs = st.executeQuery(query);
		
		while (rs.next()) {
			metrics.put("leagues", rs.getInt("leagues"));
			metrics.put("grounds", rs.getInt("grounds"));
			metrics.put("teams", rs.getInt("teams"));
			metrics.put("players", rs.getInt("players"));
			metrics.put("matches", rs.getInt("matches"));
			metrics.put("umpires", rs.getInt("umpires"));
		}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return metrics;
	}
	protected Map<String, Integer> getAssociationMetrics2(List<ClubDtoLite> clubs, String seriesType, String category, 
			Map<Integer,String> clubSeriesMap ,String fromDate, String toDate) throws Exception {
		
		Map<String, Integer> metrics = new HashMap<String, Integer>();
		
		String query ="SELECT " + 
				"    sum(runs) runs," + 
				"    sum(fours) fours,"
				+ "  sum(wickets) wickets,"
				+ "  sum(sixers) sixers from (" ;
		for (int i = 0; i < clubs.size(); i++) {
			int clubId = clubs.get(i).getClubId();
			query += " ( SELECT ( SELECT SUM(fours) fours FROM   club" + clubId + ".player_statistics_summary pss   ";

			if (!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)
					|| (clubSeriesMap != null && clubSeriesMap.size() > 0)) {
				query += " , club" + clubId + ".league l  where pss.series_id = l.league_id ";
			} else if (!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
				query += " where 1=1 ";
			}

			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and  l.series_type = '" + seriesType + "'";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = '" + category + "'";
			}
			if (clubSeriesMap != null && clubSeriesMap.size() > 0) {
				query += " and l.league_id in ( " + clubSeriesMap.get(clubId) + ")";
			}

			if (!CommonUtility.isNullOrEmpty(fromDate)) {
				query += " and pss.match_date >= STR_TO_DATE('" + fromDate + "',  '%m/%d/%Y') ";
			}
			if (!CommonUtility.isNullOrEmpty(toDate)) {
				query += " and pss.match_date <= STR_TO_DATE('" + toDate + "',  '%m/%d/%Y') ";
			}
			query += ") fours, (select sum(t1_total+t2_total) runs from club" + clubId + ".matches ";

			if (!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)
					|| (clubSeriesMap != null && clubSeriesMap.size() > 0)) {
				query += " m, club" + clubId + ".fixtures f, club" + clubId
						+ ".league l where m.match_id = f.match_id and f.league_id = l.league_id ";
			} else if (!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
				query += " where 1=1 ";
			}

			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and l.series_type = '" + seriesType + "'";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = '" + category + "'";
			}
			if (clubSeriesMap != null && clubSeriesMap.size() > 0) {
				query += " and l.league_id in ( " + clubSeriesMap.get(clubId) + ")";
			}
			if (!CommonUtility.isNullOrEmpty(fromDate)) {
				query += " and match_date >= STR_TO_DATE('" + fromDate + "',  '%m/%d/%Y')";
			}
			if (!CommonUtility.isNullOrEmpty(toDate)) {
				query += " and match_date <= STR_TO_DATE('" + toDate + "',  '%m/%d/%Y')";
			}

			query += " ) runs, (select sum(t1_wickets+t2_wickets) wickets from club" + clubId + ".matches  ";

			if (!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)
					|| (clubSeriesMap != null && clubSeriesMap.size() > 0)) {
				query += " m, club" + clubId + ".fixtures f, club" + clubId
						+ ".league l where m.match_id = f.match_id and f.league_id = l.league_id ";
			} else if (!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
				query += " where 1=1 ";
			}

			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and l.series_type = '" + seriesType + "'";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = '" + category + "'";
			}
			if (clubSeriesMap != null && clubSeriesMap.size() > 0) {
				query += " and l.league_id in ( " + clubSeriesMap.get(clubId) + ")";
			}
			if (!CommonUtility.isNullOrEmpty(fromDate)) {
				query += " and match_date >= STR_TO_DATE('" + fromDate + "',  '%m/%d/%Y')";
			}
			if (!CommonUtility.isNullOrEmpty(toDate)) {
				query += " and match_date <= STR_TO_DATE('" + toDate + "',  '%m/%d/%Y')";
			}
			query += " ) wickets, ( SELECT SUM(sixers) sixers FROM   club" + clubId
					+ ".player_statistics_summary pss   ";

			if (!CommonUtility.isNullOrEmpty(seriesType) || !CommonUtility.isNullOrEmpty(category)
					|| (clubSeriesMap != null && clubSeriesMap.size() > 0)) {
				query += " , club" + clubId + ".league l  where pss.series_id = l.league_id ";
			} else if (!CommonUtility.isNullOrEmpty(fromDate) || !CommonUtility.isNullOrEmpty(toDate)) {
				query += " where 1=1 ";
			}

			if (!CommonUtility.isNullOrEmpty(seriesType)) {
				query += " and  l.series_type = '" + seriesType + "'";
			}
			if (!CommonUtility.isNullOrEmpty(category)) {
				query += " and l.category = '" + category + "'";
			}
			if (clubSeriesMap != null && clubSeriesMap.size() > 0) {
				query += " and l.league_id in ( " + clubSeriesMap.get(clubId) + ")";
			}

			if (!CommonUtility.isNullOrEmpty(fromDate)) {
				query += " and pss.match_date >= STR_TO_DATE('" + fromDate + "',  '%m/%d/%Y') ";
			}
			if (!CommonUtility.isNullOrEmpty(toDate)) {
				query += " and pss.match_date <= STR_TO_DATE('" + toDate + "',  '%m/%d/%Y') ";
			}
			query += ") sixers " + (i != clubs.size() - 1 ? ")  UNION " : ") ");
		}
		query+= " ) a ";
		
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.createStatement();
		
		rs = st.executeQuery(query);
		
		while (rs.next()) {
			metrics.put("runs", rs.getInt("runs"));
			metrics.put("fours", rs.getInt("fours"));
			metrics.put("sixers", rs.getInt("sixers"));
			metrics.put("wickets", rs.getInt("wickets"));
		}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return metrics;
	}
	
	private PlayerMatchRecordsDtoFB populatePlayerFootBallMatchRecordDto(ResultSet rs) throws SQLException {
		
		PlayerMatchRecordsDtoFB player = new PlayerMatchRecordsDtoFB();
		
		player.setPlayerId(rs.getInt("player_id"));
		player.setFirstName(rs.getString("f_name"));
		player.setLastName(rs.getString("l_name"));
		player.setTeamId(rs.getInt("team_id"));
		player.setTeamName(rs.getString("team_name"));
		player.setAgainstId(rs.getInt("against_id"));
		player.setAgainstName(rs.getString("against_name"));
		player.setWinnerId(rs.getInt("winner_id"));
		player.setWinnerName(rs.getString("winner_name"));
		player.setMatchId(rs.getInt("match_id"));
		player.setMatchDate(rs.getString("match_date"));
		player.setGoalsScored(rs.getInt("goals_scored"));
		player.setAssists(rs.getInt("assists"));
		player.setShotsOnTargets(rs.getInt("shot_on_targets")); 
		player.setGoalsScoredPenalty(rs.getInt("goals_scored_penalty"));
		player.setGoalsScoredFreeKick(rs.getInt("goals_scored_free_kick"));
		player.setTackles(rs.getInt("tackles"));
		player.setInterceptions(rs.getInt("interceptions"));
		player.setGoalsSaved(rs.getInt("goals_saved"));
		player.setYellowCards(rs.getInt("yellow_cards"));
		player.setRedCards(rs.getInt("red_cards"));	
		player.setOffside(rs.getInt("offside"));
		
		return player;
	}
	
	private PlayerStatisticsFBDto populatePlayerFootBallStatDto(ResultSet rs) throws SQLException {
		
		PlayerStatisticsFBDto player = new PlayerStatisticsFBDto();

		player.setPlayerId(rs.getInt("player_id"));
		player.setFirstName(rs.getString("f_name"));
		player.setLastName(rs.getString("l_name"));
		player.setGoalsScored(rs.getInt("goals_scored"));
		player.setAssists(rs.getInt("assists"));
		player.setShotsOnTargets(rs.getInt("shot_on_targets"));
		player.setGoalsScoredPenalty(rs.getInt("goals_scored_penalty"));
		player.setGoalsScoredFreeKick(rs.getInt("goals_scored_free_kick"));
		player.setTackles(rs.getInt("tackles"));
		player.setInterceptions(rs.getInt("interceptions"));
		player.setGoalsSaved(rs.getInt("goals_saved"));
		player.setYellowCards(rs.getInt("yellow_cards"));
		player.setRedCards(rs.getInt("red_cards"));
		player.setOffside(rs.getInt("offside"));
		player.setSeriesId(rs.getInt("league_id"));
		player.setSeriesName(rs.getString("league_name"));
		player.setMatches(rs.getInt("matches"));
		player.setTeamId(rs.getInt("team_id"));
		player.setTeamCode(rs.getString("team_code"));
		player.setTeamName(rs.getString("team_name"));
		player.setTeamlogo_file_path(rs.getString("logo_file_path"));
		player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
		player.setPlayingRole(rs.getString("playing_role"));
		player.setMatchyear(rs.getString("match_year"));
		
		return player;
	}
}
