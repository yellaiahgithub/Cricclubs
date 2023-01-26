package com.cricket.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cricket.dto.Pair;
import com.cricket.dto.UserDto;
import com.cricket.dto.lite.UserLiteDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.batch.GraphDBBackGroundProcess;

public class UserFactory {

	private static final int SOCIAL_FACEBOOK_ID = 1;
	private static UserDAO userDao = null;

	private static UserDAO getDaoInstance() {
		if (userDao == null) {
			userDao = new UserDAO();
		}
		return userDao;
	}

	public static UserDto getUser(String userName, int clubId) throws Exception {
		if (!CommonUtility.isNullOrEmptyOrNULL(userName)) {
			return getDaoInstance().getUser(userName, clubId);
		}
		return null;
	}

	public static UserDto getUserByUserName(String userName, int clubId) throws Exception {
		if (!CommonUtility.isNullOrEmptyOrNULL(userName)) {
			return getDaoInstance().getUserByUserName(userName, clubId);
		}
		return null;
	}

	public static UserDto getAllUser(String userName) throws Exception {
		if (!CommonUtility.isNullOrEmptyOrNULL(userName)) {
			return getDaoInstance().getAllUser(userName);
		}
		return null;
	}

	public static List<UserDto> getUsersByUsernamePassword(String username, String password) throws Exception {
		return getDaoInstance().getGlobalUsers(username, password);
	}

	public static UserDto getUserById(int userId, int clubId) throws Exception {
		return getDaoInstance().getUserById(userId, clubId);
	}

	public static UserDto getUserByIdV1(int userId) throws Exception {
		return getDaoInstance().getUserByIdV1(userId);
	}

	public static String getUserFullNameById(int userId) throws Exception {
		return getDaoInstance().getUserFullName(userId);
	}

	public static int getUserIdByEmail(String email) throws Exception {
		if (CommonUtility.isNullOrEmpty(email)) {
			return 0;
		}
		int userId = getDaoInstance().getUserIdByEmail(email);
		if (userId > 0) {
			return userId;
		}
		return 0;
	}

