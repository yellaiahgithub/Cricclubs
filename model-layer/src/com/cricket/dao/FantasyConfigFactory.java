package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.FantasyConfigDto;

public class FantasyConfigFactory {
	
	private static FantasyConfigDAO fantasyConfigDAO = null;

	private static FantasyConfigDAO getDaoInstance() {
		if (fantasyConfigDAO == null) {
			fantasyConfigDAO = new FantasyConfigDAO();
		}
		return fantasyConfigDAO;
	}

	public static List<FantasyConfigDto> getFantasyConfigDetails(String name) throws Exception {
		return getDaoInstance().getFantasyConfigDetails(name);
	}
	
	public static Map<String,Integer> getConfigDetailsBySearchString(String searchStr) throws Exception {
		return getDaoInstance().getConfigDetailsBySearchString(searchStr);
	}
	
	
	public static float getFantasyConfigValueByName(String name) throws Exception {		
		return getDaoInstance().getFantasyConfigValueByName(name);			
	}
	
	public static boolean isFantasyWriterDBConnected() throws Exception {		
		return getDaoInstance().isFantasyWriterDBConnected();			
	}
	public static boolean isFantasyReaderDBConnected() throws Exception {		
		return getDaoInstance().isFantasyReaderDBConnected();			
	}
	
}
