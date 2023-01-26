package com.cricket.exception;

import java.util.HashMap;
import java.util.Map;

public class CCErrorConstant {

	public static final String PLAYER_NOT_FOUND_C = "PNFC";
	public static final String MATCH_NOT_FOUND_C = "MNFC";
	public static final String LEAGUE_NOT_FOUND_C = "LNFC";
	public static final String FIXTURE_NOT_FOUND_C = "FNFC";
	public static final String TEAM_NOT_FOUND_C = "TNFC";
	public static final String UNKNOWN_ERROR_C = "UNER";
	public static final String INPUT_PARAM_MISSING_C = "IPMC";
	public static final String UNAUTHORIZED_ACCESS = "UNAC";
	public static final String ADMIN_ACCESS_REQUIRED = "AARC";
	public static final String FANTASY_ADMIN_ACCESS_REQUIRED = "FAARC";
	public static final String ADMIN_CAPTAIN_ACCESS_REQUIRED = "ACARC";
	public static final String PLAYER_ACCESS_REQUIRED = "PARC";
	public static final String VALID_IDS_REQUIRED = "VIRC";
	public static final String UPDATE_FAILED = "UFC";
	public static final String LEAGUE_NAME_REQUIRED = "LNRC";
	public static final String SCORER_CHANGED_ERROR = "SCEC";
	public static final String BOTH_TEAM_NAMES_REQUIRED = "BTNRC";
	public static final String NO_CRITERIA_SELECTED_FOR_PLAYERS = "NCSFPC";
	public static final String CLUB_ID_REQUIRED = "CIRC";
	public static final String UNABLE_TO_READ_FORM_PARAMETERS = "UTRFPC";
	public static final String SESSION_EXPIRED_C = "SEC";
	public static final String UNABLE_TO_PERFORM_OPERATION = "UTPOC";
	public static final String EXCEEDING_TEAM_LIMIT = "ETLC";
	public static final String PLAYER_DELETE_EXCEPTION = "PDE";
	public static final String UNABLE_TO_ADD_BALL = "UNTABALL";
	
	public static Map<String, String> errorDescMap = new HashMap<String, String>();
	
	static {
		
		errorDescMap.put(CLUB_ID_REQUIRED, "ClubId Required");
		
		errorDescMap.put(PLAYER_NOT_FOUND_C, "Player Not Found with given details.");
		errorDescMap.put(MATCH_NOT_FOUND_C, "Match Not Found with given details.");
		errorDescMap.put(LEAGUE_NOT_FOUND_C, "League Not Found with given details.");
		errorDescMap.put(FIXTURE_NOT_FOUND_C, "Fixture Not Found with given details.");
		errorDescMap.put(TEAM_NOT_FOUND_C, "Team Not Found with given details.");
		errorDescMap.put(INPUT_PARAM_MISSING_C, "Request param missing for the requested page.");
		errorDescMap.put(UNAUTHORIZED_ACCESS, "You are not authorized to access this page.");
		errorDescMap.put(ADMIN_ACCESS_REQUIRED, "Admin Acess is required to perform this operation.");
		errorDescMap.put(FANTASY_ADMIN_ACCESS_REQUIRED, "Fantasy Admin Acess is required to perform this operation.");
		errorDescMap.put(ADMIN_CAPTAIN_ACCESS_REQUIRED, "Only Captain or Admin can perform this operation.");
		errorDescMap.put(PLAYER_ACCESS_REQUIRED, "Player Acess is required to perform this operation.");
		errorDescMap.put(PLAYER_ACCESS_REQUIRED, "Player Acess is required to perform this operation.");
		errorDescMap.put(VALID_IDS_REQUIRED, "Valid String of Ids required.");
		errorDescMap.put(UPDATE_FAILED, "Update Failed - zero rows updated");
		errorDescMap.put(LEAGUE_NAME_REQUIRED, "League name is required.");
		errorDescMap.put(BOTH_TEAM_NAMES_REQUIRED, "Both teams are required to create a match.");
		errorDescMap.put(NO_CRITERIA_SELECTED_FOR_PLAYERS, "No criteria selected for Player.");
		errorDescMap.put(SCORER_CHANGED_ERROR, "Scorer Changed");
		errorDescMap.put(UNABLE_TO_READ_FORM_PARAMETERS, "Unable to read form Parameters.");
		errorDescMap.put(SESSION_EXPIRED_C, "session expired.");
		errorDescMap.put(UNABLE_TO_PERFORM_OPERATION, "Unable to perform the operation");
		errorDescMap.put(EXCEEDING_TEAM_LIMIT, "Team Limit Exceeded.");
		errorDescMap.put(PLAYER_DELETE_EXCEPTION, "Unable to delete player. Exception occured.");
		errorDescMap.put(UNABLE_TO_ADD_BALL, "Unable to add Ball. Please contact Admin.");
		errorDescMap.put(UNKNOWN_ERROR_C, "Unknown Error Occured.");
	}
	
	
}
