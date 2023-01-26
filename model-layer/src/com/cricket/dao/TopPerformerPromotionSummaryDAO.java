/*
 * Created on July 31, 2021
 *
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.promotions.TopPerformerSummaryDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author MadhuKiran K Rajuladevi
 * 
 */
public class TopPerformerPromotionSummaryDAO {
	
	static Logger log = LoggerFactory.getLogger(TopPerformerPromotionSummaryDAO.class);
	protected TopPerformerPromotionSummaryDAO() {
	}

	protected List<TopPerformerSummaryDto> getTopPerformersSummaryList(int clubID, Date from_date, Date to_date) throws Exception {
		List<TopPerformerSummaryDto> topPerformers = new ArrayList<TopPerformerSummaryDto>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String fromDateStr = null, toDateStr = null;
		if(from_date!=null) {
			fromDateStr = dateFormat.format(from_date);  
		}
		if(to_date!=null) {
			toDateStr = dateFormat.format(to_date);  
		}  
		
		if(from_date!=null && to_date!=null && from_date.after(to_date)) {
			throw new Exception("From Date is after the To Date.");
		}
		
		String queryCondition = "";
		if(clubID>0) {
			queryCondition = "tps.club_id=? and";
		}
		//Currently Limiting the max records to 100 for performance.
		String query = "select tps.player_id , (select CONCAT(p.f_name,' ',p.l_name) as player_name from mcc.player p where player_id =tps.player_id) as player_name, tps.country as country, \r\n" + 
						"tps.club_id, tps.club_name , tps.series_id , tps.match_id , tps.series_name , tps.match_type_name , tps.t1_name , tps.t2_name , tps.total_points , \r\n" + 
						"tps.runs_scored , tps.balls_faced , tps.sixers , tps.fours , ROUND((tps.runs_scored / tps.balls_faced) *100,1) as batting_strike_rate, \r\n" +  // -- tps.batting_points ,
						" CONCAT(floor(coalesce(balls_bowled,0) / 6),'.',(coalesce(balls_bowled,0) % 6)) as overs_bowled, tps.wickets , tps.maidens, tps.match_date, tps.runs_given  \r\n" + //-- tps.bowling_points ,
						"from summary.top_performers_summary tps where "+queryCondition+
						" tps.match_date >= str_to_date(?,'%Y-%m-%d')  and tps.match_date <= DATE_ADD(str_to_date(?,'%Y-%m-%d'),INTERVAL 1 DAY) and player_id <> '-1' " +
						" and tps.total_points>0  order by tps.total_points desc limit 100";
		

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubID, false);
			pst = conn.prepareStatement(query);
			int index =1;
				if(clubID>0) {
					pst.setInt(index++, clubID);
				}
				pst.setString(index++, fromDateStr);
				pst.setString(index++, toDateStr);
				
