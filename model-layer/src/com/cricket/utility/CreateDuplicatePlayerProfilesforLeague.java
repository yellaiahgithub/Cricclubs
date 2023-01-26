package com.cricket.utility;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.PlayerFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.UserDto;

public class CreateDuplicatePlayerProfilesforLeague {
	
	public static void main(String[] args) throws Exception {

		System.out.println("....Program Start...");	
		
		int clubId = 8097;
		
		List<Integer> playerIds = new ArrayList<Integer>();
		
		List<PlayerDto> dbPlayersList = PlayerFactory.getAllActivePlayers(clubId);
		
		if (!CommonUtility.isListNullEmpty(dbPlayersList)) {
			for (PlayerDto pdto : dbPlayersList) {
				if (!playerIds.contains(pdto.getPlayerID())) {

					playerIds.add(pdto.getPlayerID());
					int oldPlayerId = pdto.getPlayerID();
					int newPlayerId = 0;
					int oldUserId = 0;
					long newUserId = 0;
					try {
						newPlayerId = PlayerFactory.registerPlayer(pdto, clubId, "MUSCAT_DUPLICATE_PROFILE");
						if (newPlayerId > 0) {
							UserDto udto = UserFactory.getUserByPlayerId(oldPlayerId);
							if (udto != null) {
								oldUserId = udto.getUserID();
								udto.setPlayerID(newPlayerId);
								udto.setAddRequestFrom("MUSCAT_DUPLICATE_PROFILE");
								newUserId = UserFactory.registerUser(udto, clubId, "MUSCAT_DUPLICATE_PROFILE");
							}
							updateStatsToNewPlayer(newPlayerId, oldPlayerId, newUserId, oldUserId, clubId);
						}
					} catch (Exception e) {
						System.out.println("Exception - playerId - " + pdto.getPlayerID() + " - " + e.getMessage());
					}
				}
			}
		}		
	}	
	
	public static boolean updateStatsToNewPlayer(int newPlayerId, int oldPlayerId, long newUserId, int oldUserId, int clubId) throws Exception {
		
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId);
		}catch (Exception e) {
			throw new Exception(e);
		}		
		try {
			conn.setAutoCommit(false);
			st = conn.createStatement();	
						
			if(newUserId>0 && oldUserId>0) {
				
				st.addBatch("update mcc.user_roles set user_id = " + newUserId + " where user_id = " + oldUserId + " and club_id = "+clubId);				
				st.addBatch("update matches set last_updated_by = "+newUserId+ " where last_updated_by = "+oldUserId);
				st.addBatch("update matches set scorer = "+newUserId +" where scorer = "+oldUserId) ;
				st.addBatch("update scorer_log set scorer_id = "+newUserId+" where scorer_id = "+oldUserId);				
			}
			if(oldUserId>0) {
				st.addBatch("delete from mcc.user_club where user_id = "+oldUserId+" and club_id = "+clubId);
			}
			if(oldPlayerId>0) {
				st.addBatch("delete from mcc.player_club where player_id = "+oldPlayerId+" and club_id = "+clubId);
			}				
				st.addBatch("update team_player set player_id = " + newPlayerId + " where player_id = " + oldPlayerId);
				st.addBatch("update batting set player_id = " + newPlayerId + " where player_id = " + oldPlayerId);
				st.addBatch("update batting set wicket_taker1 = " + newPlayerId + " where wicket_taker1 = " + oldPlayerId);
				st.addBatch("update batting set wicket_taker2 = " + newPlayerId + " where wicket_taker2 = " + oldPlayerId);
				st.addBatch("update team set captain = " + newPlayerId + " where captain = " + oldPlayerId);
				st.addBatch("update team set vice_captain = " + newPlayerId + " where vice_captain = " + oldPlayerId);
				st.addBatch("update matches set man_of_the_match = " + newPlayerId + " where man_of_the_match = " + oldPlayerId);
				st.addBatch("update matches set team_one_captain = " + newPlayerId + " where team_one_captain = " + oldPlayerId);
				st.addBatch("update matches set team_two_captain = " + newPlayerId + " where team_two_captain = " + oldPlayerId);
				st.addBatch("update matches set team_one_vice_captain = " + newPlayerId + " where team_one_vice_captain = " + oldPlayerId);
				st.addBatch("update matches set team_two_vice_captain = " + newPlayerId + " where team_two_vice_captain = " + oldPlayerId);
				st.addBatch("update ball set batsman = " + newPlayerId + " where batsman = " + oldPlayerId);
				st.addBatch("update ball set bowler = " + newPlayerId + " where bowler = " + oldPlayerId);
				st.addBatch("update ball set runner = " + newPlayerId + " where runner = " + oldPlayerId);
				st.addBatch("update ball set out_person = " + newPlayerId + " where out_person = " + oldPlayerId);
				st.addBatch("update ball set wicket_taker_1 = " + newPlayerId + " where wicket_taker_1 = " + oldPlayerId);
				st.addBatch("update ball set wicket_taker_2 = " + newPlayerId + " where wicket_taker_2 = " + oldPlayerId);	
				st.addBatch("update match_player_team set player_id = " + newPlayerId + " where player_id = " + oldPlayerId);
				st.addBatch("update bowling set player_id = " + newPlayerId + " where player_id = " + oldPlayerId);			
				st.addBatch("update player_statistics_summary set player_id = " + newPlayerId + " where player_id = " + oldPlayerId);
				st.addBatch("update player_statistics_summary set wicket_taker1 = " + newPlayerId + " where wicket_taker1 = " + oldPlayerId);
				st.addBatch("update player_statistics_summary set wicket_taker2 = " + newPlayerId + " where wicket_taker2 = " + oldPlayerId);
			
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
		System.out.println("Profile - oldPlayerId - " + oldPlayerId + " - newPlayerId " + newPlayerId);
		return true;
	}
}
