package com.cricket.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dao.CaptainMatchReportFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dao.UmpireMatchReportFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.CountOfPlayerStatus;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.UserDto;

public class MatchUtility {

	public static String MATCH_TYPE_ALL = "'l','e','ql','sl','q','s','f','3p'";
	public static String MATCH_TYPE_PO = "'e','ql','q','s','f','3p'";
	
	public static List<String> listOfAllMatch = Arrays.asList("l","p","q","s","sl","f","e","ql","3p", "po");
	
	public static boolean isValidateMatchType(String matchType){
		if(MatchUtility.listOfAllMatch.contains(matchType) || CommonUtility.isNullOrEmpty(matchType) )return true;
		return false;
	}
	
	
	public static String getMatchTypeString(String matchType) {
		String matchTypeStr = "";
		if (matchType != null && matchType.equals("l")) {
			matchTypeStr = "L";
		} else if (matchType != null && matchType.equals("p")) {
			matchTypeStr = "P";
		} else if (matchType != null && matchType.equals("q")) {
			matchTypeStr = "QF";
		} else if (matchType != null && matchType.equals("s")) {
			matchTypeStr = "SF";
		} else if (matchType != null && matchType.equals("sl")) {
			matchTypeStr = "SL";
		} else if (matchType != null && matchType.equals("f")) {
			matchTypeStr = "F";
		} else if (matchType != null && matchType.equals("e")) {
			matchTypeStr = "E";
		} else if (matchType != null && matchType.equals("ql")) {
			matchTypeStr = "Q";
		} else if (matchType != null && matchType.equals("3p")) {
			matchTypeStr = "3P";
		}
		return matchTypeStr;
	}

	public static String getMatchTypeFullString(String matchType) {
		String matchTypeStr = "";
		if (matchType != null && matchType.equals("l")) {
			matchTypeStr = "League";
		} else if (matchType != null && matchType.equals("p")) {
			matchTypeStr = "Practice";
		} else if (matchType != null && matchType.equals("q")) {
			matchTypeStr = "Quarter Final";
		} else if (matchType != null && matchType.equals("s")) {
			matchTypeStr = "Semi Final";
		} else if (matchType != null && matchType.equals("sl")) {
			matchTypeStr = "Super League";
		} else if (matchType != null && matchType.equals("f")) {
			matchTypeStr = "Final";
		} else if (matchType != null && matchType.equals("e")) {
			matchTypeStr = "Eliminator";
		} else if (matchType != null && matchType.equals("ql")) {
			matchTypeStr = "Qualifier";
		} else if (matchType != null && matchType.equals("3p")) {
			matchTypeStr = "3rd Position";
		}
		return matchTypeStr;
	}
	
	public static String convertLongMatchTypeToShort(String matchType){
		String matchTypeStr = "";
		if (matchType != null && matchType.equalsIgnoreCase("League")) {
			matchTypeStr = "l";
		} else if (matchType != null && matchType.equalsIgnoreCase("Super League")) {
			matchTypeStr = "sl";
		} else if (matchType != null && matchType.equalsIgnoreCase("Practice")) {
			matchTypeStr = "p";
		} else if (matchType != null && matchType.equalsIgnoreCase("Qualifier")) {
			matchTypeStr = "ql";
		} else if (matchType != null && matchType.equalsIgnoreCase("Quarter Final")) {
			matchTypeStr = "q";
		} else if (matchType != null && matchType.equalsIgnoreCase("Semi Final")) {
			matchTypeStr = "s";
		} else if (matchType != null && matchType.equalsIgnoreCase("Final")) {
			matchTypeStr = "f";
		} else if (matchType != null && matchType.equalsIgnoreCase("Eliminator")) {
			matchTypeStr = "e";
		} else if (matchType != null && matchType.equalsIgnoreCase("3rd Position")) {
			matchTypeStr = "3p";
		}
		return matchTypeStr;
	}

