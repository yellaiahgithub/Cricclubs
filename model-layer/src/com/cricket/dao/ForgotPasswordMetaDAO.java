package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cricket.dto.CountriesDto;
import com.cricket.dto.ForgotPasswordMetaDto;
import com.cricket.utility.DButility;

public class ForgotPasswordMetaDAO {
	
	protected int insertForgotPasswordOtp(ForgotPasswordMetaDto passwordMetaDto) throws Exception {
		
		String query = "insert into forgot_password_meta(user_id,email_token,created_at,mobile_otp,status) "
				+ "values (?,?,NOW(),?,?)";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		int metaId = 0;
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, passwordMetaDto.getUserId());
			pst.setString(2, DButility.escapeString(passwordMetaDto.getEmailToken()));			
			pst.setString(3,  passwordMetaDto.getMobileOtp());
			pst.setInt(4,  passwordMetaDto.getStatus());
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
	
	protected void updateForgotPasswordOtpStatus(long userId) throws Exception {
		
		String query = "update forgot_password_meta set status = 1 where user_id = ? and status = 0";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;		
		try {			
			pst = conn.prepareStatement(query);					
			pst.setLong(1, userId);
			pst.execute();			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
	}	
	
	protected void updateForgotPasswordOtpForMobile(ForgotPasswordMetaDto fpDto) throws Exception {
		
		String query = "update forgot_password_meta set mobile_otp = ? where user_id = ? "
				+ "and status = 0 and (email_token is null or email_token = '')";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;		
		try {			
			pst = conn.prepareStatement(query);		
			
			pst.setString(1, fpDto.getMobileOtp());
			pst.setLong(2, fpDto.getUserId());
			
			pst.execute();	
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
	}
	
	protected void updateForgotPasswordOtpForEmail(ForgotPasswordMetaDto fpDto) throws Exception {
		
		String query = "update forgot_password_meta set email_token = ? where user_id = ? "
				+ "and status = 0 and (mobile_otp is null or mobile_otp = '')";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;		
		try {			
			pst = conn.prepareStatement(query);		
			
			pst.setString(1, fpDto.getEmailToken());
			pst.setLong(2, fpDto.getUserId());
			
			pst.execute();	
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}		
	}
	
	protected ForgotPasswordMetaDto getForgotPasswordMetaByUserId(long userId) throws Exception {
		
		String query = " select id, user_id, email_token, created_at, mobile_otp, expiry_at, status "
				+ "from forgot_password_meta where user_id = ? and status = 0 order by created_at desc limit 1";
				
		ForgotPasswordMetaDto metaDto = new ForgotPasswordMetaDto();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;		
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			pst.setLong(1, userId);
			rs = pst.executeQuery();			
			while (rs.next()) {					
				metaDto.setId(rs.getInt("id"));	
				metaDto.setId(rs.getInt("user_id"));	
				metaDto.setEmailToken(rs.getString("email_token"));
				metaDto.setCreatedAt(rs.getDate("created_at"));
				metaDto.setMobileOtp(rs.getString("mobile_otp"));
				metaDto.setExpiryAt(rs.getDate("expiry_at"));
				metaDto.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return metaDto;
	}	
}
