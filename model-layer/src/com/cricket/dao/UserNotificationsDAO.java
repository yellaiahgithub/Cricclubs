/*
 * Created on Feb 5, 2019
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.UserNotificationsDto;
import com.cricket.utility.DButility;

public class UserNotificationsDAO {
	
	protected UserNotificationsDAO() {

	}
	protected List<UserNotificationsDto> getUserNotifications(int clubId, int userId, int limit) throws Exception {
		
		List<UserNotificationsDto> undtos = new ArrayList<UserNotificationsDto>();
		String query = "SELECT user_id, scorecard, news, article, comments, album, documents, "  
				+ "match_reminder, weekly_summary, marketing from mcc.user_notifications where 1 = 1 ";
		
		
		if (userId > 0) {
			query += "\nand user_id =? " ;
		}							
		if (limit != 0) {
			query += "\n LIMIT ? ";
		}
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			
			if (userId > 0) {
				pst.setInt(i++, userId);				
			}									
			if(limit != 0) {
				pst.setInt(i++, limit);
			}			
			rs = pst.executeQuery();
			while (rs.next()) {	
				
				UserNotificationsDto undto = new UserNotificationsDto();
				
				undto.setUserId(rs.getInt("user_id"));
				undto.setScorecard(rs.getInt("scorecard"));
				undto.setNews(rs.getInt("news"));				
				undto.setArticles(rs.getInt("article"));				
				undto.setComments(rs.getInt("comments"));
				undto.setAlbum(rs.getInt("album"));
				undto.setDocuments(rs.getInt("documents"));
				undto.setMatchReminder(rs.getInt("match_reminder"));				
				undto.setWeeklySummary(rs.getInt("weekly_summary"));
				undto.setMarketing(rs.getInt("marketing"));
				
				undtos.add(undto);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return undtos;

	}
	protected int getIsUserWeeklySummary(int userId) throws Exception {
		
		String query = "SELECT weekly_summary from mcc.user_notifications where user_id = "+userId;
		int weeklySummary = 0;
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);					
			rs = pst.executeQuery();
			while (rs.next()) {				
				weeklySummary = rs.getInt("weekly_summary");
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return weeklySummary;
	}
	protected void insertUserNotification(UserNotificationsDto undto) throws Exception {
		
		String query = "insert into mcc.user_notifications(user_id, scorecard, comments, " 
						+ "match_reminder) values (?,?,?,?) ";

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			pst.setLong(i++, undto.getUserId());
			pst.setInt(i++, undto.getScorecard());
			//pst.setInt(i++, undto.getNews());
			//pst.setInt(i++, undto.getArticles());
			pst.setInt(i++, undto.getComments());
			//pst.setInt(i++, undto.getAlbum());
			//pst.setInt(i++, undto.getDocuments());
			pst.setInt(i++, undto.getMatchReminder());
			//pst.setInt(i++, undto.getWeeklySummary());
			//pst.setInt(i++, undto.getMarketing());			
						
			pst.executeUpdate();						
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void updateUserNotification(UserNotificationsDto undto) throws Exception {

		String query = "update mcc.user_notifications set scorecard=?,news=?,article=?,"
				+ "comments=?,album=?,documents=?,match_reminder=?,"
				+ "weekly_summary=?,marketing=? where user_id = ? ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		
		try {			
			pst = conn.prepareStatement(query);
			
			int i = 1;
			
			pst.setInt(i++, undto.getScorecard());
			pst.setInt(i++, undto.getNews());
			pst.setInt(i++, undto.getArticles());
			pst.setInt(i++, undto.getComments());
			pst.setInt(i++, undto.getAlbum());
			pst.setInt(i++, undto.getDocuments());
			pst.setInt(i++, undto.getMatchReminder());
			pst.setInt(i++, undto.getWeeklySummary());
			pst.setInt(i++, undto.getMarketing());
			pst.setLong(i++, undto.getUserId());
			
			pst.executeUpdate();			
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
