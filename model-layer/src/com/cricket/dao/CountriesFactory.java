package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.CountriesDto;
import com.cricket.dto.StateDto;
import com.cricket.utility.CommonUtility;

public class CountriesFactory {

	private static CountriesDAO countryDAO = null;

	private static CountriesDAO getDaoInstance() {
		if (countryDAO == null) {
			countryDAO = new CountriesDAO();
		}
		return countryDAO;
	}

	public static List<CountriesDto> getCountries() throws Exception {
		return getDaoInstance().getCountries(0,null);
	}
	
	public static Map<String, String> getCountryCodeNameMap() throws Exception {
		return getDaoInstance().getCountryCodeNameMap();
	}
	
	public static CountriesDto getCountryByCode(String countryCode) throws Exception {
		if(!CommonUtility.isNullOrEmptyOrNULL(countryCode)) {
			List<CountriesDto> countries = getDaoInstance().getCountries(0,countryCode);
			return countries.get(0);
		}
		return null;		
	}
	
	public static List<StateDto> getStates(String countryCode) throws Exception {
		return getDaoInstance().getStates(countryCode,null,0);
	}
	
	public static StateDto getStateById(int stateId) throws Exception {
		
		List<StateDto> states = getDaoInstance().getStates(null,null,stateId);
		
		if(!CommonUtility.isListNullEmpty(states)) {
			return states.get(0);
		}
		return null;	
	}
	
	public static StateDto getStateByName(String countryCode, String stateName) throws Exception {
		
		List<StateDto> states = getDaoInstance().getStates(countryCode, stateName,0);
		
		if(!CommonUtility.isListNullEmpty(states)) {
			return states.get(0);
		}
		return null;		 
	}
	
	public static int insertFantasyCountry(CountriesDto countryDto) throws Exception {
		return getDaoInstance().insertFantasyCountry(countryDto);
	}
	
	public static void updateFantasyCountry(CountriesDto countryDto) throws Exception {
		getDaoInstance().updateFantasyCountry(countryDto);
	}
	
	public static void deleteFantasyCountry(int countryId) throws Exception {
		getDaoInstance().deleteFantasyCountry(countryId);
	}
	
	public static int insertFantasyState(StateDto stateDto) throws Exception {
		return getDaoInstance().insertFantasyState(stateDto);
	}
	
	public static void updateFantasyState(StateDto stateDto) throws Exception {
		getDaoInstance().updateFantasyState(stateDto);
	}
	
	public static void deleteFantasyState(int stateId) throws Exception {
		getDaoInstance().deleteFantasyState(stateId);
	}
	
}
