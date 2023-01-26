package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.UserReferralDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class UserReferralDAO {
	
	public String SELECT_QUERY = "select id,user_id,referral_code,used_referral_code,is_bonus_applied,"
			+ "created_date from user_referral_details ";
	
	private void populateUserReferralDto(UserReferralDto urDto, ResultSet rs) throws SQLException {
		
		urDto.setId(rs.getLong("id"));
		urDto.setUserId(rs.getLong("user_id"));
		urDto.setReferralCode(rs.getString("referral_code"));	
		urDto.setUsedReferralCode(rs.getString("used_referral_code"));	
		urDto.setIsBonusApplied(rs.getInt("is_bonus_applied"));
		
	}
	
	protected List<UserReferralDto> getUserReferralDetails(long userId, String referralCode) throws Exception {
		
		String query = SELECT_QUERY+ " where 1=1 ";		
		
		if(userId>0) {
			query = query+" and user_id = ?";
		}	
		if(!CommonUtility.isNullOrEmpty(referralCode)) {
			query = query+" and used_referral_code = ?";
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<UserReferralDto> urDtos = new ArrayList<UserReferralDto>();
		UserReferralDto urDto = null;
		try {
			pst = conn.prepareStatement(query);		
			
			int index = 1;
						
			if(userId>0) {
				pst.setLong(index++,userId);
			}
			if(!CommonUtility.isNullOrEmpty(referralCode)) {
				pst.setString(index++,referralCode);
			}
			
			rs = pst.executeQuery();
			while (rs.next()) {
				urDto = new UserReferralDto();
				populateUserReferralDto(urDto, rs);		
				urDtos.add(urDto);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return urDtos;
	}	
	
	protected long insertUserReferralDetails(UserReferralDto urDto) throws Exception {
		
		String query ="insert into user_referral_details(user_id,referral_code,used_referral_code,created_date) "
				+ "values ( ?,?,?,NOW())";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		long urId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index=1;
			
			pst.setLong(index++,urDto.getUserId());
			pst.setString(index++,urDto.getReferralCode());
			pst.setString(index++,urDto.getUsedReferralCode());	
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				urId = rs.getLong(1);
			}			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return urId;
	}

	public void deleteUserReferralRecord(long userId) throws Exception {
		
		String query = "delete from user_referral_details where user_id = ?";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			pst.setLong(1, userId);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
		
	}	
}
