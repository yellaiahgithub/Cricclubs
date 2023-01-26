/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.utility.DButility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CaptainMatchReportDAO {

	Logger log = LoggerFactory.getLogger(CaptainMatchReportDAO.class);
	
	protected void createCaptainReport(String report,int userId, String matchId,int clubId) throws Exception {
		String query;
		Connection conn = null;		
		PreparedStatement st = null;		
		try {
         deleteCaptainMatchReport(clubId,userId,matchId);
		 query = "insert into captain_match_report(user_id,match_id,report) values (?,?,?)";
				
		 conn = DButility.getConnection(clubId);
		 Blob blob = conn.createBlob();
			blob.setBytes(1, report.getBytes());
			st = conn.prepareStatement(query);
			
			st.setInt(1, userId);
			st.setString(2, matchId);
			st.setBlob(3, blob);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
			
	protected String getCaptainReport(int userId, String matchId, int clubId) throws Exception {
		//UmpireMatchReportDto reports = new UmpireMatchReportDto();
		//CaptainMatchReportDto report = new CaptainMatchReportDto();
		String query =
				"select user_id," +
						"match_id," +
						"report " +					
						"from captain_match_report " +
						"where 1=1 ";
		if (matchId != null) {
			query += " AND match_id= ?";
		}
		if(userId != 0) {
			query += " AND user_id = ?";
		}
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		String blobdata = null;

		try {
			pst = conn.prepareStatement(query);
			int index =1;
			if(matchId != null) {
				pst.setString(index++, matchId);
			}
			if (userId != 0) {
				pst.setInt(index++, userId);
			}
			rs = pst.executeQuery();
			while(rs.next()){
				
				blobdata = rs.getString("report");
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return blobdata;

	}
	
	protected int checkCaptainReport(int userId, String matchId, int clubId) throws Exception {
		//UmpireMatchReportDto reports = new UmpireMatchReportDto();
		String query =
				"select count(*) as count from " +					
						" captain_match_report " +
						"where 1=1 ";
		if (matchId != null) {
			query += " AND match_id= ?";
		}
		if(userId != 0) {
			query += " AND user_id = ?";
		}
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		int blobdata = 0;

		try {
			pst = conn.prepareStatement(query);
			int index =1;
			if(matchId != null) {
				pst.setString(index++, matchId);
			}
			if (userId != 0) {
				pst.setInt(index++, userId);
			}
			rs = pst.executeQuery();
			while(rs.next())
			{
			blobdata = rs.getInt("count");
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return blobdata;

	}
	
	protected void deleteCaptainMatchReport(int clubId,int userId,String matchId) throws Exception {

		String query;
		PreparedStatement st = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId);			
			query="delete from captain_match_report where user_id=? and match_id=?";

			st = conn.prepareStatement(query);
			st.setInt(1, userId);
			st.setString(2, matchId);
			
			st.executeUpdate();
			

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}	
}
