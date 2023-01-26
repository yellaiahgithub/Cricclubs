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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.ClubDto;
import com.cricket.dto.LiveScoreActionsDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.Pair;
import com.cricket.dto.PlayerAllIncidentStatsFBDto;
import com.cricket.dto.insights.BowlingOverComparisonDTO;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.football.dto.PlayerMatchDtoFB;
import com.google.common.collect.Lists;

/**
 * @author ganesh
 * 
 */
public class MatchDAO {
	
	static Logger log = LoggerFactory.getLogger(MatchDAO.class);
	
	protected static String MATCH_QUERY = "select				\n" + "match_id,			\n" + "l.series_type, \n" + "l.league_name,"
			+ "team_one,			\n" + "team_two,			\n" + "toss_won,			\n"
			+ "batting_first,			\n" + "overs,				\n" + " m.winner,				\n"
			+ "MAN_OF_THE_MATCH,				\n" + "location,			\n" + "match_type,			\n"
			+ "DATE_FORMAT(MATCH_DATE,  '%m/%d/%Y') MATCH_DATE,			\n"
			+ "team_one_captain,		\n" + "team_two_captain,		\n"
			+ "team_one_vice_captain,		\n" + "team_two_vice_captain,		\n"
			+ "t1_total,		\n" + "t2_total,		\n" + "t1_balls,		\n"
			+ "t2_balls,		\n" + "t1_wickets,		\n" + "t2_wickets,		\n"
			+ "t1_1total,		\n" + "t2_1total,		\n" + "t1_1balls,		\n"
			+ "t2_1balls,		\n" + "t1_1wickets,		\n" + "t2_1wickets,		\n"
			+ "t1_2total,		\n" + "t2_2total,		\n" + "t1_2balls,		\n"
			+ "t2_2balls,		\n" + "t1_2wickets,		\n" + "t2_2wickets,		\n"
			+ "t1_byes, t1_penalty, t2_penalty,		\n" + "t2_byes,		\n" + "t1_lbyes,		\n"
			+ "t2_lbyes,		\n" 
			+ "t1_1byes, t1_1penalty, t2_1penalty,		\n" + "t2_1byes,		\n" + "t1_1lbyes,		\n"
			+ "t2_1lbyes,		\n" 
			+ "t1_2byes, t1_2penalty, t2_2penalty,		\n" + "t2_2byes,		\n" + "t1_2lbyes,		\n"
			+ "t2_2lbyes, "
			+ "m.is_dls, m.t2_target, m.r1_res_available, m.t2_revised_over, m.status, "
			+ "t1.team_name team_one_name,	\n"
			+ "t2.team_name team_two_name,	\n"
			+ "t1.team_code team_one_code,	\n"
			+ "t2.team_code team_two_code,	\n" 
			+ "IFNULL(t1.logo_file_path,'') t1_logo_file_path, \n"
			+ "IFNULL(t2.logo_file_path,'') t2_logo_file_path, \n" 
			+ "m.is_complete,	\n"
			+ "m.is_abandoned,	\n"
			+ "m.is_locked,	 m.is_followon,		\n"
			+ "m.comment,m.umpire1,m.umpire2,	\n"
			+ "m.notification_sent,	\n"
			+ "m.scorer,m.fow1,m.fow2,m.fow1_2,m.fow2_2,	m.abandone_type,m.last_updated_date,TimestampDiff(SECOND, m.last_updated_date,NOW()) time_diff, m.last_updated_by, m.live_youtube_link,\n"
			+ "t1.league, l.maximum_players	,  m.is_trump \n" + " from				\n" + "matches m,		\n" + "team t1,		\n"
			+ "team t2, league l		\n" + "WHERE		\n" + "t1.team_id = m.team_one		\n"
			+ "AND t2.team_id = m.team_two AND t1.league = l.league_id		\n";

	protected static final String MATCH_PLAYER_TEAM_QUERY = "SELECT						\n"
			+ "MATCH_ID,					\n" + "PLAYER_ID,				\n" + "TEAM_ID					\n"
			+ "FROM						\n" + "match_player_team mpt		\n" + "WHERE						\n"
			+ "mpt.MATCH_ID = 	\n";
	protected static final String MATCH_PLAYER_TEAM_QUERY_END = "\n order by mpt.batting_position";

	protected int insertMatch(MatchDto matchDto, int clubId, Map<Integer,Integer> PlayersBattingPosition) throws Exception {

		String query = "";
		Statement st = null;
		Statement st1 = null;
		Connection conn = null;
		ResultSet rs = null;
		int matchId = 0;
		try {
			query = "INSERT INTO matches(TEAM_ONE, TEAM_ONE_CAPTAIN, TEAM_ONE_VICE_CAPTAIN, TEAM_TWO, TEAM_TWO_CAPTAIN, TEAM_TWO_VICE_CAPTAIN, "
					+ "TOSS_WON, BATTING_FIRST, OVERS, WINNER, LOCATION, match_type, MATCH_DATE,MAN_OF_THE_MATCH,"
					+ "is_abandoned,is_followon,comment,scorer,abandone_type,last_updated_date,last_updated_by, live_youtube_link, "
					+ "is_trump, is_dls, t2_target, t2_revised_over, status) VALUES ("
					+ matchDto.getTeamOne()
					+ ","
					+ matchDto.getTeamOneCaptain()
					+ ","
					+ matchDto.getTeamOneViceCaptain()
					+ ","
					+ matchDto.getTeamTwo()
					+ ","
					+ matchDto.getTeamTwoCaptain()
					+ ","
					+ matchDto.getTeamTwoViceCaptain()
					+ ","
					+ matchDto.getTossWon()
					+ ","
					+ matchDto.getBattingFirst()
					+ ","
					+ matchDto.getOvers()
					+ ","
					+ matchDto.getWinner()
					+ ","
					+ ((matchDto.getLocation() == null) ? "null" : ("'"
							+ matchDto.getLocation() + "'"))
					+ ","
					+ ((matchDto.getMatchType() == null) ? "null" : ("'"
							+ matchDto.getMatchType() + "'"))
					+ ", STR_TO_DATE('"
					+ matchDto.getMatchDate()
					+ "','%m/%d/%Y')"
					+ ", "
					+ matchDto.getManOfTheMatch()
					+ ", "
					+ matchDto.getIsAbandoned()
					+ ", "
					+ matchDto.getIsFollowon()
					+ ", '"
					+ DButility.escapeLine(matchDto.getComment()) + "'," + matchDto.getScorer() 
					+ ",'" 
					+ matchDto.getAbandoneType() + "',NOW(),"+matchDto.getLastUpdatedBy()
					+",'"
					+(matchDto.getLive_streaming_link()==null?"":matchDto.getLive_streaming_link())
					+ "'," 
					+ matchDto.getIsTrump() 
					+ ","
					+ matchDto.isDls()
					+ ","
					+ matchDto.getT2Target()
					+ ","
					+ CommonUtility.oversToBalls(matchDto.getT2RevisedOvers()+"")
					+ ",'"
					+ matchDto.getStatus()
					+"')";
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.execute(query, Statement.RETURN_GENERATED_KEYS);
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				matchId = rs.getInt(1);
			}

			if (matchId != 0) {
				query = null;
				st1 = conn.createStatement();
				if (matchDto.getPlayers1() != null && !matchDto.getPlayers1().isEmpty()) {
					List<Integer> playersTeamOne = matchDto.getPlayers1();
					int t1PlayerBattingPosition=99;
					for (int i = 0; i < playersTeamOne.size(); i++) {
						if (!CommonUtility.isMapNullEmpty(PlayersBattingPosition)) {
							if (PlayersBattingPosition.containsKey(playersTeamOne.get(i))) {
								t1PlayerBattingPosition = PlayersBattingPosition.get(playersTeamOne.get(i));
							}else {
								t1PlayerBattingPosition=99;
							}
						}
						query = "INSERT INTO match_player_team(MATCH_ID, PLAYER_ID, TEAM_ID,batting_position) VALUES ("
								+ matchId
								+ ","
								+ (new Long(playersTeamOne.get(i))).longValue()
								+ ","
								+ matchDto.getTeamOne() +"," + t1PlayerBattingPosition + ")";
						st1.addBatch(query);
						query = null;
	
					}
				}
				if (matchDto.getPlayers2() != null && !matchDto.getPlayers2().isEmpty()) {
					List<Integer> playersTeamTwo = matchDto.getPlayers2();
					int t2PlayerBattingPosition=99;
					for (int i = 0; i < playersTeamTwo.size(); i++) {
						if (!CommonUtility.isMapNullEmpty(PlayersBattingPosition)) {
							if (PlayersBattingPosition.containsKey(playersTeamTwo.get(i))) {
								t2PlayerBattingPosition = PlayersBattingPosition.get(playersTeamTwo.get(i));
							}else {
								t2PlayerBattingPosition=99;
							}
						}
						query = "INSERT INTO match_player_team(MATCH_ID, PLAYER_ID, TEAM_ID,batting_position) VALUES ("
								+ matchId
								+ ","
								+ (new Long(playersTeamTwo.get(i))).longValue()
								+ ","
								+ matchDto.getTeamTwo() + "," + t2PlayerBattingPosition + ")";
						st1.addBatch(query);
						query = null;
					}
				}
				st1.executeBatch();
			}

		} catch (Exception e) {
			log.error(e.getMessage() + query, e);
			throw new Exception(e.getMessage());
		} finally {
			
			DButility.closeStatement(st1);
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchId;
	}
	
