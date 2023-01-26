package com.cricket.dao;

import java.util.List;

import com.cricket.dlscalculation.MatchDLRecord;


public class MatchDLRecordsFactory {

	private static MatchDLRecordsDAO matchDLRecordsDAO = null;
	
	private static MatchDLRecordsDAO getDaoInstance(){
		if(matchDLRecordsDAO == null){
			matchDLRecordsDAO = new MatchDLRecordsDAO();
		}
		return matchDLRecordsDAO;
	}
	
	public static List<MatchDLRecord> getDLRecords( int matchId, int inningId, int clubId) throws Exception {
		return getDaoInstance().getDLRecords(matchId, inningId, clubId);
		
	}
	
	public static float getT2BallsAtLastInterruption(int matchId, int clubId) throws Exception {
		return getDaoInstance().getT2BallsAtLastInterruption(matchId, clubId);
		
	}
	
	public static int save( MatchDLRecord matchDLRecord, int clubId) throws Exception {
		return getDaoInstance().save(matchDLRecord, clubId);
		
	}
	
	public static int update( MatchDLRecord matchDLRecord, int clubId) throws Exception {
		return getDaoInstance().update(matchDLRecord, clubId);
		
	}
}
