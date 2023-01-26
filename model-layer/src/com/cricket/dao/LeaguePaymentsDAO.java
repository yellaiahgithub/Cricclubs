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

import com.cricket.dto.LeaguePaymentDto;
import com.cricket.utility.DButility;

/**
 * @author vk
 */
public class LeaguePaymentsDAO {
	
	private static String SELECT_LEAGUE_PAYMENTS_QUERY = "select league_payment_id, payment_date, payment_desc, " +
			"				amount, orig_trx_amount,payment_from_type, payment_from_id, " +
			"				payment_to, creation_date, update_date from league_payments";
	
	private static String INSERT_LEAGUE_PAYMENTS = "INSERT INTO league_payments " +
			"				(payment_date, payment_desc, " +
			"				amount, orig_trx_amount,payment_from_type, payment_from_id, " +
			"				payment_to, checkout_activity_id, creation_date, update_date) VALUES (NOW(), ?, ?,?,?,?,?, ?,NOW(), NOW())";
	
	protected int createLeaguePayment(LeaguePaymentDto leaguePaymentDto, int clubId) throws Exception {

		if (clubId <=0)
			throw new Exception("Club ID is invalid: "+clubId);
		
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		int leaguePaymentId = 0;
		
		int index = 1;

		try {
			st = conn.prepareStatement(INSERT_LEAGUE_PAYMENTS, Statement.RETURN_GENERATED_KEYS);
			
			
			st.setString(index++, leaguePaymentDto.getPaymentDesc());
			st.setDouble(index++, leaguePaymentDto.getAmount());
			st.setDouble(index++, leaguePaymentDto.getOrigTrxAmount());
			st.setString(index++, leaguePaymentDto.getPaymentFromType());
			st.setString(index++, leaguePaymentDto.getPaymentFromId());
			st.setString(index++, leaguePaymentDto.getPaymentTo());
			st.setInt(index++, leaguePaymentDto.getCheckoutActivityId());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				leaguePaymentId = rs.getInt(1);
			}

			if (leaguePaymentId > 0) {
				index = 1;				
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return leaguePaymentId;
	}
	
	
	
	/**
	 * @param dto
	 * @return
	 */
	protected LeaguePaymentDto getLeaguePayment(int leaguePaymentId, int clubId)
			throws Exception {
		
		List<LeaguePaymentDto> leaguePayments = new ArrayList<LeaguePaymentDto>();
		
		leaguePayments = getLeaguePayments(leaguePaymentId, clubId);
		
		if (leaguePayments.size() == 0) {
			throw new Exception("League Payment not found");
		} else if (leaguePayments.size() > 0)
			return (LeaguePaymentDto)leaguePayments.get(0);
		else
			throw new Exception("More than one League Payment Record Available");
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<LeaguePaymentDto> getLeaguePayments(int leaguePaymentId, int clubId)
			throws Exception {
		
		List<LeaguePaymentDto> leaguePayments = new ArrayList<LeaguePaymentDto>();
		
		String query = SELECT_LEAGUE_PAYMENTS_QUERY;
		
		boolean whereAdded = false;
		
		if (leaguePaymentId > 0) {
			query += " where league_payment_id = ?";
			whereAdded = true;
		} 
				
		query += " order by update_date desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DButility.getReadConnection(clubId);
			
			
			st = conn.prepareStatement(query);
			int index = 1;
			
			
			if (leaguePaymentId > 0) {
				
				st.setInt(index++, leaguePaymentId);
			} 		
						
			rs = st.executeQuery();
			prepareLeaguePaymentList(leaguePayments, clubId, rs);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return leaguePayments;
	}

	private void prepareLeaguePaymentList(List<LeaguePaymentDto> leaguePayments, int clubId, ResultSet rs)
			throws SQLException {
		
		while (rs.next()) {
			
			LeaguePaymentDto leaguePaymentDto = new LeaguePaymentDto();
			
			leaguePaymentDto.setLeaguePaymentId(rs.getInt("league_payment_id"));
			leaguePaymentDto.setClubId(clubId);
			
			leaguePaymentDto.setPaymentDate(rs.getTimestamp("payment_date"));
			leaguePaymentDto.setPaymentDesc(rs.getString("payment_desc"));
			leaguePaymentDto.setAmount(rs.getDouble("amount"));
			leaguePaymentDto.setOrigTrxAmount(rs.getDouble("orig_trx_amount"));
			
			leaguePaymentDto.setPaymentFromType(rs.getString("payment_from_type"));
			leaguePaymentDto.setPaymentFromId(rs.getString("payment_from_id"));
			leaguePaymentDto.setPaymentTo(rs.getString("payment_to"));
			
			leaguePaymentDto.setUpdateDate(rs.getTimestamp("update_date"));
			
			leaguePayments.add(leaguePaymentDto);
		}
	}
}
