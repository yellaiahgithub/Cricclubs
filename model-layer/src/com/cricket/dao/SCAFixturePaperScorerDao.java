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
import java.util.HashMap;
import java.util.Map;

import com.cricket.utility.DButility;

public class SCAFixturePaperScorerDao {

	protected SCAFixturePaperScorerDao() {

	}

	protected int getFixturePaperScorer(int fixtureId, int clubId ) throws Exception {		
				
		String query = "SELECT scorer_id FROM sca_fixture_paper_scorer where 1 = 1 ";
		int paperScorer = 0;
		if (fixtureId > 0) {
			query += "\nand fixture_id = ? ";
		}		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);	
			int i = 1;
			if (fixtureId > 0) {
				pst.setInt(i++, fixtureId);				
			}						
			rs = pst.executeQuery();
			while (rs.next()) {			
				paperScorer = rs.getInt("scorer_id");		
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return paperScorer;
	}
	
	protected Map<Integer,String> getFixturePaperScorerMap(int clubId) throws Exception {		
		
		String query = "SELECT fixture_id, concat(f_name,' ',l_name) scorer_name "
				+ "FROM sca_fixture_paper_scorer fps, mcc.umpire_view uv "
				+ "where uv.club_id = "+clubId+" and fps.scorer_id = uv.user_id ";		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer,String> fixturePaperScorerIdMap = new HashMap<Integer, String>();
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			while (rs.next()) {			
				fixturePaperScorerIdMap.put(rs.getInt(1), rs.getString(2));		
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return fixturePaperScorerIdMap;
	}
	
	protected void insertPaperScorers(int fixtureId, int scorerId, int clubId) throws Exception {

		String query = "insert into sca_fixture_paper_scorer(fixture_id,scorer_id) values (?,?)";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		int i = 1;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(i++, fixtureId);			
			if( scorerId > 0 ) {
				pst.setInt(i++, scorerId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public void updatePaperScorers(int fixtureId, int scorerId, int clubId) throws Exception {

		String query = "update sca_fixture_paper_scorer set ";
		if (scorerId > 0) {
			query += "scorer_id = ?";
		}
		if (fixtureId > 0) {
			query += " where fixture_id = ?";
		}
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		int i = 1;
		try {
			pst = conn.prepareStatement(query);
			if (scorerId > 0) {
				pst.setInt(i++, scorerId);
			}
			if (fixtureId > 0) {
				pst.setInt(i++, fixtureId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void deletePaperScorers(int fixtureId, int scorerId, int clubId) throws Exception {

		String query = "delete from sca_fixture_paper_scorer where fixture_id = " + fixtureId;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
