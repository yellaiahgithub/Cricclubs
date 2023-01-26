package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.NotificationDto;
import com.cricket.utility.DButility;

public class NotificationDAO {
	
	static Logger log = LoggerFactory.getLogger(NotificationDAO.class);
	
	public int addNotification(NotificationDto dto) throws Exception {

		String query =  "insert into notifications(logged_in_user_id,user_id,first_name,last_name,profile_pic_path,"
				+ "read_unread_status,message,created_date) values(?,?,?,?,?,?,?,now())";
				
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setInt(index++, dto.getLoggedInUserId());			
			pst.setInt(index++,dto.getUserId());
			pst.setString(index++, dto.getFirstName());
			pst.setString(index++,dto.getLastName());			
			pst.setString(index++, dto.getProfilePicPath());			
			pst.setInt(index++, dto.getReadUnreadStatus());			
			pst.setString(index++, dto.getMessage());			
				
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return 1;
	}
	
	protected int addNotifications(List<NotificationDto> notificationList) throws Exception {
		
		String query = "insert into notifications(logged_in_user_id,club_id,fixture_id,team_id,match_id," 
					+ "read_unread_status,message,message_type,created_date) values (?,?,?,?,?,?,?,?,now())";
		
		int noOfRecords = 0;		
		Connection conn = DButility.getDefaultConnection();
		
		PreparedStatement pst = null;		
		try {
			pst = conn.prepareStatement(query);
			
			conn.setAutoCommit(false);
			
			for(NotificationDto dto : notificationList) {
				
				int index = 1;
				
				pst.setInt(index++, dto.getLoggedInUserId());			
				pst.setInt(index++,dto.getClubId());
				pst.setInt(index++,dto.getFixtureId());
				pst.setInt(index++,dto.getTeamId());
				pst.setInt(index++,dto.getMatchId());
				pst.setInt(index++, 0);			
				pst.setString(index++, dto.getMessage());		
				pst.setString(index++, dto.getMessageType());
				
				pst.addBatch();
			}
			int[] count = pst.executeBatch();			
			conn.commit();
			
			noOfRecords = count.length;
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);			
		}
		return noOfRecords;
	}

	protected List<NotificationDto> getNotificationList(int loggedInUserId) throws Exception {
		
		List<NotificationDto> notifications = new ArrayList<NotificationDto>();
		
		String query = "select id,logged_in_user_id,user_id,club_id,match_id,fixture_id,team_id,"
				+ "first_name,last_name,profile_pic_path,created_date, read_unread_status,message, message_type "
				+ " from notifications where logged_in_user_id = ? ";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, loggedInUserId);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				
				NotificationDto notification = new NotificationDto();
				
				notification.setNotificationId(rs.getInt("id"));
				notification.setLoggedInUserId(rs.getInt("logged_in_user_id"));
				notification.setUserId(rs.getInt("user_id"));
				notification.setClubId(rs.getInt("club_id"));
				notification.setMatchId(rs.getInt("match_id"));
				notification.setFixtureId(rs.getInt("fixture_id"));
				notification.setTeamId(rs.getInt("team_id"));
				notification.setFirstName(rs.getString("first_name"));
				notification.setLastName(rs.getString("last_name"));
				notification.setNotificationDate(rs.getDate("created_date"));
				notification.setProfilePicPath(rs.getString("profile_pic_path"));
				notification.setReadUnreadStatus(rs.getInt("read_unread_status"));
				notification.setMessage(rs.getString("message"));
				notification.setMessageType(rs.getString("message_type"));
				
				notifications.add(notification);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return notifications;
	}
	
	protected int getUnreadNotificationsCount(int loggedInUserId) throws Exception {

		int notificationsCount = 0;

		String query = "select count(*) notifications_count from notifications "
				+ "where logged_in_user_id = ? and read_unread_status = 0";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, loggedInUserId);

			rs = pst.executeQuery();

			while (rs.next()) {
				notificationsCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return notificationsCount;
	}
	
	public void updateNotifications(long notificationId, int userId) throws Exception {

		String query = "update notifications set read_unread_status = 1  where logged_in_user_id = ?";
		
		if(notificationId>0) {
			query += " and id = ?";
		}

		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1,userId);			
			if(notificationId>0) {
				pst.setLong(2,notificationId);
			}			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}	
	}
	
	public void clearNotifications(long notificationId, int userId) throws Exception {

		String query = "delete from notifications where logged_in_user_id = ?";
		if(notificationId>0) {
			query += " and id = ?";
		}

		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			
			pst.setInt(1,userId);			
			if(notificationId>0) {
				pst.setLong(2,notificationId);
			}			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}	
	}

	public void addClientRequestLog(int clubId, String clientIp, String target) throws Exception {
		
		String query = "insert into mcc.client_request_logs(request_time, club_id, target, client_pub_ip) values(now(),?,?,?)";
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1,clubId);
			pst.setString(2,target);
			pst.setString(3,clientIp);
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
