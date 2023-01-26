package com.cricket.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.ClubDto;
import com.cricket.dto.ClubRegisterVerificationDto;
import com.cricket.dto.CustomPageDto;
import com.cricket.dto.LeagueDetailsDto;
import com.cricket.dto.SponsorDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.ClubLiteUI;
import com.cricket.mailService.Notifier;
import com.cricket.utility.ClubCacheHandler;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.cricket.utility.batch.GraphDBBackGroundProcess;
import com.ibatis.common.jdbc.ScriptRunner;

public class ClubFactory {
	static Logger log = LoggerFactory.getLogger(ClubFactory.class);
	private static ClubDAO clubDao = null;
	
	private static ClubDAO getDaoInstance(){
		if(clubDao == null){
			clubDao = new ClubDAO();
		}
		return clubDao;
	}

	public static ClubDto getClub(int clubId) throws Exception{
		if(clubId <= 0){
			return null;
		}
		ClubDto club = ClubCacheHandler.getClubFromCache(clubId);
		if(club != null){
			return club;
		}else{
			club = getDaoInstance().getClub(clubId, true);
			if(club!=null){
				club.setLeagueList(LeagueFactory.getLeagues(clubId, 500));
				club.setSponsors(SponsorFactory.getSponsors(clubId));
				ClubCacheHandler.setClubToCache(club);
				
			}
		}
		return club;
	}
	
	public static ClubLiteUI getClubLiteUI(int clubId) throws Exception {
		ClubLiteUI clubLiteUI = ClubCacheHandler.getClubLiteUI(clubId);
		if(clubLiteUI == null || clubLiteUI.getClubId() <= 0 || clubLiteUI.getPages() == null || clubLiteUI.getSponsors() == null) {
			clubLiteUI = new ClubLiteUI();
			clubLiteUI.setClubId(clubId);
			List<CustomPageDto> pages = CustomPagesFactory.getAllPages(clubId, false);
			List<SponsorDto> sponsors = SponsorFactory.getSponsors(clubId);
			clubLiteUI.setPages(pages);
			clubLiteUI.setSponsors(sponsors);
			ClubCacheHandler.setClubLiteUI(clubLiteUI);
		}
		return clubLiteUI;
	}

	public static int createClub(ClubDto club) throws Exception{
		return getDaoInstance().createClub(club);
	}
	
	@Deprecated
	public static List<ClubDto> getClubListSCC(ClubDto club) throws Exception {
		return getDaoInstance().getClubListSCC();
	}

	public static void updateCurrentLeague(int leagueId,int clubId) throws Exception{
		 getDaoInstance().updateCurrentLeague(leagueId,clubId);
		 ClubCacheHandler.clearClubFromCache(clubId);
	}

	public static void updateClub(ClubDto club) throws Exception{
		getDaoInstance().updateClub(club);
		if(club!=null){
			ClubCacheHandler.clearClubFromCache(club.getClubId());
			GraphDBBackGroundProcess.updateLeagueGraphPage(club);
		}
	}
	
	public static void updateClubForUpgrade(ClubDto club) throws Exception{
		getDaoInstance().updateClubForUpgrade(club);
		if(club!=null){
			ClubCacheHandler.clearClubFromCache(club.getClubId());
		}
	}
	
	public static void updateClubPaymentDetails(ClubDto club) throws Exception{
		getDaoInstance().updateClubPaymentDetails(club);
		if(club!=null){
			ClubCacheHandler.clearClubFromCache(club.getClubId());
		}
	}
	
	public static void updateClubFeatures(ClubDto club) throws Exception{
		getDaoInstance().updateClubFeatures(club);
		if(club!=null){
			ClubCacheHandler.clearClubFromCache(club.getClubId());
		}
	}

	public static List<ClubDto> getAllActiveClubs() throws Exception {
		 List<ClubDto> clubs = ClubCacheHandler.getClubsFromCache();
		if(clubs != null & !clubs.isEmpty()){
			return clubs;
		}else{
			clubs = getAllActiveClubsFromDB();
		}
		return clubs;
	}
	
	public static List<ClubDtoLite> getActiveLiteClubs() throws Exception {
		 List<ClubDtoLite> clubs = ClubCacheHandler.getLiteClubsFromCache();
		if(clubs != null && !clubs.isEmpty()){
			return clubs;
		}else{
			clubs = getLiteClubsFromDB(0, true);
		}
		return clubs;
	}
	
