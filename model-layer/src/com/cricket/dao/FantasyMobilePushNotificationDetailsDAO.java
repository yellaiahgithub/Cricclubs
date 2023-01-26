package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cricket.dto.pushnotification.FantasyMobilePushNotificationDetailsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class FantasyMobilePushNotificationDetailsDAO {

	public FantasyPushNotificationCredentials getPushNotificationCredentialsByProviderAndType( String provider, String type) throws Exception {
		
		String query = "select * from fantasy_pushnotification_credentials where provider = ? and type = ? ";

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		FantasyPushNotificationCredentials pushNotificationCredentials = null;
		
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, provider);
			pst.setString(2, type);			
			rs = pst.executeQuery();
			while (rs.next()) {
				pushNotificationCredentials = populatePushNotificationCredentials(rs);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pushNotificationCredentials;
		
	}	

	private FantasyPushNotificationCredentials populatePushNotificationCredentials(
			ResultSet rs) throws SQLException {
		
		FantasyPushNotificationCredentials pushNotificationCredential = new FantasyPushNotificationCredentials();
		
		pushNotificationCredential.setProvider(rs.getString("provider"));
		pushNotificationCredential.setType(rs.getString("type"));
		pushNotificationCredential.setApiUrl(rs.getString("api_url"));
		pushNotificationCredential.setAuthorizationKey(rs.getString("authorization_key"));
		
		return pushNotificationCredential;
	}

	public int insetMobilePNNDetails(FantasyMobilePushNotificationDetailsDto fupnDto) throws Exception {
		
		String query =  "insert into mobile_push_notification_details(device_token, device_type, device_model, device_name, device_serial, "
				+ " os_version, user_id, display_name, is_Virtual, uuid, created_date, created_by, last_updated_by, last_updated_date) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, fupnDto.getDeviceToken());			
			pst.setString(index++,fupnDto.getDeviceType());
			pst.setString(index++, fupnDto.getDeviceModel());
			pst.setString(index++,fupnDto.getDeviceName());			
			pst.setString(index++, fupnDto.getDeviceSerial());			
			pst.setString(index++, fupnDto.getOsVersion());					
			pst.setLong(index++, fupnDto.getUserId());
			pst.setString(index++, fupnDto.getDisplayName());	
			pst.setBoolean(index++, fupnDto.isVirtual());			
			pst.setString(index++, fupnDto.getUUID());			
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
			pst.setString(index++, fupnDto.getCreatedBy());			
			pst.setString(index++, fupnDto.getLastUpdatedBy());			
			pst.setDate(index++, CommonUtility.utilToSqlDate(new Date()));			
				
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return 1;
	}

	public List<FantasyMobilePushNotificationDetailsDto> getMobilePNDetails( FantasyMobilePushNotificationDetailsDto fmpnDetails) throws Exception {
		
		String query = "select * from mobile_push_notification_details where 1 = 1 ";
		
		List<FantasyMobilePushNotificationDetailsDto> mobileNotificationDetails = new ArrayList<FantasyMobilePushNotificationDetailsDto>();
		
		
			if(!CommonUtility.isNullOrEmpty(fmpnDetails.getDeviceToken())){
				query +=  " and device_token = ? ";
			}			
			if(fmpnDetails.getUserId()>0){
				query +=  " and user_id = ? ";
			}
			if(!CommonUtility.isNullOrEmpty(fmpnDetails.getDisplayName())){
				query +=  " and display_name = ? ";
			}
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			
				if(!CommonUtility.isNullOrEmpty(fmpnDetails.getDeviceToken())){
					pst.setString(index++, fmpnDetails.getDeviceToken());
				}								
				if(fmpnDetails.getUserId()>0){
					pst.setLong(index++,  fmpnDetails.getUserId());
				}
				if(!CommonUtility.isNullOrEmpty(fmpnDetails.getDisplayName())){
					pst.setString(index++,  fmpnDetails.getDisplayName());
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
	
	protected List<String> getFantasyUserDeviceTokens() throws Exception {
		
		String query = " SELECT DISTINCT device_token FROM mobile_push_notification_details ";	
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> deviceTokens = new ArrayList<String>();		
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			while (rs.next()) {					
				deviceTokens.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return deviceTokens;
	}

	private FantasyMobilePushNotificationDetailsDto populateMobileNotificationDetails(
			ResultSet rs) throws SQLException {
		
		FantasyMobilePushNotificationDetailsDto mobileNotificationDetail = new FantasyMobilePushNotificationDetailsDto();
		
		mobileNotificationDetail.setId(rs.getLong("id"));
		mobileNotificationDetail.setDeviceToken(rs.getString("device_token"));
		mobileNotificationDetail.setDeviceType(rs.getString("device_type"));
		mobileNotificationDetail.setDeviceModel(rs.getString("device_model"));
		mobileNotificationDetail.setDeviceName(rs.getString("device_name"));
		mobileNotificationDetail.setDeviceSerial(rs.getString("device_serial"));
		mobileNotificationDetail.setOsVersion(rs.getString("os_version"));
		mobileNotificationDetail.setDisplayName(rs.getString("display_name"));
		mobileNotificationDetail.setUserId(rs.getLong("user_id"));
		mobileNotificationDetail.setVirtual(rs.getBoolean("is_Virtual"));
		mobileNotificationDetail.setUUID(rs.getString("UUID"));
		mobileNotificationDetail.setCreatedDate(rs.getDate("created_date"));
		mobileNotificationDetail.setCreatedBy(rs.getString("created_by"));
		mobileNotificationDetail.setLastUpdatedBy(rs.getString("last_updated_by"));
		mobileNotificationDetail.setLastUpdatedDate(rs.getDate("last_updated_date"));
		
		return mobileNotificationDetail;
	}

	public int updateMobilePNDetails(FantasyMobilePushNotificationDetailsDto mobileNotificationDetail) throws Exception {
		String query = "UPDATE mobile_push_notification_details SET device_token=?, device_type = ?, device_model = ?, device_name = ?, device_serial = ?, "
				+ " os_version = ?, display_name = ?, user_id = ?, is_virtual = ?, uuid = ?, last_updated_date= now() WHERE id = ?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, mobileNotificationDetail.getDeviceToken());			
			pst.setString(index++,mobileNotificationDetail.getDeviceType());
			pst.setString(index++, mobileNotificationDetail.getDeviceModel());
			pst.setString(index++,mobileNotificationDetail.getDeviceName());			
			pst.setString(index++, mobileNotificationDetail.getDeviceSerial());			
			pst.setString(index++, mobileNotificationDetail.getOsVersion());			
			pst.setString(index++, mobileNotificationDetail.getDisplayName());			
			pst.setLong(index++, mobileNotificationDetail.getUserId());			
			pst.setBoolean(index++, mobileNotificationDetail.isVirtual());			
			pst.setString(index++, mobileNotificationDetail.getUUID());					
			pst.setLong(index++, mobileNotificationDetail.getId());			
							
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return 1;
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
				tokens.add(rs.getString("device_token"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return tokens;
		
	}

	public List<String> getFantasyUserDeviceTokensByCountry(String selectedCountryString, String testUserId) throws Exception {
		
		String query = " SELECT distinct device_token FROM mobile_push_notification_details mpn join user_fantasy u on mpn.user_id = u.user_id join countries c on c.code = u.country_code where c.id in ( "
				+ "  "+selectedCountryString+" ) " ;
								
				if(!CommonUtility.isNullOrEmptyOrNULL(testUserId)) {
					query+= "and u.user_id = (select user_id from user where email = '"+testUserId+"' ) ";
				}

		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> deviceTokens = new ArrayList<String>();		
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			while (rs.next()) {					
				deviceTokens.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return deviceTokens;
	}


	public List<String> getFantasyUserMobileNoByCountry(String selectedCountryString, String testUserId) throws Exception {
		
		String query = "select concat('+', uf.mobile_code, u.phone) as mobile_no from user_fantasy uf join user u on uf.user_id = u.user_id and uf.country_code in "
				+ " (select code from countries where id in ("+selectedCountryString+")) ";
		
		if(!CommonUtility.isNullOrEmptyOrNULL(testUserId)) {
			query+= " and u.email = '"+testUserId+"'";
		}
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> mobileNums = new ArrayList<String>();		
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			while (rs.next()) {					
				mobileNums.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return mobileNums;
	}

	public List<String> getFantasyUsereEmailsByCountry(String selectedCountryString, String testUserId) throws Exception {
		
		String query = "select u.email from user_fantasy uf join user u on uf.user_id = u.user_id and uf.country_code in "
				+ " (select code from countries where id in ("+selectedCountryString+")) ";
		
		if(!CommonUtility.isNullOrEmptyOrNULL(testUserId)) {
			query+= " and u.email = '"+testUserId+"'";
		}
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> mobileNums = new ArrayList<String>();		
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			while (rs.next()) {					
				mobileNums.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return mobileNums;
	}
}
