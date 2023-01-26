package com.football.dao;

import java.util.List;

import com.cricket.dto.GoalIncidentDto;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.utility.CommonUtility;
import com.football.dto.IncidentDto;

public class IncidentFactory {
	
	public static IncidentDao incidentDao = null;

	private static IncidentDao getDaoInstance() {
		if (incidentDao == null) {
			incidentDao = new IncidentDao();
		}
		return incidentDao;
	}
	
	public static String FIRST_HALF_STARTED = "First Half Started";
	public static String SECOND_HALF_STARTED = "Second Half Started";
	public static String GOAL_SCORED = "Goal Scored";
	public static String GOAL_SCORED_FREE_KICK = "Goal Scored - Free kick";
	public static String GOAL_SCORED_PENALTY_KICK ="Goal Scored - Penalty kick";
	public static String GOAL_SAVED ="Goal saved";
	public static String GOAL_SAVED_PENALTY ="Goal saved - Penalty";
	public static String SELF_GOAL ="Own Goal";
	public static String GOAL_ASSISTS ="Assists";
	public static String SHOT_ON_TARGET ="Shot on target";
	public static String SUBSTITUTE ="Substitute";
	public static String CORNER ="Corner";
	public static String INTERCEPTION ="Interception";
	public static String TACKLE ="Tackle";
	public static String OFFSIDE ="Offside";
	public static String YELLOW_CARD ="Yellow card";
	public static String RED_CARD ="Red card";
	public static String FOUL ="Foul";	
	public static String FOUL_FREE_KICK = "Foul - Free kick";
	public static String FOUL_PENALTY_KICK ="Foul - Penalty kick";
	public static String PENALTY_MISSED ="Penalty Missed";
	public static String ADDITIONAL_TIME ="Additional Time";
	
	public static IncidentDto getIncidentByClientId(long clientId, int clubId) throws Exception {
		List<IncidentDto> dtoList = getDaoInstance().getIncidentsForFootBallMatch(null, clientId, 0, null, clubId, false);
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
	
	public static IncidentDto getIncidentByIncidentId(int incidentId, int clubId) throws Exception {
		List<IncidentDto> dtoList = getDaoInstance().getIncidentsForFootBallMatch(null, 0, incidentId, null, clubId, false);
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
	
	public static List<IncidentDto> getAllIncidentsForFootBallMatch(int matchId, int clubId) throws Exception {
		return getDaoInstance().getIncidentsForFootBallMatch(matchId+"", 0, 0, null, clubId, false);
	}

	public static List<IncidentDto> getIncidentsForFootBallMatches(String matchIdsStr, int clubId) throws Exception {
		return getDaoInstance().getIncidentsForFootBallMatch(matchIdsStr, 0, 0, null, clubId, false);
	}
	
	public static List<IncidentDto> getIncidentsForLiveFootBallMatch(int matchId, long clientId, int incidentId, 
			int clubId, boolean isScoring) throws Exception {
		return getDaoInstance().getIncidentsForFootBallMatch(matchId+"", clientId, incidentId, null, clubId, isScoring);
	}
	
	public static List<GoalIncidentDto> getGoalScoredIncidentsForMatches(String matchIdsStr, int clubId) throws Exception {
		return getDaoInstance().getGoalScoredIncidentsForMatches(matchIdsStr, clubId);
	}
		
	public static int insertIncident(IncidentDto dto, int clubId) throws Exception {
		return getDaoInstance().insertIncident(dto,clubId);
	}
	
	public static int updateIncident(IncidentDto dto, int clubId) throws Exception {
		return getDaoInstance().updateIncident(dto,clubId);
	}
	
	public static String deleteIncidentByClientId(long clientId, int clubId) throws Exception {
		return getDaoInstance().deleteIncident(clientId, 0, clubId);
	}
	
	public static String deleteIncidentByIncidentId(int incidentId, int clubId) throws Exception {
		return getDaoInstance().deleteIncident(0, incidentId, clubId);
	}
	
	public static void updateIncidentApi(IncidentDto idto, int clubId, int scorerId, String sessionId) throws Exception {
		
		if (!"test".equals(sessionId) && !"admin".equals(sessionId)) {
			boolean scorerCheck = false;
			scorerCheck = getDaoInstance().checkScorer(scorerId, clubId, sessionId);
			if (!scorerCheck) {
				throw new CCException("Scorer Changed", CCErrorConstant.SCORER_CHANGED_ERROR);
			}
		}
		getDaoInstance().updateIncident(idto, clubId);
	}
	
	public static void deleteIncidentApi(int incidentId, int clubId, int scorerId, String sessionId) throws Exception {
		
		List<IncidentDto> dtoList = getDaoInstance().getIncidentsForFootBallMatch(null, 0, incidentId, null, clubId, false);
		IncidentDto idto = new IncidentDto();
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			idto = dtoList.get(0);
		}
		if (!"test".equals(sessionId) && !"admin".equals(sessionId) && idto != null) {
			boolean scorerCheck = getDaoInstance().checkScorer(scorerId, clubId, sessionId);
			if (!scorerCheck) {
				throw new CCException("Scorer Changed", CCErrorConstant.SCORER_CHANGED_ERROR);
			}
		}
		getDaoInstance().deleteIncident(0, incidentId, clubId);
	}
	
	public static int getMatchIdByIncidentClientId(long clientId, int clubId) throws Exception {
		return getDaoInstance().getMatchIdByIncidentClientId(clientId, clubId);
	}
	
}