	public static void setUmpairsFromFixture(MatchDto match, FixtureDto fixture) throws Exception {
		if(fixture == null && match != null && match.getMatchID() > 0) fixture = FixturesFactory.getFixtureForMatch(match.getMatchID(), match.getClubID());
			if(fixture != null && match != null){
				if(!CommonUtility.isNullOrEmpty(String.valueOf(fixture.getUmpire1Id())) && (fixture.getUmpire1Id()>0)) {
					UserDto user = UserFactory.getUserById(fixture.getUmpire1Id(), match.getClubID());
					if(user != null){
						//int report = UmpireMatchReportFactory.checkUmpireReport(user.getUserID(),String.valueOf(fixture.getFixtureId()), match.getClubID());			
						match.setUmpire1UserId("u-"+user.getUserID());
						
						match.setUmpire1(user.getFullName());
						fixture.setUmpire1(user.getFullName());
					}
				} else if((fixture.getUmpire1TeamId() > 0)) {
					TeamDto team = TeamFactory.getTeamByTeamId(fixture.getUmpire1TeamId(),match.getClubID());
					if(team != null) {
						match.setUmpire1(team.getTeamName());
						match.setUmpire1UserId("t-"+team.getTeamID());
					}
				}
				if(!CommonUtility.isNullOrEmpty(String.valueOf(fixture.getUmpire2Id())) && (fixture.getUmpire2Id()>0)) {
					UserDto user = UserFactory.getUserById(fixture.getUmpire2Id(), match.getClubID());
					if(user != null){
						//int report = UmpireMatchReportFactory.checkUmpireReport(user.getUserID(),String.valueOf(fixture.getFixtureId()), match.getClubID());
						match.setUmpire2UserId("u-"+user.getUserID());
						
						match.setUmpire2(user.getFullName());
						fixture.setUmpire2(user.getFullName());
					}
				} else if((fixture.getUmpire2TeamId() > 0)) {
					TeamDto team = TeamFactory.getTeamByTeamId(fixture.getUmpire2TeamId(),match.getClubID());
					if(team != null) {
						match.setUmpire2(team.getTeamName());
						match.setUmpire2UserId("t-"+team.getTeamID());
					}
				}
			}
	}
	
	public static long checkCaptainReport(MatchDto match,TeamDto team,int clubId ) throws Exception{
		long userId = 0;
		UserDto user = null;
		if(match != null){
			
			if(team != null) 
				user = UserFactory.getUserByPlayerId(team.getCaptain(), clubId);	
			
			if(user != null){
				userId = CaptainMatchReportFactory.checkCaptainReport(user.getUserID(),String.valueOf(match.getMatchID()), clubId);					
			}			
		}
		return userId;		
	}
	
	public static long checkUmpireReport(int umpireUserId, FixtureDto fixture,int  clubId ) throws Exception{
		long userId = 0;
		if(fixture != null && umpireUserId>0){
			userId = UmpireMatchReportFactory.checkUmpireReport(umpireUserId, String.valueOf(fixture.getFixtureId()), clubId);
		}
		return userId;		
	}
	
	public static boolean isValidUmpairForMatch(MatchDto match, UserDto user) throws Exception {
		FixtureDto fixture = null;
		if(match != null) {
			 fixture = FixturesFactory.getFixtureForMatch(match.getMatchID(), match.getClubID());
		}
		
		if(fixture != null && (!CommonUtility.isNullOrEmpty(String.valueOf(fixture.getUmpire2Id())) && (fixture.getUmpire2Id()>0) 
				|| !CommonUtility.isNullOrEmpty(String.valueOf(fixture.getUmpire1Id())) && (fixture.getUmpire1Id()>0))) {
			if(user != null && (user.getUserID() == fixture.getUmpire1Id() || user.getUserID() == fixture.getUmpire2Id()))
			return true;
		}
		
		return false;
	}
	
	public static boolean isValidUmpairForFixture(FixtureDto fixture, UserDto user) throws Exception {		
		
		if(fixture != null && user != null && (fixture.getUmpire1Id() == user.getUserID() || fixture.getUmpire2Id() == user.getUserID()) ) {
			return true;
		}
		return false;
	}
	
	public static void setUmpairsName(FixtureDto fixtures, int clubId) throws Exception {


		// Check for User or Team and set ID with u- / t- 
		if(fixtures != null){
			if(fixtures.getUmpire1Id() >0) {
				UserDto user = UserFactory.getUserById(fixtures.getUmpire1Id(), clubId);
				if(user != null){
					fixtures.setUmpire1Name(user.getFullName());
				}
			}
			if(fixtures.getUmpire1TeamId() >0) {
				TeamDto team = TeamFactory.getTeamByTeamId(fixtures.getUmpire1TeamId(), clubId);
				if(team != null){
					fixtures.setUmpire1Name(team.getTeamName());
				}
			}
			if(fixtures.getUmpire2Id() >0) {
				UserDto user = UserFactory.getUserById(fixtures.getUmpire2Id(), clubId);
				if(user != null){
					fixtures.setUmpire2Name(user.getFullName());
				}
			}
			if(fixtures.getUmpire2TeamId() >0) {
				TeamDto team = TeamFactory.getTeamByTeamId(fixtures.getUmpire2TeamId(), clubId);
				if(team != null){
					fixtures.setUmpire2Name(team.getTeamName());
				}
			}
		
		}
	}
	
