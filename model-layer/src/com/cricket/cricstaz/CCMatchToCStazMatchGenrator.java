package com.cricket.cricstaz;

import static com.cricket.cricstaz.CricStazCommonStatics.ABANDONE_TYPE_ABANDONED;
import static com.cricket.cricstaz.CricStazCommonStatics.ABANDONE_TYPE_FOREFEIT;
import static com.cricket.cricstaz.CricStazCommonStatics.AUTO_ADD_OTHER_DETAILS_FALSE;
import static com.cricket.cricstaz.CricStazCommonStatics.AUTO_ADD_PLAYERS_FALSE;
import static com.cricket.cricstaz.CricStazCommonStatics.MATCH_TYPE_1INNINGS;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_BOWLED;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_CATCH;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_CATCH_BEHIND;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_DID_NOT_BAT;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_HANDLED_BALL;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_HIT_WICKET;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_LBW;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_NOT_OUT;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_OBSTRUCTED;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_RUN_OUT;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_STUMP;
import static com.cricket.cricstaz.CricStazCommonStatics.OT_TIMED_OUT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.PartnershipDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.lite.ScoreCardBean;
import com.cricket.utility.CommonLogic;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.NotificationHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class CCMatchToCStazMatchGenrator {
	private static Logger log = LoggerFactory.getLogger(CCMatchToCStazMatchGenrator.class);
	/*private static final String CRICT_STAZ_LEAGUE_NAME = "Cricket Leinster Open Competitions";
	private static final String userName = "santosh@cricclubs.com";
	private static final String password = "4636204";*/
	private static final String dobFormatCS  = "dd/MM/yyyy";
	public static void main(String a[]) throws Exception {
		//uploadCricStazForClub(6265, 142, "SantoshCC", "santosh@cricclubs.com", "4636204"); 27, 55, 42
		// Cricket Leinster Open Competitions
		uploadCricStazForClub9638(144);
		
		// Cricket Leinster Women's Competitions
		// uploadCricStazForClub9897(3);
		
	}
	public static void uploadCricStazForClub(int clubId, int matchId, String leagueName, String userId, String password) throws Exception{
		uploadCricStazData(clubId, matchId, leagueName, userId, password);
	}
	public static void uploadCricStazForClub9638(int matchId){
		try {
			uploadCricStazData(9638, matchId, "Cricket Leinster Open Competitions", "support@cricclubs.com", "7868640");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error( "uploadCricStazForClub9638 " +  e.getMessage());
			NotificationHelper.handleException(e);
		}
	}
	// Cricket Leinster Women's Competitions
	public static void uploadCricStazForClub9897(int matchId){
		try {
			uploadCricStazData(9897, matchId, "Cricket Leinster Women's Competitions", "support@cricclubs.com", "7868640");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("uploadCricStazForClub9897 "+ e.getMessage());
			NotificationHelper.handleException(e);
		}
	}
	
	// Cricket Leinster Development League
		public static void uploadCricStazForClub9899(int matchId){
			try {
				uploadCricStazData(9899, matchId, "Cricket Leinster Development League", "support@cricclubs.com", "7868640");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error("uploadCricStazForClub9899 "+e.getMessage());
				NotificationHelper.handleException(e);
			}
		}
	
	private static void uploadCricStazData(int clubId, int matchId, String leagueName, String userName, String password) throws Exception{
		CricStazMatch csMatchObj = genrateCricStazMatch(clubId, matchId, leagueName);
		Gson json = new GsonBuilder().disableHtmlEscaping().create();
		String jsonData = json.toJson(csMatchObj);
		POSTRequest(jsonData, userName, password);
	}
	
	private static CricStazMatch genrateCricStazMatch(int clubId, int matchId, String leagueName) throws Exception{
		
		ScoreCardBean bean = CommonLogic.prepareFullScorecard(matchId, clubId);
		MatchDto match = bean.getMatchInfo();
		CricStazMatch csMatch = new CricStazMatch();
		
		if(match != null) {
			FixtureDto fixture = FixturesFactory.getFixtureForMatch(match.getMatchID(), clubId);
			LeagueDto leagueDto = LeagueFactory.getLeague(match.getLeagueId(), clubId) ;
			List<PlayerDto> players = PlayerFactory.getPlayersOfMatch(match.getMatchID(), clubId);
			populateMatchObject(csMatch, leagueName, match, fixture, leagueDto, players, clubId);
			populateCSInningsData(bean, match, fixture, csMatch, players);
		}
		return csMatch;
	}
	
	private static void populateCSInningsData(ScoreCardBean bean, MatchDto match, FixtureDto fixture, CricStazMatch csMatch, List<PlayerDto> players) {

		CSInnings csInnings1 = new CSInnings();
		CSInnings csInnings2 = new CSInnings();
		csMatch.getMatch().setInnings1(csInnings1);
		csMatch.getMatch().setInnings2(csInnings2);
		csInnings1.setBattingTeam(match.getTeamOneName());
		csInnings2.setBattingTeam(match.getTeamTwoName());
		
		populateBatsmans(csInnings1, bean.getTeam1Batting(), players);
		populateBowler(csInnings1,  bean.getTeam2Bowling(), players);
		populateBatsmans(csInnings2, bean.getTeam2Batting(), players);
		populateBowler(csInnings2,  bean.getTeam1Bowling(), players);
		
		populateFOW(bean, match, fixture, csInnings1, csInnings2, players);
		
		/*populateFOW(csInnings2,  bean.getTeam1Bowling(), players);
		populateFOW(csInnings2,  bean.getTeam1Bowling(), players);*/
		
		csInnings1.setByes(String.valueOf(match.getT1byes()));
		csInnings1.setLegbyes(String.valueOf(match.getT1lbyes()));
		csInnings1.setPenalties(String.valueOf(match.getT1penalty()));
		csInnings1.setWides(String.valueOf(match.getT2Wides()));
		csInnings1.setNoballs(String.valueOf(match.getT2noballs()));
		
		csInnings2.setByes(String.valueOf(match.getT2byes()));
		csInnings2.setLegbyes(String.valueOf(match.getT2lbyes()));
		csInnings2.setPenalties(String.valueOf(match.getT2penalty()));
		csInnings2.setWides(String.valueOf(match.getT1Wides()));
		csInnings2.setNoballs(String.valueOf(match.getT1noballs()));
	
		
	}
	private static void populateFOW(ScoreCardBean bean, MatchDto match, FixtureDto fixture, CSInnings csInnings1, CSInnings csInnings2, List<PlayerDto> players) {
		
		Map partnershipMap = bean.getPartnershipMap();
		getCSFowData(players, partnershipMap, "1-",  match.getT1wickets(), csInnings1);
		getCSFowData(players, partnershipMap,  "2-", match.getT2wickets(), csInnings2);
		
	}
	private static void getCSFowData(List<PlayerDto> players, Map partnershipMap,
			String inningNumberFOW, int wickets, CSInnings csInnings) {
		
		List<CSFOW> csFow1s = new ArrayList<CSFOW>();
		
		for(int i=0;i<=wickets;i++){
			List<PartnershipDto> partnershipList = (List<PartnershipDto>)partnershipMap.get(inningNumberFOW+i);
			if(partnershipList != null && !partnershipList.isEmpty() & partnershipList.size() > 0){
		  		for(PartnershipDto partnership:partnershipList){
		  			if(partnership.getOutPlayerId() > 0) {
		  				PlayerDto player = getPlayerById(partnership.getOutPlayerId(), players);
			  			if(player != null) {
			  				CSFOW fw = new CSFOW(String.valueOf(partnership.getTeamTotal()), player.getFirstName(), player.getLastName(), CommonUtility.changeDateFormat(player.getDateOfBirth(), dobFormatCS));
			  				csFow1s.add(fw);
			  			}
		  			}
		  			
		  		}
	  		}
		}
		int fowCount = 1;
		csInnings.setFOW1(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW2(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW3(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW4(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW5(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW6(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW7(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW8(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW9(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW10(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW11(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW12(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW13(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
		csInnings.setFOW14(csFow1s.size() >=fowCount ? csFow1s.get(fowCount-1):null);fowCount++;
	}

	private static void populateBowler(CSInnings csInnings, List<BowlingDto> teamBowling, List<PlayerDto> players) {

		// CSBatsman batsMan = new CSBatsman(firstname, lastname, dOB, howout, fielder, bowler, score, fours, sixes, balls, mins, mVP)
		List<CSBowler> csBowlers = new ArrayList<CSBowler>(6);
		for(BowlingDto bowler : teamBowling) {
			 PlayerDto player = getPlayerById(bowler.getPlayerID(), players);
			
			CSBowler csbowler = null;
			if(bowler != null) {
				csbowler = new CSBowler(player.getFirstName(), player.getLastName(),  CommonUtility.changeDateFormat(player.getDateOfBirth(), dobFormatCS), String.valueOf(bowler.getOvers()),
						String.valueOf(bowler.getMaidens()), String.valueOf(bowler.getWickets()), 
						String.valueOf(bowler.getRuns()), String.valueOf(bowler.getWides()),String.valueOf(bowler.getNoBalls()));
				csBowlers.add(csbowler);
			}
			
		}
		int bowlersCount = 1;
		csInnings.setBowler1(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler2(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler3(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler4(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler5(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler6(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler7(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler8(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler9(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler10(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
		csInnings.setBowler11(csBowlers.size() >=bowlersCount ? csBowlers.get(bowlersCount-1):null);bowlersCount++;
	}

	private static void populateBatsmans(CSInnings innings, List<BattingDto> teamBatting, List<PlayerDto> players) {
		// CSBatsman batsMan = new CSBatsman(firstname, lastname, dOB, howout, fielder, bowler, score, fours, sixes, balls, mins, mVP)
		List<CSBatsman> csBatsmans = new ArrayList<CSBatsman>(11);
		List<CSBatsman> csBatsmansDnB = new ArrayList<CSBatsman>(11);
		for(BattingDto batting : teamBatting) {
			PlayerDto batsMan = getPlayerById(batting.getPlayerID(), players);
			PlayerDto fielder = getPlayerById(CommonUtility.stringToInt(batting.getWicketTaker2()), players);
			CSUserInfo csFielder = null;
			if(fielder != null) {
				csFielder = new CSUserInfo(fielder.getFirstName(), fielder.getLastName(), CommonUtility.changeDateFormat(fielder.getDateOfBirth(), dobFormatCS));
			}
			PlayerDto bowler = getPlayerById(CommonUtility.stringToInt(batting.getWicketTaker1()), players);
			CSUserInfo csbowler = null;
			if(bowler != null) {
				csbowler = new CSUserInfo(bowler.getFirstName(), bowler.getLastName(), CommonUtility.changeDateFormat(bowler.getDateOfBirth(), dobFormatCS));
			}
			CSBatsman csBatsman = new CSBatsman(batsMan.getFirstName(), batsMan.getLastName(), CommonUtility.changeDateFormat(batsMan.getDateOfBirth(), dobFormatCS), getCSHouOut(batting.getHowOut()), 
					csFielder, csbowler,String.valueOf(batting.getRunsScored()), String.valueOf(batting.getFours()), 
					String.valueOf(batting.getSixers()), String.valueOf(batting.getBallsFaced()), "0", "1");
			
			if("0".equalsIgnoreCase(csBatsman.getHowout())) {
				csBatsmansDnB.add(csBatsman);
			}else {
				csBatsmans.add(csBatsman);
			}
			
		}
		csBatsmans.addAll(csBatsmansDnB);
		int batsManCount = 1;
		innings.setBatsman1(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman2(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman3(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman4(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman5(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman6(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman7(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman8(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman9(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman10(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
		innings.setBatsman11(csBatsmans.size() >=batsManCount ? csBatsmans.get(batsManCount-1):null);batsManCount++;
			
	}

	private static String getCSHouOut(String howOut) {
		if(howOut == null) {
			return OT_DID_NOT_BAT;
		}
		if(howOut.isEmpty()) {
			return OT_NOT_OUT;
		}
		
		String csOutType = OT_DID_NOT_BAT;
		switch (howOut) {
		case "b":
			csOutType = OT_BOWLED;
			break;
		case "st":
			csOutType = OT_STUMP;
			break;
		case "ct":
			csOutType = OT_CATCH;
			break;
		case "ctw":
			csOutType = OT_CATCH_BEHIND;
			break;
		case "lbw":
			csOutType = OT_LBW;
			break;
		case "ro":
			csOutType = OT_RUN_OUT;
			break;
		case "ht":
			csOutType = OT_HIT_WICKET;
			break;
		case "hdb":
			csOutType = OT_HANDLED_BALL;
			break;
		case "hbt":
			csOutType = OT_OBSTRUCTED;
			break;
		case "obf":
			csOutType = OT_OBSTRUCTED;
			break;
		case "to":
			csOutType = OT_TIMED_OUT;
			break;
		default:
			csOutType = OT_DID_NOT_BAT;
			break;
		}
		return csOutType;
		
	}

	private static void populateMatchObject(CricStazMatch matchObj, String leagueName, MatchDto match, FixtureDto fixture, LeagueDto leagueDto, List<PlayerDto> players, int clubId ) {
		CSMatch csMatch = new CSMatch();
		matchObj.setMatch(csMatch);
		
		csMatch.setLeague(leagueName);
		csMatch.setAutoAddPlayers(AUTO_ADD_PLAYERS_FALSE);
		csMatch.setAutoAddOthers(AUTO_ADD_OTHER_DETAILS_FALSE);
		//csMatch.setExternalMatchID(String.valueOf(match.getMatchID()));
		csMatch.setMatchType(MATCH_TYPE_1INNINGS);
		csMatch.setDatePlayed(stringToCsDate(match.getMatchDate()));
		csMatch.setExternalMatchID(String.valueOf(match.getMatchID()));
		// TODO :  Need to check how to update with efficient way.
		csMatch.setGround(null);
			try {
				if(fixture.getGroundId() > 0) {
					GroundDto ground = GroundFactory.getGround(fixture.getGroundId(), clubId);
					csMatch.setGround(ground.getName());
				}
			} catch (Exception e) {
				
			}
		csMatch.setGrade(leagueDto.getName());
		
		csMatch.setUmpire1(new CSUserInfo(fixture.getUmpire1Name()));
		csMatch.setUmpire2(new CSUserInfo(fixture.getUmpire2Name()));
		
		csMatch.setHomeTeam(fixture.getTeamOneName());
		csMatch.setAwayTeam(fixture.getTeamTwoName());
		csMatch.setHomeClub(fixture.getTeamOneName());
		csMatch.setAwayClub(fixture.getTeamTwoName());
		int homeCapt = match.getTeamOneCaptain();
		int awayCapt = match.getTeamTwoCaptain();
		if(fixture.getTeamOne() != match.getTeamOne()) {
			homeCapt = match.getTeamTwoCaptain();
			awayCapt = match.getTeamOneCaptain();
		}
		csMatch.setHomeCaptain(getCCUserInfoByCCId(homeCapt, players));
		csMatch.setAwayCaptain(getCCUserInfoByCCId(awayCapt, players));
		
		csMatch.setComment(match.getComment());
		// TODO : Need to check if bonus points requried to add.
		int matchT1Point = match.getTeam1Points(leagueDto)/* + match.getTeam1BonusPoints(leagueDto)*/;
		int matchT2Point = match.getTeam2Points(leagueDto)/* + match.getTeam2BonusPoints(leagueDto)*/;
		
		if(fixture.getTeamOne() == match.getTeamOne()) {
			csMatch.setHomePoints(String.valueOf(matchT1Point));
			csMatch.setAwayPoints(String.valueOf(matchT2Point));
		}else {
			csMatch.setHomePoints(String.valueOf(matchT2Point));
			csMatch.setAwayPoints(String.valueOf(matchT1Point));
		}
		
		csMatch.setSubResult(""); // TODO : Need to check for sub result.
		
		csMatch.setMargin(match.getResultMargin());
		
		csMatch.setToss(getHomeOrAwayTeam(match.getTossWon(), fixture.getTeamOne()));
		csMatch.setResult(getResult(match, fixture));
	}

	private static String getResult(MatchDto match, FixtureDto fixture) {
		// TODO Auto-generated method stub
		if(match.getIsComplete() == 0) {
			return "7";
		}
		if(!CommonUtility.isNullOrEmpty(match.getAbandoneType())) {
			if (ABANDONE_TYPE_ABANDONED.equals(match.getAbandoneType())) {
				return "8";
			} else if (ABANDONE_TYPE_FOREFEIT.equals(match.getAbandoneType())) {
				return "5";
			} else {
				return "3";
			}
		} else {
			if (match.getWinner() > 0) {
				if (fixture.getTeamOne() == match.getWinner()) {
					return "1";
				} else {
					return "2";
				}
			} else {
				return "3";
			}
		}
	}

	private static String getHomeOrAwayTeam(int winTeam, int teamOne) {
		if(winTeam == teamOne) {
			return "0";
		}else {
			return "1";
		}
	}

	private static CSUserInfo getCCUserInfoByCCId(int teamOneCaptain, List<PlayerDto> players) {
		PlayerDto player = getPlayerById(teamOneCaptain, players);
		if(player != null) {
			return new CSUserInfo(player.getFirstName(), player.getLastName(), CommonUtility.changeDateFormat(player.getDateOfBirth(), dobFormatCS));
		}
		return null;
	}

	private static PlayerDto getPlayerById(int teamOneCaptain, List<PlayerDto> players) {
		if(players != null && !players.isEmpty()) {
			for(PlayerDto player : players) {
				if(player.getPlayerID() == teamOneCaptain) {
					return player;
				}
			}
		}
		return null;
	}

	private static CSDate stringToCsDate(String matchDate) {
		if(!CommonUtility.isNullOrEmpty(matchDate)) {
			String date[] = matchDate.split("/");
			if(date != null && date.length == 3) {
				return new CSDate(date[1], date[0], date[2]);
			}
		}
		return null;
	}

	public static void POSTRequest(String POST_PARAMS, String userName, String password) throws IOException {
	   
	    URL obj = new URL("https://www2.cricketstatz.com/ss/addj.aspx");
	    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
	    postConnection.setRequestMethod("POST");
	   
        String authString = userName + ":" + password;
        String authStringEnc = CommonUtility.Base64Encode(authString);
	    postConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
	    postConnection.setRequestProperty("Content-Type", "application/json");
	    postConnection.setDoOutput(true);
	    OutputStream os = postConnection.getOutputStream();
	    os.write(POST_PARAMS.getBytes());
	    os.flush();
	    os.close();
	    int responseCode = postConnection.getResponseCode();
	   
	    
	    
	    //if () { //success
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	            postConnection.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in .readLine()) != null) {
	            response.append(inputLine);
	        } in .close();	      
	}
	
}
