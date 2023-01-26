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

import org.apache.commons.lang3.StringEscapeUtils;

import com.cricket.dto.UploadDataDto;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UploadDataDAO {

	protected UploadDataDAO(){
		
	}
	protected UploadDataDto getUploadData(int userId, int status, int request_type, int clubId) throws Exception { 
		UploadDataDto data = null;
		String query ="SELECT request_id,user_id,request_type,status,club_id,data FROM upload_data WHERE club_id = ? and user_id = ? and status = ? and request_type = ?";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,clubId );
			pst.setInt(2,userId );
			pst.setInt(3,status );
			pst.setInt(4,request_type);
		    rs = pst.executeQuery();
		while (rs.next()) {
			data = new UploadDataDto();
			data.setRequestId(rs.getInt("request_id"));
			data.setUserId(rs.getInt("user_id"));
			data.setStatus(rs.getInt("status"));
			data.setRequestType(rs.getInt("request_type"));
			data.setClubId(rs.getInt("club_id"));
			data.setData(StringEscapeUtils.unescapeHtml4(rs.getString("data")));
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return data;

	}

	protected int insertUploadedData(UploadDataDto data) throws Exception {

		String query ="insert into upload_data(user_id,request_type,status,club_id,data,create_date) values (?,?,?,?,?,NOW())";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int id = 0;
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, data.getUserId());
			pst.setInt(2,data.getRequestType());
			pst.setInt(3,data.getStatus());
			pst.setInt(4,data.getClubId());
			pst.setString(5,StringEscapeUtils.escapeHtml4(data.getData()));
			pst.execute();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return id;
	}
	
	protected void updateStatus(int requestId,int status) throws Exception {
		String query ="update upload_data set status = ? where request_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,status );
			pst.setInt(2,requestId);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected void deleteRequests(int userId,int status, int clubId,int requestType) throws Exception {

		String query = "delete from upload_data where user_id = ? and status = ? and club_id = ? and request_type = ?";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,userId);
			pst.setInt(2,status);
			pst.setInt(3,clubId);
			pst.setInt(4,requestType);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void deleteMatchUploadData(int matchId, int clubId, int requestType) throws Exception {

		String query = "delete from upload_data where user_id = ? and club_id = ? and request_type = ?";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,matchId);
			pst.setInt(2,clubId);
			pst.setInt(3,requestType);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