	public static ClubDtoLite getActiveLiteClub(int clubId, boolean isActive) throws Exception {
		ClubDtoLite club = ClubCacheHandler.getClubLiteFromCache(clubId);
		if(club == null){
			List<ClubDtoLite> clubs = getLiteClubsFromDB(clubId, true);
			if(clubs != null && !clubs.isEmpty()) {
				club = clubs.get(0);
				club.setLeagueList(LeagueFactory.getLiteLeagues(clubId));
				ClubCacheHandler.setClubLiteToCache(club);
			}
		}
		return club;
	}
	
	public static ClubDtoLite getLiteClubFromDB(int clubId, boolean isActiveOnly) throws Exception {
		List<ClubDtoLite> clubs = getDaoInstance().getLiteClubsFromDB(clubId, isActiveOnly);
		ClubDtoLite club = null;
		if(clubs != null && !clubs.isEmpty()) {
			club = clubs.get(0);
			club.setLeagueList(LeagueFactory.getLiteLeagues(clubId));
			ClubCacheHandler.setClubLiteToCache(club);
		}
		return club;
	}
	public static List<ClubDtoLite> getLiteClubsFromDB(int clubId, boolean isActiveOnly) throws Exception {
		return getDaoInstance().getLiteClubsFromDB(clubId, isActiveOnly);
	}
	
	public static ClubDtoLite getLiteClubDetails(int clubId) throws Exception {
		return getDaoInstance().getLiteClubDetails(clubId);
	}

	public static List<ClubDto> getAllActiveClubsFromDB() throws Exception {
		return getDaoInstance().getAllActiveClubs();
	}

	public static List<ClubDto> getClubs(String search) throws Exception {
		return getDaoInstance().getClubs(search);
	}
	
	public static List<Integer> getClubIdsByCreatedDateRange(String fromDate, String toDate) throws Exception {
		return getDaoInstance().getClubIdsByCreatedDateRange(fromDate, toDate);
	}
	
	public static Map<Integer, Integer> getClubIdYearByCreatedDateRange(String fromDate, String toDate) throws Exception {
		return getDaoInstance().getClubIdYearByCreatedDateRange(fromDate, toDate);
	}
	
	public static LeagueDetailsDto getLeagueDetails(String query) throws Exception {
		return getDaoInstance().getLeagueDetails(query);
	}
	
	public static int isClubExistsByName(String clubName) throws Exception {
		return getDaoInstance().isClubExistsByName(clubName);
	}
	
	public static int isClubExistsInDB(int clubId) throws Exception {
		return getDaoInstance().isClubExistsInDB(clubId);
	}
	
	public static int isClubExistsByShortCutURL(String shortUrl) throws Exception {
		return getDaoInstance().isClubExistsByShortCutURL(shortUrl);
	}
	
	public static List<ClubDto> getClubs(String search, boolean isActiveCheck) throws Exception {
		return getDaoInstance().getClubs(search, isActiveCheck);
	}
	
	public static List<ClubDto> getLeaguesByNameStartWith(String search, boolean isActiveCheck) throws Exception {
		return getDaoInstance().getLeaguesByNameStartWith(search, isActiveCheck);
	}
	
	public static Integer getClubIdByShortUrl(String search) throws Exception {
		return getDaoInstance().getClubIdByShortUrl(search);
	}

	public static Integer getClubIdByCustomDomain(String search) throws Exception {
		return getDaoInstance().getClubIdByCustomDomain(search);
	}
	
	public static Integer getMaxClubID() throws Exception {
		return getDaoInstance().getMaxClubID();
	}
	
	public static List<Integer> getAcademyClubs() throws Exception {
		return getDaoInstance().getAcademyClubs();
	}
	
	public static void saveStartupMessage(ClubDtoLite club) throws Exception{
		getDaoInstance().saveStartupMessage(club);
		if(club!=null){
			ClubCacheHandler.clearClubFromCache(club.getClubId());
		}
	}
	
