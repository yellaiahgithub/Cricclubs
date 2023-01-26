package com.cricket.dao;

import java.util.List;

import com.cricket.dto.FantasyBonusDto;
import com.cricket.utility.CommonUtility;

public class FantasyBonusFactory {
	
	public static final int ACTIVE = 1;
	public static final int INACTIVE = 0;
	public static final int SHOWINAPP = 1;
	public static final int NOTFORAPP = 0;
	
	private static FantasyBonusDAO fantasyBonusDAO = null;

	private static FantasyBonusDAO getDaoInstance() {
		if (fantasyBonusDAO == null) {
			fantasyBonusDAO = new FantasyBonusDAO();
		}
		return fantasyBonusDAO;
	}

	public static List<FantasyBonusDto> getOfferSByBonusForApp(String offerFor) throws Exception {
		return getDaoInstance().getOffers(null,0,1,SHOWINAPP,offerFor);
	}
	
	public static List<FantasyBonusDto> getOffersForAdmin() throws Exception {
		return getDaoInstance().getOffers(null,0,-1,-1,null);
	}
	
	public static List<FantasyBonusDto> getOffersByBonusFor(String offerFor) throws Exception {
		return getDaoInstance().getOffers(null,0,1,-1,offerFor);
	}
	
	public static FantasyBonusDto getOfferByCode(String bonusCode, int status) throws Exception {
		
		List<FantasyBonusDto> bonusDtos = getDaoInstance().getOffers(bonusCode,0,status,-1,null);
		if(!CommonUtility.isListNullEmpty(bonusDtos)) {
			return bonusDtos.get(0);
		}
		return null;		
	}
	
	public static FantasyBonusDto getOfferById(int offerId) throws Exception {
		
		List<FantasyBonusDto> bonusDtos = getDaoInstance().getOffers(null,offerId,-1,-1,null);
		if(!CommonUtility.isListNullEmpty(bonusDtos)) {
			return bonusDtos.get(0);
		}
		return null;		
	}
	
	public static void updateFantasyOffer(FantasyBonusDto offer) throws Exception {
		getDaoInstance().updateFantasyOffer(offer);		
	}
	
	public static void insertFantasyOffer(FantasyBonusDto offer) throws Exception {
		getDaoInstance().insertFantasyOffer(offer);		
	}
	
}
