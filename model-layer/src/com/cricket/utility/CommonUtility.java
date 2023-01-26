package com.cricket.utility;

import static com.cricket.utility.ApplicationConstants.CDN_SERVER_PATH_KEY;
import static com.cricket.utility.ApplicationConstants.DB_TIMEZONE_KEY;
import static com.cricket.utility.ApplicationConstants.DOC_REPO_PATH_KEY;
import static com.cricket.utility.ApplicationConstants.DOC_REP_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.configuration.EnviromentProperties;
import com.cricket.dao.CareerStatsSummaryFactory;
import com.cricket.dao.ClubFactory;
import com.cricket.dao.FantasyPlayerFactory;
import com.cricket.dao.FantasyTeamPlayerFactory;
import com.cricket.dao.FantasyTeamsFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.MatchDLRecordsFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.NewsFactory;
import com.cricket.dao.PlayerStatisticsFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dao.TopPerformersSummaryFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dlscalculation.DLSCalculation;
import com.cricket.dlscalculation.DLSInputData;
import com.cricket.dlscalculation.MatchDLRecord;
import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.FantasyPlayerDto;
import com.cricket.dto.FantasyTeamPlayerDto;
import com.cricket.dto.FantasyTeamsDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.NewsDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.Ticket;
import com.cricket.dto.UserDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.ClubSearchDto;
import com.cricket.dto.lite.LeagueLite;
import com.cricket.dto.statistics.PlayerStatisticSummaryDto;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.scheduler.CalendarUtil;
import com.cricket.scheduler.FixtureMetric;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class CommonUtility {
	private static final Logger log = LoggerFactory.getLogger(CommonUtility.class);
	public static final String FAVOURITE_CLUB_COOCKIE_NAME = "favouriteClub-";
	public static final String RECENTLY_VIEWED_COOCKIE_NAME = "recentlyViewed";
	public static final String DOCUMENTS_REP_WIN = "C:\\documentsRep";
	public static final String DOCUMENTS_REP_LINUX = "/home/nganeshreddi/documentsRep";
	private static Pattern pattern;
	private static Pattern timePattern;
	private static Map<String, String> countryMap = new LinkedHashMap<String, String>();
	private static Map<String, String> purpose = new LinkedHashMap<String, String>();
	private static Map<String, String> matchType = new LinkedHashMap<String, String>();
	private static Map<String, String> type = new LinkedHashMap<String, String>();
    private static final char[] PASSWORD = "!cricEncryptionclubs!".toCharArray();
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
	private static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat timestamp_formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static final String TIME_PATTERN="(1[012]|[1-9]):[0-5][0-9](\\s)(AM|PM|am|pm)";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String LOGGED_IN_USER_COOCKIE = "loggedInUserCoockie";
	private static String[] daysOfWeek = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	private static String FIXTURE_DATE_FORMATE = "MM/dd/yyyy hh:mm aa";

	public static List<String> listOfAllSeriesTypes = Arrays.asList("Twenty20","One Day","Ten10","Test","Youth","Women","2X");	
	public static boolean isValidateSeriesType(String seriesType){
		if(CommonUtility.listOfAllSeriesTypes.contains(seriesType) || CommonUtility.isNullOrEmpty(seriesType) )return true;
		return false;
	}	

	private static String db_timezone = "CST";
	
	static {
		pattern = Pattern.compile(EMAIL_PATTERN);
		timePattern = Pattern.compile(TIME_PATTERN);
		String dbTimeZone = EnviromentProperties.getEnvProperty(DB_TIMEZONE_KEY);
		if(!isNullOrEmpty(dbTimeZone)) {
			db_timezone = dbTimeZone;
		}
		
	}
	
	
	public static boolean isTnCRequired(ClubDtoLite club, UserDto loggedInUser, PlayerDto player){
		
		if(!CommonUtility.isNullOrEmpty(club.getPlayerTerms()) && 
				(player != null && player.getPlayerID() >0)){
			return true;
		}
		return false;
	}

	public static String getYouTubeLink(String link){
		if(link!=null ){
			try {
				String[] videoId = link.split("v=");
				return "//www.youtube.com/embed/"+videoId[1]+"?rel=0&autoplay=1&mute=1";
			} catch (Exception e) {
				// log.error(e.getMessage(),e);
			}
		}
		return "//www.youtube.com/embed/iVZJiaFZrQM?rel=0&autoplay=1";
	}
	
	public static boolean getBooleanfromObject(Object obj){
		boolean bol = false;
		if(obj != null){
			try {
				if(Boolean.valueOf((String) obj)){
					return true;
				}
			} catch (Exception e) {
				//log.error(e.getMessage(),e);
			}
		}
		return bol;
	}
	
	public static boolean isPlayingWithEatchOther(TeamDto curTeam, TeamDto opnTeam, List<FixtureDto> fixtures){
		
		boolean isPlaying = false;
		
		for(FixtureDto fx : fixtures){
			if((fx.getTeamOne() == curTeam.getTeamID() && fx.getTeamTwo() == opnTeam.getTeamID()) || 
					fx.getTeamOne() == opnTeam.getTeamID() && fx.getTeamTwo() == curTeam.getTeamID()){
				isPlaying = true;
				break;
			}
		}
		
		return isPlaying;
	}
	
	public static Map<TeamDto, FixtureMetric> generateFixtureMetricMap(List<TeamDto> teams, List<FixtureDto> fixtures) {
		Map<TeamDto, FixtureMetric> map = new LinkedHashMap<TeamDto, FixtureMetric>();
		Integer dayOfWeek = 0;
		Integer countForDay = 0;
		
		for(TeamDto team : teams){
			for(FixtureDto fx : fixtures){
				
				if(fx.getTeamTwo() == team.getTeamID() || fx.getTeamOne() == team.getTeamID()){
					FixtureMetric fixtureMetric = map.get(team)  == null?new FixtureMetric(): map.get(team);
					fixtureMetric.setTotalMatch(fixtureMetric.getTotalMatch() + 1);
					if(team.getGroundId() > 0 && fx.getGroundId() > 0){
						if(fx.getGroundId() == team.getGroundId()){
							fixtureMetric.setHomeMatch(fixtureMetric.getHomeMatch()+1);
						}else{
							fixtureMetric.setAwayMatch(fixtureMetric.getAwayMatch()+1);
						}
					}
					Map<Integer, Integer> matchForDay = fixtureMetric.getMatchForDay() == null ? new HashMap<Integer, Integer>() : fixtureMetric.getMatchForDay();
					try {
						dayOfWeek = CalendarUtil.dayOfWeek(CalendarUtil.getDateFromString(fx.getDate(), "MM/dd/yyyy"));
						countForDay = matchForDay.get(dayOfWeek) == null ? 0:matchForDay.get(dayOfWeek);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					matchForDay.put(dayOfWeek, ++countForDay);
					fixtureMetric.setMatchForDay(matchForDay);
					map.put(team, fixtureMetric);
				}
			}
		}
		
		return map;
	}
	
	public static String escapeQuotes(String s) throws Exception {
		String retvalue = s;
		if (s != null && (s.contains("'") || s.contains("\"")) ) {
			if(s.contains("'")) {
				retvalue =  s.replaceAll("'", "\\\\\'"); //for Javascript purpose adding 
			}
			if(s.contains("\"")) {
				retvalue =  s.replaceAll("\"", ""); // ignored for delete purpose 
			}
		}
		return retvalue == null ? "" : retvalue;
	}
	
	
	public static String escapeStringQuotes(String s) throws Exception {
		String retvalue = s;
		if (s != null && (s.contains("'") && s.indexOf("'") != -1) ) {
			if(s.contains("'")) {
				retvalue =  s.replaceAll("'", "\\\\\'");
			}
		}
		if (s != null && s.indexOf('"') != -1) {
				StringBuffer hold = new StringBuffer();
				char c;
				for (int i = 0; i < s.length(); i++) {
				if ((c = s.charAt(i)) == '"') {
				hold.append("");
				} else {
				hold.append(c);
				}
				}
				retvalue = hold.toString();
				}
				return retvalue == null ? "" : retvalue;
	}
	
	public static String hideSingleQuotes(String s) throws Exception {
		String retvalue = s;
		if (!CommonUtility.isNullOrEmptyOrNULL(s) && (s.contains("\'")) ) {	
				retvalue =  s.replaceAll("\'", ""); // ignored and delete 
		}
		return CommonUtility.isNullOrEmptyOrNULL(retvalue) ? "" : retvalue.trim();
	}

	
	
	public static int getMatchCountForDay(Map.Entry<TeamDto, FixtureMetric> entry, int day){
		if(entry == null || entry.getValue() == null 
				|| entry.getValue().getMatchForDay() == null 
				||  entry.getValue().getMatchForDay().get(day) == null){
			return 0;
		}
			
		return entry.getValue().getMatchForDay().get(day);
	}
	
	public static Map<String, Integer> generateFixtureDateMetric( List<FixtureDto> fixtures) {

		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(FixtureDto fixture : fixtures){
			String date = fixture.getDate();
			Integer numberOfMatch = map.get(date) == null ? 0 : map.get(date);
			map.put(date, ++numberOfMatch);
		}
		return map;
	}
	public static Map<String, Integer> generateFixtureDateGroundMetric( List<FixtureDto> fixtures) {

		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		List<String> dateGroundStr = new ArrayList<String>();
		for(FixtureDto fixture : fixtures){
			String date = fixture.getDate();
			Integer numberOfGrounds= map.get(date) == null ? 0 : map.get(date);
			if(!dateGroundStr.contains(date+fixture.getGroundId())) {
				dateGroundStr.add(date+fixture.getGroundId());
				map.put(date, ++numberOfGrounds);
			}
		}
		return map;
	}
	
	private static String SplitTeamName(String teamName) {
		StringBuilder result = new StringBuilder();

		String[] words = teamName.split(" ");
		int counter = 0;
		for (String word : words) {
			if (!word.equals("XI") && !word.equals("11") && !word.equals("-") && word.length() > 1) {
				String firstLetter = word.substring(0, 1);
				String tempWord = word.substring(1, word.length());
				tempWord = tempWord.replaceAll("[AEIOU 0-9]", "");
				result.append(firstLetter);
				result.append(tempWord);
				if (counter < words.length) {
					result.append(" ");
				}
				counter++;
			}
		}
		return ((result.toString()).trim());
	}
	
	public static int longToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        return 0;
	    }
	    return (int) l;
	}
	
