package com.cricket.utility;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.beans.LiveScoreOverlayBean;
import com.cricket.beans.LiveScoreOverlayBeanFB;
import com.cricket.dao.ClubFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.MatchOverlayConfigFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.PlayerStatisticsFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dto.BallDto;
import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.MatchOverlayConfigDto;
import com.cricket.dto.OverDto;
import com.cricket.dto.OverlayMatchDataDto;
import com.cricket.dto.PartnershipDto;
import com.cricket.dto.PlayerBattingDto;
import com.cricket.dto.PlayerBowlingDto;
import com.cricket.dto.PlayerClubStatisticsDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerDtoFB;
import com.cricket.dto.SubstituteDtoFB;
import com.cricket.dto.TeamDto;
import com.cricket.dto.TeamGoalDto;
import com.cricket.dto.lite.BallBean;
import com.cricket.dto.lite.BallByBallBean;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.GroupBean;
import com.cricket.dto.lite.LeagueLite;
import com.cricket.dto.lite.ScoreCardBean;
import com.cricket.exception.CCException;
import com.cricket.helpers.CommonHelper;
import com.football.dao.IncidentFactory;
import com.football.dto.IncidentDto;

public class OverlayUtil {
	private static Logger log = LoggerFactory.getLogger(OverlayUtil.class);
	public static LiveScoreOverlayBean prepareScoreOverlayData(int clubId, int matchId) throws Exception {

		MatchOverlayConfigDto config = null; 
		OverlayMatchDataDto ovMtchDto = null;
		
		try {
			config = MatchOverlayConfigFactory.getConfig(matchId, clubId);
			ovMtchDto =  MatchOverlayConfigFactory.getOverlayMatchData(clubId, matchId);
			if(ovMtchDto == null)
			{
				MatchDto matchDto = MatchesFactory.getMatch(matchId, clubId);
				
				ovMtchDto = new OverlayMatchDataDto();

				ovMtchDto.setClubId(clubId);
				ovMtchDto.setMatchId(matchId);
				ovMtchDto.setMatchDate(matchDto.getMatchDate());
				ovMtchDto.setTeam1Id(matchDto.getTeamOne());
				ovMtchDto.setTeam2Id(matchDto.getTeamTwo());
				
				MatchOverlayConfigFactory.insertOverlayMatchData(ovMtchDto);
			}
		} catch (Exception e1) {
			// do nothing as the config will be null
		}
		int viewId = 1;
		if (config != null) {
			viewId = config.getViewId();
		}
		LiveScoreOverlayBean score = new LiveScoreOverlayBean();
		score.setView(viewId); // Not sending data with these overlay config. //30 & 31 - Not configured
		if(viewId == 0 || 
				viewId == 9 || viewId == 10 || viewId == 11 || viewId == 12){
			score.setView(viewId); // Not sending data with these overlay config.
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("matchId", String.valueOf(matchId));
			values.put("liveYouTubeLink", "//www.youtube.com/embed/iVZJiaFZrQM?rel=0&autoplay=1");
			score.setValues(values);
		}else if (viewId == 1 || 
				viewId == 16 || viewId == 17 || viewId == 18 || viewId == 19 ||  
				viewId == 23 || viewId == 24 || viewId == 25 || viewId == 26 || viewId == 27 ||viewId == 36 || viewId == 37 || viewId == 38 || viewId == 39 ||
				viewId == 29)  {
			scoreOverlay(clubId, matchId, viewId, score);
		}else if(viewId == 40){
			scoreOverlay(clubId, matchId, viewId, score);
			
		} else if (viewId == 2 || viewId == 3 || viewId == 4 || viewId == 5 ||  viewId == 6|| viewId == 7 || viewId == 8 
				|| viewId == 13 || viewId == 14 || viewId == 15
				|| viewId == 28 
				|| viewId == 32 || viewId == 33 || viewId == 34 || viewId == 35) {
			// batting T1 - 2, batting T2 - 4, bowling T1 - 3 bowling T2 - 5
			battingBowlingOverlay(clubId, matchId, viewId, score);
		}else if(viewId == 20) {
			pointsTableOverLay(clubId, matchId, viewId, score);
		}else if(viewId == 21 || viewId == 22 ) {
			score.setView(viewId); // Not sending data with these overlay config.
			Map<String, Object> values = new HashMap<String, Object>();
			String matchYouTubeLink = "//www.youtube.com/embed/iVZJiaFZrQM?rel=0&autoplay=1";
			MatchDto match = MatchesFactory.getMatch(matchId, clubId);			
			if(match != null) {
				matchYouTubeLink = CommonUtility.getYouTubeLink(match.getLive_streaming_link().trim());
			}
			values.put("matchId", String.valueOf(matchId));			
			values.put("liveYouTubeLink", matchYouTubeLink);
			score.setValues(values);
		}
		return score;
	}
	
	public static LiveScoreOverlayBeanFB prepareScoreOverlayDataFB(int clubId, int matchId) throws Exception {

		MatchOverlayConfigDto config = null;
		OverlayMatchDataDto ovMtchDto = null;
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);
		ClubDtoLite club= ClubFactory.getActiveLiteClub(clubId, false);
		LeagueLite leagueLite= club.getLeague(match.getLeagueId());
		

		try {
			config = MatchOverlayConfigFactory.getConfig(matchId, clubId);
			ovMtchDto = MatchOverlayConfigFactory.getOverlayMatchData(clubId, matchId);
			if (ovMtchDto == null) {

				ovMtchDto = new OverlayMatchDataDto();

				ovMtchDto.setClubId(clubId);
				ovMtchDto.setMatchId(matchId);
				ovMtchDto.setMatchDate(match.getMatchDate());
				ovMtchDto.setTeam1Id(match.getTeamOne());
				ovMtchDto.setTeam2Id(match.getTeamTwo());

				MatchOverlayConfigFactory.insertOverlayMatchData(ovMtchDto);
			}
		} catch (Exception e1) {
			// do nothing as the config will be null
		}
		int viewId = 1;
		if (config != null) {
			viewId = config.getViewId();
		}
		LiveScoreOverlayBeanFB score = new LiveScoreOverlayBeanFB();
		score.setView(viewId);
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		
		List<IncidentDto> incidentsList = IncidentFactory.getAllIncidentsForFootBallMatch(matchId, clubId);
		List<TeamGoalDto> t1GoalIncidents = new ArrayList<TeamGoalDto>();
		List<TeamGoalDto> t2GoalIncidents = new ArrayList<TeamGoalDto>();

		List<PlayerDto> t1Players = TeamFactory.getTeamPlayersList(match.getTeamOne(), clubId);
		List<PlayerDto> t2Players = TeamFactory.getTeamPlayersList(match.getTeamTwo(), clubId);
		
		Map<Integer, PlayerDto> playerIdDtoMap = new HashMap<Integer, PlayerDto>();
		
		List<Integer> t1PlayerIds = new ArrayList<Integer>();
		List<Integer> t2PlayerIds = new ArrayList<Integer>();
		
		List<PlayerDtoFB> teamOnePlayers = new ArrayList<PlayerDtoFB>();
		List<PlayerDtoFB> teamTwoPlayers = new ArrayList<PlayerDtoFB>();
		List<SubstituteDtoFB> teamOneSubstitutePlayers = new ArrayList<SubstituteDtoFB>();
		List<SubstituteDtoFB> teamTwoSubstitutePlayers = new ArrayList<SubstituteDtoFB>();

		for (PlayerDto dto : t1Players) {
			playerIdDtoMap.put(dto.getPlayerID(), dto);
			if(match.getPlayers1().contains(dto.getPlayerID())) {
				teamOnePlayers.add(populateAndGetPlayerFB(dto));
			}
			t1PlayerIds.add(dto.getPlayerID());
		}
		for (PlayerDto dto : t2Players) {
			playerIdDtoMap.put(dto.getPlayerID(), dto);
			if(match.getPlayers2().contains(dto.getPlayerID())) {
				teamTwoPlayers.add(populateAndGetPlayerFB(dto));
			}
			t2PlayerIds.add(dto.getPlayerID());
		}
		int t1Goals = 0;
		int t1Corners = 0;
		int t1Fouls = 0;
		int t1RedCards = 0;
		int t1YellowCards = 0;
		int t1GoalsSaved = 0;
		int t1offSides = 0;
		
		int t2Goals = 0;
		int t2Corners = 0;
		int t2Fouls = 0;
		int t2RedCards = 0;
		int t2YellowCards = 0;
		int t2GoalsSaved = 0;
		int t2offSides = 0;
		String  firstHalfStartTime = "";
		String  secondHalfStartTime = "";
		
		DateFormat formatterUTC = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
		
