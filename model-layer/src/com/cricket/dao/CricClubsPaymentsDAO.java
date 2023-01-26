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

import com.cricket.dto.CricClubsPaymentDto;
import com.cricket.utility.DButility;

/**
 * @author vk
 */
public class CricClubsPaymentsDAO {
		
	private static String SELECT_CRICCLUBS_PAYMENTS_QUERY = "select payment_id, payment_date, payment_desc, " +
			"				amount, orig_trx_amount, payment_source, source_id, provider, " +
			"				source_account_num, update_date from cricclubs_payments";
	
	private static String INSERT_CRICCLUBS_PAYMENTS = "INSERT INTO cricclubs_payments " +
			"				(payment_date, payment_desc, " +
			"				amount, orig_trx_amount, payment_source, source_id, provider," +
			"				source_account_num, update_date) VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?, NOW())";
	
	protected int createCricClubsPayment(CricClubsPaymentDto cricclubsPaymentDto) throws Exception {

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		int paymentId = 0;
		
		int index = 1;

		try {
			st = conn.prepareStatement(INSERT_CRICCLUBS_PAYMENTS, Statement.RETURN_GENERATED_KEYS);
				
			st.setString(index++, cricclubsPaymentDto.getPaymentDesc());
			st.setDouble(index++, cricclubsPaymentDto.getAmount());
			st.setDouble(index++, cricclubsPaymentDto.getOrigTrxAmount());
			st.setString(index++, cricclubsPaymentDto.getPaymentSource());
			st.setString(index++, cricclubsPaymentDto.getSourceId());
			st.setString(index++, cricclubsPaymentDto.getProvider());
			st.setString(index++, cricclubsPaymentDto.getSourceAccountNum());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				paymentId = rs.getInt(1);
			}

			if (paymentId > 0) {
				index = 1;				
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return paymentId;
	}
	
	
	/**
	 * @param dto
	 * @return
	 */
	protected CricClubsPaymentDto getCricClubsPayment(int paymentId)
			throws Exception {
		
		List<CricClubsPaymentDto> cricclubsPayments = new ArrayList<CricClubsPaymentDto>();
		
		cricclubsPayments = getCricClubsPayments(paymentId);
		
		if (cricclubsPayments.size() == 0) {
			throw new Exception("League Payment not found");
		} else if (cricclubsPayments.size() > 0)
			return (CricClubsPaymentDto)cricclubsPayments.get(0);
		else
			throw new Exception("More than one Payment Record Available");
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<CricClubsPaymentDto> getCricClubsPayments(int paymentId)
			throws Exception {
		
		List<CricClubsPaymentDto> payments = new ArrayList<CricClubsPaymentDto>();
		
		String query = SELECT_CRICCLUBS_PAYMENTS_QUERY;
		
		boolean whereAdded = false;
		
		if (paymentId > 0) {
			query += " where payment_id = ?";
			whereAdded = true;
		} 
				
		query += " order by update_date desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DButility.getDefaultConnection();			
			
			st = conn.prepareStatement(query);
			int index = 1;
			
			if (paymentId > 0) {
				
				st.setInt(index++, paymentId);
			} 		
						
			rs = st.executeQuery();
			prepareCricClubsPaymentList(payments, rs);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return payments;
	}

	private void prepareCricClubsPaymentList(List<CricClubsPaymentDto> payments, ResultSet rs)
			throws SQLException {
				
		while (rs.next()) {
			
			CricClubsPaymentDto cricclubsPaymentDto = new CricClubsPaymentDto();
			
			cricclubsPaymentDto.setPaymentId(rs.getInt("payment_id"));
			
			cricclubsPaymentDto.setPaymentDate(rs.getTimestamp("payment_date"));
			cricclubsPaymentDto.setPaymentDesc(rs.getString("payment_desc"));
			cricclubsPaymentDto.setAmount(rs.getDouble("amount"));
			cricclubsPaymentDto.setOrigTrxAmount(rs.getDouble("orig_trx_amount"));
			
			cricclubsPaymentDto.setPaymentSource(rs.getString("payment_source"));
			cricclubsPaymentDto.setSourceId(rs.getString("source_id"));
			cricclubsPaymentDto.setProvider(rs.getString("provider"));
			cricclubsPaymentDto.setSourceAccountNum(rs.getString("source_account_num"));
			
			cricclubsPaymentDto.setUpdateDate(rs.getTimestamp("update_date"));
			
			payments.add(cricclubsPaymentDto);
		}
	}
}
