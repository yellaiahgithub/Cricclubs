package com.cricket.utility;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.AuditPlayerMergeFactory;
import com.cricket.dto.AuditPlayerMergeDto;

@SuppressWarnings("unused")
public class UnMergePlayers {
	
	public static void main(String[] args) throws Exception {

		System.out.println("UnMerge Program Start...");		
		
		
		//Primary profile means[from-data] which has player information
		int primaryPlayerId = 2968305;
		//Secondary profile means[To data] ,primary player information need to transfer to secondary player. 
		int secondaryPlayerId = 2422096;
		boolean isPlayerExists = false;		
		boolean isUserExists = false;		
		Boolean success = false;
		
		List<AuditPlayerMergeDto> dtoList = new ArrayList<AuditPlayerMergeDto>();
		
		if(primaryPlayerId > 0 && secondaryPlayerId > 0) {
			dtoList = AuditPlayerMergeFactory.getPlayerMergeAuditData(primaryPlayerId, secondaryPlayerId);				
		}	
		if (dtoList != null && dtoList.size() > 0) {
			
			for (AuditPlayerMergeDto dto : dtoList) {
				if (!isPlayerExists && !isUserExists) {
					Connection conn = null;					
					PreparedStatement pst = null;
					ResultSet rs = null;
					String query = null;
					
					try {
						conn = DButility.getDefaultConnection();
						
						//check if player exists
						try {
							query = "select 1 from mcc.player where player_id = " + dto.getSecondaryPlayerId();
							pst = conn.prepareStatement(query);
							rs = pst.executeQuery();
							while (rs!= null && rs.next()) {
								isPlayerExists = true;
							}
						} catch (Exception e) {
							System.out.println("Secondary Player not found");
						} 
						//check if user exists
						
						try {
							query = "select 1 from mcc.user where user_id = " + dto.getSecondaryUserId();
							pst = conn.prepareStatement(query);
							rs = pst.executeQuery();
							while (rs.next()) {
								isUserExists = true;
							}
						} catch (Exception e) {
							System.out.println("Secondary User not found");
						} finally {
							DButility.dbCloseAll(conn, pst, rs);
						}
						
					}catch (Exception e) {
						throw new Exception(e);
					}finally {
						DButility.closeConnection(conn);
					}
				}
				
				System.out.println("UnMerge start for player "+dto.getSecondaryPlayerId()+" and for the club "+dto.getSecondaryClubId());
				
				success = UnMerge(dto,isPlayerExists,isUserExists);
				
				if(success) {					
					isPlayerExists = true;
					isUserExists = true;
					
					System.out.println("UnMerge successfull for player "+dto.getSecondaryPlayerId()+" and for the club "+dto.getSecondaryClubId());					
				}else {
					System.out.println("UnMerge not successfull for player "+dto.getSecondaryPlayerId()+" and for the club "+dto.getSecondaryClubId());
				}
			}
		}else {
			System.out.println("UnMerge Data Not Found ");
		}
		System.out.println("UnMerge Program End!! ");
	}	
	