		for (IncidentDto dto : incidentsList) {
			if(dto.getIncidentType().equalsIgnoreCase("First Half Started")) {
				firstHalfStartTime = formatterUTC.format(dto.getCreatedTime());
			}
			if(dto.getIncidentType().equalsIgnoreCase("Second Half Started")) {
				secondHalfStartTime = formatterUTC.format(dto.getCreatedTime());
			}
			
			if (t1PlayerIds.contains(dto.getPlayer1Id())) {
				if (dto.getIncidentType().contains(IncidentFactory.GOAL_SCORED)) {
					t1Goals += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.CORNER)) {
					t1Corners += 1;
				}else if (dto.getIncidentType().contains("Foul")) {
					t1Fouls += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.YELLOW_CARD)) {
					t1YellowCards += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.RED_CARD)) {
					t1RedCards += 1;
				}else if (dto.getIncidentType().toUpperCase().contains((IncidentFactory.GOAL_SAVED).toUpperCase())) {
					t1GoalsSaved += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.OFFSIDE)) {
					t1offSides += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.SUBSTITUTE)) {
					teamOneSubstitutePlayers.add(populateAndGetSubstituteFB(dto, playerIdDtoMap));
				}else if (dto.getIncidentType().contains(IncidentFactory.SELF_GOAL)) {
					t2Goals += 1;
				}
			} else if (t2PlayerIds.contains(dto.getPlayer1Id())) {
				if (dto.getIncidentType().contains(IncidentFactory.GOAL_SCORED)) {
					t2Goals += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.CORNER)) {
					t2Corners += 1;
				}else if (dto.getIncidentType().contains("Foul")) {
					t2Fouls += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.YELLOW_CARD)) {
					t2YellowCards += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.RED_CARD)) {
					t2RedCards += 1;
				}else if (dto.getIncidentType().toUpperCase().contains((IncidentFactory.GOAL_SAVED).toUpperCase())) {
					t2GoalsSaved += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.OFFSIDE)) {
					t2offSides += 1;
				}else if (dto.getIncidentType().contains(IncidentFactory.SUBSTITUTE)) {
					teamTwoSubstitutePlayers.add(populateAndGetSubstituteFB(dto, playerIdDtoMap));					
				}else if (dto.getIncidentType().contains(IncidentFactory.SELF_GOAL)) {
					t1Goals += 1;
				}
			}
			if (t1PlayerIds.contains(dto.getPlayer1Id())) {
				dto.setPlayer1TeamId(match.getTeamOne());
				dto.setPlayer1TeamName(match.getTeamOneName());
				dto.setPlayer1TeamLogoPath(match.getT1_logo_file_path());
			} else if (t2PlayerIds.contains(dto.getPlayer1Id())) {
				dto.setPlayer1TeamId(match.getTeamTwo());
				dto.setPlayer1TeamName(match.getTeamTwoName());
				dto.setPlayer1TeamLogoPath(match.getT2_logo_file_path());
			}
			if (dto.getPlayer2Id() > 0) {
				if (t1PlayerIds.contains(dto.getPlayer2Id())) {
					dto.setPlayer2TeamId(match.getTeamOne());
					dto.setPlayer2TeamName(match.getTeamOneName());
					dto.setPlayer2TeamLogoPath(match.getT1_logo_file_path());
				} else if (t2PlayerIds.contains(dto.getPlayer2Id())) {
					dto.setPlayer2TeamId(match.getTeamTwo());
					dto.setPlayer2TeamName(match.getTeamTwoName());
					dto.setPlayer2TeamLogoPath(match.getT2_logo_file_path());
				}
			}
			dto.setTeam1Goals(t1Goals);
			dto.setTeam2Goals(t2Goals);
			if (dto.getIncidentType().contains(IncidentFactory.GOAL_SCORED)) {
				if(dto.getPlayer1TeamId() == match.getTeamOne()) {
					t1GoalIncidents.add(getGoalRes(playerIdDtoMap, dto));
				}else {
					t2GoalIncidents.add(getGoalRes(playerIdDtoMap, dto));
				}
			}
			if (dto.getIncidentType().contains(IncidentFactory.SELF_GOAL)) {
				if(dto.getPlayer1TeamId() == match.getTeamOne()) {
					t2GoalIncidents.add(getGoalRes(playerIdDtoMap, dto));
				}else {
					t1GoalIncidents.add(getGoalRes(playerIdDtoMap, dto));
				}
			}
		}
		
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");		
		Format monthFormat = new SimpleDateFormat("MMM");
		
	    String dateStr = match.getMatchDate();
	    Date date = formatter.parse(dateStr);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    String monthName = monthFormat.format(date);
	    String dayNames[] = new DateFormatSymbols().getWeekdays();  
	    
	    String matchDate = dayNames[calendar.get(Calendar.DAY_OF_WEEK)] +", "+ calendar.get(Calendar.DAY_OF_MONTH)+" "+monthName+", "+calendar.get(Calendar.YEAR);
	    FixtureDto fixture = FixturesFactory.getFixtureForMatch(matchId, clubId);
	    String matchTime = "";
	    if(fixture != null) {
	    	matchTime = fixture.getTime();
	    }
		
		values.put("seriesName", match.getSeriesName());
		values.put("matchId", String.valueOf(matchId));
		values.put("matchDate", matchDate);
		values.put("matchTime", matchTime);
		values.put("liveYouTubeLink", match.getLive_streaming_link());
		values.put("teamOneName", match.getTeamOneName());
		values.put("teamTwoName", match.getTeamTwoName());
		values.put("location", !CommonUtility.isNullOrEmptyOrNULL(match.getLocation())?match.getLocation():"");
		values.put("teamOneLogo", match.getT1_logo_file_path());
		values.put("teamTwoLogo", match.getT2_logo_file_path());
		values.put("firstHalfStartTime", firstHalfStartTime);
		values.put("secondHalfStartTime", secondHalfStartTime);
		
		values.put("t1Goals", t1Goals);
		values.put("t1Corners", t1Corners);
		values.put("t1Fouls", t1Fouls);
		values.put("t1RedCards", t1RedCards);
		values.put("t1YellowCards", t1YellowCards);
		values.put("t1GoalsSaved", t1GoalsSaved);
		values.put("t1offSides", t1offSides);
		
		values.put("t2Goals", t2Goals);
		values.put("t2Corners", t2Corners);
		values.put("t2Fouls", t2Fouls);
		values.put("t2RedCards", t2RedCards);
		values.put("t2YellowCards", t2YellowCards);
		values.put("t2GoalsSaved", t2GoalsSaved);
		values.put("t2offSides", t2offSides);
		values.put("t1Players", teamOnePlayers);
		values.put("t2Players", teamTwoPlayers);
		values.put("t1GoalIncidents", t1GoalIncidents);
		values.put("t2GoalIncidents", t2GoalIncidents);
		values.put("teamOneSubstitutePlayers", teamOneSubstitutePlayers);
		values.put("teamTwoSubstitutePlayers", teamTwoSubstitutePlayers);
		values.put("game_time", leagueLite.getGameTime());
		
		
		
		score.setValues(values);

		return score;
	}

	private static TeamGoalDto getGoalRes(Map<Integer, PlayerDto> playerIdDtoMap, IncidentDto dto) {
		
		TeamGoalDto gdto = new TeamGoalDto();
		
		gdto.setPlayerOneName(playerIdDtoMap.get(dto.getPlayer1Id()).getFullName());
		gdto.setPlayerOneTeamName(dto.getPlayer1TeamName());
		gdto.setPlayerOneTeamLogoPath(dto.getPlayer1TeamLogoPath());
		if(dto.getPlayer2Id()>0){
			gdto.setPlayerTwoName(playerIdDtoMap.get(dto.getPlayer2Id()).getFullName());
			gdto.setPlayerTwoTeamName(dto.getPlayer2TeamName());
			gdto.setPlayerTwoTeamLogoPath(dto.getPlayer2TeamLogoPath());
		}
		gdto.setIncidentTime(dto.getMinsOfGame());
		
		return gdto;
	}

	private static PlayerDtoFB populateAndGetPlayerFB(PlayerDto dto) {
		
		PlayerDtoFB pdto = new PlayerDtoFB();
		
		pdto.setPlayerId(dto.getPlayerID());
		pdto.setPlayerName(dto.getFullName());
		pdto.setProfilePicFilePath(dto.getProfilepic_file_path());
		pdto.setPlayingRole(dto.getPlayingRole());
		pdto.setJerseyNum(dto.getJerseyNumber());
		
		return pdto;
	}
	
	private static SubstituteDtoFB populateAndGetSubstituteFB(IncidentDto idto, Map<Integer, PlayerDto> playerIdDtoMap) {
		
		SubstituteDtoFB pdto = new SubstituteDtoFB();
		
		if(playerIdDtoMap.containsKey(idto.getPlayer1Id())) {
			
			PlayerDto dto1 = playerIdDtoMap.get(idto.getPlayer1Id());
			
			pdto.setPlayer1Id(dto1.getPlayerID());
			pdto.setPlayer1Name(dto1.getFullName());
			pdto.setPlayer1ProfilePicPath(dto1.getProfilepic_file_path());
			pdto.setPlayer1PlayingRole(dto1.getPlayingRole());
			pdto.setPlayer1JerseyNum(dto1.getJerseyNumber());
		}
		if(playerIdDtoMap.containsKey(idto.getPlayer2Id())) {
			
			PlayerDto dto2 = playerIdDtoMap.get(idto.getPlayer2Id());			
			
			pdto.setPlayer2Id(dto2.getPlayerID());
			pdto.setPlayer2Name(dto2.getFullName());
			pdto.setPlayer2ProfilePicPath(dto2.getProfilepic_file_path());
			pdto.setPlayer2PlayingRole(dto2.getPlayingRole());
			pdto.setPlayer2JerseyNum(dto2.getJerseyNumber());
		}
		pdto.setIncidentTime(idto.getMinsOfGame());
		
		return pdto;
	}

	private static void scorePartnerShip(int clubId, int matchId, int viewId, LiveScoreOverlayBean score) {
		
		MatchDto match;
		try {
			match = MatchesFactory.getMatch(matchId, clubId);
			List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
			Map<String, Object> records = CommonLogic.consolidateMatchRecords(balls, match);
			score.setValues(records);
			
		} catch (Exception e) {
			
		}
	}

	private static void pointsTableOverLay(int clubId, int matchId, int viewId, LiveScoreOverlayBean score) throws Exception {
		score.setView(20);
		String league = null;
		MatchDto matchDto = MatchesFactory.getMatch(matchId, clubId);
		league = String.valueOf(matchDto.getLeagueId());
		DecimalFormat df = new DecimalFormat("#.##");
		List<TeamDto> teams = TeamFactory.getPointsTable(league, clubId);
		if(!CommonUtility.isListNullEmpty(teams)) {
			for(TeamDto t1 : teams) {
				t1.setNetRunRate(CommonUtility.stringToDouble(df.format(t1.getNetRunRate())));
			}
		}
		List<TeamDto> superTeams = TeamFactory.getPointsTableForSuperLeague(league, clubId);
		if(!CommonUtility.isListNullEmpty(superTeams)) {
			for(TeamDto t2 : superTeams) {
				t2.setNetRunRate(CommonUtility.stringToDouble(df.format(t2.getNetRunRate())));
			}
		}
		LeagueDto series = LeagueFactory.getLeague(CommonUtility.stringToInt(league), clubId);
		String sortingOrder=null;
		sortingOrder=LeagueFactory.getSortingOrder(series.getLeagueId()+"", clubId);
		CommonLogic.calculatePointstable(clubId, series, teams, sortingOrder);
		List<GroupBean> groups = new CommonHelper().createGroupsFromTeams(teams, series, "Group ", clubId);
		if (!CommonUtility.isListNullEmpty(superTeams)) {
			if (clubId == 8279) {
				for (TeamDto superTeam : superTeams) {
					superTeam.setGroup(1);
				}
			}
			CommonLogic.calculatePointstable(clubId, series, superTeams, sortingOrder);
			groups.addAll(new CommonHelper().createGroupsFromTeams(superTeams, series, "Super Group ", clubId));
		}
		Map values = new HashMap<>();
		values.put("groups", groups);
		values.put("teams", teams);
		String liveYouTubeLink = CommonUtility.getYouTubeLink(matchDto.getLive_streaming_link().trim());;
		values.put("liveYouTubeLink", liveYouTubeLink);	
		score.setValues(values);
	}
	
	public static void careerStatsOverlay(int clubId, int matchId, int viewId, LiveScoreOverlayBean score) throws Exception {
		
		BallByBallBean ballByBallBean = CommonLogic.prepareBallByBall(matchId, clubId);
		Map<String, Object> values = new HashMap<String, Object>();
		String batsman1Name = "";
		String batsman2Name = "";
		String batsman1ProfileImange = "";
		String batsman2ProfileImange = "";
		String batsman1Runs = "";
		String batsman2Runs = "";
		String batsman1Balls = "";
		String batsman2Balls = "";
		String batsman1Fours = "";
		String batsman1Sixers = "";
		String batsman2Fours = "";
		String batsman2Sixers = "";
		int totalOvers = -1;
		String bowlerName = "";
		String bowlerProfileImange = "";
		String bowlerRuns = "";
		String bowlerWickets = "";
		String bowlerOvers = "";
		String bowlerMaidens = "";
		String t1Code = "";
		String t2Code = "";
		String t1Name = "";
		String t2Name = "";
		String t1Total = "";
		String t2Total = "";
		String t1Wickets = "";
		String t2Wickets = "";
		String t1Overs = "";
		String t2Overs = "";
		String t1RR = "";
		String t2RR = "";
		String RRR = "";
		String requiredRuns = "";
		String remainingOvers = "";
		String firstLogo = "";
		String secondLogo = "";
		String isMatchEnded = "";
		String result = "";
		String shortResult = "";
		String batsman1IsOut = "0";
		String batsman2IsOut = "0";
		String tossWon = "";
		String batsman1OutString = "";
		String batsman2OutString = "";
		float t2TargetOvers = 0;
		int t2TargetRuns = 0;
		boolean isSecondInningsStarted = false;
		String liveYouTubeLink = "";

		List<String> bowlerBalls = new ArrayList<String>();
		MatchDto match = ballByBallBean.getMatchInfo();
		List<BattingDto> team1Batting = null;
		List<BattingDto> team2Batting = null;
		
		try {
			team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(), match.getBattingFirst(), clubId);
			team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(), match.getBattingSecond(), clubId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		Map<String, String> playerMap = PlayerUtility.createPlayerMap(team1Batting,  team2Batting,clubId);
		ClubDto club = ClubFactory.getClub(clubId);
		int firstNameFirst = 0;
		if(club != null){
			firstNameFirst = club.getFirstNameFirst();
		}
		if (ballByBallBean.getLatestBatting() != null && ballByBallBean.getLatestBatting().size() > 0) {
			BattingDto latestBatting0 = ballByBallBean.getLatestBatting().get(0);
			if (latestBatting0 != null) {
				if (clubId == 6745) {
					batsman1Name = latestBatting0.getPlayerShortNameForT20InterpoFest();
				} else {
					batsman1Name = latestBatting0.getPlayerShortName(firstNameFirst) + ".";
					batsman1IsOut = latestBatting0.getIsOut();
					batsman1OutString = latestBatting0.getOutStringCustomReq(playerMap);
				}
				batsman1ProfileImange = latestBatting0.getProfilepic_file_path();
				batsman1Runs = "" + latestBatting0.getRunsScored();
				batsman1Balls = "" + latestBatting0.getBallsFaced();
				batsman1Fours = "" + latestBatting0.getFours();
				batsman1Sixers = "" + latestBatting0.getSixers();
			}
		}

		if (ballByBallBean.getLatestBatting() != null && ballByBallBean.getLatestBatting().size() > 1) {
			BattingDto latestBatting1 = ballByBallBean.getLatestBatting().get(1);
			if (latestBatting1 != null) {
				if (clubId == 6745) {
					batsman2Name = latestBatting1.getPlayerShortNameForT20InterpoFest();
				} else {
					batsman2Name = latestBatting1.getPlayerShortName(firstNameFirst) + ".";
					batsman2IsOut = latestBatting1.getIsOut();
					batsman2OutString = latestBatting1.getOutStringCustomReq(playerMap);
				}
				batsman2ProfileImange = latestBatting1.getProfilepic_file_path();
				batsman2Runs = "" + latestBatting1.getRunsScored();
				batsman2Balls = "" + latestBatting1.getBallsFaced();
				batsman2Fours = "" + latestBatting1.getFours();
				batsman2Sixers = "" + latestBatting1.getSixers();
			}
		}
		
		String lastOutName = "";
		String lastOutIsOut = "";
		String lastOutString = "";
		String lastOutProfileImange = "";
		String lastOutRuns = "";
		String lastOutBalls = "";
		String lastOutFours = "";
		String lastOutSixers = "";
		
		if (ballByBallBean.getLastOutPlayer() != null && ballByBallBean.getLastOutPlayer().getPlayerID() > 0) {
			BattingDto lastOutPlayer1 = ballByBallBean.getLastOutPlayer();
			if (lastOutPlayer1 != null) {
				if (clubId == 6745) {
					lastOutName = lastOutPlayer1.getPlayerShortNameForT20InterpoFest();
				} else {
					lastOutName = lastOutPlayer1.getPlayerShortName(firstNameFirst) + ".";
					lastOutIsOut = lastOutPlayer1.getIsOut();
					lastOutString = lastOutPlayer1.getOutStringCustomReq(playerMap);
				}
				lastOutProfileImange = lastOutPlayer1.getProfilepic_file_path();
				lastOutRuns = "" + lastOutPlayer1.getRunsScored();
				lastOutBalls = "" + lastOutPlayer1.getBallsFaced();
				lastOutFours = "" + lastOutPlayer1.getFours();
				lastOutSixers = "" + lastOutPlayer1.getSixers();
			}
		}

		if (ballByBallBean.getLatestBowling() != null && ballByBallBean.getLatestBowling().size() > 0) {
			BowlingDto latestBowling0 = ballByBallBean.getLatestBowling().get(0);
			if(latestBowling0 != null) {
				if(clubId == 6745){
					bowlerName = latestBowling0.getPlayerShortNameForT20InterpoFest();
				}
				else{
					bowlerName = latestBowling0.getPlayerShortName(firstNameFirst)+ ".";}
					bowlerProfileImange = latestBowling0.getProfilepic_file_path();
					bowlerRuns = latestBowling0.getRuns()+ "";
					bowlerWickets = latestBowling0.getWickets() + "";
					bowlerOvers = latestBowling0.getOvers();
					bowlerMaidens = latestBowling0.getMaidens() + "";
				}
			}
		if (ballByBallBean.getTeam4Balls() != null
				&& ballByBallBean.getTeam4Balls().size() > 0) {
			BallBean lastBall = ballByBallBean.getTeam4Balls().get(0);
			for (BallBean ball : ballByBallBean.getTeam4Balls()) {
				if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
						&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
					bowlerBalls.add(ball.getRunsDisplay());
				}
			}

		} else if (ballByBallBean.getTeam3Balls() != null
				&& ballByBallBean.getTeam3Balls().size() > 0) {
			BallBean lastBall = ballByBallBean.getTeam3Balls().get(0);
			for (BallBean ball : ballByBallBean.getTeam3Balls()) {
				if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
						&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
					bowlerBalls.add(ball.getRunsDisplay());
				}
			}

		} else if (ballByBallBean.getTeam2Balls() != null
				&& ballByBallBean.getTeam2Balls().size() > 0) {
			BallBean lastBall = ballByBallBean.getTeam2Balls().get(0);
			for (BallBean ball : ballByBallBean.getTeam2Balls()) {
				if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
						&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
					bowlerBalls.add(ball.getRunsDisplay());
				}
			}

		} else if (ballByBallBean.getTeam1Balls() != null
				&& ballByBallBean.getTeam1Balls().size() > 0) {
			BallBean lastBall = ballByBallBean.getTeam1Balls().get(0);
			for (BallBean ball : ballByBallBean.getTeam1Balls()) {
				if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
						&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL)) {					
					bowlerBalls.add(ball.getRunsDisplay());
				}
			}
		}
		Collections.reverse(bowlerBalls);

	
		if (match != null) {
			t1Code = match.getTeamOneCode();
			t2Code = match.getTeamTwoCode();
			t1Name = match.getTeamOneName();
			t2Name = match.getTeamTwoName();
			t1Total = match.getT1total() + "";
			t2Total = match.getT2total() + "";
			t1Wickets = match.getT1wickets() + "";
			t2Wickets = match.getT2wickets() + "";
			t1Overs = match.getT1overs() + "";
			t2Overs = match.getT2overs() + "";
			totalOvers = match.getOvers();
			liveYouTubeLink = CommonUtility.getYouTubeLink(match.getLive_streaming_link().trim());
			/*if(match.getTossWon() == 1) tossWon = match.getTeamOneName();
			else tossWon = match.getTeamOneName();*/
			

			int ballsInOver = 6;
			
			if("100b".equalsIgnoreCase(match.getSeriesType())) {
				ballsInOver = 5;
			}
			
			if (match.getTossWon() > 0) {
                tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
			} else {
				tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
			}
		    t2TargetOvers = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2RevisedOvers() : match.getOvers();
            t2TargetRuns = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2Target() : match.getT1total()+1;     
			t1RR = CommonUtility.calculateRunRate(match.getT1total(),
					(double) (match.getT1balls()) / ballsInOver);
			t2RR = CommonUtility.calculateRunRate(match.getT2total(),
					(double) (match.getT2balls()) / ballsInOver);
			RRR = CommonUtility.calculateRunRate(
					t2TargetRuns - match.getT2total(),
					(double) ( CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver) - match.getT2balls()) / ballsInOver);
			requiredRuns = "" + (t2TargetRuns - match.getT2total());
			remainingOvers = CommonUtility.ballsToOvers(CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver) - match.getT2balls(), ballsInOver);
			isSecondInningsStarted = match.isSecondInningsStarted();
			firstLogo = CommonUtility
					.getTeamLogoPath(isSecondInningsStarted ? match
							.getT2_logo_file_path() : match
							.getT1_logo_file_path());
			secondLogo = CommonUtility
					.getTeamLogoPath(isSecondInningsStarted ? match
							.getT1_logo_file_path() : match
							.getT2_logo_file_path());
			t1Code = CommonUtility.trimTeamName(
					match.getFirstBattingTeamName(), t1Code);
			t2Code = CommonUtility.trimTeamName(
					match.getSecondBattingTeamName(), t2Code);
			isMatchEnded = match.getIsComplete()+"";
			result = match.getResult();
			if(!CommonUtility.isNullOrEmpty(result) && result.contains("ov)")) {
				result = result.replace("ov)", "OVERS)");
			}
			shortResult = match.getResultForTitle();
			if(match.getTossWon() > 0) {
				values.put("toss", ((match.getTossWon() == match.getTeamOne())? match.getTeamOneName():match.getTeamTwoName())+ " WON THE TOSS AND ELECTED TO " + ((match.getTossWon()==match.getBattingFirst())?"BAT":"BOWL" ));
			}else {
				values.put("toss", "");
			}
		}
		values.put("totalOvers", totalOvers);
		values.put("tossWon", tossWon);
		values.put("batsman1OutString", batsman1OutString);
		values.put("batsman2OutString", batsman2OutString);
		values.put("lastOutString", lastOutString);
		values.put("batsman1Name", batsman1Name);
		values.put("batsman2Name", batsman2Name);
		values.put("lastOutName", lastOutName);
		values.put("batsman1ProfileImange", CommonUtility.isNullOrEmptyOrNULL(batsman1ProfileImange) ? "/documentsRep/profilePics/no_image.png":batsman1ProfileImange);
		values.put("batsman2ProfileImange", CommonUtility.isNullOrEmptyOrNULL(batsman2ProfileImange) ? "/documentsRep/profilePics/no_image.png":batsman2ProfileImange);
		values.put("lastOutProfileImange", lastOutProfileImange);
		values.put("batsman1Runs", batsman1Runs);
		values.put("lastOutRuns", lastOutRuns);
		values.put("batsman2Runs", batsman2Runs);
		values.put("batsman1IsOut", batsman1IsOut);
		values.put("lastOutIsOut", lastOutIsOut);
		values.put("batsman2IsOut", batsman2IsOut);
		values.put("batsman1Balls", batsman1Balls);
		values.put("lastOutBalls", lastOutBalls);
		values.put("batsman2Balls", batsman2Balls);
		values.put("batsman1Fours", batsman1Fours);
		values.put("lastOutFours", lastOutFours);
		values.put("batsman1Sixers", batsman1Sixers);
		values.put("lastOUtSixers", lastOutSixers);
		values.put("batsman2Fours", batsman2Fours);
		values.put("batsman2Sixers", batsman2Sixers);
		values.put("bowlerName", bowlerName);
		values.put("bowlerProfileImange", CommonUtility.isNullOrEmptyOrNULL(bowlerProfileImange) ? "/documentsRep/profilePics/no_image.png":bowlerProfileImange);
		values.put("bowlerRuns", bowlerRuns);
		values.put("bowlerWickets", bowlerWickets);
		values.put("bowlerOvers", bowlerOvers);
		values.put("bowlerMaidens", bowlerMaidens);
		values.put("t1Code", t1Code);
		values.put("t2Code", t2Code);
		values.put("t1Name", t1Name);
		values.put("t2Name", t2Name);
		values.put("t1Total", t1Total);
		values.put("t2Total", t2Total);
		values.put("t1Wickets", t1Wickets);
		values.put("t2Wickets", t2Wickets);
		values.put("t1Overs", t1Overs);
		values.put("t2Overs", t2Overs);
		values.put("t1RR", t1RR);
		values.put("t2RR", t2RR);
		values.put("RRR", RRR);
		values.put("requiredRuns", requiredRuns);
		values.put("remainingOvers", remainingOvers);
		values.put("firstLogo", firstLogo);
		values.put("secondLogo", secondLogo);
		values.put("isSecondInningsStarted", isSecondInningsStarted + "");
		values.put("matchId", "" + matchId);
		values.put("isMatchEnded", isMatchEnded);
		values.put("result", result);
		values.put("shortResult", shortResult);
		values.put("liveYouTubeLink", liveYouTubeLink);		
		if(isSecondInningsStarted){
			values.put("tTotal", t2Total);
			values.put("tWickets", t2Wickets);
			values.put("overs", t2Overs);
			values.put("runrate", t2RR);
			values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+t1Code+"</span> "+" " + t1Total + "/" + t1Wickets + "<span class='numberColorClass'> Overs</span> " + t1Overs);
			values.put("showMsgForScoreNeeded", "<span class='numberColorClass'>NEED</span> " + requiredRuns + " <span class='numberColorClass'>FROM</span> " + remainingOvers + " <span class='numberColorClass'>OVERS</span> " + RRR + " <span class='numberColorClass'>RRR</span>");
		}else{
			values.put("tTotal", t1Total);
			values.put("tWickets", t1Wickets);
			values.put("overs", t1Overs);
			values.put("runrate", t1RR);
			values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" vs " + "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> ");
		}
		score.setView(viewId);
		score.setValues(values);
		score.setSecondInningsStarted(isSecondInningsStarted);
		score.setBalls(bowlerBalls);

		// for in progress 2X games
		if (match != null && match.getClubID() != 0 && match.getLeagueId() != 0) {
			int leagueId = match.getLeagueId();
			try {
				//ClubDto club = ClubFactory.getClub(match.getClubID());
				// TODO : Leaving as of now since this page should have clubObject. if not we should not access this code. Expecting not null club.
				LeagueDto leagueObj = club.getLeague(leagueId);
				if (leagueObj != null && "2X".equals(leagueObj.getSeriesType())) {
					score.setIs2XCricket(true);
				}
				
				if(leagueObj != null && "Test".equalsIgnoreCase(leagueObj.getSeriesType())) {
					populateDataForTestMatch(values, match, score);
				}
				
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	public static void scoreOverlay(int clubId, int matchId, int viewId, 
			LiveScoreOverlayBean score) throws Exception {
		
		BallByBallBean ballByBallBean = CommonLogic.prepareBallByBall(matchId, clubId);
		
		Map<String, Object> values = new HashMap<String, Object>();
		String batsman1Name = "";
		String batsman2Name = "";
		String batsman1ProfileImange = "";
		String batsman2ProfileImange = "";
		String batsman1Runs = "";
		String batsman2Runs = "";
		String batsman1Balls = "";
		String batsman2Balls = "";
		String batsman1Fours = "";
		String batsman1Sixers = "";
		String batsman2Fours = "";
		String batsman2Sixers = "";
		int totalOvers = -1;
		String bowlerName = "";
		String bowlerProfileImange = "";
		String bowlerRuns = "";
		String bowlerWickets = "";
		String bowlerOvers = "";
		String bowlerMaidens = "";
		String t1Code = "";
		String t2Code = "";
		String t1Name = "";
		String t2Name = "";
		String t1Total = "";
		String t2Total = "";
		String t1Wickets = "";
		String t2Wickets = "";
		String t1Overs = "";
		String t2Overs = "";
		String t1RR = "";
		String t2RR = "";
		String RRR = "";
		String requiredRuns = "";
		String remainingOvers = "";
		String firstLogo = "";
		String secondLogo = "";
		String isMatchEnded = "";
		String result = "";
		String shortResult = "";
		String batsman1IsOut = "0";
		String batsman2IsOut = "0";
		String tossWon = "";
		String batsman1OutString = "";
		String batsman2OutString = "";
		String careerPlayerName = "";
		String careerProfileImage = "";
		String manOfTheMatch = "";
		String momImagePath = "";
		String seriesName ="";
		String revisedTarget = "";
		String oversOrBalls = "OVERS ";
		String rrOrRPB = "RR ";
		
		float t2TargetOvers = 0;
		int t2TargetRuns = 0;
		boolean isSecondInningsStarted = false;
		boolean isSuperOver = false;
		boolean isSuperOverSecondInningsStarted = false;
		String liveYouTubeLink = "";
		Map<String, String> careerStats = new HashMap<String, String>();

		List<String> bowlerBalls = new ArrayList<String>();
		MatchDto match = ballByBallBean.getMatchInfo();
		List<BattingDto> team1Batting = null;
		List<BattingDto> team2Batting = null;
		Map<String, OverDto> overMap = null;		
		Map<String, OverDto> superOverMap = null;
		
		if(ballByBallBean != null) {
			superOverMap = ballByBallBean.getSuperOverMap();
			if(superOverMap != null && superOverMap.size()>0) {
				isSuperOver = true;
			}
		}
		if(match != null && match.getSuperOverCurrentInnings()>0) {
			isSuperOver = true;
			if(match.getSuperOverCurrentInnings()>=2) {
				isSuperOverSecondInningsStarted = true;
			}
		}
		int superOverInningsNum1 = ballByBallBean.getSuperOverInningsNum1();
		int superOverInningsNum2 = ballByBallBean.getSuperOverInningsNum2();
		
		int ballInOver = 6;
		if(match != null && "100b".equalsIgnoreCase(match.getSeriesType())) {
			ballInOver = 5;
		}
		
		try {
			team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(), match.getBattingFirst(), clubId);
			team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(), match.getBattingSecond(), clubId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		Map<String, String> playerMap = PlayerUtility.createPlayerMap(team1Batting,  team2Batting,clubId);
		ClubDto club = ClubFactory.getClub(clubId);
		int firstNameFirst = 0;
		if(club != null){
			firstNameFirst = club.getFirstNameFirst();
		}
		BattingDto latestBatting = null; 
		
		if (ballByBallBean.getLatestBatting() != null && ballByBallBean.getLatestBatting().size() > 0) {
			latestBatting = ballByBallBean.getLatestBatting().get(0);
			if (latestBatting != null) {
				if (clubId == 6745) {
					batsman1Name = latestBatting.getPlayerShortNameForT20InterpoFest();
				} else if (clubId == 12047 && !CommonUtility.isNullOrEmpty(latestBatting.getNickName())) {
					batsman1Name = latestBatting.getNickName();
				} else {
					batsman1Name = latestBatting.getPlayerShortName(firstNameFirst) + ".";
				}
				batsman1IsOut = latestBatting.getIsOut();
				batsman1OutString = latestBatting.getOutStringCustomReq(playerMap);
				batsman1ProfileImange = latestBatting.getProfilepic_file_path();
				batsman1Runs = "" + latestBatting.getRunsScored();
				batsman1Balls = "" + latestBatting.getBallsFaced();
				batsman1Fours = "" + latestBatting.getFours();
				batsman1Sixers = "" + latestBatting.getSixers();
				
				if(viewId == 24 ) {
					careerStats = getCareerStatsForPlayer(latestBatting.getPlayerID(), clubId,0, viewId);
					careerPlayerName = batsman1Name;
					careerProfileImage = batsman1ProfileImange;
				}else if(viewId == 36) {
					careerStats = getCareerStatsForPlayer(latestBatting.getPlayerID(), clubId,match.getLeagueId(), viewId);
					careerPlayerName = batsman1Name;
					careerProfileImage = batsman1ProfileImange;
				}
			}
		}
		BattingDto runner = null;
		if (ballByBallBean.getLatestBatting() != null && ballByBallBean.getLatestBatting().size() > 1) {
			runner = ballByBallBean.getLatestBatting().get(1);
			if (runner != null) {
				if (clubId == 6745) {
					batsman2Name = runner.getPlayerShortNameForT20InterpoFest();
				} else if (clubId == 12047 && !CommonUtility.isNullOrEmpty(runner.getNickName())) {
					batsman2Name = runner.getNickName();
				} else {
					batsman2Name = runner.getPlayerShortName(firstNameFirst) + ".";
				}
				batsman2IsOut = runner.getIsOut();
				batsman2OutString = runner.getOutStringCustomReq(playerMap);
				batsman2ProfileImange = runner.getProfilepic_file_path();
				batsman2Runs = "" + runner.getRunsScored();
				batsman2Balls = "" + runner.getBallsFaced();
				batsman2Fours = "" + runner.getFours();
				batsman2Sixers = "" + runner.getSixers();
				if(viewId == 25) {
					careerStats = getCareerStatsForPlayer(runner.getPlayerID(), clubId, 0, viewId);
					careerPlayerName = batsman2Name;
					careerProfileImage = batsman2ProfileImange;
				}else if(viewId == 37) {
					careerStats = getCareerStatsForPlayer(runner.getPlayerID(), clubId,match.getLeagueId(), viewId);
					careerPlayerName = batsman2Name;
					careerProfileImage = batsman2ProfileImange;
				}
			}
		}		
		
		String lastOutName = "";
		String lastOutIsOut = "";
		String lastOutString = "";
		String lastOutProfileImange = "";
		String lastOutRuns = "";
		String lastOutBalls = "";
		String lastOutFours = "";
		String lastOutSixers = "";
		
		if (ballByBallBean.getLastOutPlayer() != null && ballByBallBean.getLastOutPlayer().getPlayerID() > 0) {
			BattingDto lastOutPlayer1 = ballByBallBean.getLastOutPlayer();
			if (lastOutPlayer1 != null) {
				if (clubId == 6745) {
					lastOutName = lastOutPlayer1.getPlayerShortNameForT20InterpoFest();
				} else if (clubId == 12047 && !CommonUtility.isNullOrEmpty(lastOutPlayer1.getNickName())) {
					lastOutName = lastOutPlayer1.getNickName();
				} else {
					lastOutName = lastOutPlayer1.getPlayerShortName(firstNameFirst) + ".";
				}
				
				lastOutIsOut = lastOutPlayer1.getIsOut();
				lastOutString = lastOutPlayer1.getOutStringCustomReq(playerMap);
				lastOutProfileImange = lastOutPlayer1.getProfilepic_file_path();
				lastOutRuns = "" + lastOutPlayer1.getRunsScored();
				lastOutBalls = "" + lastOutPlayer1.getBallsFaced();
				lastOutFours = "" + lastOutPlayer1.getFours();
				lastOutSixers = "" + lastOutPlayer1.getSixers();
				if(viewId == 27 ) {
					careerStats = getCareerStatsForPlayer(lastOutPlayer1.getPlayerID(), clubId,0, viewId);
					careerPlayerName = lastOutName;
					careerProfileImage = lastOutProfileImange;
				}else if(viewId == 39) {
					careerStats = getCareerStatsForPlayer(lastOutPlayer1.getPlayerID(), clubId,match.getLeagueId(), viewId);
					careerPlayerName = lastOutName;
					careerProfileImage = lastOutProfileImange;
				}
					
			}
		}
		
		if (ballByBallBean.getLatestBowling() != null && ballByBallBean.getLatestBowling().size() > 0) {
			BowlingDto latestBowling0 = ballByBallBean.getLatestBowling().get(0);
			if(latestBowling0 != null) {
				if(clubId == 6745){
					bowlerName = latestBowling0.getPlayerShortNameForT20InterpoFest();
				}else if (clubId == 12047 && !CommonUtility.isNullOrEmpty(latestBowling0.getNickName())) {
					bowlerName = latestBowling0.getNickName();
				} 
				else{
					bowlerName = latestBowling0.getPlayerShortName(firstNameFirst)+ ".";
				}
					bowlerProfileImange = latestBowling0.getProfilepic_file_path();
					bowlerRuns = latestBowling0.getRuns()+ "";
					bowlerWickets = latestBowling0.getWickets() + "";
					bowlerOvers = latestBowling0.getOvers(ballInOver);
					bowlerMaidens = latestBowling0.getMaidens() + "";
					if(viewId == 26) {
						careerStats = getCareerStatsForPlayer(latestBowling0.getPlayerID(), clubId, 0,viewId);
						careerPlayerName = bowlerName;
						careerProfileImage = bowlerProfileImange;
					}else if(viewId == 38) {
						careerStats = getCareerStatsForPlayer(latestBowling0.getPlayerID(), clubId,match.getLeagueId(), viewId);
						careerPlayerName = bowlerName;
						careerProfileImage = bowlerProfileImange;
						
					}
				}
			}
		
		if(isSuperOver) {
			
			if (ballByBallBean.getTeam2SuperBalls() != null
					&& ballByBallBean.getTeam2SuperBalls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam2SuperBalls().get(0);
				for (BallBean ball : ballByBallBean.getTeam2SuperBalls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}

			} else if (ballByBallBean.getTeam1SuperBalls() != null
					&& ballByBallBean.getTeam1SuperBalls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam1SuperBalls().get(0);
				for (BallBean ball : ballByBallBean.getTeam1SuperBalls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL)) {					
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}
			}
			
		}else {
			if (ballByBallBean.getTeam4Balls() != null
					&& ballByBallBean.getTeam4Balls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam4Balls().get(0);
				for (BallBean ball : ballByBallBean.getTeam4Balls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}

			} else if (ballByBallBean.getTeam3Balls() != null
					&& ballByBallBean.getTeam3Balls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam3Balls().get(0);
				for (BallBean ball : ballByBallBean.getTeam3Balls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}

			} else if (ballByBallBean.getTeam2Balls() != null
					&& ballByBallBean.getTeam2Balls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam2Balls().get(0);
				for (BallBean ball : ballByBallBean.getTeam2Balls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL) ) {
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}

			} else if (ballByBallBean.getTeam1Balls() != null
					&& ballByBallBean.getTeam1Balls().size() > 0) {
				BallBean lastBall = ballByBallBean.getTeam1Balls().get(0);
				for (BallBean ball : ballByBallBean.getTeam1Balls()) {
					if (ball.getOver() == lastBall.getOver() && !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL) 
							&& !ball.getBallType().equalsIgnoreCase(CommonLogic.BALL_TYPE_NO_COUNT_BALL)) {					
						bowlerBalls.add(ball.getRunsDisplay());
					}
				}
			}
			Collections.reverse(bowlerBalls);
		}
		
		if (match != null) {
			
			int ballsInOver = 6;			
			if("100b".equalsIgnoreCase(match.getSeriesType())) {
				ballsInOver = 5;
			}
			t1Code = match.getTeamOneCode();
			t2Code = match.getTeamTwoCode();
			t1Name = match.getTeamOneName();
			t2Name = match.getTeamTwoName();
			t1Total = match.getT1total() + "";
			t2Total = match.getT2total() + "";
			t1Wickets = match.getT1wickets() + "";
			t2Wickets = match.getT2wickets() + "";
			t1Overs = match.getT1overs() + "";
			t2Overs = match.getT2overs() + "";
			totalOvers = match.getOvers();
			liveYouTubeLink = CommonUtility.getYouTubeLink(match.getLive_streaming_link().trim());
			seriesName = match.getSeriesName();
			/*if(match.getTossWon() == 1) tossWon = match.getTeamOneName();
			else tossWon = match.getTeamOneName();*/
			if (match.getTossWon() > 0) {
				if (match.getTossWon() == match.getTeamOne()) {
					if(!CommonUtility.isNullOrEmpty(match.getTeamOneCode())){
						tossWon =  match.getTeamOneCode();
					}
					else {
						tossWon =match.getTeamOneName();
					}
				}  else {
					if (match.getTossWon() == match.getTeamTwo()) {
						if(!CommonUtility.isNullOrEmpty(match.getTeamTwoCode())){
							tossWon =  match.getTeamTwoCode();
						}
						else {
							tossWon =match.getTeamTwoName();
						}
					}
				}     
			}
		
		    t2TargetOvers = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2RevisedOvers() : match.getOvers();
            t2TargetRuns = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2Target() : match.getT1total()+1; 
			t1RR = CommonUtility.calculateRunRate(match.getT1total(),
					(double) (match.getT1balls()) / ballsInOver);			
			t2RR = CommonUtility.calculateRunRate(match.getT2total(),
					(double) (match.getT2balls()) / ballsInOver);
			RRR = CommonUtility.calculateRunRate(
					t2TargetRuns - match.getT2total(),
					(double) ( CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver) - match.getT2balls()) / ballsInOver);
			requiredRuns = "" + (t2TargetRuns - match.getT2total());
			remainingOvers = CommonUtility.ballsToOvers(CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver) - match.getT2balls(), ballsInOver);
			isSecondInningsStarted = match.getIsComplete()==1?match.isSecondInningsStarted():match.getCurrentInnings()>=2;
			firstLogo = CommonUtility
					.getTeamLogoPath(isSecondInningsStarted ? match
							.getT2_logo_file_path() : match
							.getT1_logo_file_path());
			secondLogo = CommonUtility
					.getTeamLogoPath(isSecondInningsStarted ? match
							.getT1_logo_file_path() : match
							.getT2_logo_file_path());
			t1Code = CommonUtility.trimTeamName(
					match.getFirstBattingTeamName(), t1Code);
			t2Code = CommonUtility.trimTeamName(
					match.getSecondBattingTeamName(), t2Code);
			isMatchEnded = match.getIsComplete()+"";
			result = match.getResult();
			if("100b".equalsIgnoreCase(match.getSeriesType())) {
				t1Overs = CommonUtility.oversToBalls(match.getT1overs(), ballsInOver)+"";
				t2Overs = CommonUtility.oversToBalls(match.getT2overs(), ballsInOver)+"";
				totalOvers = CommonUtility.oversToBalls(match.getOvers()+"", ballsInOver);
				t2TargetOvers = CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver);
				remainingOvers = CommonUtility.oversToBalls(remainingOvers+"", ballsInOver)+"";
				t1RR = CommonUtility.calculateRPB(match.getT1total(), match.getT1balls());
				t2RR = CommonUtility.calculateRPB(match.getT2total(), match.getT2balls());
				oversOrBalls = "BALLS ";
				rrOrRPB = "RPB ";
			}
			if(!CommonUtility.isNullOrEmpty(result) && result.contains("ov)")) {
				result = result.replace("ov)", oversOrBalls+")");
			}
			if(!CommonUtility.isNullOrEmpty(result) && result.contains("b)")) {
				result = result.replace("b)", oversOrBalls+")");
			}
			shortResult = match.getResultForTitle();
			if(match.getTossWon() > 0) {
				values.put("toss", ((match.getTossWon() == match.getTeamOne())? match.getTeamOneName():match.getTeamTwoName())+ " WON THE TOSS AND ELECTED TO " + ((match.getTossWon()==match.getBattingFirst())?"BAT":"BOWL" ));
			}else {
				values.put("toss", "");
			}
			
			try {
				genratePartnershiMapNew(latestBatting, runner, ballByBallBean.getLastOutPlayer(), 
						ballByBallBean.getPartnershipMap(), club, values, match);
				
			} catch (Exception e) {
				//log.error(e.getMessage(), e);
			}
		}
		
		if(match.getIsComplete() == 1) {
			
			for(BattingDto bdto : team1Batting) {
				if(match.getManOfTheMatch()>0) {
					if(bdto.getPlayerID() == match.getManOfTheMatch()) {						
						manOfTheMatch = bdto.getFirstName()+" "+bdto.getLastName();
						momImagePath = bdto.getProfilepic_file_path();
						break;
					}
				}				
			}
			if( CommonUtility.isNullOrEmpty(manOfTheMatch)) {
				
				for(BattingDto bdto : team2Batting) {
					if(match.getManOfTheMatch()>0) {
						if(bdto.getPlayerID() == match.getManOfTheMatch()) {						
							manOfTheMatch = bdto.getPlayerName();
							momImagePath = bdto.getProfilepic_file_path();
							break;
						}
					}					
				}				
			}			
		}
		if(match.isDls()) {
			revisedTarget = match.getT2Target()+"";
		}
		if(isSuperOver) {
			
			OverDto t1Over = new OverDto();
			OverDto t2Over = new OverDto();
			
			if(superOverInningsNum1 == 1) {
				t1Over = superOverMap.get(superOverInningsNum1+"-0");
				t2Over = superOverMap.get(superOverInningsNum2+"-0");
			}else if(superOverInningsNum1 == 2) {
				t1Over = superOverMap.get(superOverInningsNum2+"-0");
				t2Over = superOverMap.get(superOverInningsNum1+"-0");
			}
			
			String teamCode1 = t1Code;
			String teamCode2 = t2Code;
			t1Overs = "";
			t2Overs = "";
			
			if(t1Over != null) {
				t1Total = t1Over.getMatchTotal()+"";
				t1Wickets = t1Over.getMatchWickets()+"";
				t1RR = CommonUtility.calculateRPB(t1Over.getMatchTotal(), t1Over.getBowler1Balls());
				t1Overs = t1Over.getBowler1Balls()+"";
				
				if(t1Over.getTeamName().equalsIgnoreCase(match.getTeamOneName())) {
					t1Name = match.getTeamOneName();
					t1Code = CommonUtility.trimTeamName(match.getTeamOneName(), teamCode1);
				}else { 
					t1Name = match.getTeamTwoName();
					t1Code = CommonUtility.trimTeamName(match.getTeamTwoName(), teamCode2);
				}
			}
			
			if(t2Over != null) {
				t2Total = t2Over.getMatchTotal()+"";
				t2Wickets = t2Over.getMatchWickets()+"";
				t2RR = CommonUtility.calculateRPB(t2Over.getMatchTotal(), t2Over.getBowler1Balls());
				t2Overs = t2Over.getBowler1Balls()+"";
				
				if(t2Over.getTeamName().equalsIgnoreCase(match.getTeamOneName())) {
					t2Name = match.getTeamOneName();
					t2Code = CommonUtility.trimTeamName(match.getTeamOneName(), teamCode1);
				}else { 
					t2Name = match.getTeamTwoName();
					t2Code =  CommonUtility.trimTeamName(match.getTeamTwoName(), teamCode2);
				}
				t2TargetOvers =1;
				if(t1Over != null) {
					t2TargetRuns = t1Over.getMatchTotal()+1; 
				}
				requiredRuns = "" + (t2TargetRuns - t2Over.getMatchTotal());
				remainingOvers = (CommonUtility.oversToBalls(t2TargetOvers+"", 6) - t2Over.getBowler1Balls())+"";
			}
			oversOrBalls = "BALLS";
			
			String winningTeamCode = "";
			
			if(t1Over != null && t2Over != null) {
				
				if(t1Over.getRuns()>t2Over.getRuns()) {
					winningTeamCode = t1Code;
				}else {
					winningTeamCode = t2Code;
				}
				result = "Match tied. "+ winningTeamCode +" won the super over.";
			}	
		}
		
		values.put("revisedTarget", revisedTarget);
		values.put("seriesName",seriesName);
		values.put("totalOvers", totalOvers);
		values.put("tossWon", tossWon);
		values.put("batsman1OutString", batsman1OutString);
		values.put("batsman2OutString", batsman2OutString);
		values.put("lastOutString", lastOutString);
		values.put("batsman1Name", batsman1Name);
		values.put("batsman2Name", batsman2Name);
		values.put("lastOutName", lastOutName);
		values.put("batsman1ProfileImange", CommonUtility.isNullOrEmptyOrNULL(batsman1ProfileImange) ? "/documentsRep/profilePics/no_image.png":batsman1ProfileImange);
		values.put("batsman2ProfileImange", CommonUtility.isNullOrEmptyOrNULL(batsman2ProfileImange) ? "/documentsRep/profilePics/no_image.png":batsman2ProfileImange);
		values.put("lastOutProfileImange", lastOutProfileImange);
		values.put("batsman1Runs", batsman1Runs);
		values.put("lastOutRuns", lastOutRuns);
		values.put("batsman2Runs", batsman2Runs);
		values.put("batsman1IsOut", batsman1IsOut);
		values.put("lastOutIsOut", lastOutIsOut);
		values.put("batsman2IsOut", batsman2IsOut);
		values.put("batsman1Balls", batsman1Balls);
		values.put("lastOutBalls", lastOutBalls);
		values.put("batsman2Balls", batsman2Balls);
		values.put("batsman1Fours", batsman1Fours);
		values.put("lastOutFours", lastOutFours);
		values.put("batsman1Sixers", batsman1Sixers);
		values.put("lastOUtSixers", lastOutSixers);
		values.put("batsman2Fours", batsman2Fours);
		values.put("batsman2Sixers", batsman2Sixers);
		values.put("bowlerName", bowlerName);
		values.put("bowlerProfileImange", CommonUtility.isNullOrEmptyOrNULL(bowlerProfileImange) ? "/documentsRep/profilePics/no_image.png":bowlerProfileImange);
		values.put("bowlerRuns", bowlerRuns);
		values.put("bowlerWickets", bowlerWickets);
		if("100b".equalsIgnoreCase(match.getSeriesType())) {
			values.put("bowlerOvers", CommonUtility.oversToBalls(bowlerOvers, ballInOver));
		}else {
			values.put("bowlerOvers", bowlerOvers);
		}
		values.put("bowlerMaidens", bowlerMaidens);
		values.put("t1Code", t1Code);
		values.put("t2Code", t2Code);
		values.put("t1Name", t1Name);
		values.put("t2Name", t2Name);
		values.put("t1Total", t1Total);
		values.put("t2Total", t2Total);
		values.put("t1Wickets", t1Wickets);
		values.put("t2Wickets", t2Wickets);
		values.put("t1Overs", t1Overs);
		values.put("t2Overs", t2Overs);
		values.put("t1RR", t1RR);
		values.put("t2RR", t2RR);
		values.put("RRR", RRR);
		values.put("requiredRuns", requiredRuns);
		values.put("remainingOvers", remainingOvers);
		values.put("firstLogo", firstLogo);
		values.put("secondLogo", secondLogo);
		values.put("isSecondInningsStarted", isSecondInningsStarted + "");
		values.put("isSuperOverSecondInningsStarted", isSuperOverSecondInningsStarted + "");
		values.put("isSuperOver", isSuperOver + "");
		values.put("matchId", "" + matchId);
		values.put("isMatchEnded", isMatchEnded);
		values.put("result", result);
		values.put("shortResult", shortResult);
		values.put("liveYouTubeLink", liveYouTubeLink);	
		values.put("manOfTheMatch", manOfTheMatch);	
		values.put("momImagePath", momImagePath);	
		try {
			values.put("totalCareerMatches", careerStats.get("totalCareerMatches"));
			values.put("totalCareerRuns", careerStats.get("totalCareerRuns"));
			values.put("totalCareerWickets", careerStats.get("totalCareerWickets"));
			values.put("careerPlayerName", careerPlayerName);
			values.put("careerProfileImage", careerProfileImage);
			values.put("batingCareerStrikeRate", careerStats.get("batingCareerStrikeRate"));
			values.put("batingCareerAverage", careerStats.get("batingCareerAverage"));
			values.put("bowlingCareerEconomy", careerStats.get("bowlingCareerEconomy"));
			values.put("bowlingCareerAverage", careerStats.get("bowlingCareerAverage"));
			
			
		}catch(Exception e) {
			
		}
		
		if(isSuperOver) {			
			if(isSuperOverSecondInningsStarted){
				values.put("tTotal", t2Total);
				values.put("tWickets", t2Wickets);
				values.put("overs", t2Overs);			
				values.put("runrate", t2RR);
				values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+t1Code+"</span> "+" " + t1Total + "/" + t1Wickets + "<span class='numberColorClass'> "+oversOrBalls+"</span> " + t1Overs);
				String showMsgForScoreNeeded = "";
				if(CommonUtility.stringToInt(requiredRuns)>0) {
					showMsgForScoreNeeded = "<span class='numberColorClass'>NEED</span> " + requiredRuns + " <span class='numberColorClass'>FROM</span> " + remainingOvers + " <span class='numberColorClass'>"+oversOrBalls+"</span> ";
					if(!"100b".equalsIgnoreCase(match.getSeriesType())) {
						showMsgForScoreNeeded += RRR +" <span class='numberColorClass'>RRR</span>";
					}
				}
				values.put("showMsgForScoreNeeded", showMsgForScoreNeeded);
			}else{
				values.put("tTotal", t1Total);
				values.put("tWickets", t1Wickets);
				values.put("overs", t1Overs);
				values.put("runrate", t1RR);
				values.put("showMsgForScoreNeeded", "");
				values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" vs " + "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> ");
			}
			
		}else {
			if(isSecondInningsStarted){
				values.put("tTotal", t2Total);
				values.put("tWickets", t2Wickets);
				values.put("overs", t2Overs);			
				values.put("runrate", t2RR);
				values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+t1Code+"</span> "+" " + t1Total + "/" + t1Wickets + "<span class='numberColorClass'> "+oversOrBalls+"</span> " + t1Overs);
				String showMsgForScoreNeeded = "";
				if(CommonUtility.stringToInt(requiredRuns)>0) {
					showMsgForScoreNeeded = "<span class='numberColorClass'>NEED</span> " + requiredRuns + " <span class='numberColorClass'>FROM</span> " + remainingOvers + " <span class='numberColorClass'>"+oversOrBalls+"</span> ";
					if(!"100b".equalsIgnoreCase(match.getSeriesType())) {
						showMsgForScoreNeeded += RRR +" <span class='numberColorClass'>RRR</span>";
					}
				}
				values.put("showMsgForScoreNeeded", showMsgForScoreNeeded);
			}else{
				values.put("tTotal", t1Total);
				values.put("tWickets", t1Wickets);
				values.put("overs", t1Overs);
				values.put("runrate", t1RR);
				values.put("showScoreMsgForOpositTeam", "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" vs " + "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> ");
			}
		}
		MatchOverlayConfigDto config = null;
		if(viewId == 40)
		{
			
			
			try {
				config = MatchOverlayConfigFactory.getConfig(matchId, clubId);
				values.put("customTextId",config.getAttribute1());
			} catch (Exception e1) {
				// do nothing as the config will be null
			}
		}
		
		values.put("oversOrBalls", oversOrBalls);
		values.put("rrOrRPB", rrOrRPB);
		score.setView(viewId);
		score.setValues(values);
		score.setSecondInningsStarted(isSecondInningsStarted);
		score.setSuperOver(isSuperOver);
		score.setSuperOverSecondInningsStarted(isSuperOverSecondInningsStarted);
		score.setBalls(bowlerBalls);

		// for in progress 2X games
		if (match != null && match.getClubID() != 0 && match.getLeagueId() != 0) {
			int leagueId = match.getLeagueId();
			try {
				//ClubDto club = ClubFactory.getClub(match.getClubID());
				// TODO : Leaving as of now since this page should have clubObject. if not we should not access this code. Expecting not null club.
				LeagueDto leagueObj = club.getLeague(leagueId);
				if (leagueObj != null && "2X".equals(leagueObj.getSeriesType())) {
					score.setIs2XCricket(true);
				}
				
				if(leagueObj != null && "Test".equalsIgnoreCase(leagueObj.getSeriesType())) {
					populateDataForTestMatch(values, match, score);
				}
				
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	private static Map<String, String> genratePartnershiMapNew(BattingDto latestBatting, BattingDto runner, 
			BattingDto lastOutPlayer, Map<String, List<PartnershipDto>> partnershipMap, 
			ClubDto club, Map<String, Object> values, MatchDto match) {
		Map<String, String> currentPartnershipMap = new HashMap<String, String>();
		
		
			if(latestBatting == null) {
				latestBatting = new BattingDto();
			}
			if( runner == null) {
				runner = new BattingDto();
			}
			
				currentPartnershipMap.put("partnershipBatsman1TotalRuns", ""+latestBatting.getRunsScored());
				currentPartnershipMap.put("partnershipBatsman1Balls", ""+latestBatting.getBallsFaced());
				if (club.getClubId() == 12047) {
					if(!CommonUtility.isNullOrEmpty(latestBatting.getNickName())) {
						currentPartnershipMap.put("partnershipBatsman1Name", latestBatting.getNickName());
					}else {
						currentPartnershipMap.put("partnershipBatsman1Name", ""+latestBatting.getPlayerShortName(club.getFirstNameFirst()));
					}
					if(!CommonUtility.isNullOrEmpty(runner.getNickName())) {
						currentPartnershipMap.put("partnershipBatsman2Name", runner.getNickName());
					}else {
						currentPartnershipMap.put("partnershipBatsman2Name", ""+runner.getPlayerShortName(club.getFirstNameFirst()));
					}
					
					
				}else {
					currentPartnershipMap.put("partnershipBatsman1Name", ""+latestBatting.getPlayerShortName(club.getFirstNameFirst()));
					currentPartnershipMap.put("partnershipBatsman2Name", ""+runner.getPlayerShortName(club.getFirstNameFirst()));
				}
				currentPartnershipMap.put("partnershipBatsman1ProfilePic", ""+latestBatting.getProfilepic_file_path());
				
				currentPartnershipMap.put("partnershipBatsman2TotalRuns", ""+runner.getRunsScored());
				currentPartnershipMap.put("partnershipBatsman2Balls", ""+runner.getBallsFaced());
				currentPartnershipMap.put("partnershipBatsman2ProfilePic", ""+runner.getProfilepic_file_path());
				
		if (partnershipMap != null && !partnershipMap.isEmpty()) {

			List<Entry<String, List<PartnershipDto>>> entryList = new ArrayList<Map.Entry<String, List<PartnershipDto>>>(
					partnershipMap.entrySet());
			
			if (entryList != null && !entryList.isEmpty()) {
				Entry<String, List<PartnershipDto>> lastEntry = entryList.get(entryList.size() - 1);
				if (lastEntry != null && lastEntry.getValue() != null && !lastEntry.getValue().isEmpty()
						&& lastEntry.getValue().size() > 0) {

					PartnershipDto ptdto = new PartnershipDto();

					if (lastEntry.getKey().contains(match.getCurrentInnings() + "")
							|| lastEntry.getKey().contains(match.getSuperOverCurrentInnings() + "")) {
						ptdto = lastEntry.getValue().get(lastEntry.getValue().size() - 1);
					}
					if (ptdto != null) {
						if (ptdto.getOtherPlayerId() == latestBatting.getPlayerID()) {

							currentPartnershipMap.put("partnershipBatsman1ContributionRuns",
									"" + ptdto.getOtherPlayerScore());
							currentPartnershipMap.put("partnershipBatsman1ContributionBalls",
									"" + ptdto.getOtherPlayerBalls());
							currentPartnershipMap.put("partnershipBatsman2ContributionRuns",
									"" + ptdto.getOutPlayerScore());
							currentPartnershipMap.put("partnershipBatsman2ContributionBalls",
									"" + ptdto.getOutPlayerBalls());

						} else if (ptdto.getOtherPlayerId() == runner.getPlayerID()) {

							currentPartnershipMap.put("partnershipBatsman2ContributionRuns",
									"" + ptdto.getOtherPlayerScore());
							currentPartnershipMap.put("partnershipBatsman2ContributionBalls",
									"" + ptdto.getOtherPlayerBalls());
							currentPartnershipMap.put("partnershipBatsman1ContributionRuns",
									"" + ptdto.getOutPlayerScore());
							currentPartnershipMap.put("partnershipBatsman1ContributionBalls",
									"" + ptdto.getOutPlayerBalls());

						}
						currentPartnershipMap.put("partnershipTotalRuns", "" + ptdto.getPartnershipTotalRuns());
						currentPartnershipMap.put("partnershipTotalBalls", "" + ptdto.getPartnershipTotalBalls());
					}
				}
			}
		}else {
			currentPartnershipMap.put("partnershipBatsman2ContributionRuns","0");
			currentPartnershipMap.put("partnershipBatsman2ContributionBalls","0");
			currentPartnershipMap.put("partnershipBatsman1ContributionRuns","0");
			currentPartnershipMap.put("partnershipBatsman1ContributionBalls","0");
			currentPartnershipMap.put("partnershipTotalRuns", "0" );
			currentPartnershipMap.put("partnershipTotalBalls", "0");
		}
			values.put("currentPartnershipMap", currentPartnershipMap);
		
		return currentPartnershipMap;
	}
	
	private static int getOtherBatsmanContributionScore(Map<String, List<PartnershipDto>> partnershipMap, String key, int playerId) {
		int runs = 0;
		if(!CommonUtility.isNullOrEmpty(key)) {
			key = key.split("-")[0];
			for(Entry<String, List<PartnershipDto>> entry : partnershipMap.entrySet()) {
				if(entry.getKey().contains(key+"-")) {
					List<PartnershipDto> partnersDto = entry.getValue();
					if(partnersDto != null && partnersDto.size() > 0) {
						runs += partnersDto.stream().filter(x->x.getOtherPlayerId() == playerId).mapToInt(y->y.getOtherPlayerScore()).sum();
					}
				}
			}
		}
		return runs;
	}
	
	private static Map<String, String> getCareerStatsForPlayer(int playerID, int clubId,int leagueId, int viewId) {
		
		Map<String, String> map = new HashMap<String, String>(3);
		String batingCareerStrikeRate = "";
		String batingCareerAverage = "";
		String bowlingCareerEconomy = "";
		String bowlingCareerAverage = "";
		DecimalFormat df = new DecimalFormat("#.##");
		
		try {
			List<PlayerBattingDto> leagueBattingList = null;
			List<PlayerBowlingDto> leagueBowlingList = null;
			List<PlayerClubStatisticsDto> otherClubStats = null;

			if(leagueId == 0) {
				leagueBattingList = PlayerStatisticsFactory.getPlayersBattingStatistics(playerID, "l", clubId);
				leagueBowlingList = PlayerStatisticsFactory.getPlayersBowlingStatistics(playerID, "l", clubId);
				if (leagueBattingList != null ) {
					otherClubStats = PlayerFactory.getPlayerOtherClubStatistics(playerID, clubId);
				}

			}else {
				leagueBattingList = PlayerStatisticsFactory.getPlayersBattingStatistics(playerID, "l",leagueId, clubId);
				leagueBowlingList = PlayerStatisticsFactory.getPlayersBowlingStatistics(playerID, "l",leagueId, clubId);
			}

			
			int totalCareerMatches = 0;
			int totalCareerRuns = 0;
			int totalCareerWickets = 0;
			int totalNotOuts = 0;
			int totalBallsFaced = 0;
			int totalBallsBowled = 0;
			int totalRunsGiven = 0;

			if (leagueBattingList != null) {
				for (PlayerBattingDto leagueBatting : leagueBattingList) {
					totalCareerMatches = totalCareerMatches + leagueBatting.getMatches();
					totalCareerRuns = totalCareerRuns + leagueBatting.getRunsScored();
					totalNotOuts = totalNotOuts + leagueBatting.getNotOuts();
					totalBallsFaced = totalBallsFaced + leagueBatting.getBallsFaced();
				}
			}

			if (leagueBowlingList != null) {
				for (PlayerBowlingDto leagueBowling : leagueBowlingList) {
					totalCareerWickets += leagueBowling.getWickets();
					totalBallsBowled += leagueBowling.getBalls();
					totalRunsGiven += leagueBowling.getRuns();
					if (leagueBattingList == null)
						totalCareerMatches = totalCareerMatches + leagueBowling.getMatches();
				}
			}
			List<PlayerBattingDto> leagueBattingList1;
			List<PlayerBowlingDto> leagueBowlingList1;
			if (otherClubStats != null && !otherClubStats.isEmpty()) {
				for (PlayerClubStatisticsDto otherStats : otherClubStats) {
					leagueBattingList1 = otherStats.getPlayerBattingRecords();
					leagueBowlingList1 = otherStats.getPlayerBowlingRecords();
					if (leagueBattingList1 != null) {
						for (PlayerBattingDto leagueBatting1 : leagueBattingList1) {
							totalCareerMatches += leagueBatting1.getMatches();
							totalCareerRuns += leagueBatting1.getRunsScored();
							totalNotOuts += leagueBatting1.getNotOuts();
							totalBallsFaced += leagueBatting1.getBallsFaced();
						}
					}

					if (leagueBowlingList1 != null) {
						for (PlayerBowlingDto leagueBowling1 : leagueBowlingList1) {
							totalCareerWickets += leagueBowling1.getWickets();
							totalBallsBowled += leagueBowling1.getBalls();
							totalRunsGiven += leagueBowling1.getRuns();
							if (leagueBattingList1 == null)
								totalCareerMatches += leagueBowling1.getMatches();
						}
					}
				}
			}
			
			if(totalBallsFaced>0) {
				batingCareerStrikeRate = df.format((double)(totalCareerRuns*100.0)/totalBallsFaced);
			}
			if(totalNotOuts>0) {
				batingCareerAverage = df.format((double)(totalCareerRuns)/totalNotOuts);
			}
			float bowlerOvers = CommonUtility.stringToFloat(CommonUtility.ballsToOvers(totalBallsBowled));
			if(totalBallsBowled>0) {
				bowlingCareerEconomy = df.format((double)(totalRunsGiven)/bowlerOvers);
			}
			if(totalCareerWickets>0) {
				bowlingCareerAverage = df.format((double)(totalRunsGiven)/totalCareerWickets);
			}
			
			map.put("totalCareerMatches", totalCareerMatches+"");
			map.put("totalCareerRuns", totalCareerRuns+"");
			map.put("totalCareerWickets", totalCareerWickets+"");
			map.put("batingCareerStrikeRate", batingCareerStrikeRate);
			map.put("batingCareerAverage", batingCareerAverage);
			map.put("bowlingCareerEconomy", bowlingCareerEconomy);
			map.put("bowlingCareerAverage", bowlingCareerAverage);
			
		} catch (Exception e) {
			map.put("totalCareerMatches", 0+"");
			map.put("totalCareerRuns", 0+"");
			map.put("totalCareerWickets", 0+"");
			map.put("batingCareerStrikeRate", batingCareerStrikeRate);
			map.put("batingCareerAverage", batingCareerAverage);
			map.put("bowlingCareerEconomy", bowlingCareerEconomy);
			map.put("bowlingCareerAverage", bowlingCareerAverage);
		}
		
	return map;
		
	}

	private static void populateDataForTestMatch(Map<String, Object> values, MatchDto match, LiveScoreOverlayBean score) {
		String tTotal = "";
		String tWickets = "";
		String overs = "";
		String showMsgForScoreNeeded = "";
		String showScoreMsgForOpositTeam = "";
		if(match.isFourthInningsStarted()) {
			if(match.getIsFollowon() > 0) {
				tTotal = match.getT1_2total() +"";
				tWickets = match.getT1_2wickets() +"";
				overs = match.getT1_2overs();
				if(match.getT1total() > match.getT2total()) {
					showMsgForScoreNeeded = "<span class='numberColorClass'>"+match.getResult()+"</span> ";
				}else {
					showMsgForScoreNeeded = "<span class='numberColorClass'>NEED</span> " + +(match.getT2total() - match.getT1total() +1) + " Run(s)";
				}
				showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> "+" " + match.getT2_1total() + "/" + match.getT2_1wickets() 
				// + "<span class='numberColorClass'> o</span> " + match.getT2_1overs()
				+" & " + match.getT2_2total() + "/" + match.getT2_2wickets() ; //+ "<span class='numberColorClass'> o</span> " + match.getT2_2overs();
				values.put("isSecondInningsStarted", false + "");
				score.setSecondInningsStarted(false);
				values.put("showInfoWithT1Buttom", "true");
			}else {
				tTotal = match.getT2_2total() + "";
				tWickets = match.getT2_2wickets() + "";
				overs =  match.getT2_2overs();
				if(match.getT2total() > match.getT1total()) {
					showMsgForScoreNeeded = "<span class='numberColorClass'>"+match.getResult()+"</span> ";
				}else {
					showMsgForScoreNeeded = "<span class='numberColorClass'>NEED</span> " + (match.getT1total() - match.getT2total() +1) + " Run(s)";
				}
				showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" " + match.getT1_1total() + "/" + match.getT1_1wickets() 
				// + "<span class='numberColorClass'> o</span> " + match.getT1_1overs()
				+" & " + match.getT1_2total() + "/" + match.getT1_2wickets() ; //+ "<span class='numberColorClass'> o</span> " + match.getT1_2overs();
				values.put("isSecondInningsStarted", true + "");
				score.setSecondInningsStarted(true);
			}
		}else if(match.isThirdInningsStarted()){

			if(match.getIsFollowon() > 0) {
				tTotal = match.getT2_2total() + "";
				tWickets = match.getT2_2wickets()+"";
				overs = match.getT2_2overs();
				
				if(match.getT2total() > match.getT1total()) {
					showMsgForScoreNeeded = "<span class='numberColorClass'> Lead by</span> " + (match.getT2total() - match.getT1total())  + " Run(s)";
				}else {
					showMsgForScoreNeeded = "<span class='numberColorClass'> Trail by</span> " + (match.getT1total() - match.getT2total()) + " Run(s)";
				}
				showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" " + match.getT1_1total() + "/" + match.getT1_1wickets() ; //+ "<span class='numberColorClass'> Overs</span> " + match.getT1_1overs() ;
				values.put("isSecondInningsStarted", true + "");
				score.setSecondInningsStarted(true);
			}else {
				tTotal = match.getT1_2total() + "";
				tWickets = match.getT1_2wickets()+"";
				overs = match.getT1_2overs();
				// TODO : By Pranab for the given below logic.
				if(match.getT2total() > match.getT1total()) {
					showMsgForScoreNeeded = "<span class='numberColorClass'> Trail by</span> " + (match.getT2total() - match.getT1total()) + " Run(s)";
				}else {
					showMsgForScoreNeeded = "<span class='numberColorClass'> Lead by</span> " + (match.getT1total() - match.getT2total()) + " Run(s)";
				}
				showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> "+" " + match.getT2_1total() + "/" + match.getT2_1wickets(); // + "<span class='numberColorClass'> Overs</span> " + match.getT2_1overs() ;
				values.put("isSecondInningsStarted", false + "");
				score.setSecondInningsStarted(false);
				values.put("showInfoWithT1Buttom", "true");
			}
		
		}else if(match.isSecondInningsStarted()){
			tTotal = match.getT2_1total()+"";
			tWickets = match.getT2_1wickets() + "";
			overs = match.getT2_1overs()  + "";
			
			if(match.getT2total() > match.getT1total()) {
				showMsgForScoreNeeded = "<span class='numberColorClass'> Lead by</span> " + (match.getT2total() - match.getT1total())+ " Run(s)";;
			}else {
				showMsgForScoreNeeded = "<span class='numberColorClass'> Trail by</span> " + (match.getT1total() - match.getT2total())+ " Run(s)";;
			}
			showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" " + match.getT1_1total() + "/" + match.getT1_1wickets() + "<span class='numberColorClass'> Overs</span> " + match.getT1_1overs() ;
			values.put("isSecondInningsStarted", true + "");
			score.setSecondInningsStarted(true);
		}else {
			tTotal = match.getT1total() + "";
			tWickets = match.getT1wickets() + "";
			overs = match.getT1overs();
			showScoreMsgForOpositTeam= "<span class='numberColorClass'>"+match.getTeamOneCode()+"</span> "+" vs " + "<span class='numberColorClass'>"+match.getTeamTwoCode()+"</span> " ;
			values.put("isSecondInningsStarted", false + "");
			score.setSecondInningsStarted(false);
		}

		values.put("tTotal", tTotal);
		values.put("tWickets", tWickets);
		values.put("overs", overs);
		values.put("showMsgForScoreNeeded", showMsgForScoreNeeded);
		values.put("showScoreMsgForOpositTeam", showScoreMsgForOpositTeam);
	}

	public static void battingBowlingOverlay(int clubId, int matchId,
			int viewId, LiveScoreOverlayBean score) throws Exception {
		boolean isTestMatch = false;
		
		ScoreCardBean bean = CommonLogic.prepareFullScorecard(matchId, clubId);
		
		List<BowlingDto> t1BowlingAll = bean.getTeam1Bowling();
		List<BowlingDto> t1BowlingNew = new ArrayList<BowlingDto>();
		//Removing players with player id zero
		
				for(BowlingDto dto : t1BowlingAll) {
					if(dto.getPlayerID()>0) {
						t1BowlingNew.add(dto);
					}
				}
				bean.setTeam1Bowling(t1BowlingNew);
				
				List<BowlingDto> t2BowlingAll = bean.getTeam2Bowling();
				List<BowlingDto> t2BowlingNew = new ArrayList<BowlingDto>();
				for(BowlingDto dto : t2BowlingAll) {
					if(dto.getPlayerID()>0) {
						t2BowlingNew.add(dto);
					}
				}
				bean.setTeam2Bowling(t2BowlingNew);
				
				List<BowlingDto> t1_2BowlingAll = bean.getTeam1_2Bowling();
				List<BowlingDto> t1_2BowlingNew = new ArrayList<BowlingDto>();
				
				for(BowlingDto dto : t1_2BowlingAll) {
					if(dto.getPlayerID()>0) {
						t1_2BowlingNew.add(dto);
					}
				}
				bean.setTeam1_2Bowling(t1_2BowlingNew);
				
				List<BowlingDto> t2_2BowlingAll = bean.getTeam2_2Bowling();
				List<BowlingDto> t2_2BowlingNew = new ArrayList<BowlingDto>();
				
				for(BowlingDto dto : t2_2BowlingAll) {
					if(dto.getPlayerID()>0) {
						t2_2BowlingNew.add(dto);
					}
				}
				bean.setTeam2_2Bowling(t2_2BowlingNew);
				
		Map<String, Object> values = new HashMap<String, Object>();
		MatchDto match = bean.getMatchInfo();// for in progress 2X games
		int firstNameFirst = 0;
		if (match.getClubID() != 0 && match.getLeagueId() != 0) {
			int leagueId = match.getLeagueId();
			try {
				ClubDto club = ClubFactory.getClub(match.getClubID());
				LeagueDto leagueObj = club.getLeague(leagueId);
				if(leagueObj != null && "Test".equalsIgnoreCase(leagueObj.getSeriesType())) {
					isTestMatch = true;
				}
				if(club != null){
					firstNameFirst = club.getFirstNameFirst();
				}
			} catch (Exception e) {
				// do nothing
			}
		}
		
		
		values.put("firstnamefirst",firstNameFirst);
		if(isTestMatch) {

			String t1Name = "";
			String t2Name = "";
			String t1Code = "";
			String t2Code = "";
			String t1Logo = "";
			String t2Logo = "";
			String t1Total = "";
			String t2Total = "";
			String t1Wickets = "";
			String t2Wickets = "";
			String t1Overs = "";
			String t2Overs = "";
			String t1RR = "";
			String t2RR = "";
			String t1_2RR = "";
			String t2_2RR = "";
			String RRR = "";
			String requiredRuns = "";
			String remainingOvers = "";
			String isMatchEnded = "";
			boolean isSecondInningsStarted = false;
			String result = "";
			String shortResult = "";
			String tossWon = "";
			int totalOvers = -1;
			float t2TargetOvers = 0;
			int t2TargetRuns = 0;
			String liveYouTubeLink = "";
			values.put("isTestMatch", "true");
			bean.getTeam1Batting();// Set How out string.
			if (match != null) {
				/*if(match.getTossWon() == 1) tossWon = match.getTeamOneName();
				else if (match.getTossWon() == 2) tossWon = match.getTeamOneName();*/
				t1Name = match.getTeamOneName();
				t2Name = match.getTeamTwoName();
				t1Code = match.getTeamOneCode();
				t2Code = match.getTeamTwoCode();
				
				t1Total = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8)? match.getT1_1total() + "":match.getT1_2total() + "";
				t2Total = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8) ? match.getT2_1total() + "":match.getT2_2total() + "";
				t1Wickets = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8) ? match.getT1_1wickets() + "":match.getT1_2wickets() + "";
				t2Wickets = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8) ? match.getT2_1wickets() + "": match.getT2_2wickets() + "";
				t1Overs = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8) ? match.getT1_1overs() + "": match.getT1_2overs() + "";
				t2Overs = (viewId == 2 || viewId == 4|| viewId == 3 || viewId == 5 || viewId == 8) ? match.getT2_1overs() + "":match.getT2_2overs() + "";
				t2TargetOvers = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2RevisedOvers() : match.getOvers();
				t2TargetRuns = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2Target() : match.getT1total()+1;
				liveYouTubeLink = CommonUtility.getYouTubeLink(match.getLive_streaming_link().trim());
	          
				t1RR = CommonUtility.calculateRunRate(match.getT1total(),
						(double) (match.getT1_1balls()) / 6);
				t2RR = CommonUtility.calculateRunRate(match.getT2total(),
						(double) (match.getT2_1balls()) / 6);
				t1_2RR = CommonUtility.calculateRunRate(match.getT1total(),
						(double) (match.getT1_2balls()) / 6);
				t2_2RR = CommonUtility.calculateRunRate(match.getT2total(),
						(double) (match.getT2_2balls()) / 6);
				RRR = CommonUtility.calculateRunRate(
						t2TargetRuns - match.getT2total(),
						(double) ( t2TargetOvers * 6 - match.getT2balls()) / 6);		
				

				int ballsInOver = 6;
				
				if("100b".equalsIgnoreCase(match.getSeriesType())) {
					ballsInOver = 5;
				}
				
				requiredRuns = "" + (t2TargetRuns - match.getT2total());
				remainingOvers = CommonUtility.ballsToOvers(CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver)- match.getT2balls(), ballsInOver);
				
				if(match.isFourthInningsStarted() && match.getIsFollowon() > 0) {
					isSecondInningsStarted = false;
					values.put("fourthInningt1", "true");
				}else if(match.isFourthInningsStarted() && match.getIsFollowon() <= 0) {
					isSecondInningsStarted = true;
					values.put("fourthInningt2", "true");
				}else if(match.isThirdInningsStarted() && match.getIsFollowon() > 0) {
					isSecondInningsStarted = true;
					values.put("thirdInningt2", "true");
				}else if(match.isThirdInningsStarted() && match.getIsFollowon() <= 0) {
					isSecondInningsStarted = false;
					values.put("thirdInningt1", "true");
				}else if(match.isSecondInningsStarted()) {
					isSecondInningsStarted = true;
				}else {
					isSecondInningsStarted = false;
					
				}
				String inningName = "";
				if(viewId == 32 ||viewId == 33 ||viewId == 34 ||viewId == 35) {
					inningName = "<span style=' font-size: 19px; color: black; text-shadow: 0px 0px grey; '> (2nd Inning)</span>";
				}else if(viewId == 2 ||viewId == 3 ||viewId == 4 ||viewId == 5){
					inningName = "<span style=' font-size: 19px; color: black; text-shadow: 0px 0px grey; '> (1st Inning)</span>";
				}
				t1Name = t1Name + inningName;
				t2Name = t2Name + inningName;
				//t1Code = match.getTeamOneCode();
				//t2Code = match.getTeamTwoCode();
				t1Code = CommonUtility.trimTeamName(
						match.getFirstBattingTeamName(), t1Code);
				t2Code = CommonUtility.trimTeamName(
						match.getSecondBattingTeamName(), t2Code);
				t1Logo = CommonUtility
						.getTeamLogoPath( match
								.getT1_logo_file_path());
				t2Logo = CommonUtility
						.getTeamLogoPath(match
								.getT2_logo_file_path());
				isMatchEnded = match.getIsComplete()+"";
				result = match.getResult();
				if(!CommonUtility.isNullOrEmpty(result) && result.contains("ov)")) {
					result = result.replace("ov)", "OVERS)");
				}
				shortResult = match.getResultForTitle();				
				if (match.getTossWon() > 0) {
	                tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
				} else {
					tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
				}
			   // tossWon = (match.getTossWon() == match.getTeamOne())? match.getTeamOneName():match.getTeamTwoName();
				totalOvers = match.getOvers();
			}
			values.put("totalOvers", totalOvers);
			values.put("isMatchEnded", isMatchEnded);
			values.put("result", result);
			values.put("shortResult", shortResult);
			values.put("tossWon", tossWon);		
			values.put("liveYouTubeLink", liveYouTubeLink);	
			values.put("showMsgForScoreNeeded", "<span class='numberColorClass'>NEED</span> " + requiredRuns + " <span class='numberColorClass'>FROM</span> " + remainingOvers + " <span class='numberColorClass'>OVERS</span> " + RRR + " <span class='numberColorClass'>RRR</span>");
			Map<Integer,Short> partnerShip = new TreeMap<Integer,Short>();
			if (viewId == 2 || viewId == 5 ||viewId == 32 || viewId == 35 || viewId == 6 || viewId == 6 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t1Name", t1Name);
				values.put("t1_2Name", t1Name);/*+"<span class='cardlabel' style=' margin-left: 5px; font-size: 14px; color: #211616; '> (2nd Inning) </span>"*/
				values.put("t1Code", t1Code);
				values.put("t1Logo", t1Logo);

			} 
			if (viewId == 3 || viewId == 4 ||viewId == 33 || viewId == 34 || viewId == 7 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t2Name", t2Name);
				values.put("t2_2Name", t2Name);/*+"<span class='cardlabel' style=' margin-left: 5px; font-size: 14px; color: #211616; '> (2nd Inning) </span>"*/
				values.put("t2Code", t2Code);
				values.put("t2Logo", t2Logo);
			}
			
			if (viewId == 2 || viewId == 3 ||viewId == 32 || viewId == 33 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t1Total", t1Total);
				values.put("t1Wickets", t1Wickets);
				values.put("t1Overs", t1Overs);
				values.put("t1RR", t1RR);
				values.put("t1_2Total", match.getT1_2total());
				values.put("t1_2Wickets", match.getT1_2wickets());
				values.put("t1_2Overs", match.getT1_2overs());
				values.put("t1_2RR", t1_2RR);
				String extras ="";
				if(viewId == 2 || viewId == 3 ) {
					extras ="" + (match.getT1_1byes() + match.getT1_1lbyes() +match.getT1_1penalty() + match.getT2_1Wides() + match.getT2_1noballs());
				}else {
					extras ="" + (match.getT1_2byes() + match.getT1_2lbyes() +match.getT1_2penalty() + match.getT2_2Wides() + match.getT2_2noballs());
				}
				values.put("t1Extras", extras);
				if(viewId == 8) {
					values.put("t1Extras", "" + (match.getT1_1byes() + match.getT1_1lbyes() +match.getT1_1penalty() + match.getT2_1Wides() + match.getT2_1noballs()));
					values.put("t1_2Extras", "" + (match.getT1_1byes() + match.getT1_1lbyes() +match.getT1_1penalty() + match.getT2_1Wides() + match.getT2_1noballs()));
				}
				if(viewId == 2 || viewId == 3 ) {
					for(int i=0;i<=match.getT1_1wickets();i++){
						List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get("1-"+i);
						if(partnershipList != null){
					  		for(PartnershipDto partnership:partnershipList){
					  			partnerShip.put(i, partnership.getTeamTotal());
					  		}
						}
					}
				}else {
					String inningNum = "3-";
					if(match.getIsFollowon() >0) {
						inningNum = "4-";
					}
					for(int i=0;i<=match.getT1_2wickets();i++){
						List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get(inningNum+i);
						if(partnershipList != null){
					  		for(PartnershipDto partnership:partnershipList){
					  			partnerShip.put(i, partnership.getTeamTotal());
					  		}
						}
					}
				}
				
				values.put("partnerShip", partnerShip);

			}
			if (viewId == 4 || viewId == 5 ||viewId == 34 || viewId == 35 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t2Total", t2Total);
				values.put("t2Wickets", t2Wickets);
				values.put("t2Overs", t2Overs);
				values.put("t2RR", t2RR);
				values.put("t2_2Total", match.getT2_2total());
				values.put("t2_2Wickets", match.getT2_2wickets());
				values.put("t2_2Overs", match.getT2_2overs());
				values.put("t2_2RR", t2_2RR);
				values.put("RRR", RRR);
				values.put("requiredRuns", requiredRuns);
				values.put("remainingOvers", remainingOvers);
				String extras ="";
				if(viewId == 4 || viewId == 5 ) {
					extras ="" + (match.getT2_1byes() + match.getT2_1lbyes() +match.getT2_1penalty() + match.getT1_1Wides() + match.getT1_1noballs());
				}else {
					extras ="" + (match.getT2_2byes() + match.getT2_2lbyes() +match.getT2_2penalty() + match.getT1_2Wides() + match.getT1_2noballs());
				}
				values.put("t2Extras", extras);
				if(viewId == 8) {
					values.put("t2Extras", "" + (match.getT2_1byes() + match.getT2_1lbyes() +match.getT2_1penalty() + match.getT1_1Wides() + match.getT1_1noballs()));
					values.put("t2_2Extras", "" + (match.getT2_2byes() + match.getT2_2lbyes() +match.getT2_2penalty() + match.getT1_2Wides() + match.getT1_2noballs()));
				}
				if(viewId == 4 || viewId == 5) {
					for(int i=0;i<=match.getT2_1wickets();i++){
						List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get("2-"+i);
						if(partnershipList != null){
					  		for(PartnershipDto partnership:partnershipList){
					  			partnerShip.put(i, partnership.getTeamTotal());
					  		}
						}
					}
				}else {
					String inningNum = "4-";
					if(match.getIsFollowon() >0) {
						inningNum = "3-";
					}
					for(int i=0;i<=match.getT2_2wickets();i++){
						List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get(inningNum+i);
						if(partnershipList != null){
					  		for(PartnershipDto partnership:partnershipList){
					  			partnerShip.put(i, partnership.getTeamTotal());
					  		}
						}
					}
				}
				
				values.put("partnerShip", partnerShip);
			}
			

			values.put("isSecondInningsStarted", isSecondInningsStarted + "");
			values.put("matchId", "" + matchId);

			List<BattingDto> team1Batting = getBattingOrder(bean.getTeam1Batting());
			List<BattingDto> team2Batting = getBattingOrder(bean.getTeam2Batting());
			List<BattingDto> team1_2Batting = getBattingOrder(bean.getTeam1_2Batting());
			List<BattingDto> team2_2Batting = getBattingOrder(bean.getTeam2_2Batting());
			
			Map<String, String> playerMap = PlayerUtility.createPlayerMap(team1Batting, team2Batting,clubId);
			Map<String, String> playerMap_2 = PlayerUtility.createPlayerMap(team1_2Batting, team2_2Batting,clubId);
			if (viewId == 2) {
				for(BattingDto battingDto : team1Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap));
				}
				List<BattingDto> tbt = getDataByInning(team1Batting, 1);
				values.put("t1Batting", tbt);
			} else if (viewId == 32) {
				for(BattingDto battingDto : team1_2Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap_2));
				}
				List<BattingDto> tbt = getDataByInning(team1_2Batting, 2);
				values.put("t1Batting", tbt);
			} else if (viewId == 4) {
				for(BattingDto battingDto : team2Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap));
				}
				List<BattingDto> tbt = getDataByInning(team2Batting, 1);
				values.put("t2Batting", tbt);
			}else if (viewId == 34) {
				for(BattingDto battingDto : team2_2Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap_2));
				}
				List<BattingDto> tbt = getDataByInning(team2_2Batting, 2);
				values.put("t2Batting", tbt);
			} else if (viewId == 5) {
				List<BowlingDto> tbl = getDataByInningBowl(bean.getTeam1Bowling(), 1);
				values.put("t1Bowling", tbl);
			} else if (viewId == 35) {
				List<BowlingDto> tbl = getDataByInningBowl(bean.getTeam1_2Bowling(), 2);
				values.put("t1Bowling", tbl);
			} else if (viewId == 3) {
				List<BowlingDto> tbl = getDataByInningBowl(bean.getTeam2Bowling(), 1);
				values.put("t2Bowling", tbl);
			} else if (viewId == 33) {
				List<BowlingDto> tbl = getDataByInningBowl(bean.getTeam2_2Bowling(), 2);
				values.put("t2Bowling", tbl);
			} else if(viewId == 6){
				List<String> players = new ArrayList<String>();
				List<String> playerPics = new ArrayList<String>();
				for(BattingDto bat: bean.getTeam1Batting()){
					if(clubId == 12047 && !CommonUtility.isNullOrEmpty(bat.getNickName())) {
						players.add(bat.getNickName());
					}else {
						players.add(bat.getPlayerName());
					}
					playerPics.add(bat.getProfilepic_file_path());
				}
				
				values.put("t1Players", players);
				values.put("t1PlayerPics", playerPics);
			}else if(viewId == 7){
				List<String> players = new ArrayList<String>();
				List<String> playerPics = new ArrayList<String>();
				for(BattingDto bat: bean.getTeam2Batting()){
					if(clubId == 12047 && !CommonUtility.isNullOrEmpty(bat.getNickName())) {
						players.add(bat.getNickName());
					}else {
						players.add(bat.getPlayerName());
					}
					playerPics.add(bat.getProfilepic_file_path());
				}
				values.put("t2Players", players);
				values.put("t2PlayerPics", playerPics);
			}
			if(viewId == 2 || viewId ==3 || viewId == 6 || viewId ==7 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ){
				if(match.getTossWon()> 0) {
					values.put("toss", ((match.getTossWon() == match.getTeamOne())? match.getTeamOneName():match.getTeamTwoName())+ " WON THE TOSS AND ELECTED TO " + ((match.getTossWon()==match.getBattingFirst())?"BAT":"BOWL" ));
		
				}else {
					values.put("toss", "");
	             }
			}
			
			if(viewId == 8){
				
				List<BattingDto> t1Batting = getTop3Batsman(getDataByInning(bean.getTeam1Batting(), 1), 2);
				List<BattingDto> t2Batting = getTop3Batsman(getDataByInning(bean.getTeam2Batting(), 1), 2);
				
				List<BowlingDto> t1Bowling = getTop3Bowlers(getDataByInningBowl(bean.getTeam1Bowling(), 1), 2);
				List<BowlingDto> t2Bowling = getTop3Bowlers(getDataByInningBowl(bean.getTeam2Bowling(), 1), 2);
				
				List<BattingDto> t1_2Batting = getTop3Batsman(getDataByInning(bean.getTeam1_2Batting(), 2), 2);
				List<BattingDto> t2_2Batting = getTop3Batsman(getDataByInning(bean.getTeam2_2Batting(), 2), 2);
				
				List<BowlingDto> t1_2Bowling = getTop3Bowlers(getDataByInningBowl(bean.getTeam1_2Bowling(), 1), 2);
				List<BowlingDto> t2_2Bowling = getTop3Bowlers(getDataByInningBowl(bean.getTeam2_2Bowling(), 1), 2);
				
				for (BattingDto batting : t1Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				for (BattingDto batting : t2Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				
				for (BattingDto batting : t1_2Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				for (BattingDto batting : t2_2Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				
				for (BowlingDto bowling : t1Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}
				for (BowlingDto bowling : t2Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}
				
				for (BowlingDto bowling : t1_2Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}
				for (BowlingDto bowling : t2_2Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}
								
				values.put("t1Batting", t1Batting);
				values.put("t2Batting", t2Batting);
				
				values.put("t1Bowling", t1Bowling);
				values.put("t2Bowling", t2Bowling);
				
				values.put("t1_2Batting", t1_2Batting);
				values.put("t2_2Batting", t2_2Batting);
				
				values.put("t1_2Bowling", t1_2Bowling);
				values.put("t2_2Bowling", t2_2Bowling);
				
				
				/*values.put("t1Batting", getTop3Batsman(getDataByInning(bean.getTeam1Batting(), 1), 2));
				values.put("t2Batting", getTop3Batsman(getDataByInning(bean.getTeam2Batting(), 1), 2));
				
				values.put("t1Bowling", getTop3Bowlers(getDataByInningBowl(bean.getTeam1Bowling(), 1), 2));
				values.put("t2Bowling", getTop3Bowlers(getDataByInningBowl(bean.getTeam2Bowling(), 1), 2));
				
				values.put("t1_2Batting", getTop3Batsman(getDataByInning(bean.getTeam1_2Batting(), 2), 2));
				values.put("t2_2Batting", getTop3Batsman(getDataByInning(bean.getTeam2_2Batting(), 2), 2));
				values.put("t1_2Bowling", getTop3Bowlers(getDataByInningBowl(bean.getTeam1_2Bowling(), 2), 2));
				values.put("t2_2Bowling", getTop3Bowlers(getDataByInningBowl(bean.getTeam2_2Bowling(), 2), 2));*/
			}
			
			score.setView(viewId);
			score.setValues(values);
			score.setSecondInningsStarted(isSecondInningsStarted);
			populateDataForTestMatch(values, match, score);
			// for in progress 2X games
			if (match.getClubID() != 0 && match.getLeagueId() != 0) {
				int leagueId = match.getLeagueId();
				try {
					ClubDto club = ClubFactory.getClub(match.getClubID());
					LeagueDto leagueObj = club.getLeague(leagueId);
					
					if (leagueObj != null ) {
						values.put("seriesName", leagueObj.getName());
						score.setIs2XCricket("2X".equals(leagueObj.getSeriesType()));
					}
				} catch (Exception e) {
					// do nothing
				}
			}
		
		}else {
			String t1Name = "";
			String t2Name = "";
			String t1Code = "";
			String t2Code = "";
			String t1Logo = "";
			String t2Logo = "";
			String t1Total = "";
			String t2Total = "";
			String t1Wickets = "";
			String t2Wickets = "";
			String t1Overs = "";
			String t2Overs = "";
			String t1RR = "";
			String t2RR = "";
			String RRR = "";
			String requiredRuns = "";
			String remainingOvers = "";
			String isMatchEnded = "";
			boolean isSecondInningsStarted = false;
			String result = "";
			String shortResult = "";
			String tossWon = "";
			int totalOvers = -1;
			float t2TargetOvers = 0;
			int t2TargetRuns = 0;
			String liveYouTubeLink = "";
			String oversOrBalls = "OVERS";
			String rrOrRPB = "RR";
			
		
			boolean isSuperOver = false;
			boolean isSuperOverSecondInningsStarted = false;
			Map<String, OverDto> superOverMap = null;
			
			if(bean != null) {
				superOverMap = bean.getSuperOverMap();
				if(superOverMap != null && superOverMap.size()>0) {
					isSuperOver = true;
					
				}
			}
			if(match != null && match.getSuperOverCurrentInnings()>0) {
				isSuperOver = true;
				if(match.getSuperOverCurrentInnings()>=2) {
					isSuperOverSecondInningsStarted = true;
				}
			}
			int superOverInningsNum1 = bean.getSuperOverInningsNum1();
			int superOverInningsNum2 = bean.getSuperOverInningsNum2();
			
			
			bean.getTeam1Batting();// Set How out string.
			int ballsInOver = 6;
			
			if (match != null) {
				/*if(match.getTossWon() == 1) tossWon = match.getTeamOneName();
				else if (match.getTossWon() == 2) tossWon = match.getTeamOneName();*/
				t1Name = match.getTeamOneName();
				t2Name = match.getTeamTwoName();
				t1Code = match.getTeamOneCode();
				t2Code = match.getTeamTwoCode();
				t1Total = match.getT1total() + "";
				t2Total = match.getT2total() + "";
				t1Wickets = match.getT1wickets() + "";
				t2Wickets = match.getT2wickets() + "";
				t1Overs = match.getT1overs() + "";
				t2Overs = match.getT2overs() + "";
				t2TargetOvers = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2RevisedOvers() : match.getOvers();
				t2TargetRuns = match.isDls() && match.getT2RevisedOvers() > 0 ? match.getT2Target() : match.getT1total()+1;
				liveYouTubeLink = CommonUtility.getYouTubeLink(match.getLive_streaming_link().trim());
	          
				t1RR = CommonUtility.calculateRunRate(match.getT1total(),
						(double) (match.getT1balls()) / 6);
				t2RR = CommonUtility.calculateRunRate(match.getT2total(),
						(double) (match.getT2balls()) / 6);
				
				RRR = CommonUtility.calculateRunRate(
						t2TargetRuns - match.getT2total(),
						(double) ( t2TargetOvers * 6 - match.getT2balls()) / 6);		
				

				if("100b".equalsIgnoreCase(match.getSeriesType())) {
					ballsInOver = 5;
					oversOrBalls = "BALLS";
					rrOrRPB = "RPB";
				}
				
				requiredRuns = "" + (t2TargetRuns - match.getT2total());
				remainingOvers = CommonUtility.ballsToOvers(CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver) - match.getT2balls(), ballsInOver);
				isSecondInningsStarted = match.isSecondInningsStarted();
				if(!isSecondInningsStarted) {
					if(match.getT1wickets()>=10 || (match.getT1balls() >= match.getOvers()*ballsInOver)) {
						isSecondInningsStarted = true;
					}
				}
				t1Code = CommonUtility.trimTeamName(
						match.getFirstBattingTeamName(), t1Code);
				t2Code = CommonUtility.trimTeamName(
						match.getSecondBattingTeamName(), t2Code);
				t1Logo = CommonUtility
						.getTeamLogoPath( match
								.getT1_logo_file_path());
				t2Logo = CommonUtility
						.getTeamLogoPath(match
								.getT2_logo_file_path());
				isMatchEnded = match.getIsComplete()+"";
				result = match.getResult();
				if(!CommonUtility.isNullOrEmpty(result) && result.contains("ov)")) {
					result = result.replace("ov)", oversOrBalls+")");
				}
				if(!CommonUtility.isNullOrEmpty(result) && result.contains("b)")) {
					result = result.replace("b)", oversOrBalls+")");
				}
				shortResult = match.getResultForTitle();
				
				/*if (match.getTossWon() > 0) {
	                tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
				} else {
					tossWon = (match.getTossWon() == match.getTeamOne()) ? match.getTeamOneName() : match.getTeamTwoName();
				}*/
			   tossWon = (match.getTossWon() == match.getTeamOne()? match.getTeamOneName():match.getTeamTwoName())+ " WON THE TOSS AND ELECTED TO " ;
			   tossWon += match.getTossWon()==match.getBattingFirst()?"BAT":"BOWL" ;

			   totalOvers = match.getOvers();
			   if("100b".equalsIgnoreCase(match.getSeriesType())) {
				   t1Overs = CommonUtility.oversToBalls(t1Overs+"", ballsInOver)+"";
				   t2Overs = CommonUtility.oversToBalls(t2Overs+"", ballsInOver)+"";
				   t2TargetOvers = CommonUtility.oversToBalls(t2TargetOvers+"", ballsInOver);
				   remainingOvers = CommonUtility.oversToBalls(remainingOvers+"", ballsInOver)+"";
				   totalOvers = CommonUtility.oversToBalls(totalOvers+"", ballsInOver);
				   t1RR = CommonUtility.calculateRPB(match.getT1total(),match.getT1balls());
				   t2RR = CommonUtility.calculateRunRate(match.getT2total(),match.getT2balls());
				}
			}
			values.put("totalOvers", totalOvers);
			values.put("isMatchEnded", isMatchEnded);
			values.put("result", result);
			values.put("shortResult",shortResult);
			values.put("tossWon", tossWon);		
			values.put("liveYouTubeLink", liveYouTubeLink);	
			String showMsgForScoreNeeded = "<span class='numberColorClass'> NEED</span> " + requiredRuns + " <span class='numberColorClass'>FROM</span> " + remainingOvers + " <span class='numberColorClass'>"+oversOrBalls+"</span> ";
			if(!"100b".equalsIgnoreCase(match.getSeriesType())) {
				showMsgForScoreNeeded += RRR + "<span class='numberColorClass'> RRR</span>";
			}
			if(viewId == 14)
			{
				showMsgForScoreNeeded = t2Code + showMsgForScoreNeeded;
			}
			values.put("showMsgForScoreNeeded", showMsgForScoreNeeded);
			Map<Integer,Short> partnerShip = new TreeMap<Integer,Short>();
			if (viewId == 2 || viewId == 5 || viewId == 6 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t1Name", t1Name);
				values.put("t1Code", t1Code);
				values.put("t1Logo", t1Logo);
	
			} 
			if (viewId == 3 || viewId == 4 || viewId == 7 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t2Name", t2Name);
				values.put("t2Code", t2Code);
				values.put("t2Logo", t2Logo);
			}
			
			if (viewId == 2 || viewId == 3 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t1Total", t1Total);
				values.put("t1Wickets", t1Wickets);
				values.put("t1Overs", t1Overs);
				values.put("t1RR", t1RR);
	
				String extras ="" + (match.getT1byes() + match.getT1lbyes() +match.getT1penalty() + match.getT2Wides() + match.getT2noballs());
				values.put("t1Extras", extras);
				for(int i=0;i<=match.getT1wickets();i++){
					List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get("1-"+i);
					if(partnershipList != null){
						PartnershipDto partnership = partnershipList.get(0);
						if(partnership.isOut()) {
							partnerShip.put(i, partnership.getTeamTotal());
						}
					}
				}
				values.put("partnerShip", partnerShip);
	
			} 
			if (viewId == 4 || viewId == 5 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ) {
				values.put("t2Total", t2Total);
				values.put("t2Wickets", t2Wickets);
				values.put("t2Overs", t2Overs);
				values.put("t2RR", t2RR);
				values.put("RRR", RRR);
				values.put("requiredRuns", requiredRuns);
				values.put("remainingOvers", remainingOvers);
	
				String extras ="" + (match.getT2byes() + match.getT2lbyes() +match.getT2penalty() + match.getT1Wides() + match.getT1noballs());
				values.put("t2Extras", extras);
				for(int i=0;i<=match.getT2wickets();i++){
					List<PartnershipDto> partnershipList = (List<PartnershipDto>)bean.getPartnershipMap().get("2-"+i);
					if(partnershipList != null){
						PartnershipDto partnership = partnershipList.get(0);
						if(partnership.isOut()) {
							partnerShip.put(i, partnership.getTeamTotal());
						}
					}
				}
				values.put("partnerShip", partnerShip);
			}
			
	
			values.put("isSecondInningsStarted", isSecondInningsStarted + "");
			values.put("matchId", "" + matchId);
			values.put("oversOrBalls", "" + oversOrBalls);
			values.put("rrOrRPB", "" + rrOrRPB);
	
			List<BattingDto> team1Batting = getBattingOrder(bean.getTeam1Batting());
			List<BattingDto> team2Batting = getBattingOrder(bean.getTeam2Batting());
			
			Map<String, String> playerMap = PlayerUtility.createPlayerMap(team1Batting, team2Batting,clubId);
			
			if (viewId == 2) {
				for(BattingDto battingDto : team1Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap));
				}
				values.put("t1Batting", team1Batting);
			} else if (viewId == 4) {
				for(BattingDto battingDto : team2Batting){
					battingDto.setOutStringCustomReq(battingDto.getOutStringCustomReq(playerMap));
				}
				values.put("t2Batting", team2Batting);
			} else if (viewId == 5) {
				values.put("t1Bowling", bean.getTeam1Bowling());
			} else if (viewId == 3) {
				values.put("t2Bowling", bean.getTeam2Bowling());
			}else if(viewId == 6){
				List<String> players = new ArrayList<String>();
				List<String> playerPics = new ArrayList<String>();
				for(BattingDto bat: bean.getTeam1Batting()){
					if(clubId == 12047 && !CommonUtility.isNullOrEmpty(bat.getNickName())) {
						players.add(bat.getNickName());
					}else {
						players.add(bat.getPlayerName());
					}
					playerPics.add(bat.getProfilepic_file_path());
				}
				
				values.put("t1Players", players);
				values.put("t1PlayerPics", playerPics);
			}else if(viewId == 7){
				List<String> players = new ArrayList<String>();
				List<String> playerPics = new ArrayList<String>();
				for(BattingDto bat: bean.getTeam2Batting()){
					if(clubId == 12047 && !CommonUtility.isNullOrEmpty(bat.getNickName())) {
						players.add(bat.getNickName());
					}else {
						players.add(bat.getPlayerName());
					}
					playerPics.add(bat.getProfilepic_file_path());
				}
				values.put("t2Players", players);
				values.put("t2PlayerPics", playerPics);
			}
			if(viewId == 6 || viewId ==7 || viewId == 8 || viewId == 13 || viewId == 14 || viewId == 15 || viewId == 28 ){
				if(match.getTossWon()> 0) {
					values.put("toss", ((match.getTossWon() == match.getTeamOne())? match.getTeamOneName():match.getTeamTwoName())+ " WON THE TOSS AND ELECTED TO " + ((match.getTossWon()==match.getBattingFirst())?"BAT":"BOWL" ));
		
				}else {
					values.put("toss", "");
	             }
			}
			
			if(viewId == 8){
				
				List<BattingDto> t1Batting = getTop3Batsman(bean.getTeam1Batting(), 3);
				List<BattingDto> t2Batting = getTop3Batsman(bean.getTeam2Batting(), 3);
				
				List<BowlingDto> t1Bowling = getTop3Bowlers(bean.getTeam1Bowling(), 3);
				List<BowlingDto> t2Bowling = getTop3Bowlers(bean.getTeam2Bowling(), 3);
				
				for (BattingDto batting : t1Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				for (BattingDto batting : t2Batting) {
					batting.setShortName(batting.getPlayerShortName(firstNameFirst));
				}
				
				for (BowlingDto bowling : t1Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}
				for (BowlingDto bowling : t2Bowling) {
					bowling.setShortName(bowling.getPlayerShortName(firstNameFirst));
				}				
								
				values.put("t1Batting", t1Batting);
				values.put("t2Bowling", t2Bowling);	
				if(match.getT2total()>0 || match.getT2balls()>0) {
					values.put("t2Batting", t2Batting);
					values.put("t1Bowling", t1Bowling);
				}else {
					values.put("t2Batting", Collections.EMPTY_LIST);
					values.put("t1Bowling", Collections.EMPTY_LIST);
				}		
				
				if(isSuperOver) {
					
					OverDto t1Over = new OverDto();
					OverDto t2Over = new OverDto();
					
					if(superOverInningsNum1 == 1) {
						t1Over = superOverMap.get(superOverInningsNum1+"-0");
						t2Over = superOverMap.get(superOverInningsNum2+"-0");
					}else if(superOverInningsNum1 == 2) {
						t1Over = superOverMap.get(superOverInningsNum2+"-0");
						t2Over = superOverMap.get(superOverInningsNum1+"-0");
					}
					
					String teamCode1 = t1Code;
					String teamCode2 = t2Code;
					t1Overs = "";
					t2Overs = "";
					
					if(t1Over != null) {
						t1Total = t1Over.getMatchTotal()+"";
						t1Wickets = t1Over.getMatchWickets()+"";
						t1RR = CommonUtility.calculateRPB(t1Over.getMatchTotal(), t1Over.getBowler1Balls());
						t1Overs = t1Over.getBowler1Balls()+"";
						
						if(t1Over.getTeamName().equalsIgnoreCase(match.getTeamOneName())) {
							t1Name = match.getTeamOneName();
							t1Code = CommonUtility.trimTeamName(match.getTeamOneName(), teamCode1);
						}else { 
							t1Name = match.getTeamTwoName();
							t1Code = CommonUtility.trimTeamName(match.getTeamTwoName(), teamCode2);
						}
					}
					
					if(t2Over != null) {
						t2Total = t2Over.getMatchTotal()+"";
						t2Wickets = t2Over.getMatchWickets()+"";
						t2RR = CommonUtility.calculateRPB(t2Over.getMatchTotal(), t2Over.getBowler1Balls());
						t2Overs = t2Over.getBowler1Balls()+"";
						
						if(t2Over.getTeamName().equalsIgnoreCase(match.getTeamOneName())) {
							t2Name = match.getTeamOneName();
							t2Code = CommonUtility.trimTeamName(match.getTeamOneName(), teamCode1);
						}else { 
							t2Name = match.getTeamTwoName();
							t2Code =  CommonUtility.trimTeamName(match.getTeamTwoName(), teamCode2);
						}
						t2TargetOvers =1;
						if(t1Over != null) {
							t2TargetRuns = t1Over.getMatchTotal()+1; 
						}
						requiredRuns = "" + (t2TargetRuns - t2Over.getMatchTotal());
						remainingOvers = (CommonUtility.oversToBalls(t2TargetOvers+"", 6) - t2Over.getBowler1Balls())+"";
					}
					oversOrBalls = "BALLS";
					
					String winningTeamCode = "";
					
					if(t1Over != null && t2Over != null) {
						
						if(t1Over.getRuns()>t2Over.getRuns()) {
							winningTeamCode = t1Code;
						}else {
							winningTeamCode = t2Code;
						}
						result = "Match tied. "+ winningTeamCode +" won the super over.";
					}	
				}
				/*values.put("t1Batting", getTop3Batsman(bean.getTeam1Batting(), 3));
				values.put("t2Batting", getTop3Batsman(bean.getTeam2Batting(), 3));
				values.put("t1Bowling", getTop3Bowlers(bean.getTeam1Bowling(), 3));
				values.put("t2Bowling", getTop3Bowlers(bean.getTeam2Bowling(), 3));*/
				values.put("result", result);	
				
			}
			
			if(viewId != 8 && match != null && match.getIsComplete()==0 && (match.isSecondInningsStarted() || match.getT1wickets()>=10 || (match.getT1balls() >= match.getOvers()*ballsInOver))) {
				result = showMsgForScoreNeeded;
			}
			values.put("result", result);	
			
			score.setView(viewId);
			score.setValues(values);
			score.setSecondInningsStarted(isSecondInningsStarted);
	
			// for in progress 2X games
			if (match.getClubID() != 0 && match.getLeagueId() != 0) {
				int leagueId = match.getLeagueId();
				try {
					ClubDto club = ClubFactory.getClub(match.getClubID());
					LeagueDto leagueObj = club.getLeague(leagueId);
					
					if (leagueObj != null ) {
						values.put("seriesName", leagueObj.getName());
						score.setIs2XCricket("2X".equals(leagueObj.getSeriesType()));
					}
				} catch (Exception e) {
					// do nothing
				}
			}
		}
		
	}
	
	private static List<BattingDto> getDataByInning(List<BattingDto> batting, int i) {
		List<BattingDto> tbt = new ArrayList<BattingDto>();
		for(BattingDto bt : batting) {
			if(bt.getInnings() == i) {
				tbt.add(bt);
			}
		}
		return tbt;
	}
	private static List<BowlingDto> getDataByInningBowl(List<BowlingDto> bowling, int i) {
		List<BowlingDto> tbl = new ArrayList<BowlingDto>();
		for(BowlingDto bl : bowling) {
			if(bl.getInnings() == i && bl.getPlayerID()>0) {
				tbl.add(bl);
			}
		}
		return tbl;
	}
	private static List<BattingDto> getTop3Batsman(List<BattingDto> team1Batting, int limit) {
		
		Collections.sort(team1Batting,new Comparator<BattingDto>() {
		    public int compare(BattingDto obj1, BattingDto obj2) {
		    	if(obj1.getRunsScored() != obj2.getRunsScored()){
		    		return obj1.getRunsScored() < obj2.getRunsScored()?1:-1;
		    	}else if(obj1.getBallsFaced() != obj2.getBallsFaced()){
		    		return obj1.getBallsFaced() > obj2.getBallsFaced()?1:-1;
		    	}
		        return 0;
		    }
		});
		
		
		return CommonUtility.getLimitedRecordsFromList(team1Batting, limit);
	}
	private static List<BowlingDto> getTop3Bowlers(List<BowlingDto> team1Bowling, int limit) {
		
		Collections.sort(team1Bowling,new Comparator<BowlingDto>() {
		    public int compare(BowlingDto obj1, BowlingDto obj2) {
		    	if(obj1.getWickets() != obj2.getWickets()){
		    		return obj1.getWickets() < obj2.getWickets()?1:-1;
		    	}else if(obj1.getRuns() != obj2.getRuns()){
		    		return obj1.getRuns() > obj2.getRuns()?1:-1;
		    	}
		        return 0;
		    }
		});
		return CommonUtility.getLimitedRecordsFromList(team1Bowling, limit);
	}
	
	private static List<BattingDto> getBattingOrder(List<BattingDto> teamList) {
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
}
