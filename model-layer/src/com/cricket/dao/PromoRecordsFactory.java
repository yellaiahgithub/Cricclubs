package com.cricket.dao;

import java.util.List;

import com.cricket.dto.PromoRecords;

public class PromoRecordsFactory {

	private static PromoRecordsDao paymentCommonDao = null;
	
	private static PromoRecordsDao getDaoInstance(){
		if(paymentCommonDao == null){
			paymentCommonDao = new PromoRecordsDao();
		}
		return paymentCommonDao;
	}
	
	public static void saveUpdatePromoRecords(List<PromoRecords> promoRecords, int clubId, int userId) throws Exception{
		getDaoInstance().saveUpdatePromoRecords(promoRecords, clubId, userId);
	}
	
	public static List<PromoRecords> getPromoRecords(int clubId, int typeId, String type, boolean showAll) throws Exception{
		return getDaoInstance().getPromoRecords(clubId, typeId, type, showAll);
	}

	public static int deletePromoCode(int clubId, int typeId, int id, String type) throws Exception {
		return getDaoInstance().deletePromoCode(clubId, typeId, id, type);
	}
}

