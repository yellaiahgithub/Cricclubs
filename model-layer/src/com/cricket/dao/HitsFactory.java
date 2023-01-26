package com.cricket.dao;

import java.util.Map;

public class HitsFactory {
	
	private static HitsDAO hitsDao = null;
	
	private static HitsDAO getDaoInstance(){
		if(hitsDao == null){
			hitsDao = new HitsDAO();
		}
		return hitsDao;
	}

	
/*	public static int getNumberOfHits(int clubId) throws Exception{
		return getDaoInstance().getNumberOfHits(clubId);
	}*/

	public static Map<Integer, Integer> getNumberOfHitsMap() throws Exception{
		return getDaoInstance().getNumberOfHitsMap();
	}
/*	public static void insertHit(HitDto hit,ClubDto club) throws Exception{
		getDaoInstance().insertHit(hit,club.getClubId());
		CacheManager.addHit(club);
	}*/
}
