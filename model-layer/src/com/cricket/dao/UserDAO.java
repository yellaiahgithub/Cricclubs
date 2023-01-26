/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.Pair;
import com.cricket.dto.UserDto;
import com.cricket.dto.UserNotificationsDto;
import com.cricket.dto.lite.UserLiteDto;
import com.cricket.helpers.UserDtoComparator;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.cricket.utility.batch.GraphDBBackGroundProcess;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UserDAO {
	
	private static Logger log = LoggerFactory.getLogger(UserDAO.class);
	
	public String SELECT_QUERY =
			"select  u.user_id,  " +
					"u.user_name," +
					"u.password," +
					"u.email, " +
					"u.is_active, " +
					"u.player_id, " +
					"u.umpire_id, " +
					"u.access_level, " +
					"u.f_name, " +
					"u.l_name, " +
					"u.gender, " +
					"u.date_of_birth, " +
					"u.phone, " +
					"u.address, " +
					"u.city, " +
					"u.state, " +
					"u.country_code, " +
					"u.postal_code, " +
					"u.club_id, " +					
					"u.player_profile_search, " +
					"u.profile_image_path, " + 
					"u.user_type, u.user_type_id, " + 
					"u.back_ground_image_path, " + 
					"u.no_of_matches_streaming " +
					" from mcc.user_view u ";

	protected UserDto getUser(String userName, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" where u.user_name= ? and u.club_id = ? ";
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			int index = 1;
			st.setString(index++, userName);
			st.setInt(index++, clubId);
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
			}

			checkIfUserIsCaptain(conn, user);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	//Testing dev pipieline
	protected UserDto getUserByUserName(String userName, int clubId) throws Exception {
		String query = SELECT_QUERY + " where u.user_name= ? LIMIT 1 ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);			
			st.setString(1, userName);			
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
			}

			checkIfUserIsCaptain(conn, user);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;

	}
	
	protected UserDto getAllUser(String userName) throws Exception {
		String query = SELECT_QUERY +
				" where u.user_name= ? ";
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, userName);			
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, user.getClubId());				
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	protected List<UserDto> getGlobalUsers(String userName, String password) throws Exception {
		String query = SELECT_QUERY +
				" where u.user_name= ?";
		
		if (password != null)
				query += " and u.password = ? ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<UserDto> users = new ArrayList<UserDto>();
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setString(index++, userName);
			
			if (password != null)
				st.setString(index++, password);
			
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, user.getClubId());
				checkIfUserIsCaptain(conn, user);
				users.add(user);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;

	}

	private void checkIfUserIsCaptain(Connection conn, UserDto user) throws SQLException {
		if (user != null && user.getPlayerID() != 0) {
			String query = "select 1 from team where is_active = '1' and (captain = ? OR vice_captain = ? ) ";
			PreparedStatement st = conn.prepareStatement(query);
			int index=1;
			st.setInt(index++, user.getPlayerID());
			st.setInt(index++, user.getPlayerID());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				user.setIsCaptain(true);
			}
			if(rs != null){
				rs.close();
			}
			if(st != null){
				st.close();
			}
		}
	}

	
	public List<Integer> captainTeams(int playerId,int clubId) throws Exception {
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> teams = new ArrayList<Integer>();
		try{
		String query = "select team_id from team where is_active = '1' and (captain = ? OR vice_captain = ? ) ";
		st = conn.prepareStatement(query);
		int index =1;
		st.setInt(index++, playerId);
		st.setInt(index++, playerId);
		rs = st.executeQuery();
		while (rs.next()) {
			teams.add(rs.getInt("team_id"));
		}
	} catch (SQLException e) {
		throw new Exception(e.getMessage());
	} finally {
		DButility.dbCloseAll(conn, st, rs);
	}
		
		
		return teams;
	}
	protected UserDto getUserById(int userId, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" where u.user_id= ? and u.club_id = ?";
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setInt(index++, userId);
			st.setInt(index++, clubId);
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
			}

			checkIfUserIsCaptain(conn, user);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;

	}
	
	protected UserDto getUserByIdV1(int userId) throws Exception {
		
		String query = "select user_id,user_name,password,f_name,l_name,gender,date_of_birth, "
				+ "email,phone,is_active,player_id,profile_image_path, back_ground_image_path, "
				+ "no_of_matches_streaming  from user where user_id= ?";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setInt(index++, userId);
			rs = st.executeQuery();
			
			while (rs.next() && user == null) {
				
				user = new UserDto();
				
				user.setUserID(rs.getInt("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setPassword(rs.getString("password"));
				user.setFname(rs.getString("f_name"));
				user.setLname(rs.getString("l_name"));
				user.setGender(rs.getString("gender"));
				user.setDateOfBirth(rs.getString("date_of_birth"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setIsActive(rs.getInt("is_active"));
				user.setPlayerID(rs.getInt("player_id"));
				user.setProfileImagePath(rs.getString("profile_image_path"));
				user.setBackGroundImagePath(rs.getString("back_ground_image_path"));
				user.setNoOfMatchesStreaming(rs.getInt("no_of_matches_streaming"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	
	protected String getUserFullName(int userId) throws Exception {
		String query = " select concat(f_name,' ',l_name) full_name from user where user_id= ?";
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String userFullName = null;
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setInt(index++, userId);
			rs = st.executeQuery();
			while (rs.next()) {
				userFullName = rs.getString("full_name");
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userFullName;
	}
	
	protected UserDto getUserByToken(String token, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" where u.token= ? and (u.club_id = ? or u.club_id = 0)";
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			int index = 1;
			st.setString(index++, token);
			st.setInt(index++, clubId);
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
				user.setToken(token);
			}

			checkIfUserIsCaptain(conn, user);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;

	}	
	
	protected List<Integer> getUserIdsByTokens(List<String> authTokenList) throws Exception {
		
		String query = "select user_id from mcc.user where token in (";
		
		for(int i=0; i<authTokenList.size(); i++) {
			query += "?,";
		}
		query = query.substring(0, query.length()-1)+")";		
		Connection conn = null;		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> userIdList = new ArrayList<Integer>();
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			int index = 1;
			for(String token : authTokenList) {
				st.setString(index++, token);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {				
				userIdList.add(rs.getInt(1));				
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userIdList;
	}
	
	protected UserDto getUserByToken(String token) throws Exception {
		
		String query = "select  u.user_id, u.user_name, u.password,u.email, u.is_active, u.player_id, u.umpire_id, "
				+ "u.access_level, u.f_name, u.l_name, u.gender, u.date_of_birth, u.phone, u.address, u.city, "
				+ "u.state, u.country_code, u.postal_code, u.player_profile_search, u.profile_image_path, "
				+ "u.back_ground_image_path, u.no_of_matches_streaming  from mcc.user u where u.token= ?";
		
		Connection conn = DButility.getDefaultReadConnection();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, token);
			rs = st.executeQuery();
			
			while (rs.next()) {
				
				user = new UserDto();
				
				user.setUserID(rs.getInt("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setIsActive(rs.getInt("is_active"));
				user.setPlayerID(rs.getInt("player_id"));
				user.setUmpireID(rs.getInt("umpire_id"));
				user.setAccessLevel(rs.getString("access_level"));
				user.setFname(rs.getString("f_name"));
				user.setLname(rs.getString("l_name"));
				user.setPhone(rs.getString("phone"));
				user.setGender(rs.getString("gender"));
				user.setDateOfBirth(rs.getString("date_of_birth"));
				user.setAddress(rs.getString("address"));
				user.setCity(rs.getString("city"));
				user.setState(rs.getString("state"));
				user.setCountryCode(rs.getString("country_code"));
				user.setPostalCode(rs.getString("postal_code"));		
				user.setPlayerProfileSearch(rs.getString("player_profile_search"));	
				user.setProfileImagePath(rs.getString("profile_image_path"));
				user.setToken(token);
				user.setBackGroundImagePath(rs.getString("back_ground_image_path"));
				user.setNoOfMatchesStreaming(rs.getInt("no_of_matches_streaming"));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}	

	private void pupulateUserRoles(UserDto user, Connection conn, int clubId) throws Exception {
		String query = "select role, attribute from mcc.user_roles where club_id = ? and user_id = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index =1;
			pst.setInt(index++, clubId);
			pst.setInt(index++, user.getUserID());
			rs = pst.executeQuery();
			while (rs.next()) {
				int role = rs.getInt("role");
				user.getRoles().add(role);
				// Set SeriesIDs if User is Admin with any of the series.
				if(role == 5) {
					List<Integer> seriesList = CommonUtility.stringToListOfInt(rs.getString("attribute"));
					if(!CommonUtility.isListNullEmpty(seriesList)) {
						user.getSeriesAdmin().addAll(seriesList);						
					}
				}
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
		}
	}

	private void populateUserDto(UserDto user, ResultSet rs) throws SQLException {
		
		user.setUserID(rs.getInt("user_id"));
		user.setUserName(rs.getString("user_name"));
		user.setPassword(rs.getString("password"));
		user.setEmail(rs.getString("email"));
		user.setIsActive(rs.getInt("is_active"));
		user.setPlayerID(rs.getInt("player_id"));
		user.setUmpireID(rs.getInt("umpire_id"));
		user.setAccessLevel(rs.getString("access_level"));
		user.setFname(rs.getString("f_name"));
		user.setLname(rs.getString("l_name"));
		user.setGender(rs.getString("gender"));
		user.setDateOfBirth(rs.getString("date_of_birth"));
		user.setPhone(rs.getString("phone"));
		user.setAddress(rs.getString("address"));
		user.setCity(rs.getString("city"));
		user.setState(rs.getString("state"));
		user.setCountryCode(rs.getString("country_code"));
		user.setPostalCode(rs.getString("postal_code"));		
		user.setClubId(rs.getInt("club_id"));		
		user.setPlayerProfileSearch(rs.getString("player_profile_search"));	
		user.setProfileImagePath(rs.getString("profile_image_path"));
		user.setUserType(rs.getString("user_type"));
		user.setUserTypeId(rs.getString("user_type_id"));
		user.setBackGroundImagePath(rs.getString("back_ground_image_path"));
		user.setNoOfMatchesStreaming(rs.getInt("no_of_matches_streaming"));
		
	
	}

	protected List<UserDto> getUserByEmail(String email, int clubId) throws Exception {
		String query =
				SELECT_QUERY +
						" where email= ? ";
		if(clubId != 0){
			query += "and u.club_id = ?";
		}
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<UserDto> users = new ArrayList<UserDto>();
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setString(index++, email);
			if(clubId != 0){
				st.setInt(index++, clubId);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, user.getClubId());
				user.setClubId(user.getClubId());
				checkIfUserIsCaptain(conn, user);
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;

	}
	
	protected boolean verifyUserByEmail(String email) throws Exception {
		
		boolean isExists = false;
		
		String query = " select 1 from user where email = ? and is_active = 1 ";
		
		Connection conn = DButility.getDefaultReadConnection();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, email);
			rs = st.executeQuery();
			while (rs.next()) {
				isExists = true;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return isExists;

	}
	
	protected boolean verifyUserByPhone(String phone) throws Exception {
		
		boolean isExists = false;
		
		String query = " select 1 from user where phone = ? and is_active = 1";
		
		Connection conn = DButility.getDefaultReadConnection();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, phone);
			rs = st.executeQuery();
			while (rs.next()) {
				isExists = true;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return isExists;

	}
	
	public List<UserLiteDto> getUserBasicDetailsWithEmail(int userId) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, profile_image_path, email, phone, is_active "
				+ "from user where email is not null and email != '' and email like '%@%'";
		
		if(userId>0) {
			query = query + " and user_id = ?";
		}				
		
		UserLiteDto user = null;
		
		List<UserLiteDto> userDtos = new ArrayList<UserLiteDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(userId>0) {
				st.setInt(1, userId);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {

				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setProfileImangePath(rs.getString("profile_image_path"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setActive(rs.getInt("is_active")==1?true:false);
				
				userDtos.add(user);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userDtos;
	}
	
	public List<UserLiteDto> getAcademyUsersBasicDetails(int userId) throws Exception {
		
		String query = "select u.user_id, u.player_id, u.l_name, u.f_name, u.profile_image_path, u.email, u.phone "
				+ "from user u, user_club uc where u.user_id = uc.user_id and uc.user_type_id>0 and uc.user_type is not null and uc.user_type != '' ";
		
		if(userId>0) {
			query = query + " and user_id = ?";
		}				
		
		UserLiteDto user = null;
		
		List<UserLiteDto> userDtos = new ArrayList<UserLiteDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(userId>0) {
				st.setInt(1, userId);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {

				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setProfileImangePath(rs.getString("profile_image_path"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				
				userDtos.add(user);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userDtos;
	}
	
	public List<UserLiteDto> getUserBasicDetailsWithNoEmail(int userId) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, profile_image_path "
				+ "from user where email is null or email = '' or email not like '%@%'";
		
		if(userId>0) {
			query = query + " and user_id = ?";
		}				
		
		UserLiteDto user = null;
		
		List<UserLiteDto> userDtos = new ArrayList<UserLiteDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(userId>0) {
				st.setInt(1, userId);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {

				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setProfileImangePath(rs.getString("profile_image_path"));
				
				userDtos.add(user);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userDtos;
	}
	
	public UserLiteDto getUserBasicDetailsByUserId(int userId) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, profile_image_path, is_active "
				+ "from user where 1 = 1 ";		
		if(userId>0) {
			query += " user_id = "+userId;		
		}
		
		UserLiteDto user = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			
			while (rs.next()) {

				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setProfileImangePath(rs.getString("profile_image_path"));
				user.setActive(rs.getInt("user_id")==1?true:false);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	public List<UserLiteDto> getUserBasicDetailsByEmailPhoneList(String email, String phone, String checkBy) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, is_active from user where ";
		if("email".equalsIgnoreCase(checkBy)) {
			query += "email = ?";
		}else if("phone".equalsIgnoreCase(checkBy)) {
			query += "phone = ?";
		}
		
		UserLiteDto user = null;
		List<UserLiteDto> users = new ArrayList<UserLiteDto>();
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if("email".equalsIgnoreCase(checkBy)) {
				st.setString(1, email);
			}else if("phone".equalsIgnoreCase(checkBy)) {
				st.setString(1, phone);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {
				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setActive(rs.getInt("is_active")==1?true:false);
				
				users.add(user);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	public UserLiteDto getUserBasicDetailsByEmail(String email) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, is_active from user where email = ?";
		
		UserLiteDto user = null;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(!CommonUtility.isNullOrEmpty(email)) {
				st.setString(1, email);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {
				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setActive(rs.getInt("is_active")==1?true:false);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	public List<UserLiteDto> getUserBasicDetailsByPhoneList(String phoneNumber) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, country_code, is_active from user where phone = ?";
		
		UserLiteDto user = null;
		List<UserLiteDto> users = new ArrayList<UserLiteDto>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(!CommonUtility.isNullOrEmpty(phoneNumber)) {
				st.setString(1, phoneNumber);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {
				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setCountryCode(rs.getString("country_code"));
				user.setActive(rs.getInt("is_active")==1?true:false);
				
				users.add(user);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	public UserLiteDto getUserBasicDetailsByPhone(String phoneNumber) throws Exception {
		
		String query = "select user_id, player_id, l_name, f_name, country_code, is_active from user where phone = ?";
		
		UserLiteDto user = null;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			if(!CommonUtility.isNullOrEmpty(phoneNumber)) {
				st.setString(1, phoneNumber);
			}
			rs = st.executeQuery();
			
			while (rs.next()) {
				user = new UserLiteDto();
				
				user.setUserId(rs.getInt("user_id"));
				user.setPlayerId(rs.getInt("player_id"));
				user.setfName(rs.getString("f_name"));
				user.setlName(rs.getString("l_name"));
				user.setCountryCode(rs.getString("country_code"));
				user.setActive(rs.getInt("is_active")==1?true:false);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	
	public Map<Integer, Integer> getUserClubMap() throws Exception {
		
		String query = "SELECT club_id, user_id FROM mcc.user_club WHERE club_id> 0 order by club_id, user_id";
		
		Map<Integer, Integer> userClubMap = new LinkedHashMap<Integer, Integer>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);			
			rs = st.executeQuery();
			
			while (rs.next()) {
				userClubMap.put(rs.getInt(2),rs.getInt(1));				
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userClubMap;
	}
	
	protected int getUserIdByEmail(String email) throws Exception {
		String query = "select user_id from mcc.user where upper(trim(email)) = upper(trim(?)) LIMIT 1";
		int userId = 0;
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;		
		try {
			st = conn.prepareStatement(query);			
			st.setString(1, email);
			rs = st.executeQuery();
			while (rs.next()) {
				userId = rs.getInt("user_id");
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return userId;
	}
	protected List<UserDto> getUserEmail(String email) throws Exception {
		String query =
				SELECT_QUERY +
						" where email= ? ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<UserDto> users = new ArrayList<UserDto>();
		try {
			st = conn.prepareStatement(query);
			st.setString(1, email);			
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, user.getClubId());
				user.setClubId(user.getClubId());				
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	protected List<UserDto> getUserName(String userName) throws Exception {
		String query =
				SELECT_QUERY +
						" where user_name= ? ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<UserDto> users = new ArrayList<UserDto>();
		try {
			st = conn.prepareStatement(query);
			st.setString(1, userName);			
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, user.getClubId());
				user.setClubId(user.getClubId());				
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	protected List<UserDto> getUsersByNotification(String notificationType, int clubId) throws Exception {
		List<UserDto> users = new ArrayList<UserDto>();
		String query = SELECT_QUERY + " , mcc.user_notifications un  "; 
				
				query += " where u.club_id = ? AND u.user_id = un.user_id ";

				if ( notificationType.equalsIgnoreCase("News") ) {
					query += " AND un.news = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("Article") ) {
					query += " AND un.article = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("ScoreCard") ) {
					query += " AND un.scorecard = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("Comments") ) {
					query += " AND un.comments = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("Album") ) {
					query += " AND un.album = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("Documents") ) {
					query += " AND un.documents = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("MatchReminder") ) {
					query += " AND un.match_reminder = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("WeeklySummary") ) {
					query += " AND un.weekly_summary = 1 ";
				}
				if ( notificationType.equalsIgnoreCase("Marketing") ) {
					query += " AND un.marketing = 1 ";
				}
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	protected void addExistingUserToClub(int userId, int clubId, String accessLevel, String loggedinuser) throws Exception {
		
		String query = "insert into user_club(user_id,club_id,access_level) values (?,?,?)";
		Connection conn = null; 
		PreparedStatement st = null;
		
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			st.setLong(1, userId);
			st.setInt(2, clubId);
			st.setString(3, accessLevel);
			
			st.executeUpdate();
			
			auditUserClub(userId+"", clubId+"", loggedinuser, "insert", conn);
			
		}catch(Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
	}
	protected long registerUser(UserDto user, int clubId, String loggedinuser) throws Exception {

		String query = "insert into user(user_name,password,email,is_active,player_id,umpire_id,f_name, "
				+ "l_name,gender,date_of_birth,phone,address,city,state,country_code,postal_code,"
				+ "profile_image_path,add_request_from,email_verification,mobile_verification) "
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		long userId = 0;
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		ResultSet rs = null;
		
		try {
			
			//st = conn.createStatement();			
			// st.executeUpdate();
			int index = 1;
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			st.setString(index++, DButility.escapeQuotes(user.getUserName()));
			st.setString(index++, user.getPassword());
			st.setString(index++, CommonUtility.trimString(user.getEmail()));
			st.setString(index++, "1");
			st.setInt(index++, user.getPlayerID());			
			st.setInt(index++, user.getUmpireID());
			st.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getFname()))));
			st.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getLname()))));
			st.setString(index++, user.getGender());
			st.setString(index++, user.getDateOfBirth());
			st.setString(index++, CommonUtility.trimString(user.getPhone()));
			st.setString(index++, CommonUtility.trimString(user.getAddress()));
			st.setString(index++, CommonUtility.trimString(user.getCity()));
			st.setString(index++, CommonUtility.trimString(user.getState()));
			st.setString(index++, CommonUtility.trimString(user.getCountryCode()));
			st.setString(index++, CommonUtility.trimString(user.getPostalCode()));	
			st.setString(index++, user.getProfileImagePath());
			st.setString(index++, user.getAddRequestFrom());	
			st.setInt(index++, user.getEmailValidation());
			st.setInt(index++, user.getMobileValidation());
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				userId = rs.getInt(1);
			}
			UserNotificationsDto undto = new UserNotificationsDto();			
			undto.setUserId(userId);
			
			UserNotificationsFactory.insertUserNotification(undto);
			
			String clubQuery = "insert into user_club(user_id,club_id,access_level) values (?,?,?)";
			st1 = conn.prepareStatement(clubQuery);
			st1.setLong(1, userId);
			st1.setInt(2, clubId);
			st1.setString(3, user.getAccessLevel());
			st1.executeUpdate();
						
			// Audit Log Methods Called and parameters sent
			String userid = Long.toString(userId);
			String clubid = Integer.toString(clubId);

			auditUser(userid, clubid, loggedinuser, "insert", conn);
			auditUserClub(userid, clubid, loggedinuser, "insert", conn);

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(st1);
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		if(userId>0 && clubId != 7683) {
			user.setUserID(CommonUtility.longToInt(userId));
			GraphDBBackGroundProcess.addUserToGraphDB(user, clubId);					
		}
		
		return userId;
	}
	
	protected long registerUserV1(UserDto user) throws Exception {

		String query = "insert into user(user_name,password,f_name,l_name,email,phone,"
				+ "country_code,is_active,add_request_from ) values (?,?,?,?,?,?,?,?,?)";
		
		long userId = 0;
		
		Connection conn = DButility.getDefaultConnection();		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			int index = 1;
			
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			st.setString(index++, user.getUserName());
			st.setString(index++, user.getPassword());
			st.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getFname()))));
			st.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getLname()))));
			st.setString(index++, CommonUtility.trimString(user.getEmail()));
			st.setString(index++, CommonUtility.trimString(user.getPhone()));
			st.setString(index++, CommonUtility.trimString(user.getCountryCode()));
			st.setInt(index++, user.getIsActive());
			st.setString(index++, user.getAddRequestFrom());
			
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				userId = rs.getInt(1);
			}
			auditUserV1(userId, "insert", conn, "self");

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}		
		if(userId>0) {
			user.setUserID(CommonUtility.longToInt(userId));
			GraphDBBackGroundProcess.addUserToGraphDB(user,0);
		}
		return userId;
	}
	
	protected void updateUser(UserDto user, int clubId, String loggedinuser) throws Exception {	
		
		int userId = user.getUserID();
		
		String query = "update user set user_name = ?,"
						+ "password = ?,"
						+ "email =?,"
						+ "f_name = ?,"
						+ "l_name =?,"
						+ "gender = ?,"
						+ "date_of_birth =?,"
						+ "phone =?,"
						+ "address =?,"
						+ "city =?,"
						+ "state =?,"
						+ "country_code =?,"
						+ "postal_code =?,"						
						+ "player_profile_search = ?, "
						+ "profile_image_path = ?"
						+ " where user_id = ?";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		PreparedStatement deletePST = null;
		PreparedStatement insertPST = null;
		
		try {
			st = conn.prepareStatement(query);
			int i = 1;
			st.setString(i++, CommonUtility.trimString(user.getUserName()));
			st.setString(i++, user.getPassword());
			st.setString(i++, CommonUtility.trimString(user.getEmail()));
			st.setString(i++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getFname()))));
			st.setString(i++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getLname()))));
			st.setString(i++, user.getGender());
			st.setString(i++, user.getDateOfBirth());
			st.setString(i++, user.getPhone());
			st.setString(i++, user.getAddress());
			st.setString(i++, user.getCity());
			st.setString(i++, user.getState());
			st.setString(i++, CommonUtility.trimString(user.getCountryCode()));
			st.setString(i++, user.getPostalCode());			
			st.setString(i++, user.getPlayerProfileSearch());
			st.setString(i++, user.getProfileImagePath());
			st.setInt(i++, userId);
			st.executeUpdate();

			deletePST = conn.prepareStatement("delete from user_roles where club_id = ? and user_id = ?");
			deletePST.setInt(1, clubId);
			deletePST.setInt(2, userId);
			deletePST.executeUpdate();

			if (!user.getRoles().isEmpty()) {
				insertPST = conn.prepareStatement("insert into user_roles(user_id,club_id,role, attribute) values(?,?,?,?)");
				for (Integer role : user.getRoles()) {
					if (role > 0) {
						insertPST.setInt(1, userId);
						insertPST.setInt(2, clubId);
						insertPST.setInt(3, role);
						if(role == 5) {
							insertPST.setString(4,CommonUtility.listOfIntToString(user.getSeriesAdmin()));
						}else {
							insertPST.setString(4, null);
						}
						insertPST.addBatch();
					}
				}
				insertPST.executeBatch();
			}			
			//Audit Log Methods Called and parameters sent 
			String userid = Integer.toString(userId);
			String clubid = Integer.toString(clubId);
			
			auditUser(userid,clubid,loggedinuser,"update",conn);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (deletePST != null) {
				deletePST.close();
			}
			if (insertPST != null) {
				insertPST.close();
			}
			DButility.closeConnectionAndStatement(conn, st);
		}		
		if(clubId!=7683) {
			GraphDBBackGroundProcess.updateGraphUser(user);			
		}
	}
	
	protected void updateUserV1(UserDto user, String changeBy) throws Exception {	
		
		String query = "update user set password = ?, email=?, phone=?, f_name=?, l_name=?, gender=?, "
				+ "date_of_birth=?, country_code=? where user_id=?";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(query);
			int i = 1;
			st.setString(i++, user.getPassword());
			st.setString(i++, CommonUtility.trimString(user.getEmail()));
			st.setString(i++, CommonUtility.trimString(user.getPhone()));
			st.setString(i++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getFname()))));
			st.setString(i++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(user.getLname()))));
			st.setString(i++, user.getGender());	
			st.setString(i++, user.getDateOfBirth());	
			st.setString(i++, user.getCountryCode());	
			st.setInt(i++, user.getUserID());
			
			st.executeUpdate();
			
			auditUserV1(user.getUserID(), "update", conn, changeBy);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		GraphDBBackGroundProcess.updateGraphUser(user);		
	}
	
	protected void updateUserUmpireId(int umpireId, int userId, String loggedinuser) throws Exception {
		String query = "update user set umpire_id =? where user_id =? ";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, umpireId);
			st.setInt(2, userId);
			st.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String userid = Integer.toString(userId);
			
			auditUser(userid,"",loggedinuser,"update",conn);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserPlayerId(int playerId, int userId, String loggedinuser) throws Exception {
		
		String query = "update user set player_id =? where user_id =? ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			st.setInt(2, userId);
			st.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String userid = Integer.toString(userId);			
			auditUser(userid,"",loggedinuser,"update",conn);		

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		try{
			UserDto user = UserFactory.getUserByPlayerId(playerId);
			if(user != null) {
				GraphDBBackGroundProcess.updateGraphUser(user);	
			}
		}catch(Exception e) {
			log.error(" Graph User Update Error "+e.getMessage());
		}		
	}
	protected void updateUserStatus(int userId, int status) throws Exception {
		
		String query = "update user set is_active =? where user_id =? ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, status);
			st.setInt(2, userId);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		try{
			UserDto user = UserFactory.getUserByIdV1(userId);
			if(user != null) {
				GraphDBBackGroundProcess.updateGraphUser(user);	
			}
		}catch(Exception e) {
			log.error(" Graph User Update Error "+e.getMessage());
		}
	}
	protected void updateUserAuthToken(int userId, String authToken) throws Exception {
		
		String query = "update user set token =? where user_id =? ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, authToken);
			st.setInt(2, userId);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected void updateUsersAuthToken(String userIds, String authToken) throws Exception {
		
		String query = "update user set token =? where user_id in ("+userIds+") ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, authToken);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserCountry(int userId, String countryCode) throws Exception {
		
		String query = "update user set country_code =? where user_id =? ";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, countryCode.trim());
			st.setInt(2, userId);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserPlayerImagePath(int playerId, int userId, String loggedinuser) throws Exception {
		String query = "update player set profilepic_file_path ='' where player_id = " + playerId;
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);				
			st.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String userid = Integer.toString(userId);
			
			auditUser(userid,"",loggedinuser,"update",conn);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserToken(String token, int userId) throws Exception {
		String query = "update user set token =?,last_access_date = NOW() where user_id =? ";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, token);
			st.setInt(2, userId);
			st.executeUpdate();		

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserProfilePicPath(String profileImagePath, int userId) throws Exception {
		
		String query = "update user set profile_image_path = ? where user_id =? ";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, profileImagePath);
			st.setInt(2, userId);
			st.executeUpdate();	

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserBackGroundImagePath(String backGroundImagePath, int userId) throws Exception {
		
		String query = "update user set back_ground_image_path = ? where user_id =? ";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, backGroundImagePath);
			st.setInt(2, userId);
			st.executeUpdate();		

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserPassword(String password, long userId) throws Exception {
		
		String query = "update user set password = ? where user_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, password);
			st.setLong(2, userId);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUserPasswordByEmail(String email, String password) throws Exception {
		
		String query = "update user set password = ? where email = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, password);
			st.setString(2, email);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected void updateUsersPasswords(String password, String userIds) throws Exception {
		
		String query = "update user set password = ? where user_id in ("+userIds+")";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, password);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	
	protected void updateUserNoOfStreamingMatches(int noOfMatches, long userId) throws Exception {
		String query = "update user set no_of_matches_streaming = ? where user_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, noOfMatches);
			st.setLong(2, userId);
			st.executeUpdate();
			
			auditUserV1(userId, "update", conn, "self");
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected void updateUserColumnByUserId(String column, String value, UserDto user) throws Exception {
		
		String query = "update user set "+column+" = ? where user_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, value);
			st.setInt(2, user.getUserID());
			
			st.executeUpdate();
			
			auditUserV1(user.getUserID(), "update", conn, "self");
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		GraphDBBackGroundProcess.updateGraphUser(user);	
	}
	
	protected void deleteUserToken(String token) throws Exception {
		String query = "update user set token ='',last_access_date = NOW() where token =? ";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, token);
			st.executeUpdate();
			

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}	
	protected boolean updateUserSubscriptions(int userId, String notificationType) throws Exception {
		String query = "";
		if(notificationType.trim().equalsIgnoreCase("weeklymail"))
			query = "update mcc.user_notifications set weekly_summary = 0 where user_id = "+userId;
		else if(notificationType.trim().equalsIgnoreCase("marketingmail"))
			query = "update mcc.user_notifications set marketing = 0 where user_id = "+userId;
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);			
			if( st.executeUpdate()>0 )
				return true;
			else 
				return false;
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}	
	protected List<UserDto> getUsersWhoPlayedMatch(long matchId, int clubId) throws Exception {
		List<UserDto> users = new ArrayList<UserDto>();
		String query = "select  u.user_id,  u.user_name,u.email, u.f_name, u.l_name, u.club_id, u.player_id "
				+ " from mcc.user_view u join match_player_team mpt  on u.player_id = mpt.player_id and mpt.match_id = ? and u.club_id = ? ;";

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			st.setLong(1, matchId);
			st.setInt(2, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				populateLiteUserDto(user, rs);
				//pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	private void populateLiteUserDto(UserDto user, ResultSet rs) throws SQLException {
		user.setUserID(rs.getInt("user_id"));
		user.setUserName(rs.getString("user_name"));
		user.setEmail(rs.getString("email"));
		user.setFname(rs.getString("f_name"));
		user.setLname(rs.getString("l_name"));
		user.setClubId(rs.getInt("club_id"));		
		user.setPlayerID(rs.getInt("player_id"));
	}

	public void registerExistingUser(UserDto user, int clubId, String loggedinuser) throws Exception {
		registerExistingUserWithAccessLevel(user, clubId, loggedinuser, false);
	}
	
	public void registerExistingUserWithAccessLevel(UserDto user, int clubId, String loggedinuser, boolean isPracticeLeagueAdmin) throws Exception {

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			String clubQuery = "";
			
			if(loggedinuser.equalsIgnoreCase("academy")) {
				if(isPracticeLeagueAdmin){
					clubQuery = "insert into user_club(user_id,club_id, access_level, user_type, user_type_id) values (?,?,?,?,?)";
				}else{
					clubQuery = "insert into user_club(user_id,club_id, user_type, user_type_id) values (?,?,?,?)";
				}
			}else {
				if(isPracticeLeagueAdmin){
					clubQuery = "insert into user_club(user_id,club_id, access_level) values (?,?,?)";
				}else{
					clubQuery = "insert into user_club(user_id,club_id) values (?,?)";
				}
			}
			
			
			st = conn.prepareStatement(clubQuery);
			int i= 1;
			if(loggedinuser.equalsIgnoreCase("academy")) {
				st.setInt(i++, user.getUserID());
				st.setInt(i++, clubId);
				if(isPracticeLeagueAdmin){
					st.setString(i++, "1");
				}
				st.setString(i++, user.getUserType());
				st.setInt(i++, Integer.parseInt(user.getUserTypeId()));
			}else {
				st.setInt(1, user.getUserID());
				st.setInt(2, clubId);
				if(isPracticeLeagueAdmin){
					st.setString(3, "1");
				}
			}
			st.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String userid = Integer.toString(user.getUserID());
			String clubid = Integer.toString(clubId);
			
			auditUserClub(userid,clubid,loggedinuser,"insert",conn);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	protected UserDto getUser(int playerID, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" where u.player_id= ?";
		if (clubId > 0) {
			query += " and u.club_id = ?";
		}
		if(playerID <= 0) {
			return null;
		}
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, playerID);
			if (clubId > 0) {
				st.setInt(2, clubId);
			}

			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
			}

			checkIfUserIsCaptain(conn, user);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	
	protected Map<Integer, Integer> getPlayerUserIdMapByPlayerIds(String playerIds) throws Exception {
		
		String query = "select player_id, user_id from user where player_id in ("+playerIds+")";
		
		Map<Integer, Integer> playerUserIdMap = new HashMap<Integer, Integer>();
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			
			while (rs.next()) {
				playerUserIdMap.put(rs.getInt(1), rs.getInt(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerUserIdMap;
	}

	protected UserDto getUmpireUser(int umpireId, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" where u.umpire_id= ?";
		if (clubId > 0) {
			query += " and u.club_id = ?";
		}
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, umpireId);
			if (clubId > 0) {
				st.setInt(2, clubId);
			}
			
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);

			}

			checkIfUserIsCaptain(conn, user);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}

	protected UserDto getUserBySocialId(String id, int socialTypeId, int clubId) throws Exception {
		String query = SELECT_QUERY +
				" , mcc.user_social us where u.user_id= us.user_id and us.social_id = ? and social_type_id = ? and u.club_id = ?";
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		UserDto user = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, id);
			st.setInt(2, socialTypeId);
			st.setInt(3, clubId);
			rs = st.executeQuery();
			while (rs.next() && user == null) {
				user = new UserDto();
				populateUserDto(user, rs);
				pupulateUserRoles(user, conn, clubId);
				user.setClubId(clubId);
			}

			checkIfUserIsCaptain(conn, user);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return user;
	}
	
	protected Map<String, Integer> getAllUserNames(int clubId) throws Exception {
		
		String query = "select u.user_id,concat(u.f_name,' ',l_name) name from mcc.user_view u where u.club_id = ? "
				+ "and u.email is not null and u.email like '%@%.%' order by 2";
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String,Integer> users = new LinkedHashMap<String,Integer>();
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				users.put(rs.getString(2), rs.getInt(1));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	protected Map<Integer, String> getUserIdNameMap(int clubId) throws Exception {
		
		String query = "select u.user_id,concat(u.f_name,' ',l_name) name from mcc.user_view u where u.club_id = ? "
				+ "and u.email is not null and u.email like '%@%.%' order by 2";
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<Integer, String> users = new LinkedHashMap<Integer, String>();
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				users.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	protected Map<Integer, String> getUserIdNameMapForUserIds(List<Long> userIds) throws Exception {
		
		String userIdsStr = "";
		for(long uid : userIds) {
			userIdsStr += uid+",";
		}
		userIdsStr = userIdsStr.substring(0, userIdsStr.length()-1);
		
		String query = "select user_id,concat(f_name,' ',l_name) name from mcc.user where user_id in ("+userIdsStr+")";
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<Integer, String> users = new LinkedHashMap<Integer, String>();
		try {
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				users.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return users;
	}
	
	protected void addSocialUser(int userId, String socialId, int socialTypeId) throws Exception {
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;

		try {
			String clubQuery = " insert into user_social (user_id,social_id, social_type_id) values (?,?,?) ";
			st = conn.prepareStatement(clubQuery);
			st.setInt(1, userId);
			st.setString(2, socialId);
			st.setInt(3, socialTypeId);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	public List<String> getAllPlayersEmail() throws Exception {
		String query = "select distinct email from user where player_id > 0";
		List<String> emailList = new ArrayList<String>();
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				emailList.add(rs.getString("email"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emailList;
	}

	public List<String> getAllAdminsEmail() throws Exception {
		String query = "select distinct email from user_view where access_level = '1' union select distinct email from club";
		List<String> emailList = new ArrayList<String>();
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				emailList.add(rs.getString("email"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emailList;
	}

	public List<String> getAllUnSubscribedMarketingEmails() throws Exception {
		String query = "select distinct u.email from mcc.user u, mcc.user_notifications un "
				+ " where u.user_id = un.user_id and ( un.marketing = 0 OR un.marketing is null OR un.marketing = '' ) "
				+ " union select distinct email from mcc.user_view where club_id in (select "
				+ "club_id from mcc.club where optout_marketing = 1)";
		List<String> emailList = new ArrayList<String>();
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				emailList.add(rs.getString("email"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emailList;
	}
	
	protected List<UserDto> getUsersByQuery(List<String> qStrings,
			int clubId) throws Exception {
		List<UserDto> players = new ArrayList<UserDto>();
		String query = "select p.user_id," + " p.l_name," + " p.f_name "
				+ "from user_view p " + "where p.club_id = ?";
		for (String str : qStrings) {
			query += " AND (p.l_name like '%" + str + "%' OR p.f_name like '%"
					+ str + "%')";
		}

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto player = new UserDto();
				player.setUserID(rs.getInt("user_id"));
				player.setFname(rs.getString("f_name"));
				player.setLname(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new UserDtoComparator(true, true, false));
		return players;
	}
	
	protected List<UserDto> getUsersByQuery(String qStrings,
			int clubId) throws Exception {
		List<UserDto> players = new ArrayList<UserDto>();
		String query = "select p.user_id, p.f_name, p.l_name, p.umpire_id, p.player_id from user_view p "
				+ " where p.club_id = ? and p.is_active = 1 and p.player_id not in (select player_id from mcc.player_club where club_id = ? and is_active != 1) "
				+ " and p.umpire_id not in ( select umpire_id from mcc.umpire_club where club_id = ? and is_active != 1 ) "
				+ " and concat(p.f_name, ' ', p.l_name) like '%" + DButility.escape(qStrings.toLowerCase()) +  "%' limit 25 ";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, clubId);
			st.setInt(3, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto player = new UserDto();
				player.setUserID(rs.getInt("user_id"));
				player.setUmpireID(rs.getInt("umpire_id"));
				player.setPlayerID(rs.getInt("player_id"));
				player.setFname(rs.getString("f_name"));
				player.setLname(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new UserDtoComparator(true, true, false));
		return players;
	}
	
	protected List<UserDto> getUsersByUmpire(String qStrings,
			int clubId) throws Exception {
		List<UserDto> players = new ArrayList<UserDto>();
		String query = "select p.user_id, p.f_name, p.l_name, p.umpire_id, p.player_id from user_view p, umpire_view u "
				+ " where p.user_id = u.user_id and p.club_id = ? and u.is_active = 1 and u.type like '%u%' "
						+ " and concat(p.f_name, ' ', p.l_name) like '%" + qStrings +  "%' limit 10 ";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto player = new UserDto();
				player.setUserID(rs.getInt("user_id"));
				player.setUmpireID(rs.getInt("umpire_id"));
				player.setPlayerID(rs.getInt("player_id"));
				player.setFname(rs.getString("f_name"));
				player.setLname(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new UserDtoComparator(true, true, false));
		return players;
	}
	
	protected List<UserDto> getUsersByPlayerQuery(String qStrings,
			int clubId) throws Exception {
		List<UserDto> players = new ArrayList<UserDto>();
		String query = "select u.user_id, u.f_name, u.l_name, u.player_id from user_view u  where u.club_id = ? "
				+ " and u.is_active = 1 and  lower(concat(u.f_name, ' ', u.l_name)) like '%" 
				+ DButility.escape(qStrings.toLowerCase()) +  "%'  order by u.f_name limit 10 ";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto player = new UserDto();
				player.setUserID(rs.getInt("user_id"));				
				player.setPlayerID(rs.getInt("player_id"));
				player.setFname(rs.getString("f_name"));
				player.setLname(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected List<UserDto> getUsersForUmpireByQuery(String qStrings,
			int clubId) throws Exception {
		List<UserDto> users = new ArrayList<UserDto>();
		String query = "select p.user_id, p.f_name, p.l_name, umpire_id, player_id from user_view p where p.club_id = ? and concat(f_name, ' ', l_name) like '%" + qStrings +  "%' and (player_id >0 or umpire_id > 0)";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				user.setUserID(rs.getInt("user_id"));
				user.setFname(rs.getString("f_name"));
				user.setLname(rs.getString("l_name"));
				users.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(users, new UserDtoComparator(true, true, false));
		return users;
	}

	public static void cleanUpEmail(String email) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			String query = "SELECT mcc.deleteEMail(?);";
			conn = DButility.getDefaultConnection();
		
			st = conn.prepareStatement(query);
			st.setString(1, email);
			rs = st.executeQuery();
		//	String value = "";
			while(rs.next()) {
			//	value = rs.getString(1);
			}
		} catch (SQLException e) {
			throw new Exception(email +" : "+ e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
		
	public static void auditUser(String user_id, String club_id, String user, String operation, Connection conn) throws Exception {
		
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		//Connection conn = null;
		ResultSet rs = null;
		String new_id = null; 
		try {
			//conn = DButility.getDefaultConnection();
			String insertQuery = "insert into user_audit(user_id,user_name,password," +
					"email, is_active, player_id, f_name, l_name, gender, date_of_birth, phone, address, city,"
					+ "state, country_code, postal_code, umpire_id, token, last_access_date, "
					+ "no_of_matches_streaming, add_request_from) "
					+" (select user_id,user_name,password,email, is_active, player_id, " +
					" f_name, l_name, gender, date_of_birth, phone, address, city, state, country_code, postal_code, "+					 
					"umpire_id, token, last_access_date, no_of_matches_streaming, add_request_from from user where user_id = ?)";
			st = conn.prepareStatement(insertQuery);
			st.setString(1, user_id);
			st.executeUpdate();
			String GetID = "select LAST_INSERT_ID() as latest_id";
			rs = st.executeQuery(GetID);
			while (rs.next()) {
				new_id = rs.getString("latest_id");
			}
			String updateQuery = "update user_audit set change_timestamp = now(), change_by=?,change_type=? " +
											" where change_id = ?";
			st1 = conn.prepareStatement(updateQuery);
			st1.setString(1, user);
			st1.setString(2, operation);
			st1.setString(3, new_id);
			st1.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeStatement(st1);
			DButility.closeStatement(st);
			//DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	public static void auditUserV1(long userId, String operation, Connection conn, String changeBy) throws Exception {
		
		PreparedStatement st = null;
		try {
			String insertQuery = "insert into user_audit(user_id,user_name,password, email, is_active, player_id, f_name, l_name, "
					+ "gender, date_of_birth, phone, address, city, state, country_code, postal_code, umpire_id, token, "
					+ "last_access_date, no_of_matches_streaming, add_request_from, "
					+ "change_timestamp, change_type, change_by) (select user_id,user_name,password,"
					+ "email, is_active, player_id, f_name, l_name, gender, date_of_birth, phone, address, city, state, country_code, postal_code, "+					 
					"umpire_id, token, last_access_date, no_of_matches_streaming, add_request_from, now(),?,? from user where user_id = ?)";
			
			st = conn.prepareStatement(insertQuery);
			st.setString(1, operation);
			st.setString(2, changeBy);
			st.setLong(3, userId);
			
			st.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeStatement(st);
		}
	}
		
		public static void auditUserClub(String user_id, String club_id, String user, String operation, Connection conn) throws Exception {
			
			PreparedStatement st = null;
			PreparedStatement st1 = null;
			//Connection conn = null;
			ResultSet rs = null;
			String new_id = null; 
			try {
				//conn = DButility.getDefaultConnection();
				String insertQuery = "insert into user_club_audit(user_id,club_id,access_level)"
											+" (select user_id,club_id,access_level from user_club where user_id = ? and club_id =?)";
				st = conn.prepareStatement(insertQuery);
				st.setString(1, user_id);
				st.setString(2, club_id);
				st.executeUpdate();
				String GetID = "select LAST_INSERT_ID() as latest_id";
				rs = st.executeQuery(GetID);
				while (rs.next()) {
					new_id = rs.getString("latest_id");
				}
				
				String updateQuery = "update user_club_audit set change_timestamp = now(), change_by=?,change_type=? " +
												" where change_id = ?";
				st1=conn.prepareStatement(updateQuery);
				st1.setString(1, user);
				st1.setString(2, operation);
				st1.setString(3, new_id);
				st1.executeUpdate();
				
			} catch (Exception e) {
				throw new Exception(e);
			} finally {
				DButility.closeStatement(st1);
				DButility.closeStatement(st);
				//DButility.closeConnectionAndStatement(conn, st);
			}
		}
		public UserLiteDto getAutherizedUsersWithAccess(String username, String password) throws Exception {

			UserLiteDto user = null;
			String query = " select  u.user_id, u.user_name,  u.player_id, u.f_name, u.l_name, u.is_active, "
					+ "u.email from mcc.user u where user_name = ? and password = ? ";
						
			Connection conn = null;
			PreparedStatement st = null;
			PreparedStatement st1 = null;			
			ResultSet rs = null;
			ResultSet rs1 = null;
			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				st.setString(1, username);
				st.setString(2, password);
				rs = st.executeQuery();
				while (rs.next()) {
					user = new UserLiteDto();
					user.setUserId(rs.getInt("user_id"));
					user.setUserName(rs.getString("user_name"));
					user.setPlayerId(rs.getInt("player_id"));
					user.setfName(rs.getString("f_name"));
					user.setlName(rs.getString("l_name"));
					user.setEmail(rs.getString("email"));
					user.setActive(rs.getBoolean("is_active"));				
				}			
								
				if(user != null && user.getUserId() > 0) {
						query =   " select club_id, access_level from user_club where user_id = ? "
								+ " union all "
								+ " select club_id, role as access_level from user_roles where user_id = ? ";						
					
					st1 = conn.prepareStatement(query);
					st1.setInt(1, user.getUserId());
					st1.setInt(2, user.getUserId());										
					rs1 = st1.executeQuery();
					
					Map<Integer, List<Integer>> rolesMapping = new HashMap<Integer, List<Integer>>();
					
					while (rs1.next()) {
						int clubId = rs1.getInt("club_id");
						List<Integer> listOfRoles =rolesMapping.get(clubId)  == null ? new ArrayList<Integer>() : rolesMapping.get(clubId);
						listOfRoles.add(rs1.getInt("access_level"));
						rolesMapping.put(clubId, listOfRoles);
					}
					user.setRolesMapping(rolesMapping);
				}				
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeRs(rs1);	
				DButility.closeStatement(st1);
				DButility.dbCloseAll(conn, st, rs);
			}			
			return user;
		}
		public UserLiteDto getAutherizedUsersWithAccessByToken(String userName, String tokenToAutoLogin) throws Exception {

			UserLiteDto user = null;
			String query = " select  u.user_id, u.profile_image_path,  u.user_name, u.player_id, u.f_name, u.l_name, u.is_active, "
					+ " u.email from mcc.user u where user_name = ? and token = ? ";
					
			Connection conn = null;
			PreparedStatement st = null;
			PreparedStatement st1 = null;			
			ResultSet rs = null;
			ResultSet rs1 = null;			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				st.setString(1, userName);
				st.setString(2, tokenToAutoLogin);
				rs = st.executeQuery();
				while (rs.next()) {
					user = new UserLiteDto();
					user.setUserId(rs.getInt("user_id"));
					user.setUserName(rs.getString("user_name"));
					user.setPlayerId(rs.getInt("player_id"));
					user.setfName(rs.getString("f_name"));
					user.setlName(rs.getString("l_name"));
					user.setEmail(rs.getString("email"));
					user.setActive(rs.getBoolean("is_active"));
					user.setProfileImangePath(rs.getString("profile_image_path"));
				}				
				
			if (user != null && user.getUserId() > 0) {

				query = " select club_id, access_level from user_club where user_id = ? " + " union all "
						+ " select club_id, role as access_level from user_roles where user_id = ? ";

				st1 = conn.prepareStatement(query);
				st1.setInt(1, user.getUserId());
				st1.setInt(2, user.getUserId());
				rs1 = st1.executeQuery();

				Map<Integer, List<Integer>> rolesMapping = new HashMap<Integer, List<Integer>>();

				while (rs1.next()) {
					int clubId = rs1.getInt("club_id");
					List<Integer> listOfRoles = rolesMapping.get(clubId) == null ? new ArrayList<Integer>()
							: rolesMapping.get(clubId);
					listOfRoles.add(rs1.getInt("access_level"));
					rolesMapping.put(clubId, listOfRoles);
				}
				user.setRolesMapping(rolesMapping);
			}
				
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeRs(rs1);
				DButility.closeStatement(st1);
				DButility.dbCloseAll(conn, st, rs);
			}			
			return user;
		}
		
		public UserLiteDto getUserByAuthTokenOrUserIdAndPassword(String tokenToAutoLogin, String userName, String password) throws Exception {

			UserLiteDto user = null;
			String query = "select  u.user_id, u.profile_image_path,  u.user_name, u.player_id, u.umpire_id, "
					+ "u.f_name, u.l_name, u.is_active, u.email, uc.club_id, uc.user_type, uc.user_type_id "
					+ "from mcc.user u , mcc.user_club uc  where uc.user_id = u.user_id   ";
			
			if(!StringUtils.isEmpty(tokenToAutoLogin)) {
				query += " and u.token = ?";
			}else if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
				
				if(CommonUtility.isOnlyDigitInString(userName)) {
					query += " and u.phone = ? " ;
				}else if(userName.contains("@")) {
					query += " and u.email = ? " ;
				}else {
					query += " and u.user_name = ? " ;
				}
				query += " and u.password = ? " ;
				
			}else {
				return null;
			}
			
			Connection conn = null;
			PreparedStatement st = null;
			PreparedStatement st1 = null;			
			ResultSet rs = null;
			ResultSet rs1 = null;			
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				if(!StringUtils.isEmpty(tokenToAutoLogin)) {
					st.setString(1, tokenToAutoLogin);
				}else if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
					st.setString(1, userName);
					st.setString(2, password);
				}
				
				rs = st.executeQuery();
				Map<String, String> userType = new HashMap<String, String>();
				while (rs.next()) {
					if(user == null) {
						user = new UserLiteDto();
						user.setUserTypeMap(userType);
						user.setUserId(rs.getInt("user_id"));
						user.setUserName(rs.getString("user_name"));
						user.setPlayerId(rs.getInt("player_id"));
						user.setfName(rs.getString("f_name"));
						user.setlName(rs.getString("l_name"));
						user.setEmail(rs.getString("email"));
						user.setActive(rs.getBoolean("is_active"));
						user.setProfileImangePath(rs.getString("profile_image_path"));
					}
					userType.put(rs.getString("user_type"), rs.getString("user_type_id"));
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeRs(rs1);
				DButility.closeStatement(st1);
				DButility.dbCloseAll(conn, st, rs);
			}			
			return user;
		}
		
		public Map<String, String> getUserTypeMap(int userId) throws Exception {

			String query = "select user_type, user_type_id from mcc.user_club where user_id = ?  ";
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			Map<String, String> userType = null;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				if(userId>0) {
					st.setInt(1, userId);
				}				
				rs = st.executeQuery();
				userType = new HashMap<String, String>();
				while (rs.next()) {					
					userType.put(rs.getString("user_type"), rs.getString("user_type_id"));
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}			
			return userType;
		}
		
		public int getUserAcademyClubId(int userId) throws Exception {

			String query = "select uc.club_id from mcc.user_club uc, mcc.club c "
					+ " where c.club_id = uc.club_id and c.is_academy = 1 and uc.user_id = ? limit 1 ";
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			int academyClubId = 0;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				if(userId>0) {
					st.setInt(1, userId);
				}				
				rs = st.executeQuery();
				while (rs.next()) {					
					academyClubId = rs.getInt(1);
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}			
			return academyClubId;
		}
		
		public UserLiteDto getUserByAuthTokenOrUserNameAndPassword(String tokenToAutoLogin, String userName, String password) throws Exception {

			UserLiteDto user = null;
			String query = "select  u.user_id, u.profile_image_path,  u.user_name, u.player_id, u.umpire_id, "
					+ "u.f_name, u.l_name, u.is_active, u.email, u.no_of_matches_streaming, u.country_code, "
					+ " u.phone from mcc.user u where is_active=1  ";
			
			if(!StringUtils.isEmpty(tokenToAutoLogin)) {
				query += " and u.token = ?";
			}else if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
				
				if(userName.chars().allMatch( Character::isDigit )) {
					query += " and u.phone = ? " ;
				}else if(userName.contains("@")) {
					query += " and u.email = ? " ;
				}else {
					query += " and u.user_name = ? " ;
				}
				query += " and u.password = ? " ;
				
			}else {
				return null;
			}
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				if(!StringUtils.isEmpty(tokenToAutoLogin)) {
					st.setString(1, tokenToAutoLogin);
				}else if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
					st.setString(1, userName);
					st.setString(2, password);
				}
				
				rs = st.executeQuery();
				while (rs.next()) {
					if(user == null) {
						
						user = new UserLiteDto();
						
						user.setUserId(rs.getInt("user_id"));
						user.setUserName(rs.getString("user_name"));
						user.setPlayerId(rs.getInt("player_id"));
						user.setfName(rs.getString("f_name"));
						user.setlName(rs.getString("l_name"));
						user.setEmail(rs.getString("email"));
						user.setActive(rs.getBoolean("is_active"));
						user.setProfileImangePath(rs.getString("profile_image_path"));
						user.setNoOfMatchesStreaming(rs.getInt("no_of_matches_streaming"));
						user.setCountryCode(rs.getString("country_code"));
						user.setPhone(rs.getString("phone"));
					}
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}			
			return user;
		}
		
		public UserLiteDto getLiteUserByUserNameAndPassword(String userName, String password) throws Exception {

			UserLiteDto user = null;
			String query = "select  u.user_id, u.profile_image_path,  u.user_name, u.player_id, u.umpire_id, "
					+ "u.f_name, u.l_name, u.is_active, u.email, u.no_of_matches_streaming, u.country_code, "
					+ " u.phone from mcc.user u where is_active = 1 and u.user_name = ? and u.password = ? ";
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				st.setString(1, userName);
				st.setString(2, password);
				
				rs = st.executeQuery();
				
				while (rs.next()) {
					if(user == null) {
						
						user = new UserLiteDto();
						
						user.setUserId(rs.getInt("user_id"));
						user.setUserName(rs.getString("user_name"));
						user.setPlayerId(rs.getInt("player_id"));
						user.setfName(rs.getString("f_name"));
						user.setlName(rs.getString("l_name"));
						user.setEmail(rs.getString("email"));
						user.setActive(rs.getBoolean("is_active"));
						user.setProfileImangePath(rs.getString("profile_image_path"));
						user.setNoOfMatchesStreaming(rs.getInt("no_of_matches_streaming"));
						user.setCountryCode(rs.getString("country_code"));
						user.setPhone(rs.getString("phone"));
					}
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}			
			return user;
		}
		
		public int checkIfUserExistByUNameOrEmail(UserDto userDto)  throws Exception {
			String query =" select count(*) count from user where (email = ? or user_name = ?) and user_id != ? ";
			
			Connection conn = DButility.getDefaultReadConnection();
			PreparedStatement st = null;
			ResultSet rs = null;
			int count = 0;
			try {
				st = conn.prepareStatement(query);
				int index =1;
				st.setString(index++, userDto.getEmail());
				st.setString(index++, userDto.getUserName());
				st.setInt(index++, userDto.getUserID());
				
				rs = st.executeQuery();
				while (rs.next()) {
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {				
				DButility.dbCloseAll(conn, st, rs);
			}
			return count;

		}
		
		public void testAddUser(int clubId, int loopCount) throws Exception {
			
			UserDto user = new UserDto();
			
			for(int i = 0; i < loopCount; i++) {
				 user = new UserDto("UserDBTest"+loopCount + ""+i, CommonUtility.encrypt(CommonUtility.getPassword(8)), "testdbuser@emai.com"+i, 
						 2, 123, 321, "FName"+i, "LName"+i, "9898"+i, "Address"+i, "City"+i,"State"+i, "PostalCode"+i, "profilePath/Testimage/image2"+i+".jps");
				
				 String query = "insert into user(user_name,password,email,is_active,player_id,umpire_id,f_name, "
						+ "l_name,phone,address,city,state,postal_code,profile_image_path ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				long userId = 0;
				Connection conn = DButility.getDefaultConnection();
				Connection conn1 = DButility.getDefaultReadConnection();
				PreparedStatement st = null;
				PreparedStatement str = null;
				PreparedStatement st1 = null;
				ResultSet rs = null;
				
				try {
					long startTime = System.currentTimeMillis();
					int index = 1;
					st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
					str = conn1.prepareStatement("select max(user_id) user_id from user");
					
					st.setString(index++, DButility.escapeQuotes(user.getUserName()));
					st.setString(index++, user.getPassword());
					st.setString(index++, CommonUtility.trimString(user.getEmail()));
					st.setString(index++, "1");
					st.setInt(index++, user.getPlayerID());			
					st.setInt(index++, user.getUmpireID());
					st.setString(index++, DButility.escapeQuotes(user.getFname()));
					st.setString(index++, DButility.escapeQuotes(user.getLname()));
					st.setString(index++, CommonUtility.trimString(user.getPhone()));
					st.setString(index++, user.getAddress());
					st.setString(index++, CommonUtility.trimString(user.getCity()));
					st.setString(index++, CommonUtility.trimString(user.getState()));
					st.setString(index++, CommonUtility.trimString(user.getPostalCode()));	
					st.setString(index++, user.getProfileImagePath());	
					st.executeUpdate();
					long userInsertEnd = System.currentTimeMillis();
					rs = str.executeQuery();
					if (rs.next()) {
						userId = rs.getInt("user_id");
					}
					long readuser = System.currentTimeMillis();
					UserNotificationsDto undto = new UserNotificationsDto();			
					undto.setUserId(userId);
					//undto.setDefaultNotifictions();			
					UserNotificationsFactory.insertUserNotification(undto);
					
					// Audit Log Methods Called and parameters sent
					String userid = Long.toString(userId);
					String clubid = Integer.toString(clubId);

					auditUser(userid, clubid, userId + "T", "insert", conn);

				} catch (SQLException e) {
					throw new Exception(e.getMessage());
				} finally {
					DButility.closeStatement(st1);
					DButility.closeConnectionAndStatement(conn, st);
					DButility.closeConnectionAndStatement(conn1, str);
				}				
			}	
			
		}
		
		protected List<UserDto> getUserSuggestionForTeamOfficial(String qStrings,
				int clubId) throws Exception {
			List<UserDto> users = new ArrayList<UserDto>();
			String query = "select user_id, f_name, l_name, player_id, umpire_id from mcc.user_view "
					+ "where club_id = "+clubId+" and (player_id > 0 or umpire_id > 0) "
					+ "and (f_name like '%"+qStrings+"%' or l_name like '%"+qStrings+"%' "
					+ "OR CONCAT(f_name,' ',l_name) LIKE '%"+qStrings+"%' OR CONCAT(l_name,' ',f_name) LIKE '%"+qStrings+"%')";
			

			query += " order by f_name,l_name limit 10";

			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				rs = st.executeQuery();
				while (rs.next()) {
					UserDto user = new UserDto();
					user.setUserID(rs.getInt("user_id"));
					user.setFname(rs.getString("f_name"));
					user.setLname(rs.getString("l_name"));
					user.setPlayerID(rs.getInt("player_id"));
					user.setUmpireID(rs.getInt("umpire_id"));
					users.add(user);
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return users;
		}

		public List<String> getAllAdminsByClubId(int clubId) throws Exception {
			String query = "SELECT * FROM mcc.user_view WHERE access_level = '1' AND club_id = "+clubId+" AND email IS NOT NULL AND email <> ''";
			List<String> emailList = new ArrayList<String>();
			Connection conn = DButility.getDefaultReadConnection();
			PreparedStatement st = null;
			ResultSet rs = null;

			try {
				st = conn.prepareStatement(query);
				rs = st.executeQuery();
				while (rs.next()) {
					emailList.add(rs.getString("email"));
				}

			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return emailList;
		}
		
		protected int getAdminUserIdByClubId(int clubId) throws Exception {
			
			String query = "SELECT user_id FROM mcc.user_view WHERE access_level = '1' AND club_id = "+clubId+" limit 1";
			Connection conn = DButility.getDefaultReadConnection();
			PreparedStatement st = null;
			ResultSet rs = null;
			int userId = 0;
			try {
				st = conn.prepareStatement(query);
				rs = st.executeQuery();
				while (rs.next()) {
					userId = rs.getInt(1);
				}

			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return userId;
		}
		
protected String getAdminUserNameByClubId(int clubId) throws Exception {
			
			String query = "SELECT f_name,l_name FROM mcc.user_view WHERE access_level = '1' AND club_id = "+clubId+" limit 1";
			Connection conn = DButility.getDefaultReadConnection();
			PreparedStatement st = null;
			ResultSet rs = null;
			String  userName = "";
			try {
				st = conn.prepareStatement(query);
				rs = st.executeQuery();
				while (rs.next()) {
					userName = rs.getString(1)+" "+rs.getString(2);
				}

			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return userName;
		}
		
		protected boolean deleteInActiveUser(int userId) throws Exception {
			
			Connection conn = null;
			Statement st = null;		
			try {						
				conn = DButility.getDefaultConnection();
						
				conn.setAutoCommit(false);
				
				st = conn.createStatement();			
				
				st.addBatch("delete from user_reg_verification where user_id = "+userId);				
				st.addBatch("delete from user where user_id = "+userId+" and is_active = 0");
				
				st.executeBatch();			
				conn.commit();	
				
			} catch (BatchUpdateException be) {
				conn.rollback();
				throw new Exception(be);
			} catch (Exception e) {
				conn.rollback();
				throw new Exception(e);
			} finally {
				DButility.closeConnectionAndStatement(conn, st);
			}
			return true;
		}

		public List<UserDto> findUserByNameAndEmail(String fName, String lName, String email, int clubId) throws Exception {
			String query =
					SELECT_QUERY +
							" where email= ? and f_name = ? and l_name = ? ";
			if(clubId != 0){
				query += "and u.club_id = ?";
			}
			Connection conn = DButility.getReadConnection(clubId);
			PreparedStatement st = null;
			ResultSet rs = null;
			List<UserDto> users = new ArrayList<UserDto>();
			try {
				st = conn.prepareStatement(query);
				int index =1;
				st.setString(index++, email);
				st.setString(index++, fName);
				st.setString(index++, lName);
				if(clubId != 0){
					st.setInt(index++, clubId);
				}
				rs = st.executeQuery();
				while (rs.next()) {
					UserDto user = new UserDto();
					populateUserDto(user, rs);
					pupulateUserRoles(user, conn, user.getClubId());
					user.setClubId(user.getClubId());
					checkIfUserIsCaptain(conn, user);
					users.add(user);
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return users;
		}
		
		public int isGlobalUser(int userId) throws Exception {

			int isGlobalUser = 0 ;
			
			String query = "select 1 from mcc.user_view u where club_id = 0 and user_id = ? and access_level = 99 ";
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				if(userId>0) {
					st.setInt(1, userId);
				}
				rs = st.executeQuery();
				
				while (rs.next()) {
					isGlobalUser = rs.getInt(1);
				}
			
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}			
			return isGlobalUser;
		}
		
		public List<Integer> getUserAllClubIds(int userId) throws Exception {
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			List<Integer> clubIds = new ArrayList<Integer>();
			String query = "select club_id from user_club where user_id= ? order by club_id";
			try {
				conn = DButility.getDefaultReadConnection();
				st = conn.prepareStatement(query);
				st.setInt(1, userId);
				rs = st.executeQuery();
				while (rs.next()) {
					clubIds.add(rs.getInt("club_id"));
				}
			} catch (Exception e) {
				throw new Exception(e);
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return clubIds;
		}
		
	public List<Integer> getUserAllClubIdsWithAdminAccess(int userId) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> clubIds = new ArrayList<Integer>();
		String query = "select club_id from user_view where user_id = ? and access_level = 1"
				+ " union "
				+ "select club_id from user_roles where user_id = ? and role = 1;";
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, userId);
			rs = st.executeQuery();
			while (rs.next()) {
				clubIds.add(rs.getInt("club_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return clubIds;
	}
		
		public Map<String,String> getUmpireAdministratorEmailsMap(int clubId) throws Exception{
			
			String query= "SELECT  email,f_name,l_name FROM user WHERE user_id IN(SELECT user_id FROM user_roles WHERE role=7 AND club_id=?) GROUP BY (email)";
			
			Map<String,String> umpireAdministratorEmailMap= new HashMap<>();
			
			Connection conn=null;
			PreparedStatement st=null;
			ResultSet rs=null;
			
			try {
				conn=DButility.getDefaultReadConnection();
				st=conn.prepareStatement(query);
				st.setInt(1, clubId);
				rs=st.executeQuery();
				while(rs.next()) {
					umpireAdministratorEmailMap.put(rs.getString("email"), rs.getString("f_name")+" "+rs.getString("l_name"));
				}
				
			}catch (Exception e) {
				throw new Exception(e);
			}finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			 
			return umpireAdministratorEmailMap;
			
		}

		public Pair getUserEmailPasswordByTeamAdmin(int teamId) throws Exception {
			String query = "SELECT u.user_name,u.password FROM mcc.user u, club14202.team_details td "
					+ "WHERE u.email = td.admin_email AND td.team_id =? order by 1 desc limit 1";
			
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			Pair userPair = new Pair();
			try {
				conn = DButility.getDefaultConnection();
				st = conn.prepareStatement(query);
				
				st.setInt(1, teamId);
				
				rs = st.executeQuery();
				while (rs.next()) {
					userPair.setValues1(rs.getString(1));
					userPair.setValues2(rs.getString(2));
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, st, rs);
			}
			return userPair;

		}

		public void deleteUserByAuthToken(int userId) throws Exception {
			
			String query = "update user set is_active=0 where user_id= " + userId;
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = DButility.getDefaultConnection();
				stmt = conn.prepareStatement(query);
				stmt.execute();
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeConnectionAndStatement(conn, stmt);
			}
			if(userId>0) {
				GraphDBBackGroundProcess.updateGraphUserIsActiveStatus(0,userId);	
			}
		}
		
		public boolean getUserPartOfAnyClub(int userId) throws Exception {
			
			String query = "select count(*) from mcc.user_club where user_id= " + userId;
			
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			boolean isValuExist = false;
			try {
				conn = DButility.getDefaultConnection();
				stmt = conn.prepareStatement(query);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					isValuExist = rs.getInt(1)>0?true:false;
				}				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, stmt, rs);
			}
			return isValuExist;
		}

}
