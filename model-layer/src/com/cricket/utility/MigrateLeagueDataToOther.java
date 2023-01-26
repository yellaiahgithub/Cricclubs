package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import com.mysql.jdbc.Statement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MigrateLeagueDataToOther {

	public static void main(String[] args) throws Exception {

		System.out.println("Program Start...");

		int clubId1 = 18192;// to League
		int clubId2 = 179;// from League

		updateClub1SeriesData(clubId1, clubId2);		
					
		updateClub1TeamsData(clubId1, clubId2);		
					
		updateClub1FixturesData(clubId1, clubId2);		
					
		updateClub1MatchesData(clubId1, clubId2);
			
		updateClub1InningsData(clubId1, clubId2);
			
		updateClub1BallsData(clubId1, clubId2);
		
		updateClub1GroundsData(clubId1, clubId2);
			
		addPlayersToClub1(clubId1, clubId2);
			System.out.println("Players Data Migrated...");
		
		migrateLeaguesDataToClub1(clubId1, clubId2);
			System.out.println("Complete League Data Migrated...");
			
		updateAutoIncrementValues(clubId1);

		System.out.println("Program End!! ");
	}

	private static List<Integer> getTableMaxVaues(int clubId1) throws Exception {
		
		String[] queryList = { "select max(ball_id) max_id from ball", "select max(batting_id) max_id from batting",
				"select max(bowling_id) max_id from bowling", "select max(fixture_id) max_id from fixtures",
				"select max(ground_id) max_id from grounds", "select max(league_id) max_id from home_leagues",
				"select max(innings_id) max_id from innings", "select max(league_id) max_id from league",
				"select max(match_id) max_id from matches", "select max(id) max_id from player_statistics_summary",
				"select max(team_id) max_id from team" };

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;		
		conn = DButility.getConnection(clubId1);
		int maxId = 0;
		List<Integer> maxIdList = new ArrayList<Integer>();
		try {
			for (String query : queryList) {
				pst = conn.prepareStatement(query);
				rs = pst.executeQuery();
				while (rs.next()) {
					maxId = rs.getInt("max_id");
					maxIdList.add(maxId);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
				
		return maxIdList;
	}

	private static void updateAutoIncrementValues(int clubId1) throws Exception {

		String[] tableList = { "ball", "batting", "bowling", "fixtures", "grounds", "home_leagues", "innings", "league",
				"matches", "player_statistics_summary", "team" };		
		List<Integer> maxIdList = getTableMaxVaues(clubId1);
		for(int i=0; i<tableList.length; i++) {
			System.out.println(tableList[i]+" "+ maxIdList.get(i));
		}
		Connection conn = null;
		Statement st = null;		
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			for(int i=0; i<tableList.length; i++) {
				st.addBatch(" alter table "+tableList[i]+" AUTO_INCREMENT = "+ maxIdList.get(i));
			}			
			st.executeBatch();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1GroundsData(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;		
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxGroundId = 0;
		
		try {
			query = " select max(ground_id) ground_id from club"+clubId2+".grounds ";
			conn = DButility.getConnection(clubId2);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxGroundId = rs.getInt("ground_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			
			String condition = " where ( ground_id > 0 and ground_id is not null and ground_id != '' ) ";
			String condition1 = " where ( HomeGround > 0 and HomeGround is not null and HomeGround != '' ) ";
			
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			
			st.addBatch(" update fixtures set ground_id=ground_id"+"+"+club2MaxGroundId + condition);
			st.addBatch(" update fixtures_audit set ground_id=ground_id"+"+"+club2MaxGroundId + condition );
			st.addBatch(" update grounds set ground_id=ground_id"+"+"+club2MaxGroundId+" order by ground_id desc " );
			st.addBatch(" update team set HomeGround=HomeGround"+"+"+club2MaxGroundId + condition1);
			st.addBatch(" update team_audit set HomeGround=HomeGround"+"+"+club2MaxGroundId + condition1);

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
	}
	private static void migrateLeaguesDataToClub1(int clubId1, int clubId2) throws Exception {	
		
		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId2);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			st.addBatch(" insert into club" + clubId1 + ".league select * from club" + clubId2 + ".league");
				System.out.println("Series Data Migrated...");
				
			st.addBatch(" insert into club" + clubId1 + ".team select * from club" + clubId2 + ".team");
			st.addBatch(" insert into club" + clubId1 + ".team_player select * from club" + clubId2 + ".team_player");
				System.out.println("Teams Data Migrated...");
				
			st.addBatch(" insert into club" + clubId1 + ".fixtures select * from club" + clubId2 + ".fixtures");
				System.out.println("Fixtures Data Migrated...");
				
			st.addBatch(" insert into club" + clubId1 + ".matches select * from club" + clubId2 + ".matches");
			st.addBatch(" insert into club" + clubId1 + ".match_player_team select * from club" + clubId2
					+ ".match_player_team");
			st.addBatch(" insert into club" + clubId1 + ".batting select * from club" + clubId2 + ".batting");
			st.addBatch(" insert into club" + clubId1 + ".bowling select * from club" + clubId2 + ".bowling");
			st.addBatch(" insert into club" + clubId1 + ".innings select * from club" + clubId2 + ".innings");
				System.out.println("Matches Data Migrated...");
				
			st.addBatch(" insert into club" + clubId1 + ".ball select * from club" + clubId2 + ".ball");
				System.out.println("Balls Data Migrated...");
				
			st.addBatch(" insert into club" + clubId1 + ".grounds select * from club" + clubId2 + ".grounds");
				System.out.println("Grounds Data Migrated...");

			//st.addBatch(" insert into club" + clubId1 + ".fixture_player_available select * from club" + clubId2
				//	+ ".fixture_player_available");
			st.addBatch(" insert into club" + clubId1 + ".club_star_player select * from club" + clubId2
					+ ".club_star_player");
			st.addBatch(" insert into club" + clubId1 + ".matches_fb_posts select * from club" + clubId2
					+ ".matches_fb_posts");
			st.addBatch(" insert into club" + clubId1 + ".umpire_match_report select * from club" + clubId2
					+ ".umpire_match_report");
			st.addBatch(" insert into club" + clubId1 + ".captain_match_report select * from club" + clubId2
					+ ".captain_match_report");

			st.addBatch(" insert into club" + clubId1 + ".scorer_log select * from club" + clubId2 + ".scorer_log");

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void addPlayersToClub1(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;		
		Statement st = null;
		
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			
			st.addBatch(" insert into mcc.player_club (select player_id," + clubId1 + ",create_date,"
					+ "is_active,accepted_terms from mcc.player_club where club_id = " + clubId2
					+ " and player_id not in ( select player_id from mcc.player_club " + " where club_id = " + clubId1
					+ " ) )");	
			
			st.addBatch(" insert into mcc.umpire_club (select umpire_id," + clubId1 + ",create_date,"
					+ "is_active from mcc.umpire_club where club_id = " + clubId2
					+ " and umpire_id not in ( select umpire_id from mcc.umpire_club " + " where club_id = " + clubId1
					+ " ) )");

			st.addBatch(" insert into mcc.user_club (user_id, club_id, access_level)  "
					+ " ( select user_id," + clubId1 + ",access_level "
					+ "from mcc.user_club where club_id = " + clubId2 + " and user_id not "
					+ " in ( select user_id from mcc.user_club where club_id = " + clubId1 + " ) )");
			
			st.addBatch(" insert into mcc.user_roles ( select user_id, " + clubId1 + " , role, attribute "
					+ "from mcc.user_roles where club_id = " + clubId2 + " and user_id not in "
					+ "( select user_id from mcc.user_roles where club_id = " + clubId1 + " ) )");

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
	private static void updateClub1BallsData(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxBallId = 0;
		
		try {
			query = " select max(ball_id) ball_id from club"+clubId2+".ball ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxBallId = rs.getInt("ball_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();

			st.addBatch(" update ball set ball_id=ball_id" + "+" + club2MaxBallId+" order by ball_id desc");
			st.addBatch(" update scorer_log set ball_id=ball_id" + "+" + club2MaxBallId);

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1InningsData(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxInningsId = 0;
		
		try {
			query = " select max(innings_id) innings_id from club"+clubId2+".innings ";
			conn = DButility.getConnection(clubId2);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxInningsId = rs.getInt("innings_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();

			st.addBatch(" update ball set innings_id=innings_id" + "+" + club2MaxInningsId);
			st.addBatch(" update innings set innings_id=innings_id" + "+" + club2MaxInningsId+" order by innings_id desc");
			
			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1MatchesData(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxMatchId = 0;
		int club2MaxBattingId = 0;
		int club2MaxBowlingId = 0;
		
		try {
			query = " select max(match_id) match_id from club"+clubId2+".matches ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxMatchId = rs.getInt("match_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			query = " select max(batting_id) batting_id from club"+clubId2+".batting ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxBattingId = rs.getInt("batting_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			query = " select max(bowling_id) bowling_id from bowling ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxBowlingId = rs.getInt("bowling_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();

			st.addBatch(" update articles set match_id=match_id" + "+" + club2MaxMatchId +" where match_id > 0 ");
			st.addBatch(" update batting set batting_id=batting_id" + "+" + club2MaxBattingId+" order by batting_id desc");
			st.addBatch(" update batting set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update bowling set bowling_id=bowling_id" + "+" + club2MaxBowlingId+" order by bowling_id desc");
			st.addBatch(" update bowling set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update captain_match_report set match_id=match_id" + "+" + club2MaxMatchId +" where match_id > 0 ");
			st.addBatch(" update fixtures set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update fixtures_audit set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update innings set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update match_dl_records set match_id=match_id" + "+" + club2MaxMatchId);
			st.addBatch(" update match_player_team set match_id=match_id" + "+" + club2MaxMatchId+" order by match_id desc");
			st.addBatch(" update matches set match_id=match_id" + "+" + club2MaxMatchId+" order by match_id desc");
			st.addBatch(" update matches_fb_posts set match_id=match_id" + "+" + club2MaxMatchId +" where match_id > 0 " );			
			st.addBatch(" update scorer_log set match_id=match_id" + "+" + club2MaxMatchId +" where match_id > 0 " );

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1FixturesData(int clubId1, int clubId2) throws Exception {		
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxFixtureId = 0;
		
		try {
			query = " select max(fixture_id) fixture_id from club"+clubId2+".fixtures ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxFixtureId = rs.getInt("fixture_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();

			st.addBatch(" update fixture_player_available set fixture_id=fixture_id" + "+" + club2MaxFixtureId);
			st.addBatch(" update fixtures set fixture_id=fixture_id" + "+" + club2MaxFixtureId+" order by fixture_id desc");
			st.addBatch(" update fixtures_audit set fixture_id=fixture_id" + "+" + club2MaxFixtureId);
			st.addBatch(" update umpire_match_report set fixture_id=fixture_id" + "+" + club2MaxFixtureId +" where fixture_id > 0 ");

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1TeamsData(int clubId1, int clubId2) throws Exception {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query = "";
		int club2MaxTeamId = 0;
		
		try {
			query = " select max(team_id) team_id from club"+clubId2+".team ";
			conn = DButility.getConnection(clubId2, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxTeamId = rs.getInt("team_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();

			st.addBatch(" update club_star_player set team_id=team_id" + "+" + club2MaxTeamId);
			st.addBatch(" update fixture_player_available set team_id=team_id" + "+" + club2MaxTeamId);
			st.addBatch(" update fixtures set team_one=team_one" + "+" + club2MaxTeamId +" where team_one > 0 ");
			st.addBatch(" update fixtures set team_two=team_two" + "+" + club2MaxTeamId +" where team_two > 0 ");
			st.addBatch(" update fixtures_audit set team_one=team_one" + "+" + club2MaxTeamId);
			st.addBatch(" update fixtures_audit set team_two=team_two" + "+" + club2MaxTeamId);
			st.addBatch(" update match_player_team set team_id=team_id" + "+" + club2MaxTeamId+" order by team_id desc");
			st.addBatch(" update matches set team_one=team_one" + "+" + club2MaxTeamId +" where team_one > 0 ");
			st.addBatch(" update matches set team_two=team_two" + "+" + club2MaxTeamId +" where team_two > 0 ");
			st.addBatch(" update matches set toss_won=toss_won" + "+" + club2MaxTeamId +" where toss_won > 0 ");
			st.addBatch(" update matches set batting_first=batting_first" + "+" + club2MaxTeamId +" where batting_first > 0 ");
			st.addBatch(" update matches set winner=winner" + "+" + club2MaxTeamId +" where winner > 0 ");
			st.addBatch(" update team set team_id=team_id" + "+" + club2MaxTeamId+" order by team_id desc");
			st.addBatch(" update team_audit set team_id=team_id" + "+" + club2MaxTeamId);
			st.addBatch(" update team_player set team_id=team_id" + "+" + club2MaxTeamId+" order by team_id desc, player_id desc");
			st.addBatch(" update team_player_audit set team_id=team_id" + "+" + club2MaxTeamId);
			st.addBatch(" update league set winner=winner" + "+" + club2MaxTeamId+" where winner > 0 ");
			st.addBatch(" update league set runner=runner" + "+" + club2MaxTeamId+" where runner > 0 ");

			st.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	private static void updateClub1SeriesData(int clubId1, int clubId2) throws Exception {

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Statement st = null;
		String query;
		int club2MaxLeagueId = 0;
		
		try {
			query = " select max(league_id) league_id from club"+clubId2+".league";
			conn = DButility.getConnection(clubId2);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				club2MaxLeagueId = rs.getInt("league_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		try {
			conn = DButility.getConnection(clubId1);
			conn.setAutoCommit(false);
			st = (Statement) conn.createStatement();
			st.addBatch(" update mcc.club set current_league = current_league" + "+" + club2MaxLeagueId+" where club_id = "+clubId1+" and ( current_league > 0 and current_league is not null and current_league != '') ");
			st.addBatch(" update league set league_id = league_id" + "+" + club2MaxLeagueId+" order by league_id desc");
			st.addBatch(" update league set parent = parent" + "+" + club2MaxLeagueId+" where parent > 0");
			st.addBatch(" update team set league = league" + "+" + club2MaxLeagueId);
			st.addBatch(" update team_audit set league = league" + "+" + club2MaxLeagueId);
			st.addBatch(" update fixtures set league_id = league_id" + "+" + club2MaxLeagueId);
			st.addBatch(" update fixtures_audit set league_id = league_id" + "+" + club2MaxLeagueId);
			st.addBatch(" update club_star_player set league_id = league_id" + "+" + club2MaxLeagueId);	
			//st.addBatch(" update home_leagues set league_id = league_id" + "+" + club2MaxLeagueId);	
			//st.addBatch(" update league_group_names set league_id = league_id" + "+" + club2MaxLeagueId);	

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
