package com.cricket.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.CricCountryDto;

public class UserFavCountryFactory {

	private static Logger log = LoggerFactory.getLogger(UserFavCountryFactory.class);
	private static UserFavCountryDAO userFavCountryDao = null;

	private static UserFavCountryDAO getDaoInstance() {
		if (userFavCountryDao == null) {
			userFavCountryDao = new UserFavCountryDAO();
		}
		return userFavCountryDao;
	}
	public static List<CricCountryDto> getUserFavCountries(int userId) {
		List<CricCountryDto> userFavCountries = null;
		try {
			userFavCountries = getDaoInstance().getUserFavCountries(userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return userFavCountries;
	}
	public static String saveUserFavCountries(int userId,String countries) {
		
		String message="";
		try {
			return getDaoInstance().saveUserFavCountries(userId,countries);
		} catch (Exception e) {
			message=e.getMessage();
			log.error(e.getMessage(), e);
			return message;
		}		
	}
}
