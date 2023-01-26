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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.MatchDto;
import com.cricket.promotions.CareerStatsSummaryDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author MadhuKiran K Rajuladevi
 * 
 */
public class CareerStatsSummaryDAO {
	
	static Logger log = LoggerFactory.getLogger(CareerStatsSummaryDAO.class);
	private static ExecutorService executor = Executors.newFixedThreadPool(5);
	
	protected CareerStatsSummaryDAO() {
	}

	protected List<CareerStatsSummaryDto> getCareerStatsSummaryList(String playerName, String sortBy, Date from_date, Date to_date) throws Exception {
		List<CareerStatsSummaryDto> careerStatsSummary = new ArrayList<CareerStatsSummaryDto>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String fromDateStr = null, toDateStr = null;
		String sortByStr = "total_runs";
		if(sortBy!=null && !sortBy.isEmpty() && !sortBy.equalsIgnoreCase("r")) {
			if(sortBy.equalsIgnoreCase("w")) {
				sortByStr="total_wickets";
			}else if(sortBy.equalsIgnoreCase("c")) {
				sortByStr="total_catches";
			}
		}
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
		if(playerName != null && !playerName.isEmpty() && !playerName.isBlank() && !playerName.trim().equalsIgnoreCase("All Players")) {
			queryCondition = " and LOWER(player_name) like LOWER(?)";
		}
		//Currently Limiting the max records to 300 for performance.
		String query =  " SELECT max(club_id) as club_id, player_id, player_name, sum(distinct_match_count) as distinct_match_count, \r\n" + 
						" sum(total_batting_innings) as total_batting_innings ,sum(total_bowling_innings) as total_bowling_innings , \r\n" + 
						" sum(total_runs) as total_runs,sum(total_balls_faced) as total_balls_faced, max(highest_score) as highest_score, sum(hundread_scored) as hundread_scored, \r\n" + 
						" sum(fifty_scored) as fifty_scored, sum(total_not_outs) as total_not_outs, sum(total_fours) as total_fours, sum(total_sixers) as total_sixers, \r\n" + 
						" sum(total_balls_bowled) as total_balls_bowled, sum(total_runs_given) as total_runs_given, \r\n" + 
						" sum(total_wicket) as total_wickets, sum(total_catches) as total_catches, sum(total_maidens) as total_maidens, sum(total_direct_indirect_ro) as total_runouts, \r\n" + 
						" DATE_FORMAT(max(last_updated),'%Y-%m-%d') as last_updated_date \r\n" + 
						" FROM summary.career_stats_summary \r\n" + 
						" where  distinct_match_count > 0 and last_updated>=str_to_date(?,'%Y-%m-%d')  and last_updated<=DATE_ADD(str_to_date(?,'%Y-%m-%d'),INTERVAL 1 DAY) "+ queryCondition + 
						" group by player_id \r\n" + 
						" order by "+sortByStr+" desc limit 300";


		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			pst = conn.prepareStatement(query);
			int index =1;	
		
			pst.setString(index++, fromDateStr);
			pst.setString(index++, toDateStr);
			
			if(playerName != null && !playerName.isEmpty() && !playerName.isBlank() && !playerName.trim().equalsIgnoreCase("All Players")) {
				pst.setString(index++, "%"+playerName+"%");
			}
			
			//log.info("Query Execution SQL: "+pst.toString());

			rs = pst.executeQuery();
			while (rs.next()) {
				CareerStatsSummaryDto dto = new CareerStatsSummaryDto();
				dto.setPlayerId(rs.getInt("player_id"));
				dto.setClubId(rs.getInt("club_id"));
				dto.setPlayerName(rs.getString("player_name"));
				dto.setTotalMatches(CommonUtility.stringToInt(rs.getString("distinct_match_count")));
				dto.setTotalBattingInnings(CommonUtility.stringToInt(rs.getString("total_batting_innings")));
				dto.setTotalBowlingInnings(CommonUtility.stringToInt(rs.getString("total_bowling_innings")));
				dto.setTotalRunsScored(CommonUtility.stringToInt(rs.getString("total_runs")));
				dto.setTotalBallsFaced(CommonUtility.stringToInt(rs.getString("total_balls_faced")));
				dto.setHighestRuns(CommonUtility.stringToInt(rs.getString("highest_score")));
				dto.setTotalHundreads(CommonUtility.stringToInt(rs.getString("hundread_scored")));
				dto.setTotalFifties(CommonUtility.stringToInt(rs.getString("fifty_scored")));
				dto.setTotalNotOuts(CommonUtility.stringToInt(rs.getString("total_not_outs")));
				dto.setTotalFours(CommonUtility.stringToInt(rs.getString("total_fours")));
				dto.setTotalSixers(CommonUtility.stringToInt(rs.getString("total_sixers")));
				dto.setTotalBallsBowled(CommonUtility.stringToInt(rs.getString("total_balls_bowled")));
				dto.setTotalRunsGiven(CommonUtility.stringToInt(rs.getString("total_runs_given")));
				dto.setTotalWickets(CommonUtility.stringToInt(rs.getString("total_wickets")));
				dto.setTotalCatches(CommonUtility.stringToInt(rs.getString("total_catches")));
				dto.setTotalMaidens(CommonUtility.stringToInt(rs.getString("total_maidens")));
				dto.setTotalRunOuts(CommonUtility.stringToInt(rs.getString("total_runouts")));
				dto.setLastUpdated(dateFormat.parse(rs.getString("last_updated_date")));

				String[] datepart = rs.getString("last_updated_date").split("-");
				dto.setLastUpdatedStr(datepart[1]+"/"+datepart[2]+"/"+datepart[0]);
											
				careerStatsSummary.add(dto);
		 }
			

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return careerStatsSummary;
	}

