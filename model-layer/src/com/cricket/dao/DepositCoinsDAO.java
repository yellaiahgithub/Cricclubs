package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.DepositCoinsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class DepositCoinsDAO {
	static Logger log = LoggerFactory.getLogger(DepositCoinsDAO.class);
	public String SELECT_QUERY = "select id,user_id,purchase_amount,currency_code,payment_via,txn_token,"
			+ "payment_gateway,status,coins,purchase_time from deposit_coins ";
	
	private void populateDepositDto(DepositCoinsDto depositDto, ResultSet rs) throws SQLException {
		
		depositDto.setId(rs.getLong("id"));
		depositDto.setUserId(rs.getLong("user_id"));
		depositDto.setPurchaseAmount(rs.getFloat("purchase_amount"));		
		depositDto.setCurrencyCode(rs.getString("currency_code"));
		depositDto.setPaymentVia(rs.getString("payment_via"));		
		depositDto.setTxnToken(rs.getString("txn_token"));
		depositDto.setPaymentGateway(rs.getString("payment_gateway"));
		depositDto.setStatus(rs.getInt("status"));
		depositDto.setCoins(rs.getInt("coins"));
		depositDto.setPurchaseTime(rs.getDate("purchase_time"));			
	}

	protected DepositCoinsDto getDepositInfo(DepositCoinsDto depositDto) throws Exception {
		
		String query = SELECT_QUERY+ " where 1=1 ";		
		
		if(depositDto.getId()>0) {
			query = query+" and id = ?";
		}
		if(depositDto.getUserId()>0) {
			query = query+" and user_id = ?";
		}		
		if(!CommonUtility.isNullOrEmptyOrNULL(depositDto.getTxnToken())) {
			query = query+" and txn_token = ?";
		}
			
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		DepositCoinsDto dbDepositDto = null;
		try {
			pst = conn.prepareStatement(query);		
			int index = 1;
			
			if(depositDto.getId()>0) {
				pst.setLong(index++,depositDto.getId());
			}
			if(depositDto.getUserId()>0) {
				pst.setLong(index++,depositDto.getUserId());
			}		
			if(!CommonUtility.isNullOrEmptyOrNULL(depositDto.getTxnToken())) {
				pst.setString(index++,depositDto.getTxnToken());
			}
			
			rs = pst.executeQuery();
			while (rs.next()) {
				dbDepositDto = new DepositCoinsDto();
				populateDepositDto(dbDepositDto, rs);				
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dbDepositDto;
	}	
	
	protected long insertDeposit(DepositCoinsDto depositsDto) throws Exception {
		
		String query ="insert into deposit_coins(user_id,purchase_amount,currency_code,txn_token,"
				+ "coins,purchase_time) values ( ?,?,?,?,?,NOW())";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		long depostId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			
			pst.setLong(index++,depositsDto.getUserId());
			pst.setFloat(index++,depositsDto.getPurchaseAmount());
			pst.setString(index++,depositsDto.getCurrencyCode());	
			pst.setString(index++,depositsDto.getTxnToken());
			pst.setInt(index++,depositsDto.getCoins());
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				depostId = rs.getLong(1);
			}
			// Add deposit to log	
			addDepositLog(depostId, 0, "Initiate Coins Deposit", conn);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return depostId;
	}

	protected void updateDeposit(DepositCoinsDto depositDto) throws Exception {
		
		String query ="update deposit_coins set payment_via=?,coins=?,status=1 where id=?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;		
		try {
			pst = conn.prepareStatement(query);
			
			pst.setString(1,depositDto.getPaymentVia());			
			pst.setInt(2,depositDto.getCoins());
			pst.setLong(3,depositDto.getId());
			
			pst.executeUpdate();
			
			// Add deposit to log	
			addDepositLog(depositDto.getId(), 1, "Complete Coins Deposit", conn);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}		
	}
	
	private void addDepositLog(long depostId, int status, String remarks, Connection conn) throws Exception {
		
		PreparedStatement st = null;			
		try {			
			String insertQuery = "insert into deposit_coins_log(deposit_id,status,remarks,created_date) "
					+ "values(?,?,?,NOW())";
			
			st = conn.prepareStatement(insertQuery);		
			
			st.setLong(1, depostId);			
			st.setInt(2, status);
			st.setString(3, remarks);
			
			st.executeUpdate();	
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {			
			DButility.closeStatement(st);			
		}
		
	}
	
}
