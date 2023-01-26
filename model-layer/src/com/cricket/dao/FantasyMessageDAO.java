package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.FantasyMessageDto;
import com.cricket.utility.DButility;

public class FantasyMessageDAO {
	
	public String SELECT_QUERY = "select id,name,email,phone,purpose,message,message_date from fantasy_messages ";
	
	private void populateFantasyMessageDto(FantasyMessageDto messageDto, ResultSet rs) throws SQLException {
		
		messageDto.setId(rs.getInt("id"));
		messageDto.setName(rs.getString("name"));
		messageDto.setEmail(rs.getString("email"));
		messageDto.setPhone(rs.getString("phone"));
		messageDto.setPurpose(rs.getString("purpose"));
		messageDto.setMessage(rs.getString("message"));
		messageDto.setMessageDate(rs.getDate("message_date"));
	}
	
	protected List<FantasyMessageDto> getFantasyMessages() throws Exception {
		
		List<FantasyMessageDto> fantasyMessages = new ArrayList<FantasyMessageDto>();
		
		String query = SELECT_QUERY +" where 1=1 ";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		FantasyMessageDto messageDto = null;
		try {
			st = conn.prepareStatement(query);			
			
			rs = st.executeQuery();	
			
			while (rs.next()) {
				messageDto = new FantasyMessageDto();
				populateFantasyMessageDto(messageDto,rs);				
				fantasyMessages.add(messageDto);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return fantasyMessages;
	}
	
	protected int insertFantasyMessage(FantasyMessageDto messageDto) throws Exception {
		
		String query ="insert into fantasy_messages(name,email,phone,purpose,message,message_date) "
				+ "values (?,?,?,?,?,NOW())";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		int messageId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			
			pst.setString(index++,messageDto.getName());	
			pst.setString(index++,messageDto.getEmail());
			pst.setString(index++,messageDto.getPhone());	
			pst.setString(index++,messageDto.getPurpose());
			pst.setString(index++,messageDto.getMessage());
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				messageId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return messageId;
	}
	
	
}
