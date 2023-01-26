package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.CoinsDailyBonusDto;
import com.cricket.dto.ContestDto;
import com.cricket.dto.FantasyCoinsDto;
import com.cricket.dto.UserLoginCoinsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class FantasyCoinsDAO {
	
	public String SELECT_QUERY = "select id,currency,amount,discount,no_of_coins from coins ";
	
	private void populateCoinsDtos(FantasyCoinsDto coinsDto, ResultSet rs) throws SQLException {
		
		coinsDto.setId(rs.getInt("id"));
		coinsDto.setCurrency(rs.getString("currency"));
		coinsDto.setAmount(rs.getFloat("amount"));	
		coinsDto.setDiscount(rs.getFloat("discount"));	
		coinsDto.setNoOfCoins(rs.getInt("no_of_coins"));	
		
	}
	
	protected List<FantasyCoinsDto> getFantasyCoinsInfo(String currency, float amount) throws Exception {
		
		List<FantasyCoinsDto> fantasyCoinsDetails = new ArrayList<FantasyCoinsDto>();
		
		String query = SELECT_QUERY +" where 1=1 ";
		
		if(!CommonUtility.isNullOrEmptyOrNULL(currency)) {
			query = query+ " and currency = ?";
		}	
		if(amount>0) {
			query = query+ " and amount = ?";
		}
		
		query = query+ " order by currency,amount ";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		FantasyCoinsDto coinsDto = null;
		try {
			st = conn.prepareStatement(query);			
			if(!CommonUtility.isNullOrEmptyOrNULL(currency)) {
				st.setString(1,currency);
			}
			if(amount>0) {
				st.setFloat(2,amount);
			}
			rs = st.executeQuery();			
			while (rs.next()) {
				coinsDto = new FantasyCoinsDto();
				populateCoinsDtos(coinsDto,rs);				
				fantasyCoinsDetails.add(coinsDto);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return fantasyCoinsDetails;
	}

	public UserLoginCoinsDto getUserLoginCoinsInfo(long userId) throws Exception {
		
		String query =  " select user_id, login_date, counter from user_login_coins where user_id = ? ";

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		UserLoginCoinsDto dto = null;
		
		try {
			st = conn.prepareStatement(query);
			if (userId > 0) {
				st.setLong(1, userId);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				
				dto = new UserLoginCoinsDto();
				
				dto.setUserId(rs.getLong("user_id"));
				dto.setLoginDate(rs.getString("login_date"));
				dto.setCounter(rs.getInt("counter"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return dto;
	}
	
	protected void insertUserLoginCoinsInfo(String dateStr, int counter, long userId) throws Exception {

		String query = "insert into user_login_coins(login_date,counter,user_id) values (?,?,?)";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			
			st.setString(1, dateStr);
			st.setInt(2, counter);
			st.setLong(3, userId);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected void updateUserLoginCoinsInfo(String dateStr, int counter, long userId) throws Exception {

		String query = "update user_login_coins set login_date=?,counter=? where user_id=?";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			
			st.setString(1, dateStr);
			st.setInt(2, counter);
			st.setLong(3, userId);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	public List<CoinsDailyBonusDto> getCoinsDailyBonusDto(int day) throws Exception {
		
		String query =  " select id, day, no_of_coins from coins_daily_bonus where 1=1 ";
		
		if(day>0) {
			query = query +" and day = ?";
		}

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		List<CoinsDailyBonusDto> dtoList = new ArrayList<CoinsDailyBonusDto>();
		CoinsDailyBonusDto dto = null;
		
		try {
			st = conn.prepareStatement(query);
			if (day>0) {
				st.setLong(1, day);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				
				dto = new CoinsDailyBonusDto();
				
				dto.setId(rs.getInt("id"));
				dto.setDay(rs.getInt("day"));
				dto.setNoOfCoins(rs.getInt("no_of_coins"));
				
				dtoList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return dtoList;
	}
	
}