	public static void activateClub(ClubDto club) throws Exception {
		
		Connection con = DButility.getDefaultConnection();

		try {
			ScriptRunner sr = new ScriptRunner(con, false, false);

			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put("\\$newSchemaName", "club" + club.getClubId());			

			sr.runScript(new StringReader(CommonUtility.applyParametersToTemplateFile("data", "newClubSchema.sql", parameters)));
			
			if(club != null && club.getIsAcademy() == 1) {
				parameters.put("\\$newClubId", club.getClubId()+"");
				parameters.put("\\$academyName", club.getName());
				sr.runScript(new StringReader(CommonUtility.applyParametersToTemplateFile("data", "newAcademySchema.sql", parameters)));
			}
			
			updateClubStatus(1,club.getClubId());
			
		} catch (Exception e) {
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL,"support@cricclubs.com", 
					"New Club Registered - Error", "Club name:" + club.getName() +"Error: " + e.getMessage(),"");
			log.error("Failed to Execute Club Activation Script. The error is "+e.getMessage(), e);
			
		} finally {
			DButility.closeConnection(con);
		}

	}

	private static void updateClubStatus(int i, int clubId) throws Exception {
		getDaoInstance().updateClubStatus(i,clubId);
		GraphDBBackGroundProcess.createLeagueGraphPage(clubId);
	}

	public static ClubDto getOnlyClub(int clubId, boolean isActive) throws Exception {
		
		return getDaoInstance().getClub(clubId, isActive);
	}

	public static void updateClubLatituteLongitude(ClubDto club) throws Exception {
		getDaoInstance().updateClubLatituteLongitude(club);
	}

	public static void createInternalClub(int clubId, String clubStr, int userId) throws Exception {
		ClubDto club = new ClubDto();
		club.setName(clubStr);
		getDaoInstance().createInternalClub(club, clubId, userId);
		
	}
	public static int createInternalClub(ClubDto club,int clubId, int userId) throws Exception {
		return getDaoInstance().createInternalClub(club, clubId, userId);
	}
	
	public static void updateInternalClub(ClubDto club, int clubId, int userId) throws Exception {
		getDaoInstance().updateInternalClub(club, clubId, userId);
		
	}
	public static int getTeamCountForLeague(int internalClubId, int clubId) throws Exception {
		int teamCount = getDaoInstance().getTeamCountForLeague(internalClubId, clubId);		
			return teamCount;
	}
	
	public static int deleteInternalClub(int internalClubId, int clubId) throws Exception {		
			getDaoInstance().deleteInternalClub(internalClubId, clubId);
			return 0;
	}
	
	public static List<ClubDto> getInternalClubs(int clubId) throws Exception{
		return getDaoInstance().getInternalClubs(clubId,0);
	}
	public static void startTrial(int clubId) throws Exception{
		ClubDtoLite club = getActiveLiteClub(clubId, true);
		if(club != null){
			getDaoInstance().startTrial(clubId, club.getTrialDate() == null);
			ClubCacheHandler.clearClubFromCache(clubId);
		}
	}	
	public static void switchToOldLook(int clubId) throws Exception{
		getDaoInstance().updateToOldLook(clubId);
		ClubCacheHandler.clearClubFromCache(clubId);
	}	

	public static ClubDto getInternalClub(int internalClubId,int clubId) throws Exception{
		List<ClubDto> internalClubs = getDaoInstance().getInternalClubs(clubId,internalClubId);
		if(internalClubs != null && !internalClubs.isEmpty()){
			return internalClubs.get(0);
		}
		return null;
	}

	public static List<Integer> getClubAdminIdsByPlayerId(int playerID, int clubId) throws Exception {
		if(playerID > 0){
			return getDaoInstance().getClubAdminIdsByPlayerId(playerID, clubId);
		}else{
			return new ArrayList<Integer>();
		}
	}
	
	public static void updateClubLogoFilePath(String logo_file_path,int clubId) throws Exception {
		getDaoInstance().updateClubLogoFilePath(logo_file_path,clubId);
		ClubCacheHandler.clearClubFromCache(clubId);
		GraphDBBackGroundProcess.updateGraphLeagueLogoPath(logo_file_path, clubId);		
	}
	
	public static void updateClubBackGroundImagePath(String backGroundImagePath,int clubId) throws Exception {
		getDaoInstance().updateClubBackGroundImagePath(backGroundImagePath,clubId);
		ClubCacheHandler.clearClubFromCache(clubId);
	}
	
	public static void updateClubUnlimitedStreamStatus(int status,int clubId) throws Exception {
		getDaoInstance().updateClubUnlimitedStreamStatus(status,clubId);
	}
	
	public static void allowStreamingForClub(int clubId) throws Exception {
		getDaoInstance().allowStreamingForClub(clubId);
	}

	public static void addPlayerToClub(int playerID, int internalClubId, int clubId, String userId) throws Exception {
		if(internalClubId > 0) {
			getDaoInstance().addPlayerToClub(playerID,internalClubId, clubId, userId);
		}
	}
	// Due to time constraint.. calling below method. we should be handling in diffrent way.
	public static int addPlayerToClub(List<String> selectedPlayersList, String internalClubId, int clubId,
			String userId) throws Exception {
		
		List<String> inactiveClubPlayers = getDaoInstance().getClubInactivePlayers(internalClubId, clubId, userId);
		
		getDaoInstance().deleteFromInternalClub(internalClubId, clubId, userId);
		/*
		for(String playerId : selectedPlayersList) {
			int player = CommonUtility.stringToInt(playerId);
			int internalClub = CommonUtility.stringToInt(internalClubId);
			if(player > 0 && internalClub > 0) {
				getDaoInstance().addPlayerToClub(player,internalClub, clubId, userId);
			}
		}
		return 0;*/
		if(inactiveClubPlayers != null && inactiveClubPlayers.size() > 0) {
			inactiveClubPlayers.addAll(selectedPlayersList);
			return getDaoInstance().addPlayersToClub(inactiveClubPlayers,internalClubId, clubId, userId);
		}else {
			return getDaoInstance().addPlayersToClub(selectedPlayersList,internalClubId, clubId, userId);
		}
	}

	public static List<ClubDto> getInternalClubOfPlayer(String playerId, int clubId) throws Exception {
		return getDaoInstance().getInternalClubOfPlayer(playerId,clubId);
	}
	
	public static  Map<String, String> getAllInternalClubs(int clubId) throws Exception{
		return getDaoInstance().getAllInternalClubs(clubId);
	}
	
	public static List<Integer> getInternalClubIdsOfPlayer(int playerId, int clubId) throws Exception{
		return getDaoInstance().getInternalClubIdsOfPlayer(playerId,clubId);
	}

	public static int lockInternalClub(int clubId, String internalClubId, String isLock) throws Exception{
		return getDaoInstance().lockInternalClub(clubId,internalClubId, isLock);
	}

	public static int lockAllInternalClub(int clubId, String isLock) throws Exception{
		return getDaoInstance().lockInternalClub(clubId, isLock);
	}
	
	public static Map<Integer, String> getAllInternalClub(String leagueIds, int clubId) throws Exception {
		return getDaoInstance().getAllInternalClub(leagueIds, clubId);
	}

	public static int deletePlayerFromInternalClub(int playerId, int clubId, String userIdBy) throws Exception {
		return getDaoInstance().deletePlayerFromInternalClub(playerId, clubId, userIdBy);
	}
	
	public static int deletePlayerFromInternalClub(int playerId, int internalClubId, int clubId, String userIdBy) throws Exception {
		return getDaoInstance().deletePlayerFromInternalClub(playerId,internalClubId, clubId, userIdBy);
	}

	public static boolean isDbConnected() {
		// TODO Auto-generated method stub
		return getDaoInstance().isDbConnected();
	}
	public static boolean isReadDbConnected() {
		// TODO Auto-generated method stub
		return getDaoInstance().isReadDbConnected();
	}

	public static void updateScheduler(boolean isTrue) throws Exception {
		getDaoInstance().updateScheduler(isTrue);
	}
	
	public static boolean getSyncScheduleState(int clubId) throws Exception {
		return getDaoInstance().getSyncScheduleState(clubId);
	}

	public static int updatePlayerIntoClubByTeam(TeamDto team, int clubId) throws Exception {
		return getDaoInstance().updatePlayerIntoClubByTeam(team, clubId);
	}

	public static List<ClubDtoLite> getPlayerClubInfo(int playerId) throws Exception{
		return getDaoInstance().getPlayerClubInfo(playerId);
	}
	
	public static List<ClubDtoLite> getUserClubInfo(long userId, int clubId, String association) throws Exception{
		return getDaoInstance().getUserClubInfo(userId, clubId, association);
	}
	
	public static List<ClubDtoLite> getAssociationClubs(String association) throws Exception{
		return getDaoInstance().getClubsByAssociation(association);
	}
	
	public static Map<Integer, String> getPlayerAllInternalClubIdMap(int clubId) throws Exception {
		return getDaoInstance().getPlayerAllInternalClubIdMap(clubId);
	}

	public static int hideShowInternalClub(int clubId, int internalClubId, int hideShowValue) throws Exception {
		getDaoInstance().hideShowInternalClub(clubId, internalClubId, hideShowValue);
		return 0;
	}
	
	public static long insertClubRegOTPDetails(int newLeagueId,String email,int otp ) throws Exception {
		return getDaoInstance().insertClubRegOTPDetails(newLeagueId,email,otp);
	}	
	
	public  static ClubRegisterVerificationDto getClubRegOTPDetails(String emailId) throws Exception {
		
		return getDaoInstance().getClubRegOTPDetails(emailId);
		
	}
	
	public static int  getClubVerifyOTPDetails(int newLeagueId,int otp,String emailId) throws Exception {
		
		 return getDaoInstance().getClubVerifyOTPDetails(newLeagueId,otp,emailId);
		
	}
	
}
