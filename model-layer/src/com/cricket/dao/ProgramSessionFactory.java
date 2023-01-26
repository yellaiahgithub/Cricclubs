package com.cricket.dao;

import java.util.List;

import com.cricket.beans.AcademySessionBean;
import com.cricket.dto.AcademyPlayerReportDto;
import com.cricket.dto.LiveProgramSessionDto;
import com.cricket.dto.ProgramSessionDto;
import com.cricket.dto.SessionPlayerDto;

public class ProgramSessionFactory {

	private static ProgramSessionDAO programSessionDao = null;

	private static ProgramSessionDAO getDaoInstance() {
		if (programSessionDao == null) {
			programSessionDao = new ProgramSessionDAO();
		}
		return programSessionDao;
	}

	public static int createSessionAndAddPlayer(SessionPlayerDto sessionPlayerDto, int clubId, String user) throws Exception {
		return getDaoInstance().createSessionAndAddPlayer(sessionPlayerDto, clubId, user);
	}
	
	public static List<ProgramSessionDto> getProgramSessions(int clubId, boolean forScoring, int limit) throws Exception {
		return getDaoInstance().getProgramSessions(0, 0, clubId, null, null,0,0, forScoring);
	}
	
	public static List<LiveProgramSessionDto> getLiveProgramSessions(int clubId, String userType, int userTypeId, int limit) throws Exception {
		return getDaoInstance().getLiveProgramSessions(clubId, userType, userTypeId, limit);
	}

	public static SessionPlayerDto getSessionInfo(int clubId, int sessionId, String restId) throws Exception {
		return getDaoInstance().getSessionInfo(sessionId, clubId, restId);
	}
	
	public static List<ProgramSessionDto> getProgramSessions(int clubId, int programId, int sessionId, String isComplete, String restId) throws Exception {
		return getDaoInstance().getProgramSessions(programId, sessionId, clubId, isComplete, restId, 0, 0, false);
	}
	public static List<ProgramSessionDto> getProgramSessions(int clubId, int programId, int sessionId, String isComplete, String restId,int batchId) throws Exception {
		return getDaoInstance().getProgramSessions(programId, sessionId, clubId, isComplete, restId, batchId, 0, false);
	}
	
	public static ProgramSessionDto getProgramSessionInfo(int clubId, int sessionId, String restId) throws Exception {
		return getDaoInstance().getProgramSessionInfo(sessionId, clubId, restId);
	}
	
	public static AcademyPlayerReportDto getPlayerReportByPlayerId(int playerId, int clubId, String fromDate, 
			String toDate, int programId, int batchId) throws Exception{
		return getDaoInstance().getPlayerReportByPlayerId(playerId, clubId, fromDate, toDate, programId, batchId);
	}
	
	public static String deleteSession(int sessionId, int clubId) throws Exception{
		return getDaoInstance().deleteSession(sessionId, clubId);
	}

	public static boolean editSession(int clubId, int userId, AcademySessionBean sessionBean) throws Exception{
		return getDaoInstance().editSession(clubId, userId, sessionBean);
	}

	public static List<Integer> getActiveAcademies() throws Exception {
		
		return getDaoInstance().getActiveAcademies();
	}

	public static void UpdateSessionScorer(int clubId, int sessionId, int userId) throws Exception {
		getDaoInstance().UpdateSessionScorer(clubId, sessionId, userId);
	}
	
	
	public static int getUserType(int club_id,String user_type_id,String userType,long program_id) {
		return getDaoInstance().getUserType(club_id,user_type_id, userType,program_id);
	}
	
}
