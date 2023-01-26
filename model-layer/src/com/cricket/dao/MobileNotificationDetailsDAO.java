package com.cricket.dao;

import static com.cricket.utility.ApplicationConstants.BY_DEVICE_SERIAL;
import static com.cricket.utility.ApplicationConstants.BY_DEVICE_TYPE;
import static com.cricket.utility.ApplicationConstants.BY_TOKEN;
import static com.cricket.utility.ApplicationConstants.BY_TOKEN_PREVIOUS;
import static com.cricket.utility.ApplicationConstants.BY_USER_ID;
import static com.cricket.utility.ApplicationConstants.BY_USER_NAME;
import static com.cricket.utility.ApplicationConstants.BY_UUID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.MatchDto;
import com.cricket.dto.pushnotification.MobileNotificationDetail;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

import io.jsonwebtoken.lang.Collections;

public class MobileNotificationDetailsDAO {
	
	
	Logger log = LoggerFactory.getLogger(MobileNotificationDetailsDAO.class);
	
	public List<PushNotificationCredential> getMobileNotificationCredentialsByProviderAndType( String provider, String type, int clubId) throws Exception {
		
		String query = "select * from pushnotification_credentials where provider = ? and type = ? and club_id in ( 0, ?) order by club_id";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<PushNotificationCredential> pushNotificationCredentials = new ArrayList<PushNotificationCredential>();
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, provider);
			pst.setString(2, type);
			pst.setInt(3, clubId);
			rs = pst.executeQuery();
			while (rs.next()) {
				pushNotificationCredentials.add(populatePushNotificationCredentials(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pushNotificationCredentials;
		
	}
	
	public List<PushNotificationCredential> getMobileNotificationCredentialsByProviderAndType(int clubId) throws Exception {
		
		String query = "select * from pushnotification_credentials where club_id in ( 0, ?) order by club_id";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<PushNotificationCredential> pushNotificationCredentials = new ArrayList<PushNotificationCredential>();
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			rs = pst.executeQuery();
			while (rs.next()) {
				pushNotificationCredentials.add(populatePushNotificationCredentials(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pushNotificationCredentials;
		
	}
	
	public PushNotificationCredential getMobileNotificationCredentialsByProvider(String provider) throws Exception {
		
		String query = "select * from pushnotification_credentials where provider = ?";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		PushNotificationCredential pnc = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, provider);
			rs = pst.executeQuery();
			while (rs.next()) {
				pnc = populatePushNotificationCredentials(rs);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pnc;
		
	}

	private PushNotificationCredential populatePushNotificationCredentials(
			ResultSet rs) throws SQLException {
		PushNotificationCredential pushNotificationCredential = new PushNotificationCredential();
		pushNotificationCredential.setProvider(rs.getString("provider"));
		pushNotificationCredential.setType(rs.getString("type"));
		pushNotificationCredential.setApiUrl(rs.getString("api_url"));
		pushNotificationCredential.setAuthorizationKey(rs.getString("authorization_key"));
		pushNotificationCredential.setClubId(rs.getInt("club_id"));
		pushNotificationCredential.setApiKeyId(rs.getString("apiKeyId"));
		return pushNotificationCredential;
	}

	public int create(MobileNotificationDetail mobileNotificationDetail) throws Exception {
		//int clubId;

		String query =  "insert into pushnotification_user_detail(token, device_type, device_model, device_name, device_serial, "
				+ " os_version, user_name, user_id, is_Virtual, UUID, created_date, created_by, "
				+ "lastUpdatedBy, lastUpdatedDate, is_one_signal_token) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, mobileNotificationDetail.getToken());			
			pst.setString(index++,mobileNotificationDetail.getDeviceType());
			pst.setString(index++, mobileNotificationDetail.getDeviceModel());
			pst.setString(index++,mobileNotificationDetail.getDeviceName());			
			pst.setString(index++, mobileNotificationDetail.getDeviceSerial());			
			pst.setString(index++, mobileNotificationDetail.getOsVersion());			
			pst.setString(index++, mobileNotificationDetail.getUserName());			
			pst.setLong(index++, mobileNotificationDetail.getUserID());			
			pst.setBoolean(index++, mobileNotificationDetail.isVirtual());			
			pst.setString(index++, mobileNotificationDetail.getUUID());			
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
			pst.setString(index++, mobileNotificationDetail.getCreatedBy());			
			pst.setString(index++, mobileNotificationDetail.getLastUpdatedBy());			
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
			pst.setInt(index++, mobileNotificationDetail.getIsOneSignalToken());			
			
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return 1;
	}

	public List<MobileNotificationDetail> getMobileNotificationDetails( MobileNotificationDetail mobileNotificationDetail, List<String> checkConditions) throws Exception {
		String query = "select * from pushnotification_user_detail where is_one_signal_token = 1 and token != 'undefined' ";
		List<MobileNotificationDetail> mobileNotificationDetails = new ArrayList<MobileNotificationDetail>();
		if(!CommonUtility.isListNullEmpty(checkConditions)){
			if(checkConditions.contains(BY_TOKEN)){
				query +=  " and token = ? ";
			}
			if(checkConditions.contains(BY_TOKEN_PREVIOUS)){
				query +=  " and token = ? ";
			}
			if(checkConditions.contains(BY_UUID)){
				query +=  " and UUID = ? ";
			}
			if(checkConditions.contains(BY_DEVICE_SERIAL)){
				query +=  " and device_serial = ? ";
			}
			if(checkConditions.contains(BY_USER_ID)){
				query +=  " and user_id = ? ";
			}
			if(checkConditions.contains(BY_USER_NAME)){
				query +=  " and user_name = ? ";
			}
			if(checkConditions.contains(BY_DEVICE_TYPE)){
				query +=  " and device_type = ? ";
			}
		}
		
		query +=  " order by lastUpdatedDate desc ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			if(!CommonUtility.isListNullEmpty(checkConditions)){
				if(checkConditions.contains(BY_TOKEN)){
					pst.setString(index++, mobileNotificationDetail.getToken());
				}
				if(checkConditions.contains(BY_TOKEN_PREVIOUS)){
					pst.setString(index++, mobileNotificationDetail.getPreviousToken());
				}
				if(checkConditions.contains(BY_UUID)){
					pst.setString(index++,  mobileNotificationDetail.getUUID());
				}
				if(checkConditions.contains(BY_DEVICE_SERIAL)){
					pst.setString(index++,  mobileNotificationDetail.getDeviceSerial());
				}
				if(checkConditions.contains(BY_USER_ID)){
					pst.setLong(index++,  mobileNotificationDetail.getUserID());
				}
				if(checkConditions.contains(BY_USER_NAME)){
					pst.setString(index++,  mobileNotificationDetail.getUserName());
				}
				if(checkConditions.contains(BY_DEVICE_TYPE)){
					pst.setString(index++,  mobileNotificationDetail.getDeviceType());
				}
			}
			
			rs = pst.executeQuery();
			while (rs.next()) {
				mobileNotificationDetails.add(populateMobileNotificationDetails(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return mobileNotificationDetails;
		
	}
	
	public List<String> getMobileTokensForNewsPublish() throws Exception {
		
		String query = "SELECT distinct token FROM pushnotification_user_detail WHERE is_one_signal_token = 1 AND token != 'undefined' "
				+ " AND user_id NOT IN ( SELECT user_id FROM user_club uc, club c WHERE uc.club_id = c.club_id AND c.optout_marketing = 1 ) ";
		
		List<String> tokenList = new ArrayList<String>();
			
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokenList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokenList;
		
	}
	
	public List<MobileNotificationDetail> getMobileNotificationCredentialsByCountries(String country) throws Exception {
		
		String query = "SELECT * FROM mcc.pushnotification_user_detail "
				+ "WHERE is_one_signal_token = 1 and user_id IN ( SELECT user_id FROM mcc.user_club WHERE club_id IN "
				+ "( SELECT club_id FROM club WHERE country IN ('"+country+"') AND club_id>0 ) AND user_id>0)";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MobileNotificationDetail> notificationCredentials = new ArrayList<MobileNotificationDetail>();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				notificationCredentials.add(populateMobileNotificationDetails(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return notificationCredentials;
		
	}
	
	public List<String> getUserMobileTokensByPlayerIds(String playerIds) throws Exception {
		
		List<String> tokenIds = new ArrayList<String>();
		
		String query = "select distinct(pud.token) as mobile_token from pushnotification_user_detail pud, mcc.user u "
				+ " where pud.user_id = u.user_id and pud.is_one_signal_token = 1 and pud.token != 'undefined' "
				+ "and u.player_id in ("+playerIds+") ";
		
		query +=  " order by lastUpdatedDate desc ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokenIds.add(rs.getString("mobile_token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokenIds;
		
	}
	
	public List<String> getUserMobileTokensByUserIds(String userIds) throws Exception {
		
		List<String> tokenIds = new ArrayList<String>();
		
		String query = "select distinct(token) as mobile_token from pushnotification_user_detail "
				+ " where is_one_signal_token = 1 and token != 'undefined' and user_id in ("+userIds+") ";
		
		query +=  " order by lastUpdatedDate desc ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokenIds.add(rs.getString("mobile_token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokenIds;
		
	}

	private MobileNotificationDetail populateMobileNotificationDetails(
			ResultSet rs) throws SQLException {
		MobileNotificationDetail mobileNotificationDetail = new MobileNotificationDetail();
		mobileNotificationDetail.setId(rs.getLong("id"));
		mobileNotificationDetail.setToken(rs.getString("token"));
		mobileNotificationDetail.setDeviceType(rs.getString("device_type"));
		mobileNotificationDetail.setDeviceModel(rs.getString("device_model"));
		mobileNotificationDetail.setDeviceName(rs.getString("device_name"));
		mobileNotificationDetail.setDeviceSerial(rs.getString("device_serial"));
		mobileNotificationDetail.setOsVersion(rs.getString("os_version"));
		mobileNotificationDetail.setUserName(rs.getString("user_name"));
		mobileNotificationDetail.setUserID(rs.getLong("user_id"));
		mobileNotificationDetail.setVirtual(rs.getBoolean("is_Virtual"));
		mobileNotificationDetail.setUUID(rs.getString("UUID"));
		mobileNotificationDetail.setCreatedDate(rs.getDate("created_date"));
		mobileNotificationDetail.setCreatedBy(rs.getString("created_by"));
		mobileNotificationDetail.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
		mobileNotificationDetail.setLastUpdatedDate(rs.getDate("lastUpdatedDate"));
		mobileNotificationDetail.setIsOneSignalToken(rs.getInt("is_one_signal_token"));
		
		return mobileNotificationDetail;
	}

	public int updateRecord(MobileNotificationDetail mobileNotificationDetail) throws Exception {
		String query = "UPDATE pushnotification_user_detail SET token=?, device_type = ?, device_model = ?, device_name = ?, device_serial = ?, "
				+ " os_version = ?, user_name = ?, user_id = ?, is_Virtual = ?, UUID = ?, lastUpdatedBy = ? , "
				+ "lastUpdatedDate=? , is_one_signal_token = ? WHERE id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, mobileNotificationDetail.getToken());			
			pst.setString(index++,mobileNotificationDetail.getDeviceType());
			pst.setString(index++, mobileNotificationDetail.getDeviceModel());
			pst.setString(index++,mobileNotificationDetail.getDeviceName());			
			pst.setString(index++, mobileNotificationDetail.getDeviceSerial());			
			pst.setString(index++, mobileNotificationDetail.getOsVersion());			
			pst.setString(index++, mobileNotificationDetail.getUserName());			
			pst.setLong(index++, mobileNotificationDetail.getUserID());			
			pst.setBoolean(index++, mobileNotificationDetail.isVirtual());			
			pst.setString(index++, mobileNotificationDetail.getUUID());	
			pst.setString(index++, mobileNotificationDetail.getLastUpdatedBy());	
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
			pst.setLong(index++, mobileNotificationDetail.getId());	
			pst.setInt(index++, mobileNotificationDetail.getIsOneSignalToken());		
							
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return 1;
	}

	public int mapUserWithPushNotification(String mobToken, int userID, String userName) throws Exception {

		String query = "UPDATE pushnotification_user_detail SET user_name = ?, user_id = ?, lastUpdatedBy = ? , lastUpdatedDate=? WHERE token=?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, userName);			
			pst.setLong(index++, userID);	
			pst.setString(index++, userName);	
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
			pst.setString(index++, mobToken);		
			result = pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		if(result <= 0) {
			MobileNotificationDetail mobileNotificationDetail =  new MobileNotificationDetail();
			mobileNotificationDetail.setUserID(userID);
			mobileNotificationDetail.setUserName(userName);
			mobileNotificationDetail.setCreatedBy("New After login");
			mobileNotificationDetail.setToken(mobToken);
			mobileNotificationDetail.setLastUpdatedBy("New After login");
			mobileNotificationDetail.setDeviceType("Mobile");
			mobileNotificationDetail.setDeviceModel("Not Available");
			mobileNotificationDetail.setDeviceName("Not Available");
			mobileNotificationDetail.setDeviceSerial("Not Available");
			mobileNotificationDetail.setOsVersion("Not Available");
			mobileNotificationDetail.setUUID("Not Available");
			mobileNotificationDetail.setVirtual(false);
			create(mobileNotificationDetail);
		}
		return result;
	
	}

	public Map<String, String> getTokensForNotification(String matchId, int clubId, String notificationType) throws Exception {
		
		String query = "select distinct device_token, notification_type from mcc.user_push_notifications where club_id = ? and fixture_id = "
				+ " (select fixture_id from fixtures where match_id = ?) and notification_type like ? ";

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> tokens = new HashMap<String, String>();
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setString(2, matchId);
			pst.setString(3, notificationType);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokens.put(rs.getString("device_token"), rs.getString("notification_type"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokens;
		
	}
	
	public List<String> getTokensByNotificationType(int fixtureId, int clubId, String notificationType) throws Exception {
		
		String query = "select distinct device_token from mcc.user_push_notifications "
				+ "where club_id = ? and fixture_id = ? and notification_type like ? ";

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> tokens = new ArrayList<String>();
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setInt(2, fixtureId);
			pst.setString(3, notificationType);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokens.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokens;
		
	}
	

	public List<String> getTokenByCustomQueryForNotification(String query) throws Exception {
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> tokens = new ArrayList<String>();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokens.add(rs.getString("token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokens;
		
	}

	public List<String> getTokenByClubid(int clubId) throws Exception {
		
		String query = "SELECT pn.token FROM pushnotification_user_detail pn left join user_club uv on uv.user_id = pn.user_id "
				+ " where uv.club_id = ? and pn.token != 'undefined' and pn.is_one_signal_token = 1 and pn.token != 'undefined' ";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> tokenIds = new ArrayList<String>();
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			rs = pst.executeQuery();
			while (rs.next()) {
				tokenIds.add(rs.getString("token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokenIds;
		
	}

	public List<String> getFevTeamTokenList(MatchDto match) throws Exception {
		
		String query = "select distinct token from pushnotification_user_detail pud, user_fav_countries ufc, cricket_country cc where pud.user_id = ufc.user_id "
				+ " and ufc.country_id = cc.id and (cc.name = ? or cc.name = ?) and pud.is_one_signal_token = 1 and pud.token != 'undefined'";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> tokenIds = new ArrayList<String>();
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, match.getTeamOneName());
			pst.setString(2, match.getTeamTwoName());
			rs = pst.executeQuery();
			while (rs.next()) {
				tokenIds.add(rs.getString("token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokenIds;
		
	}
}
