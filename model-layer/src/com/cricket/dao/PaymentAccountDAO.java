/*
 * Created on May 08, 2016
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
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.PaymentAccountDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author vk
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PaymentAccountDAO {
	
	/*
			+--------------------+--------------+------+-----+---------+----------------+
			| Field              | Type         | Null | Key | Default | Extra          |
			+--------------------+--------------+------+-----+---------+----------------+
			| payment_account_id | int(11)      | NO   | PRI | NULL    | auto_increment |
			| club_id            | int(11)      | YES  |     | NULL    |                |
			| series_id          | int(11)      | YES  |     | NULL    |                |
			| provider           | varchar(45)  | YES  |     | NULL    |                |
			| account_num        | varchar(45)  | YES  |     | NULL    |                |
			| token              | varchar(300) | YES  |     | NULL    |                |
			| status             | varchar(45)  | YES  |     | NULL    |                |
			| creation_date      | datetime     | YES  |     | NULL    |                |
			| update_date        | datetime     | YES  |     | NULL    |                |
			+--------------------+--------------+------+-----+---------+----------------+
	 **/
	
	private static String SELECT_PAYMENT_ACCOUNTS_QUERY = "select payment_account_id, club_id, series_id, provider, account_num, token, status, creation_date, update_date from mcc.payment_accounts";
	
	private static String INSERT_PAYMENT_ACCOUNTS = "INSERT INTO payment_accounts  (CLUB_ID, SERIES_ID, PROVIDER, ACCOUNT_NUM, TOKEN, STATUS, CREATION_DATE, UPDATE_DATE) VALUES ( ?, ?, ?, ?, ?, ?,NOW(), NOW())";
	
	protected int createPaymentAccount(PaymentAccountDto paymentAccountDto) throws Exception {

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		int paymentAccountId = 0;
		
		int index = 1;

		try {
			st = conn.prepareStatement(INSERT_PAYMENT_ACCOUNTS, Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(index++, paymentAccountDto.getClubId());
			st.setInt(index++, paymentAccountDto.getSeriesId());
			st.setString(index++, paymentAccountDto.getProvider());
			st.setString(index++, paymentAccountDto.getAccountNum());
			st.setString(index++, paymentAccountDto.getToken());
			st.setString(index++, paymentAccountDto.getStatus());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				paymentAccountId = rs.getInt(1);
			}

			if (paymentAccountId > 0) {
				index = 1;
				
	
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return paymentAccountId;
	}
	
	
	/**
	 * @param dto
	 * @return
	 */
	protected PaymentAccountDto getPaymentAccount(int paymentAccountId, String provider, int clubId, int seriesId, String status)
			throws Exception {
		
		List<PaymentAccountDto> paymentAccounts = new ArrayList<PaymentAccountDto>();
		
		paymentAccounts = getPaymentAccounts(paymentAccountId, provider, clubId, seriesId, status);
		
		if (paymentAccounts.size() == 0) {
			throw new Exception("Payment Account not found");
		} else if (paymentAccounts.size() > 0)
			return (PaymentAccountDto)paymentAccounts.get(0);
		else
			throw new Exception("More than one Payment Accounts available");
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PaymentAccountDto> getPaymentAccounts(int paymentAccountId, String provider, int clubId, int seriesId, String status)
			throws Exception {
		List<PaymentAccountDto> paymentAccounts = new ArrayList<PaymentAccountDto>();
		
		String query = SELECT_PAYMENT_ACCOUNTS_QUERY;
		
		boolean whereAdded = false;
		
		if (paymentAccountId > 0) {
			query += " where payment_account_id = ?";
			whereAdded = true;
		} 		
		
		if (!CommonUtility.isNullOrEmpty(provider)) {
			
			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "provider = ?";
			whereAdded = true;
		} 		
	
		if (clubId > 0) {

			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "club_id = ?";
			whereAdded = true;
		} 
		
		if (seriesId > 0) {

			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "series_id = ?";
			whereAdded = true;
		}

		if (!CommonUtility.isNullOrEmpty(status)) {
			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "status = ?";
			whereAdded = true;
		}
		
		query += " order by update_date desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DButility.getDefaultReadConnection();
			
			
			st = conn.prepareStatement(query);
			int index = 1;
			
			
			if (paymentAccountId > 0) {
				
				st.setInt(index++, paymentAccountId);
			} 		
			
			if (!CommonUtility.isNullOrEmpty(provider)) {
				
				st.setString(index++, provider);
			} 		
	
			if (clubId > 0) {
				st.setInt(index++, clubId);
			} 
			
			if (seriesId > 0) {
				st.setInt(index++, seriesId);
			}

			if (!CommonUtility.isNullOrEmpty(status)) {
				st.setString(index++, status);
			}
			
			rs = st.executeQuery();
			preparePaymentAccountList(paymentAccounts, rs);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return paymentAccounts;
	}

	private void preparePaymentAccountList(List<PaymentAccountDto> paymentAccounts, ResultSet rs)
			throws SQLException {
		
		while (rs.next()) {
			
			PaymentAccountDto paymentAccountDto = new PaymentAccountDto();
			
			paymentAccountDto.setPaymentAccountId(rs.getInt("payment_account_id"));
			paymentAccountDto.setClubId(rs.getInt("club_id"));
			paymentAccountDto.setSeriesId(rs.getInt("series_id"));
			paymentAccountDto.setProvider(rs.getString("provider"));
			paymentAccountDto.setAccountNum(rs.getString("account_num"));
			paymentAccountDto.setToken(rs.getString("token"));
			paymentAccountDto.setStatus(rs.getString("status"));
			
			paymentAccountDto.setCreationDate(rs.getTimestamp("creation_date"));
			paymentAccountDto.setUpdateDate(rs.getTimestamp("update_date"));
			
			paymentAccounts.add(paymentAccountDto);
		}
	}
}
