package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.FantasyContentDto;
import com.cricket.dto.FantasyFaqsDto;

public class FantasyContentFactory {
	
	private static FantasyContentDAO fantasyContentDAO = null;

	private static FantasyContentDAO getDaoInstance() {
		if (fantasyContentDAO == null) {
			fantasyContentDAO = new FantasyContentDAO();
		}
		return fantasyContentDAO;
	}

	public static String getContentDesc(String name) throws Exception {
		return getDaoInstance().getContentDesc(name);
	}
	
	public static Map<String,String> getContentMap() throws Exception {
		return getDaoInstance().getContentMap();
	}
	
	public static List<FantasyFaqsDto> getFantasyFaqs() throws Exception {
		return getDaoInstance().getFantasyFaqs();
	}
	
	public static void updateContestDesc(FantasyContentDto contentDto) throws Exception {
		getDaoInstance().updateContestDesc(contentDto);
	}
	
}
