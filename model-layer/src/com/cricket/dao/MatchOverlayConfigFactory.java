package com.cricket.dao;

import com.cricket.dto.MatchOverlayConfigDto;
import com.cricket.dto.OverlayMatchDataDto;

public class MatchOverlayConfigFactory {
	
	private static MatchOverlayConfigDAO matchOverlayConfigDAO = null;
	
	private static MatchOverlayConfigDAO getDaoInstance(){
		if(matchOverlayConfigDAO == null){
			matchOverlayConfigDAO = new MatchOverlayConfigDAO();
		}
		return matchOverlayConfigDAO;
	}

	
	public static MatchOverlayConfigDto getConfig(int matchId,int clubId) throws Exception{
		return getDaoInstance().getMatchOverlayConfig(clubId, matchId);
	}

	public static void createConfig(MatchOverlayConfigDto config) throws Exception {
		getDaoInstance().createConfig(config);
	}
	
	public static void updateConfig(MatchOverlayConfigDto config) throws Exception {
		getDaoInstance().updateConfig(config);
	}
	
	public static  OverlayMatchDataDto getOverlayMatchData(int clubId,int matchId) throws Exception{
		return getDaoInstance().getOverlayMatchData(clubId,matchId);
	}
	
	public static void insertOverlayMatchData(OverlayMatchDataDto odto) throws Exception {
		getDaoInstance().insertOverlayMatchData(odto);
	}

}
