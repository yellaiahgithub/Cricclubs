/*
 * Created on Feb 5, 2019
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.ScoreBoardDeviceDTO;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class ScoreBoardDeviceDAO {
	
	protected ScoreBoardDeviceDAO() {

	}
	protected List<ScoreBoardDeviceDTO> getScoreBoardDevices(int clubId,int deviceId, String deviceName, int groundId, int limit) throws Exception {
		
		List<ScoreBoardDeviceDTO> sbDevices = new ArrayList<ScoreBoardDeviceDTO>();
		String query =
				"SELECT club_id, "+ 
						"device_id," +
						"device_name," +
						"ground_id," +
						"font_color," +						
						"bg_color," +
						"template_name " +
						"FROM mcc.score_board_device " +
						"where 1 = 1 ";
		
		if (clubId > 0) {
			query += "\nand club_id =? " ;
		}
		if (deviceId > 0) {
			query += "\nand device_id =? " ;
		}
		if (!CommonUtility.isNullOrEmpty(deviceName)) {
			query += "\nand device_name =? " ;
		}
		if (groundId > 0) {
			query += "\nand ground_id =? " ;
		}		
		if (limit != 0) {
			query += "\n LIMIT ? ";
		}
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			if (clubId > 0) {
				pst.setInt(i++, clubId);				
			} 
			if (deviceId > 0) {
				pst.setInt(i++, deviceId);				
			} 
			if (!CommonUtility.isNullOrEmpty(deviceName)) {
				pst.setString(i++, deviceName);				
			} 
			if (groundId > 0) {
				pst.setInt(i++, groundId);				
			} 			
			if(limit != 0) {
				pst.setInt(i++, limit);
			}			
			rs = pst.executeQuery();
			while (rs.next()) {				
				ScoreBoardDeviceDTO sbDevice = new ScoreBoardDeviceDTO();		
				sbDevice.setClubId(rs.getInt("club_id"));
				sbDevice.setDeviceId(rs.getInt("device_id"));
				sbDevice.setDeviceName(rs.getString("device_name"));
				sbDevice.setGroundId(rs.getInt("ground_id"));
				sbDevice.setFontColor(rs.getString("font_color"));
				sbDevice.setBGColor(rs.getString("bg_color"));
				sbDevice.setTemplateName(rs.getString("template_name"));
				sbDevices.add(sbDevice);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return sbDevices;

	}
	protected int insertScoreBoardDevice(ScoreBoardDeviceDTO sbDevice, int clubId, int userId) throws Exception {
		
		int deviceId = 0;
		String query = "insert into mcc.score_board_device(club_id,device_name,ground_id,"
				+ "font_color,bg_color,template_name,created_by,created_date) values (?,?,?,?,?,?,?,NOW())";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			pst.setInt(i++, sbDevice.getClubId());
			pst.setString(i++, sbDevice.getDeviceName());
			pst.setInt(i++, sbDevice.getGroundId());
			pst.setString(i++, sbDevice.getFontColor());
			pst.setString(i++,sbDevice.getBGColor());
			pst.setString(i++,sbDevice.getTemplateName());
			pst.setInt(i++,userId);
			
			pst.executeUpdate();	
			
			rs = pst.executeQuery("select max(device_id) device_id from mcc.score_board_device");
			rs.next();
			deviceId = rs.getInt("device_id");
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
		return deviceId;
	}
	
	protected void updateScoreBoardDevice(ScoreBoardDeviceDTO sbDevice, int clubId, int userId) throws Exception {

		String query = "update mcc.score_board_device set club_id = ?, "
				+ "device_name = ?,"
				+ "ground_id = ?,"
				+ "font_color =?,"
				+ "bg_color = ?,"
				+ "template_name = ?,"
				+ "updated_by = ?, "
				+ "updated_date = NOW() "
				+ " where device_id = ?";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		
		try {			
			pst = conn.prepareStatement(query);
			
			int i = 1;
			
			pst.setInt(i++, sbDevice.getClubId());
			pst.setString(i++, sbDevice.getDeviceName());
			pst.setInt(i++, sbDevice.getGroundId());
			pst.setString(i++, sbDevice.getFontColor());
			pst.setString(i++,sbDevice.getBGColor());
			pst.setString(i++,sbDevice.getTemplateName());
			pst.setInt(i++,userId);
			pst.setInt(i++,sbDevice.getDeviceId());
			
			pst.executeUpdate();			
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void deleteScoreBoardDevice(int deviceId, int clubId) throws Exception {

		String query = "delete from mcc.score_board_device where device_id = "+ deviceId;
		Connection conn = DButility.getDefaultConnection();
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
