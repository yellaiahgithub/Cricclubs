package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cricket.dto.EmailMobileLogDto;
import com.cricket.utility.DButility;

public class EmailMobileLogDAO {
	
	protected int addEmailMobileLog(EmailMobileLogDto metaDto) throws Exception {
		
		String query = "insert into change_email_mobile_log(user_id,field_type,new_email_mobile,otp,status) "
				+ "values (?,?,?,?,?)";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		int metaId = 0;
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, metaDto.getUserId());
			pst.setString(2, metaDto.getFieldType());			
			pst.setString(3, metaDto.getNewEmailMobile());
			pst.setString(4, metaDto.getOtp());
			pst.setInt(5, metaDto.getStatus());			
			pst.execute();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				metaId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
		return metaId;
	}
	
	protected void updateEmailMobileLogOtpStatus(long logId) throws Exception {
		
		String query = "update change_email_mobile_log set status = 1 where id = ? and status = 0";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;		
		try {	
			pst = conn.prepareStatement(query);
			pst.setLong(1, logId);
			pst.execute();			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
	}	
	
	protected void updateChangeMobileLogOtp(EmailMobileLogDto logDto) throws Exception {
		
		String query = "update change_email_mobile_log set otp = ? where user_id = ? "
				+ "and new_email_mobile = ? and field_type = ? and status = ?";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;		
		try {	
			pst = conn.prepareStatement(query);
			
			int index = 1;
			
			pst.setString(index++, logDto.getOtp());			
			pst.setLong(index++, logDto.getUserId());
			pst.setString(index++, logDto.getNewEmailMobile());	
			pst.setString(index++, logDto.getFieldType());
			pst.setInt(index++, logDto.getStatus());
			
			pst.execute();	
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
	}	
	

	public int checkEmailMobileLogExistsForUser(EmailMobileLogDto logDto) throws Exception {
		String query = " select id from change_email_mobile_log where user_id =? "
				+ "and field_type= ? and new_email_mobile = ? and otp = ? and status = 0 ";	
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;	
		int logId = 0;
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			
			int index = 1;
			
			pst.setLong(index++, logDto.getUserId());
			pst.setString(index++, logDto.getFieldType());			
			pst.setString(index++, logDto.getNewEmailMobile());
			pst.setString(index++, logDto.getOtp());	
			
			rs = pst.executeQuery();			
			while (rs.next()) {					
				logId =  rs.getInt("id");
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return logId;
	}	
	
	
	public String getExitingOtpFromMobileEmailLog(long userId, String fieldType, String emailOrMobile) throws Exception {
		
		String query = " select otp from change_email_mobile_log where user_id =? "
				+ "and field_type= ? and new_email_mobile = ? and status = 0 ";		
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;	
		String otp = null;
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			
			int index = 1;
			
			pst.setLong(index++, userId);
			pst.setString(index++, fieldType);			
			pst.setString(index++, emailOrMobile);
						
			rs = pst.executeQuery();
			
			while (rs.next()) {					
				otp =  rs.getString("otp");
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return otp;
	}
}