	public static Map<String,String> combinedUmpires(List<TeamDto> teams, List<UserDto> users) {
		Map<String,String> umpiresmap = new LinkedHashMap<String, String>();
		for(TeamDto team:teams){
			umpiresmap.put("t-"+team.getTeamID(),team.getTeamName() + " (Team)");
		}
		for(UserDto user:users){
			if(user.getUmpireID() > 0 && user.getPlayerID() > 0){
				umpiresmap.put("u-"+user.getUserID() ,user.getFullName() + " (Umpire,Player)");
			}else if(user.getUmpireID() > 0){
				umpiresmap.put("u-"+user.getUserID() ,user.getFullName() + " (Umpire)");
			}else if(user.getPlayerID() > 0){
				umpiresmap.put("u-"+user.getUserID() ,user.getFullName() + " (Player)");
			}
		}
		return umpiresmap;
	}
	
	public static void setUmpiresInformation(FixtureDto fixture, String umpire1, String umpire2) {
		if(!CommonUtility.isNullOrEmpty(umpire1) && umpire1.startsWith("t-")){
			fixture.setUmpire1TeamId(CommonUtility.stringToInt(umpire1.replaceAll("t-", "")));
		}
		if(!CommonUtility.isNullOrEmpty(umpire1) && umpire1.startsWith("u-")){
			fixture.setUmpire1Id(CommonUtility.stringToInt(umpire1.replaceAll("u-", "")));
		}

		if(!CommonUtility.isNullOrEmpty(umpire2) && umpire2.startsWith("t-")){
			fixture.setUmpire2TeamId(CommonUtility.stringToInt(umpire2.replaceAll("t-", "")));
		}
		if(!CommonUtility.isNullOrEmpty(umpire2) && umpire2.startsWith("u-")){
			fixture.setUmpire2Id(CommonUtility.stringToInt(umpire2.replaceAll("u-", "")));
		}
		
	}
	
	public static void determineWinner(MatchDto match, LeagueDto league) {
		
		if (match.getIsAbandoned() !=1) {
			match.setWinner(calculateWinner(match, league));
			match.setAbandoneType("");
		} else {
			if(!MatchDto.ABANDONE_TYPE_ABANDONED.equals(match.getAbandoneType()) && !MatchDto.ABANDONE_TYPE_FOREFEIT.equals(match.getAbandoneType())){
				match.setIsAbandoned(0);
				//winner is already part of match.
			}
			//match.setWinner(0);
		}
	}
	
