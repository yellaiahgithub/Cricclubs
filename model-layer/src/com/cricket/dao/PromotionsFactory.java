package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.PromotionsDto;
import com.cricket.dto.hotstarpromo.HsplLeagueDto;
import com.cricket.dto.hotstarpromo.HsplPromoCode;
import com.cricket.dto.hotstarpromo.HsplTeamDto;

public class PromotionsFactory {
	
	private static PromotionsDAO promotionsDAO = null;
	
	private static PromotionsDAO getDaoInstance(){
		if(promotionsDAO == null){
			promotionsDAO = new PromotionsDAO();
		}
		return promotionsDAO;
	}

	public static List<PromotionsDto> GetPromoDetailsByRegisteredEmail(String emailId) throws Exception{
		return getDaoInstance().GetPromoDetailsByRegisteredEmail(emailId);
	}
	
	public static List<PromotionsDto> GetRegistreInfoForAdminEmail(String emailId) throws Exception{
		return getDaoInstance().GetRegistreInfoForAdminEmail(emailId);
	}
	
	public static boolean AddPromotionalDetails(PromotionsDto promotionsDto) throws Exception{
		return getDaoInstance().AddPromotionalDetails(promotionsDto);
	}
	
	public static boolean UpdatePromotionalDetails(PromotionsDto promotionsDto) throws Exception{
		return getDaoInstance().UpdatePromotionalDetails(promotionsDto);
	}
	
	public static boolean checkHotstarValidation(String email) throws Exception{
		return getDaoInstance().checkHotstarValidation(email);
	}
	
	public static boolean DeleteInsertPromotionsDetails(String mail) throws Exception{
		return getDaoInstance().DeleteInsertPromotionsDetails(mail);
	}
	
	/***
	 * HotStar Premier League
	 */
	
	public static boolean AddHsplLeagueActivation(HsplLeagueDto hsplLeagueDto) {
		return getDaoInstance().activateHsPLLeague(hsplLeagueDto);
	}
	
	public static String activateHsplTeam(HsplTeamDto hsplTeamDto) {
		return getDaoInstance().activateHsPLTeam(hsplTeamDto);
	}
	
	public static boolean markHsplEmailSent(int leagueId, int teamId) {
		return getDaoInstance().markHplPromotionEmailSent(leagueId, teamId);
	}
	
	public static boolean updateHsplsubscriptionCounts(List<HsplPromoCode> hsplPromoCodeList) {
		return getDaoInstance().updateHPLPormoCodeSubscriptionCount(hsplPromoCodeList);
	}
	
	public static List<HsplPromoCode> getLeagueTeamSubscriptionCountByLeagueId(int leagueId){
		return getDaoInstance().getSubscriptionCountByLeagueId(leagueId);
	}

	public static HsplPromoCode getHplActivationStatus(int clubId, int teamId) {
		// TODO Auto-generated method stub
		return getDaoInstance().isHplActivated(clubId, teamId);
	}

	public static Map<String, List<HsplPromoCode>> getHplPromoCodeSubscriptions(String ServerName) {
		// TODO Auto-generated method stub
		return getDaoInstance().getHplPromoCodeSubsriptions(ServerName);
	}
	
}
