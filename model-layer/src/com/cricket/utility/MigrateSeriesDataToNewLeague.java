package com.cricket.utility;

import java.sql.Connection;
import java.sql.Statement;

public class MigrateSeriesDataToNewLeague {

	public static void main(String[] args) throws Exception {

		System.out.println("Program Start...");

		int clubId1 = 35294;// New League 
		int clubId2 = 14202;// Old League
		
		String seriesIds = "174,192,200,202,203,206";
		
		migrateLeaguesDataToNewClub(clubId1, clubId2, seriesIds);
		
		System.out.println("Series Data Migrated...");
			
		addPlayersToNewClub(clubId1, clubId2, seriesIds);
		
		System.out.println("Players Data Migrated...");
		
		System.out.println("Program End!! ");
	}

	private static void migrateLeaguesDataToNewClub(int clubId1, int clubId2, String seriesIds) throws Exception {	
		
		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId2);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			
			//Series Data
			st.addBatch(" insert into club" + clubId1 + ".league select * from club" + clubId2 + ".league where league_id in ("+seriesIds+")");
			
			//Teams Data
			st.addBatch(" insert into club" + clubId1 + ".team select * from club" + clubId2 + ".team where league in ("+seriesIds+")");
			st.addBatch(" insert into club" + clubId1 + ".team_player select * from club" + clubId2 + ".team_player where team_id in ( select team_id from club" + clubId2 + ".team where league in ("+seriesIds+"))");
			
			//Series Data
				
			st.addBatch(" insert into club" + clubId1 + ".fixtures select * from club" + clubId2 + ".fixtures where league_id in ("+seriesIds+")");
			
			//Match Data
			
			st.addBatch(" insert into club" + clubId1 + ".innings select * from club" + clubId2 + ".innings where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+"))");				
			st.addBatch(" insert into club" + clubId1 + ".matches(match_id,team_one,team_two,toss_won,batting_first,overs,winner,location,match_date,team_one_captain,team_two_captain,team_one_vice_captain,team_two_vice_captain,t1_total,\"\r\n"
					+ "					+ \"t2_total,t1_balls,t2_balls,t1_wickets,t2_wickets,t1_byes,t1_penalty,t2_penalty,t2_byes,t1_lbyes,t2_lbyes,match_type,man_of_the_match,is_complete,\"\r\n"
					+ "					+ \"is_abandoned,comment,notification_sent,is_locked,scorer,scorer_session,abandone_type,umpire1,umpire2,fow1,fow2,last_updated_date,last_updated_by,t1_1total,t1_2total,t2_1total,t2_2total,t1_1balls,\"\r\n"
					+ "					+ \"t1_2balls,t2_1balls,t2_2balls,t1_1wickets,t1_2wickets,t2_1wickets,t2_2wickets,t1_1byes,t2_1byes,t1_1lbyes,t2_1lbyes,t1_1penalty,t2_1penalty,t1_2byes,t1_2penalty,t2_2penalty,t2_2byes,t1_2lbyes,\"\r\n"
					+ "					+ \"t2_2lbyes,fow1_2,fow2_2,live_youtube_link,is_followon,is_trump,is_dls,t2_target,r1_res_available,t2_revised_over,status,src_site,src_league_id,src_match_id) select match_id,team_one,team_two,toss_won,batting_first,overs,winner,location,match_date,team_one_captain,team_two_captain,team_one_vice_captain,team_two_vice_captain,t1_total,"
					+ "t2_total,t1_balls,t2_balls,t1_wickets,t2_wickets,t1_byes,t1_penalty,t2_penalty,t2_byes,t1_lbyes,t2_lbyes,match_type,man_of_the_match,is_complete,"
					+ "is_abandoned,comment,notification_sent,is_locked,scorer,scorer_session,abandone_type,umpire1,umpire2,fow1,fow2,last_updated_date,last_updated_by,t1_1total,t1_2total,t2_1total,t2_2total,t1_1balls,"
					+ "t1_2balls,t2_1balls,t2_2balls,t1_1wickets,t1_2wickets,t2_1wickets,t2_2wickets,t1_1byes,t2_1byes,t1_1lbyes,t2_1lbyes,t1_1penalty,t2_1penalty,t1_2byes,t1_2penalty,t2_2penalty,t2_2byes,t1_2lbyes,"
					+ "t2_2lbyes,fow1_2,fow2_2,live_youtube_link,is_followon,is_trump,is_dls,t2_target,r1_res_available,t2_revised_over,status,src_site,src_league_id,src_match_id"
					+" from club" + clubId2 + ".matches where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			st.addBatch(" insert into club" + clubId1 + ".match_player_team select * from club" + clubId2+ ".match_player_team where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			st.addBatch(" insert into club" + clubId1 + ".batting select * from club" + clubId2 + ".batting where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			st.addBatch(" insert into club" + clubId1 + ".bowling select * from club" + clubId2 + ".bowling where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+"))");
			
			st.addBatch(" insert into club" + clubId1 + ".internal_club select * from club" + clubId2 + ".internal_club where club_id in (select club_id from club" + clubId1 +".team where club_id>0)");
			st.addBatch(" insert into club" + clubId1 + ".club_player select * from club" + clubId2 + ".club_player where internal_club_id in (select club_id from club" + clubId1 +".team where club_id>0)");
			
			//Ball Data
				
			st.addBatch(" insert into club" + clubId1 + ".ball select * from club" + clubId2 + ".ball where innings_id in ( select innings_id from club" + clubId2 + ".innings where match_id in (select match_id from club" + clubId2 + ".fixtures where match_id>0 and league_id in ("+seriesIds+")))");
			
			//Stats Data
			
			st.addBatch(" insert into club" + clubId1 + ".player_statistics_summary select * from club" + clubId2 + ".player_statistics_summary where series_id in ("+seriesIds+")");
			
			//Series Data
			
			//st.addBatch(" delete from league where league_id in ("+seriesIds+")");
			
			//st.addBatch("delete from player_statistics_summary where series_id in ("+seriesIds+")");
			
			st.executeBatch();
			
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void addPlayersToNewClub(int clubId1, int clubId2, String seriesIds) throws Exception {
		
		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			
			st.addBatch(" insert into mcc.player_club (select player_id," + clubId1 + ",create_date,"
					+ "is_active,accepted_terms from mcc.player_club where club_id = " + clubId2
					+ " and player_id in (select player_id from club" + clubId1 + ".team_player) "
							+ " and player_id not in ( select player_id from mcc.player_club " + " where club_id = " + clubId1 + " ) )");

			st.addBatch(" insert into mcc.user_club (user_id, club_id, access_level)  ( select user_id," + clubId1 + ",access_level "
					+ "from mcc.user_club where club_id = " + clubId2 + " and user_id in "
							+ "( select user_id from mcc.user where player_id in (select player_id from club" + clubId1 + ".team_player )) "
									+ "and user_id not in ( select user_id from mcc.user_club where club_id = " + clubId1 + " ) )");	

			
			st.executeBatch();
			
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
}