	public static boolean UnMerge(AuditPlayerMergeDto dto, boolean isPlayerExists, boolean isUserExists) throws Exception {
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;
		
		int clubId = dto.getSecondaryClubId(); 
		int master = dto.getSecondaryPlayerId();
		int secondary = dto.getPrimaryPlayerId();
		
		boolean isPlayerInClub = false;		
		boolean isUserInClub = false;
		
		try {
			conn = DButility.getConnection(clubId);
		}catch (Exception e) {
			throw new Exception(e);
		}		
		try {
			
			try {
				query = "select 1 from mcc.player_club where club_id = ? and player_id = ?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, clubId);
				pst.setInt(2, master);
				rs = pst.executeQuery();
				while (rs.next()) {
					isPlayerInClub = true;
				}
			} catch (Exception e) {
				System.out.println("Player Not found in club"+clubId);
			} 
			
			try {
				query = "select 1 from mcc.user_club where club_id = ? and user_id = ?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, clubId);
				pst.setInt(2, dto.getSecondaryUserId());
				rs = pst.executeQuery();
				while (rs.next()) {
					isUserInClub = true;
				}
			} catch (Exception e) {
				System.out.println("User Not found in club"+clubId);
			} 
			
			conn.setAutoCommit(false);
			st = conn.createStatement();
			
			if(!isPlayerExists) {
				st.addBatch("insert into mcc.player(player_id, l_name, f_name, batting_style, phone, address, " 
						+ " email, profile_description, nickname, bowling_style, date_of_birth, playing_role, " 
						+ "  src_player_id, profilepic_file_path)"
						+" select player_id, l_name, f_name, batting_style, phone, address, email, profile_description, "
						+ " nickname, bowling_style, date_of_birth, playing_role, src_player_id, " 
						+ " profilepic_file_path from mcc.player_audit where player_id = "+ master+" order by change_id desc limit 1");				
			}			
			if(!isUserExists) {
				st.addBatch("insert into mcc.user(user_id, user_name, password, email, phone, address, city, state, "
						+ " postal_code, is_active, player_id, f_name, l_name, umpire_id, token, last_access_date) "
						+ " select user_id, user_name, password, email, phone, address, city, state, postal_code, "
						+ " is_active, player_id, f_name, l_name, umpire_id, token, last_access_date from mcc.user_audit "
						+ " where user_id = "+  dto.getSecondaryUserId() +" order by change_id desc limit 1");
				
				//assign player id to user
				st.addBatch("update mcc.user set player_id = "+ master+" where user_id =  "+ dto.getSecondaryUserId());	
				
				//assign umpire id to user
				st.addBatch("update mcc.user set umpire_id = "+ dto.getSecondaryUmpireId()+" where user_id =  "+ dto.getSecondaryUserId());
				
			}else {
				st.addBatch("update mcc.user set player_id = "+ master+" where user_id =  "+ dto.getSecondaryUserId());
			}
			
			if(!isPlayerInClub) {
				st.addBatch("insert into mcc.player_club values("+master+","+clubId+",NOW(),1,0,0)");
			}			
			if(!isUserInClub) {
				st.addBatch("insert into mcc.user_club(user_id,club_id) values("+dto.getSecondaryUserId()+","+clubId+")");				
			}
			
			st.addBatch("update team_player set player_id = " + master + " where player_id = " + secondary
					+ " and team_id in ("+dto.getTeamIds()+")");
			st.addBatch("update batting set player_id = " + master + " where player_id = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update batting set wicket_taker1 = " + master + " where wicket_taker1 = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update batting set wicket_taker2 = " + master + " where wicket_taker2 = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update team set captain = " + master + " where captain = " + secondary
					+ " and team_id in ( "+ dto.getTeamIds()+")");
			st.addBatch("update team set vice_captain = " + master + " where vice_captain = " + secondary
					+ " and team_id in ( "+ dto.getTeamIds()+")");
			st.addBatch("update matches set man_of_the_match = " + master + " where man_of_the_match = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update matches set team_one_captain = " + master + " where team_one_captain = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update matches set team_two_captain = " + master + " where team_two_captain = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update matches set team_one_vice_captain = " + master + " where team_one_vice_captain = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update matches set team_two_vice_captain = " + master + " where team_two_vice_captain = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update ball set batsman = " + master + " where batsman = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update ball set bowler = " + master + " where bowler = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update ball set runner = " + master + " where runner = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update ball set out_person = " + master + " where out_person = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update ball set wicket_taker_1 = " + master + " where wicket_taker_1 = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update ball set wicket_taker_2 = " + master + " where wicket_taker_2 = " + secondary
					+ " and innings_id in ( select innings_id from innings where match_id in ( "+ dto.getMatchIds()+"))");
			st.addBatch("update articles set user_id = " + dto.getSecondaryUserId() + " where user_id = "+dto.getPrimaryUserId()
					+ " and match_id in ( "+ dto.getMatchIds() +")");
			
			if(!CommonUtility.isNullOrEmpty(dto.getCommentIds()) ) {
				st.addBatch("update comments set user_id = "+dto.getSecondaryUserId() +" where user_id = "+dto.getPrimaryUserId() 
				+ " and comment_id in ("+dto.getCommentIds() +")");
			}
		
			st.addBatch("update matches set last_updated_by = "+dto.getSecondaryUserId()
					+ " where last_updated_by = "+dto.getPrimaryUserId()+" and match_id in ("+dto.getMatchIds()+ ")");
			st.addBatch("update match_player_team set player_id = " + master + " where player_id = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update bowling set player_id = " + master + " where player_id = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update matches set scorer = "+dto.getSecondaryUserId() +" where "
					+ " scorer = "+dto.getPrimaryUserId() +" and match_id in ("+dto.getMatchIds() +")") ;
			st.addBatch("update scorer_log set scorer_id = "+dto.getSecondaryUserId()+" where "
					+ " scorer_id = "+dto.getPrimaryUserId()+" and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update player_statistics_summary set player_id = " + master + " where player_id = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update player_statistics_summary set wicket_taker1 = " + master + " where wicket_taker1 = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update player_statistics_summary set wicket_taker2 = " + master + " where wicket_taker2 = " + secondary
					+ " and match_id in ( "+ dto.getMatchIds()+")");
			st.addBatch("update mcc.audit_player_merge set unmerge_date_time = NOW(), unmerge_status = 1 where id = "+dto.getId());	
			
			st.executeBatch();			
			conn.commit();			
			
		} catch (BatchUpdateException be) {
			conn.rollback();
			throw new Exception(be);
		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e);
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		return true;
	}
}
