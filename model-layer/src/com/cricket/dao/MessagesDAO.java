/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.MessageDto;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessagesDAO {

	protected MessagesDAO(){
		
	}
	protected List<MessageDto> getMessages(int limit, int clubId) throws Exception { 
		List<MessageDto> messages = new ArrayList<MessageDto>();
		String query ="SELECT message_id,name,email,contact,date,message,subject FROM "+DButility.getDBName(clubId)+".messages WHERE subject NOT Like 'Index:%' and deleted is null ";
				
		query += "\norder by DATE desc ";

			if(limit != 0){
				query += "\n LIMIT ?";
			}
		Connection conn = DButility.getReadConnection(clubId, false);
		PreparedStatement pst = null;
		
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			if(limit != 0){
				pst.setInt(1,limit);
			}
		    rs = pst.executeQuery();
		while (rs.next()) {
			MessageDto message = new MessageDto();
			message.setMessageId(rs.getInt("message_id"));
			message.setName(rs.getString("name"));
			message.setEmail(rs.getString("email"));
			message.setContact(rs.getString("contact"));
			message.setDate(rs.getDate("date"));
			message.setSubject(rs.getString("subject"));
			message.setMessage(rs.getString("message"));
			messages.add(message);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return messages;

	}

	protected void insertMessage(MessageDto message,int clubId) throws Exception {

		String query ="insert into messages(name,email,contact,subject,message,date) values (?,?,?,?,?,NOW())";
		
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, DButility.escapeLine(message.getName()));
			pst.setString(2,DButility.escapeLine(message.getEmail()));
			pst.setString(3,DButility.escapeLine(message.getContact()));
			pst.setString(4,DButility.escapeLine(message.getSubject()));
			pst.setString(5,DButility.escapeLine(message.getMessage()));
			
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
	
	protected void insertDefaultMessage(MessageDto message) throws Exception {

		String query ="insert into messages(name,email,contact,subject,message,date) values (?,?,?,?,?,NOW())";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1,DButility.escapeLine(message.getName()));
			pst.setString(2,DButility.escapeLine(message.getEmail()));
			pst.setString(3,DButility.escapeLine(message.getContact()));
			pst.setString(4,"Index:"+ DButility.escapeLine(message.getSubject()));
			pst.setString(5, DButility.escapeLine(message.getMessage()));
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected void deleteMessage(int messageId,int clubId) throws Exception {

		String query =
			"update messages set deleted = '1' "
				+ " where message_id = ?" ;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,messageId);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