public static String trimTeamName(String teamName, String teamCode) {
		
		
		if (!CommonUtility.isNullOrEmpty(teamCode)) {
			return teamCode.toUpperCase();
		} 
				

		teamName = teamName.toUpperCase();
		
		teamName = teamName.replaceAll("[^A-Za-z0-9]"," ");

		StringBuilder result = new StringBuilder();

		if (teamName.length() <= 3) {
			result.append(teamName);
		} else {
			String spTeamName = SplitTeamName(teamName);
			if (spTeamName.length() < 3) {
				result.append(teamName.substring(0, 3));
			} else if ((spTeamName.replaceAll(" ", "")).length() <= 3) {
				result.append(spTeamName.replaceAll(" ", ""));
			} else {
				String[] words = spTeamName.split(" ");
				String word1 = words[0];
				String firstLetter = word1.substring(0, 1);
				if (words.length == 1) {
					String tempWord1 = word1.substring(1, 3);
					result.append(firstLetter);
					result.append(tempWord1);
				} else if (words.length == 2) {
					String word2 = words[1];
					String firstLetter2;
					if (word2.length() < 2) {
						firstLetter2 = word2.substring(0, 1);
						firstLetter = word1.substring(0, 2);
					} else {
						firstLetter2 = word2.substring(0, 2);
					}
					result.append(firstLetter);
					result.append(firstLetter2);
				} else {
					String tempWord2 = words[1];
					tempWord2 = tempWord2.substring(0, 1);
					String tempWord3 = words[2];
					tempWord3 = tempWord3.substring(0, 1);
					result.append(firstLetter);
					result.append(tempWord2);
					result.append(tempWord3);
				}
			}
		}
		return result.toString();
	}

	public static String getDisplayName(String fullName, int length) {
		String playerDisplayName = toDisplayCase(fullName);
		if (fullName.length() > length) {			
			playerDisplayName = fullName.substring(0, length) + "..";
		} else {			
			playerDisplayName = fullName;
		}
		return playerDisplayName;
	}

	public static String trimString(String string, int limit) {
		if (!isNullOrEmpty(string)) {			
			return string.length() <= limit ? string : string.substring(0, limit);
		}
		return "";
	}
	
	public static String getCustomIdWithName(PlayerDto player,int clubId,int length) {
		String custId = "";
		if(player != null && clubId == 7683 && player.getSrcPlayerId() != null ) {
			
			int r = CommonUtility.stringToInt(player.getSrcPlayerId());
			custId = String.format("%05d", r ); 
			if(player.getFirstName().length() >= 4) {
				custId = player.getFirstName().substring(0, 4)+ custId;
			}else {
				custId = player.getFirstName() +custId; 				
			}
		}
		return custId;
	}

	public static int stringToInt(String value) {
		int i = 0;
		if (value != null && !value.equals("")) {
			try {
				i = Integer.parseInt(value.trim());
			} catch (NumberFormatException nFE) {
				i = 0;
			}
		}
		return i;
	}
	
	public static double stringToDouble(String value) {
		double i = 0;
		if (value != null && !value.equals("")) {
			try {
				i = Double.parseDouble(value);
			} catch (NumberFormatException nFE) {
				i = 0;
			}
		}
		return i;
	}
	
	public static float stringToFloat(String value) {
		float i = 0;
		if (value != null && !value.equals("")) {
			try {
				i = Float.parseFloat(value);
			} catch (NumberFormatException nFE) {
				i = 0;
			}
		}
		return i;
	}

	public static String ballsToOvers(int balls) {
		String overs = "0.0";
		if (balls != 0) {
			overs = "" + (int) balls / 6;
			overs += "." + (balls % 6);
		}
		return overs;
	}
	
	public static String ballsToOvers(int balls, int ballNum) {
		String overs = "0.0";
		if (balls != 0) {
			overs = "" + (int) balls / ballNum;
			overs += "." + (balls % ballNum);
		}
		return overs;
	}
	public static String Round(double Rval, int Rpl) {
		String precision = "0.";
		for (int i = 0; i < Rpl; i++) {
			precision += "0";
		}
		DecimalFormat twoDForm = new DecimalFormat(precision);
		return String.valueOf(twoDForm.format(Rval));
	}

	public static long stringToLong(String value) {
		long i = 0;
		if (value != null && !value.equals("")) {
			try {
				i = Long.parseLong(value);
			} catch (NumberFormatException nFE) {
				i = 0;
			}
		}
		return i;
	}

	public static String dateToString(Date date) {
		if (date == null)
			return "";
		return formatter.format(date);
	}
	
	public static String dateToStringFormatter(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return simpleDateFormat.format(date);
	}
	
	public static String dateToString(Date date, String dateFormateReq, String endTimeZone) {
		if (date == null)
			return "";

		try {
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			String str = sdf1.format(date);
			String dbTimeZone = EnviromentProperties.getEnvProperty(DB_TIMEZONE_KEY);
			
			sdf1.setTimeZone(TimeZone.getTimeZone(dbTimeZone));
			Date date1 = sdf1.parse(str);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormateReq);

			// Here you say to java the initial timezone. This is the secret
			sdf.setTimeZone(TimeZone.getTimeZone(endTimeZone));
			// Will print in UTC

			// Here you set to your timezone
			//sdf.setTimeZone(TimeZone.getDefault());
			// Will print on your default Timezone
			String dateOutput = sdf.format(calendar.getTime());
			return dateOutput;
		} catch (ParseException e) {
			
		}
		return "";
	}
	
	public static String dateToStringByTimeZone(String dateStr, String dateFormat, String clubDateFormat, 
			String clubTimeZone, String clubTimeFormat) {

		if (dateStr == null)
			return "";
		try {
			SimpleDateFormat sdfDB = new SimpleDateFormat(dateFormat);
			String dbTimeZone = EnviromentProperties.getEnvProperty(DB_TIMEZONE_KEY);
			sdfDB.setTimeZone(TimeZone.getTimeZone(dbTimeZone));
			Date dateDB = sdfDB.parse(dateStr);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateDB);
			SimpleDateFormat sdfOut = null;
			if ("24".equalsIgnoreCase(clubTimeFormat)) {
				sdfOut = new SimpleDateFormat(clubDateFormat + " HH:mm:ss");
			} else {
				sdfOut = new SimpleDateFormat(clubDateFormat + " hh:mm aa");
			}
			sdfOut.setTimeZone(TimeZone.getTimeZone(clubTimeZone));
			return sdfOut.format(calendar.getTime());

		} catch (Exception e) {
			System.out.println();
		}
		return "";
	}
	
	public static String dateToString_HHMM_Format(Date date, String dateFormateReq) {
		if (date == null)
			return "";

		try {
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			String str = sdf1.format(date);
			
			Date date1 = sdf1.parse(str);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormateReq);

			String dateOutput = sdf.format(calendar.getTime());
			return dateOutput;
		} catch (ParseException e) {
			
		}
		return "";
	}
	
	
	public static String timestampToString(Date date, String formate, String endTimeZone) {
		if (date == null)
			return "";
		SimpleDateFormat timestamp_formatter = new SimpleDateFormat(formate+" HH:mm:ss");
		return timestamp_formatter.format(date);
	}

	public static Date stringToDate(String date) throws ParseException {
		if (isNullOrEmpty(date))
			return null;
		try {
			return formatter.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String stringToStringDate(String strDate, String formate) {
		if (isNullOrEmpty(strDate))
			return null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dayMonth = "";
			try {
				Date date = null;
				date = formatter.parse(strDate);
				formatter.applyPattern(formate);
				dayMonth = formatter.format(date);

			} catch (Exception e) {
				return strDate;
			}
			return dayMonth;
		
		} catch (Exception e) {
			return null;
		}
	}

	public static Date stringToDateByFormate(String date, SimpleDateFormat clubDateFormator) throws ParseException {
		if (isNullOrEmpty(date))
			return null;
		return clubDateFormator.parse(date);
	}
	
	public static boolean isNullOrEmpty(String value) {
		return ((value == null) || "".equals(value.trim()) || "null".equals(value.trim()));
	}
	
	public static String addPlayerLink(String playerName, String playerId, int clubId) {
		if (playerName != null && stringToInt(playerId) > 0) {
			return "<a href='viewPlayer.do?playerId=" + playerId + "&clubId=" + clubId + "'>" + playerName + "</a>";
		} else if (playerName != null) {
			return playerName;
		}
		return "";
	}

	public static String restoreHTMLSafeString(String value) {
		if (value != null && !"".equals(value)) {
			value = value.replaceAll("%20", " ");
			value = value.replaceAll("%28", "(");
			value = value.replaceAll("%29", ")");
		}
		return value;
	}

	public static String getDocumentsRepPath() {
		return EnviromentProperties.getEnvProperty(DOC_REPO_PATH_KEY);
	}
	
	public static String getDocRepPath() {
		return EnviromentProperties.getEnvProperty(DOC_REP_PATH);
	}

	public static String numberToAlphabet(int number) {
		char c = (char) (number + 64);
		return Character.toString(c);
	}

	public static String removeHTMLTags(String str) {
		if (str != null) {
			String plainString = str.replaceAll("\\<.*?>", "");
			return plainString.trim();
		}
		return "";
	}



	public static int oversToBalls(String overs) {
		if (isNullOrEmpty(overs)) {
			return 0;
		}
		if (isInteger(overs)) {
			return Integer.parseInt(overs) * 6;
		} else if (overs.contains(".")) {
			String[] overArray = overs.split("\\.");
			return (Integer.parseInt(overArray[0]) * 6) + Integer.parseInt(overArray[1]);
		}
		return 0;
	}

	public static int oversToBalls(String overs, int ballsInOver) {
		if (isNullOrEmpty(overs)) {
			return 0;
		}
		if (isInteger(overs)) {
			return Integer.parseInt(overs) * ballsInOver;
		} else if (overs.contains(".")) {
			String[] overArray = overs.split("\\.");
			return (Integer.parseInt(overArray[0]) * ballsInOver) + Integer.parseInt(overArray[1]);
		}
		return 0;
	}
	
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}

	public static int numberofWordsInString(String string) {
		if (isNullOrEmpty(string))
			return 0;
		String trim = string.trim();
		return trim.split("\\s+").length; // separate string around spaces
	}

	public static boolean validateEmail(String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public static boolean validateTime(String time) {
				
		Matcher matcher = timePattern.matcher(time);
		if (!matcher.matches()) {
			String pattren2 = "([0-12][1-9]):[0-5][0-9](\\s)?(?i)(am|pm|PM|AM)";
			Pattern timePatternNew = Pattern.compile(pattren2);
			matcher = timePatternNew.matcher(time);
			return matcher.matches();
		}
		return matcher.matches();
	}

	public static void addUniqueIntegerToList(List<Integer> list, int playerID) {
		if (playerID > 0) {
			for (Integer number : list) {
				if (number == playerID) {
					return;
				}
			}
			list.add(playerID);
		}
	}

	public static String getPassword(int n) {
		char[] pw = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			pw[i] = (char) c;
		}
		return new String(pw);
	}

	public static String getRandomNumber(int n) {
		char[] pw = new char[n];
		for (int i = 0; i < n; i++) {
			pw[i] = (char) ('0' + (int) (Math.random() * 10));
		}
		return new String(pw);
	}

	public static int calculateAge(String dateString) {

		Date dateOfBirth = null;
		int age = 0 ;
		try {
			dateOfBirth = formatter.parse(dateString);

			Calendar dob = Calendar.getInstance();
			dob.setTime(dateOfBirth);
			Calendar today = Calendar.getInstance();
			age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
				age--;
			} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
					&& today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
				age--;
			}
		} catch (Exception e) {
			return 0;
		}
		return age;
	}
	
	public static int calculateAgeByYear(String dateString) {

		Date dateOfBirth = null;

		try {			
			dateOfBirth = new SimpleDateFormat("yyyy").parse(dateString);
		} catch (ParseException e) {
			return 0;
		}
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateOfBirth);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		
		return age;
	}

	public static String getDayMonth(String strDate) {
		String dayMonth = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = null;
			date = formatter.parse(strDate);
			formatter.applyPattern("dd MMM");
			dayMonth = formatter.format(date);

		} catch (ParseException e) {
			return strDate;
		}
		return dayMonth;
	}
	
	public static String getDateToStringFormated(Date date, String format) {
		String dayMonth = "";
		try {
			if(date != null){
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				dayMonth = formatter.format(date);
			}
		} catch (Exception e) {
		}
		return dayMonth;
	}
	
	public static String getMonthYear(Date date) {
		String dayMonth = "";
		if(date != null){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			formatter.applyPattern("MMM yyyy");
			dayMonth = formatter.format(date);
		}
		return dayMonth;
	}
	
	public static String getMonthDateYear(Date date) {
		String dayMonth = "";
		if(date != null){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			formatter.applyPattern("MMM dd, yyyy");
			dayMonth = formatter.format(date);
		}
		return dayMonth;
	}

	public static String calculateRunRate(int runs, double overs) {
		if (overs > 0 && runs >= 0) {
			return CommonUtility.Round((double) runs / overs, 2);
		}
		return "--.--";
	}
	
	public static String calculateRPB(int runs, int balls) {
		if (balls > 0 && runs >= 0) {
			return CommonUtility.Round((double) runs / balls, 2);
		}
		return "--.--";
	}

	public static String toDisplayCase(String s) {

		final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the
														// character following
														// to be capitalized

		StringBuilder sb = new StringBuilder();
		boolean capNext = true;

		for (char c : s.toCharArray()) {
			c = (capNext)
					? Character.toUpperCase(c)
					: Character.toLowerCase(c);
			sb.append(c);
			capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit
																		// cast
																		// not
																		// needed
		}
		return sb.toString();
	}

	public static String getClubBasedURL(String url, ClubDtoLite club, boolean continueEmbedView) {

		return getClubBasedURL(url, club, continueEmbedView, new HashMap<String, String>());
	}

	public static String getClubBasedURL(String url, ClubDtoLite club, boolean continueEmbedView, Map<String, String> paramMap) {

		if (paramMap == null)
			paramMap = new HashMap<String, String>();

		String clubURL = "#";
		if (url != null && !"".equals(url)) {
			if (url.equals("home.do")) {
				clubURL = "/" + club.getShortURL();
			} else {
				boolean containsParam = url.contains("?");
				clubURL = "/" + club.getShortURL() + "/" + url
						+ (containsParam ? "&" : "?") + "clubId=" + club.getClubId();
				if (continueEmbedView) {
					containsParam = clubURL.contains("?");
					clubURL += (containsParam ? "&" : "?") + "continueEmbed=true&embedView=true";
				}

				for (Map.Entry<String, String> entry : paramMap.entrySet()) {

					String key = entry.getKey();
					String value = entry.getValue();

					if (new Boolean(key))
						clubURL += "&" + key + "=" + new Boolean(value).booleanValue();
				}
			}
		}

		return clubURL;
	}

