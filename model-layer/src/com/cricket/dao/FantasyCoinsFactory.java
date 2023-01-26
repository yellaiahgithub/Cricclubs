package com.cricket.dao;

import java.util.List;

import com.cricket.dto.CoinsDailyBonusDto;
import com.cricket.dto.FantasyCoinsDto;
import com.cricket.dto.UserLoginCoinsDto;
import com.cricket.utility.CommonUtility;

public class FantasyCoinsFactory {
	
	private static FantasyCoinsDAO fantasyCoinsDAO = null;

	private static FantasyCoinsDAO getDaoInstance() {
		if (fantasyCoinsDAO == null) {
			fantasyCoinsDAO = new FantasyCoinsDAO();
		}
		return fantasyCoinsDAO;
	}

	public static List<FantasyCoinsDto> getFantasyCoinsInfo(String currency) throws Exception {
		return getDaoInstance().getFantasyCoinsInfo(currency,0);
	}
	
	public static FantasyCoinsDto getFantasyCoinsByAmount(String currency, float amount) throws Exception {
		
		List<FantasyCoinsDto> dtoList = getDaoInstance().getFantasyCoinsInfo(currency,amount);
		
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
	
	public static UserLoginCoinsDto getUserLoginCoinsInfo(long userId) throws Exception {
		return getDaoInstance().getUserLoginCoinsInfo(userId);
	}
	
	public static void insertUserLoginCoinsInfo(String dateStr, int counter, long userId) throws Exception {
		getDaoInstance().insertUserLoginCoinsInfo(dateStr, counter, userId);
	}
	
	public static void updateUserLoginCoinsInfo(String dateStr, int counter, long userId) throws Exception {
		getDaoInstance().updateUserLoginCoinsInfo(dateStr, counter, userId);
	}
	
	public static List<CoinsDailyBonusDto> getCoinsDailyBonusDtos() throws Exception {
		return getDaoInstance().getCoinsDailyBonusDto(0);
	}
	
	public static CoinsDailyBonusDto getCoinsDailyBonusByDay(int day) throws Exception {

		List<CoinsDailyBonusDto> dtoList = getDaoInstance().getCoinsDailyBonusDto(day);
		
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
	
}