	public static int calculateWinner(MatchDto match, LeagueDto league) {
		
		if(league.getSeriesType().contains("Test")) {
			if(match.getIsFollowon() <= 0) {
				if((match.getT2_2balls() > 0 || match.getT2_2total()>0)) {
					if(match.getT1TotalRuns() < match.getT2TotalRuns()) {
						return match.getBattingSecond();
					}//changed to this condition cuz draw is not working LM-1120
					else if(match.getT2_2wickets() >= match.getT2TotalWickets() && match.getT1TotalRuns() > match.getT2TotalRuns()){
						
					//}else if(match.getT1TotalRuns() > match.getT2TotalRuns()){
						return match.getBattingFirst();
					}else {
						return 0;
					}
				}else if((match.getT2_2balls() <= 0  && match.getT1_2wickets() >= match.getT1TotalWickets() 
						&&  match.getT2TotalRuns() > match.getT1TotalRuns())){
					return match.getBattingSecond();
				}else if(match.getT2_2balls() <= 0){
					return 0;
				}
			} else if(match.getIsFollowon() > 0) {
				if((match.getT1_2balls() <= 0 || match.getT1_2total()<= 0)) {
					if(match.getT2_2wickets() >= match.getT2TotalWickets() && match.getT1total() > match.getT2total()){
						return match.getBattingFirst();
					}else {
						return 0;
					}
				}else if((match.getT1_2balls() > 0 || match.getT1_2total() > 0)){
					if(match.getT1total() > match.getT2total()) {
						return match.getBattingFirst();
					}else if(match.getT1_2wickets() >= match.getT1TotalWickets() && match.getT1total() < match.getT2total() ){
						return match.getBattingSecond();
					}else {
						return 0;
					}
				}
			}
			return 0;
		}else {
			

			// check for IsDl, if its true...
			// get the newTotalScore (DL target for that match with innings 1 as t1total)
			int t1Total = match.getT1total();
			if(match.isDls() && match.getT2Target() > 0) {
				t1Total = match.getT2Target() -1;
			}
			if ((t1Total) > (match.getT2total())) {
				return match.getBattingFirst();
			} else if  ((t1Total) < (match.getT2total())) {
				return ((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne());
			} else {
				return 0;
			}
		}
		
	}
	
	public static int calculateWinnerFB(MatchDto match) {
		
		if(match.getT1total()>match.getT2total()) {
			return match.getTeamOne();
		}else if(match.getT2total()>match.getT1total()) {
			return match.getTeamTwo();
		}
		return 0;
	}


	private static Map<String, List<FixtureDto>> filterByClub(List<FixtureDto> fixtures) {
		Map<String, List<FixtureDto>> map = new LinkedHashMap<String, List<FixtureDto>>();
		
		for(FixtureDto fx : fixtures) {
			List<FixtureDto> fxList = map.get(fx.getClubId()+"-"+fx.getClubName()) == null ? new ArrayList<FixtureDto>() :  map.get(fx.getClubId()+"-"+fx.getClubName());
			fxList.add(fx);
			map.put(fx.getClubId()+"-"+fx.getClubName(), fxList);
		}
			return map;	
	}

	

	private static Map<String, List<FixtureDto>> filterByWeek(List<FixtureDto> fixtures) {
		Map<String, List<FixtureDto>> map = new LinkedHashMap<String, List<FixtureDto>>();
		String mapKey = null;
		for(FixtureDto fx : fixtures) {
			mapKey = getMapKey(fx.getDate());
			if(mapKey != null && !mapKey.isEmpty()) {
				List<FixtureDto> fxList = map.get(mapKey) == null ? new ArrayList<FixtureDto>() :  map.get(mapKey);
				fxList.add(fx);
				map.put(mapKey, fxList);
			}
		}
		return map;
	}

	private static String getMapKey(String input) {
		String format = "MM/dd/yyyy";
		String outputFormat = "dd-MMM-yy";
		int weekStart = Calendar.MONDAY;
		int weekOfTheYear;
		int year = new Date().getYear();
		try {
			weekOfTheYear = getWeekOfTheYear(weekStart, input, format);
			year = getYear(input, format);
		} catch (ParseException e) {
			return null;
		}
	
		String weekStartEndDate = weekOfTheYear + "-"+getStartEndDateString(weekOfTheYear, weekStart,outputFormat , year);
		return weekStartEndDate;
	}

	private static int getWeekOfTheYear(int weekStart, String input, String format ) throws ParseException {
		Calendar calender = Calendar.getInstance();
		calender.setFirstDayOfWeek(weekStart);
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(input);
		calender.setTime(date);
		int weekOfTheYear = calender.get(Calendar.WEEK_OF_YEAR);
		return weekOfTheYear;
	}

	private static int getYear(String input, String format ) throws ParseException {
		Calendar calender = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(input);
		calender.setTime(date);
		int year = calender.get(Calendar.YEAR);
		return year;
	}
	
	private static String getStartEndDateString(int weekNumber, int weekStart, String dateformate, int year) {
		String weekDates;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNumber);
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		cal.set(Calendar.DAY_OF_WEEK, weekStart);
		weekDates = formatter.format(cal.getTime());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		formatter = new SimpleDateFormat("MMM dd, yyyy");
		weekDates = weekDates + " to "+ formatter.format(cal.getTime());
		return weekDates;
	}
	
	public static Map<String, List<MatchDto>> getMatcehsByFilterType(List<MatchDto> matches, String filterType) {
		if("Weekly".equalsIgnoreCase(filterType)) {
			return filterMatchByWeek(matches);
		}else if("Club".equalsIgnoreCase(filterType)) {
			return  filterByMatchClub(matches);
		}else {
			Map<String, List<MatchDto>> map = new HashMap<String, List<MatchDto>>();
			map.put("All Fixtures", matches);
			return map;
		}
	}



	public static Map<String, List<FixtureDto>> getFixtureByFilterType(List<FixtureDto> fixtures, String filterType) {
		if("Weekly".equalsIgnoreCase(filterType)) {
			return filterByWeek(fixtures);
		}else if("Club".equalsIgnoreCase(filterType)) {
			return  filterByClub(fixtures);
		}else {
			Map<String, List<FixtureDto>> map = new HashMap<String, List<FixtureDto>>();
			map.put("All Fixtures", fixtures);
			return map;
		}
	}
	private static Map<String, List<MatchDto>> filterByMatchClub(List<MatchDto> matches) {
		Map<String, List<MatchDto>> map = new LinkedHashMap<String, List<MatchDto>>();
		
		for(MatchDto mtch : matches) {
			List<MatchDto> mtchList = map.get(mtch.getClubID()+"-"+mtch.getClubName());
			if( mtchList == null) {
				mtchList = new ArrayList<MatchDto>();
				map.put(mtch.getClubID()+"-"+mtch.getClubName(), mtchList);
			}
			mtchList.add(mtch);
		}
			return map;	
	}