				rs = pst.executeQuery();
				while (rs.next()) {
					TopPerformerSummaryDto dto = new TopPerformerSummaryDto();
					dto.setPlayerId(rs.getInt("player_id"));
					dto.setPlayerName(rs.getString("player_name"));
					dto.setClubId(CommonUtility.stringToInt(rs.getString("club_id")));				
					dto.setClubName(rs.getString("club_name"));
					dto.setSeriesId(CommonUtility.stringToInt(rs.getString("series_id")));
					dto.setMatchId(CommonUtility.stringToInt(rs.getString("match_id")));
					dto.setSeriesName(rs.getString("series_name"));
					dto.setMatchTypeName(rs.getString("match_type_name"));
					dto.setT1_name(rs.getString("t1_name"));
					dto.setT2_name(rs.getString("t2_name"));	
					dto.setTotal_points(CommonUtility.stringToInt(rs.getString("total_points")));
					dto.setRunsScored(CommonUtility.stringToInt(rs.getString("runs_scored")));
					dto.setBallsFaced(CommonUtility.stringToInt(rs.getString("balls_faced")));
					dto.setSixers(CommonUtility.stringToInt(rs.getString("sixers")));
					dto.setFours(CommonUtility.stringToInt(rs.getString("fours")));
					dto.setBatting_strike_rate(CommonUtility.stringToFloat(rs.getString("batting_strike_rate")));
					dto.setOversBowled(rs.getString("overs_bowled"));
					dto.setWickets(CommonUtility.stringToInt(rs.getString("wickets")));
					dto.setMaidens(CommonUtility.stringToInt(rs.getString("maidens")));
					dto.setCountry(rs.getString("country"));
					dto.setRunsGiven(CommonUtility.stringToInt(rs.getString("runs_given")));
					
					dto.setMatch_date(dateFormat.parse(rs.getString("match_date")));
					String[] datepart = rs.getString("match_date").split("-");
					dto.setMatchDateStr(datepart[1]+"/"+datepart[2]+"/"+datepart[0]);
												
					topPerformers.add(dto);
			 }
			

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return topPerformers;
	}

	protected void insertTopPerformerSummary(int clubID, int matchID, int no_of_top_performers_per_match) throws Exception {

		String clubSchema="";
		if(clubID==1) {
			clubSchema = "FROM mcc.player_statistics_summary stat \r\n";
		}else {
			clubSchema = "FROM club?.player_statistics_summary stat \r\n";
		}
		
		String query = "INSERT INTO summary.top_performers_summary\r\n" + 
				"(player_id, team_id, series_id, match_id, club_id, match_type, match_date, club_name, series_name, match_type_name,\r\n" + 
				"country,t1_id, t2_id, t1_name, t2_name, total_points, \r\n" + 
				"batting_points, runs_scored, balls_faced, fours, sixers, bowling_points, balls_bowled, runs_given, wickets, maidens, \r\n" + 
				"fielding_points, direct_ro, catches, stumpings, man_of_the_match)\r\n" + 
				"\r\n" + 
				"SELECT stat.player_id ,stat.team_id ,stat.series_id ,stat.match_id , summary.club_id as club_id,\r\n" + 
				"stat.match_type , stat.match_date , summary.club_name, summary.series_name,summary.match_type as match_type_name,\r\n" + 
				"summary.country , summary.t1_id , summary.t2_id, summary.t1_name, summary.t2_name , \r\n" + 
				"(stat.bowling_points+stat.batting_points+stat.fielding_points) AS total_points,\r\n" + 
				"stat.batting_points ,stat.runs_scored ,stat.balls_faced ,stat.fours ,stat.sixers ,\r\n" + 
				"stat.bowling_points ,stat.balls_bowled ,stat.runs_given ,stat.wickets ,stat.maidens ,\r\n" + 
				"stat.fielding_points ,stat.direct_ro ,stat.catches ,stat.stumpings ,\r\n" + 
				"stat.man_of_the_match \r\n" + 
				clubSchema +
				"INNER JOIN summary.match_summary summary ON summary.club_id=? AND summary.match_id=stat.match_id\r\n" + 
				"where summary.match_id=? and (stat.bowling_points+stat.batting_points+stat.fielding_points)>0 \r\n" + 
				"GROUP BY stat.player_id,stat.team_id,stat.series_id,stat.match_id,5\r\n" + 
				"ORDER BY total_points DESC \r\n" + 
				"LIMIT ? ";
				

		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DButility.getConnection(clubID);
			pst = conn.prepareStatement(query);
			if(clubID==1) {
				pst.setInt(1, clubID);
				pst.setInt(2, matchID);
				pst.setInt(3, no_of_top_performers_per_match);
			}else {
				pst.setInt(1, clubID);
				pst.setInt(2, clubID);
				pst.setInt(3, matchID);
				pst.setInt(4, no_of_top_performers_per_match);
			}

			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
	
	protected void insertTopPerformerSummaryWithConn(Connection conn, int clubID, int matchID, int no_of_top_performers_per_match) throws Exception {

		String clubSchema="";
		if(clubID==1) {
			clubSchema = "FROM mcc.player_statistics_summary stat \r\n";
		}else {
			clubSchema = "FROM club?.player_statistics_summary stat \r\n";
		}
		
		String query = "INSERT INTO summary.top_performers_summary\r\n" + 
				"(player_id, team_id, series_id, match_id, club_id, match_type, match_date, club_name, series_name, match_type_name,\r\n" + 
				"country,t1_id, t2_id, t1_name, t2_name, total_points, \r\n" + 
				"batting_points, runs_scored, balls_faced, fours, sixers, bowling_points, balls_bowled, runs_given, wickets, maidens, \r\n" + 
				"fielding_points, direct_ro, catches, stumpings, man_of_the_match)\r\n" + 
				"\r\n" + 
				"SELECT stat.player_id ,stat.team_id ,stat.series_id ,stat.match_id , summary.club_id as club_id,\r\n" + 
				"stat.match_type , stat.match_date , summary.club_name, summary.series_name,summary.match_type as match_type_name,\r\n" + 
				"summary.country , summary.t1_id , summary.t2_id, summary.t1_name, summary.t2_name , \r\n" + 
				"(stat.bowling_points+stat.batting_points+stat.fielding_points) AS total_points,\r\n" + 
				"stat.batting_points ,stat.runs_scored ,stat.balls_faced ,stat.fours ,stat.sixers ,\r\n" + 
				"stat.bowling_points ,stat.balls_bowled ,stat.runs_given ,stat.wickets ,stat.maidens ,\r\n" + 
				"stat.fielding_points ,stat.direct_ro ,stat.catches ,stat.stumpings ,\r\n" + 
				"stat.man_of_the_match \r\n" + 
				clubSchema +
				"INNER JOIN summary.match_summary summary ON summary.club_id=? AND summary.match_id=stat.match_id\r\n" + 
				"where summary.match_id=? and (stat.bowling_points+stat.batting_points+stat.fielding_points)>0 \r\n" + 
				"GROUP BY stat.player_id,stat.team_id,stat.series_id,stat.match_id,5\r\n" + 
				"ORDER BY total_points DESC \r\n" + 
				"LIMIT ? ";
				

		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			if(clubID==1) {
				pst.setInt(1, clubID);
				pst.setInt(2, matchID);
				pst.setInt(3, no_of_top_performers_per_match);
			}else {
				pst.setInt(1, clubID);
				pst.setInt(2, clubID);
				pst.setInt(3, matchID);
				pst.setInt(4, no_of_top_performers_per_match);
			}
			
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst);
		}

	}

	protected void deleteTopPerformerSummary(int clubID, int matchID) throws Exception {
		
		String query ="delete from summary.top_performers_summary where  match_id =? and club_id =? ";
		
				
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DButility.getConnection(clubID);
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, matchID);
			pst.setInt(2, clubID);	
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
	
	protected void populateTopPerformerSummary() throws Exception {
		//Populate the Top Performers Summary Table with last 1 year top performers per match data
		Connection conn = null;
		String query = "select club_id , match_id from summary.match_summary where  iscomplete = true and match_date>DATE_SUB(CURRENT_DATE() , INTERVAL 1 YEAR) ";
		PreparedStatement pst = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				new TopPerformerPromotionSummaryDAO().insertTopPerformerSummaryWithConn(conn,CommonUtility.stringToInt(rs.getString("club_id")),CommonUtility.stringToInt(rs.getString("match_id")),1);			
			}
		}catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
		
	}
}