	public static UserDto getUserByEmail(String email, int clubId) throws Exception {
		if (CommonUtility.isNullOrEmpty(email)) {
			return null;
		}
		List<UserDto> users = getDaoInstance().getUserByEmail(email, clubId);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	public static Pair getUserEmailPasswordByTeamAdmin(int teamId) throws Exception {
		return getDaoInstance().getUserEmailPasswordByTeamAdmin(teamId);
	}

	public static UserDto getUserEmail(String email) throws Exception {
		if (CommonUtility.isNullOrEmpty(email)) {
			return null;
		}
		List<UserDto> users = getDaoInstance().getUserEmail(email);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	public static boolean verifyUserByEmail(String email) throws Exception {
		return getDaoInstance().verifyUserByEmail(email);
	}

	public static boolean verifyUserByPhone(String phone) throws Exception {
		return getDaoInstance().verifyUserByPhone(phone);
	}

	public static UserDto getUserByName(String userName) throws Exception {

		if (CommonUtility.isNullOrEmpty(userName)) {
			return null;
		}
		List<UserDto> users = getDaoInstance().getUserName(userName);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;

	}

	public static List<UserDto> getUsersByEmail(String email) throws Exception {
		if (CommonUtility.isNullOrEmpty(email)) {
			return null;
		}
		return getDaoInstance().getUserByEmail(email, 0);

	}

	public static long registerUser(UserDto user, int clubId, String loggedinuser) throws Exception {
		return getDaoInstance().registerUser(user, clubId, loggedinuser);
	}

	public static void addExistingUserToClub(int userId, int clubId, String accessLevel, String loggedinuser)
			throws Exception {
		getDaoInstance().addExistingUserToClub(userId, clubId, accessLevel, loggedinuser);
	}

	public static long registerUserV1(UserDto user) throws Exception {
		return getDaoInstance().registerUserV1(user);
	}

	public static List<UserLiteDto> getUserBasicDetailsWithEmail(int userId) throws Exception {
		return getDaoInstance().getUserBasicDetailsWithEmail(userId);
	}

	public static List<UserLiteDto> getAcademyUsersBasicDetails(int userId) throws Exception {
		return getDaoInstance().getAcademyUsersBasicDetails(userId);
	}

	public static List<UserLiteDto> getUserBasicDetailsWithNoEmail(int userId) throws Exception {
		return getDaoInstance().getUserBasicDetailsWithNoEmail(userId);
	}

	public static UserLiteDto getUserBasicDetailsByUserId(int userId) throws Exception {
		return getDaoInstance().getUserBasicDetailsByUserId(userId);
	}

	public static UserLiteDto getUserBasicDetailsByPhone(String phoneNumber) throws Exception {
		return getDaoInstance().getUserBasicDetailsByPhone(phoneNumber);
	}
	
	public static List<UserLiteDto> getUserBasicDetailsByPhoneList(String phoneNumber) throws Exception {
		return getDaoInstance().getUserBasicDetailsByPhoneList(phoneNumber);
	}

	public static UserLiteDto getUserBasicDetailsByEmail(String email) throws Exception {
		return getDaoInstance().getUserBasicDetailsByEmail(email);
	}
	public static List<UserLiteDto> getUserBasicDetailsByEmailPhoneList(String email,String phone, String checkBy) throws Exception {
		return getDaoInstance().getUserBasicDetailsByEmailPhoneList(email,phone,checkBy);
	}

	public static void updateUser(UserDto user, int clubId, String loggedinuser) throws Exception {
		getDaoInstance().updateUser(user, clubId, loggedinuser);
	}

	public static void updateUserV1(UserDto user, String changeBy) throws Exception {
		getDaoInstance().updateUserV1(user, changeBy);
	}

	public static List<UserDto> getUsersByNotification(String notificationType, int clubId) throws Exception {
		return getDaoInstance().getUsersByNotification(notificationType, clubId);
	}

	public static List<UserDto> getUsersWhoPlayedMatch(long matchId, int clubId) throws Exception {
		return getDaoInstance().getUsersWhoPlayedMatch(matchId, clubId);
	}

	public static List<String> getAllPlayersEmail() throws Exception {
		return getDaoInstance().getAllPlayersEmail();
	}

	public static List<String> getAllUnSubscribedMarketingEmails() throws Exception {
		return getDaoInstance().getAllUnSubscribedMarketingEmails();
	}

	public static List<String> getAllAdminsEmail() throws Exception {
		return getDaoInstance().getAllAdminsEmail();
	}

	public static UserDto getUserByPlayerId(int playerID) throws Exception {
		return getDaoInstance().getUser(playerID, 0);
	}

	public static Map<Integer, Integer> getPlayerUserIdMapByPlayerIds(String playerIds) throws Exception {
		return getDaoInstance().getPlayerUserIdMapByPlayerIds(playerIds);
	}

	public static UserDto getUserByPlayerId(int playerID, int clubId) throws Exception {
		return getDaoInstance().getUser(playerID, clubId);
	}

	public static UserDto getUserByUmpireId(int umpireId) throws Exception {
		return getDaoInstance().getUmpireUser(umpireId, 0);
	}

	public static UserDto getUserByUmpireId(int umpireId, int clubId) throws Exception {
		return getDaoInstance().getUmpireUser(umpireId, clubId);
	}

	public static void registerExistingUser(UserDto user, int clubId, String loggedinuser) throws Exception {
		getDaoInstance().registerExistingUser(user, clubId, loggedinuser);
	}

	public static UserDto getUserByFacebookId(String id, int clubId) throws Exception {
		return getDaoInstance().getUserBySocialId(id, SOCIAL_FACEBOOK_ID, clubId);
	}

	public static void addFacebookUser(int userId, String id) throws Exception {
		getDaoInstance().addSocialUser(userId, id, SOCIAL_FACEBOOK_ID);
	}

	public static List<UserDto> getUsersByQuery(List<String> qStr, int clubId) throws Exception {
		return getDaoInstance().getUsersByQuery(qStr, clubId);
	}

	public static List<UserDto> getUsersByQuery(String qStr, int clubId) throws Exception {
		return getDaoInstance().getUsersByQuery(qStr, clubId);
	}

	public static List<UserDto> getUsersByUmpire(String qStr, int clubId) throws Exception {
		return getDaoInstance().getUsersByUmpire(qStr, clubId);
	}

	public static List<UserDto> getUsersByPlayerQuery(String qStr, int clubId) throws Exception {
		return getDaoInstance().getUsersByPlayerQuery(qStr, clubId);
	}

	public static void cleanUpEmail(String email) throws Exception {
		getDaoInstance().cleanUpEmail(email);
	}

	public static void updateUserUmpireId(int umpireID, int userID, String loggedinuser) throws Exception {
		getDaoInstance().updateUserUmpireId(umpireID, userID, loggedinuser);
	}

	public static void updateUserPlayerId(int playerID, int userID, String loggedinuser) throws Exception {
		getDaoInstance().updateUserPlayerId(playerID, userID, loggedinuser);
	}
	
	public static void updateUserStatus( int userID, int status) throws Exception {
		getDaoInstance().updateUserStatus(userID, status);
	}

	public static void updateUserAuthToken(int userID, String authToken) throws Exception {
		getDaoInstance().updateUserAuthToken(userID, authToken);
	}

	public static void updateUsersAuthToken(String userIds, String authToken) throws Exception {
		getDaoInstance().updateUsersAuthToken(userIds, authToken);
	}
	
	public static void updateUserCountry(int userID, String countryCode) throws Exception {
		getDaoInstance().updateUserCountry(userID, countryCode);
	}

	public static void updateUserPlayerImagePath(int playerID, int userID, String loggedinuser) throws Exception {
		getDaoInstance().updateUserPlayerImagePath(playerID, userID, loggedinuser);
	}

	public static UserDto getUserByToken(String token, int clubId) throws Exception {
		return getDaoInstance().getUserByToken(token, clubId);
	}

	public static UserDto getUserByToken(String token) throws Exception {
		return getDaoInstance().getUserByToken(token);
	}
	
	public static List<Integer> getUserIdsByTokens(List<String> authTokenList) throws Exception {
		return getDaoInstance().getUserIdsByTokens(authTokenList);
	}

	public static void updateUserToken(String token, int userID) throws Exception {
		getDaoInstance().updateUserToken(token, userID);
	}

	public static boolean getUserPartOfAnyClub(int userID) throws Exception {
		return getDaoInstance().getUserPartOfAnyClub(userID);
	}

	public static void deleteUserByAuthToken(int userID) throws Exception {
		getDaoInstance().deleteUserByAuthToken(userID);
	}

	public static void updateUserProfilePicPath(String profileImagePath, int userID) throws Exception {
		getDaoInstance().updateUserProfilePicPath(profileImagePath, userID);
		GraphDBBackGroundProcess.updateGraphUserProfilePicPath(profileImagePath, userID);
	}

	public static void updateUserAuthToken(String column, String value, UserDto user) throws Exception {
		getDaoInstance().updateUserColumnByUserId(column, value, user);
	}

	public static void updateUserEmail(String column, String value, UserDto user) throws Exception {
		getDaoInstance().updateUserColumnByUserId(column, value, user);
	}

	public static void updateUserMobile(String column, String value, UserDto user) throws Exception {
		getDaoInstance().updateUserColumnByUserId(column, value, user);
	}

	public static void updateUserBackGroundImagePath(String backGroundImagePath, int userID) throws Exception {
		getDaoInstance().updateUserBackGroundImagePath(backGroundImagePath, userID);
		GraphDBBackGroundProcess.updateUserBackGroundImagePath(backGroundImagePath, userID);
	}

	public static void updateUserPassword(String password, long userId) throws Exception {
		getDaoInstance().updateUserPassword(password, userId);
	}

	public static void updateUsersPasswords(String password, String userIds) throws Exception {
		getDaoInstance().updateUsersPasswords(password, userIds);
	}
	
	public static void updateUserPasswordByEmail(String email, String password) throws Exception {
		getDaoInstance().updateUserPasswordByEmail(email, password);
	}
	
	public static void updateUserNoOfStreamingMatches(int noOfMatches, long userId) throws Exception {
		getDaoInstance().updateUserNoOfStreamingMatches(noOfMatches, userId);
	}

	public static void deleteUserToken(String token) throws Exception {
		getDaoInstance().deleteUserToken(token);
	}

	public static boolean updateUserSubscriptions(int userId, String notificationType) throws Exception {
		return getDaoInstance().updateUserSubscriptions(userId, notificationType);
	}

	public static List<Integer> getCaptainteams(int playerId, int clubId) throws Exception {
		return getDaoInstance().captainTeams(playerId, clubId);
	}

	public static Map<String, Integer> getAllUserNames(int clubId) throws Exception {
		return getDaoInstance().getAllUserNames(clubId);
	}

	public static Map<Integer, String> getUserIdNameMap(int clubId) throws Exception {
		return getDaoInstance().getUserIdNameMap(clubId);
	}

	public static Map<Integer, String> getUserIdNameMapForUserIds(List<Long> userIds) throws Exception {
		return getDaoInstance().getUserIdNameMapForUserIds(userIds);
	}

	public static void registerExistingUserWithAccessLevel(UserDto userDto, int clubId, String loggedinuser,
			boolean isPracticeLeagueAdmin) throws Exception {
		getDaoInstance().registerExistingUserWithAccessLevel(userDto, clubId, loggedinuser, isPracticeLeagueAdmin);
	}

	public static UserLiteDto getAutherizedUsersWithAccess(String username, String password) throws Exception {
		return getDaoInstance().getAutherizedUsersWithAccess(username, password);
	}

	public static UserLiteDto getAutherizedUsersWithAccessByToken(String userName, String tokenToAutoLogin)
			throws Exception {
		return getDaoInstance().getAutherizedUsersWithAccessByToken(userName, tokenToAutoLogin);
	}

	public static UserLiteDto getUserByAuthTokenOrUserIdAndPassword(String tokenToAutoLogin, String userName,
			String password) throws Exception {
		return getDaoInstance().getUserByAuthTokenOrUserIdAndPassword(tokenToAutoLogin, userName, password);
	}

	public static UserLiteDto getLiteUserByUserNameAndPassword(String userName, String password) throws Exception {
		if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
			return getDaoInstance().getLiteUserByUserNameAndPassword(userName, CommonUtility.encrypt(password));
		} else {
			return null;
		}
	}

	public static UserLiteDto getUserByUserIdAndPassword(String userName, String password) throws Exception {
		return getDaoInstance().getUserByAuthTokenOrUserIdAndPassword(null, userName, CommonUtility.encrypt(password));
	}

	public static UserLiteDto getUserByUserNameAndPassword(String userName, String password) throws Exception {
		return getDaoInstance().getUserByAuthTokenOrUserNameAndPassword(null, userName,
				CommonUtility.encrypt(password));
	}

	public static UserLiteDto getUserByEmailOrPhoneAndPassword(String userName, String password) throws Exception {
		return getDaoInstance().getUserByAuthTokenOrUserNameAndPassword(null, userName,
				CommonUtility.encrypt(password));
	}

	public static int isGlobalUser(int userId) throws Exception {
		if (userId <= 0) {
			return 0;
		}
		return getDaoInstance().isGlobalUser(userId);
	}

	public static Map<String, String> getUserTypeMap(int userId) throws Exception {
		return getDaoInstance().getUserTypeMap(userId);
	}

	public static int getUserAcademyClubId(int userId) throws Exception {
		return getDaoInstance().getUserAcademyClubId(userId);
	}

	public static UserLiteDto getUserByAuthToken(String tokenToAutoLogin) throws Exception {
		return getDaoInstance().getUserByAuthTokenOrUserNameAndPassword(tokenToAutoLogin, null, null);
	}

	public static int checkIfUserExistByUNameOrEmail(UserDto userDto) throws Exception {
		return getDaoInstance().checkIfUserExistByUNameOrEmail(userDto);
	}

	public static void testAddUser(int clubIdParam, int loopCount) throws Exception {
		getDaoInstance().testAddUser(clubIdParam, loopCount);
	}

	public static List<UserDto> getUserSuggestionForTeamOfficial(int clubIdParam, String qStrFull) throws Exception {
		if (clubIdParam <= 0 || CommonUtility.isNullOrEmpty(qStrFull)) {
			return Collections.emptyList();
		}
		return getDaoInstance().getUserSuggestionForTeamOfficial(qStrFull, clubIdParam);

	}

	public static List<String> getAdminsByClubId(int clubId) throws Exception {
		// TODO Auto-generated method stub
		return getDaoInstance().getAllAdminsByClubId(clubId);
	}

	public static int getAdminUserIdByClubId(int clubId) throws Exception {
		return getDaoInstance().getAdminUserIdByClubId(clubId);
	}

	public static String getAdminUserNameByClubId(int clubId) throws Exception {
		return getDaoInstance().getAdminUserNameByClubId(clubId);
	}

	public static Map<Integer, Integer> getUserClubMap() throws Exception {
		return getDaoInstance().getUserClubMap();
	}

	public static boolean deleteInActiveUser(int userId) throws Exception {
		return getDaoInstance().deleteInActiveUser(userId);
	}

	public static UserDto findUserByNameAndEmail(String fName, String lName, String email, int clubId)
			throws Exception {
		List<UserDto> users = getDaoInstance().findUserByNameAndEmail(fName, lName, email, clubId);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	public static List<Integer> getUserAllClubIds(int userId) throws Exception {
		return getDaoInstance().getUserAllClubIds(userId);
	}
	
	public static List<Integer> getUserAllClubIdsWithAdminAccess(int userId) throws Exception {
		return getDaoInstance().getUserAllClubIdsWithAdminAccess(userId);
	}

	public static Map<String, String> getUmpireAdministratorsEmailsMap(int clubId) throws Exception {
		return getDaoInstance().getUmpireAdministratorEmailsMap(clubId);
	}

}