	protected void updateCareerStatsSummary(int clubID, MatchDto match) throws Exception {
		
		executor.submit(new Runnable() {
			@Override
			public void run() {
				
				try {
										
					//Iterate for all players of the match
					for(Integer playerId : match.getAllPlayersForTheMatch()) {
						
						List<Integer> clubIDs = PlayerFactory.getClubIdsForPlayer(playerId);
								
						
						
						for (Integer currClubID : clubIDs) {
							
							if(currClubID<=0) {
								continue;
							}
							
							
							// For each player in that particular club
							// Delete and insert the latest player records for that particular club if exists
							String delQuery = "delete from summary.career_stats_summary where player_id=? and club_id=?";
							
							// For a particular club, total stats of the player.
							String insertQuery="INSERT INTO summary.career_stats_summary  \r\n" + 
									"(club_id, player_id, player_name, distinct_match_count, total_batting_innings,total_bowling_innings,\r\n" + 
									"total_runs, total_balls_faced, highest_score, hundread_scored, fifty_scored, total_not_outs, total_fours, total_sixers, \r\n" + 
									"total_balls_bowled, total_runs_given, total_wicket, total_catches,total_maidens,total_direct_indirect_ro)  \r\n" + 
									"  \r\n" + 
									"select ? as club_id , p.player_id , concat(p.f_name,' ',p.l_name) as player_name,  \r\n" + 
									"count(distinct pss.match_id ) as distinct_match_count,  SUM(IF(pss.how_out is null, 0, 1)) total_batting_innings, \r\n" + 
									"SUM(IF(pss.balls_bowled = 0, 0, 1)) total_bowling_innings, \r\n" + 
									"sum(pss.runs_scored) as total_runs, sum(pss.balls_faced) as total_balls_faced,  \r\n" + 
									"MAX(pss.runs_scored) as highest_score, SUM(IF(pss.runs_scored > 99, 1, 0)) hundread_scored,  \r\n" + 
									"SUM(IF(pss.runs_scored > 49,  IF(pss.runs_scored < 75, 1, 0),  0)) fifty_scored,  \r\n" + 
									"sum(IF((trim(pss.how_out) = '' or pss.how_out = 'rt'), 1, 0)) as total_not_outs,  \r\n" + 
									"sum(pss.fours ) as total_fours,sum(pss.sixers ) as total_sixers, \r\n" + 
									"SUM(pss.balls_bowled) as total_balls_bowled, \r\n" + 
									"SUM(pss.runs_given) total_runs_given,\r\n" + 
									"sum(pss.wickets ) as total_wicket,   \r\n" + 
									" sum(pss.catches ) as total_catches,SUM(pss.maidens) total_maidens, sum(pss.direct_ro + pss.indirect_ro) as total_direct_indirect_ro\r\n" + 
									"from club?.player_statistics_summary pss   \r\n" + 
									"inner join mcc.player p on p.player_id =pss.player_id   \r\n" + 
									"where pss.match_type <> 'p' and pss.player_id = ? ";
							Connection conn = null;
							PreparedStatement delPst = null;
							PreparedStatement insertPst = null;
							try {
							conn = DButility.getDefaultConnection();
							delPst = conn.prepareStatement(delQuery);
							int index1 =1;	
						
							delPst.setInt(index1++, playerId);
							delPst.setInt(index1++, currClubID);							
							
							delPst.executeUpdate();
							
							insertPst = conn.prepareStatement(insertQuery);
							int index2 =1;	
						
							insertPst.setInt(index2++, currClubID);
							insertPst.setInt(index2++, currClubID);
							insertPst.setInt(index2++, playerId);
														
							
							int execInsert = insertPst.executeUpdate();
							
							//if(execInsert>0) {
							//	log.warn("Player Career Stats have been Updated !!!");
							//}			
							
							} catch (SQLException e) {
								throw new Exception(e.getMessage());
							} finally {
								if(delPst!=null) {
									DButility.closeStatement(delPst);
								}
								if(insertPst!=null) {
									DButility.closeStatement(insertPst);
								}
								if(conn!=null) {
									DButility.closeConnection(conn);
								}
								
							}
						}
						
					}
					
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

			}

		});

		
	}
	
}
