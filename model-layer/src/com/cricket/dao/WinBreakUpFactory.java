package com.cricket.dao;

import java.util.List;

import com.cricket.dto.WinBreakUpDto;

public class WinBreakUpFactory {

	private static WinBreakUpDAO winBreakUpDAO = null;

	private static WinBreakUpDAO getDaoInstance() {
		if (winBreakUpDAO == null) {
			winBreakUpDAO = new WinBreakUpDAO();
		}
		return winBreakUpDAO;
	}

	public static List<WinBreakUpDto> getWinBreakUps(int contestId, int masterContestId) throws Exception {
		return getDaoInstance().getWinBreakUps(contestId, masterContestId);		
	}
	
	public static List<WinBreakUpDto> getWinBreakUpsForMastetContestIds(String masterContestIds) throws Exception {
		return getDaoInstance().getWinBreakUpsForMastetContestIds(masterContestIds);		
	}
	
	public static int insertWinningBreakUps(List<WinBreakUpDto> wbrList) throws Exception {
		return getDaoInstance().insertWinningBreakUps(wbrList);		
	}
	
	public static void deleteMasterWinningBreakUps(int masterContestId) throws Exception {
		 getDaoInstance().deleteMasterWinningBreakUps(masterContestId);		
	}
	
	public static void deleteContestWinningBreakUps(int contestId) throws Exception {
		 getDaoInstance().deleteContestWinningBreakUps(contestId);		
	}
	
	public static List<WinBreakUpDto> getMasterWinBreakUpsByNoOfParticipants(int noOfParticipants) throws Exception {
		return getDaoInstance().getMasterWinBreakUpsByNoOfParticipants(noOfParticipants);		
	}
	
	public static void addWinningBreakUpsForDupContest(int oldContestId, int newContestId) throws Exception {
		getDaoInstance().addWinningBreakUpsForDupContest(oldContestId, newContestId);		
	}

}
