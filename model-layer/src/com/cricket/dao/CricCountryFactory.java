package com.cricket.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.CricCountryDto;

public class CricCountryFactory {
	static Logger log = LoggerFactory.getLogger(CricCountryFactory.class);
	private static CricCountryDAO cricCountryDao = null;

	private static CricCountryDAO getDaoInstance() {
		if (cricCountryDao == null) {
			cricCountryDao = new CricCountryDAO();
		}
		return cricCountryDao;
	}
	public static List<CricCountryDto> getCricCountries() throws Exception{
		List<CricCountryDto> cricCountryList = null;
		try {
			cricCountryList = getDaoInstance().getCricCountries();
		} catch (Exception e) {
			throw new Exception(e.getMessage());		}
		return cricCountryList;
	}

}