	private static Map<String, List<MatchDto>> filterMatchByWeek(List<MatchDto> matches) {
		Map<String, List<MatchDto>> map = new LinkedHashMap<String, List<MatchDto>>();
		String mapKey = null;
		for(MatchDto mtch : matches) {
			mapKey = getMapKey(mtch.getMatchDate());
			if(mapKey != null && !mapKey.isEmpty()) {
				List<MatchDto> mtchList = map.get(mapKey);
				
				if(map.get(mapKey) == null) {
					mtchList = new ArrayList<MatchDto>();
					map.put(mapKey, mtchList);
				}
				mtchList.add(mtch);
				
			}
		}
		return map;
	}


	public static void mapFixturesWithPlayerAvailability(List<FixtureDto> fixtures) throws Exception {
		Map<Integer, List<FixtureDto>> fixMapByClub = new HashMap<Integer, List<FixtureDto>>();
		List<FixtureDto> byClubList = null;
		if(!CommonUtility.isListNullEmpty(fixtures)) {
			
			for(FixtureDto fx : fixtures) {
				byClubList = fixMapByClub.get(fx.getClubId());
				if(byClubList == null) {
					byClubList = new ArrayList<FixtureDto>();
					fixMapByClub.put(fx.getClubId(), byClubList);
				}
				byClubList.add(fx);
			}
		}
		
		List<CountOfPlayerStatus> countOfPlayerStatus = null;
		if (fixMapByClub != null && !fixMapByClub.isEmpty()) {
			countOfPlayerStatus = FixturesFactory.getPlayerStatusData(fixMapByClub);
		}
		
		if (countOfPlayerStatus != null && !countOfPlayerStatus.isEmpty()) {
			for (FixtureDto fx : fixtures) {
				CountOfPlayerStatus cops = new CountOfPlayerStatus(fx.getClubId(), fx.getFixtureId(),
						fx.getPlayerTeam(), 0, 0, 0);
				if (countOfPlayerStatus.contains(cops)) {
					cops = countOfPlayerStatus.get(countOfPlayerStatus.indexOf(cops));
					fx.setCountOfPlayerStatus(cops);
				}
			} 
		}
	}
	
	public static List<String> getListOfTokenForOver(Map<String, String> tokensMap, MatchDto match) {
		double overDone = 0d;
		if(match.isFourthInningsStarted()) {
			overDone = CommonUtility.stringToDouble( match.getT2_2overs());
		}else if(match.isThirdInningsStarted()) {
			overDone = CommonUtility.stringToDouble( match.getT1_2overs());
		}else if(match.isSecondInningsStarted()) {
			overDone = CommonUtility.stringToDouble( match.getT2overs());
		}else if(match.isFirstInningsStarted()) {
			overDone = CommonUtility.stringToDouble( match.getT1overs());
		}
		List<String> tokens = new ArrayList<String>();
		if(overDone >= 1) {
			for(Map.Entry<String, String> ent : tokensMap.entrySet()) {
				String overVlaue = ent.getValue();
				overVlaue = overVlaue.replaceAll("OVERS", "");
				overVlaue = overVlaue.replaceAll("OVER", "");
				overVlaue = overVlaue.replaceAll("_", "");
				double modulus = overDone % CommonUtility.stringToInt(overVlaue);
				
				if(modulus == 0) {
					tokens.add(ent.getKey());
				}
			}
		}
		return tokens;
	}
	
	public static List<String> getListOfTokenForOverNotifications(Map<String, String> tokensMap, int overNum) {
		List<String> tokens = new ArrayList<String>();
		if(overNum >= 1) {
			for(Map.Entry<String, String> ent : tokensMap.entrySet()) {
				String overVlaue = ent.getValue();
				overVlaue = overVlaue.replaceAll("OVERS", "");
				overVlaue = overVlaue.replaceAll("OVER", "");
				overVlaue = overVlaue.replaceAll("_", "");
				double modulus = overNum % CommonUtility.stringToInt(overVlaue);
				
				if(modulus == 0) {
					tokens.add(ent.getKey());
				}
			}
		}
		return tokens;
	}
	
}