//	public static String getClubBasedURLForEmail(String url, ClubDto club) {
//		return getClubBasedURLForEmail(url, club, null);
//	}

	public static String getClubBasedURLForEmail(String url, ClubDtoLite club, String serverName) {
		String clubURL = "#";
		if (url != null && !"".equals(url)) {
			if (url.equals("home.do")) {
				clubURL = "/" + club.getShortURL() + "?source=email";
			} else if(url.equals("activateClub.do")){
				String token = CommonUtility.getTokenForUrl(club.getClubId()+"" );
				clubURL = "/#!/activateclub/" +club.getClubId() + "/" +(token == null?club.getShortURL():token) ;
			}  else if(url.equals("activateLeague.do")){
				String token = CommonUtility.getTokenForUrl(club.getClubId()+"" );
				clubURL = "/activateLeague.do?clubId=" +club.getClubId() + "&token=" +(token == null?club.getShortURL():token) ;
			} else {
				boolean containsParam = url.contains("?");
				clubURL = "/" + club.getShortURL() + "/" + url
						+ (containsParam ? "&" : "?") + "clubId=" + club.getClubId() + "&source=email";
			}
		}
		if(serverName != null){
			serverName = serverName.replaceAll(".api", "");
		}
		
		return "http://" + (serverName == null ? "cricclubs.com" : serverName) + clubURL;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getLimitedRecordsFromList(List list, int limit) {
		List newList = new ArrayList();

		if (list != null && !list.isEmpty() && limit > 0 && limit < list.size()) {
			for (int i = 0; i < limit; i++) {
				newList.add(list.get(i));
			}
		} else {
			return list;
		}

		return newList;
	}

	public static String applyParametersToTemplateFile(String folder, String templatename, Map<String, String> parameters) throws Exception {
		String returnString = "";
		StringBuilder body = new StringBuilder("");
		File file = new File(CommonUtility.getDocumentsRepPath() + File.separator + folder + File.separator + templatename);
		
		// Scanner reader1 = new Scanner(file);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			String str = null;
			str = reader.readLine();

			while (str != null) {
				body.append(str + "\n");
				str = reader.readLine();
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		returnString = body.toString();

		Set<String> keys = parameters.keySet();
		for (Iterator<String> itr = keys.iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			String value = (String) parameters.get(key);
			returnString = returnString.replaceAll(key, value);

		}

		return returnString;
	}

	public static String getClubBasedURL(String string, ClubDtoLite club) {
		return getClubBasedURL(string, club, false);
	}

	public static boolean validateCaptcha(String response, String remoteip)
	{
		String secret = "6Lc8JAoTAAAAAMCO3M4dpOHhtmpIJoHU-ZSLlqop";
		URLConnection connection = null;
		InputStream is = null;
		JsonReader rdr = null;
		String charset = "UTF-8";
		boolean googleResponse = false;

		String url = "https://www.google.com/recaptcha/api/siteverify";
		try {
			String query = String.format("secret=%s&response=%s&remoteip=%s",
					URLEncoder.encode(secret, charset),
					URLEncoder.encode(response, charset),
					URLEncoder.encode(remoteip, charset));

			connection = new URL(url + "?" + query).openConnection();
			is = connection.getInputStream();
			rdr = new JsonReader(new InputStreamReader(is));
			rdr.beginObject();
			while (rdr.hasNext()) {
				String name = rdr.nextName();
				if ("success".equals(name)) {
					googleResponse = rdr.nextBoolean();
				} else {
					rdr.skipValue();
			    }
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					//log.error(e.getMessage(),e);
				}

			}
			if (rdr != null) {
				try {
					rdr.close();
				} catch (IOException e) {
				}
			}
		}
		return googleResponse;
	}

	public static String getCDN() {
		return EnviromentProperties.getEnvProperty(CDN_SERVER_PATH_KEY);
	}

	public static String displayVerificationImage(String email) {
		return displayVerificationImage(email, false);
	}

	public static String displayVerificationImage(String email, boolean dontShowNotVerified) {
		if (isNullOrEmpty(email)) {
			if (dontShowNotVerified)
				return "";
			return "<img alt='Not Verified' title='Not Verified' src='" + getCDN() + "/images/question.png' style='width: 16px;height: 16px;margin: 0px;'> ";
		}
		return "<img alt='Verified' title='Verified' src='" + getCDN() + "/images/ok.png' style='width: 16px;height: 16px;margin: 0px;'>";
	}

	public static String displayTCImage(PlayerDto player, int clubId) {
		if (clubId == 64 || clubId == 17620) {
			if (!player.isAcceptedTerms()) {
				return "<img alt='Terms Not Accepted' title='Terms Not Accepted' src='" + getCDN() + "/images/not_accepted.png' style='width: 16px;height: 16px;margin: 0px;'> ";
			} else {
				return "<img alt='Terms Accepted' title='Terms Accepted' src='" + getCDN() + "/images/accepted.png' style='width: 16px;height: 16px;margin: 0px;'>";
			}
		}
		return "";
	}
	
	public static String displayTCImageInPlayerProfile(PlayerDto player, int clubId) {
		if (clubId == 64 || clubId == 17620) {
			if (!player.isAcceptedTerms()) {
				return "<img alt='Terms Not Accepted' title='Terms Not Accepted' src='" + getCDN() + "/images/not_accepted.png' style='height: 10px; width: 10px!important; '> ";
			} else {
				return "<img alt='Terms Accepted' title='Terms Accepted' src='" + getCDN() + "/images/accepted.png' style='height: 10px; width: 10px!important; '>";
			}
		}
		return "";
	}

	public static Map<String, String> getCountryMap() {
		if (countryMap.isEmpty()) {
			countryMap.put("USA", "United States");
			countryMap.put("IND", "India");
			countryMap.put("KWT", "Kuwait");
			countryMap.put("ARE", "United Arab Emirates");
			countryMap.put("GBR", "United Kingdom");
			countryMap.put("PAK", "Pakistan");
			countryMap.put("AFG", "Afghanistan");
			countryMap.put("ALA", "land Islands");
			countryMap.put("ALB", "Albania");
			countryMap.put("DZA", "Algeria");
			countryMap.put("ASM", "American Samoa");
			countryMap.put("AND", "Andorra");
			countryMap.put("AGO", "Angola");
			countryMap.put("AIA", "Anguilla");
			countryMap.put("ATA", "Antarctica");
			countryMap.put("ATG", "Antigua and Barbuda");
			countryMap.put("ARG", "Argentina");
			countryMap.put("ARM", "Armenia");
			countryMap.put("ABW", "Aruba");
			countryMap.put("AUS", "Australia");
			countryMap.put("AUT", "Austria");
			countryMap.put("AZE", "Azerbaijan");
			countryMap.put("BHS", "Bahamas");
			countryMap.put("BHR", "Bahrain");
			countryMap.put("BGD", "Bangladesh");
			countryMap.put("BRB", "Barbados");
			countryMap.put("BLR", "Belarus");
			countryMap.put("BEL", "Belgium");
			countryMap.put("BLZ", "Belize");
			countryMap.put("BEN", "Benin");
			countryMap.put("BMU", "Bermuda");
			countryMap.put("BTN", "Bhutan");
			countryMap.put("BOL", "Bolivia, Plurinational State of");
			countryMap.put("BES", "Bonaire, Sint Eustatius and Saba");
			countryMap.put("BIH", "Bosnia and Herzegovina");
			countryMap.put("BWA", "Botswana");
			countryMap.put("BVT", "Bouvet Island");
			countryMap.put("BRA", "Brazil");
			countryMap.put("IOT", "British Indian Ocean Territory");
			countryMap.put("BRN", "Brunei Darussalam");
			countryMap.put("BGR", "Bulgaria");
			countryMap.put("BFA", "Burkina Faso");
			countryMap.put("BDI", "Burundi");
			countryMap.put("KHM", "Cambodia");
			countryMap.put("CMR", "Cameroon");
			countryMap.put("CAN", "Canada");
			countryMap.put("CPV", "Cape Verde");
			countryMap.put("CYM", "Cayman Islands");
			countryMap.put("CAF", "Central African Republic");
			countryMap.put("TCD", "Chad");
			countryMap.put("CHL", "Chile");
			countryMap.put("CHN", "China");
			countryMap.put("CXR", "Christmas Island");
			countryMap.put("CCK", "Cocos (Keeling) Islands");
			countryMap.put("COL", "Colombia");
			countryMap.put("COM", "Comoros");
			countryMap.put("COG", "Congo");
			countryMap.put("COD", "Congo, the Democratic Republic of the");
			countryMap.put("COK", "Cook Islands");
			countryMap.put("CRI", "Costa Rica");
			countryMap.put("CIV", "C�te d'Ivoire");
			countryMap.put("HRV", "Croatia");
			countryMap.put("CUB", "Cuba");
			countryMap.put("CUW", "Cura�ao");
			countryMap.put("CYP", "Cyprus");
			countryMap.put("CZE", "Czech Republic");
			countryMap.put("DNK", "Denmark");
			countryMap.put("DJI", "Djibouti");
			countryMap.put("DMA", "Dominica");
			countryMap.put("DOM", "Dominican Republic");
			countryMap.put("ECU", "Ecuador");
			countryMap.put("EGY", "Egypt");
			countryMap.put("SLV", "El Salvador");
			countryMap.put("GNQ", "Equatorial Guinea");
			countryMap.put("ERI", "Eritrea");
			countryMap.put("EST", "Estonia");
			countryMap.put("ETH", "Ethiopia");
			countryMap.put("FLK", "Falkland Islands (Malvinas)");
			countryMap.put("FRO", "Faroe Islands");
			countryMap.put("FJI", "Fiji");
			countryMap.put("FIN", "Finland");
			countryMap.put("FRA", "France");
			countryMap.put("GUF", "French Guiana");
			countryMap.put("PYF", "French Polynesia");
			countryMap.put("ATF", "French Southern Territories");
			countryMap.put("GAB", "Gabon");
			countryMap.put("GMB", "Gambia");
			countryMap.put("GEO", "Georgia");
			countryMap.put("DEU", "Germany");
			countryMap.put("GHA", "Ghana");
			countryMap.put("GIB", "Gibraltar");
			countryMap.put("GRC", "Greece");
			countryMap.put("GRL", "Greenland");
			countryMap.put("GRD", "Grenada");
			countryMap.put("GLP", "Guadeloupe");
			countryMap.put("GUM", "Guam");
			countryMap.put("GTM", "Guatemala");
			countryMap.put("GGY", "Guernsey");
			countryMap.put("GIN", "Guinea");
			countryMap.put("GNB", "Guinea-Bissau");
			countryMap.put("GUY", "Guyana");
			countryMap.put("HTI", "Haiti");
			countryMap.put("HMD", "Heard Island and McDonald Islands");
			countryMap.put("VAT", "Holy See (Vatican City State)");
			countryMap.put("HND", "Honduras");
			countryMap.put("HKG", "Hong Kong");
			countryMap.put("HUN", "Hungary");
			countryMap.put("ISL", "Iceland");
			countryMap.put("IDN", "Indonesia");
			countryMap.put("IRN", "Iran, Islamic Republic of");
			countryMap.put("IRQ", "Iraq");
			countryMap.put("IRL", "Ireland");
			countryMap.put("IMN", "Isle of Man");
			countryMap.put("ISR", "Israel");
			countryMap.put("ITA", "Italy");
			countryMap.put("JAM", "Jamaica");
			countryMap.put("JPN", "Japan");
			countryMap.put("JEY", "Jersey");
			countryMap.put("JOR", "Jordan");
			countryMap.put("KAZ", "Kazakhstan");
			countryMap.put("KEN", "Kenya");
			countryMap.put("KIR", "Kiribati");
			countryMap.put("PRK", "Korea, Democratic People's Republic of");
			countryMap.put("KOR", "Korea, Republic of");
			countryMap.put("KGZ", "Kyrgyzstan");
			countryMap.put("LAO", "Lao People's Democratic Republic");
			countryMap.put("LVA", "Latvia");
			countryMap.put("LBN", "Lebanon");
			countryMap.put("LSO", "Lesotho");
			countryMap.put("LBR", "Liberia");
			countryMap.put("LBY", "Libya");
			countryMap.put("LIE", "Liechtenstein");
			countryMap.put("LTU", "Lithuania");
			countryMap.put("LUX", "Luxembourg");
			countryMap.put("MAC", "Macao");
			countryMap.put("MKD", "Macedonia, the former Yugoslav Republic of");
			countryMap.put("MDG", "Madagascar");
			countryMap.put("MWI", "Malawi");
			countryMap.put("MYS", "Malaysia");
			countryMap.put("MDV", "Maldives");
			countryMap.put("MLI", "Mali");
			countryMap.put("MLT", "Malta");
			countryMap.put("MHL", "Marshall Islands");
			countryMap.put("MTQ", "Martinique");
			countryMap.put("MRT", "Mauritania");
			countryMap.put("MUS", "Mauritius");
			countryMap.put("MYT", "Mayotte");
			countryMap.put("MEX", "Mexico");
			countryMap.put("FSM", "Micronesia, Federated States of");
			countryMap.put("MDA", "Moldova, Republic of");
			countryMap.put("MCO", "Monaco");
			countryMap.put("MNG", "Mongolia");
			countryMap.put("MNE", "Montenegro");
			countryMap.put("MSR", "Montserrat");
			countryMap.put("MAR", "Morocco");
			countryMap.put("MOZ", "Mozambique");
			countryMap.put("MMR", "Myanmar");
			countryMap.put("NAM", "Namibia");
			countryMap.put("NRU", "Nauru");
			countryMap.put("NPL", "Nepal");
			countryMap.put("NLD", "Netherlands");
			countryMap.put("NCL", "New Caledonia");
			countryMap.put("NZL", "New Zealand");
			countryMap.put("NIC", "Nicaragua");
			countryMap.put("NER", "Niger");
			countryMap.put("NGA", "Nigeria");
			countryMap.put("NIU", "Niue");
			countryMap.put("NFK", "Norfolk Island");
			countryMap.put("MNP", "Northern Mariana Islands");
			countryMap.put("NOR", "Norway");
			countryMap.put("OMN", "Oman");
			countryMap.put("PLW", "Palau");
			countryMap.put("PSE", "Palestinian Territory, Occupied");
			countryMap.put("PAN", "Panama");
			countryMap.put("PNG", "Papua New Guinea");
			countryMap.put("PRY", "Paraguay");
			countryMap.put("PER", "Peru");
			countryMap.put("PHL", "Philippines");
			countryMap.put("PCN", "Pitcairn");
			countryMap.put("POL", "Poland");
			countryMap.put("PRT", "Portugal");
			countryMap.put("PRI", "Puerto Rico");
			countryMap.put("QAT", "Qatar");
			countryMap.put("REU", "R�union");
			countryMap.put("ROU", "Romania");
			countryMap.put("RUS", "Russian Federation");
			countryMap.put("RWA", "Rwanda");
			countryMap.put("BLM", "Saint Barth�lemy");
			countryMap.put("SHN", "Saint Helena, Ascension and Tristan da Cunha");
			countryMap.put("KNA", "Saint Kitts and Nevis");
			countryMap.put("LCA", "Saint Lucia");
			countryMap.put("MAF", "Saint Martin (French part)");
			countryMap.put("SPM", "Saint Pierre and Miquelon");
			countryMap.put("VCT", "Saint Vincent and the Grenadines");
			countryMap.put("WSM", "Samoa");
			countryMap.put("SMR", "San Marino");
			countryMap.put("STP", "Sao Tome and Principe");
			countryMap.put("SAU", "Saudi Arabia");
			countryMap.put("SEN", "Senegal");
			countryMap.put("SRB", "Serbia");
			countryMap.put("SYC", "Seychelles");
			countryMap.put("SLE", "Sierra Leone");
			countryMap.put("SGP", "Singapore");
			countryMap.put("SXM", "Sint Maarten (Dutch part)");
			countryMap.put("SVK", "Slovakia");
			countryMap.put("SVN", "Slovenia");
			countryMap.put("SLB", "Solomon Islands");
			countryMap.put("SOM", "Somalia");
			countryMap.put("ZAF", "South Africa");
			countryMap.put("SGS", "South Georgia and the South Sandwich Islands");
			countryMap.put("SSD", "South Sudan");
			countryMap.put("ESP", "Spain");
			countryMap.put("LKA", "Sri Lanka");
			countryMap.put("SDN", "Sudan");
			countryMap.put("SUR", "Suriname");
			countryMap.put("SJM", "Svalbard and Jan Mayen");
			countryMap.put("SWZ", "Swaziland");
			countryMap.put("SWE", "Sweden");
			countryMap.put("CHE", "Switzerland");
			countryMap.put("SYR", "Syrian Arab Republic");
			countryMap.put("TWN", "Taiwan, Republic of China");
			countryMap.put("TJK", "Tajikistan");
			countryMap.put("TZA", "Tanzania, United Republic of");
			countryMap.put("THA", "Thailand");
			countryMap.put("TLS", "Timor-Leste");
			countryMap.put("TGO", "Togo");
			countryMap.put("TKL", "Tokelau");
			countryMap.put("TON", "Tonga");
			countryMap.put("TTO", "Trinidad and Tobago");
			countryMap.put("TUN", "Tunisia");
			countryMap.put("TUR", "Turkey");
			countryMap.put("TKM", "Turkmenistan");
			countryMap.put("TCA", "Turks and Caicos Islands");
			countryMap.put("TUV", "Tuvalu");
			countryMap.put("UGA", "Uganda");
			countryMap.put("UKR", "Ukraine");
			countryMap.put("UMI", "United States Minor Outlying Islands");
			countryMap.put("URY", "Uruguay");
			countryMap.put("UZB", "Uzbekistan");
			countryMap.put("VUT", "Vanuatu");
			countryMap.put("VEN", "Venezuela, Bolivarian Republic of");
			countryMap.put("VNM", "Viet Nam");
			countryMap.put("VGB", "Virgin Islands, British");
			countryMap.put("VIR", "Virgin Islands, U.S.");
			countryMap.put("WLF", "Wallis and Futuna");
			countryMap.put("ESH", "Western Sahara");
			countryMap.put("YEM", "Yemen");
			countryMap.put("ZMB", "Zambia");
			countryMap.put("ZWE", "Zimbabwe");
		}
		return countryMap;
	}
	public static Map<String, String> getPurpose() {
		if (purpose.isEmpty()) {
			purpose.put("Club Registration Status", "Club Registration Status");
			purpose.put("Report a Problem", "Report a Problem");
			purpose.put("Suggestion", "Suggestion");
			purpose.put("Interested to Join CricClubs", "Interested to Join CricClubs");
			purpose.put("Other..", "Other..");
					}

		return purpose;
	}

	public static Map<String, String> getMatchType() {
		if (matchType.isEmpty()) {
			matchType.put("l", "League");
			matchType.put("sl", "Super League");
			matchType.put("po", "Playoff");			
			}

		return matchType;
	}
	
	public static Map<String, String> gettype() {
		if (type.isEmpty()) {
			type.put("u", "Umpire");
			type.put("c", "Coach");
			type.put("s", "Scorer");			
			}

		return type;
	}
	
		public static String getCountryDisplayValue(String countryCode) {
		return getCountryMap().get(countryCode);
	}

	public static <K, V extends Comparable<? super V>> Map<K, V>
			sortByValue(Map<K, V> map)
	{
		List<Map.Entry<K, V>> list =
				new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>()
		{
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	private static boolean isTeamLogoExists(String logo_file_path){
		try{
				File dir1 = new File (CommonUtility.getDocumentsRepPath() + "/.." +logo_file_path.trim());
				return dir1.exists();
		}catch(Exception e){
			return false;
		}
	}
		
	/***
	 * @param logo_file_path
	 * @return
	 *  method takes the partial filepath and call a method
	 *  which returns fullpath with env based domain.
	 */
	public static String getTeamLogoPath(String logo_file_path){
		
		if (!CommonUtility.isNullOrEmpty(logo_file_path)) {
			return logo_file_path;
		}else{
			int random = (int) (Math.random() * 4);
			return CommonUtility.getCDN() + "/img/icons/no-image-team"+(random+1)+".jpg";
		}
	}
	
	public static String getDayOfWeek(int day){
		return daysOfWeek[day-1];
	}
	
    public static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
    	if(StringUtils.isEmpty(property)) {
    		return null;
    	}
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return Base64.getEncoder().encodeToString(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }
    
   
    
    public static String getTokenForUrl(String src){
    	String token;
		try {
			token = encrypt(src);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			return null;
		}
    	token = token.replaceAll("[^a-zA-Z0-9]" , "");
    	return token;
    }

    public static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(Base64Decode(property)), StandardCharsets.UTF_8);
    }
    
	public static boolean isWithinRange(Date date, int range) {
		if (date == null)
			return false;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 7);
		if (c.getTime().compareTo(new Date()) < 0) {
			return false;
		}
		return true;
	}
	
	public static String timeLapsed(Date date){
		if(date == null){
			return "-";
		}
		long different = new Date().getTime() - date.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;
		
		return elapsedMinutes + " min, " + elapsedSeconds + " sec";
	}
	
	public static String timeLapsedFromSeconds(long seconds){
		if(seconds == 0){
			return "";
		}
		if(seconds > 86400){
			return (seconds/86400) + " day"+(seconds>=172800?"s":"")+" ago.";
		}
		long different = seconds;
		long minutesInSec = 60;

		long elapsedMinutes = different / minutesInSec;
		different = different % minutesInSec;

		long elapsedSeconds = different;
		
		
		
		return elapsedMinutes + " min, " + elapsedSeconds + " sec ago.";
	}
	
	/*public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		TimeZone fromTimeZone = calendar.getTimeZone();
		TimeZone toTimeZone = TimeZone.getTimeZone("CST");

		calendar.setTimeZone(fromTimeZone);
		calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
		if (fromTimeZone.inDaylightTime(calendar.getTime())) {
		    calendar.add(Calendar.MILLISECOND, calendar.getTimeZone().getDSTSavings() * -1);
		}

		calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
		if (toTimeZone.inDaylightTime(calendar.getTime())) {
		    calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
		}

	}*/
	
	public static String getUmpireCoachScorerString(String type) {
		String typeStr = "";
		if (type != null && type.equalsIgnoreCase("u")) {
			typeStr = "Umpire";
		} else if (type != null && type.equalsIgnoreCase("c")) {
			typeStr = "Coach";
		}else if (type != null && type.equalsIgnoreCase("s")) {
			typeStr = "Scorer";
		}else if (type != null && type.equalsIgnoreCase("ucs")) {
			typeStr = "Umpire,Coach & Scorer";
		}else if (type != null && type.equalsIgnoreCase("uc")) {
			typeStr = "Umpire & Coach";
		}else if (type != null && type.equalsIgnoreCase("us")) {
			typeStr = "Umpire & Scorer";
		}else if (type != null && type.equalsIgnoreCase("cs")) {
			typeStr = "Coach & Scorer";
		}
		return typeStr;
	}
	
	public static String getShortUmpireCoachScorerString(String type) {
		String typeStr = "";
		if (type != null && type.equalsIgnoreCase("Umpire")) {
			typeStr = "u";
		} else if (type != null && type.equalsIgnoreCase("Coach")) {
			typeStr = "c";
		}else if (type != null && type.equalsIgnoreCase("Scorer")) {
			typeStr = "s";
		}else if (type != null && type.equalsIgnoreCase("Umpire,Coach & Scorer")) {
			typeStr = "ucs";
		}else if (type != null && type.equalsIgnoreCase("Umpire & Coach")) {
			typeStr = "uc";
		}else if (type != null && type.equalsIgnoreCase("Umpire & Scorer")) {
			typeStr = "us";
		}else if (type != null && type.equalsIgnoreCase("Coach & Scorer")) {
			typeStr = "cs";
		}
		return typeStr;
	}

	public static boolean isValidLeagueId(String leagueId) {
		try {
			if(isInteger(leagueId)){
				return true;
			}
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}
	
	public static java.sql.Date utilToSqlDate(Date date){
	    return new java.sql.Date(date.getTime());
	}

	public static boolean isNullOrEmptyOrNULL(String value) {
		return ((value == null) || "".equals(value.trim()) || "null".equalsIgnoreCase(value.trim()) || "undefined".equalsIgnoreCase(value.trim()));
	}
	
	public static Date getStringToCSTDateByFormate(String sDate ,String format ) {
		SimpleDateFormat formatter=new SimpleDateFormat(format); 
		try {
			return getDateWithTimeZone(formatter.parse(sDate), TimeZone.getTimeZone("CST"), formatter);
		} catch (Exception e) {
			return null;
		}  
	}
	
	public static Date getDateWithTimeZone(final Date str, final TimeZone tz, SimpleDateFormat formatter){
		  try {
			  String date1 = formatter.format(str);
			  formatter.setTimeZone(tz);
			  return formatter.parse(date1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public static boolean isListNullEmpty(List<?> list) {
		// TODO Auto-generated method stub
		return (list == null || list.isEmpty());
	}
	public static boolean isSetNullEmpty(Set<?> list) {
		// TODO Auto-generated method stub
		return (list == null || list.isEmpty());
	}
	public static boolean isMapNullEmpty(Map<?,?> map) {
		// TODO Auto-generated method stub
		return (map == null || map.isEmpty());
	}
	public static boolean verifyIfClubAdmin(UserDto user, int clubId) {
		//if(clubId == 7683 || clubId == 10126 || clubId == 50 || clubId== 1809){
			List<Integer> clubsAdmin = user.getAdminClubs();
			if(clubsAdmin != null && !clubsAdmin.isEmpty()){
				return true;
			}
		//}
		
		return false;
	}

	public static boolean isOnlyDigitInString(String str) throws CCException {
		
		if(!str.contains(",") && CommonUtility.isValidLeagueId(str)) {
			return true;
		}
		String st[] = str.split(",");		
		if(st != null){
			for(int i = 0; i < st.length; i++){
				int num = CommonUtility.stringToInt(st[i]);
				if(num <= 0){
					throw new CCException("Valid Ids Required.", CCErrorConstant.VALID_IDS_REQUIRED);
				}
			}
		}
		return true;
	}
	public static int getBallsFromOver(String oversBowled) {
		int totalBalls = 0;
		if(!isNullOrEmptyOrNULL(oversBowled)){
			if(oversBowled.contains(".")){
				String[] ballsOver = oversBowled.split("\\.");
				int overs = stringToInt(ballsOver[0]);
				totalBalls = (overs * 6) + stringToInt(ballsOver[1]);
			}else{
				totalBalls = stringToInt(oversBowled) * 6;
			}
		}
		return totalBalls;
	}
	
	public static int getBallsFromOver(String oversBowled, int ballsPerOver) {
		int totalBalls = 0;
		if(!isNullOrEmptyOrNULL(oversBowled)){
			if(oversBowled.contains(".")){
				String[] ballsOver = oversBowled.split("\\.");
				int overs = stringToInt(ballsOver[0]);
				totalBalls = (overs * ballsPerOver) + stringToInt(ballsOver[1]);
			}else{
				totalBalls = stringToInt(oversBowled) * ballsPerOver;
			}
		}
		return totalBalls;
	}

	
	
	public static MatchDto populateDLTargetAtInning1End(int clubId, String matchId) throws Exception {
		List<MatchDLRecord> matchDLRecords = MatchDLRecordsFactory.getDLRecords(CommonUtility.stringToInt(matchId), 1, clubId);
		MatchDto matchDto = MatchesFactory.getMatch(CommonUtility.stringToLong(matchId), clubId);
		List<DLSInputData> intruptions = new ArrayList<DLSInputData>();
		
		double g50Value = ApplicationConstants.G_50_VALUE;
		
		int t1TotalRun =  matchDto.getT1_1total();
		for(MatchDLRecord rec : matchDLRecords){
			intruptions.add(convertIntoDlsInputList(rec));
		}
		String matchType = "50";
		
		if(matchDto.getOvers() <= 20) {
			matchType = "20";
		}
		Map res = DLSCalculation.calculateDLSScore(intruptions, 1,t1TotalRun, g50Value, matchType);
		matchDto.setR1ResAvailable(CommonUtility.stringToDouble(String.valueOf(res.get("totalR1Used"))));
		matchDto.setT2Target((int)CommonUtility.stringToDouble(String.valueOf(res.get("targetScore"))));

		MatchesFactory.updateMatchR1AvailableAndT2Target(matchDto, clubId);
		return matchDto;
	}
	public static DLSInputData convertIntoDlsInputList(MatchDLRecord rec) {
		DLSInputData dlsData = new DLSInputData();
		
		dlsData.setBallsAtStartOfPlay(CommonUtility.getBallsFromOver(String.valueOf(rec.getOversAtStartOfInning())));
		dlsData.setBallsDecidedAtStart(CommonUtility.getBallsFromOver(String.valueOf(rec.getMaxOver())));
		dlsData.setBallsPlayed(CommonUtility.getBallsFromOver(String.valueOf(rec.getOversPlayed())));
		dlsData.setRevisedBalls(CommonUtility.getBallsFromOver(String.valueOf(rec.getOversRevised())));
		dlsData.setWicketLost(rec.getWicketsLost());
		
		return dlsData;
	}

	public static  void populateMatchDlRecordFromDB(MatchDLRecord matchDLRecord, MatchDto matchDto, int clubId) throws Exception {
		float lastRevisedOvers = 0;
		if(matchDLRecord.getInningNum() == 1 || matchDto.getT2RevisedOvers() <= 0){
			lastRevisedOvers = matchDto.getOvers();
		}else {
			lastRevisedOvers = matchDto.getT2RevisedOvers();
		}
		matchDLRecord.setOversRevised(CommonUtility.stringToDouble(CommonUtility.ballsToOvers(CommonUtility.oversToBalls(lastRevisedOvers+"") - CommonUtility.oversToBalls(matchDLRecord.getOversLost()+""))));
		String oversPlayer = ScoringFactory.getOverPlayed(matchDLRecord.getMatchID(), clubId, matchDLRecord.getInningNum());
		matchDLRecord.setOversPlayed(Double.valueOf(CommonUtility.ballsToOvers(CommonUtility.oversToBalls(oversPlayer))));
		FixtureDto fixtureDto = FixturesFactory.getFixtureForMatch(matchDLRecord.getMatchID(), clubId);
		LeagueDto league = LeagueFactory.getLeague(fixtureDto.getLeagueId(), clubId);
		matchDLRecord.setMaxOver(league.getMaxOvers());
		matchDLRecord.setOversAtStartOfInning(matchDto.getOvers());
		int wicketLost = ScoringFactory.getWicketLost(matchDLRecord.getMatchID(), clubId, matchDLRecord.getInningNum());
		matchDLRecord.setWicketsLost(wicketLost);
	}
	
	public static String stringMask(String string){
			String name= "";
			if(!CommonUtility.isNullOrEmpty(string)){				
				name = string.replaceAll("(?<=..).", "*"); 				
			}
		return name;
		
	}
	public static String emailMask(String email){
		String emailMask= "";
		
		if(!CommonUtility.isNullOrEmpty(email)){				
			emailMask = email.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*"); 
		}	
	return emailMask;
	
	}
	
	public static int convertAlphaToNumeric(String alphabet){
		String str = alphabet;
		int groupNum = 0;
	    char[] ch  = str.toCharArray();
	    for(char c : ch)
	    {
	        int temp = (int)c;
	        int temp_integer = 64; //for upper case
	if(temp<=90 & temp>=65)
		groupNum = temp-temp_integer;
	    //System.out.print(temp-temp_integer);
	    }
	return groupNum;	
	}
	
	public static String convertNumericToAlpha(int numeric){
		String groupName = "";
		if(numeric >0 && numeric <27){
			groupName = String.valueOf((char)(Integer.valueOf(numeric) + 64));
		}
	return groupName;	
	}

	public static  MatchDto getParScoreInning2(int clubId, String matchId)
			throws Exception {
		MatchDLRecord matchDLRecord = new MatchDLRecord();
		matchDLRecord.setMatchID(CommonUtility.stringToInt(matchId));
		matchDLRecord.setInningNum(2);
		String oversPlayed = ScoringFactory.getOverPlayed(matchDLRecord.getMatchID(), clubId, 2);
		MatchDto matchDto = MatchesFactory.getMatch(matchDLRecord.getMatchID(), clubId);
		int balls = CommonUtility.oversToBalls(oversPlayed);
		int ballsRevOv = CommonUtility.oversToBalls(String.valueOf(matchDto.getT2RevisedOvers()));
		matchDLRecord.setOversLost(CommonUtility.stringToDouble(CommonUtility.ballsToOvers(ballsRevOv - balls)));
		
		CommonUtility.populateMatchDlRecordFromDB(matchDLRecord, matchDto, clubId);
		
		/*int t2RevisedOvers = matchDto.getT2RevisedOvers() - (int) CommonUtility.stringToDouble(oversPlayed);
		int oversPlyed = (int) CommonUtility.stringToDouble(oversPlayed);*/
		//matchDLRecord.setOversLost( matchDto.getT2RevisedOvers() - (int) CommonUtility.stringToDouble(oversPlayed));
		
		//List<MatchDLRecord> matchDLRecords1 = MatchDLRecordsFactory.getDLRecords(CommonUtility.stringToInt(matchId), 1, clubId);
		List<MatchDLRecord> matchDLRecords2 = MatchDLRecordsFactory.getDLRecords(matchDLRecord.getMatchID(), 2, clubId);
		//List<DLSInputData> intruptions1 = new ArrayList<DLSInputData>();
		matchDLRecords2.add(matchDLRecord);
		List<DLSInputData> intruptions2 = new ArrayList<DLSInputData>();
		double g50Value = ApplicationConstants.G_50_VALUE;
		
		/*for(MatchDLRecord rec : matchDLRecords1){
			intruptions1.add(convertIntoDlsInputList(rec));
		}
		*/
		for(MatchDLRecord rec : matchDLRecords2){
			intruptions2.add(convertIntoDlsInputList(rec));
		}
		
		int t1FinalScore = matchDto.getT1_1total();
		double r1ResAvailable = 100.0d;
		if(matchDto.isDls()){
			if(matchDto.getR1ResAvailable() > 0){
				r1ResAvailable = matchDto.getR1ResAvailable();
			}
		}
		String matchType = "50";
		
		if(matchDto.getOvers() <= 20) {
			matchType = "20";
		}
		Map<String, Object> res = DLSCalculation.calculateDLSScore(intruptions2, 2, t1FinalScore, g50Value, r1ResAvailable, matchType);
		
		int newTargetForT2 = (int)CommonUtility.stringToDouble(String.valueOf(res.get("targetScore")));
		
		matchDto.setT2RevisedOvers(CommonUtility.oversToBalls(matchDLRecord.getOversRevised()+""));
		matchDto.setT2Target(newTargetForT2);
		return matchDto;
	}
	
	public static double getT2TotalResLoss(int clubId, MatchDto match, List<DLSInputData> intruptions) throws Exception {
		
		double lostR2 = 0.0;
		String matchType = "50";
		if("Twenty20".equalsIgnoreCase(match.getSeriesType())) {
			matchType = "20";
		}		
		if(intruptions != null && intruptions.size()>0) {				
			for (DLSInputData intruption : intruptions) {

				int ballsLeftRS = intruption.getBallsAtStartOfPlay() - intruption.getBallsPlayed();
				int ballsLeftRR = intruption.getBallsAtStartOfPlay()
						- (intruption.getBallsPlayed() + intruption.getBallsLost());

				double rs2 = DLSCalculation.getPercentageFromDLSChart(ballsLeftRS, intruption.getWicketLost(),
						matchType);
				double rr2 = DLSCalculation.getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(),
						matchType);

				lostR2 += (rs2 - rr2);
			}			
		}
		return lostR2;
	}

	public static Date getDateFromFixtureString(String date, String time) {
		String input = date + " " + time;
		DateFormat df = new SimpleDateFormat(FIXTURE_DATE_FORMATE);
		Date date1 = null;
		try {
			date1 = df.parse(input);
		} catch (Exception e) {
			df = new SimpleDateFormat("MM/dd/yyyy");
			try {
				date1 = df.parse(date);
			} catch (Exception e1) {
				//log.error(e.getMessage(),e);
			}
		}
		return date1;
	}

	public static String changeDateFormat(String date, String reqiredFormat) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat dfReq = new SimpleDateFormat(reqiredFormat);
		Date date1 = null;
		try {
			date1 = df.parse(date);
			return dfReq.format(date1);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getClubNameById(int clubId) {
		String clubName = "";
		try {
			clubName = ClubFactory.getActiveLiteClub(clubId, true).getName();
		} catch (Exception e) {
			log.error(e.getMessage() + " For " + clubId); 
		}
		return clubName;
	}
	
	public static ClubDto getClubDtoObject(int clubId) {
		try {
			return ClubFactory.getClub(clubId); //please discuss with team
		} catch (Exception e) {
			log.error(e.getMessage() + " For " + clubId); 
		}
		return null;
	}
	

	public static List<LeagueLite> getLiteLeagueList(List<LeagueDto> leagueList) {
		List<LeagueLite> liteLeagues = null;
		if(leagueList != null) {
			liteLeagues = new ArrayList<LeagueLite>(leagueList.size());
			LeagueLite lglite = null;
			for(LeagueDto league : leagueList) {
				lglite = getLeagueLiteObject(league);
				liteLeagues.add(lglite);
			}
		}
		return liteLeagues;
	}
	
	public static LeagueLite getLeagueLiteObject(Object obj) {
		LeagueLite lgLite = new LeagueLite();
		try {
			Gson json = new Gson();
			if(obj != null && obj instanceof LeagueDto) {
				LeagueDto lg = (LeagueDto)obj;
				lgLite = json.fromJson(json.toJson(lg), LeagueLite.class);
				return lgLite;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static String objectToJson(Object obj) {
		return new Gson().toJson(obj);
	}

	public static boolean stringToBoolean(String showAllPlayers) {
		if("true".equalsIgnoreCase(showAllPlayers)) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String Base64Encode(String value) {
        String encodedValue = "";

        try {
            encodedValue = Base64.getEncoder().encodeToString(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        }
        // new String(pbeCipher.doFinal(new BASE64Decoder().decodeBuffer(property)),
        // "UTF-8")
        return encodedValue;
    }

 

    public static byte[] Base64Decode(String value) {
        byte[] encodedValue = null;
        try {
			encodedValue = Base64.getDecoder().decode(value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return encodedValue;
    }

	public static List<Integer> stringToListOfInt(String string) {
		List<Integer> listOfInt = new ArrayList<Integer>();
		
		try {
			List<String> numbers = Arrays.asList(string.split(","));
			for (String number : numbers) {
				int seriesId = stringToInt(number);
				if (seriesId > 0) {
					listOfInt.add(seriesId);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return listOfInt;
	}

	public static String listOfIntToString(List<Integer> seriesAdmin) {
		String citiesCommaSeparated = null;
		try {
			citiesCommaSeparated = seriesAdmin.stream().map(String::valueOf).collect(Collectors.joining(","));
		} catch (Exception e) {
		}
		return citiesCommaSeparated;
	}
	
	public static void insertDefaultNews(long newUserId ,ClubDto clubDto) throws Exception {
		
		NewsDto news = new NewsDto();
		news.setUserId(newUserId);
		news.setTitle("Welcome to "+clubDto.getName());
		String newsContent = "</strong>Welcome to "+clubDto.getName()+"</strong>" + 
				" Checkout this space for latest news and updates from "+clubDto.getName()  
				+ " Powered by CricClubs - Manage Your Cricket league for FREE with"
				+ " Live Scoring. For more information on CricClubs please"
				+ " visit <a href=\"/\">CricClubs.com</a>.";						
		news.setNews(newsContent);						
		news.setShowOnHome(true);
		news.setImage("/utilsv2/img/banner/banner.jpg");
		NewsFactory.insertNews(news, clubDto.getClubId());
	}
	
	 public static boolean isExactContain(String source, String subItem){
         String pattern = "\\b"+subItem+"\\b";
         Pattern p=Pattern.compile(pattern);
         Matcher m=p.matcher(source);
         return m.find();
    }

	public static void validateAndUpdateFixtureDate(FixtureDto fixture) {
		if(fixture != null) {
			try {
				Date date = stringToDate(fixture.getDate());
				Date oldDate = stringToDate("01/01/1000");
				
				if(date != null && oldDate != null) {
					if(date.before(oldDate)) {
						date.setYear(date.getYear() + 2000);
						fixture.setDate(dateToString(date));
					}
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			}
		}
		
		
	}
	
	/**
	 * @param dto
	 */
	public static void createLoginForUser(PlayerDto dto, UserDto uDto, int clubId, String password, UserDto user) throws Exception {
		
		UserDto dbUser = UserFactory.getUserByEmail(dto.getEmail(), clubId);
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		
		if (dbUser == null) {
			UserDto newUser = new UserDto();
			newUser.setUserName(dto.getEmail());
			String userPassword = CommonUtility.isNullOrEmpty(password) ? CommonUtility.getPassword(8) : password;
			newUser.setPassword(CommonUtility.encrypt(userPassword));
			newUser.setEmail(dto.getEmail());
			newUser.setFname(dto.getFirstName());
			newUser.setLname(dto.getLastName());
			newUser.setPhone(uDto.getPhone());
			newUser.setAddress(uDto.getAddress());
			newUser.setCity(uDto.getCity());
			newUser.setState(uDto.getState());
			newUser.setPostalCode(uDto.getPostalCode());			
			newUser.setPlayerID(dto.getPlayerID());
			newUser.setCountryCode(club.getCountry());
			
			if(user==null) {
				UserFactory.registerUser(newUser, clubId, "Guest");
			}else {
				UserFactory.registerUser(newUser, clubId, Integer.toString(user.getUserID()));
			}			
			NotificationHelper.sendWelcomePlayerEmail(newUser, club,false);
			
		} else {
			UserFactory.updateUserPlayerId(dto.getPlayerID(), dbUser.getUserID(), Integer.toString(dbUser.getUserID()));
			NotificationHelper.sendWelcomePlayerEmail(dbUser, club, true);
		}
	}
	
	public static void addExistingUserToClub(PlayerDto dto, int clubId) throws Exception {
		UserDto user = UserFactory.getUserByPlayerId(dto.getPlayerID());
		UserDto clubUser = UserFactory.getUserById(user.getUserID(), clubId);
		if (clubUser == null) {
			UserFactory.registerExistingUser(user, clubId, Integer.toString(user.getUserID()));
		}
		//ClubDto club = ClubFactory.getClub(clubId);
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		NotificationHelper.sendWelcomePlayerEmail(user, club, true);
	}

	public static List<String> splitString(String customDomain) {
		if(!isNullOrEmptyOrNULL(customDomain)) {
			String str[] = customDomain.split(",");
			List<String> al = new ArrayList<String>();
			al = Arrays.asList(str);
			return al;
		}
		return Collections.EMPTY_LIST;
		
	}
	
	public static String getTitleMsg(Ticket tk) {
		
		if(tk != null) {
			if(!CommonUtility.isNullOrEmptyOrNULL(tk.getAct_by())) {
				return "Action taken by " + tk.getAct_by() + "("+tk.getAct_email()+") on " +  getDateString(tk.getActionTakenDate());
			}
		}
		
		return "";
	}
	
	public static String getDateString(Date date) {
		 String str = "";
		if(date != null) {
			SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MMMM/yyyy");
		    str = simpleformat.format(date);
		}
	    return str;
	}
	
	public static void main(String as[]) throws GeneralSecurityException, IOException {
		System.out.println(decrypt("uAwa71V9Tj4="));
	}
	
	/***
	 * @param path
	 * @return
	 * Method takes partial path from caller method and returns 
	 * with env based domain name pre appended.
	 */
	public static String getImagePrefix(HttpServletRequest req) {
			String env = CommonUtility.isNullOrEmpty(System.getProperty("env"))?"dev":System.getProperty("env");
			return "local".equalsIgnoreCase(env)?"https://www-dev.cricclubs.com" : "dev".equalsIgnoreCase(env) 
						? "https://www-dev.cricclubs.com" : "test".equalsIgnoreCase(env) 
								? "https://www-test.cricclubs.com" : env.contains("sca") ? "https://dlbdbr7dq37if.cloudfront.net" : "https://cricclubs.com";
	}
	
	public static String getAppUrl(HttpServletRequest req) {
		String env = CommonUtility.isNullOrEmpty(System.getProperty("env"))?"dev":System.getProperty("env");
		return "qa".equalsIgnoreCase(env)?"http://localhost:8080" : "dev".equalsIgnoreCase(env) 
					? "https://www-dev.cricclubs.com" : "test".equalsIgnoreCase(env) 
							? "https://www-test.cricclubs.com" : env.contains("sca") ? "https://scores.cricketsingapore.com" : "https://cricclubs.com";
	}
	
	public static void refreshMatchStats(int matchId, int clubId) {
		
		try {
			MatchDto match = MatchesFactory.getMatch(matchId, clubId);
			
			List<BattingDto> team1Batting = null;
			List<BattingDto> team2Batting = null;
			List<BowlingDto> team1Bowling = null;
			List<BowlingDto> team2Bowling = null;
			team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
					match.getBattingFirst(), clubId);
			team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
					(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
					clubId);
			team1Bowling = PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(match.getMatchID(),
					match.getBattingFirst(), clubId);
			team2Bowling = PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(match.getMatchID(),
					(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
					clubId);

			List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos = PlayerStatisticsSummaryGenrator.getPlayerStatisticSummaryDtos(team1Batting, team2Batting, team1Bowling, team2Bowling, match,clubId);
			PlayerStatisticsFactory.deleteAllPlayerSumary(match.getMatchID(), clubId);
			PlayerStatisticsFactory.saveAllPlayerSumary(playerStatisticsSummaryDtos, clubId);
			
			//Top performer update to Match Stats Summary (Remove and Add)
			TopPerformersSummaryFactory.deleteTopPerformersSummary(matchId, clubId);
			TopPerformersSummaryFactory.saveTopPerformersSummary(matchId, clubId, 1);
			
			//All players of the Match, Career Stats are Updated for current club
			CareerStatsSummaryFactory.updateCareerStatsSummary(match, clubId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static String trimString(String string) {
		if(string != null) {
			return string.trim();
		}
		return "";
	}
	
	public static String trimStringOrNull(String string) {
		if(string != null) {
			return string.trim();
		}
		return null;
	}
	
	public static String getValidMatchType(String matchType) {
		return CommonUtility.isNullOrEmptyOrNULL(matchType) ? "l" : matchType;
	}

	public static String arrayToStringOfID(String[] selectedCountryList) {
		
		if(selectedCountryList != null && selectedCountryList.length > 0) {
			return String.join(",", selectedCountryList);
		}
		return null;
	}

	public static String getDataFromJson(JSONObject data, String key) {
		try {
			return data.getString(key);
		} catch (Exception e) {
		}
		return "";
	}

	public static String getMaxCharForName(String name) {
		if(!StringUtils.isEmpty(name) && name.length() > 50) {
			return name.substring(0, 50);
		}
		return name;
	}
	
	public static List<ClubSearchDto> getLiteClubsFromLite(List<ClubDtoLite> clubs) {
		List<ClubSearchDto> liteClubs = new ArrayList<ClubSearchDto>();
		for (ClubDtoLite club : clubs) {
			liteClubs.add(populateClubLite(club));
		}
		return liteClubs;
	}
	
	private static ClubSearchDto populateClubLite(ClubDtoLite club) {
		ClubSearchDto lite = new ClubSearchDto();
		lite.setClubId(club.getClubId());
		lite.setName(club.getName());
		lite.setPracticeClub(club.isPracticeClub());
		lite.setAllowRegister(club.getAllowRegister());
		lite.setClubStructureEnabled(club.isClubStructureEnabled());
		lite.setIsAcademy(club.getIsAcademy());
		lite.setCanScorerAddPlayer(club.isCanScorerAddPlayer());
		lite.setLogoURL(CommonUtility.getImagePrefix(null)
				+ ((!CommonUtility.isNullOrEmpty(club.getLogo_file_path())) ? club.getLogo_file_path()
						: "/documentsRep/logos/defaultLogo.png"));
		return lite;
	}
	
	/**
	 * @param team1Batting
	 * @return
	 */
	public static List<BattingDto> getBattingOrder(List<BattingDto> teamList) {
		List<BattingDto> battingOrderList = new ArrayList<BattingDto>();
		if (teamList != null) {
			Iterator<BattingDto> itr = teamList.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				if (dto.getIsOut() == null) {
					battingOrderList.add(dto);
				}
			}
			teamList.removeAll(battingOrderList);
			teamList.addAll(battingOrderList);
		}
		return teamList;
	}

	/**
	 * @param team1Batting
	 * @return
	 */
	public static List<BowlingDto> getBowlingOrder(List<BowlingDto> teamList) {
		List<BowlingDto> bowlingOrderList = new ArrayList<BowlingDto>();
		if (teamList != null) {
			Iterator<BowlingDto> itr = teamList.iterator();
			while (itr.hasNext()) {
				BowlingDto dto = itr.next();
				if (dto.getBalls() == 0) {
					bowlingOrderList.add(dto);
				}
			}
			teamList.removeAll(bowlingOrderList);
			//teamList.addAll(bowlingOrderList);
		}
		return teamList;
	}
	
	public static boolean isInternationalLeague(int clubId) {
		
		if(clubId == 11707) {
			return true;
		}else {
			return false;
		}
	}

	public static String getClubTimeFormat(String timeStr) {
		
		int hour = 0;
		String hourStr = "";
		
		if (!isNullOrEmptyOrNULL(timeStr) && validateTime(timeStr)) {
			System.out.println(timeStr);
			if (timeStr.toUpperCase().contains("AM")) {
				timeStr = timeStr.substring(0, timeStr.indexOf(" "));
				// added code for AM cuz for 12 AM it is showing as 12 only where actually it is
				// 00:00

				String hourStrAm = timeStr.substring(0, timeStr.indexOf(":"));
				if (!isNullOrEmpty(hourStrAm)) {
					hour = stringToInt(hourStrAm);
				}
				if (hour == 12) {

					timeStr = "00" + timeStr.substring(timeStr.indexOf(":"));
				}

			} else if (timeStr.toUpperCase().contains("PM")) {
				hourStr = timeStr.substring(0, timeStr.indexOf(":"));
				if (!isNullOrEmpty(hourStr)) {
					hour = stringToInt(hourStr);
				}
				// added hour != 12 cuz for 12 PM it is adding 12 and showing as 24
				if (hour > 0 && hour != 12) {
					hour = hour + 12;
				}

				timeStr = hour + timeStr.substring(timeStr.indexOf(":"), timeStr.indexOf(" "));
			}
		}
		
		return timeStr;
	}
	
	public static String get12HourTime(String timeStr) {
		
		int hour = 0;
		String hourStr = ""; 
		String minStr = ""; 
		
		if(!isNullOrEmpty(timeStr)) {
			if(timeStr.contains(":")) {
				hourStr = timeStr.substring(0,timeStr.indexOf(":"));
				minStr = timeStr.substring(timeStr.indexOf(":")+1);
				if(!isNullOrEmpty(hourStr)) {
					hour = stringToInt(hourStr);
				}
				if(hour==12) {
					timeStr = "12:"+minStr+" PM";
				}else if(hour>12) {
					hour = hour-12;
					timeStr = hour+":"+minStr+" PM";
				}else {
					timeStr = timeStr+ " AM";
					if(hour==0) {
						timeStr = "12:"+minStr+" AM";
					}					
				}
				
			}
		}
		return timeStr;
	}
	
	public static String get24HourTime(String timeStr) {
		
		int hour = 0;
		String hourStr = ""; 
		String minStr = ""; 
		
		String timeStrOld = timeStr;
		
		timeStr = timeStr.substring(0,timeStr.indexOf(" "));
		
		if(!isNullOrEmpty(timeStr)) {
			if(timeStr.contains(":")) {
				hourStr = timeStr.substring(0,timeStr.indexOf(":"));
				minStr = timeStr.substring(timeStr.indexOf(":")+1);
				if(!isNullOrEmpty(hourStr)) {
					hour = stringToInt(hourStr);
				}
				if (timeStrOld.contains("PM")) {
					if (hour == 12) {
						timeStr = "12:" + minStr;
					} else {
						hour = hour + 12;
						timeStr = hour + ":" + minStr;
					}
				}
			}
		}
		return timeStr;
	}
	
	public static void syncFantasyTeamPlayers(int fixtureId, int matchId, int clubId) throws Exception {

		FixtureDto fixture = FixturesFactory.getFixture(fixtureId, clubId);

		FantasyTeamsDto fantasyTeam1 = FantasyTeamsFactory.getFantasyTeamByCCTeamId(fixture.getTeamOne(), clubId);
		FantasyTeamsDto fantasyTeam2 = FantasyTeamsFactory.getFantasyTeamByCCTeamId(fixture.getTeamTwo(), clubId);

		syncFantasyTeamPlayersByFantasyTeamId(fantasyTeam1.getId());
		syncFantasyTeamPlayersByFantasyTeamId(fantasyTeam2.getId());

	}
	
	public static void syncFantasyTeamPlayersByFantasyTeamId(int fantasyTeamId) throws Exception {

		FantasyTeamsDto fantasyTeamDto = FantasyTeamsFactory.getFantasyTeamById(fantasyTeamId);

		int clubId = fantasyTeamDto.getCcClubId();
		int ccTeamId = fantasyTeamDto.getCcTeamId();

		List<PlayerDto> teamPlayers = TeamFactory.getTeamPlayersList(ccTeamId, clubId);
		List<Integer> teamPlayerIds = new ArrayList<Integer>();
		String playerIdsStr = "";

		Map<Integer, PlayerDto> playerIdMap = new HashMap<Integer, PlayerDto>();

		if (!CommonUtility.isListNullEmpty(teamPlayers)) {
			for (PlayerDto player : teamPlayers) {
				playerIdMap.put(player.getPlayerID(), player);
				teamPlayerIds.add(player.getPlayerID());
				playerIdsStr += player.getPlayerID() + ",";
			}
		}

		if (playerIdsStr.contains(",")) {
			playerIdsStr = playerIdsStr.substring(0, playerIdsStr.length() - 1);
		}
		List<Integer> existingFantasyccPlayerids = FantasyPlayerFactory.getExistingFantasyCCPlayerIds(playerIdsStr);

		List<FantasyPlayerDto> fpDtos = new ArrayList<FantasyPlayerDto>();

		for (Integer playerId : teamPlayerIds) {
			if (!existingFantasyccPlayerids.contains(playerId)) {
				fpDtos.add(populateAndGetFantasyPlayerDto(playerIdMap, playerId));
			}
		}
		if(!CommonUtility.isListNullEmpty(fpDtos)) {			
			FantasyPlayerFactory.copyPlayersToFantasy(fpDtos);
		}

			Map<Integer, Integer> fantasyPlayeridsMap = FantasyPlayerFactory.getFantasyPlayerIdsMap(playerIdsStr);
			List<FantasyTeamPlayerDto> existingFantasyTeamPlayers = FantasyTeamPlayerFactory.getFantasyTeamPlayers(fantasyTeamId);
			List<Long> existingFantasyTeamPlayerIds = new ArrayList<Long>();
			if(!CommonUtility.isListNullEmpty(existingFantasyTeamPlayers)) {
				for(FantasyTeamPlayerDto dto : existingFantasyTeamPlayers) {
					existingFantasyTeamPlayerIds.add(dto.getCcPlayerId());
				}				
			}
			List<FantasyTeamPlayerDto> ftpDtos = new ArrayList<FantasyTeamPlayerDto>();

			for (PlayerDto player : teamPlayers) {

				if (!existingFantasyTeamPlayerIds.contains(Long.valueOf(player.getPlayerID()))) {

					FantasyTeamPlayerDto ftpDto = new FantasyTeamPlayerDto();

					ftpDto.setCcClubId(clubId);
					ftpDto.setCcPlayerId(player.getPlayerID());
					ftpDto.setCcTeamId(ccTeamId);
					ftpDto.setPlayerId(fantasyPlayeridsMap.get(player.getPlayerID()));
					ftpDto.setTeamId(fantasyTeamDto.getId());

					ftpDtos.add(ftpDto);
				}
			}
			if(!CommonUtility.isListNullEmpty(ftpDtos)) {
				FantasyTeamPlayerFactory.copyTeamPlayersToFantasy(ftpDtos);
			}
		
	}
	
	private static FantasyPlayerDto populateAndGetFantasyPlayerDto(Map<Integer, PlayerDto> playerIdMap, Integer playerId) {
		
		FantasyPlayerDto fpDto = new FantasyPlayerDto();
		
		PlayerDto player = playerIdMap.get(playerId);
		fpDto.setCcPlayerId(player.getPlayerID());
		fpDto.setName(player.getFullName());
		String nickName = player.getNickName();
		if(CommonUtility.isNullOrEmpty(nickName)) {
			nickName = player.getFirstName().substring(0,1)+" "+player.getLastName();
		}
		fpDto.setNickName(nickName);
		String category = "alr";
		if("Bowler".equalsIgnoreCase(player.getPlayingRole())) {
			category = "bow";
		}else if("Batsman".equalsIgnoreCase(player.getPlayingRole())) {
			category = "bat";
		}else if("Wicket Keeper".equalsIgnoreCase(player.getPlayingRole())) {
			category = "wk";
		}
		fpDto.setCategory(category);
		fpDto.setCredits(8);
		fpDto.setGender(player.getGender());
		fpDto.setImagePath(player.getProfilepic_file_path());
		
		return fpDto;
	}
	
	public static int isClubForCricket(int clubId) throws Exception {
		
		int sportId = 1;
		if(clubId>0) {
			ClubDto club = ClubFactory.getClub(clubId);
			if(club != null && club.isFootBall()) {
				sportId = 2;
			}			
		}
		return sportId;
	}
	
}
