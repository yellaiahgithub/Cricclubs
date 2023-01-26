package com.cricket.utility;

import java.sql.Connection;
import java.sql.Statement;

public class DeleteSeriesData {

	public static void main(String[] args) throws Exception {

		int clubId = 343;// LeagueId
		
		String seriesIds = "132,133,134,138,139,140,142,143,144,141,137,131";
		
		deleteSeriesData(clubId, seriesIds);
		
		System.out.println("Program End!! ");
	}

	private static void deleteSeriesData(int clubId, String seriesIds) throws Exception {	
		
		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			
			//st.addBatch(" delete from ball where innings_id in ( select innings_id from innings where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+")))");
			
			st.addBatch(" delete from match_player_team where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" delete from batting where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" delete from bowling where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" delete from innings where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" delete from matches where match_id in (select match_id from fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" delete from fixtures where league_id in ("+seriesIds+")");
			
			st.addBatch(" delete from team_player where team_id in ( select team_id from team where league in ("+seriesIds+"))");
			
			st.addBatch(" delete from league where league_id in ("+seriesIds+")");
			
			st.addBatch("delete from player_statistics_summary where series_id in ("+seriesIds+")");
			
			st.executeBatch();
			
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
}