	protected void restoreMatch(MatchDto matchDto, int clubId) throws Exception {

		String query = "";
		Statement st = null;
		Statement st1 = null;
		Connection conn = null;
		ResultSet rs = null;
		int matchId = matchDto.getMatchID();
		try {
			query = "INSERT INTO matches(match_id, TEAM_ONE, TEAM_ONE_CAPTAIN, TEAM_ONE_VICE_CAPTAIN, TEAM_TWO, TEAM_TWO_CAPTAIN, TEAM_TWO_VICE_CAPTAIN, "
					+ "TOSS_WON, BATTING_FIRST, OVERS, WINNER, LOCATION, match_type, MATCH_DATE,MAN_OF_THE_MATCH,"
					+ "is_abandoned,is_followon,comment,scorer,abandone_type,last_updated_date,last_updated_by, live_youtube_link, "
					+ "is_trump, is_dls, t2_target, t2_revised_over, status) VALUES ("
					+ matchDto.getMatchID()
					+ ","
					+ matchDto.getTeamOne()
					+ ","
					+ matchDto.getTeamOneCaptain()
					+ ","
					+ matchDto.getTeamOneViceCaptain()
					+ ","
					+ matchDto.getTeamTwo()
					+ ","
					+ matchDto.getTeamTwoCaptain()
					+ ","
					+ matchDto.getTeamTwoViceCaptain()
					+ ","
					+ matchDto.getTossWon()
					+ ","
					+ matchDto.getBattingFirst()
					+ ","
					+ matchDto.getOvers()
					+ ","
					+ matchDto.getWinner()
					+ ","
					+ ((matchDto.getLocation() == null) ? "null" : ("'"
							+ matchDto.getLocation() + "'"))
					+ ","
					+ ((matchDto.getMatchType() == null) ? "null" : ("'"
							+ matchDto.getMatchType() + "'"))
					+ ", STR_TO_DATE('"
					+ matchDto.getMatchDate()
					+ "','%m/%d/%Y')"
					+ ", "
					+ matchDto.getManOfTheMatch()
					+ ", "
					+ matchDto.getIsAbandoned()
					+ ", "
					+ matchDto.getIsFollowon()
					+ ", '"
					+ DButility.escapeLine(matchDto.getComment()) + "'," + matchDto.getScorer() 
					+ ",'" 
					+ matchDto.getAbandoneType() + "',NOW(),"+matchDto.getLastUpdatedBy()
					+",'"
					+(matchDto.getLive_streaming_link()==null?"":matchDto.getLive_streaming_link())
					+ "'," 
					+ matchDto.getIsTrump() 
					+ ","
					+ matchDto.isDls()
					+ ","
					+ matchDto.getT2Target()
					+ ","
					+ CommonUtility.oversToBalls(matchDto.getT2RevisedOvers()+"")
					+ ",'"
					+ matchDto.getStatus()
					+"')";
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.execute(query);

			if (matchDto.getMatchID()>0) {
				query = null;
				st1 = conn.createStatement();
				if (matchDto.getPlayers1() != null && !matchDto.getPlayers1().isEmpty()) {
					List<Integer> playersTeamOne = matchDto.getPlayers1();
					for (int i = 0; i < playersTeamOne.size(); i++) {
	
						query = "INSERT INTO match_player_team(MATCH_ID, PLAYER_ID, TEAM_ID) VALUES ("
								+ matchId
								+ ","
								+ (new Long(playersTeamOne.get(i))).longValue()
								+ ","
								+ matchDto.getTeamOne() + ")";
						st1.addBatch(query);
						query = null;
	
					}
				}
				if (matchDto.getPlayers2() != null && !matchDto.getPlayers2().isEmpty()) {
					List<Integer> playersTeamTwo = matchDto.getPlayers2();
					for (int i = 0; i < playersTeamTwo.size(); i++) {
	
						query = "INSERT INTO match_player_team(MATCH_ID, PLAYER_ID, TEAM_ID) VALUES ("
								+ matchId
								+ ","
								+ (new Long(playersTeamTwo.get(i))).longValue()
								+ ","
								+ matchDto.getTeamTwo() + ")";
						st1.addBatch(query);
						query = null;
	
					}
				}
				st1.executeBatch();
			}

		} catch (Exception e) {
			log.error(e.getMessage() + query, e);
			throw new Exception(e.getMessage());
		} finally {			
			DButility.closeStatement(st1);
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	protected void updateMatch(MatchDto matchDto, int clubId) throws Exception {

		String query;
		Statement st = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			query = "\n 	UPDATE matches SET" + "\n		TEAM_ONE = "
					+ matchDto.getTeamOne()
					+ "\n		,TEAM_ONE_CAPTAIN = "
					+ matchDto.getTeamOneCaptain()
					+ "\n		,TEAM_ONE_VICE_CAPTAIN = "
					+ matchDto.getTeamOneViceCaptain()
					+ "\n		,TEAM_TWO = "
					+ matchDto.getTeamTwo()
					+ "\n		,TEAM_TWO_CAPTAIN = "
					+ matchDto.getTeamTwoCaptain()
					+ "\n		,TEAM_TWO_VICE_CAPTAIN = "
					+ matchDto.getTeamTwoViceCaptain()
					+ "\n		,TOSS_WON = "
					+ matchDto.getTossWon()
					+ "\n		,BATTING_FIRST = "
					+ matchDto.getBattingFirst()
					+ "\n		,OVERS = "
					+ matchDto.getOvers()
					+ "\n		,MATCH_TYPE = '"
					+ matchDto.getMatchType()
					+"'"
					+ "\n		,t2_target = "
					+ matchDto.getT2Target()
					+ "\n		,t2_revised_over = "
					+ CommonUtility.oversToBalls(matchDto.getT2RevisedOvers()+"")
					+ "\n		,is_dls = "
					+ matchDto.isDls()
					
					
					+ "\n		,WINNER = "	+ matchDto.getWinner()
					+ "\n		,status = '"	+ matchDto.getStatus()+ "'" 
					+ "\n		,comment = '" + DButility.escapeLine(matchDto.getComment())+ "'"
					+ "\n		,live_youtube_link = '" + (matchDto.getLive_streaming_link()==null?"":matchDto.getLive_streaming_link())+ "'"
					+"\n 		,last_updated_date = NOW(),last_updated_by ="+matchDto.getLastUpdatedBy()
					+ "\n		,is_abandoned = "
					+ matchDto.getIsAbandoned()
					+ "\n	       ,is_followon ="
					+ matchDto.getIsFollowon()
					+ "\n		,abandone_type = '" + matchDto.getAbandoneType() + "', LOCATION = "
					+ (CommonUtility.isNullOrEmpty(matchDto.getLocation()) ? "null"
							: "'" + matchDto.getLocation() + "'")
					+ "\n		,is_trump = "
					+ matchDto.getIsTrump()
					+ "\n		,MATCH_DATE =  STR_TO_DATE('"
					+ matchDto.getMatchDate() + "','%m/%d/%Y')"
					+ "\n	WHERE " + "\n		MATCH_ID=" + matchDto.getMatchID();
			st.addBatch(query);
			st.addBatch(getDeleteQueryForMPTtable(matchDto.getMatchID()));
			addInsertQueryForMPTtable(matchDto.getMatchID(),
					matchDto.getTeamOne(), matchDto.getPlayers1(), st);
			addInsertQueryForMPTtable(matchDto.getMatchID(),
					matchDto.getTeamTwo(), matchDto.getPlayers2(), st);
			st.addBatch(getDeleteQueryForBattingTable(matchDto.getMatchID(),
					matchDto.getAllPlayersForTheMatch()));
			st.addBatch(getDeleteQueryForBowlingTable(matchDto.getMatchID(),
					matchDto.getAllPlayersForTheMatch()));

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	protected void deleteMatch(long matchId, int clubId, int userId) throws Exception {

		String query;
		Statement st = null;
		Connection conn = null;
		try {
			//MatchDto match = getMatchByMatchId(matchId, clubId);
			//Gson gson = new Gson();
			//String matchJson = gson.toJson(match);
			
			conn = DButility.getConnection(clubId);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			query = "DELETE FROM matches  WHERE MATCH_ID= " + matchId;
			
		//	st.addBatch(getAuditQueryForMatch(matchId, userId, matchJson, ApplicationConstants.RECORDS_TYPE_MATCH, ApplicationConstants.AUDIT_TYPE_DELETE));
			st.addBatch(query);
			st.addBatch(getDeleteQueryForMPTtable(matchId));
			st.addBatch(getDeleteQueryForBattingTable(matchId));
			st.addBatch(getDeleteQueryForBowlingTable(matchId));
			st.addBatch(getDeleteQueryForStatisticSummary(matchId));
			st.addBatch(getDeleteQueryForBallsTable(matchId));
			st.addBatch(getDeleteQueryForInningsTable(matchId));
			st.executeBatch();
			
			conn.commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected void insertAuditRecrords(long matchId, int clubId, int userId, String matchJson, String query) throws Exception {

		Statement st = null;
		Connection conn = null;
		try {
			
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected void backUpBallsData(long matchId, int clubId) throws Exception {
		
		String query = "INSERT INTO ball_backup(ball_id,innings_id,over,ball,runs,ball_type, "
				+ "batsman,runner,bowler,comment,out_method,out_person,wicket_taker_1,"
				+ "wicket_taker_2,direction,is_four,is_six,create_time,updated_date,"
				+ "client_id,youtube_Link,start_hh,start_mm,start_ss, end_hh,end_mm,"
				+ "end_ss,subt_plyr_name, no_ball_custom_runs, pitch_map, is_super_over) "
				+ "select ball_id,innings_id,over,ball,runs,ball_type,"
				+ "batsman,runner,bowler,comment,out_method,out_person,wicket_taker_1,"
				+ "wicket_taker_2,direction,is_four,is_six,create_time,updated_date,"
				+ "client_id,youtube_Link,start_hh,start_mm,start_ss, end_hh,end_mm,"
				+ "end_ss,subt_plyr_name, no_ball_custom_runs, pitch_map, is_super_over "
				+ "from ball where innings_id in (select innings_id from innings where match_id = "+matchId+")";

		Statement st = null;
		Connection conn = null;
		try {			
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	private String getDeleteQueryForBallsTable(long matchId) {
		return "delete from ball where innings_id in ( select innings_id from innings where match_id = "+ matchId + " ) ;";
	}

	private String getAuditQueryForMatch(long matchId, int userId, String matchJson, String recordsType,
			String auditType) {
		
		String query = "INSERT INTO audit_records (record_type, audit_type, record_id, requested_by, data, created_date) "
				+ " values('"+recordsType+"','"+auditType+"', '"+matchId+"','"+userId+"','"+matchJson+"',now())";
		
		return query;
	}

	private String getDeleteQueryForStatisticSummary(long matchId) {
		return "DELETE FROM player_statistics_summary WHERE match_id = "+ matchId;
	}

	private String getDeleteQueryForMPTtable(long matchId) {
		String query = "DELETE FROM match_player_team WHERE MATCH_ID = "
				+ matchId;
		return query;
	}

	private void addInsertQueryForMPTtable(long matchId, long teamId,
			List<Integer> players, Statement stmt) throws SQLException {
		String query = "";
		if (players != null) {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != null) {
					query = "INSERT INTO match_player_team(MATCH_ID, PLAYER_ID, TEAM_ID) VALUES ("
							+ matchId
							+ ","
							+ players.get(i).intValue()
							+ "," + teamId + ")";
					stmt.addBatch(query);
				}
			}

		}
	}

	private String getDeleteQueryForBowlingTable(long matchId, List<Integer> players) {
		String query = "DELETE FROM bowling WHERE MATCH_ID = " + matchId;
		if (players != null && !players.isEmpty()) {
			query += " AND PLAYER_ID NOT IN (";
			for (int i = 0; i < players.size(); i++) {
				query += players.get(i);
				if (i != (players.size() - 1))
					query += ",";
				else
					query += ")";
			}
			return query;
		}
		return query;
	}

	private String getDeleteQueryForBattingTable(long matchId, List<Integer> players) {
		String query = "DELETE FROM batting WHERE MATCH_ID = " + matchId;
		if (players != null && !players.isEmpty()) {
			query += " AND PLAYER_ID NOT IN (";
			for (int i = 0; i < players.size(); i++) {
				query += players.get(i);
				if (i != (players.size() - 1))
					query += ",";
				else
					query += ")";
			}
			return query;

		}
		return query;
	}

	private String getDeleteQueryForBowlingTable(long matchId) {
		String query = "DELETE FROM bowling WHERE MATCH_ID = " + matchId;
		return query;
	}	

	private String getDeleteQueryForBattingTable(long matchId) {
		String query = "DELETE FROM batting WHERE MATCH_ID = " + matchId;
		return query;
	}

	private String getDeleteQueryForBallTable(long matchId) {
		String query = "DELETE FROM ball WHERE innings_ID in ( select innings_id from innings where match_id = " + matchId + ") ";
		return query;
	}

	private String getDeleteQueryForInningsTable(long matchId) {
		String query = "DELETE FROM innings where match_id = " + matchId;
		return query;
	}

	/**
	 * @param year 
	 * @param dto
	 * @return
	 */
	protected List<MatchDto> getMatches(int skip, int limit, int clubId, int leagueId,
			int teamId, int days,String date,int groundId, String internalClubId, int status, String fromDate, String toDate, int year) throws Exception {
		List<MatchDto> matches = new ArrayList<MatchDto>();
		String query = MATCH_QUERY;
		
		if (status == 1) {
			query += "\nand m.is_complete = 1 ";
		}
		
		if (groundId != 0) {
			query += "\nand m.match_id in ( select m1.match_id from matches m1, fixtures f1 where m1.match_id = f1.match_id and f1.ground_id = "+groundId  +" ) ";
		}


		if (leagueId != 0) {
			query += "\nand t1.league = " + leagueId;
		}

		if (teamId != 0) {
			query += "\n and (m.team_one = " + teamId + " OR m.team_two = "
					+ teamId + ") ";
		}
		
		if(CommonUtility.stringToInt(internalClubId) > 0) {
			query += "  and (t1.club_id = "+internalClubId+" or t2.club_id = "+internalClubId+") ";
		}
		
		if(days != 0){
			query += " and match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
		}	
		if(!CommonUtility.isNullOrEmpty(date)){
			query += " and match_date >= '"+date+"' ";
		}	
		if(!CommonUtility.isNullOrEmpty(fromDate) && !CommonUtility.isNullOrEmpty(toDate)) {
			query += " and match_date >=STR_TO_DATE('"+fromDate+"',  '%m/%d/%Y') and match_date <=STR_TO_DATE('"+toDate+"',  '%m/%d/%Y') ";
		}
		if(year !=0) {
			query += "\n and YEAR( match_date) = "+year;
		}
		
		query += " order by m.match_date desc,m.match_id desc	\n";
		if (limit != 0) {
			if(skip>0) {
				query += " LIMIT " + skip + ", "+ limit;
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
				matches.add(populateMatch(rs,clubId));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matches;
	}
	
	protected List<MatchDto> getMatcheBasicDetails(int clubId) throws Exception {
		
		List<MatchDto> matches = new ArrayList<MatchDto>();
		
		String query = "select match_id, l.series_type, team_one, team_two, toss_won, batting_first, match_type, m.is_complete "
				+ " from matches m,	team t1, team t2, league l WHERE t1.team_id = m.team_one	 AND t2.team_id = m.team_two AND t1.league = l.league_id " 
				+ " order by m.match_date desc,m.match_id desc ";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				
				MatchDto match = new MatchDto();
				
				match.setClubID(clubId);			
				match.setMatchID(rs.getInt("match_id"));
				match.setSeriesType(rs.getString("series_type"));
				match.setTeamOne(rs.getInt("team_one"));
				match.setTeamTwo(rs.getInt("team_two"));
				match.setTossWon(rs.getInt("toss_won"));
				match.setBattingFirst(rs.getInt("batting_first"));
				match.setMatchType(rs.getString("match_type"));
				match.setIsComplete(rs.getInt("is_complete"));
				
				matches.add(match);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matches;
	}
	
	protected Integer getFootBallMatchCountByPlayerId(int playerId, List<Integer> clubIds) throws Exception {
		
		int matchCount = 0;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		query += "select sum(no_of_matches) from ( ";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}			
				query += " select sum(no_of_matches) no_of_matches from "
						+ "( SELECT count(distinct(i.match_id)) no_of_matches "
						+ "FROM "+clubSchema+".incidents i, "+clubSchema+".match_player_team mpt "
						+ "WHERE i.match_id = mpt.match_id and mpt.player_id = "+playerId + " ) final_query_"+clubId;
				
					query += " UNION ";
			}
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			query += " ) final_query ";
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					matchCount = rs.getInt(1);
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		return matchCount;
	}
	
	protected List<MatchDto> getClubAllLiveMatches(int clubId, int limit) throws Exception {
		List<MatchDto> matches = new ArrayList<MatchDto>();
		String query = MATCH_QUERY;

		query += " and is_complete = 0 ";			

		query += " order by m.match_date desc,m.match_id desc	\n";
		if (limit != 0) {
			query += " LIMIT " + limit;
		}

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				matches.add(populateMatch(rs,clubId));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matches;
	}

	private MatchDto populateMatch(ResultSet rs, int clubId) throws SQLException {
		MatchDto match = new MatchDto();
		match.setClubID(clubId);
		match.setSeriesType(rs.getString("series_type"));
		match.setSeriesName(rs.getString("league_name"));
		match.setMatchID(rs.getInt("match_id"));
		match.setTeamOne(rs.getInt("team_one"));
		match.setTeamOneName(rs.getString("team_one_name"));
		match.setTeamOneCode(rs.getString("team_one_code"));
		match.setTeamOneCaptain(rs.getInt("team_one_captain"));
		match.setTeamOneViceCaptain(rs.getInt("team_one_vice_captain"));
		match.setTeamTwo(rs.getInt("team_two"));
		match.setTeamTwoName(rs.getString("team_two_name"));
		match.setTeamTwoCode(rs.getString("team_two_code"));
		match.setTeamTwoCaptain(rs.getInt("team_two_captain"));
		match.setTeamTwoViceCaptain(rs.getInt("team_two_vice_captain"));
		match.setMatchDate(rs.getString("match_date"));
		match.setBattingFirst(rs.getInt("batting_first"));
		match.setTossWon(rs.getInt("toss_won"));
		match.setOvers(rs.getInt("overs"));
		match.setWinner(rs.getInt("winner"));
		match.setLocation(rs.getString("location"));
		match.setMatchType(rs.getString("match_type"));
		
		match.setT1total(rs.getInt("t1_total"));
		match.setT2total(rs.getInt("t2_total"));
		match.setT1balls(rs.getInt("t1_balls"));
		match.setT2balls(rs.getInt("t2_balls"));
		match.setT1wickets(rs.getInt("t1_wickets"));
		match.setT2wickets(rs.getInt("t2_wickets"));
		
		match.setT1_1total(rs.getInt("t1_1total"));
		match.setT2_1total(rs.getInt("t2_1total"));
		match.setT1_1balls(rs.getInt("t1_1balls"));
		match.setT2_1balls(rs.getInt("t2_1balls"));
		match.setT1_1wickets(rs.getInt("t1_1wickets"));
		match.setT2_1wickets(rs.getInt("t2_1wickets"));
		
		match.setT1_2total(rs.getInt("t1_2total"));
		match.setT2_2total(rs.getInt("t2_2total"));
		match.setT1_2balls(rs.getInt("t1_2balls"));
		match.setT2_2balls(rs.getInt("t2_2balls"));
		match.setT1_2wickets(rs.getInt("t1_2wickets"));
		match.setT2_2wickets(rs.getInt("t2_2wickets"));
		
		match.setT1byes(rs.getInt("t1_byes"));
		match.setT2byes(rs.getInt("t2_byes"));
		match.setT1lbyes(rs.getInt("t1_lbyes"));
		match.setT2lbyes(rs.getInt("t2_lbyes"));
		match.setT1penalty(rs.getInt("t1_penalty"));
		match.setT2penalty(rs.getInt("t2_penalty"));
		
		match.setT1_1byes(rs.getInt("t1_1byes"));
		match.setT2_1byes(rs.getInt("t2_1byes"));
		match.setT1_1lbyes(rs.getInt("t1_1lbyes"));
		match.setT2_1lbyes(rs.getInt("t2_1lbyes"));
		match.setT1_1penalty(rs.getInt("t1_1penalty"));
		match.setT2_1penalty(rs.getInt("t2_1penalty"));
		
		match.setT1_2byes(rs.getInt("t1_2byes"));
		match.setT2_2byes(rs.getInt("t2_2byes"));
		match.setT1_2lbyes(rs.getInt("t1_2lbyes"));
		match.setT2_2lbyes(rs.getInt("t2_2lbyes"));
		match.setT1_2penalty(rs.getInt("t1_2penalty"));
		match.setT2_2penalty(rs.getInt("t2_2penalty"));
		
		match.setT1_logo_file_path(rs.getString("t1_logo_file_path"));
		match.setT2_logo_file_path(rs.getString("t2_logo_file_path"));
		
		match.setManOfTheMatch(rs.getInt("MAN_OF_THE_MATCH"));
		match.setIsComplete(rs.getInt("IS_COMPLETE"));
		match.setIsLocked(rs.getInt("IS_LOCKED"));
		match.setIsAbandoned(rs.getInt("IS_ABANDONED"));
		match.setIsFollowon(rs.getInt("is_followon"));
		match.setIsTrump(rs.getInt("is_trump"));
		match.setAbandoneType(rs.getString("abandone_type"));
		
		match.setComment(rs.getString("COMMENT"));
		match.setLeagueId(rs.getInt("league"));
		match.setMaximumPlayers(rs.getInt("maximum_players"));
		match.setScorer(rs.getInt("scorer"));
		match.setUmpire1(rs.getString("umpire1"));
		match.setUmpire2(rs.getString("umpire2"));
		match.setNotificationSent(rs.getString("notification_sent"));
		match.setFow1(rs.getString("fow1"));
		match.setFow2(rs.getString("fow2"));
		match.setFow1_2(rs.getString("fow1_2"));
		match.setFow2_2(rs.getString("fow2_2"));
		match.setLastUpdatedDate(rs.getTimestamp("last_updated_date"));
		match.setLastUpdatedBy(rs.getInt("last_updated_by"));
		match.setLive_streaming_link(rs.getString("live_youtube_link"));
		match.setTimeSinceLastUpdate(CommonUtility.timeLapsedFromSeconds(rs.getLong("time_diff")));
		match.setDls(rs.getBoolean("is_dls"));
		match.setT2Target(rs.getInt("t2_target"));
		match.setR1ResAvailable(rs.getDouble("r1_res_available"));
		match.setT2RevisedOvers(rs.getInt("t2_revised_over"));
		match.setResult(match.getResult());
		match.setStatus(rs.getString("status"));

		return match;
	}

	protected MatchDto getMatchByMatchId(long matchId, int clubId)
			throws Exception {
		String query = MATCH_QUERY + " AND m.match_id =	" + matchId + "\n";

		Connection conn = null;
		Statement st1 = null;
		ResultSet rs1 = null;
		Statement st2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		MatchDto matchDto = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st1 = conn.createStatement();
			rs1 = st1.executeQuery(query);
			while (rs1.next()) {
				matchDto = populateMatch(rs1, clubId);
			}
			if(matchDto != null){
				query = MATCH_PLAYER_TEAM_QUERY;
				st2 = conn.createStatement();
				query += " '" + matchId + "' and team_id='" + matchDto.getTeamOne()
						+ "' " + MATCH_PLAYER_TEAM_QUERY_END;
				rs2 = st2.executeQuery(query);
				List<Integer> players1 = new ArrayList<Integer>();
				while (rs2.next()) {
					players1.add(rs2.getInt("PLAYER_ID"));
				}
				matchDto.setPlayers1(players1);
	
				query = MATCH_PLAYER_TEAM_QUERY;
				st3 = conn.createStatement();
				query += " '" + matchId + "' and team_id='" + matchDto.getTeamTwo()
						+ "' " + MATCH_PLAYER_TEAM_QUERY_END;
				rs3 = st3.executeQuery(query);
				List<Integer> players2 = new ArrayList<Integer>();
				while (rs3.next()) {
					players2.add(rs3.getInt("PLAYER_ID"));
				}
				matchDto.setPlayers2(players2);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(st2);
			DButility.closeStatement(st3);
			DButility.closeRs(rs2);
			DButility.closeRs(rs3);
			DButility.dbCloseAll(conn, st1, rs1);
		}
		return matchDto;
	}
	
	protected Map<String, Integer> getPlayerIdListForLeagueMatches(int clubId, String matchIdsStr) throws Exception{
		
		Map<String, Integer> matchTeamIdPlayerIdList = new HashMap<String, Integer>();
		String query = "select match_id, team_id, count(*) from match_player_team where match_id in ("+matchIdsStr+") "
				+ " group by match_id, team_id";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				matchTeamIdPlayerIdList.put(rs.getInt(1)+"-"+rs.getInt(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchTeamIdPlayerIdList;
	}

	/**
	 * @param match
	 */
	protected void saveMatchStatistics(MatchDto match, int clubId)
			throws Exception {
		String query = "update matches set " +
				"t1_total=" + match.getT1total()
				+ ",t2_total=" + match.getT2total() + 
				",t1_balls="+ match.getT1balls() 
				+ ",t2_balls=" + match.getT2balls() + 
				 ",t1_wickets=" + match.getT1wickets() 
				 + ",t2_wickets="+match.getT2wickets() +
				
				",t1_1total=" + match.getT1_1total()
				+ ",t2_1total=" + match.getT2_1total()  
				+ ",t1_1balls="+ match.getT1_1balls() 
				+ ",t2_1balls=" + match.getT2_1balls()  
				 + ",t1_1wickets=" + match.getT1_1wickets() 
				+ ",t2_1wickets=" + match.getT2_1wickets() +
				
				",t1_2total=" + match.getT1_2total()
				+ ",t2_2total=" + match.getT2_2total()  
				+ ",t1_2balls="+ match.getT1_2balls() 
				+ ",t2_2balls=" + match.getT2_2balls()  
				 + ",t1_2wickets=" + match.getT1_2wickets() 
				+ ",t2_2wickets=" + match.getT2_2wickets() +
				
				",last_updated_date = NOW(),last_updated_by ="+match.getLastUpdatedBy()				
				
				+ ",t1_byes=" + match.getT1byes() + ", t1_penalty=" + match.getT1penalty() + " ,t2_penalty=" + match.getT2penalty()
				+ ",t2_byes=" + match.getT2byes() + ",t1_lbyes="
				+ match.getT1lbyes() + ",t2_lbyes=" + match.getT2lbyes()
				
				+ ",t1_1byes=" + match.getT1_1byes() + ", t1_1penalty=" + match.getT1_1penalty() + " ,t2_1penalty=" + match.getT2_1penalty()
				+ ",t2_1byes=" + match.getT2_1byes() + ",t1_1lbyes="
				+ match.getT1_1lbyes() + ",t2_1lbyes=" + match.getT2_1lbyes()
				
				+ ",t1_2byes=" + match.getT1_2byes() + ", t1_2penalty=" + match.getT1_2penalty() + " ,t2_2penalty=" + match.getT2_2penalty()
				+ ",t2_2byes=" + match.getT2_2byes() + ",t1_2lbyes="
				+ match.getT1_2lbyes() + ", t2_revised_over="+CommonUtility.oversToBalls(match.getT2RevisedOvers()+"")
				+ ", t2_2lbyes=" + match.getT2_2lbyes()
				+ ", fow1_2= '"+ DButility.escapeLine(match.getFow1_2()) +"', fow2_2= '"+ DButility.escapeLine(match.getFow2_2()) +"' "
				
				+ ",winner=" + match.getWinner() + ",MAN_OF_THE_MATCH = "
				+ match.getManOfTheMatch() + ", umpire1= '" + match.getUmpire1() + "', umpire2= '" + match.getUmpire2() + "',is_complete = "
				+ match.getIsComplete() + ", is_trump = " + match.getIsTrump() + ", fow1= '"+ DButility.escapeLine(match.getFow1()) +"', fow2= '"
				+ DButility.escapeLine(match.getFow2()) +"', is_abandoned = "
				+ match.getIsAbandoned() + ", abandone_type = '" + match.getAbandoneType() + "', notification_sent = "
				+ ("".equals(match.getNotificationSent()) ? "null" : match.getNotificationSent()) + " where match_id =	"
				+ match.getMatchID() + "\n";

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected void saveMatchStatisticsFB(MatchDto match, int clubId) throws Exception {
		
		String query = "update matches set t1_total= "+match.getT1total()+",t2_total= "+match.getT2total()
				+",last_updated_date = NOW(),last_updated_by ="+match.getLastUpdatedBy()
				+ ",winner=" + match.getWinner() + ",MAN_OF_THE_MATCH = "+match.getManOfTheMatch() 
				+ ", umpire1= '" + match.getUmpire1() + "', umpire2= '" + match.getUmpire2() 
				+ "',is_complete = '" + match.getIsComplete() +"', is_abandoned = " + match.getIsAbandoned() 
				+ ", abandone_type = '" + match.getAbandoneType() + "', notification_sent = "
				+ ("".equals(match.getNotificationSent()) ? "null" : match.getNotificationSent()) 
				+ " where match_id = " + match.getMatchID();

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	
	protected void updateFootBallMatchGoals(int t1Goals, int t2Goals, int matchId, int clubId) throws Exception {
		
		String query = "update matches set t1_total=?,t2_total=? where match_id=?";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId, true);
			st = conn.prepareStatement(query);
			
			st.setInt(1, t1Goals);
			st.setInt(2, t2Goals);
			st.setInt(3, matchId);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

	}

	protected void updateLock(int lock, int matchId, int clubId)
			throws Exception {
		String query = "update matches set is_locked = " + lock
				+ " where match_id =	" + matchId + "\n";

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

	}
	
	protected void updateLockByLeague(int lock, int leagueId, int clubId)
	throws Exception {
String query = "update matches set is_locked = " + lock
		+ " where match_id in (select match_id from (select match_id from matches m, team t where t.team_id = m.team_one and t.league = "+leagueId+") as matchListTempTable )";

Connection conn = null;
Statement st = null;
ResultSet rs = null;
try {
	conn = DButility.getConnection(clubId);
	st = conn.createStatement();
	st.executeUpdate(query);
} catch (SQLException e) {
	throw new Exception(e.getMessage());
} finally {
	DButility.dbCloseAll(conn, st, rs);
}

}

	public Map<String, String> getMatchesByScorer(int userID, int clubId) throws Exception {
		String query = "select match_Id,scorer from matches where scorer = " + userID + " and is_complete != 1";
		Map<String, String> matchesMap = new HashMap<String, String>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				matchesMap.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchesMap;
	}
	
	protected List<MatchDto> getMatchesByUmpire(int skip, int limit, int clubId, int leagueId,
			int teamId, int days,String date,int umpireUserId, String internalClubId, int status) throws Exception {
		List<MatchDto> matches = new ArrayList<MatchDto>();
		String query = MATCH_QUERY;
		
		if (status == 1) {
			query += "\nand m.is_complete = 1 ";
		}
			
		if (umpireUserId > 0) {
			query += "\n and m.match_id in ( select m1.match_id from matches m1, fixtures f1 where m1.match_id = f1.match_id and "
					+ " (f1.umpire1_id = "+umpireUserId  + " or f1.umpire2_id =" + umpireUserId + " or f1.scorer_id =" + umpireUserId +")) ";
		}


		if (leagueId != 0) {
			query += "\nand t1.league = " + leagueId;
		}

		if (teamId != 0) {
			query += "\n and (m.team_one = " + teamId + " OR m.team_two = "
					+ teamId + ") ";
		}
		
		if(CommonUtility.stringToInt(internalClubId) > 0) {
			query += "  and (t1.club_id = "+internalClubId+" or t2.club_id = "+internalClubId+") ";
		}
		
		if(days != 0){
			query += " and match_date >= ( CURDATE() - INTERVAL "+days+" DAY ) ";
		}	
		if(!CommonUtility.isNullOrEmpty(date)){
			query += " and match_date >= '"+date+"' ";
		}	
		query += " order by m.match_date desc,m.match_id desc	\n";
		if (limit != 0) {
			if(skip>0) {
				query += " LIMIT " + skip + ", "+ limit;
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
				matches.add(populateMatch(rs,clubId));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matches;
	}

	public Map<Integer, Integer> getMatchScorerMap(int clubId) throws Exception {
		String query = "select match_Id,scorer from matches where is_complete != 1 ";
		Map<Integer, Integer> matchesMap = new HashMap<Integer, Integer>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				matchesMap.put(rs.getInt(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchesMap;
	}
	public Map<Integer, String> getMatchScorerNameMap(int clubId) throws Exception {
		String query = "select uv.user_id,concat(uv.f_name,' ',uv.l_name) scorer_name from mcc.user_view uv, matches m "
						+ " where uv.club_id = "+clubId+" and uv.user_id = m.scorer "
						+ " and (m.is_complete = 0 or m.is_complete is null)";
		Map<Integer, String> scorerNameMap = new HashMap<Integer, String>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				scorerNameMap.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return scorerNameMap;
	}

	public List<Integer> getPlayersOfMatches(List<Integer> matchIds, int clubId) throws Exception {
		String query = "select distinct player_id from match_player_team where match_id in (" + StringUtils.join(matchIds,",") + ")";
		List<Integer> players = new ArrayList<Integer>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				players.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	public void updateResOverDls(MatchDto matchDto, int clubId) throws Exception {

			String query = "update matches set is_dls = 1, overs = "+matchDto.getOvers()+"  where match_id = " + matchDto.getMatchID();
			
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			try {
				conn = DButility.getConnection(clubId);
				st = conn.createStatement();
				st.executeUpdate(query);
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}

	}
	
	public void auditCreateMatch(int clubId, int fixtureId, int matchId, String matchInfo, int createdBy) throws Exception {

		String query = "insert into audit_create_match(club_id,fixture_id,match_info,created_by,created_date) values(?,?,?,?,now())";
		
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setInt(index++, clubId);
			st.setInt(index++, fixtureId);
			st.setString(index++, matchInfo);
			st.setInt(index++, createdBy);
			
			 st.executeUpdate();
			 
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	public String getCreateMatchJson(int clubId, int fixtureId) throws Exception {

		String query = "select match_info from audit_create_match where club_id=? and fixture_id = ? ";
		String matchInfo = "";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs= null;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setInt(index++, clubId);	
			st.setInt(index++, fixtureId);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				matchInfo = rs.getString("match_info");
			}
			 
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchInfo;
	}

	public void updateMatchR1AvailableAndT2Target(MatchDto matchDto, int clubId) throws Exception {

		String query = "update matches set r1_res_available = "+matchDto.getR1ResAvailable()+
				", t2_target = "+matchDto.getT2Target()+
				", t2_revised_over = "+CommonUtility.oversToBalls(matchDto.getOvers()+"")+
				"  where match_id = " + matchDto.getMatchID();
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

}

	public void updateResOverTarget(MatchDto matchDto, int clubId) throws Exception {

		String query = "update matches set is_dls = 1, t2_revised_over = "+CommonUtility.oversToBalls(matchDto.getT2RevisedOvers()+"")+
				" , t2_target = " +matchDto.getT2Target()+ "  where match_id = " + matchDto.getMatchID();
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

}

	public List<MatchDto> getPlayerMatches(int playerId) throws Exception {
		List<MatchDto> matches = new ArrayList<MatchDto>();
		String query = "{CALL mcc.GetPlayerMatches(?)}";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareCall(query);
			st.setInt(1, playerId);
			rs = st.executeQuery();
			while (rs.next()) {
				matches.add(populateMatch(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matches;
	}
	
	//Users with whom he/she played most number of matches (from the teams that he is part of or played against).
	public List<Integer> getListOfPlayerIdsByMostNoOfMatches(int playerId, List<Integer> clubIds, int limit) throws Exception {
		
		List<Integer> playerIds = new ArrayList<Integer>();
		
		String query = "select player_id, no_of_matches from (";		
		int noOfClubs = clubIds.size();
		int i = 0;		
		String schemaName = "";
		for(Integer cid : clubIds) {
			i++;
			if(cid == 1) {
				schemaName = "mcc";
			}else {
				schemaName = "club"+cid;
			}
			query += " SELECT player_id, COUNT(match_id) no_of_matches FROM "+schemaName+".match_player_team " + 
					"WHERE match_id IN (SELECT match_id FROM "+schemaName+".match_player_team WHERE player_id = ?) " + 
					" AND player_id != ? GROUP BY player_id ";
			if(i<noOfClubs) {
				query += " UNION ";
			}
		}
		query += " ) final_query ORDER BY no_of_matches DESC LIMIT ? ";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareCall(query);
			int index = 1;
			for(int j=0;  j<noOfClubs; j++) {
				st.setInt(index++, playerId);
				st.setInt(index++, playerId);
			}
			st.setInt(index++, limit);
			rs = st.executeQuery();
			while (rs.next()) {
				playerIds.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerIds;
	}
	
	protected List<MatchDto> getPlayerMatchesNew(int playerId, List<Integer> clubIds) throws Exception {
		
		List<MatchDto> matches = new ArrayList<MatchDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}
				String clubName = club.getName();
				clubName = clubName.replace("'", "");
				query += "SELECT  " + clubId + " club_Id, '" + clubName + "' club_name, match_id, "
						+ " l.series_type, l.league_name series_name,team_one, team_two, toss_won, batting_first, overs, m.winner, MAN_OF_THE_MATCH, "
						+ " DATE_FORMAT(MATCH_DATE, '%m/%d/%Y') MATCH_DATE,  team_one_captain, team_two_captain, team_one_vice_captain, location, match_type,"
						+ " team_two_vice_captain, t1_total, t2_total, t1_balls, t2_balls, t1_wickets, t2_wickets, t1_1total, t2_1total, t1_1balls, "
						+ " t2_1balls, t1_1wickets, t2_1wickets, t1_2total, t2_2total, t1_2balls, t2_2balls, t1_2wickets, t2_2wickets, t1_byes, "
						+ " t1_penalty, t2_penalty, t2_byes, t1_lbyes, t2_lbyes, t1_1byes, t1_1penalty, t2_1penalty, t2_1byes, t1_1lbyes, t2_1lbyes, t1_2byes, "
						+ " t1_2penalty, t2_2penalty, t2_2byes, t1_2lbyes, t2_2lbyes, m.is_dls, m.t2_target, m.r1_res_available, m.t2_revised_over, "
						+ " t1.team_name team_one_name, t2.team_name team_two_name, t1.team_code team_one_code, t2.team_code team_two_code, "
						+ " IFNULL(t1.logo_file_path, '') t1_logo_file_path, IFNULL(t2.logo_file_path, '') t2_logo_file_path, m.is_complete, m.is_abandoned, "
						+ " m.is_locked, m.is_followon, m.comment, m.umpire1, m.umpire2, m.notification_sent, m.scorer, m.fow1, m.fow2, m.fow1_2, m.fow2_2, "
						+ " m.abandone_type, m.last_updated_date, TIMESTAMPDIFF(SECOND,  m.last_updated_date, NOW()) time_diff, m.last_updated_by, "
						+ " m.status, t1.league, l.maximum_players, m.is_trump, m.live_youtube_link " + " FROM "
						+ clubSchema + ".matches m, " + clubSchema + ".team t1, " + clubSchema + ".team t2, " + clubSchema + ".league l "
						+ " WHERE t1.team_id = m.team_one AND t2.team_id = m.team_two AND t1.league = l.league_id and l.hide_series = 0 "
						+ " and m.match_id  in (select match_id from " + clubSchema+ ".match_player_team where player_id = " + playerId + ") ";
				
					query += " UNION ";
			}
			
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					matches.add(populateMatch(rs));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		
		return matches;
	}
	
	protected List<PlayerMatchDtoFB> getPlayerMatchesFB(int playerId, List<Integer> clubIds) throws Exception {
		
		List<PlayerMatchDtoFB> matches = new ArrayList<PlayerMatchDtoFB>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}
				String clubName = club.getName();
				clubName = clubName.replace("'", "");
				
				query += "SELECT "+clubId+" club_Id, '"+clubName+ "' club_name, i.player1_id player_id, m.match_id, "
						+ "( SELECT CONCAT(f_name,' ',l_name) FROM mcc.player WHERE player_id = i.player1_id ) player_name, "
						+ "i.mins_of_game, "
						+ "(SELECT team_id FROM "+clubSchema+".team_player WHERE player_id = i.player1_id AND team_id IN (m.team_one, m.team_two) ) player_team_id, "
						+ " l.series_type, l.league_name series_name, m.team_one, m.team_two, m.winner, "
						+ " DATE_FORMAT(MATCH_DATE, '%m/%d/%Y') MATCH_DATE,  location, m.match_type,"
						+ " m.t1_total, m.t2_total, t1.team_name team_one_name, t2.team_name team_two_name, "
						+ " t1.team_code team_one_code, t2.team_code team_two_code, "
						+ " IFNULL(t1.logo_file_path, '') t1_logo_file_path, IFNULL(t2.logo_file_path, '') t2_logo_file_path, "
						+ "m.is_complete, m.is_abandoned, m.is_locked, m.umpire1, m.abandone_type, m.last_updated_date, "
						+ "TIMESTAMPDIFF(SECOND,  m.last_updated_date, NOW()) time_diff, m.last_updated_by, "
						+ " m.status, t1.league, m.live_youtube_link " 
						+ "FROM "+clubSchema+".incidents i, "+clubSchema+".matches m, "+clubSchema+".team_player tp, "
						+ clubSchema + ".team t1, " + clubSchema + ".team t2, " + clubSchema + ".league l "
						+ "WHERE i.match_id = m.match_id AND i.incident_type LIKE '%Goal Scored%' "
						+ "AND (m.team_one = tp.team_id OR m.team_two = tp.team_id) AND t1.team_id = m.team_one AND t2.team_id = m.team_two "
						+ "AND t1.league = l.league_id AND t2.league = l.league_id  AND l.hide_series = 0 "
						+ "AND m.match_id IN ( SELECT match_id FROM "+clubSchema+".match_player_team WHERE player_id = "+playerId +" ) "
						+ " GROUP BY i.player1_id, i.match_id, i.mins_of_game ";
				
					query += " UNION ";
			}
			
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			
			query += " order by i.match_id, i.player1_id, i.mins_of_game ";
			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					matches.add(populateMatchFB(rs));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		
		return matches;
	}
	
	protected List<MatchDto> getMatchesForPlayerAsUmpire(int userId, List<Integer> clubIds) throws Exception {
		
		List<MatchDto> matches = new ArrayList<MatchDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}
				String clubName = club.getName();
				clubName = clubName.replace("'", "");
				query += "SELECT  " + clubId + " club_Id, '" + clubName + "' club_name, m.match_id, "
						+ " l.series_type, l.league_name series_name,m.team_one, m.team_two, m.toss_won, m.batting_first, m.overs, m.winner, MAN_OF_THE_MATCH, "
						+ " DATE_FORMAT(MATCH_DATE, '%m/%d/%Y') MATCH_DATE,  team_one_captain, team_two_captain, team_one_vice_captain, m.location, m.match_type,"
						+ " m.team_two_vice_captain, t1_total, t2_total, t1_balls, t2_balls, t1_wickets, t2_wickets, t1_1total, t2_1total, t1_1balls, "
						+ " t2_1balls, t1_1wickets, t2_1wickets, t1_2total, t2_2total, t1_2balls, t2_2balls, t1_2wickets, t2_2wickets, t1_byes, "
						+ " t1_penalty, t2_penalty, t2_byes, t1_lbyes, t2_lbyes, t1_1byes, t1_1penalty, t2_1penalty, t2_1byes, t1_1lbyes, t2_1lbyes, t1_2byes, "
						+ " t1_2penalty, t2_2penalty, t2_2byes, t1_2lbyes, t2_2lbyes, m.is_dls, m.t2_target, m.r1_res_available, m.t2_revised_over, "
						+ " t1.team_name team_one_name, t2.team_name team_two_name, t1.team_code team_one_code, t2.team_code team_two_code, "
						+ " IFNULL(t1.logo_file_path, '') t1_logo_file_path, IFNULL(t2.logo_file_path, '') t2_logo_file_path, m.is_complete, m.is_abandoned, "
						+ " m.is_locked, m.is_followon, m.comment, m.umpire1, m.umpire2, m.notification_sent, m.scorer, m.fow1, m.fow2, m.fow1_2, m.fow2_2, "
						+ " m.abandone_type, m.last_updated_date, TIMESTAMPDIFF(SECOND,  m.last_updated_date, NOW()) time_diff, m.last_updated_by, "
						+ " m.status, t1.league, l.maximum_players, m.is_trump, m.live_youtube_link " + " FROM "
						+ clubSchema + ".matches m, " + clubSchema + ".team t1, " + clubSchema + ".team t2, "  + clubSchema + ".fixtures f, " + clubSchema + ".league l "
						+ " WHERE m.match_id = f.match_id AND f.match_id>0 AND t1.team_id = m.team_one AND t2.team_id = m.team_two "
						+ " and f.team_one = t1.team_id and f.team_two = t2.team_id "
						+ " AND f.league_id = l.league_id and l.hide_series = 0 AND (f.umpire1_id = "+userId+" or f.umpire2_id = "+userId+" )";
				
					query += " UNION ";
			}
			
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					matches.add(populateMatch(rs));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		
		return matches;
	}
	
	protected List<MatchDto> getMatchesForPlayerAsScorer(int userId, List<Integer> clubIds) throws Exception {
		
		List<MatchDto> matches = new ArrayList<MatchDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";				
		String clubSchema = "";
		for (int clubId : clubIds) {
			ClubDto club = ClubFactory.getClub(clubId);
			if (club != null) {		
				if (clubId > 1) {
					clubSchema = "club" + clubId;
				} else if (clubId == 1) {
					clubSchema = "mcc";
				}
				String clubName = club.getName();
				clubName = clubName.replace("'", "");
				
				query += "SELECT  " + clubId + " club_Id, '" + clubName + "' club_name, match_id, "
						+ " l.series_type, l.league_name series_name,team_one, team_two, toss_won, batting_first, overs, m.winner, MAN_OF_THE_MATCH, "
						+ " DATE_FORMAT(MATCH_DATE, '%m/%d/%Y') MATCH_DATE,  team_one_captain, team_two_captain, team_one_vice_captain, location, match_type,"
						+ " team_two_vice_captain, t1_total, t2_total, t1_balls, t2_balls, t1_wickets, t2_wickets, t1_1total, t2_1total, t1_1balls, "
						+ " t2_1balls, t1_1wickets, t2_1wickets, t1_2total, t2_2total, t1_2balls, t2_2balls, t1_2wickets, t2_2wickets, t1_byes, "
						+ " t1_penalty, t2_penalty, t2_byes, t1_lbyes, t2_lbyes, t1_1byes, t1_1penalty, t2_1penalty, t2_1byes, t1_1lbyes, t2_1lbyes, t1_2byes, "
						+ " t1_2penalty, t2_2penalty, t2_2byes, t1_2lbyes, t2_2lbyes, m.is_dls, m.t2_target, m.r1_res_available, m.t2_revised_over, "
						+ " t1.team_name team_one_name, t2.team_name team_two_name, t1.team_code team_one_code, t2.team_code team_two_code, "
						+ " IFNULL(t1.logo_file_path, '') t1_logo_file_path, IFNULL(t2.logo_file_path, '') t2_logo_file_path, m.is_complete, m.is_abandoned, "
						+ " m.is_locked, m.is_followon, m.comment, m.umpire1, m.umpire2, m.notification_sent, m.scorer, m.fow1, m.fow2, m.fow1_2, m.fow2_2, "
						+ " m.abandone_type, m.last_updated_date, TIMESTAMPDIFF(SECOND,  m.last_updated_date, NOW()) time_diff, m.last_updated_by, "
						+ " m.status, t1.league, l.maximum_players, m.is_trump, m.live_youtube_link " + " FROM "
						+ clubSchema + ".matches m, " + clubSchema + ".team t1, " + clubSchema + ".team t2, " + clubSchema + ".league l "
						+ " WHERE t1.team_id = m.team_one AND t2.team_id = m.team_two AND t1.league = l.league_id and l.hide_series = 0 AND m.scorer = "+userId;
				
					query += " UNION ";
			}
			
		}
		if(!CommonUtility.isNullOrEmpty(query)) {
			
			query = query.substring(0,query.lastIndexOf("UNION") );
			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareCall(query);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					matches.add(populateMatch(rs));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
		}
		
		return matches;
	}

	private MatchDto populateMatch(ResultSet rs) throws SQLException {
		
		MatchDto match = new MatchDto();
		
		match.setClubID(rs.getInt("club_id"));
		match.setClubName(rs.getString("club_name"));
		match.setSeriesType(rs.getString("series_type"));
		match.setSeriesName(rs.getString("series_name"));
		match.setMatchID(rs.getInt("match_id"));
		match.setTeamOne(rs.getInt("team_one"));
		match.setTeamOneName(rs.getString("team_one_name"));
		match.setTeamOneCode(rs.getString("team_one_code"));
		match.setTeamOneCaptain(rs.getInt("team_one_captain"));
		match.setTeamOneViceCaptain(rs.getInt("team_one_vice_captain"));
		match.setTeamTwo(rs.getInt("team_two"));
		match.setTeamTwoName(rs.getString("team_two_name"));
		match.setTeamTwoCode(rs.getString("team_two_code"));
		match.setTeamTwoCaptain(rs.getInt("team_two_captain"));
		match.setTeamTwoViceCaptain(rs.getInt("team_two_vice_captain"));
		match.setMatchDate(rs.getString("match_date"));
		match.setBattingFirst(rs.getInt("batting_first"));
		match.setTossWon(rs.getInt("toss_won"));
		match.setOvers(rs.getInt("overs"));
		match.setWinner(rs.getInt("winner"));
		match.setLocation(rs.getString("location"));
		match.setMatchType(rs.getString("match_type"));
		
		match.setT1total(rs.getInt("t1_total"));
		match.setT2total(rs.getInt("t2_total"));
		match.setT1balls(rs.getInt("t1_balls"));
		match.setT2balls(rs.getInt("t2_balls"));
		match.setT1wickets(rs.getInt("t1_wickets"));
		match.setT2wickets(rs.getInt("t2_wickets"));
		
		match.setT1_1total(rs.getInt("t1_1total"));
		match.setT2_1total(rs.getInt("t2_1total"));
		match.setT1_1balls(rs.getInt("t1_1balls"));
		match.setT2_1balls(rs.getInt("t2_1balls"));
		match.setT1_1wickets(rs.getInt("t1_1wickets"));
		match.setT2_1wickets(rs.getInt("t2_1wickets"));
		
		match.setT1_2total(rs.getInt("t1_2total"));
		match.setT2_2total(rs.getInt("t2_2total"));
		match.setT1_2balls(rs.getInt("t1_2balls"));
		match.setT2_2balls(rs.getInt("t2_2balls"));
		match.setT1_2wickets(rs.getInt("t1_2wickets"));
		match.setT2_2wickets(rs.getInt("t2_2wickets"));
		
		match.setT1byes(rs.getInt("t1_byes"));
		match.setT2byes(rs.getInt("t2_byes"));
		match.setT1lbyes(rs.getInt("t1_lbyes"));
		match.setT2lbyes(rs.getInt("t2_lbyes"));
		match.setT1penalty(rs.getInt("t1_penalty"));
		match.setT2penalty(rs.getInt("t2_penalty"));
		
		match.setT1_1byes(rs.getInt("t1_1byes"));
		match.setT2_1byes(rs.getInt("t2_1byes"));
		match.setT1_1lbyes(rs.getInt("t1_1lbyes"));
		match.setT2_1lbyes(rs.getInt("t2_1lbyes"));
		match.setT1_1penalty(rs.getInt("t1_1penalty"));
		match.setT2_1penalty(rs.getInt("t2_1penalty"));
		
		match.setT1_2byes(rs.getInt("t1_2byes"));
		match.setT2_2byes(rs.getInt("t2_2byes"));
		match.setT1_2lbyes(rs.getInt("t1_2lbyes"));
		match.setT2_2lbyes(rs.getInt("t2_2lbyes"));
		match.setT1_2penalty(rs.getInt("t1_2penalty"));
		match.setT2_2penalty(rs.getInt("t2_2penalty"));
		
		match.setT1_logo_file_path(rs.getString("t1_logo_file_path"));
		match.setT2_logo_file_path(rs.getString("t2_logo_file_path"));
		
		match.setManOfTheMatch(rs.getInt("MAN_OF_THE_MATCH"));
		match.setIsComplete(rs.getInt("IS_COMPLETE"));
		match.setIsLocked(rs.getInt("IS_LOCKED"));
		match.setIsAbandoned(rs.getInt("IS_ABANDONED"));
	
		match.setIsFollowon(rs.getInt("is_followon"));
		match.setIsTrump(rs.getInt("is_trump"));
		match.setAbandoneType(rs.getString("abandone_type"));
		match.setComment(rs.getString("COMMENT"));
		match.setLeagueId(rs.getInt("league"));
		match.setMaximumPlayers(rs.getInt("maximum_players"));
		match.setScorer(rs.getInt("scorer"));
		match.setUmpire1(rs.getString("umpire1"));
		match.setUmpire2(rs.getString("umpire2"));
		match.setNotificationSent(rs.getString("notification_sent"));
		match.setFow1(rs.getString("fow1"));
		match.setFow2(rs.getString("fow2"));
		match.setFow1_2(rs.getString("fow1_2"));
		match.setFow2_2(rs.getString("fow2_2"));
		match.setLastUpdatedDate(rs.getTimestamp("last_updated_date"));
		match.setLastUpdatedBy(rs.getInt("last_updated_by"));
		match.setLive_streaming_link(rs.getString("live_youtube_link"));
		match.setTimeSinceLastUpdate(CommonUtility.timeLapsedFromSeconds(rs.getLong("time_diff")));
		match.setDls(rs.getBoolean("is_dls"));
		match.setT2Target(rs.getInt("t2_target"));
		match.setR1ResAvailable(rs.getDouble("r1_res_available"));
		match.setT2RevisedOvers(rs.getInt("t2_revised_over"));
		match.setStatus(rs.getString("status"));
		match.setResult(match.getResult());

		return match;
	}
	
	private PlayerMatchDtoFB populateMatchFB(ResultSet rs) throws SQLException {
		
		PlayerMatchDtoFB match = new PlayerMatchDtoFB();
		
		match.setClubId(rs.getInt("club_id"));
		match.setClubName(rs.getString("club_name"));
		match.setSeriesType(rs.getString("series_type"));
		match.setSeriesName(rs.getString("series_name"));
		match.setMatchId(rs.getInt("match_id"));
		match.setTeamOne(rs.getInt("team_one"));
		match.setTeamOneName(rs.getString("team_one_name"));
		match.setTeamOneCode(rs.getString("team_one_code"));
		match.setTeamTwo(rs.getInt("team_two"));
		match.setTeamTwoName(rs.getString("team_two_name"));
		match.setTeamTwoCode(rs.getString("team_two_code"));
		match.setMatchDate(rs.getString("match_date"));	
		match.setWinner(rs.getInt("winner"));
		match.setMatchType(rs.getString("match_type"));
		match.setT1Goals(rs.getInt("t1_total"));
		match.setT2Goals(rs.getInt("t2_total"));
		match.setT1_logo_file_path(rs.getString("t1_logo_file_path"));
		match.setT2_logo_file_path(rs.getString("t2_logo_file_path"));
		match.setIsComplete(rs.getInt("IS_COMPLETE"));		
		match.setSeriesId(rs.getInt("league"));
		match.setLastUpdatedDate(rs.getTimestamp("last_updated_date"));
		match.setTimeSinceLastUpdate(CommonUtility.timeLapsedFromSeconds(rs.getLong("time_diff")));
		match.setStatus(rs.getString("status"));
		match.setPlayerId(rs.getInt("player_id"));
		match.setPlayerName(rs.getString("player_name"));
		match.setPlayerTeamId(rs.getInt("player_team_id"));
		match.setIncidentTime(rs.getString("mins_of_game"));

		return match;
	}


	public void updateMatchLiveScoreURL(long matchId, int clubId, String liveSteamURL, boolean isMultipleStreams) throws Exception {

		String query = "update matches set live_youtube_link = '"+liveSteamURL+"'  where match_id = " + matchId;
		
		if(isMultipleStreams) {
			query = "update matches set live_youtube_link = CONCAT(live_youtube_link,',"+liveSteamURL+"') where match_id = " + matchId;
		}

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}	
	}
	
	public void deleteMatchLiveScoreLink(long matchId, int fixtureId, int clubId) throws Exception {

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			
			conn.setAutoCommit(false);
			st = conn.createStatement();
			
			st.addBatch("update matches set live_youtube_link = ''  where match_id = " + matchId);
			st.addBatch("update summary.match_summary set live_youtube_link = ''  where match_id = " + matchId+" and club_id = "+clubId);
			st.addBatch("delete from mcc.live_score_feed where fixture_id = "+fixtureId+" and club_id = "+clubId);
			
			st.executeBatch();
			
			conn.commit();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}	
	}

	public Map<Integer, Integer> getInningsInAMatch(int clubid, int matchId) throws Exception {
		String query = "select innings_number, innings_id from innings WHERE match_id = ? ";
		Map<Integer, Integer> responseMap = new HashMap<Integer, Integer>();

		Connection conn = DButility.getReadConnection(clubid);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();

			while (rs.next()) {
				responseMap.put(rs.getInt(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return responseMap;
	}
	
	public List<MatchDto> getPreviousEncounters(int clubId, int team1Id, int team2Id, String fixtureDate) throws Exception {

		List<MatchDto> matchList = new ArrayList<MatchDto>();;

		String query = MATCH_QUERY+ " and team_one IN (?,?) AND team_two IN (?,?)";
		
		if(!CommonUtility.isNullOrEmpty(fixtureDate)) {
			query += " and m.match_date < STR_TO_DATE(?,  '%m/%d/%Y') ";
		}
		
		query += " ORDER BY match_date DESC LIMIT 3 ";

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);

			int index = 1;

			st.setInt(index++, team1Id);
			st.setInt(index++, team2Id);
			st.setInt(index++, team1Id);
			st.setInt(index++, team2Id);
			
			if(!CommonUtility.isNullOrEmpty(fixtureDate)) {
				st.setString(index++, fixtureDate);
			}

			rs = st.executeQuery();

			while (rs.next()) {				
				matchList.add(populateMatch(rs,clubId));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchList;
	}
	
	public List<String> getRecentFormForTeam(int clubId, int teamId, String fixtureDate, int limit) throws Exception {
		
		List<String> recentForms = new ArrayList<String>();
		
		String query ="SELECT IF(winner>0, IF(winner = ?, 'W','L'), 'O') win_loss FROM matches "
				+ "WHERE is_complete = 1 and match_date <= STR_TO_DATE(?,  '%m/%d/%Y') "
				+ " AND (team_one = ? OR team_two = ?) ORDER BY match_date DESC LIMIT ?  ";
		
		// get data before fixture date

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setInt(index++, teamId);			
			st.setString(index++, fixtureDate);
			st.setInt(index++, teamId);
			st.setInt(index++, teamId);
			st.setInt(index++, limit);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				recentForms.add(rs.getString("win_loss"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return recentForms;
	}
	
	protected List<LiveScoreActionsDto> liveScoreActionsData(int matchId, int clubId) throws Exception {

		String query = "select fixture_id,match_id,over,ball,innings_number,actions,created_date  FROM live_score_actions where match_id = ?";

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<LiveScoreActionsDto> actionsList = new ArrayList<LiveScoreActionsDto>();;
		
		try {
			pst = conn.prepareStatement(query);
			if (matchId > 0) {
				pst.setInt(1, matchId);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				LiveScoreActionsDto actionDto = new LiveScoreActionsDto();
				
				actionDto.setMatchId(rs.getInt("match_id"));
				actionDto.setFixtureId(rs.getInt("fixture_id"));
				actionDto.setOver(rs.getInt("over"));
				actionDto.setBall(rs.getInt("ball"));
				actionDto.setInningsNumber(rs.getInt("innings_number"));
				actionDto.setActions(rs.getString("actions"));
				actionDto.setCreatedDate(rs.getString("created_date"));				
				
				actionsList.add(actionDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return actionsList;
	}

	public Map<Integer, int[]> getBattingRunsBreakdownForGame(int clubId, int matchId) throws Exception {

		Map<Integer, int[]> runsBreakDown = new HashMap<>();
		String query = ""
				+ "SELECT "
				+ "innings_id, "
				+ "innings_number, "
				+ "runs, "
				+ "Count(runs) AS count "
				+ "From ( "
				+ "select ball.innings_id, innings.innings_number, "
				+ "CASE "
				+ "when ball_type = 'Good Ball' then ball.runs "
				+ "when ball_type = 'No Ball of Bat' then ball.runs - ball.no_ball_custom_runs "
				+ "End as runs "
				+ "FROM ball "
				+ "join innings on innings.innings_id = ball.innings_id "
				+ "WHERE "
				+ "ball.innings_id in (select innings_id from innings WHERE match_id = ?) "
				+ "AND (ball.ball_type = 'Good Ball' or ball.ball_type = 'No Ball of Bat')) tb "
				+ "GROUP BY runs, tb.innings_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();

			while (rs.next()) {
				int inningsNumber = rs.getInt("innings_number");
				int runs = rs.getInt("runs");
				int count = rs.getInt("count");

				if (runsBreakDown.containsKey(runs)) {
					runsBreakDown.get(runs)[inningsNumber - 1] = count;
				} else {
					int[] run = new int[2];
					run[inningsNumber - 1] = count;
					runsBreakDown.put(runs, run);
				}
			}

			return runsBreakDown;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	public Map<String, int[]> getBowlingOutTypesForGame(int clubId, int matchId) throws Exception {

		Map<String, int[]> outTypeBreakDown = new HashMap<>();
		String query = ""
				+ "SELECT "
				+ "ball.innings_id, "
				+ "innings.innings_number, "
				+ "ball.out_method, "
				+ "Count(ball.out_method) AS count "
				+ "FROM ball "
				+ "join innings on innings.innings_id = ball.innings_id "
				+ "WHERE "
				+ "ball.innings_id in (select innings_id from innings WHERE match_id = ?) "
				+ "AND ball.out_method <> '' "
				+ "GROUP BY ball.out_method, innings_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();

			while (rs.next()) {
				int inningsNumber = rs.getInt("innings_number");
				String outMethod = rs.getString("out_method");
				int count = rs.getInt("count");

				if (outTypeBreakDown.containsKey(outMethod)) {
					outTypeBreakDown.get(outMethod)[inningsNumber - 1] = count;
				} else {
					int[] run = new int[4];
					run[inningsNumber - 1] = count;
					outTypeBreakDown.put(outMethod, run);
				}
			}

			return outTypeBreakDown;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	public Map<String, List<Integer>> getRunsInGroupOfOvers(int clubId, int matchId) throws Exception {
		Map<Integer, Integer> innings = MatchesFactory.getInningsInAMatch(clubId, matchId);

		Map<String, List<Integer>> response = new HashMap<>();
		String query = ""
				+ "SELECT "
				+ "over, "
				+ "sum(ball.runs) AS runs "
				+ "FROM ball "
				+ "WHERE "
				+ "ball.innings_id = ? "
				+ "AND ball.ball_type <> 'Auto Comment Ball' "
				+ "GROUP BY over";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			for (Integer inningsNumber: innings.keySet()) {

				st.setInt(1, innings.get(inningsNumber));
				rs = st.executeQuery();

				Map<Integer, Integer> runsPerOver = new HashMap<>();

				while (rs.next()) {
					runsPerOver.put(rs.getInt(1), rs.getInt(2));
				}

				List<List<Integer>> lists = Lists.partition(new ArrayList<>(runsPerOver.keySet()), 5);

				for (List<Integer> overGroup : lists) {
					String key = overGroup.get(0)+1 + "-" + (overGroup.get(0) + 5);
					List<Integer> runsForOverGroup = overGroup.stream().map(over -> runsPerOver.get(over)).collect(Collectors.toList());
					Integer sum = runsForOverGroup.stream().mapToInt(Integer::intValue).sum();
					if(response.containsKey(key)) {
						response.get(key).set(inningsNumber - 1, sum);
					} else {
						List<Integer> sums = new ArrayList<>();
						sums.add(0);
						sums.add(0);
						sums.set(inningsNumber - 1, sum);
						response.put(key, sums);
					}
				}
			}

			return response;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	public List<List<BowlingOverComparisonDTO>> getBowlingOverByOverComparison(int clubId, int matchId) throws Exception {
		List<List<BowlingOverComparisonDTO>> response = new ArrayList<>();

		String query = ""
				+ "SELECT "
				+ "over, "
				+ "innings_number, "
				+ "sum(runs) AS totalRuns, "
				+ "sum(CASE WHEN wicket_taker_1 THEN 1 ELSE 0 END) as wicketsInOver " // use out-person instead of wicket taker 1
				+ "FROM ball "
				+ "join innings on innings.innings_id = ball.innings_id "
				+ "WHERE "
				+ "ball.innings_id in (select innings_id from innings WHERE match_id = ?) "
				+ "GROUP BY over, ball.innings_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();

			int[] score = new int[2];
			int[] wickets = new int[2];
			while (rs.next()) {
				int inningsNumber = rs.getInt("innings_number");
				score[inningsNumber - 1] += rs.getInt("totalRuns");
				wickets[inningsNumber - 1] += rs.getInt("wicketsInOver");

				BowlingOverComparisonDTO overData = new BowlingOverComparisonDTO();
				overData.setOverNumber(rs.getInt("over") + 1);
				overData.setRunsInTheOver(rs.getInt("totalRuns"));
				overData.setWicketsInTheOver(rs.getInt("wicketsInOver"));
				overData.setScoreAfterOver(score[inningsNumber - 1] + "/" + wickets[inningsNumber - 1]);

				if (response.size() < rs.getInt("over") + 1) {
					List<BowlingOverComparisonDTO> overs = new ArrayList<>();
					overs.add(inningsNumber - 1, overData);
					response.add(rs.getInt("over"), overs);
				} else {
					response.get(rs.getInt("over")).add(inningsNumber - 1, overData);
				}
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return  response;

	}

	public Map<Integer, double[]> getBattingBoundryPercentagePerBall(int clubId, int matchId) throws Exception {

		Map<Integer, int[]> responseCounts = new HashMap<>();
		Map<Integer, double[]> response = new HashMap<>();
		String query = ""
				+ "SELECT "
				+ "ball, "
				+ "ball.innings_id, "
				+ "count(ball) as count "
				+ "FROM ball "
				+ "WHERE "
				+ " ball.innings_id in( SELECT innings_id FROM innings WHERE match_id = ?) and(ball.is_four OR ball.is_six) "
				+ "GROUP BY innings_id, ball";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			rs = st.executeQuery();

			while (rs.next()) {
				int inningsId = rs.getInt("innings_id");
				int ballNumber = rs.getInt("ball");
				int count = rs.getInt("count");
				
				if(ballNumber<=6) {
					if (responseCounts.containsKey(inningsId)) {
						responseCounts.get(inningsId)[ballNumber - 1] = count;
					} else {
						int[] counts = new int[6];
						counts[ballNumber - 1] = count;
						responseCounts.put(inningsId, counts);
					}
				}
			}
			for(int key: responseCounts.keySet()) {
				response.put(key, calculatePercentageGivenAListOfNumbers(responseCounts.get(key)));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return response;
	}

	private double[] calculatePercentageGivenAListOfNumbers(int[] numbers) {
		int sum = Arrays.stream(numbers).sum();
		return Arrays.stream(numbers).mapToDouble(number -> ((float)number/(float)sum) * 100.0).toArray();
	}
	
	protected Pair getLiveMatchesofTCSCC(boolean test) throws Exception{
		Pair pair = new Pair();
		String query = "select match_id, 23026 club_id from club23026.matches where "+(test?"1=1":"is_complete <> 1")+" union\r\n" + 
				"select m.match_id, 1809 club_id from club1809.matches m, club1809.fixtures f where f.match_id = m.match_id and f.ground_id  =16 and is_complete <> 1 limit 1 ";
		

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			
			rs = st.executeQuery();

			while (rs.next()) {
				pair.setValues1(rs.getString(1));
				pair.setValues2(rs.getString(2));
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return pair;
		
	}

	public void insertOutPlayerForMatchFB(int clubId, int matchId, int playerId, int teamId) throws Exception {

		String query = "INSERT INTO match_player_team(match_id, player_id, team_id, is_substitute) VALUES (?,?,?,2)";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);

			int index = 1;

			st.setInt(index++, matchId);
			st.setInt(index++, playerId);
			st.setInt(index++, teamId);

			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

	}
	
	public void updateSubstitutePlayerForMatchFB(int clubId, int matchId, int playerId) throws Exception {

		String query = "update match_player_team set is_substitute = 1 where match_id = ? and player_id = ?";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);

			int index = 1;

			st.setInt(index++, matchId);
			st.setInt(index++, playerId);

			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

	}

}
