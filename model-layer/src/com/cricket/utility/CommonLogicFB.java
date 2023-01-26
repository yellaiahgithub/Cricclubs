package com.cricket.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.beans.HomePageLeagueFB;
import com.cricket.beans.ParScoreBall;
import com.cricket.beans.ParScoreData;
import com.cricket.beans.TypeOfRuns;
import com.cricket.dao.ClubFactory;
import com.cricket.dao.DLSCalculationFactory;
import com.cricket.dao.FacebookFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.MatchDLRecordsFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.PlayerStatisticsFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dao.UmpireFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dao.adconfig.AdConfigGlobalFactory;
import com.cricket.dlscalculation.DLSInputData;
import com.cricket.dlscalculation.MatchDLRecord;
import com.cricket.dto.BallDto;
import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.FacebookPageDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.OverDto;
import com.cricket.dto.PartnershipDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerStatisticsFBDto;
import com.cricket.dto.SeriesNumbersBeanFB;
import com.cricket.dto.SessionPlayerDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.UmpireDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.adconfig.AdConfigGlobalDTO;
import com.cricket.dto.lite.BallBean;
import com.cricket.dto.lite.BallByBallBean;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.LeagueLite;
import com.cricket.dto.lite.LiveMatchInfo;
import com.cricket.dto.lite.ScoreCardBean;
import com.cricket.dto.lite.ScoreCardGraphsBean;
import com.football.dao.IncidentFactory;
import com.football.dto.IncidentDto;

public class CommonLogicFB {
	private static Logger log = LoggerFactory.getLogger(CommonLogicFB.class);
	public static List<String> ListOfSeriesTypes = Arrays.asList("Test", "1 Day", "Twenty20", "Ten10", "2X", "Youth",
			"Women", "Other");

	public static String BALL_TYPE_WIDE = "Wide";
	public static String BALL_TYPE_NOBALL = "No Ball";
	public static String BALL_TYPE_GOOD_NOBALL_BYE = "Good No Ball Bye";
	public static String BALL_TYPE_GOOD_NOBALL_LEG_BYE = "Good No Ball Leg Bye";
	public static String BALL_TYPE_NOBALL_BYE = "No Ball Bye";
	public static String BALL_TYPE_NOBALL_LEG_BYE = "No Ball Leg Bye";
	public static String BALL_TYPE_NOBALL_BAT = "No Ball of Bat";
	public static String BALL_TYPE_GOOD_WIDE = "Good Wide";
	public static String BALL_TYPE_GOOD_NOBALL = "Good No Ball";
	public static String BALL_TYPE_GOOD_NOBALL_BAT = "Good No Ball of Bat";
	public static String BALL_TYPE_BYE = "Bye";
	public static String BALL_TYPE_LEG_BYE = "Leg Bye";
	public static String BALL_TYPE_GOODBALL = "Good Ball";
	public static String BALL_TYPE_INPROGRESS = "In Progress";
	public static String BALL_TYPE_POSITIVE_PENALTY = "Add Penalties";
	public static String BALL_TYPE_NEGATIVE_PENALTY = "Remove Penalties";
	public static String BALL_TYPE_BOWLER_COUNT_BALL = "Bowler Count Ball";
	public static String BALL_TYPE_NO_COUNT_BALL = "No Count Ball";
	public static String BALL_TYPE_AUTO_COMMENT_BALL = "Auto Comment Ball";
	public static String BALL_TYPE_AUTO_COMMENT_BALL_EDIT = "Auto Comment Ball Edit";

	public static List<String> ListOfBallTypes = Arrays.asList("Wide", "No Ball", "No Ball of Bat", "Bye", "Leg Bye",
			"Good Ball", "In Progress", "Add Penalties", "Remove Penalties", "Good No Ball Bye", "Good No Ball Leg Bye",
			"No Ball Bye", "No Ball Leg Bye", "Bowler Count Ball","No Count Ball");

	public static String OUT_METHOD_RUN_OUT = "ro";
	public static String OUT_METHOD_BOWLED = "b";
	public static String OUT_METHOD_LBW = "lbw";
	public static String OUT_METHOD_HIT_WICKET = "ht";
	public static String OUT_METHOD_CATCH = "ct";
	public static String OUT_METHOD_CATCH_WK = "ctw";
	public static String OUT_METHOD_RETIRED = "rt";
	public static String OUT_METHOD_RETIRED_OUT = "rto";
	public static String OUT_METHOD_STUMPED = "st";
	public static String OUT_METHOD_HIT_BALL_TWICE = "hbt";
	public static String OUT_METHOD_HANDLED_BALL = "hdb";
	public static String OUT_METHOD_TIMED_OUT = "to";
	public static String OUT_METHOD_OBSTRUCTING_THE_FIELD = "obf";
	public static String OUT_METHOD_MANKADING = "mk";

	public static List<String> LIST_OF_OUT_METHOD = Arrays.asList("ro", "b", "lbw", "ht", "ct", "ctw", "rt", "rto",
			"st", "hbt", "hdb", "to", "obf", "mk");

	public static List<String> ListOfOutMethods = Arrays.asList("Bowled", "Caught", "WktKpr Catch", "Run Out",
			"Stumped", "LBW", "Retired Hurt", "Retired Out", "Hit Wicket", "Hit Ball Twice", "Handled Ball",
			"Timed Out", "Obstructing The Field", "Run Out (Mankad Out)");

	public static void updateFootBallMatchGoals(int matchId, int clubId, boolean forScoring) throws Exception {
		
		List<IncidentDto> incidentList = IncidentFactory.getIncidentsForLiveFootBallMatch(matchId, 0, 0, clubId, forScoring);
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);
				
		int t1Goals = 0;
		int t2Goals = 0;
		
		if(!CommonUtility.isListNullEmpty(incidentList)) {
			for(IncidentDto dto : incidentList) {
				if(dto.getIncidentType().toUpperCase().contains((IncidentFactory.GOAL_SCORED).toUpperCase())) {
					if (match.getPlayers1().contains(dto.getPlayer1Id())) {
						t1Goals += 1;
					} else if (match.getPlayers2().contains(dto.getPlayer1Id())) {
						t2Goals += 1;
					}
				}else if(dto.getIncidentType().equals(IncidentFactory.SELF_GOAL)) {
					if (match.getPlayers1().contains(dto.getPlayer1Id())) {
						t2Goals += 1;
					} else if (match.getPlayers2().contains(dto.getPlayer1Id())) {
						t1Goals += 1;
					}
				}
			}
		}
		MatchesFactory.updateFootBallMatchGoals(t1Goals, t2Goals, matchId, clubId);
	}

	public static void calculatePointstable(int clubId, LeagueDto seriesDto, List<TeamDto> teams) throws Exception {
		for (TeamDto team : teams) {
			team.setPoints((team.getWon() * seriesDto.getWinPoints()) + (team.getTie() * seriesDto.getTiePoints()));
		}
	}
	
	public static String getIncidentComment(MatchDto match, IncidentDto dto) throws Exception {
		
		String comment = "";
		
		if(dto.getIncidentType().toUpperCase().contains((IncidentFactory.GOAL_SCORED).toUpperCase())) {
			
			comment = "Goal! "+match.getTeamOneName()+": "+dto.getTeam1Goals()+","+match.getTeamTwoName()+": "+dto.getTeam2Goals()+".";
			
			if(IncidentFactory.GOAL_SCORED.equalsIgnoreCase(dto.getIncidentType())){
				comment += " Scored by "+dto.getPlayer1Name()+".";
				if(dto.getPlayer2Id()>0) {
					comment += " Assisted by "+dto.getPlayer2Name();
				}
			}else if(IncidentFactory.GOAL_SCORED_FREE_KICK.equalsIgnoreCase(dto.getIncidentType())){
				comment += " Scored by "+dto.getPlayer1Name()+" ("+dto.getPlayer1TeamName()+") scored a goal off a free kick";
			}else if(IncidentFactory.GOAL_SCORED_PENALTY_KICK.equalsIgnoreCase(dto.getIncidentType())){
				comment += " Scored by "+dto.getPlayer1Name()+" ("+dto.getPlayer1TeamName()+") scored a goal off a penality kick";
			}	
		}else if(IncidentFactory.GOAL_SAVED.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Attempt saved. Shot by "+dto.getPlayer2Name()+"("+dto.getPlayer2TeamName()+") "
					+ "and saved by "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+")";
		
		}else if(IncidentFactory.YELLOW_CARD.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Yellow Card! "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+") is shown Yellow card.";
		
		}else if(IncidentFactory.RED_CARD.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Red Card! "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+") is shown Red card.";
		
		}else if(IncidentFactory.OFFSIDE.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Offside! "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+") caught offside.";
		
		}else if(IncidentFactory.SUBSTITUTE.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Substitution, "+dto.getPlayer1TeamName()+". "+dto.getPlayer1Name()+" replaces "+dto.getPlayer2Name();
		
		}else if(IncidentFactory.TACKLE.equalsIgnoreCase(dto.getIncidentType())){
			comment = dto.getPlayer2Name()+"("+dto.getPlayer2TeamName()+") tackles the ball "
					+ "from "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+")";
		
		}else if(IncidentFactory.INTERCEPTION.equalsIgnoreCase(dto.getIncidentType())){
			comment = dto.getPlayer2Name()+"("+dto.getPlayer2TeamName()+") intercepts the pass "
					+ "from "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+")";
		
		}else if(IncidentFactory.CORNER.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Corner kick conceded by "+dto.getPlayer2Name()+"("+dto.getPlayer2TeamName()
			+"). "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+") takes the shot";
		
		}else if(IncidentFactory.SHOT_ON_TARGET.equalsIgnoreCase(dto.getIncidentType())){
			comment = "Attempt missed. Shot by "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+")";
		
		}else if(dto.getIncidentType().contains(IncidentFactory.FOUL)){
			comment = dto.getIncidentType()+"! Foul by "+dto.getPlayer1Name()+"("+dto.getPlayer1TeamName()+")";
		
		} else if (IncidentFactory.GOAL_SAVED_PENALTY.equals(dto.getIncidentType())) {
			comment = "Penalty saved. Saved by " + dto.getPlayer1Name() + "(" + dto.getPlayer1TeamName()
					+ ") and shot attempt by " + dto.getPlayer2Name() + "(" + dto.getPlayer1TeamName() + ")";
		
		} else if (IncidentFactory.PENALTY_MISSED.equals(dto.getIncidentType())) {
			comment = "Penalty Missed. Shot by " + dto.getPlayer1Name() + " (" + dto.getPlayer1TeamName()+ ")";
		
		} else if (IncidentFactory.SELF_GOAL.equals(dto.getIncidentType())) {
			comment = "Goal! "+match.getTeamOneName()+": "+dto.getTeam1Goals()+","+match.getTeamTwoName()
					+": "+dto.getTeam2Goals()+".Own Goal by " + dto.getPlayer1Name() + "(" + dto.getPlayer1TeamName() + ")";
		}
		
		return comment;
	}

	private static void calculateTeamRatings(List<MatchDto> matches, List<TeamDto> teams) {
		
		Map<Integer, TeamDto> teamIdDtoMap = new HashMap<Integer, TeamDto>();
		
		if(!CommonUtility.isListNullEmpty(teams)) {
			for(TeamDto team : teams) {
				teamIdDtoMap.put(team.getTeamID(), team);
			}
		}
		
		Collections.reverse(matches);
		
		for (MatchDto match : matches) {
			
			TeamDto t1 = teamIdDtoMap.get(match.getTeamOne());
			TeamDto t2 = teamIdDtoMap.get(match.getTeamTwo());
			
			if (match.getWinner()>0) {			
				populateTeamRatingDetails(match, t1, t2);
			}
		}
	}

	private static void populateTeamRatingDetails(MatchDto match, TeamDto t1, TeamDto t2) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 07, 01);
		Date august1 = cal.getTime();
		
		//System.out.println("team One "+t1.getTeamName() + " team Two " +t2.getTeamName());
		
		int t1noOfMatchesToAdd = 1;
		int t2noOfMatchesToAdd = 1;
		
		float t1CurrentRating = t1.getCurrentRating();
		float t2CurrentRating = t2.getCurrentRating();
				
		String t1RatingCalcStr = "";
		String t2RatingCalcStr = "";
		
		if(t1.getRating()>0  && CommonUtility.isNullOrEmpty(t1.getRatingCalcStr())) {
			t1noOfMatchesToAdd = 2;
			t1.setRatingCalcStr("("+t1.getRating());
			
		}
		if(t2.getRating()>0  && CommonUtility.isNullOrEmpty(t2.getRatingCalcStr())) {
			t2noOfMatchesToAdd = 2;
			t2.setRatingCalcStr("("+t2.getRating());
		}
		
		t1.setNoOfMatchesPlayed(t1.getNoOfMatchesPlayed()+t1noOfMatchesToAdd);
		t2.setNoOfMatchesPlayed(t2.getNoOfMatchesPlayed()+t2noOfMatchesToAdd);

		int t1PointsToAdd = match.getWinner() == t1.getTeamID() ? 100 : -100;
		int t2PointsToAdd = match.getWinner() == t2.getTeamID() ? 100 : -100;
		String t1Symbol = match.getWinner() == t1.getTeamID() ? "+" : "-";
		String t2Symbol = match.getWinner() == t2.getTeamID() ? "+" : "-";
		
		if(t1CurrentRating ==0) {
			t1CurrentRating = t1.getRating()==0?2000:t1.getRating();
		}
		if(t2CurrentRating ==0) {
			t2CurrentRating = t2.getRating()==0?2000:t2.getRating();
		}
		
		float t1PrevCurrentRating = t1CurrentRating;
		float t2PrevCurrentRating = t2CurrentRating;
		
		String t1PrevRatingCalcStr = t1.getRatingCalcStr();
		String t2PrevRatingCalcStr = t2.getRatingCalcStr();
		
		if (!CommonUtility.isNullOrEmpty(t1.getRatingCalcStr())) {
			t1RatingCalcStr = t1.getRatingCalcStr() + "+" + "(" + t2CurrentRating + t1Symbol + 100 + ")";
		} else {
			t1RatingCalcStr = "(" + t2CurrentRating + t1Symbol + 100 + ")";
		}
		if (!CommonUtility.isNullOrEmpty(t2.getRatingCalcStr())) {
			t2RatingCalcStr = t2.getRatingCalcStr() + "+" + "(" + t1CurrentRating + t2Symbol + 100 + ")";
		} else {
			t2RatingCalcStr = "(" + t1CurrentRating + t2Symbol + 100 + ")";
		}
		float t1PrvRating = t1CurrentRating;
		t1CurrentRating = ((t1CurrentRating*(t1.getNoOfMatchesPlayed()-1))+t2CurrentRating+t1PointsToAdd)/t1.getNoOfMatchesPlayed();
		t2CurrentRating = ((t2CurrentRating*(t2.getNoOfMatchesPlayed()-1))+t1PrvRating+t2PointsToAdd)/t2.getNoOfMatchesPlayed();
				
		if(match.getWinner() == t1.getTeamID() && t1PrevCurrentRating > t1CurrentRating) {
			t1CurrentRating = t1PrevCurrentRating;
			
			//if team created after August 1st 2021 dont decrease the number of matches, average by its current rating.
			if(t1.getCreationDate() != null && t1.getCreationDate().after(august1)) {
				if (!CommonUtility.isNullOrEmpty(t1.getRatingCalcStr())) {
					t1RatingCalcStr = t1.getRatingCalcStr() + "+" + "(" + t1CurrentRating + ")";
				}
			}else {
				t1RatingCalcStr = t1PrevRatingCalcStr;
				t1.setNoOfMatchesPlayed(t1.getNoOfMatchesPlayed()-1);
			}
		}
				
		if(match.getWinner() == t2.getTeamID() && t2PrevCurrentRating > t2CurrentRating) {
			t2CurrentRating = t2PrevCurrentRating;
			
			//if team created after August 1st 2021 dont decrease the number of matches, average by its current rating.
			if(t2.getCreationDate() != null && t2.getCreationDate().after(august1)) {
				if (!CommonUtility.isNullOrEmpty(t2.getRatingCalcStr())) {
					t2RatingCalcStr = t2.getRatingCalcStr() + "+" + "(" + t2CurrentRating + ")";
				}
			}else {
				t2RatingCalcStr = t2PrevRatingCalcStr;
				t2.setNoOfMatchesPlayed(t2.getNoOfMatchesPlayed()-1);
			}
		}

		if(match.getWinner()>0 && match.getWinner() != t1.getTeamID() && t1PrevCurrentRating < t1CurrentRating) {
			t1CurrentRating = t1PrevCurrentRating;
			
			//if team created after August 1st 2021 dont decrease the number of matches, average by its current rating.
			if(t1.getCreationDate() != null && t1.getCreationDate().after(august1)) {
				if (!CommonUtility.isNullOrEmpty(t1.getRatingCalcStr())) {
					t1RatingCalcStr = t1.getRatingCalcStr() + "+" + "(" + t1CurrentRating + ")";
				}
			}else {
				t1RatingCalcStr = t1PrevRatingCalcStr;
				t1.setNoOfMatchesPlayed(t1.getNoOfMatchesPlayed()-1);
			}
		}
		
		if(match.getWinner()>0 && match.getWinner() != t2.getTeamID() && t2PrevCurrentRating < t2CurrentRating) {
			
			t2CurrentRating = t2PrevCurrentRating;
			//if team created after August 1st 2021 dont decrease the number of matches, average by its current rating.
			if(t2.getCreationDate() != null && t2.getCreationDate().after(august1)) {
				if (!CommonUtility.isNullOrEmpty(t2.getRatingCalcStr())) {
					t2RatingCalcStr = t2.getRatingCalcStr() + "+" + "(" + t2CurrentRating + ")";
				}
			}else {
				t2RatingCalcStr = t2PrevRatingCalcStr;
				t2.setNoOfMatchesPlayed(t2.getNoOfMatchesPlayed()-1);
			}
		}
		
		
		t1.setRatingCalcStr(t1RatingCalcStr);
		t1.setCurrentRating(t1CurrentRating);		
		
		t2.setRatingCalcStr(t2RatingCalcStr);
		t2.setCurrentRating(t2CurrentRating);
		
		//System.out.println("team One Ratings "+t1.getCurrentRating() + " team Two Ratings " +t2.getCurrentRating());
	}

	public static void sortTeamsbasedOnPointsAndNRR(List<TeamDto> teams, final List<MatchDto> matches, final int clubId) {
		List<String> sortOrderList = new ArrayList<String>();
		
		final List<String> newList=sortOrderList;
		Collections.sort(teams, new Comparator<TeamDto>() {

			@Override
			public int compare(TeamDto o1, TeamDto o2) {
				double points1 = 0;
				double points2 = 0;
				double winPCT1 = 0;
				double winPCT2 = 0;
				int h2h1 = 0;
				int h2h2 = 0;
				int wicketsTaken1 = 0;
				int wicketsTaken2 = 0;
				if(matches !=null) {
					for (MatchDto match : matches) {
						if ((o1.getTeamID() == match.getTeamOne() || o1.getTeamID() == match.getTeamTwo())
								&& (o2.getTeamID() == match.getTeamOne() || o2.getTeamID() == match.getTeamTwo())) {
	
							if (match.getWinner() == o1.getTeamID()) {
								h2h1++;
							}
							if (match.getWinner() == o2.getTeamID()) {
								h2h2++;
							}
						}
					}
				}
				
				int matches1 = o1.getWon() + o1.getLost();
				int matches2 = o2.getWon() + o2.getLost();
				if(matches1 > 0) {
					winPCT1 = ((double) o1.getWon() * 100 / matches1);
				}
				if(matches2 > 0) {
					winPCT2 = ((double) o2.getWon() * 100 / matches2);
				} 
				
				if (clubId == 1257) {
					if (matches1 > 0) {
						points1 = ((double) o1.getWon() / matches1) - ((double) o1.getPenalty() / 100);
					}
					if (matches2 > 0) {
						points2 = ((double) o2.getWon() / matches2) - ((double) o2.getPenalty() / 100);
					}
					if (points1 == points2) {
						points1 += o1.getPoints();
						points2 += o2.getPoints();
					}
					
					if (winPCT2 != winPCT1) {
						return winPCT2 > winPCT1 ? 1 : -1;
					}else if(points2 != points1) { // added Up JIRA WEB-2837
						return points2 > points1 ? 1 : -1;
					}else if (h2h1 != h2h2) {
						return h2h2 > h2h1 ? 1 : -1;
					} else {
						return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
					}
					
					/* JIRA WEB-2837
					else if (o2.getNetRunRate() != o1.getNetRunRate()) {
						return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
					} else {
						return points2 > points1 ? 1 : -1;
					}*/

				} else {
					// Order Team by Points -> Win -> NRR -> Team ID
					if (clubId == 14968 || clubId == 6265) {
						
						

						points1 = o1.getPoints();
						points2 = o2.getPoints();

						if (points2 != points1) {
							return points2 > points1 ? 1 : -1;
						} else {
							if(clubId==14968) {
								if (o2.getNetRunRate() != o1.getNetRunRate()) 
									return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
								else {
									if (winPCT2 != winPCT1) {
										return winPCT2 > winPCT1 ? 1 : -1;
									}else {
										return o2.getTeamID() > o1.getTeamID() ? 1 : -1;
									}
								}
							}
							else {
								if (winPCT2 != winPCT1) {
									return winPCT2 > winPCT1 ? 1 : -1;
								} else if (o2.getNetRunRate() != o1.getNetRunRate()) {
									return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
								} else {
									return o2.getTeamID() > o1.getTeamID() ? 1 : -1;
								}
							}
						}

					} else if (clubId == 232) {
						matches1 = o1.getMatches() - o1.getZeroPoint();
						if (matches1 > 0) {
							points1 = (double) o1.getPoints() / matches1;
						}
						matches2 = o2.getMatches() - o2.getZeroPoint();
						if (matches2 > 0) {
							points2 = (double) o2.getPoints() / matches2;
						}
					} else if (clubId == 116) {
						points1 = o1.getAveragePoints();
						points2 = o2.getAveragePoints();
					} else {
						points1 = o1.getPoints();
						points2 = o2.getPoints();
					}
					if(clubId == 18786 || clubId == 50) {
						wicketsTaken1 = o1.getWicketsTaken();
						wicketsTaken2 = o2.getWicketsTaken();
					}
					
					if(newList!=null && !newList.isEmpty()) {
						for(String so:newList) {
							if("points".equals(so)) {
								if (points2 != points1) {
									return points2 > points1 ? 1 : -1;
								}
							}
							else if("wins".equals(so)) {
								if (o2.getWon() != o1.getWon()) {
									return o2.getWon() > o1.getWon() ? 1 : -1;
								}
							}
							else if("winpercent".equals(so)) {
								if (winPCT2 != winPCT1) {
									return winPCT2 > winPCT1 ? 1 : -1;
								} 
							}
							else if("nrr".equals(so)) {
								if (o2.getNetRunRate() != o1.getNetRunRate()) {
									return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
								}
							}
							else if("H2H".equals(so)) {
								if (h2h1 != h2h2) {
									return h2h2 > h2h1 ? 1 : -1;
								}
							}
						}
						return 0;
					}else {
						if (points2 != points1) {
							return points2 > points1 ? 1 : -1;
						} else if (o2.getNetRunRate() != o1.getNetRunRate()) {
							return o2.getNetRunRate() > o1.getNetRunRate() ? 1 : -1;
						} else if(wicketsTaken2 != wicketsTaken1) {
							return wicketsTaken2 > wicketsTaken1 ? 1 : -1;
						}else if (h2h1 != h2h2) {
							return h2h2 > h2h1 ? 1 : -1;
						} else if (o2.getWon() != o1.getWon()) {
							return o2.getWon() > o1.getWon() ? 1 : -1;
						}else {
							return o2.getTeamID() > o1.getTeamID() ? 1 : -1;
						}
					}
				}
			}
		});

	}
	
	public static List<PlayerDto> getConsolidatedPoints(List<PlayerStatisticsFBDto> playerStats,  int clubId) {
		
		Map<String, PlayerDto> playersmap = new HashMap<String, PlayerDto>();
		List<PlayerDto> players = new ArrayList<PlayerDto>();

		if (playerStats != null && !playerStats.isEmpty()) {
			Iterator<PlayerStatisticsFBDto> itr = playerStats.iterator();
			while (itr.hasNext()) {  
				PlayerStatisticsFBDto player = (PlayerStatisticsFBDto) itr.next();
				int points = player.getPoints();
				if (playersmap.get("" + player.getPlayerId()) != null) {
					PlayerDto p = (PlayerDto) playersmap.get("" + player.getPlayerId());
					p.setPoints(p.getPoints() + points);
					p.setPlayingRole(player.getPlayingRole());
				}else {
					PlayerDto p = new PlayerDto();
					p.setPlayerID(player.getPlayerId());
					p.setFirstName(player.getFirstName());
					p.setLastName(player.getLastName());
					p.setPlayingRole(player.getPlayingRole());
					p.setPoints(points);
					p.setMatchesPlayed(player.getMatches());
					p.setTeamId(player.getTeamId());
					p.setTeamName(player.getTeamName());
					p.setTeamCode(player.getTeamCode());
					p.setTeamlogo_file_path(player.getTeamlogo_file_path());
					p.setProfilepic_file_path(player.getProfilepic_file_path());
					playersmap.put("" + p.getPlayerID(), p);
				}
			}
		}	

		Collection<PlayerDto> playersC = playersmap.values();
		players = new ArrayList<PlayerDto>(playersC);
		Collections.sort(players);
		return players;
	}

	public static Map<String, Object> consolidateMatchRecords(List<BallDto> allBalls, MatchDto match) {
		
		List<BallDto> balls = new ArrayList<BallDto>();
		List<BallDto> superBalls = new ArrayList<BallDto>();
		
		BallDto innings1LastBall = new BallDto();
		BallDto innings2LastBall = new BallDto();
		BallDto innings3LastBall = new BallDto();
		BallDto innings4LastBall = new BallDto();
		
		int superOverInningsNum1 = 0;
		int superOverInningsNum2 = 0;
		
		for(BallDto b : allBalls) {
			if(b.getIsSuperOver()==0) {
				balls.add(b);
				if(b.getInningsNumber() == 1 && b.getBatsman()>0   
						&& !b.getBallType().equals("Bowler Count Ball")) {
					innings1LastBall = b;
				}else if(b.getInningsNumber() == 2 && b.getBatsman()>0  
						&& !b.getBallType().equals("Bowler Count Ball")) {
					innings2LastBall = b;
				}else if(b.getInningsNumber() == 3 && b.getBatsman()>0  
						&& !b.getBallType().equals("Bowler Count Ball")) {
					innings3LastBall = b;
				}else if(b.getInningsNumber() == 4 && b.getBatsman()>0  
						&& !b.getBallType().equals("Bowler Count Ball")) {
					innings4LastBall = b;
				}
			}else if(b.getIsSuperOver()==1) {
				superBalls.add(b);
				if(b.getBatsman()>0 && superOverInningsNum1==0){
		        	if(match.getPlayers1().contains(b.getBatsman())){
		        		superOverInningsNum1 = 2;
		        		superOverInningsNum2 = 1;		        		
		        	}else if(match.getPlayers2().contains(b.getBatsman())){
		        		superOverInningsNum1 = 1;
		        		superOverInningsNum2 = 2;
		        	}
		        }
			}
		}
		if(!CommonUtility.isListNullEmpty(balls)) {
			match.setCurrentInnings(balls.get(balls.size()-1).getInningsNumber());
		}		
		if(!CommonUtility.isListNullEmpty(superBalls)) {
			match.setSuperOverCurrentInnings(superBalls.get(superBalls.size()-1).getInningsNumber());	
		}
		
		List<BattingDto> t1Batting = new ArrayList<BattingDto>();
		List<BattingDto> t2Batting = new ArrayList<BattingDto>();
		List<BowlingDto> t1Bowling = new ArrayList<BowlingDto>();
		List<BowlingDto> t2Bowling = new ArrayList<BowlingDto>();
		List<BattingDto> t1Batting_2 = new ArrayList<BattingDto>();
		List<BattingDto> t2Batting_2 = new ArrayList<BattingDto>();
		List<BowlingDto> t1Bowling_2 = new ArrayList<BowlingDto>();
		List<BowlingDto> t2Bowling_2 = new ArrayList<BowlingDto>();
		Map<String, OverDto> overMap = new LinkedHashMap<String, OverDto>();
		Map<String, OverDto> superOverMap = new LinkedHashMap<String, OverDto>();
		Map<String, List<PartnershipDto>> partnershipMap = new LinkedHashMap<String, List<PartnershipDto>>();
		PartnershipDto oldPS1Dto = new PartnershipDto();
		PartnershipDto oldPS2Dto = new PartnershipDto();
		PartnershipDto oldPS3Dto = new PartnershipDto();
		PartnershipDto oldPS4Dto = new PartnershipDto();
		
		Map<Integer, Short> playerRunsMap = new LinkedHashMap<Integer, Short>();
		Map<Integer, Short> bowlerBallsMap = new LinkedHashMap<Integer, Short>();
		Map<String, TypeOfRuns> typeOfRuns = new HashMap<String, TypeOfRuns>();

		if(match.getSeriesType().contains("Test") || match.getSeriesType().contains("2X")) {
			if(match.getIsFollowon()>0) {
				BallDto oldInnings3LastBall = innings3LastBall;
				innings3LastBall = innings4LastBall;
				innings4LastBall = oldInnings3LastBall;
			}
		}
		
		int totalBallInOver = 6;
		if ("100b".equalsIgnoreCase(match.getSeriesType())) {
			totalBallInOver = 5;
		}

		int t1Byes = 0;
		int t1LByes = 0;
		int t1Balls = 0;
		int t1Wickets = 0;
		int t1Total = 0;
		int t1Penalty = 0;

		int t1_1Byes = 0;
		int t1_1LByes = 0;
		int t1_1Balls = 0;
		int t1_1Wickets = 0;
		int t1_1Total = 0;
		int t1_1Penalty = 0;

		int t1_2Byes = 0;
		int t1_2LByes = 0;
		int t1_2Balls = 0;
		int t1_2Wickets = 0;
		int t1_2Total = 0;
		int t1_2Penalty = 0;

		int t2Byes = 0;
		int t2LByes = 0;
		int t2Balls = 0;
		int t2Wickets = 0;
		int t2Total = 0;
		int t2Penalty = 0;

		int t2_1Byes = 0;
		int t2_1LByes = 0;
		int t2_1Balls = 0;
		int t2_1Wickets = 0;
		int t2_1Total = 0;
		int t2_1Penalty = 0;

		int t2_2Byes = 0;
		int t2_2LByes = 0;
		int t2_2Balls = 0;
		int t2_2Wickets = 0;
		int t2_2Total = 0;
		int t2_2Penalty = 0;

		TypeOfRuns t1 = new TypeOfRuns();
		TypeOfRuns t2 = new TypeOfRuns();
		TypeOfRuns t3 = new TypeOfRuns();
		TypeOfRuns t4 = new TypeOfRuns();
		Map<Integer, Integer> bowlerHatricWC = new HashMap<Integer, Integer>();
		boolean isHatricOnThisBall = false;
		
		for (BallDto ball : balls) {

			if (BALL_TYPE_NO_COUNT_BALL.equalsIgnoreCase(ball.getBallType())
					&& !CommonUtility.isNullOrEmpty(ball.getComment())
					&& CommonUtility.isNullOrEmpty(ball.getOutMethod())) {
				continue;
			}

			/* Commenting as part of the ticket https://cricclubs.atlassian.net/browse/SPORTAPI-520
				 
				 if (BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(ball.getBallType())
				 
						&& !CommonUtility.isNullOrEmpty(ball.getComment()) && ball.getBatsman() <= 0
						&& ball.getBowler() <= 0 && ball.getRunner() <= 0) {
					continue;
				}
			
			*/

			if (BALL_TYPE_NEGATIVE_PENALTY.equals(ball.getBallType())) {
				ball.setRuns(ball.getRuns() * -1);
			}
			isHatricOnThisBall = false;
			if (!BALL_TYPE_NEGATIVE_PENALTY.equalsIgnoreCase(ball.getBallType())
					&& !BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(ball.getBallType())
					&& !BALL_TYPE_NO_COUNT_BALL.equalsIgnoreCase(ball.getBallType())) {
				if (OUT_METHOD_BOWLED.equals(ball.getOutMethod()) || OUT_METHOD_LBW.equals(ball.getOutMethod())
						|| OUT_METHOD_CATCH.equals(ball.getOutMethod())
						|| OUT_METHOD_CATCH_WK.equals(ball.getOutMethod())
						|| OUT_METHOD_STUMPED.equals(ball.getOutMethod())) {

					int wicketCount = CommonUtility.stringToInt(bowlerHatricWC.get(ball.getBowler()) + "") + 1;
					if (wicketCount >= 3) {
						isHatricOnThisBall = true;
						bowlerHatricWC.put(ball.getBowler(), 0);
					} else {
						bowlerHatricWC.put(ball.getBowler(), wicketCount);
					}
				} else {
					bowlerHatricWC.put(ball.getBowler(), 0);
				}
			}

			if (ball.getInningsNumber() == 1) {
				t1Total += ball.getRuns();
				t1_1Total += ball.getRuns();
				BattingDto batsMan = getBatsMan(t1Batting, match, ball.getBatsman(), match.getBattingFirst());
				BattingDto runner = getBatsMan(t1Batting, match, ball.getRunner(), match.getBattingFirst());
				BowlingDto bowler = getBowler(t2Bowling, match, ball,
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());
				if (isHatricOnThisBall && bowler != null) {
					bowler.setHattricks(bowler.getHattricks() + 1);
				}

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, balls, t1);
				
				if (BALL_TYPE_BYE.equals(ball.getBallType())) {
					t1Byes += ball.getRuns();
					if (ball.getBall() <= totalBallInOver) {
						t1Balls += 1;
						t1_1Balls += 1;
					}
					t1_1Byes += ball.getRuns();
				} else if (BALL_TYPE_NOBALL.equals(ball.getBallType()) && ball.getRuns() > 1) {

					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t1Byes += ball.getRuns() - ball.getNoBallCustomRuns();
						t1_1Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1Byes += ball.getRuns() - 1;
						t1_1Byes += ball.getRuns() - 1;
					}

				}
				// Bye and Legby changes
				else if (BALL_TYPE_NOBALL_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {
					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t1Byes += ball.getRuns() - ball.getNoBallCustomRuns();
						t1_1Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1Byes += ball.getRuns() - 1;
						t1_1Byes += ball.getRuns() - 1;
					}
				} else if (BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {
					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t1LByes += ball.getRuns() - ball.getNoBallCustomRuns();
						t1_1LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1LByes += ball.getRuns() - 1;
						t1_1LByes += ball.getRuns() - 1;
					}
				}
				// Bye and Legby changes ---- End

				else if (BALL_TYPE_LEG_BYE.equals(ball.getBallType())) {
					t1LByes += ball.getRuns();
					if (ball.getBall() <= totalBallInOver) {
						t1Balls += 1;
						t1_1Balls += 1;
					}
					t1_1LByes += ball.getRuns();

				} else if (BALL_TYPE_POSITIVE_PENALTY.equals(ball.getBallType())
						|| BALL_TYPE_NEGATIVE_PENALTY.equals(ball.getBallType())) {
					t1Penalty += ball.getRuns();
					t1_1Penalty += ball.getRuns();
				} else if (BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t1Balls += 1;
						t1_1Balls += 1;
					}

					if (BALL_TYPE_GOOD_NOBALL_BYE.equalsIgnoreCase(ball.getBallType())) {
						t1Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
					if (BALL_TYPE_GOOD_NOBALL_LEG_BYE.equalsIgnoreCase(ball.getBallType())) {
						t1LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
				}

				if (BALL_TYPE_GOODBALL.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t1Balls += 1;
						t1_1Balls += 1;
					}
				}
				if (BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType())) {
					if (ball.getBall() <= 6) {
						t1Balls += 1;
						t1_1Balls += 1;
					}
				}
				if (CommonUtility.isNullOrEmpty(ball.getOutMethod()) && innings1LastBall != null 
						&& innings1LastBall.getBallId() == ball.getBallId()) {
					
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1Total);
					partnership.setTeamBalls((short) t1Balls);
					partnership.setOut(false);
					
					//short psBalls = (short) (CommonUtility.getBallsFromOver(partnership.getOverNumber() + "." + partnership.getBallNumber(),totalBallInOver) - 
					//		CommonUtility.getBallsFromOver(oldPS1Dto.getOverNumber() + "." + oldPS1Dto.getBallNumber(),totalBallInOver));
					partnership.setPartnershipTotalRuns((short)(t1Total-oldPS1Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1Balls-oldPS1Dto.getTeamBalls()));

					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t1Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t1Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					partnershipMap.put(ball.getInningsNumber() + "-" + t1Wickets, partnershipList);
					
					oldPS1Dto = partnership;
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())
						&& !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					t1Wickets += 1;
					t1_1Wickets += 1;

					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1Total);
					partnership.setTeamBalls((short) t1Balls);

					List<PartnershipDto> partnershipList = new ArrayList<PartnershipDto>();
					partnershipList.add(partnership);
					partnershipMap.put(ball.getInningsNumber() + "-" + t1Wickets, partnershipList);
					partnership.setPartnershipTotalRuns((short)(t1Total-oldPS1Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1Balls-oldPS1Dto.getTeamBalls()));
					
					oldPS1Dto = partnership;
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod()) && OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1Total);
					partnership.setTeamBalls((short) t1Balls);
					partnership.setRetired(true);
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t1Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t1Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					partnership.setPartnershipTotalRuns((short)(t1Total-oldPS1Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1Balls-oldPS1Dto.getTeamBalls()));
					
					oldPS1Dto = partnership;
				}
				
				/*
				 * Commenting as part of ticket https://cricclubs.atlassian.net/browse/SPORTAPI-520
				  if ((((ball.isValidBall() || CommonLogic.BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType()))
				 
						&& ball.getBall() == totalBallInOver) || isLastBallOfInnings(balls, ball))
						&& !CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(ball.getBallType())) {
				*/
					
				if (((ball.isValidBall() || CommonLogicFB.BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType()))
						&& ball.getBall() == totalBallInOver) || isLastBallOfInnings(balls, ball)) {
					
					OverDto overDto = populateOverStats(balls, ball, batsMan, runner, bowler);
					overDto.setMatchTotal((short) t1Total);
					overDto.setMatchWickets((short) t1Wickets);
					overDto.setTeamName((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamOneName()
							: match.getTeamTwoName());
					overMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}

			} else if (ball.getInningsNumber() == 2) {
				t2Total += ball.getRuns();
				t2_1Total += ball.getRuns();
				BattingDto batsMan = getBatsMan(t2Batting, match, ball.getBatsman(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());
				BattingDto runner = getBatsMan(t2Batting, match, ball.getRunner(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());
				BowlingDto bowler = getBowler(t1Bowling, match, ball, match.getBattingFirst());
				if (isHatricOnThisBall && bowler != null) {
					bowler.setHattricks(bowler.getHattricks() + 1);
				}

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, balls, t2);

				if (BALL_TYPE_BYE.equals(ball.getBallType())) {
					t2Byes += ball.getRuns();
					if (ball.getBall() <= 6) {
						t2Balls += 1;
						t2_1Balls += 1;
					}
					t2_1Byes += ball.getRuns();

				} else if (BALL_TYPE_NOBALL.equals(ball.getBallType()) && ball.getRuns() > 1) {

					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2Byes += ball.getRuns() - 1;
					}
					// t2Byes += ball.getRuns() - 1;
				}

				// Byes and Leg Byes changes
				else if (BALL_TYPE_NOBALL_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {
					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2Byes += ball.getRuns() - 1;
					}

				} else if (BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {

					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2LByes += ball.getRuns() - 1;
					}

				}
				// --- Byes and Leg Byes changes

				else if (BALL_TYPE_LEG_BYE.equals(ball.getBallType())) {
					t2LByes += ball.getRuns();
					if (ball.getBall() <= totalBallInOver) {
						t2Balls += 1;
						t2_1Balls += 1;
					}
					t2_1LByes += ball.getRuns();

				} else if (BALL_TYPE_POSITIVE_PENALTY.equals(ball.getBallType())
						|| BALL_TYPE_NEGATIVE_PENALTY.equals(ball.getBallType())) {
					t2Penalty += ball.getRuns();
					t2_1Penalty += ball.getRuns();
				} else if (BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {
					if (ball.getBall() <= 6) {
						t2Balls += 1;
						t2_1Balls += 1;
					}
					if (BALL_TYPE_GOOD_NOBALL_BYE.equalsIgnoreCase(ball.getBallType())) {
						t2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
					if (BALL_TYPE_GOOD_NOBALL_LEG_BYE.equalsIgnoreCase(ball.getBallType())) {
						t2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
				}

				if (BALL_TYPE_GOODBALL.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t2Balls += 1;
						t2_1Balls += 1;
					}
				}

				if (BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t2Balls += 1;
						t2_1Balls += 1;
					}
				}

				if (CommonUtility.isNullOrEmpty(ball.getOutMethod()) && innings2LastBall != null 
						&& innings2LastBall.getBallId() == ball.getBallId()) {
					
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t2Total);
					partnership.setTeamBalls((short) t2Balls);
					partnership.setOut(false);
					partnership.setPartnershipTotalRuns((short)(t2Total-oldPS2Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2Balls-oldPS2Dto.getTeamBalls()));
					
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t2Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					partnershipMap.put(ball.getInningsNumber() + "-" + t2Wickets, partnershipList);
					
					oldPS2Dto = partnership;
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod()) && !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					t2Wickets += 1;
					t2_1Wickets += 1;
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t2Total);
					partnership.setTeamBalls((short) t2Balls);
					partnership.setPartnershipTotalRuns((short)(t2Total-oldPS2Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2Balls-oldPS2Dto.getTeamBalls()));
					
					List<PartnershipDto> partnershipList = new ArrayList<PartnershipDto>();
					partnershipList.add(partnership);
					
					oldPS2Dto = partnership;

					partnershipMap.put(ball.getInningsNumber() + "-" + t2Wickets, partnershipList);
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod()) && OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t2Total);
					partnership.setTeamBalls((short) t2Balls);
					partnership.setRetired(true);
					partnership.setPartnershipTotalRuns((short)(t2Total-oldPS2Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2Balls-oldPS2Dto.getTeamBalls()));
					
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t2Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					
					oldPS2Dto = partnership;
				}
				
				if ((ball.isValidBall() && ball.getBall() == totalBallInOver) || isLastBallOfInnings(balls, ball)) {
					OverDto overDto = populateOverStats(balls, ball, batsMan, runner, bowler);
					overDto.setMatchTotal((short) t2Total);
					overDto.setMatchWickets((short) t2Wickets);
					overDto.setTeamName((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwoName()
							: match.getTeamOneName());
					overMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}
				if(match.getSeriesType().contains("Test") || match.getSeriesType().contains("2X")) {
					if (innings2LastBall != null && innings2LastBall.getBallId() == ball.getBallId()) {
						playerRunsMap = new HashMap<Integer, Short>();
						bowlerBallsMap = new HashMap<Integer, Short>();
					}
				}
				
			} else if ((match.getIsFollowon() <= 0 && ball.getInningsNumber() == 3)
					|| (match.getIsFollowon() > 0 && ball.getInningsNumber() == 4)) {
				t1_2Total += ball.getRuns();
				t1Total += ball.getRuns();
				BattingDto batsMan = getBatsMan(t1Batting_2, match, ball.getBatsman(), match.getBattingFirst());
				BattingDto runner = getBatsMan(t1Batting_2, match, ball.getRunner(), match.getBattingFirst());
				BowlingDto bowler = getBowler(t2Bowling_2, match, ball,
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());

				if (isHatricOnThisBall && bowler != null) {
					bowler.setHattricks(bowler.getHattricks() + 1);
				}

				if (BALL_TYPE_BYE.equals(ball.getBallType())) {
					t1_2Byes += ball.getRuns();
					t1_2Balls += 1;
				} else if (BALL_TYPE_NOBALL.equals(ball.getBallType()) && ball.getRuns() > 1) {

					if (ball.getNoBallCustomRuns() > 1) {
						t1_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1_2Byes += ball.getRuns() - 1;
					}

					// t1_2Byes += ball.getRuns() - 1;
				}

				// Byes and Leg Byes changes
				else if (BALL_TYPE_NOBALL_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {

					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t1_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1_2Byes += ball.getRuns() - 1;
					}

				} else if (BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {

					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t1_2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t1_2LByes += ball.getRuns() - 1;
					}

				}
				// --- Byes and Leg Byes changes

				else if (BALL_TYPE_LEG_BYE.equals(ball.getBallType())) {
					t1_2LByes += ball.getRuns();
					t1_2Balls += 1;
				} else if (BALL_TYPE_POSITIVE_PENALTY.equals(ball.getBallType())
						|| BALL_TYPE_NEGATIVE_PENALTY.equals(ball.getBallType())) {
					t1_2Penalty += ball.getRuns();
				} else if (BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {
					t1_2Balls += 1;
					if (BALL_TYPE_GOOD_NOBALL_BYE.equalsIgnoreCase(ball.getBallType())) {
						t1_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
					if (BALL_TYPE_GOOD_NOBALL_LEG_BYE.equalsIgnoreCase(ball.getBallType())) {
						t1_2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
				}

				if (BALL_TYPE_GOODBALL.equals(ball.getBallType())) {
					t1_2Balls += 1;
				}
				if (BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t1_2Balls += 1;
					}
				}

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, balls, t3);
				
				if (CommonUtility.isNullOrEmpty(ball.getOutMethod()) && innings3LastBall != null 
						&& innings3LastBall.getBallId() == ball.getBallId()) {
					
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1_2Total);
					partnership.setTeamBalls((short) t1_2Balls);
					partnership.setOut(false);
					partnership.setPartnershipTotalRuns((short)(t1_2Total-oldPS3Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1_2Balls-oldPS3Dto.getTeamBalls()));
					
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t1_2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t1_2Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					oldPS3Dto = partnership;
					partnershipMap.put(ball.getInningsNumber() + "-" + t1_2Wickets, partnershipList);
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())
						&& !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					t1_2Wickets += 1;
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1_2Total);
					partnership.setTeamBalls((short) t1_2Balls);
					partnership.setPartnershipTotalRuns((short)(t1_2Total-oldPS3Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1_2Balls-oldPS3Dto.getTeamBalls()));
					List<PartnershipDto> partnershipList = new ArrayList<PartnershipDto>();
					partnershipList.add(partnership);
					oldPS3Dto = partnership;
					partnershipMap.put(ball.getInningsNumber() + "-" + t1_2Wickets, partnershipList);
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())
						&& OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1_2Total);
					partnership.setTeamBalls((short) t1_2Balls);
					partnership.setOut(false);
					partnership.setPartnershipTotalRuns((short)(t1_2Total-oldPS3Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t1_2Balls-oldPS3Dto.getTeamBalls()));
					partnership.setRetired(true);
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t1_2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t1_2Wickets, partnershipList);
					}
					oldPS3Dto = partnership;
					partnershipList.add(partnership);
				}

				if ((ball.isValidBall() && ball.getBall() == totalBallInOver) || isLastBallOfInnings(balls, ball)) {
					OverDto overDto = populateOverStats(balls, ball, batsMan, runner, bowler);
					overDto.setMatchTotal((short) t1_2Total);
					overDto.setMatchWickets((short) t1_2Wickets);
					overDto.setTeamName((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamOneName(): match.getTeamTwoName());
					overMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}

			} else if ((match.getIsFollowon() <= 0 && ball.getInningsNumber() == 4)
					|| (match.getIsFollowon() > 0 && ball.getInningsNumber() == 3)) {
				t2_2Total += ball.getRuns();
				t2Total += ball.getRuns();
				BattingDto batsMan = getBatsMan(t2Batting_2, match, ball.getBatsman(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());
				BattingDto runner = getBatsMan(t2Batting_2, match, ball.getRunner(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());
				BowlingDto bowler = getBowler(t1Bowling_2, match, ball, match.getBattingFirst());

				if (isHatricOnThisBall && bowler != null) {
					bowler.setHattricks(bowler.getHattricks() + 1);
				}

				if (BALL_TYPE_BYE.equals(ball.getBallType())) {
					t2_2Byes += ball.getRuns();
					t2_2Balls += 1;
				} else if (BALL_TYPE_NOBALL.equals(ball.getBallType()) && ball.getRuns() > 1) {
					if (ball.getNoBallCustomRuns() > 1) {
						t2_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2_2Byes += ball.getRuns() - 1;
					}
					// t2_2Byes += ball.getRuns() - 1;
				}
				// Byes and Leg Byes changes
				else if (BALL_TYPE_NOBALL_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {
					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t2_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2_2Byes += ball.getRuns() - 1;
					}

				} else if (BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType()) && ball.getRuns() > 1) {
					// if No Balls Runs more then one run
					if (ball.getNoBallCustomRuns() > 1) {
						t2_2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					} else {
						t2_2LByes += ball.getRuns() - 1;
					}

				}
				// --- Byes and Leg Byes changes

				else if (BALL_TYPE_LEG_BYE.equals(ball.getBallType())) {
					t2_2LByes += ball.getRuns();
					t2_2Balls += 1;
				} else if (BALL_TYPE_POSITIVE_PENALTY.equals(ball.getBallType())
						|| BALL_TYPE_NEGATIVE_PENALTY.equals(ball.getBallType())) {
					t2_2Penalty += ball.getRuns();
				} else if (BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {
					t2_2Balls += 1;
					if (BALL_TYPE_GOOD_NOBALL_BYE.equalsIgnoreCase(ball.getBallType())) {
						t2_2Byes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
					if (BALL_TYPE_GOOD_NOBALL_LEG_BYE.equalsIgnoreCase(ball.getBallType())) {
						t2_2LByes += ball.getRuns() - ball.getNoBallCustomRuns();
					}
				}

				if (BALL_TYPE_GOODBALL.equals(ball.getBallType())) {
					t2_2Balls += 1;
				}
				if (BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType())) {
					if (ball.getBall() <= totalBallInOver) {
						t2_2Balls += 1;
					}
				}

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, balls, t4);

				if (CommonUtility.isNullOrEmpty(ball.getOutMethod()) && innings4LastBall != null 
						&& innings4LastBall.getBallId() == ball.getBallId()) {
					
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t2_2Total);
					partnership.setTeamBalls((short) t2_2Balls);
					partnership.setOut(false);
					partnership.setPartnershipTotalRuns((short)(t2_2Total-oldPS4Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2_2Balls-oldPS4Dto.getTeamBalls()));
					
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t2_2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t2_2Wickets, partnershipList);
					}
					partnershipList.add(partnership);
					partnershipMap.put(ball.getInningsNumber() + "-" + t2_2Wickets, partnershipList);
					
					oldPS4Dto = partnership;
					
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())&& !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					
					t2_2Wickets += 1;

					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t2_2Total);
					partnership.setTeamBalls((short) t2_2Balls);
					partnership.setPartnershipTotalRuns((short)(t2_2Total-oldPS4Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2_2Balls-oldPS4Dto.getTeamBalls()));
					List<PartnershipDto> partnershipList = new ArrayList<PartnershipDto>();
					partnershipList.add(partnership);
					oldPS4Dto = partnership;
					partnershipMap.put(ball.getInningsNumber() + "-" + t2_2Wickets, partnershipList);
				} else if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())
						&& OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					PartnershipDto partnership = preparePartnershipRecord(ball, batsMan, runner, playerRunsMap, bowlerBallsMap);
					partnership.setTeamTotal((short) t1_2Total);
					partnership.setTeamBalls((short) t2_2Balls);
					partnership.setPartnershipTotalRuns((short)(t2_2Total-oldPS4Dto.getTeamTotal()));
					partnership.setPartnershipTotalBalls((short)(t2_2Balls-oldPS4Dto.getTeamBalls()));
					partnership.setRetired(true);
					List<PartnershipDto> partnershipList = partnershipMap.get(ball.getInningsNumber() + "-" + t1_2Wickets);
					if (partnershipList == null) {
						partnershipList = new ArrayList<PartnershipDto>();
						partnershipMap.put(ball.getInningsNumber() + "-" + t1_2Wickets, partnershipList);
					}
					oldPS4Dto = partnership;
					partnershipList.add(partnership);

				}

				if ((ball.isValidBall() && ball.getBall() == totalBallInOver) || isLastBallOfInnings(balls, ball)) {
					OverDto overDto = populateOverStats(balls, ball, batsMan, runner, bowler);
					overDto.setMatchTotal((short) t2_2Total);
					overDto.setMatchWickets((short) t2_2Wickets);
					overDto.setTeamName((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwoName()
							: match.getTeamOneName());
					overMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}
			}
		}
		match.setT1byes(t1Byes);
		match.setT1lbyes(t1LByes);
		match.setT1balls(t1Balls);
		match.setT1wickets(t1Wickets);
		match.setT1total(t1Total);
		match.setT1penalty(t1Penalty);

		match.setT1_1byes(t1_1Byes);
		match.setT1_1lbyes(t1_1LByes);
		match.setT1_1balls(t1_1Balls);
		match.setT1_1wickets(t1_1Wickets);
		match.setT1_1total(t1_1Total);
		match.setT1_1penalty(t1_1Penalty);

		match.setT1_2byes(t1_2Byes);
		match.setT1_2lbyes(t1_2LByes);
		match.setT1_2balls(t1_2Balls);
		match.setT1_2wickets(t1_2Wickets);
		match.setT1_2total(t1_2Total);
		match.setT1_2penalty(t1_2Penalty);

		match.setT2byes(t2Byes);
		match.setT2lbyes(t2LByes);
		match.setT2balls(t2Balls);
		match.setT2wickets(t2Wickets);
		match.setT2total(t2Total);
		match.setT2penalty(t2Penalty);

		match.setT2_1byes(t2_1Byes);
		match.setT2_1lbyes(t2_1LByes);
		match.setT2_1balls(t2_1Balls);
		match.setT2_1wickets(t2_1Wickets);
		match.setT2_1total(t2_1Total);
		match.setT2_1penalty(t2_1Penalty);

		match.setT2_2byes(t2_2Byes);
		match.setT2_2lbyes(t2_2LByes);
		match.setT2_2balls(t2_2Balls);
		match.setT2_2wickets(t2_2Wickets);
		match.setT2_2total(t2_2Total);
		match.setT2_2penalty(t2_2Penalty);

		typeOfRuns.put("t1", t1);
		typeOfRuns.put("t2", t2);
		typeOfRuns.put("t3", t3);
		typeOfRuns.put("t4", t4);

		Map<String, Object> matchRecords = new HashMap<String, Object>();

		addInningsToBattingRecords(t1Batting, t2Batting, 1);
		addInningsToBowlingRecords(t1Bowling, t2Bowling, 1);
		addInningsToBattingRecords(t1Batting_2, t2Batting_2, 2); // TODO : Check if innings affecting anywhere else.
		addInningsToBowlingRecords(t1Bowling_2, t2Bowling_2, 2); // TODO : Check if innings affecting anywhere else.

		matchRecords.put("t1Batting", t1Batting);
		matchRecords.put("t2Batting", t2Batting);
		matchRecords.put("t1Bowling", t1Bowling);
		matchRecords.put("t2Bowling", t2Bowling);
		matchRecords.put("t1Batting_2", t1Batting_2);
		matchRecords.put("t2Batting_2", t2Batting_2);
		matchRecords.put("t1Bowling_2", t1Bowling_2);
		matchRecords.put("t2Bowling_2", t2Bowling_2);

		matchRecords.put("overMap", overMap);
		matchRecords.put("partnershipMap", partnershipMap);
		matchRecords.put("typeOfRuns", typeOfRuns);

		// for in progress 2X games
		if (match.getClubID() != 0 && match.getLeagueId() != 0) {
			int leagueId = match.getLeagueId();
			try {
				ClubDto club = ClubFactory.getClub(match.getClubID()); // please verify with team
				LeagueDto league = club.getLeague(leagueId);
				if (league != null && "2X".equals(league.getSeriesType())) {
					Collections.sort(balls, new BallDtoComparator());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error(
						e.getMessage() + " consolidateMatchRecords MatchID - " + match != null ? match.getMatchID() + ""
								: " Match not found " + " and ClubId " + match != null ? match.getClubID() + ""
										: " Club not found ");
			}
		}
		
		matchRecords.put("superOverMap", getSuperOverMap(match, superBalls, superOverMap, 
				totalBallInOver, matchRecords));
		
		matchRecords.put("superOverInningsNum1", superOverInningsNum1);
		matchRecords.put("superOverInningsNum2", superOverInningsNum2);
		
		return matchRecords;
	}
	
	public static Map<String, OverDto> getSuperOverMap(MatchDto match, List<BallDto> superBalls, 
			Map<String, OverDto> superOverMap, int totalBallInOver, Map<String, Object> matchRecords) {
	
		int t1Total = 0;
		int t2Total = 0;
		int t1Wickets = 0;
		int t2Wickets = 0;
		
		List<BattingDto> t1Batting = new ArrayList<BattingDto>();
		List<BattingDto> t2Batting = new ArrayList<BattingDto>();
		List<BowlingDto> t1Bowling = new ArrayList<BowlingDto>();
		List<BowlingDto> t2Bowling = new ArrayList<BowlingDto>();
		
		TypeOfRuns t1 = new TypeOfRuns();
		TypeOfRuns t2 = new TypeOfRuns();
		
		String team1Name = "";
		String team2Name = "";
		
		for (BallDto ball : superBalls) {

			if (ball.getInningsNumber() == 1) {

				t1Total += ball.getRuns();
				
				if (!CommonUtility.isNullOrEmpty(ball.getOutMethod()) && !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					t1Wickets += 1;
				} 

				BattingDto batsMan = getBatsMan(t1Batting, match, ball.getBatsman(), match.getBattingFirst());
				BattingDto runner = getBatsMan(t1Batting, match, ball.getRunner(), match.getBattingFirst());
				BowlingDto bowler = getBowler(t2Bowling, match, ball, (match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, superBalls, t1);
				
				if ((((ball.isValidBall() || CommonLogicFB.BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType()))
						&& ball.getBall() == totalBallInOver) || isLastBallOfInnings(superBalls, ball))) {

					OverDto overDto = populateOverStats(superBalls, ball, batsMan, runner, bowler);
					if(ball.getBatsman()>0 && ball.getIsSuperOver()>0){
			        	if(match.getPlayers1().contains(ball.getBatsman())){
			        		team1Name = match.getTeamOneName();
			        	}else if(match.getPlayers2().contains(ball.getBatsman())){
			        		team1Name = match.getTeamTwoName();
			        	}
			        }
					overDto.setTeamName(team1Name);
					overDto.setMatchTotal((short) t1Total);
					overDto.setMatchWickets((short) t1Wickets);
					superOverMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}
				
			} else if (ball.getInningsNumber() == 2) {
				
				t2Total += ball.getRuns();
				
				if (!CommonUtility.isNullOrEmpty(ball.getOutMethod()) && !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
					t2Wickets += 1;
				} 

				BattingDto batsMan = getBatsMan(t2Batting, match, ball.getBatsman(), match.getBattingFirst());
				BattingDto runner = getBatsMan(t2Batting, match, ball.getRunner(), match.getBattingFirst());
				BowlingDto bowler = getBowler(t1Bowling, match, ball, (match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamTwo());

				addUpBowlerAndBatsman(ball, batsMan, runner, bowler, superBalls, t2);

				if ((((ball.isValidBall() || CommonLogicFB.BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType()))
						&& ball.getBall() == totalBallInOver) || isLastBallOfInnings(superBalls, ball))) {

					OverDto overDto = populateOverStats(superBalls, ball, batsMan, runner, bowler);
					if(ball.getBatsman()>0 && ball.getIsSuperOver()>0){
			        	if(match.getPlayers1().contains(ball.getBatsman())){
			        		team2Name = match.getTeamOneName();
			        	}else if(match.getPlayers2().contains(ball.getBatsman())){
			        		team2Name = match.getTeamTwoName();
			        	}
			        }
					overDto.setTeamName(team2Name);
					overDto.setMatchTotal((short) t2Total);
					overDto.setMatchWickets((short) t2Wickets);
					superOverMap.put(ball.getInningsNumber() + "-" + ball.getOver(), overDto);
				}
			}
		}
		addInningsToBattingRecords(t1Batting, t2Batting, 1);
		addInningsToBowlingRecords(t1Bowling, t2Bowling, 1);
		
		matchRecords.put("t1BattingSuper", t1Batting);
		matchRecords.put("t2BattingSuper", t2Batting);
		matchRecords.put("t1BowlingSuper", t1Bowling);
		matchRecords.put("t2BowlingSuper", t2Bowling);
		
		return superOverMap;
	}

	public static void addInningsToBowlingRecords(List<BowlingDto> t1Bowling, List<BowlingDto> t2Bowling, int innings) {
		if (t1Bowling != null) {
			for (BowlingDto bowling : t1Bowling) {
				bowling.setInnings(innings);
			}
		}
		if (t2Bowling != null) {
			for (BowlingDto bowling : t2Bowling) {
				bowling.setInnings(innings);
			}
		}
	}

	public static void addInningsToBattingRecords(List<BattingDto> t1Batting, List<BattingDto> t2Batting, int innings) {
		if (t1Batting != null) {
			int i= 1;
			for (BattingDto batting : t1Batting) {
				batting.setInnings(innings);
				batting.setBattingPosition(i++);
			}
		}
		if (t2Batting != null) {
			int i= 1;
			for (BattingDto batting : t2Batting) {
				batting.setInnings(innings);
				batting.setBattingPosition(i++);
			}
		}

	}

	private static PartnershipDto preparePartnershipRecord(BallDto ball, BattingDto batsMan, BattingDto runner,
			Map<Integer, Short> playerRunsMap, Map<Integer, Short> playerBallsMap) {

		BattingDto outPerson = null;
		BattingDto otherPerson = null;

		if (ball != null && batsMan != null && ball.getOutPerson() == batsMan.getPlayerID()) {
			outPerson = batsMan;
			otherPerson = runner;
		} else if (ball != null && runner != null && ball.getOutPerson() == runner.getPlayerID()) {
			outPerson = runner;
			otherPerson = batsMan;
		} else if(ball != null && batsMan != null && ball.getOutPerson() == 0) {
			outPerson = batsMan;
			otherPerson = runner;
		}

		PartnershipDto partnership = new PartnershipDto();
		if (outPerson != null) {
			int outPersonRunsOld = 0;
			int outPersonBallssOld = 0;
			if (playerRunsMap.size() > 0 && playerRunsMap.get(outPerson.getPlayerID()) != null) {
				outPersonRunsOld = playerRunsMap.get(outPerson.getPlayerID());
			}
			if (playerBallsMap.size() > 0 && playerBallsMap.get(outPerson.getPlayerID()) != null) {
				outPersonBallssOld = playerBallsMap.get(outPerson.getPlayerID());
			}
			partnership.setOutPlayerId(outPerson.getPlayerID());
			partnership.setProfilepic_file_path(outPerson.getProfilepic_file_path());
			playerRunsMap.put(outPerson.getPlayerID(), (short) outPerson.getRunsScored());
			playerBallsMap.put(outPerson.getPlayerID(), (short) outPerson.getBallsFaced());
			partnership.setOutPlayerScore((short) (outPerson.getRunsScored() - outPersonRunsOld));
			partnership.setOutPlayerBalls((short) (outPerson.getBallsFaced() - outPersonBallssOld));
		}

		if (otherPerson != null) {
			int otherPersonRunsOld = 0;
			if (playerRunsMap.size() > 0 && playerRunsMap.get(otherPerson.getPlayerID()) != null) {
				otherPersonRunsOld = playerRunsMap.get(otherPerson.getPlayerID());
			}
			int otherPersonBallssOld = 0;
			if (playerBallsMap.size() > 0 && playerBallsMap.get(otherPerson.getPlayerID()) != null) {
				otherPersonBallssOld = playerBallsMap.get(otherPerson.getPlayerID());
			}
			partnership.setOtherPlayerId(otherPerson.getPlayerID());
			partnership.setProfilepic_file_path(otherPerson.getProfilepic_file_path());
			playerRunsMap.put(otherPerson.getPlayerID(), (short) otherPerson.getRunsScored());
			playerBallsMap.put(otherPerson.getPlayerID(), (short) otherPerson.getBallsFaced());
			partnership.setOtherPlayerScore((short) (otherPerson.getRunsScored() - otherPersonRunsOld));
			partnership.setOtherPlayerBalls((short) (otherPerson.getBallsFaced()-otherPersonBallssOld));
		}
		
		partnership.setOverNumber((short) ball.getOver());
		partnership.setBallNumber((byte) ball.getBall());
		
		return partnership;
	}

	private static boolean isLastBallOfInnings(List<BallDto> balls, BallDto ball) {
		if (balls.indexOf(ball) == balls.size() - 1) {
			return true;
		} else if (ball.getInningsId() != balls.get(balls.indexOf(ball) + 1).getInningsId()) {
			return true;
		}
		return false;
	}

	private static OverDto populateOverStats(List<BallDto> balls, BallDto ball, BattingDto batsMan, BattingDto runner,
			BowlingDto bowler) {
		OverDto overDto = new OverDto();

		overDto.setOverNumber((short) ball.getOver());
		overDto.setInningsNumber(ball.getInningsNumber());
		overDto.setBatsman1(ball.getBatsman());
		overDto.setBatsman2(ball.getRunner());
		if (batsMan != null) {
			overDto.setBatsman1Balls((short) batsMan.getBallsFaced());
			overDto.setBatsman1Runs((short) batsMan.getRunsScored());
			overDto.setB1Sixers((short) batsMan.getSixers());
			overDto.setB1Fours((short) batsMan.getFours());
		}

		if (runner != null) {
			overDto.setBatsman2Balls((short) runner.getBallsFaced());
			overDto.setBatsman2Runs((short) runner.getRunsScored());
			overDto.setB2Fours((short) runner.getFours());
			overDto.setB2Sixers((short) runner.getSixers());
		}

		if (bowler != null) {
			overDto.setBowler1(bowler.getPlayerID());
			overDto.setBowler1maidens((short) bowler.getMaidens());
			overDto.setBowler1Balls((short) bowler.getBalls());
			overDto.setBowler1Runs((short) bowler.getRuns());
			overDto.setBowler1Wickets((short) bowler.getWickets());
		}

		getRunsOfOver(overDto, balls, balls.indexOf(ball));
		return overDto;
	}

	private static boolean checkForMaiden(List<BallDto> balls, int index) {
		BallDto ball = balls.get(index);
		int over = ball.getOver();
		int bowler = ball.getBowler();
		for (int i = index; i >= 0; i--) {
			BallDto ball_1 = balls.get(i);
			// For baby over - Two bowlers bowled same over, its not maiden.
			if (!BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(ball_1.getBallType()) && 
					ball_1.getOver() == over && bowler != ball_1.getBowler()) {
				return false;
			}

			if (ball_1.getOver() == over && (ball_1.getRuns() > 0)
					&& (!BALL_TYPE_LEG_BYE.equalsIgnoreCase(ball_1.getBallType())
							&& !BALL_TYPE_BYE.equalsIgnoreCase(ball_1.getBallType())
							&& !BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(ball_1.getBallType()))) {

				return false;
			}

			if (ball_1.getOver() != over) {
				break;
			}
		}
		return true;
	}

	private static void getRunsOfOver(OverDto overDto, List<BallDto> balls, int index) {
		BallDto ball = balls.get(index);
		int over = ball.getOver();
		int runs = 0;
		for (int i = index; i >= 0; i--) {
			BallDto ball1 = balls.get(i);
			if (ball1.getOver() != over || ball1.getInningsNumber() != ball.getInningsNumber()) {
				break;
			}
			runs += ball1.getRuns();
			overDto.getBalls().add(ball1.getRunsDisplay());
			if (ball1.isOut() && !OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {
				overDto.getOutBalls().add(ball1);
			}
		}

		overDto.setRuns((short) runs);
		Collections.reverse(overDto.getBalls());
	}

	private static void addUpBowlerAndBatsman(BallDto ball, BattingDto batsMan, BattingDto runner, BowlingDto bowler,
			List<BallDto> balls, TypeOfRuns t1) {

		if (BALL_TYPE_BOWLER_COUNT_BALL.equals(ball.getBallType())) {
			if (ball.getBall() <= 6) {
				addBallToBowler(bowler);
			}

		}

		if (BALL_TYPE_GOODBALL.equals(ball.getBallType())) {
			addBallToBatsMan(batsMan);
			if (ball.getBall() <= 6) {
				addBallToBowler(bowler);
			}

			addRunsToBatsman(batsMan, ball, t1);
			addRunsToBowler(bowler, ball.getRuns());
			if( ball.getRuns() == 0) {
				bowler.setDotBalls(bowler.getDotBalls() + 1);
			}
		}

		if (bowler != null
				&& (BALL_TYPE_WIDE.equals(ball.getBallType()) || BALL_TYPE_GOOD_WIDE.equals(ball.getBallType()))) {
			bowler.setWides(bowler.getWides() + ball.getRuns());
			addRunsToBowler(bowler, ball.getRuns());
		}

		if (bowler != null && (BALL_TYPE_NOBALL.equals(ball.getBallType())
				|| BALL_TYPE_NOBALL_BYE.equals(ball.getBallType()) || BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType()))) {

			if (ball.getNoBallCustomRuns() > 1) {
				bowler.setNoBalls(bowler.getNoBalls() + ball.getNoBallCustomRuns());
				addRunsToBowler(bowler, ball.getNoBallCustomRuns());
			} else {
				bowler.setNoBalls(bowler.getNoBalls() + 1);
				addRunsToBowler(bowler, 1);
			}
			addBallToBatsMan(batsMan);
		}
		if (bowler != null && (BALL_TYPE_NOBALL_BAT.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType()))) {
			if (BALL_TYPE_NOBALL_BAT.equals(ball.getBallType()) && ball.getNoBallCustomRuns() > 1) {
				bowler.setNoBalls(bowler.getNoBalls() + ball.getNoBallCustomRuns());
			} else {
				bowler.setNoBalls(bowler.getNoBalls() + 1);
			}
			addRunsToBowler(bowler, ball.getRuns());
			addBallToBatsMan(batsMan);
			addRunsToBatsman(batsMan, ball, t1);
		}

		if (BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType()) || BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())) {
			
			if (ball.getBall() <= 6) {
				addBallToBowler(bowler);
				if (!BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {
					addBallToBatsMan(batsMan);
					if (!BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())) {
						addRunsToBowler(bowler, ball.getNoBallCustomRuns());
					}
				}
				if (BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType()) 
						|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
						|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())) {

					if (ball.getNoBallCustomRuns() > 0) {
						bowler.setNoBalls(bowler.getNoBalls() + ball.getNoBallCustomRuns());
					} else {
						bowler.setNoBalls(bowler.getNoBalls() + 1);
					}
				}
			}
		}

		if (BALL_TYPE_BYE.equals(ball.getBallType()) || BALL_TYPE_LEG_BYE.equals(ball.getBallType())) {
			addBallToBatsMan(batsMan);

			if (ball.getBall() <= 6) {
				addBallToBowler(bowler);
			}
		}

		if (!CommonUtility.isNullOrEmpty(ball.getOutMethod())) {
			BattingDto outPerson = null;
			if (ball != null && batsMan != null && ball.getOutPerson() == batsMan.getPlayerID()) {
				outPerson = batsMan;
			} else if (ball != null && runner != null && ball.getOutPerson() == runner.getPlayerID()) {
				outPerson = runner;
			}

			if (outPerson != null) {
				
				outPerson.setHowOut(ball.getOutMethod());
				outPerson.setWicketTaker1("" + ball.getWicketTaker1());
				outPerson.setWicketTaker2("" + ball.getWicketTaker2());
				outPerson.setIsOut(OUT_METHOD_RETIRED.equals(ball.getOutMethod()) ? "0" : "1");
				if (!OUT_METHOD_RETIRED.equals(ball.getOutMethod()) && !OUT_METHOD_RUN_OUT.equals(ball.getOutMethod())
						&& !OUT_METHOD_MANKADING.equals(ball.getOutMethod())
						&& !OUT_METHOD_TIMED_OUT.equals(ball.getOutMethod())
						&& !OUT_METHOD_OBSTRUCTING_THE_FIELD.equals(ball.getOutMethod())
						&& !OUT_METHOD_RETIRED_OUT.equals(ball.getOutMethod())) {
					
					if (bowler != null)
						bowler.setWickets(bowler.getWickets() + 1);
				}
				if ((ball.getWicketTaker1() == -1 || ball.getWicketTaker2() == -1)
						|| !CommonUtility.isNullOrEmptyOrNULL(ball.getSubtPlyrName())) {
					outPerson.setSubtPlyrName(ball.getSubtPlyrName());
				}
			}
		}
		if (isLegalBall(ball) && ball.getBall() == 6 && checkForMaiden(balls, balls.indexOf(ball))) {
			if (bowler != null)
				bowler.setMaidens(bowler.getMaidens() + 1);

		}
		if (CommonUtility.isNullOrEmpty(ball.getOutMethod())) {
			if (batsMan != null) {
				batsMan.setHowOut("");
				batsMan.setWicketTaker1("");
				batsMan.setWicketTaker2("");
				batsMan.setIsOut("0");
			}
			if (runner != null) {
				runner.setHowOut("");
				runner.setWicketTaker1("");
				runner.setWicketTaker2("");
				runner.setIsOut("0");
			}
		}

	}

	public static boolean isLegalBall(BallDto ball) {
		return BALL_TYPE_GOODBALL.equals(ball.getBallType()) || BALL_TYPE_GOOD_WIDE.equals(ball.getBallType())
				|| BALL_TYPE_BYE.equals(ball.getBallType()) || BALL_TYPE_LEG_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType());
	}

	private static void addRunsToBowler(BowlingDto bowler, int runs) {
		if (bowler != null)
			bowler.setRuns(bowler.getRuns() + runs);
	}

	private static void addRunsToBatsman(BattingDto batsMan, BallDto ball, TypeOfRuns t1) {
		if (BALL_TYPE_NOBALL_BAT.equals(ball.getBallType()) || BALL_TYPE_NOBALL.equals(ball.getBallType())
				|| BALL_TYPE_NOBALL_BYE.equals(ball.getBallType())
				|| BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL_BAT.equals(ball.getBallType())
				|| BALL_TYPE_GOOD_NOBALL.equals(ball.getBallType())) {

			if (batsMan != null && !BALL_TYPE_NOBALL_BYE.equals(ball.getBallType())
					&& !BALL_TYPE_NOBALL_LEG_BYE.equals(ball.getBallType())
					&& !BALL_TYPE_GOOD_NOBALL_BYE.equals(ball.getBallType())
					&& !BALL_TYPE_GOOD_NOBALL_LEG_BYE.equals(ball.getBallType())) {

				// if No Balls Runs more then one run
				if (ball.getNoBallCustomRuns() > 1) {
					batsMan.setRunsScored(batsMan.getRunsScored() + ball.getRuns() - ball.getNoBallCustomRuns());
				} else {
					batsMan.setRunsScored(batsMan.getRunsScored() + ball.getRuns() - 1);
				}

			}
			t1.addRuns(ball.getRuns() - 1);
		} else {
			if (batsMan != null) {
				batsMan.setRunsScored(batsMan.getRunsScored() + ball.getRuns());
				t1.addRuns(ball.getRuns());
			}
		}
		if (batsMan != null && ball.isFour() && batsMan != null) {
			batsMan.setFours(batsMan.getFours() + 1);
		} else if (batsMan != null && ball.isSix() && batsMan != null) {
			batsMan.setSixers(batsMan.getSixers() + 1);
		}
	}

	private static void addBallToBowler(BowlingDto bowler) {
		if (bowler != null)
			bowler.setBalls(bowler.getBalls() + 1);
	}

	private static void addBallToBatsMan(BattingDto batsMan) {
		if (batsMan != null)
			batsMan.setBallsFaced(batsMan.getBallsFaced() + 1);
	}

	private static BowlingDto getBowler(List<BowlingDto> bowling, MatchDto match, BallDto ball, int teamId) {
		if (!(ball.getBowler() > 0)) {
			if (!ball.getBallType().equalsIgnoreCase(BALL_TYPE_AUTO_COMMENT_BALL)) {
				// Customizing bowler as not null
				BowlingDto bowler = new BowlingDto();
				bowler.setMatchID(match.getMatchID());
//			bowler.setPlayerID(ball.getBowler());
				bowler.setTeamId(teamId);
				bowler.setFirstName("Unknown");
				bowler.setLastName("Bowler");
				bowler.setPlayerID(-1);
				bowling.add(bowler);
				return bowler;
			}
		}
		for (BowlingDto bowler : bowling) {
			if (bowler.getPlayerID() == ball.getBowler()) {
				return bowler;
			}
		}
		BowlingDto bowler = new BowlingDto();
		bowler.setMatchID(match.getMatchID());
		bowler.setPlayerID(ball.getBowler());
		bowler.setTeamId(teamId);
		bowling.add(bowler);
		return bowler;
	}

	private static BattingDto getBatsMan(List<BattingDto> batting, MatchDto match, int playerId, int teamId) {
		if (!(playerId > 0)) {
			return null;
		}
		for (BattingDto batsMan : batting) {
			if (batsMan.getPlayerID() == playerId) {
				return batsMan;
			}
		}

		BattingDto batsMan = new BattingDto();
		batsMan.setMatchID(match.getMatchID());
		batsMan.setPlayerID(playerId);
		batsMan.setTeamId(teamId);
		batsMan.setIsOut("0");
		batting.add(batsMan);
		return batsMan;
	}

	public static void addDidNotBatPlayers(List<BattingDto> teamBatting, List<Integer> list, List<PlayerDto> players) {
		for (Integer playerId : list) {
			if (playerId != null) {
				boolean batted = didPlayerBat(teamBatting, playerId);
				if (!batted) {
					PlayerDto player = getPlayerFromList(playerId, players);
					BattingDto batting = new BattingDto();
					if (player != null) {
						batting.setPlayerID(player.getPlayerID());
						batting.setFirstName(player.getFirstName());
						batting.setLastName(player.getLastName());
						batting.setProfilepic_file_path(player.getProfilepic_file_path());
						batting.setNickName(player.getNickName());
						//batting.setInnings(innings);
						teamBatting.add(batting);
					}
				}
			}
		}

	}
	
	public static void addDidNotBatPlayers(List<BattingDto> teamBatting, List<Integer> list, List<PlayerDto> players, int inningsNumber) {
		for (Integer playerId : list) {
			if (playerId != null) {
				boolean batted = didPlayerBat(teamBatting, playerId, inningsNumber);
				if (!batted) {
					PlayerDto player = getPlayerFromList(playerId, players);
					BattingDto batting = new BattingDto();
					if (player != null) {
						batting.setPlayerID(player.getPlayerID());
						batting.setFirstName(player.getFirstName());
						batting.setLastName(player.getLastName());
						batting.setProfilepic_file_path(player.getProfilepic_file_path());
						batting.setNickName(player.getNickName());
						batting.setInnings(inningsNumber);
						teamBatting.add(batting);
					}
				}
			}
		}

	}

	private static PlayerDto getPlayerFromList(Integer playerId, List<PlayerDto> players) {
		for (PlayerDto player : players) {
			if (playerId.equals(player.getPlayerID())) {
				return player;
			}
		}
		return null;
	}

	private static boolean didPlayerBat(List<BattingDto> teamBatting, Integer playerId) {
		for (BattingDto batting : teamBatting) {
			if (playerId.equals(batting.getPlayerID())) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean didPlayerBat(List<BattingDto> teamBatting, Integer playerId, int inningsNum) {
		for (BattingDto batting : teamBatting) {
			if(batting.getInnings() == inningsNum) {
				if (playerId.equals(batting.getPlayerID())) {
					return true;
				}
			}
		}
		return false;
	}

	public static void setPlayerDetailsForBatsman(List<BattingDto> teamBatting, List<PlayerDto> players) {
		for (BattingDto batting : teamBatting) {
			for (PlayerDto player : players) {
				if (batting.getPlayerID() == player.getPlayerID()) {
					batting.setFirstName(player.getFirstName());
					batting.setLastName(player.getLastName());
					batting.setNickName(player.getNickName());
					batting.setProfilepic_file_path(player.getProfilepic_file_path());
					batting.setBattingStyle(player.getBattingStyle());
				}
			}
		}
	}

	public static void setPlayerDetailsForBowler(List<BowlingDto> teamBowling, List<PlayerDto> players) {
		for (BowlingDto bowling : teamBowling) {
			for (PlayerDto player : players) {
				if (bowling.getPlayerID() == player.getPlayerID()) {
					bowling.setFirstName(player.getFirstName());
					bowling.setLastName(player.getLastName());
					bowling.setNickName(player.getNickName());
					bowling.setProfilepic_file_path(player.getProfilepic_file_path());
					bowling.setBowlingStyle(player.getBowlingStyle());
				}
			}
		}
	}

	public static int calculateWinner(MatchDto match) {

		if (!match.isDls()) {
			if ((match.getT1total()) > (match.getT2total())) {
				return match.getBattingFirst();
			} else if ((match.getT1total()) < (match.getT2total())) {
				return ((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne());
			}
		} else {
			if ((match.getT2Target()) >= (match.getT2total())) {
				return match.getBattingFirst();
			} else if ((match.getT2Target() - 1) < (match.getT2total())) {
				return ((match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne());
			}
		}
		return 0;
	}
	
	public static String getFourthInningsRRRAtEndOfOver(MatchDto match, OverDto over) {
		
		int runsNeeded = 0;
		int remainingWickets = 0;
		String rrr = "";
		
		if (match.isDls()) {
			runsNeeded = (((match.getT2_1total()+match.getT2_2total())-match.getT1_1total())-over.getMatchTotal()+1);
			remainingWickets = match.getT1_2wickets() - over.getMatchWickets();
			rrr = CommonUtility.calculateRunRate(runsNeeded,match.getOvers()-over.getOverNumber()-1);			
		} else {
			runsNeeded = (((match.getT1_1total()+match.getT1_2total())-match.getT2_1total())-over.getMatchTotal()+1);
			remainingWickets = match.getT2_2wickets() - over.getMatchWickets();
			rrr = CommonUtility.calculateRunRate(runsNeeded,match.getOvers()-over.getOverNumber()-1);
		}
		
		String rrrEquation = runsNeeded+" runs needed in ";
		rrrEquation+= match.getOvers() - over.getOverNumber()-1 + " overs ("+(match.getOvers() - over.getOverNumber()-1)*6+" balls) with ";
		rrrEquation+= remainingWickets+" wickets remaining.  RRR: "+rrr;
		
		return rrrEquation;
	}

	public static boolean isUserCaptainOfaTeam(List<TeamDto> teams, int team1, int team2, UserDto user) {

		if (user != null && user.isTeamAdmin() && user.getTeams() != null) {
			if (user.getTeams().contains(team1) || user.getTeams().contains(team2)) {
				return true;
			}
		}

		if (teams == null || team1 == 0 || team2 == 0 || user.getPlayerID() == 0) {
			return false;
		}

		for (TeamDto team : teams) {
			if (team.getTeamID() == team1 || team.getTeamID() == team2) {
				if (team.getCaptain() == user.getPlayerID() || team.getViceCaptain() == user.getPlayerID()
						|| user.isCaptain(team.getTeamID(), team.getClubId())) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static int registerNewPlayer(ClubDtoLite club, PlayerDto dto, String user, 
			String addRequestFrom) throws Exception {
		
		int playerIdFromDB = 0;
		PlayerDto localPlayer = null;
		if(dto.getPlayerID() > 0) {
			localPlayer = PlayerFactory.getPlayerBasicDetails(dto.getPlayerID());
			playerIdFromDB = localPlayer != null && localPlayer.getPlayerID() > 0 ? localPlayer.getPlayerID() : 0;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			playerIdFromDB = PlayerFactory.globalPlayerCheckByEmail(dto.getEmail());
		}

		if (!CommonUtility.isNullOrEmpty(dto.getCustomId()) && club.getClubId() != 7683) {
			playerIdFromDB = PlayerFactory.globalPlayerCheckByCustomId(dto.getCustomId());
		}

		if (playerIdFromDB == 0) {
			int id = PlayerFactory.registerPlayer(dto, club.getClubId(), user);
			dto.setPlayerID(id);
			createLoginForUser(dto, club, addRequestFrom);
		} else {
			dto.setPlayerID(playerIdFromDB);
			PlayerDto clubPlayer = PlayerFactory.getPlayerById(playerIdFromDB, club.getClubId());
			if(clubPlayer == null) {
				PlayerFactory.addPlayerToClub(dto, club.getClubId(), user);
			}
			addExistingUserToClub(dto, club);
		}

		if (dto.getTeamId() != 0) {
			TeamFactory.addPlayerToTeam(dto.getPlayerID(), dto.getTeamId(), club.getClubId(), false, "NewPlayer");

			// if(dto.isInternalClubAdd()) {
			TeamDto team = TeamFactory.getTeamByTeamId(dto.getTeamId(), club.getClubId());
			if (team != null && team.getClubId() > 0) {
				ClubFactory.addPlayerToClub(dto.getPlayerID(), team.getClubId(), club.getClubId(), user);
			}
			// }
		}
		return dto.getPlayerID();
	}

	public static int registerNewUmpire(ClubDtoLite club, UmpireDto dto) throws Exception {
		int playerIdFromDB = 0;
		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			playerIdFromDB = UmpireFactory.globalUmpireCheckByEmail(dto.getEmail());
		}

		if (playerIdFromDB == 0) {
			int id = UmpireFactory.registerUmpire(dto, club.getClubId());
			dto.setUmpireID(id);
			createLoginForUser(dto, club);
		} else {
			dto.setUmpireID(playerIdFromDB);
			UmpireFactory.addUmpireToClub(dto, club.getClubId());
			addExistingUserToClub(dto, club);
		}

		return dto.getUmpireID();
	}

	private static void createLoginForUser(UmpireDto dto, ClubDtoLite club) throws Exception {
		UserDto user = new UserDto();
		boolean userExists = false;
		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			UserDto dbUser = UserFactory.getUserByEmail(dto.getEmail(), club.getClubId());
			if (dbUser != null) {
				userExists = true;
				user = dbUser;
			} else {
				user.setUserName(dto.getEmail());
			}
		} else {
			user.setUserName(dto.getFirstName() + "." + dto.getLastName());
		}
		if (!userExists) {
			user.setFname(dto.getFirstName());
			user.setLname(dto.getLastName());
			user.setUmpireID(dto.getUmpireID());
			user.setPassword(CommonUtility.getPassword(8));
			user.setEmail(dto.getEmail());
			user.setCountryCode(club.getCountry());

			if (user == null)
				UserFactory.registerUser(user, club.getClubId(), "Guest");
			else
				UserFactory.registerUser(user, club.getClubId(), Integer.toString(user.getUserID()));
		} else {
			if (user == null)
				UserFactory.updateUserUmpireId(dto.getUmpireID(), user.getUserID(), "Guest");
			else
				UserFactory.updateUserUmpireId(dto.getUmpireID(), user.getUserID(), Integer.toString(user.getUserID()));
		}
		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			if (club.isSCA() && !userExists) {
				NotificationHelper.sendWelcomeUmpireEmail(user, club,
						CommonUtility.getUmpireCoachScorerString(dto.getType()), false);
			} else if (!club.isSCA()) {
				NotificationHelper.sendWelcomeUmpireEmail(user, club,
						CommonUtility.getUmpireCoachScorerString(dto.getType()), true);
			}
		}
	}

	private static void addExistingUserToClub(UmpireDto dto, ClubDtoLite club) throws Exception {
		UserDto user = UserFactory.getUserByUmpireId(dto.getUmpireID());
		int clubId = club.getClubId();
		UserDto clubUser = UserFactory.getUserById(user.getUserID(), clubId);
		if (clubUser == null) {
			if (user == null)
				UserFactory.registerExistingUser(user, clubId, "Guest");
			else
				UserFactory.registerExistingUser(user, clubId, Integer.toString(user.getUserID()));
		}
		if (!club.isSCA()) {
			NotificationHelper.sendWelcomePlayerEmail(user, club, true);
		}

	}

	private static void createLoginForUser(PlayerDto dto, ClubDtoLite club, String addRequestFrom) throws Exception {
		UserDto user = new UserDto();
		boolean userExists = false;
		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			UserDto dbUser = UserFactory.getUserByEmail(dto.getEmail(), club.getClubId());
			if (dbUser != null) {
				userExists = true;
				user = dbUser;
			} else {
				user.setUserName(dto.getEmail());
			}
		} else {
			user.setUserName(dto.getFirstName() + "." + dto.getLastName());
		}
		if (!userExists) {
			user.setFname(dto.getFirstName());
			user.setLname(dto.getLastName());
			user.setPlayerID(dto.getPlayerID());
			user.setPassword(CommonUtility.encrypt(CommonUtility.getPassword(8)));
			user.setEmail(dto.getEmail());
//			user.setEmail(user.getUserName()+"@"+dto.getFirstName()+".com");
			user.setCountryCode(club.getCountry());
			user.setAddRequestFrom(addRequestFrom);

			if (user == null)
				UserFactory.registerUser(user, club.getClubId(), "Guest");
			else
				UserFactory.registerUser(user, club.getClubId(), Integer.toString(user.getUserID()));

			if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
				NotificationHelper.sendWelcomePlayerEmail(user, club, false);
			}

		} else {
			if (user == null)
				UserFactory.updateUserPlayerId(dto.getPlayerID(), user.getUserID(), "Guest");
			else
				UserFactory.updateUserPlayerId(dto.getPlayerID(), user.getUserID(), Integer.toString(user.getUserID()));

			if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
				NotificationHelper.sendWelcomePlayerEmail(user, club, true);
			}
		}

	}

	private static void addExistingUserToClub(PlayerDto dto, ClubDtoLite club) throws Exception {
		int clubId = club.getClubId();
		UserDto user = UserFactory.getUserByPlayerId(dto.getPlayerID());
		UserDto clubUser = UserFactory.getUserById(user.getUserID(), clubId);
		if (clubUser == null) {
			if (user == null)
				UserFactory.registerExistingUser(user, clubId, "Guest");
			else
				UserFactory.registerExistingUser(user, clubId, Integer.toString(user.getUserID()));
		}

		NotificationHelper.sendWelcomePlayerEmail(user, club, true);
	}

	public static void addCaptainAndViceCaptainToMatch(MatchDto match, int clubId) throws Exception {
		TeamDto team1 = TeamFactory.getTeamByTeamId(match.getBattingFirst(), clubId);
		match.setTeamOneCaptain(team1.getCaptain());
		match.setTeamOneViceCaptain(team1.getViceCaptain());

		TeamDto team2 = TeamFactory.getTeamByTeamId(match.getBattingSecond(), clubId);
		match.setTeamTwoCaptain(team2.getCaptain());
		match.setTeamTwoViceCaptain(team2.getViceCaptain());
	}

	// "Did Not Bat","Not out","Bowled","Caught","Run
	// out","Stumped","LBW","Retired","Hit Wicket"
	public static String getOutMethod(String outMethod) {
		if (CommonUtility.isNullOrEmpty(outMethod)) {
			return "";
		} else if (outMethod.equals("Bowled")) {
			return CommonLogicFB.OUT_METHOD_BOWLED;
		} else if (outMethod.equals("Caught")) {
			return CommonLogicFB.OUT_METHOD_CATCH;
		} else if (outMethod.equals("WktKpr Catch")) {
			return CommonLogicFB.OUT_METHOD_CATCH_WK;
		} else if (outMethod.equals("Run Out")) {
			return CommonLogicFB.OUT_METHOD_RUN_OUT;
		} else if (outMethod.equals("Stumped")) {
			return CommonLogicFB.OUT_METHOD_STUMPED;
		} else if (outMethod.equals("LBW")) {
			return CommonLogicFB.OUT_METHOD_LBW;
		} else if (outMethod.equals("Retired Hurt")) {
			return CommonLogicFB.OUT_METHOD_RETIRED;
		} else if (outMethod.equals("Retired Out")) {
			return CommonLogicFB.OUT_METHOD_RETIRED_OUT;
		} else if (outMethod.equals("Hit Wicket")) {
			return CommonLogicFB.OUT_METHOD_HIT_WICKET;
		} else if (outMethod.equals("Hit Ball Twice")) {
			return CommonLogicFB.OUT_METHOD_HIT_BALL_TWICE;
		} else if (outMethod.equals("Handled Ball")) {
			return CommonLogicFB.OUT_METHOD_HANDLED_BALL;
		} else if (outMethod.equals("Timed Out")) {
			return CommonLogicFB.OUT_METHOD_TIMED_OUT;
		} else if (outMethod.equals("Obstructing The Field")) {
			return CommonLogicFB.OUT_METHOD_OBSTRUCTING_THE_FIELD;
		} else if (outMethod.equals("Run Out (Mankad Out)")) {
			return CommonLogicFB.OUT_METHOD_MANKADING;
		}
		return "";
	}

	public static String getOutMethodString(String outMethod) {
		if (CommonUtility.isNullOrEmpty(outMethod)) {
			return "Not Out";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_BOWLED)) {
			return "Bowled";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_CATCH)) {
			return "Caught";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_CATCH_WK)) {
			return "WktKpr Catch";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_RUN_OUT)) {
			return "Run Out";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_STUMPED)) {
			return "Stumped";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_LBW)) {
			return "LBW";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_RETIRED)) {
			return "Retired Hurt";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_RETIRED_OUT)) {
			return "Retired Out";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_HIT_WICKET)) {
			return "Hit Wicket";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_HIT_BALL_TWICE)) {
			return "Hit Ball Twice";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_HANDLED_BALL)) {
			return "Handled Ball";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_TIMED_OUT)) {
			return "Timed Out";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_OBSTRUCTING_THE_FIELD)) {
			return "Obstructing The Field";
		} else if (outMethod.equals(CommonLogicFB.OUT_METHOD_MANKADING)) {
			return "Run Out (Mankad Out)";
		}
		return "";
	}

	public static String getSeriesCode(String seriesType) {
		if (CommonUtility.isNullOrEmpty(seriesType)) {
			return "OTHER";
		} else if (seriesType.equals("Twenty20")) {
			return "T20";
		} else if (seriesType.equals("Ten10")) {
			return "T10";
		} else if (seriesType.equals("One Day")) {
			return "1 DAY";
		} else if (seriesType.equals("OneDay")) {
			return "1 DAY";
		} else if (seriesType.equals("2X")) {
			return "2X";
		} else if (seriesType.equals("Youth")) {
			return "YOUTH";
		} else if (seriesType.equals("Women")) {
			return "WOMEN";
		} else if (seriesType.equals("Test")) {
			return "TEST";
		} else if (seriesType.equals("100b")) {
			return "100Ball";
		} else if (seriesType.equals("Retired Hurt")) {
			return CommonLogicFB.OUT_METHOD_RETIRED;
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static List<HomePageLeagueFB> getHomepageleague(int leagueId, ClubDto club) throws Exception {
		int clubId = club.getClubId();
		List<LeagueDto> allLeagues = club.getLeagueList();
		List<HomePageLeagueFB> leagues = new ArrayList<HomePageLeagueFB>();
		LeagueDto leagueDto = null;
		for (LeagueDto series : allLeagues) {
			if (series.isHasDivisions()) {
				for (LeagueDto div : series.getDivisions()) {
					if (leagueId == div.getLeagueId()) {
						leagueDto = div;
					}
				}
			} else if (leagueId == series.getLeagueId()) {
				leagueDto = series;
			}
		}

		List<LeagueDto> selectedleagues = new ArrayList<LeagueDto>();
		if (leagueDto != null && !leagueDto.isHasDivisions()) {
			selectedleagues.add(leagueDto);
		} else if (leagueDto != null && leagueDto.getDivisions() != null && !leagueDto.getDivisions().isEmpty()) {
			selectedleagues.addAll(leagueDto.getDivisions());
		}

		for (LeagueDto selectedLeague : selectedleagues) {
			HomePageLeagueFB league = new HomePageLeagueFB();

			league.setLeagueName(selectedLeague.getName());
			league.setLeagueId(selectedLeague.getLeagueId());

			// Player Series Records.
			List<PlayerStatisticsFBDto> topGoals = PlayerStatisticsFactory.getPlayersStatisticsFB(clubId,0,0,0, 
					""+selectedLeague.getLeagueId(),null,0,null,null,null,0,0);
			league.setTopGoalsScoredRecords(CommonUtility.getLimitedRecordsFromList(topGoals, 5));
			
			List<PlayerStatisticsFBDto> topAssists = new ArrayList<PlayerStatisticsFBDto>();
			
			if(topGoals != null && !topGoals.isEmpty()){
				topAssists.addAll(topGoals);
				Collections.sort(topAssists, new Comparator<PlayerStatisticsFBDto>() {
				    public int compare(PlayerStatisticsFBDto obj1, PlayerStatisticsFBDto obj2) {
				    	if(obj1.getAssists() < obj2.getAssists()) {
				    		return -1;
				    	}else if(obj1.getAssists() > obj2.getAssists()) {
				    		return 1;
				    	}
				    	return 0;
				    }
				});
			}
			league.setTopAssistsRescords(CommonUtility.getLimitedRecordsFromList(topAssists, 5));

			List<PlayerDto> players = CommonLogicFB.getConsolidatedPoints(topGoals, clubId);
			league.setPlayerPoints(CommonUtility.getLimitedRecordsFromList(players, 5));

			// player weekly Records
			
			List<PlayerStatisticsFBDto> weeklyGoalsScored = PlayerStatisticsFactory.getAllPlayersLastWeekStatisticsFB(null,	
					"" + selectedLeague.getLeagueId(), clubId);
			league.setWeeklyTopGoalsScoredRecords(CommonUtility.getLimitedRecordsFromList(weeklyGoalsScored, 3));
			
			List<PlayerStatisticsFBDto> weeklyAssists = new ArrayList<PlayerStatisticsFBDto>();
			
			if(weeklyGoalsScored != null && !weeklyGoalsScored.isEmpty()){
				weeklyAssists.addAll(weeklyGoalsScored);
				Collections.sort(weeklyAssists, new Comparator<PlayerStatisticsFBDto>() {
				    public int compare(PlayerStatisticsFBDto obj1, PlayerStatisticsFBDto obj2) {
				    	if(obj1.getAssists() < obj2.getAssists()) {
				    		return -1;
				    	}else if(obj1.getAssists() > obj2.getAssists()) {
				    		return 1;
				    	}
				    	return 0;
				    }
				});
			}
			league.setWeeklyTopAssistsRecords(CommonUtility.getLimitedRecordsFromList(weeklyAssists, 3));
			
			List<PlayerDto> weeklyPlayers = CommonLogicFB.getConsolidatedPoints(weeklyGoalsScored, clubId);
			
			league.setWeeklyPlayerPoints(CommonUtility.getLimitedRecordsFromList(weeklyPlayers, 3));

			league.setSeriesInNumbers(prepareNumbersForLeague(weeklyGoalsScored, players, 0, 0));

			List<TeamDto> teams = TeamFactory.getPointsTable(selectedLeague.getLeagueId() + "", clubId);
			calculatePointstable(clubId, selectedLeague, teams);
			league.setTeams(teams);
			league.setGroupNames(TeamFactory.getGroupNames(selectedLeague.getLeagueId(), clubId));
			league.setGroups(selectedLeague.getGroups());

			leagues.add(league);
		}
		return leagues;
	}

	@SuppressWarnings("unchecked")
	public static List<HomePageLeagueFB> getHomePageLeagueWithoutDivisions(int leagueId, ClubDto club) throws Exception {
		
		int clubId = club.getClubId();
		List<LeagueDto> allLeagues = LeagueFactory.getLeaguesWithDivisions(clubId, leagueId);
		List<HomePageLeagueFB> leagues = new ArrayList<HomePageLeagueFB>();
		LeagueDto leagueDto = null;
		for (LeagueDto series : allLeagues) {
			if (leagueId == series.getLeagueId()) {
				leagueDto = series;
			}
		}

		List<LeagueDto> selectedleagues = new ArrayList<LeagueDto>();
		selectedleagues.add(leagueDto);

		for (LeagueDto selectedLeague : selectedleagues) {
			
			HomePageLeagueFB league = new HomePageLeagueFB();

			league.setLeagueName(selectedLeague.getName());
			league.setLeagueId(selectedLeague.getLeagueId());
			
			// Player Series Records.
			List<PlayerStatisticsFBDto> topGoals = PlayerStatisticsFactory.getPlayersStatisticsFB(clubId,0,0,0, 
					""+selectedLeague.getLeagueId(),null,0,null,null,null,0,0);
			league.setTopGoalsScoredRecords(CommonUtility.getLimitedRecordsFromList(topGoals, 5));
			
			List<PlayerStatisticsFBDto> topAssists = new ArrayList<PlayerStatisticsFBDto>();
			
			if(topGoals != null && !topGoals.isEmpty()){
				topAssists.addAll(topGoals);
				Collections.sort(topAssists, new Comparator<PlayerStatisticsFBDto>() {
				    public int compare(PlayerStatisticsFBDto obj1, PlayerStatisticsFBDto obj2) {
				    	if(obj1.getAssists() > obj2.getAssists()) {
				    		return -1;
				    	}else if(obj1.getAssists() < obj2.getAssists()) {
				    		return 1;
				    	}
				    	return 0;
				    }
				});
			}
			league.setTopAssistsRescords(CommonUtility.getLimitedRecordsFromList(topAssists, 5));

			List<PlayerDto> players = CommonLogicFB.getConsolidatedPoints(topGoals, clubId);
			league.setPlayerPoints(CommonUtility.getLimitedRecordsFromList(players, 5));

			// player weekly Records
			List<PlayerStatisticsFBDto> weeklyGoalsScored = PlayerStatisticsFactory.getAllPlayersLastWeekStatisticsFB(null,	
					"" + selectedLeague.getLeagueId(), clubId);
			
			league.setWeeklyTopGoalsScoredRecords(CommonUtility.getLimitedRecordsFromList(weeklyGoalsScored, 3));
			
			List<PlayerStatisticsFBDto> weeklyAssists = new ArrayList<PlayerStatisticsFBDto>();
			
			if(weeklyGoalsScored != null && !weeklyGoalsScored.isEmpty()){
				weeklyAssists.addAll(weeklyGoalsScored);
				Collections.sort(weeklyAssists, new Comparator<PlayerStatisticsFBDto>() {
				    public int compare(PlayerStatisticsFBDto obj1, PlayerStatisticsFBDto obj2) {
				    	if(obj1.getAssists() > obj2.getAssists()) {
				    		return -1;
				    	}else if(obj1.getAssists() < obj2.getAssists()) {
				    		return 1;
				    	}
				    	return 0;
				    }
				});
			}
			league.setWeeklyTopAssistsRecords(CommonUtility.getLimitedRecordsFromList(weeklyAssists, 3));
			
			List<PlayerDto> weeklyPlayers = CommonLogicFB.getConsolidatedPoints(weeklyGoalsScored, clubId);
			league.setWeeklyPlayerPoints(CommonUtility.getLimitedRecordsFromList(weeklyPlayers, 3));
			
			int matchesCount = LeagueFactory.getMatchesCountForSeries(leagueId, clubId);
			int teamsCount = LeagueFactory.getTeamsCountForSeries(leagueId, clubId);

			league.setSeriesInNumbers(prepareNumbersForLeague(topGoals, players, matchesCount, teamsCount));

			List<TeamDto> teams = TeamFactory.getPointsTableFB(selectedLeague.getLeagueId() + "", clubId);
			calculatePointstable(clubId, selectedLeague, teams);
			league.setTeams(teams);
			league.setGroupNames(TeamFactory.getGroupNames(selectedLeague.getLeagueId(), clubId));
			league.setGroups(selectedLeague.getGroups());

			leagues.add(league);
		}
		return leagues;
	}

	private static SeriesNumbersBeanFB prepareNumbersForLeague(List<PlayerStatisticsFBDto> playerStats, List<PlayerDto> players, 
			int matchesCount, int teamsCount) {

		SeriesNumbersBeanFB numbers = new SeriesNumbersBeanFB();
		if (players != null && !players.isEmpty()) {			
			numbers.setPlayers(players.size());
			numbers.setMatches(matchesCount);
			numbers.setTeams(teamsCount);
			
			for (PlayerStatisticsFBDto stat : playerStats) {
				numbers.setGoals(numbers.getGoals()+stat.getGoalsScored());
				numbers.setAssists(numbers.getAssists()+stat.getAssists());
				numbers.setYellowCards(numbers.getYellowCards()+stat.getYellowCards());
				numbers.setRedCards(numbers.getRedCards()+stat.getRedCards());
			}
		}
		return numbers;
	}

	public static ScoreCardBean prepareFullScorecard(long matchId, int clubId) throws Exception {
		ScoreCardBean matchBean = new ScoreCardBean();
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);

		matchBean.setMatchInfo(match);
		List<BattingDto> team1Batting;
		List<BattingDto> team2Batting;
		List<BowlingDto> team1Bowling;
		List<BowlingDto> team2Bowling;

		List<BattingDto> team1_2Batting = null;
		List<BattingDto> team2_2Batting = null;
		List<BowlingDto> team1_2Bowling = null;
		List<BowlingDto> team2_2Bowling = null;

		Map<String, List<PartnershipDto>> partnershipMap = new HashMap<String, List<PartnershipDto>>();
		try {

			if (match != null && match.isLiveMatch() && match.getIsComplete() != 1) {
				List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
				Map<String, Object> records = CommonLogicFB.consolidateMatchRecords(balls, match);
				match.setResult(match.getResult());
				team1Batting = (List<BattingDto>) records.get("t1Batting");
				team2Batting = (List<BattingDto>) records.get("t2Batting");
				team1Bowling = (List<BowlingDto>) records.get("t1Bowling");
				team2Bowling = (List<BowlingDto>) records.get("t2Bowling");

				team1_2Batting = records.get("t1Batting_2") != null ? (List<BattingDto>) records.get("t1Batting_2")
						: null;
				team2_2Batting = records.get("t2Batting_2") != null ? (List<BattingDto>) records.get("t2Batting_2")
						: null;
				team1_2Bowling = records.get("t1Bowling_2") != null ? (List<BowlingDto>) records.get("t1Bowling_2")
						: null;
				team2_2Bowling = records.get("t2Bowling_2") != null ? (List<BowlingDto>) records.get("t2Bowling_2")
						: null;

				partnershipMap = (Map<String, List<PartnershipDto>>) records.get("partnershipMap");
				List<PlayerDto> players = PlayerFactory.getPlayersOfMatch(match.getMatchID(), clubId);

				CommonLogicFB.addDidNotBatPlayers(team1Batting,
						(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
						players);
				CommonLogicFB.addDidNotBatPlayers(team2Batting,
						(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
						players);
				if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() <= 0) {
					CommonLogicFB.addDidNotBatPlayers(team1_2Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
							players);
					CommonLogicFB.addDidNotBatPlayers(team2_2Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
							players);
				} else if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() > 0) {
					CommonLogicFB.addDidNotBatPlayers(team2_2Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
							players);
					CommonLogicFB.addDidNotBatPlayers(team1_2Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
							players);
				}

				CommonLogicFB.setPlayerDetailsForBatsman(team1Batting, players);
				CommonLogicFB.setPlayerDetailsForBatsman(team2Batting, players);
				CommonLogicFB.setPlayerDetailsForBowler(team1Bowling, players);
				CommonLogicFB.setPlayerDetailsForBowler(team2Bowling, players);

				CommonLogicFB.setPlayerDetailsForBatsman(team1_2Batting, players);
				CommonLogicFB.setPlayerDetailsForBatsman(team2_2Batting, players);
				CommonLogicFB.setPlayerDetailsForBowler(team1_2Bowling, players);
				CommonLogicFB.setPlayerDetailsForBowler(team2_2Bowling, players);

			} else {
				if (match.isLiveMatch()) {
					List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
					Map<String, Object> records = CommonLogicFB.consolidateMatchRecords(balls, match);
					partnershipMap = (Map<String, List<PartnershipDto>>) records.get("partnershipMap");
					match = MatchesFactory.getMatch(matchId, clubId);
					matchBean.setMatchInfo(match);

				}
				team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
						match.getBattingFirst(), clubId);

				team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
						clubId);

				team2Bowling = removeEmptyBowlers(PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(
						match.getMatchID(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
						clubId));

				team1Bowling = removeEmptyBowlers(PlayerStatisticsFactory
						.getPlayersBowlingByMatchIdTeamId(match.getMatchID(), match.getBattingFirst(), clubId));

				team1_2Batting = team1Batting;

				team2_2Batting = team2Batting;

				team2_2Bowling = team2Bowling;

				team1_2Bowling = team1Bowling;

			}
			if (clubId == 6745) {
				Map<String, String> playersMap = createPlayerLastNameMap(team1Batting, team2Batting);
				for (BattingDto batting : team1Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				for (BattingDto batting : team2Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
			} else {
				Map<String, String> playersMap = createPlayerMap(team1Batting, team2Batting, team1_2Batting,
						team2_2Batting, clubId);
				for (BattingDto batting : team1Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				for (BattingDto batting : team2Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				if (team1_2Batting != null) {
					for (BattingDto batting : team1_2Batting) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
				if (team2_2Batting != null) {
					for (BattingDto batting : team2_2Batting) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
			}

			matchBean.setTeam1Batting(team1Batting);
			matchBean.setTeam2Batting(team2Batting);
			matchBean.setTeam1Bowling(team1Bowling);
			matchBean.setTeam2Bowling(team2Bowling);

			matchBean.setTeam1_2Batting(team1_2Batting);
			matchBean.setTeam2_2Batting(team2_2Batting);
			matchBean.setTeam1_2Bowling(team1_2Bowling);
			matchBean.setTeam2_2Bowling(team2_2Bowling);
			matchBean.setPartnershipMap(partnershipMap);

			int t1wides = 0;
			int t2wides = 0;
			int t1NoBalls = 0;
			int t2NoBalls = 0;

			int t1_1wides = 0;
			int t2_1wides = 0;
			int t1_1NoBalls = 0;
			int t2_1NoBalls = 0;

			int t1_2wides = 0;
			int t2_2wides = 0;
			int t1_2NoBalls = 0;
			int t2_2NoBalls = 0;

			if (team1Bowling != null) {
				for (BowlingDto dto : team1Bowling) {
					if ((dto.getBalls() != 0 || dto.getRuns() > 0) && dto.getInnings() == 1) {
						t1wides += dto.getWides();
						t1NoBalls += dto.getNoBalls();
						t1_1wides += dto.getWides();
						t1_1NoBalls += dto.getNoBalls();
					}
				}

			}

			if (team2Bowling != null) {
				for (BowlingDto dto : team2Bowling) {
					if ((dto.getBalls() != 0 || dto.getRuns() > 0) && dto.getInnings() == 1) {
						t2wides += dto.getWides();
						t2NoBalls += dto.getNoBalls();
						t2_1wides += dto.getWides();
						t2_1NoBalls += dto.getNoBalls();
					}
				}

			}

			/* if (match.isLiveMatch() && match.getIsComplete() != 1) { */
			if (team1_2Bowling != null) {
				for (BowlingDto dto : team1_2Bowling) {
					if ((dto.getBalls() != 0 || dto.getRuns() > 0) && dto.getInnings() == 2) {
						t1wides += dto.getWides();
						t1NoBalls += dto.getNoBalls();
						t1_2wides += dto.getWides();
						t1_2NoBalls += dto.getNoBalls();
					}
				}

			}

			if (team2_2Bowling != null) {
				for (BowlingDto dto : team2_2Bowling) {
					if ((dto.getBalls() != 0 || dto.getRuns() > 0) && dto.getInnings() == 2) {
						t2wides += dto.getWides();
						t2NoBalls += dto.getNoBalls();
						t2_2wides += dto.getWides();
						t2_2NoBalls += dto.getNoBalls();
					}
				}

			}

			match.setT1Wides(t1wides);
			match.setT2Wides(t2wides);
			match.setT1noballs(t1NoBalls);
			match.setT2noballs(t2NoBalls);

			match.setT1_1Wides(t1_1wides);
			match.setT2_1Wides(t2_1wides);
			match.setT1_1noballs(t1_1NoBalls);
			match.setT2_1noballs(t2_1NoBalls);

			match.setT1_2Wides(t1_2wides);
			match.setT2_2Wides(t2_2wides);
			match.setT1_2noballs(t1_2NoBalls);
			match.setT2_2noballs(t2_2NoBalls);
			/* } */

			// Adding Location to Match Info for API
			if (matchId > 0) {
				String location = "";
				FixtureDto fixture = FixturesFactory.getFixtureForMatch((int) matchId, clubId);
				if (fixture != null) {
					// Adding umpires to Match Info for API
					String umpire1 = "";
					String umpire2 = "";
					if (!CommonUtility.isNullOrEmpty(fixture.getUmpire1Name())) {
						umpire1 = fixture.getUmpire1Name();
					}
					if (!CommonUtility.isNullOrEmpty(fixture.getUmpire2Name())) {
						umpire2 = fixture.getUmpire2Name();
					}

					match.setUmpire1(umpire1);
					match.setUmpire2(umpire2);
					if (fixture.getGroundId() > 0) {
						GroundDto ground = GroundFactory.getGround(fixture.getGroundId(), clubId);
						if (ground != null && !CommonUtility.isNullOrEmpty(ground.getName())) {
							location = ground.getName();
							/* match.setLocation(ground.getName()); */
						}
					}
				}
				match.setLocation(location);
			}
			// Adding Time to Match Info for API
			if (match != null) {
				if (match.isLiveMatch()) {
					match.setStartTime(CommonLogicFB.getFirstInngingsStartTime(match, clubId));
					match.setEndTime(CommonLogicFB.getSecondInngingsEndTime(match, clubId));
				}
				// Adding Series to Match Info for API
				String seriesName = "";
				if (match.getLeagueId() > 0) {
					ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
					seriesName = club.getLeagueName(match.getLeagueId());
				}
				match.setSeriesName(seriesName);

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " Error in prepareFullScorecard " + " MatchID - " + matchId + " and ClubId "+ clubId);
		}
		return matchBean;
	}

	public static ScoreCardGraphsBean prepareFullScorecardGraphs(long matchId, int clubId) throws Exception {
		ScoreCardGraphsBean matchBean = new ScoreCardGraphsBean();
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);
		matchBean.setMatchInfo(match);
		List<BattingDto> team1Batting;
		List<BattingDto> team2Batting;
		List<BowlingDto> team1Bowling;
		List<BowlingDto> team2Bowling;

		List<BattingDto> team1Batting_2 = null;
		List<BattingDto> team2Batting_2 = null;
		List<BowlingDto> team1Bowling_2 = null;
		List<BowlingDto> team2Bowling_2 = null;

		Map<String, List<PartnershipDto>> partnershipMap = new HashMap<String, List<PartnershipDto>>();
		Map<String, OverDto> overMap = new LinkedHashMap<String, OverDto>();
		Map<String, TypeOfRuns> typeOfRuns = new HashMap<String, TypeOfRuns>();

		try {

			if (match.isLiveMatch() && match.getIsComplete() != 1) {
				List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
				Map<String, Object> records = CommonLogicFB.consolidateMatchRecords(balls, match);
				match.setResult(match.getResult());
				team1Batting = (List<BattingDto>) records.get("t1Batting");
				team2Batting = (List<BattingDto>) records.get("t2Batting");
				team1Bowling = (List<BowlingDto>) records.get("t1Bowling");
				team2Bowling = (List<BowlingDto>) records.get("t2Bowling");

				team1Batting_2 = (List<BattingDto>) records.get("t1Batting_2");
				team2Batting_2 = (List<BattingDto>) records.get("t2Batting_2");
				team1Bowling_2 = (List<BowlingDto>) records.get("t1Bowling_2");
				team2Bowling_2 = (List<BowlingDto>) records.get("t2Bowling_2");

				partnershipMap = (Map<String, List<PartnershipDto>>) records.get("partnershipMap");
				overMap = (Map<String, OverDto>) records.get("overMap");
				typeOfRuns = (Map<String, TypeOfRuns>) records.get("typeOfRuns");
				List<PlayerDto> players = PlayerFactory.getPlayersOfMatch(match.getMatchID(), clubId);

				CommonLogicFB.addDidNotBatPlayers(team1Batting,
						(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
						players);
				CommonLogicFB.addDidNotBatPlayers(team2Batting,
						(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
						players);

				if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() <= 0) {
					CommonLogicFB.addDidNotBatPlayers(team1Batting_2,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
							players);
					CommonLogicFB.addDidNotBatPlayers(team2Batting_2,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
							players);
				} else if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() > 0) {
					CommonLogicFB.addDidNotBatPlayers(team2Batting_2,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
							players);
					CommonLogicFB.addDidNotBatPlayers(team1Batting_2,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
							players);
				}

				CommonLogicFB.setPlayerDetailsForBatsman(team1Batting, players);
				CommonLogicFB.setPlayerDetailsForBatsman(team2Batting, players);
				CommonLogicFB.setPlayerDetailsForBowler(team1Bowling, players);
				CommonLogicFB.setPlayerDetailsForBowler(team2Bowling, players);

			} else {
				if (match.isLiveMatch()) {
					List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
					Map<String, Object> records = CommonLogicFB.consolidateMatchRecords(balls, match);
					partnershipMap = (Map<String, List<PartnershipDto>>) records.get("partnershipMap");
					overMap = (Map<String, OverDto>) records.get("overMap");
					typeOfRuns = (Map<String, TypeOfRuns>) records.get("typeOfRuns");
				}
				team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
						match.getBattingFirst(), clubId);

				team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
						clubId);

				team2Bowling = removeEmptyBowlers(PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(
						match.getMatchID(),
						(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(),
						clubId));

				team1Bowling = removeEmptyBowlers(PlayerStatisticsFactory
						.getPlayersBowlingByMatchIdTeamId(match.getMatchID(), match.getBattingFirst(), clubId));
			}
			if (clubId == 6745) {
				Map<String, String> playersMap = createPlayerLastNameMap(team1Batting, team2Batting);
				for (BattingDto batting : team1Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				for (BattingDto batting : team2Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				if (team1Batting_2 != null) {
					for (BattingDto batting : team1Batting_2) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
				if (team2Batting_2 != null) {
					for (BattingDto batting : team2Batting_2) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
			} else {
				Map<String, String> playersMap = createPlayerMap(team1Batting, team2Batting, team1Batting_2,
						team2Batting_2, clubId);
				for (BattingDto batting : team1Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				for (BattingDto batting : team2Batting) {
					batting.setOutStringNoLink(playersMap, clubId);
				}
				if (team1Batting_2 != null) {
					for (BattingDto batting : team1Batting_2) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
				if (team2Batting_2 != null) {
					for (BattingDto batting : team2Batting_2) {
						batting.setOutStringNoLink(playersMap, clubId);
					}
				}
			}

			matchBean.setTeam1Batting(team1Batting);
			matchBean.setTeam2Batting(team2Batting);
			matchBean.setTeam1Bowling(team1Bowling);
			matchBean.setTeam2Bowling(team2Bowling);

			matchBean.setTeam1_2Batting(team1Batting_2);
			matchBean.setTeam2Batting(team2Batting_2);
			matchBean.setTeam1Bowling(team1Bowling_2);
			matchBean.setTeam2Bowling(team2Bowling_2);

			matchBean.setPartnershipMap(partnershipMap);
			matchBean.setOverMap(overMap);
			matchBean.setTypeOfRuns(typeOfRuns);

			int t1wides = 0;
			int t2wides = 0;
			int t1NoBalls = 0;
			int t2NoBalls = 0;
			if (team1Bowling != null) {
				for (BowlingDto dto : team1Bowling) {
					if (dto.getBalls() != 0) {
						t1wides += dto.getWides();
						t1NoBalls += dto.getNoBalls();
					}
				}

			}

			if (team2Bowling != null) {
				for (BowlingDto dto : team2Bowling) {
					if (dto.getBalls() != 0) {
						t2wides += dto.getWides();
						t2NoBalls += dto.getNoBalls();
					}
				}

			}
			match.setT1Wides(t1wides);
			match.setT2Wides(t2wides);
			match.setT1noballs(t1NoBalls);
			match.setT2noballs(t2NoBalls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " - prepareFullScorecardGraphs " + " MatchID - " + matchId + " and ClubId "
					+ clubId);
		}
		return matchBean;
	}

	private static List<BowlingDto> removeEmptyBowlers(List<BowlingDto> playersBowling) {
		List<BowlingDto> bowlers = null;
		if (playersBowling != null) {
			bowlers = new ArrayList<BowlingDto>();
			for (BowlingDto bowling : playersBowling) {
				if (bowling.getBalls() > 0 || bowling.getRuns() > 0) {
					bowlers.add(bowling);
				}
			}
		}
		return bowlers;
	}

	public static Map<String, String> createPlayerMap(List<BattingDto> team1Batting, List<BattingDto> team2Batting,
			List<BattingDto> team1_2Batting, List<BattingDto> team2_2Batting, int clubId) throws Exception {
		Map<String, String> players = new HashMap<String, String>();
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		int firstNameFirst = 0;
		if (club != null) {
			firstNameFirst = club.getFirstNameFirst();
		}
		if (team1Batting != null) {
			Iterator<BattingDto> itr = team1Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
			}
		}
		if (team2Batting != null) {
			Iterator<BattingDto> itr = team2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
			}
		}
		if (team1_2Batting != null) {
			Iterator<BattingDto> itr = team1_2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
			}
		}
		if (team2_2Batting != null) {
			Iterator<BattingDto> itr = team2_2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
			}
		}
		players.put("-1", "Substitute");

		return players;
	}

	private static Map<String, String> createPlayerLastNameMap(List<BattingDto> team1Batting,
			List<BattingDto> team2Batting) {
		Map<String, String> players = new HashMap<String, String>();
		if (team1Batting != null) {
			Iterator<BattingDto> itr = team1Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getLastName());
			}
		}
		if (team2Batting != null) {
			Iterator<BattingDto> itr = team2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getLastName());
			}
		}

		players.put("-1", "Substitute");

		return players;
	}

	private static Map<String, String> createPlayerFullNameMap(List<BattingDto> team1Batting,
			List<BattingDto> team2Batting, List<BattingDto> team1Batting_2, List<BattingDto> team2Batting_2) {
		Map<String, String> players = new HashMap<String, String>();
		if (team1Batting != null) {
			Iterator<BattingDto> itr = team1Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerName());
			}
		}
		if (team2Batting != null) {
			Iterator<BattingDto> itr = team2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerName());
			}
		}

		if (team1Batting_2 != null) {
			Iterator<BattingDto> itr = team1Batting_2.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerName());
			}
		}
		if (team2Batting_2 != null) {
			Iterator<BattingDto> itr = team2Batting_2.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				players.put(dto.getPlayerID() + "", dto.getPlayerName());
			}
		}
		return players;
	}

	public static BallByBallBean prepareBallByBall(long matchId, int clubId) {

		BallByBallBean matchBean = new BallByBallBean();
		try {
			MatchDto match = MatchesFactory.getMatch(matchId, clubId);
			if (match != null) {
				FixtureDto fixture = FixturesFactory.getFixtureForMatch(match.getMatchID(), match.getClubID());
				if (fixture != null) {
					match.setUmpire1(fixture.getUmpire1Id() + "");
					match.setUmpire2(fixture.getUmpire2Id() + "");
				}
			}
			matchBean.setMatchInfo(match);
			String runsEquation = match.getRunsNeededEquation();
			match.setRunsEquation(runsEquation);

			if (match != null) {
				List<BattingDto> team1Batting = null;
				List<BattingDto> team2Batting = null;
				List<BowlingDto> team1Bowling = null;
				List<BowlingDto> team2Bowling = null;

				List<BattingDto> team1Batting_2 = null;
				List<BattingDto> team2Batting_2 = null;
				List<BowlingDto> team1Bowling_2 = null;
				List<BowlingDto> team2Bowling_2 = null;
				
				List<BattingDto> team1BattingSuper = null;
				List<BattingDto> team2BattingSuper = null;
				List<BowlingDto> team1BowlingSuper = null;
				List<BowlingDto> team2BowlingSuper = null;

				if (match.isLiveMatch()) {
					List<BallDto> balls1 = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);

					Map<String, Object> records = CommonLogicFB.consolidateMatchRecords(balls1, match);

					team1Batting = (List<BattingDto>) records.get("t1Batting");
					team2Batting = (List<BattingDto>) records.get("t2Batting");
					team1Bowling = (List<BowlingDto>) records.get("t1Bowling");
					team2Bowling = (List<BowlingDto>) records.get("t2Bowling");

					team1Batting_2 = (List<BattingDto>) records.get("t1Batting_2");
					team2Batting_2 = (List<BattingDto>) records.get("t2Batting_2");
					team1Bowling_2 = (List<BowlingDto>) records.get("t1Bowling_2");
					team2Bowling_2 = (List<BowlingDto>) records.get("t2Bowling_2");

					matchBean.setOverMap((Map<String, OverDto>) records.get("overMap"));
					matchBean.setSuperOverMap((Map<String, OverDto>) records.get("superOverMap"));
					
					team1BattingSuper = (List<BattingDto>) records.get("t1BattingSuper");
					team2BattingSuper = (List<BattingDto>) records.get("t2BattingSuper");
					team1BowlingSuper = (List<BowlingDto>) records.get("t1BowlingSuper");
					team2BowlingSuper = (List<BowlingDto>) records.get("t2BowlingSuper");
					
					matchBean.setSuperOverInningsNum1((int) records.get("superOverInningsNum1"));
					matchBean.setSuperOverInningsNum2((int) records.get("superOverInningsNum2"));
					
					List<PlayerDto> players = PlayerFactory.getPlayersOfMatch(match.getMatchID(), clubId);

					CommonLogicFB.addDidNotBatPlayers(team1Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1() : match.getPlayers2(),
							players);
					CommonLogicFB.addDidNotBatPlayers(team2Batting,
							(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2() : match.getPlayers1(),
							players);

					if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() <= 0) {
						CommonLogicFB.addDidNotBatPlayers(team1Batting_2,
								(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1()
										: match.getPlayers2(),
								players);
						CommonLogicFB.addDidNotBatPlayers(team2Batting_2,
								(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2()
										: match.getPlayers1(),
								players);
					} else if ("Test".equalsIgnoreCase(match.getSeriesType()) && match.getIsAbandoned() > 0) {
						CommonLogicFB.addDidNotBatPlayers(team2Batting_2,
								(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers1()
										: match.getPlayers2(),
								players);
						CommonLogicFB.addDidNotBatPlayers(team1Batting_2,
								(match.getBattingFirst() == match.getTeamOne()) ? match.getPlayers2()
										: match.getPlayers1(),
								players);
					}

					CommonLogicFB.setPlayerDetailsForBatsman(team1Batting, players);
					CommonLogicFB.setPlayerDetailsForBatsman(team2Batting, players);
					CommonLogicFB.setPlayerDetailsForBowler(team1Bowling, players);
					CommonLogicFB.setPlayerDetailsForBowler(team2Bowling, players);

					CommonLogicFB.setPlayerDetailsForBatsman(team1Batting_2, players);
					CommonLogicFB.setPlayerDetailsForBatsman(team2Batting_2, players);
					CommonLogicFB.setPlayerDetailsForBowler(team1Bowling_2, players);
					CommonLogicFB.setPlayerDetailsForBowler(team2Bowling_2, players);
					
					CommonLogicFB.setPlayerDetailsForBatsman(team1BattingSuper, players);
					CommonLogicFB.setPlayerDetailsForBatsman(team2BattingSuper, players);
					CommonLogicFB.setPlayerDetailsForBowler(team1BowlingSuper, players);
					CommonLogicFB.setPlayerDetailsForBowler(team2BowlingSuper, players);

					Map<String, String> playersMap = createPlayerMap(team1Batting, team2Batting, team1Batting_2,
							team2Batting_2, clubId);
					matchBean.setPlayersMap(playersMap);
					// Map<String, String> playersMap1 = createPlayerMap(team1Batting, team2Batting,
					// team1Batting_2, team2Batting_2);
					Map<String, BattingDto> battingMap = new HashMap<String, BattingDto>();
					Map<String, BattingDto> battingMapRegualr = createBattingRecordsMap(team1Batting, team2Batting,
							team1Batting_2, team2Batting_2, match);
					Map<String, BowlingDto> bowlingMap = new HashMap<String, BowlingDto>();
					
					Map<String, BowlingDto> bowlingMapRegular = createBowlingRecordsMap(team1Bowling, team2Bowling,
							team1Bowling_2, team2Bowling_2, match);
					
					Map<String, BattingDto> battingMapSuper = createBattingRecordsMapSuper(team1BattingSuper, team2BattingSuper, match);
					Map<String, BowlingDto> bowlingMapSuper = createBowlingRecordsMapSuper(team1BowlingSuper, team2BowlingSuper, match);

					List<BallDto> balls = new ArrayList<BallDto>();
					List<BallDto> regularBalls = new ArrayList<BallDto>();
					List<BallDto> superBalls = new ArrayList<BallDto>();
					for (BallDto ball : (balls1)) {
						if (!ball.getBallType().equals(CommonLogicFB.BALL_TYPE_BOWLER_COUNT_BALL)) {
							if(ball.getIsSuperOver()>0) {
								superBalls.add(ball);
							}else {
								regularBalls.add(ball);
							}
						}
					}
					if(!CommonUtility.isListNullEmpty(superBalls)) {
						balls.addAll(superBalls);
						battingMap = battingMapSuper;
						bowlingMap = bowlingMapSuper;
					}else {
						balls.addAll(regularBalls);
						battingMap = battingMapRegualr;
						bowlingMap = bowlingMapRegular;
					}

					if (match.isInProgress() && balls != null && balls.size() > 0) {
						BallDto LastBall = balls.get(balls.size() - 1);

						BallDto previousLastBall = balls.size() > 2 ? balls.get(balls.size() - 2) : null;
						BallDto prePreviousLastBall = balls.size() > 3 ? balls.get(balls.size() - 3) : null;
						if(previousLastBall != null && 
								LastBall.getInningsNumber() != previousLastBall.getInningsNumber()) {
							previousLastBall = null;
						}
						if(prePreviousLastBall != null && 
								LastBall.getInningsNumber() != prePreviousLastBall.getInningsNumber()) {
							prePreviousLastBall = null;
						}
						BallDto tempBall = balls.get(balls.size() - 1);
						// Valid last ball
						int ballSize = balls.size() - 1;
						for (int i = ballSize; i >= 0; i--) {
							LastBall = balls.get(i);
							if (!CommonLogicFB.BALL_TYPE_NO_COUNT_BALL.equalsIgnoreCase(LastBall.getBallType())
							// &&
							// !CommonLogic.BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(LastBall.getBallType())
							) {
								break;
							}
						}
						BattingDto lastOutPlayer = null;

						for (int i = ballSize; i >= 0; i--) {
							tempBall = balls.get(i);
							if (tempBall.getOutPerson() > 0) {
								lastOutPlayer = battingMap.get("" + tempBall.getOutPerson());
								break;
							}
						}
						BowlingDto currentBowler = null;
						for (int i = ballSize; i >= 0; i--) {
							tempBall = balls.get(i);
							if (tempBall.getBowler() > 0) {
								currentBowler = (BowlingDto) bowlingMap.get("" + tempBall.getBowler());
								break;
							}
						}
						matchBean.setLastOutPlayer(lastOutPlayer);
						matchBean.setCurrentBowler(currentBowler);

						BattingDto latestBatting = null;
						BattingDto runner = null;

						if (!CommonLogicFB.BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(LastBall.getBallType())
								|| !(CommonLogicFB.BALL_TYPE_AUTO_COMMENT_BALL.equalsIgnoreCase(LastBall.getBallType())
										&& LastBall.getBatsman() <= 0 && LastBall.getBowler() <= 0
										&& LastBall.getRunner() <= 0)) {

							int varBatsman = LastBall.getBatsman() > 0 ? LastBall.getBatsman()
									: (previousLastBall != null && previousLastBall.getBatsman() > 0)
											? previousLastBall.getBatsman()
											: (prePreviousLastBall != null && prePreviousLastBall.getBatsman() > 0)
													? prePreviousLastBall.getBatsman()
													: 0;
							int varRunner = LastBall.getRunner() > 0 ? LastBall.getRunner()
									: (previousLastBall != null && previousLastBall.getRunner() > 0)
											? previousLastBall.getRunner()
											: (prePreviousLastBall != null && prePreviousLastBall.getRunner() > 0)
													? prePreviousLastBall.getRunner()
													: 0;
							int varBowler = LastBall.getBowler() > 0 ? LastBall.getBowler()
									: (previousLastBall != null && previousLastBall.getBowler() > 0)
											? previousLastBall.getBowler()
											: (prePreviousLastBall != null && prePreviousLastBall.getBowler() > 0)
													? prePreviousLastBall.getBowler()
													: 0;
							
							if(match.getCurrentInnings()>2){
								latestBatting = (BattingDto) battingMap.get("2-" + varBatsman);
								runner = (BattingDto) battingMap.get("2-" + varRunner);
							}else {
								latestBatting = (BattingDto) battingMap.get("" + varBatsman);
								runner = (BattingDto) battingMap.get("" + varRunner);
							}

							if ("No Ball".equalsIgnoreCase(LastBall.getBallType())
									|| "Wide".equalsIgnoreCase(LastBall.getBallType())
									|| "Good Wide".equalsIgnoreCase(LastBall.getBallType())
									|| "No Ball of Bat".equalsIgnoreCase(LastBall.getBallType())
									|| "Good No Ball".equalsIgnoreCase(LastBall.getBallType())
									|| "No Ball Bye".equalsIgnoreCase(LastBall.getBallType())
									|| "Good No Ball Bye".equalsIgnoreCase(LastBall.getBallType())
									|| "No Ball Leg Bye".equalsIgnoreCase(LastBall.getBallType())
									|| "Good No Ball Leg Bye".equalsIgnoreCase(LastBall.getBallType())
									|| "Good No Ball of Bat".equalsIgnoreCase(LastBall.getBallType())) {
								if (LastBall.getRuns() % 2 == 0) {
									BattingDto temp = latestBatting;
									latestBatting = runner;
									runner = temp;
								}
							} else {
								if (LastBall.getRuns() % 2 == 1) {
									BattingDto temp = latestBatting;
									latestBatting = runner;
									runner = temp;
								}
							}

							matchBean.getLatestBatting().add(latestBatting);

							if (runner != null) {
								matchBean.getLatestBatting().add(runner);
							}

							/*
							 * if (runner != null && CommonUtility.isNullOrEmpty(runner.getHowOut())) {
							 * matchBean.getLatestBatting().add(runner); }
							 */
							if(match.getCurrentInnings()>2){
								matchBean.getLatestBowling().add((BowlingDto) bowlingMap.get("2" + varBowler));								
							}else {
								matchBean.getLatestBowling().add((BowlingDto) bowlingMap.get("" + varBowler));
							}
							if (LastBall.getOver() > 0) {
								OverDto over = (OverDto) matchBean.getOverMap().get(LastBall.getInningsNumber() + "-" + (LastBall.getOver() - 1));
								if (over != null) {
									if(match.getCurrentInnings()>2){
										matchBean.getLatestBowling().add((BowlingDto) bowlingMap.get("2" + over.getBowler1()));							
									}else {
										matchBean.getLatestBowling().add((BowlingDto) bowlingMap.get("" + over.getBowler1()));
									}
								}
							}
						}
					}
					
					  if(!CommonUtility.isListNullEmpty(superBalls))
					  { balls.addAll(regularBalls);
					  }
					 
					if (balls != null && balls.size() > 0) {
						for (BallDto ball : balls) {
							if (ball.getInningsNumber() == 1) {
								BallBean ballBean = null;
								if(ball.getIsSuperOver()==1) {
									ballBean = ballToBallBeanSuper(clubId, playersMap, battingMapSuper, ball);
								}else {
									ballBean = ballToBallBean(clubId, playersMap, battingMap, ball);
								}
								matchBean.getTeam1Balls().add(ballBean);
							} else if (ball.getInningsNumber() == 2) {
								BallBean ballBean = null;
								if(ball.getIsSuperOver()==1) {
									ballBean = ballToBallBeanSuper(clubId, playersMap, battingMapSuper, ball);
								}else {
									ballBean = ballToBallBean(clubId, playersMap, battingMap, ball);
								}
								matchBean.getTeam2Balls().add(ballBean);
							} else if (ball.getInningsNumber() == 3) {
								BallBean ballBean = ballToBallBean(clubId, playersMap, battingMap, ball);
								matchBean.getTeam3Balls().add(ballBean);
							} else if (ball.getInningsNumber() == 4) {
								BallBean ballBean = ballToBallBean(clubId, playersMap, battingMap, ball);
								matchBean.getTeam4Balls().add(ballBean);
							}

						}
						if (match.isLiveMatch()) {
							Collections.reverse(matchBean.getTeam1Balls());
							Collections.reverse(matchBean.getTeam2Balls());
							Collections.reverse(matchBean.getTeam3Balls());
							Collections.reverse(matchBean.getTeam4Balls());
						}

					}
					String outFromMap = "";
					String lastWicket = "";

					Map<String, List<PartnershipDto>> partnershipMap = (Map<String, List<PartnershipDto>>) records.get("partnershipMap");

					if (partnershipMap != null && !partnershipMap.isEmpty()) {
						matchBean.setPartnershipMap(partnershipMap);
						if (match.isFourthInningsStarted()) {
							outFromMap = "4-" + match.getT2_2wickets();
							lastWicket = getLastWicketString(clubId, team2Batting_2, partnershipMap, playersMap,
									outFromMap, match.getT2_2wickets());

						} else if (match.isThirdInningsStarted()) {
							outFromMap = "3-" + match.getT1_2wickets();
							lastWicket = getLastWicketString(clubId, team1Batting_2, partnershipMap, playersMap,
									outFromMap, match.getT1_2wickets());
						} else if (match.isSecondInningsStarted()) {
							outFromMap = "2-" + match.getT2wickets();
							lastWicket = getLastWicketString(clubId, team2Batting, partnershipMap, playersMap,
									outFromMap, match.getT2wickets());
						} else if (match.isFirstInningsStarted()) {
							outFromMap = "1-" + match.getT1wickets();
							lastWicket = getLastWicketString(clubId, team1Batting, partnershipMap, playersMap,
									outFromMap, match.getT1wickets());
						}
						match.setLastWicket(lastWicket);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " prepareBallByBall " + " MatchID - " + matchId + " and ClubId " + clubId,e);
		}
		return matchBean;
	}

	private static String getLastWicketString(int clubId, List<BattingDto> teamBatting,
			Map<String, List<PartnershipDto>> partnershipMap, Map<String, String> playerMap, String outFromMap,
			int wicketAtPoint) {
		String wicketString = "";
		List<PartnershipDto> partnerships = (List<PartnershipDto>) partnershipMap.get(outFromMap);
		if (partnerships != null && !partnerships.isEmpty()) {
			PartnershipDto ptrdto = partnerships.get(0);
			if (ptrdto != null && ptrdto.getOtherPlayerId() > 0) {
				int outPerson = ptrdto.getOutPlayerId();
				if (outPerson > 0) {
					for (BattingDto bt : teamBatting) {
						if (outPerson == bt.getPlayerID()) {
							wicketString = "Last Wkt: ";
							wicketString += bt.getPlayerName() + " " + bt.buildOutString(playerMap, clubId, false) + " "
									+ bt.getRunsScored() + "(" + bt.getBallsFaced() + ")" + " @" + ptrdto.getTeamTotal()
									+ "/" + wicketAtPoint + " in " + ptrdto.getOverNumber() + "."
									+ ptrdto.getBallNumber();
							break;
						}
					}
				}
			}
		}
		return wicketString;
	}

	private static BallBean ballToBallBean(int clubId, Map<String, String> playersMap,
			Map<String, BattingDto> battingMap, BallDto ball) {

		BallBean ballBean = new BallBean();

		ballBean.setBallId(ball.getBallId());
		ballBean.setOver(ball.getOver());
		ballBean.setBall(ball.getBall());
		ballBean.setRuns(ball.getRuns());
		ballBean.setBowler(ball.getBowler());
		ballBean.setStriker(ball.getBatsman());
		ballBean.setNonStriker(ball.getRunner());
		ballBean.setRunsDisplay(ball.getRunsDisplay());
		ballBean.setBallType(ball.getBallType());
		ballBean.setBowlerName(playersMap.get(ball.getBowler() + ""));
		ballBean.setStrikerName(playersMap.get(ball.getBatsman() + ""));
		ballBean.setNonStrikerName(playersMap.get(ball.getRunner() + ""));
		ballBean.setDirection(ball.getDirection());
		ballBean.setPitchMap(ball.getPitchMap());
		ballBean.setFour(ball.isFour());
		ballBean.setSix(ball.isSix());
		ballBean.setWicketTaker1(ball.getWicketTaker1());
		ballBean.setWicketTaker2(ball.getWicketTaker2());
		ballBean.setOutPerson(ball.getOutPerson());
		ballBean.setIsSuperOver(ball.getIsSuperOver());

		String commentary = "";
		commentary = ball.getBallInfoDisplay(playersMap, ball);

		if (ball.isOut()) {
			if (!CommonLogicFB.OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {

				commentary += "<strong> OUT!</strong> ";

				commentary += "<strong>" + ball.getOutMethodForDisplay().toUpperCase() + "</strong><br>";
				BattingDto dto = (BattingDto) battingMap.get("" + ball.getOutPerson());
				if (dto != null) {

					commentary += " <strong>" + dto.getPlayerName() + " "
							+ dto.buildOutString(playersMap, clubId, false) + " " + dto.getRunsScored() + "</strong> ";
					commentary += "(" + dto.getBallsFaced() + "balls  " + dto.getFours() + " fours, " + dto.getSixers()
							+ " sixes) ";
					commentary += "SR " + dto.getStrikeRate();
				}
			}

			if (CommonLogicFB.OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {

				BattingDto dto = (BattingDto) battingMap.get("" + ball.getOutPerson());
				if (dto != null) {

					commentary += " <strong>" + dto.getPlayerName() + " is RETIRED.</strong>";

				}
			}
		}
		ballBean.setCommentary(commentary);
		return ballBean;
	}
	
	private static BallBean ballToBallBeanSuper(int clubId, Map<String, String> playersMap,
			Map<String, BattingDto> battingMap, BallDto ball) {

		BallBean ballBean = new BallBean();

		ballBean.setBallId(ball.getBallId());
		ballBean.setOver(ball.getOver());
		ballBean.setBall(ball.getBall());
		ballBean.setRuns(ball.getRuns());
		ballBean.setBowler(ball.getBowler());
		ballBean.setStriker(ball.getBatsman());
		ballBean.setNonStriker(ball.getRunner());
		ballBean.setRunsDisplay(ball.getRunsDisplay());
		ballBean.setBallType(ball.getBallType());
		ballBean.setBowlerName(playersMap.get(ball.getBowler() + ""));
		ballBean.setStrikerName(playersMap.get(ball.getBatsman() + ""));
		ballBean.setNonStrikerName(playersMap.get(ball.getRunner() + ""));
		ballBean.setDirection(ball.getDirection());
		ballBean.setPitchMap(ball.getPitchMap());
		ballBean.setFour(ball.isFour());
		ballBean.setSix(ball.isSix());
		ballBean.setWicketTaker1(ball.getWicketTaker1());
		ballBean.setWicketTaker2(ball.getWicketTaker2());
		ballBean.setOutPerson(ball.getOutPerson());
		ballBean.setIsSuperOver(ball.getIsSuperOver());

		String commentary = "";
		commentary = ball.getBallInfoDisplay(playersMap, ball);

		if (ball.isOut()) {
			if (!CommonLogicFB.OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {

				commentary += "<strong> OUT!</strong> ";

				commentary += "<strong>" + ball.getOutMethodForDisplay().toUpperCase() + "</strong><br>";
				BattingDto dto = (BattingDto) battingMap.get("" + ball.getOutPerson());
				
				if (dto != null) {

					commentary += " <strong>" + dto.getPlayerName() + " "
							+ dto.buildOutString(playersMap, clubId, false) + " " + dto.getRunsScored() + "</strong> ";
					commentary += "(" + dto.getBallsFaced() + "balls  " + dto.getFours() + " fours, " + dto.getSixers()
							+ " sixes) ";
					commentary += "SR " + dto.getStrikeRate();
				}
			}

			if (CommonLogicFB.OUT_METHOD_RETIRED.equals(ball.getOutMethod())) {

				BattingDto dto = (BattingDto) battingMap.get("" + ball.getOutPerson());
				if (dto != null) {

					commentary += " <strong>" + dto.getPlayerName() + " is RETIRED.</strong>";

				}
			}
		}
		ballBean.setCommentary(commentary);
		return ballBean;
	}

	public static Map<String, BattingDto> createBattingRecordsMap(List<BattingDto> team1Batting,
			List<BattingDto> team2Batting, List<BattingDto> team1Batting_2, List<BattingDto> team2Batting_2,
			MatchDto match) {
		Map<String, BattingDto> battingMap = new HashMap<String, BattingDto>();
		if (team1Batting != null) {
			for (BattingDto batting : team1Batting) {
				if(batting.getInnings() == 1) {
					battingMap.put("" + batting.getPlayerID(), batting);
				}
			}
		}
		if (team2Batting != null) {
			for (BattingDto batting : team2Batting) {
				if(batting.getInnings() == 1) {
					battingMap.put("" + batting.getPlayerID(), batting);
				}
			}
		}

		if (match != null && (!match.isLiveMatch() || match.getIsComplete() == 1 || match.isThirdInningsStarted())) {
			if (team1Batting_2 != null) {
				for (BattingDto batting : team1Batting_2) {
					if(batting.getInnings() == 2) {
						battingMap.put("2-" + batting.getPlayerID(), batting);
					}
				}
			}
			if (team2Batting_2 != null) {
				for (BattingDto batting : team2Batting_2) {
					if(batting.getInnings() == 2) {
						battingMap.put("2-" + batting.getPlayerID(), batting);
					}
				}
			}
		}
		return battingMap;
	}
	
	public static Map<String, BattingDto> createBattingRecordsMapSuper(List<BattingDto> team1BattingSuper,
			List<BattingDto> team2BattingSuper,	MatchDto match) {
		Map<String, BattingDto> battingMap = new HashMap<String, BattingDto>();
		if (team1BattingSuper != null) {
			for (BattingDto batting : team1BattingSuper) {
				battingMap.put("" + batting.getPlayerID(), batting);
			}
		}
		if (team2BattingSuper != null) {
			for (BattingDto batting : team2BattingSuper) {
				battingMap.put("" + batting.getPlayerID(), batting);
			}
		}
		
		return battingMap;
	}

	public static Map<String, BowlingDto> createBowlingRecordsMap(List<BowlingDto> team1Bowling,
			List<BowlingDto> team2Bowling, List<BowlingDto> team1Bowling_2, List<BowlingDto> team2Bowling_2,
			MatchDto match) {
		Map<String, BowlingDto> bowlingMap = new HashMap<String, BowlingDto>();
		if (team1Bowling != null) {
			for (BowlingDto bowling : team1Bowling) {
				if(bowling.getInnings() == 1) {
					bowlingMap.put("" + bowling.getPlayerID(), bowling);
				}
			}
		}
		if (team2Bowling != null) {
			for (BowlingDto bowling : team2Bowling) {
				if(bowling.getInnings() == 1) {
					bowlingMap.put("" + bowling.getPlayerID(), bowling);
				}
			}
		}
		if (team1Bowling_2 != null) {
			for (BowlingDto bowling : team1Bowling_2) {
				if(bowling.getInnings() == 2) {
					bowlingMap.put("2" + bowling.getPlayerID(), bowling);
				}
			}
		}
		if (team2Bowling_2 != null) {
			for (BowlingDto bowling : team2Bowling_2) {
				if(bowling.getInnings() == 2) {
					bowlingMap.put("2" + bowling.getPlayerID(), bowling);
				}
			}
		}
		return bowlingMap;
	}
	
	public static Map<String, BowlingDto> createBowlingRecordsMapSuper(List<BowlingDto> team1BowlingSuper,
			List<BowlingDto> team2BowlingSuper,	MatchDto match) {
		
		Map<String, BowlingDto> bowlingMap = new HashMap<String, BowlingDto>();
		if (team1BowlingSuper != null) {
			for (BowlingDto bowling : team1BowlingSuper) {
				bowlingMap.put("" + bowling.getPlayerID(), bowling);
			}
		}
		if (team2BowlingSuper != null) {
			for (BowlingDto bowling : team2BowlingSuper) {
				bowlingMap.put("" + bowling.getPlayerID(), bowling);
			}
		}		
		return bowlingMap;
	}

	public static List<PlayerDto> setTeamsForPlayers(List<PlayerDto> players) {

		if (players != null && !players.isEmpty()) {
			LinkedHashMap<String, PlayerDto> playersMap = new LinkedHashMap<String, PlayerDto>();
			Iterator<PlayerDto> itr = players.iterator();
			while (itr.hasNext()) {
				PlayerDto player = (PlayerDto) itr.next();
				if (playersMap.get(String.valueOf(player.getPlayerID())) == null) {
					playersMap.put(String.valueOf(player.getPlayerID()), player);
				}
			}
			return new ArrayList<PlayerDto>(playersMap.values());
		}
		return null;
	}

	public static LiveMatchInfo prepareLiveMatchInfo(long matchId, int clubId) {
		LiveMatchInfo matchInfo = new LiveMatchInfo();
		try {
			MatchDto match = MatchesFactory.getMatch(matchId, clubId);
			matchInfo.setMatchId(match.getMatchID());
			matchInfo.setTeamOne(match.getTeamOne());
			matchInfo.setTeamOneName(match.getTeamOneName());
			matchInfo.setTeamOneCaptain(match.getTeamOneCaptain());
			matchInfo.setTeamTwo(match.getTeamTwo());
			matchInfo.setTeamTwoName(match.getTeamTwoName());
			matchInfo.setTeamTwoCaptain(match.getTeamTwoCaptain());
			matchInfo.setTossWon(match.getTossWon());
			matchInfo.setBattingFirst(match.getBattingFirst());
			matchInfo.setOvers(match.getOvers());
			matchInfo.setWinner(match.getWinner());
			matchInfo.setLocation(match.getLocation());
			matchInfo.setMatchDate(match.getMatchDate());
			matchInfo.setManOfTheMatch(match.getManOfTheMatch());
			matchInfo.setMatchType(match.getMatchType());
			matchInfo.setIsComplete(match.getIsComplete());
			matchInfo.setScorer(match.getScorer());
			matchInfo.setIsFollowon(match.getIsFollowon());
			matchInfo.setDls(match.isDls());
			matchInfo.setR1ResAvailable(match.getR1ResAvailable());
			matchInfo.setT2Target(match.getT2Target());
			matchInfo.setT2RevisedOvers(match.getT2RevisedOvers());

			Map<Integer, String> t1Players = new TreeMap<Integer, String>();
			Map<Integer, String> t2Players = new TreeMap<Integer, String>();
			List<PlayerDto> players = PlayerFactory.getPlayersOfMatch(match.getMatchID(), clubId);
			for (PlayerDto player : players) {
				if (match.getPlayers1().contains(player.getPlayerID())) {
					t1Players.put(player.getPlayerID(),
							getTrimedName(player.getFullName(), 13) + getJerseyNumberString(player));
				} else if (match.getPlayers2().contains(player.getPlayerID())) {
					t2Players.put(player.getPlayerID(),
							getTrimedName(player.getFullName(), 13) + getJerseyNumberString(player));
				}
			}

			matchInfo.setPlayers1(CommonUtility.sortByValue(t1Players));
			matchInfo.setPlayers2(CommonUtility.sortByValue(t2Players));

			ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
			matchInfo.setCanAddPlayers(club.isCanScorerAddPlayer());
			matchInfo.setClubStructure(club.isClubStructureEnabled());
			
			try {
				LeagueDto league = LeagueFactory.getLeague(match.getLeagueId(), clubId);
				if(league != null) {
					if(league.getParent() > 0) {
						league = LeagueFactory.getLeague(league.getParent(), clubId);
					}
					matchInfo.setSeriesName(league.getName());
					matchInfo.setOversPerPair(league.getPairOvers());
					matchInfo.setRunsLostPerWicket(league.getTeamRunsLost());
					matchInfo.setRunsGainPerWicket(league.getTeamRunsGain());
					matchInfo.setStrikeChange(league.getStrikeChange());
					matchInfo.setStartingScore(league.getStartScore());
					
					if(!CommonUtility.isNullOrEmpty(matchInfo.getOversPerPair()) || !CommonUtility.isNullOrEmpty(matchInfo.getRunsLostPerWicket()) 
							|| !CommonUtility.isNullOrEmpty(matchInfo.getRunsGainPerWicket()) || matchInfo.getStrikeChange() > 0 
								|| !CommonUtility.isNullOrEmpty(matchInfo.getStartingScore())) {
						matchInfo.setCustomFormat(true);
					}
				}
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " prepareLiveMatchInfo " + " MatchID - " + matchId + " and ClubId " + clubId);
		}

		return matchInfo;
	}

	public static String getTrimedName(String fullName, int length) {
		if (!CommonUtility.isNullOrEmpty(fullName)) {
			return fullName.substring(0, Math.min(fullName.length(), length));
		}
		return "";
	}

	public static String getJerseyNumberString(PlayerDto player) {
		if (player != null && !CommonUtility.isNullOrEmpty(player.getJerseyNumber())) {
			return " (" + player.getJerseyNumber() + ")";
		}
		return "";
	}

	public static String getMatchTimings(List<IncidentDto> incidentsList, MatchDto match, ClubDtoLite club) {
		
		String returnStr = "";
		
		String firstHalfStart = "";
		String firstHalfEnd = "";
		long firtsHalfDiffMin = 0;
		long breakDiffMin = 0;
		String secondHalfStart = "";
		String secondHalfEnd = "";
		long secondHalfDiffMin = 0;
		
		IncidentDto firstHalfStartDto=null;
		IncidentDto lastDto =null;
		if(!CommonUtility.isListNullEmpty(incidentsList)) {
		 firstHalfStartDto = incidentsList.get(0);
		 lastDto = incidentsList.get(incidentsList.size() - 1);
		}
		IncidentDto breakDto = null;
		IncidentDto secondHalfStartDto = null;
		
		for (IncidentDto dto : incidentsList) {
			if ("Second Half Started".equalsIgnoreCase(dto.getIncidentType())) {
				secondHalfStartDto = dto;
			}
			if ("break".equalsIgnoreCase(dto.getIncidentType())) {
				breakDto = dto;
			}
		}
		if (breakDto == null && secondHalfStartDto != null) {
			breakDto = secondHalfStartDto;
		}

		if (!firstHalfStart.isEmpty()) {
			firstHalfStart = CommonUtility.dateToString(firstHalfStartDto.getCreatedTime(), "HH:mm", club.getTimeZone());
		}
		if (breakDto != null) {
			firstHalfEnd = CommonUtility.dateToString_HHMM_Format(breakDto.getCreatedTime(), "HH:mm");
		}
		if (firstHalfStartDto != null && breakDto != null) {
			long firstHalfDiff = breakDto.getCreatedTime().getTime() - firstHalfStartDto.getCreatedTime().getTime();
			firtsHalfDiffMin = (firstHalfDiff / 1000) / 60;
		}
		if (breakDto != null && secondHalfStartDto != null) {
			long breakDiff = secondHalfStartDto.getCreatedTime().getTime() - breakDto.getCreatedTime().getTime();
			breakDiffMin = (breakDiff / 1000) / 60;
		}
		if (secondHalfStartDto != null) {
			secondHalfStart = CommonUtility.dateToString(secondHalfStartDto.getCreatedTime(), "HH:mm",
					club.getTimeZone());
		}
		if (secondHalfStartDto != null) {
			long secondHalfDiff = lastDto.getCreatedTime().getTime() - secondHalfStartDto.getCreatedTime().getTime();
			secondHalfDiffMin = (secondHalfDiff / 1000) / 60;
		}
		if (match.getIsComplete() == 1) {
			if (lastDto != null) {
				secondHalfEnd = CommonUtility.dateToString(lastDto.getCreatedTime(), "HH:mm", club.getTimeZone());
			}
		}

		if (breakDto == null) {
			returnStr += "<tr title='" + club.getTimeZone()
					+ "'><td style='padding-left: 10px'><strong>1st Half Start: </Strong></td><td>" + firstHalfStart
					+ " </td></tr>";
		} else {
			returnStr += "<tr title='" + club.getTimeZone()
					+ "'><td style='padding-left: 10px'><strong>1st Half: </Strong></td><td><strong>" 
					+ " </strong> &nbsp; &nbsp; &nbsp; &nbsp;" + firstHalfStart + "   &nbsp; &nbsp; &nbsp; &nbsp;"
					 + " </td></tr>";

			/*
			 * if (secondHalfStartDto != null) { returnStr += "<tr  title='" +
			 * club.getTimeZone() +
			 * "'><font size='1'><td style='padding-left: 10px'><strong><font size='2'>break:</font></Strong></td><td><strong>"
			 * + breakDiffMin + " min</strong> &nbsp; &nbsp; &nbsp; &nbsp;" + firstHalfEnd +
			 * "   &nbsp; &nbsp; &nbsp; &nbsp;" + secondHalfStart + " </td></font></tr>"; }
			 */
			if (secondHalfStartDto != null) {
				if (match.getIsComplete() == 0) {
					returnStr += "<tr  title='" + club.getTimeZone()
							+ "'><td style='padding-left: 10px'><strong>2nd Half Start: </Strong></td><td>"
							+ secondHalfStart + " </td></tr>";
				} else if (match.getIsComplete() == 1) {
					returnStr += "<tr  title='" + club.getTimeZone()
							+ "'><td style='padding-left: 10px'><strong>2nd Half: </Strong></td><td><strong>"
							 + " </strong> &nbsp; &nbsp; &nbsp; &nbsp;" + secondHalfStart
							+ "   &nbsp; &nbsp; &nbsp; &nbsp;"  + " </td></tr>";
				}
			}
		}

		return returnStr + "";
	}
	
	public static Map<String, String> getMatchTimingsMap(List<BallDto> balls, MatchDto match, ClubDtoLite club) {
		
		Map<String, String> matchTimingsMap = new HashMap<String, String>();
		
		if (balls != null && match != null && !balls.isEmpty()) {
			int secondInningsIndex = 0;
			int index = 0;
			int validBallIndex = 0;
			String firstInnStart = "";
			for (BallDto ball : balls) {
				if (ball.getInningsNumber() == 2) {
					secondInningsIndex = index;
					break;
				}
				index++;
			}
			for (BallDto ball : balls) {
				if (ball.getOver() == 0 && ball.getBatsman() > 0 && ball.getBowler() > 0 && ball.getRunner() > 0) {
					firstInnStart = CommonUtility.dateToString(ball.getCreateDate(), "HH:mm", club.getTimeZone());
					break;
				}
				validBallIndex++;
			}
			if (secondInningsIndex > 0) {
				String firstInnEnd = CommonUtility.dateToString(balls.get(secondInningsIndex - 1).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long firstDiff = (balls.get(secondInningsIndex - 1).getCreateDate().getTime())
						- (balls.get(validBallIndex).getCreateDate().getTime());
				long firtsDiffMin = (firstDiff / 1000) / 60;
				
				matchTimingsMap.put("1st_innings_diff_mins", firtsDiffMin+"");

				String secondInnStart = CommonUtility.dateToString(balls.get(secondInningsIndex).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long breakDiff = (balls.get(secondInningsIndex).getCreateDate().getTime())
						- (balls.get(secondInningsIndex - 1).getCreateDate().getTime());
				long breakDiffMin = (breakDiff / 1000) / 60;

				if (!match.isInProgress()) {
					String secondInnEnd = CommonUtility.dateToString(balls.get(balls.size() - 1).getCreateDate(),
							"HH:mm", club.getTimeZone());
					long secDiff = (balls.get(balls.size() - 1).getCreateDate().getTime())
							- (balls.get(secondInningsIndex).getCreateDate().getTime());
					long secDiffMin = (secDiff / 1000) / 60;
					matchTimingsMap.put("2nd_innings_diff_mins", secDiffMin+"");
				} 
			}
		}

		return matchTimingsMap;
	}

	public static String getMatchTimingsTheme2Mob(List<BallDto> balls, MatchDto match, ClubDtoLite club) {
		String returnStr = "";
		if (balls != null && match != null && !balls.isEmpty()) {
			int secondInningsIndex = 0;
			int index = 0;
			int validBallIndex = 0;
			String firstInnStart = "";
			for (BallDto ball : balls) {
				if (ball.getInningsNumber() == 2) {
					secondInningsIndex = index;
					break;
				}
				index++;
			}
			for (BallDto ball : balls) {
				if (ball.getOver() == 0 && ball.getBatsman() > 0 && ball.getBowler() > 0 && ball.getRunner() > 0) {
					firstInnStart = CommonUtility.dateToString(ball.getCreateDate(), "HH:mm", club.getTimeZone());
					break;
				}
				validBallIndex++;
			}
			if (secondInningsIndex <= 0) {
				returnStr += "<li title='" + club.getTimeZone() + "'><label>1st Innings Start: </label><td>"
						+ firstInnStart + " </td></li>";
			}
			if (secondInningsIndex > 0) {
				String firstInnEnd = CommonUtility.dateToString(balls.get(secondInningsIndex - 1).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long firstDiff = (balls.get(secondInningsIndex - 1).getCreateDate().getTime())
						- (balls.get(validBallIndex).getCreateDate().getTime());
				long firtsDiffMin = (firstDiff / 1000) / 60;

				String secondInnStart = CommonUtility.dateToString(balls.get(secondInningsIndex).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long breakDiff = (balls.get(secondInningsIndex).getCreateDate().getTime())
						- (balls.get(secondInningsIndex - 1).getCreateDate().getTime());
				long breakDiffMin = (breakDiff / 1000) / 60;

				returnStr += "<li title='" + club.getTimeZone()
						+ "'><label>1st Innings  </label> :<p class='mb-0'><span class='mr-3'>" + firtsDiffMin
						+ " Min (" + firstInnStart + " -  " + firstInnEnd + ")</span> </p></li>";

				returnStr += "<li  title='" + club.getTimeZone()
						+ "'><label>Innings break</label> :<p class='mb-0'><span class='mr-3'>"

						+ breakDiffMin + " Min (" + firstInnEnd + " - " + secondInnStart + ") </span> </p></li>";
				if (!match.isInProgress()) {
					String secondInnEnd = CommonUtility.dateToString(balls.get(balls.size() - 1).getCreateDate(),
							"HH:mm", club.getTimeZone());
					long secDiff = (balls.get(balls.size() - 1).getCreateDate().getTime())
							- (balls.get(secondInningsIndex).getCreateDate().getTime());
					long secDiffMin = (secDiff / 1000) / 60;
					returnStr += "<li  title='" + club.getTimeZone()
							+ "'><label>2nd Innings </label>:<p class='mb-0'> <span class='mr-3'>" + secDiffMin
							+ " Min (" + secondInnStart + " - " + secondInnEnd + ") </span> </p></li>";
				} else {
					returnStr += "<li  title='" + club.getTimeZone()
							+ "'><label>2nd Innings Start</label>:<p class='mb-0'> <span class='mr-3'>" + secondInnStart
							+ "</span> </p></li>";
				}
			}
		}

		return returnStr + "";
	}

	public static String getMatchTimingsTheme2(List<BallDto> balls, MatchDto match, ClubDtoLite club) {
		String returnStr = "";
		if (balls != null && match != null && !balls.isEmpty()) {
			int secondInningsIndex = 0;
			int index = 0;
			int validBallIndex = 0;
			String firstInnStart = "";
			for (BallDto ball : balls) {
				if (ball.getInningsNumber() == 2) {
					secondInningsIndex = index;
					break;
				}
				index++;
			}
			for (BallDto ball : balls) {
				if (ball.getOver() == 0 && ball.getBatsman() > 0 && ball.getBowler() > 0 && ball.getRunner() > 0) {
					firstInnStart = CommonUtility.dateToString(ball.getCreateDate(), "HH:mm", club.getTimeZone());
					break;
				}
				validBallIndex++;
			}
			if (secondInningsIndex <= 0) {
				returnStr += "<tr title='" + club.getTimeZone() + "'><td>1st Innings Start: </td><td>" + firstInnStart
						+ " </td></tr>";
			}
			if (secondInningsIndex > 0) {
				String firstInnEnd = CommonUtility.dateToString(balls.get(secondInningsIndex - 1).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long firstDiff = (balls.get(secondInningsIndex - 1).getCreateDate().getTime())
						- (balls.get(validBallIndex).getCreateDate().getTime());
				long firtsDiffMin = (firstDiff / 1000) / 60;

				String secondInnStart = CommonUtility.dateToString(balls.get(secondInningsIndex).getCreateDate(),
						"HH:mm", club.getTimeZone());
				long breakDiff = (balls.get(secondInningsIndex).getCreateDate().getTime())
						- (balls.get(secondInningsIndex - 1).getCreateDate().getTime());
				long breakDiffMin = (breakDiff / 1000) / 60;

				returnStr += "<tr title='" + club.getTimeZone() + "'><td>1st Innings <td>:</td> </td><td>"
						+ firtsDiffMin + " Min (" + firstInnStart + " -  " + firstInnEnd + ") </td></tr>";
				returnStr += "<tr  title='" + club.getTimeZone() + "'><td>Innings break<td>:</td></td><td>"
						+ breakDiffMin + " Min (" + firstInnEnd + " - " + secondInnStart + ") </td></tr>";
				if (!match.isInProgress()) {
					String secondInnEnd = CommonUtility.dateToString(balls.get(balls.size() - 1).getCreateDate(),
							"HH:mm", club.getTimeZone());
					long secDiff = (balls.get(balls.size() - 1).getCreateDate().getTime())
							- (balls.get(secondInningsIndex).getCreateDate().getTime());
					long secDiffMin = (secDiff / 1000) / 60;
					returnStr += "<tr  title='" + club.getTimeZone() + "'><td>2nd Innings<td>:</td> </td><td>"
							+ secDiffMin + " Min (" + secondInnStart + " - " + secondInnEnd + ") </td></tr>";
				} else {
					returnStr += "<tr  title='" + club.getTimeZone() + "'><td>2nd Innings Start<td>:</td> </td><td>"
							+ secondInnStart + " </td></tr>";
				}
			}
		}

		return returnStr + "";
	}

	public static String getFirstInngingsStartTime(MatchDto match, int clubId) throws Exception {
		String startTime = "";
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
		if (balls != null && match != null && !balls.isEmpty()) {
			startTime = CommonUtility.dateToString(balls.get(0).getCreateDate(), "HH:mm", club.getTimeZone());
		}
		return startTime;
	}

	public static String getSecondInngingsEndTime(MatchDto match, int clubId) throws Exception {
		String endTime = "";
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), clubId);
		if (!match.isInProgress() && balls != null && balls.size()>0) {
			endTime = CommonUtility.dateToString(balls.get(balls.size() - 1).getCreateDate(), "HH:mm", club.getTimeZone());

		}
		return endTime;
	}

	public static void postInFacebook(MatchDto match, int clubId) throws Exception {
		List<FacebookPageDto> pages = FacebookFactory.getFacebookPagesOfClub(clubId);
		String message = match.getResult();
		String link = "https://cricclubs.com/viewScorecard.do?matchId=" + match.getMatchID() + "&clubId=" + clubId
				+ "&source=facebook";
		if (pages != null && !pages.isEmpty()) {
			for (FacebookPageDto page : pages) {
				String postId = FacebookFactory.getFacebookPostId(page.getId(), match.getMatchID(), clubId);
				if (!CommonUtility.isNullOrEmpty(postId)) {
					if (page.isPage()) {
						FacebookConnector.editPagePost(message, link, postId, page.getAccessToken(), page.getPageId());
					} else {
						FacebookConnector.editGroupPost(message, link, postId, page.getGroupId());
					}
				} else {
					if (page.isPage()) {
						postId = FacebookConnector.createPagePost(message, link, page.getAccessToken(),
								page.getPageId());
					} else {
						postId = FacebookConnector.createGroupPost(message, link, page.getGroupId());
					}

					FacebookFactory.addMatchPost(postId, page.getId(), match.getMatchID(), clubId);
				}
			}
		}

	}

	public static List<Integer> getUpperDivisions(int clubId, LeagueDto league) throws Exception {
		List<Integer> upperDivisions = new ArrayList<Integer>();
		if (league.getParent() != 0) {
			List<LeagueLite> allLeagues = ClubFactory.getActiveLiteClub(clubId, true).getLeagueList();
			LeagueLite parent = null;
			for (LeagueLite l : allLeagues) {
				if (l.getLeagueId() == league.getParent()) {
					parent = l;
				}
			}
			for (LeagueLite l : parent.getDivisions()) {
				if (l.getLeagueId() == league.getLeagueId()) {
					break;
				} else {
					upperDivisions.add(l.getLeagueId());
				}
			}
		}
		return upperDivisions;
	}

	public static Map<String, AdConfigGlobalDTO> getAdConfigGlobal(ClubDtoLite clubDto) {
		if (clubDto == null) {
			return null;
		}
		Map<String, AdConfigGlobalDTO> map = new HashMap<String, AdConfigGlobalDTO>();
		try {
			List<AdConfigGlobalDTO> adConfigGlobalDTOs = AdConfigGlobalFactory.getAdConfigGlobalDTOs();
			for (AdConfigGlobalDTO adConfigGlobalDTO : adConfigGlobalDTOs) {
				if (validateAdConfig(adConfigGlobalDTO, clubDto)) {
					if ("BANNER".equalsIgnoreCase(adConfigGlobalDTO.getAdType())) {
						map.put("BANNER", adConfigGlobalDTO);
					}
					if ("SQUARE".equalsIgnoreCase(adConfigGlobalDTO.getAdType())) {
						map.put("SQUARE", adConfigGlobalDTO);
					}
					if ("EMAIL".equalsIgnoreCase(adConfigGlobalDTO.getAdType())) {
						map.put("EMAIL", adConfigGlobalDTO);
					}
					if ("HOTSTAR".equalsIgnoreCase(adConfigGlobalDTO.getAdType())) {
						map.put("HOTSTAR", adConfigGlobalDTO);
					}
					if ("MOBILE_APP".equalsIgnoreCase(adConfigGlobalDTO.getAdType())) {
						map.put("MOBILE_APP", adConfigGlobalDTO);
					}
				}
			}

		} catch (Exception e) {
			// log.error(e.getMessage(), e);
			return null;
		}
		return map;
	}

	private static boolean validateAdConfig(AdConfigGlobalDTO adConfigGlobalDTO, ClubDtoLite clubDto) {
		if (!CommonUtility.isNullOrEmptyOrNULL(adConfigGlobalDTO.getCity())) {
			if (clubDto.getCity() != null && clubDto.getCity().equalsIgnoreCase(adConfigGlobalDTO.getCity())
					&& clubDto.getState() != null && clubDto.getState().equalsIgnoreCase(adConfigGlobalDTO.getState())
					&& clubDto.getCountry() != null
					&& clubDto.getCountry().equalsIgnoreCase(adConfigGlobalDTO.getCountry())) {
				return true;
			}
		} else if (!CommonUtility.isNullOrEmptyOrNULL(adConfigGlobalDTO.getState())) {
			if (clubDto.getState() != null && clubDto.getState().equalsIgnoreCase(adConfigGlobalDTO.getState())
					&& clubDto.getCountry() != null
					&& clubDto.getCountry().equalsIgnoreCase(adConfigGlobalDTO.getCountry())) {
				return true;
			}
		} else if (!CommonUtility.isNullOrEmptyOrNULL(adConfigGlobalDTO.getCountry())) {
			if (clubDto.getCountry() != null && clubDto.getCountry().equalsIgnoreCase(adConfigGlobalDTO.getCountry())) {
				return true;
			} else if ("ALL".equalsIgnoreCase(adConfigGlobalDTO.getCountry())) {
				return true;
			}
		}

		return false;
	}

	public static void arangeBattingBowlingList(ScoreCardBean fullScorecard) {

		if (fullScorecard != null) {

			List<BattingDto> t1Bt = fullScorecard.getTeam1Batting();
			List<BattingDto> t2Bt = fullScorecard.getTeam2Batting();
			List<BattingDto> t1_2Bt = fullScorecard.getTeam1_2Batting();
			List<BattingDto> t2_2Bt = fullScorecard.getTeam2_2Batting();

			fullScorecard.setTeam1Batting(removeIfInningWrongBt(t1Bt, 1));
			fullScorecard.setTeam2Batting(removeIfInningWrongBt(t2Bt, 1));
			fullScorecard.setTeam1_2Batting(removeIfInningWrongBt(t1_2Bt, 2));
			fullScorecard.setTeam2_2Batting(removeIfInningWrongBt(t2_2Bt, 2));

			List<BowlingDto> t1Bl = fullScorecard.getTeam1Bowling();
			List<BowlingDto> t2Bl = fullScorecard.getTeam2Bowling();
			List<BowlingDto> t1_2Bl = fullScorecard.getTeam1_2Bowling();
			List<BowlingDto> t2_2Bl = fullScorecard.getTeam2_2Bowling();

			fullScorecard.setTeam1Bowling(removeIfInningWrongBl(t1Bl, 1));
			fullScorecard.setTeam2Bowling(removeIfInningWrongBl(t2Bl, 1));
			fullScorecard.setTeam1_2Bowling(removeIfInningWrongBl(t1_2Bl, 2));
			fullScorecard.setTeam2_2Bowling(removeIfInningWrongBl(t2_2Bl, 2));

		}

	}

	private static List<BowlingDto> removeIfInningWrongBl(List<BowlingDto> tB, int i) {
		List<BowlingDto> blList = new ArrayList<BowlingDto>();
		if (tB != null && !tB.isEmpty()) {
			Iterator<BowlingDto> itr = tB.iterator();
			while (itr.hasNext()) {
				BowlingDto btd = itr.next();
				if (btd.getInnings() == i) {
					blList.add(btd);
				}
			}

		}
		return blList;
	}

	private static List<BattingDto> removeIfInningWrongBt(List<BattingDto> tBt, int i) {
		List<BattingDto> t1BtList = new ArrayList<BattingDto>();
		if (tBt != null && !tBt.isEmpty()) {
			Iterator<BattingDto> itr = tBt.iterator();

			while (itr.hasNext()) {
				BattingDto btd = itr.next();
				if (btd.getInnings() == i) {
					t1BtList.add(btd);
				}
			}
		}
		return t1BtList;
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
			teamList.addAll(bowlingOrderList);
		}
		return teamList;
	}

	public static void setPlayerDetailsForPartnershipMap(List<PlayerDto> listOffPlayers,
			Map<String, List<PartnershipDto>> partnershipMap) {
		if (partnershipMap != null && !partnershipMap.isEmpty()) {
			for (Map.Entry<String, List<PartnershipDto>> entry : partnershipMap.entrySet()) {
				List<PartnershipDto> listOfPartner = entry.getValue();
				if (listOfPartner != null && !listOfPartner.isEmpty()) {
					for (PartnershipDto ptrdto : listOfPartner) {
						for (PlayerDto pdto : listOffPlayers) {
							if (pdto.getPlayerID() == ptrdto.getOutPlayerId()) {
								ptrdto.setProfilepic_file_path(pdto.getProfilepic_file_path());
							}
						}
					}
				}
			}
		}
	}

	public static String getPlayerName(Map playersMap, int playerId, int clubId, int firstNameFirst) {

		String playerName = (String) playersMap.get("" + playerId);
		if (CommonUtility.isNullOrEmpty(playerName) && clubId > 0) {
			try {
				PlayerDto matchPlayer = PlayerFactory.getPlayerById(playerId, clubId);
				playerName = matchPlayer.getPlayerShortName(firstNameFirst);
				playersMap.put(matchPlayer.getPlayerID(), playerName);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return playerName;
	}

	public static ParScoreData getParScoareData(MatchDto match, int clubId) {

		int t2TargetBalls = 0;
		int t2BallsLeft = 0;

		int t1Score = match.getT1_1total();
		int t2BallsPlayed = match.getT2balls();
		int t2Wickets = match.getT2wickets();

		double parScore = 0.0;
		double t2TotalResLoss = 0.0;
		double t1ResAvailable = 100.0d;
		double t2ResAvailable = 0.0;
		double t2ResAvaiableSpc = 0.0;

		ParScoreBall psBall;
		List<ParScoreBall> psBalls = new ArrayList<ParScoreBall>();
		ParScoreData psData = new ParScoreData();
		List<String> data;

		Map<Integer, Double> percentagesMap = null;
		try {
			percentagesMap = DLSCalculationFactory.getPercentagesFromDLSChart();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " getParScoareData MatchID - " + match != null ? match.getMatchID() + ""
					: " Match not found " + " and ClubId " + clubId);
		}

		String matchType = "50";
		if (match.getOvers() <= 20) {
			matchType = "20";
		}

		double g50Value = ApplicationConstants.G_50_VALUE;

		if (match.isDls()) {
			if (match.getR1ResAvailable() > 0) {
				t1ResAvailable = match.getR1ResAvailable();
			}
			List<DLSInputData> intruptions = new ArrayList<DLSInputData>();

			List<MatchDLRecord> matchDLRecords2 = null;
			try {
				matchDLRecords2 = MatchDLRecordsFactory.getDLRecords(match.getMatchID(), 2, clubId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (matchDLRecords2 != null && matchDLRecords2.size() > 0) {
				for (MatchDLRecord rec : matchDLRecords2) {
					intruptions.add(CommonUtility.convertIntoDlsInputList(rec));
				}
				DLSInputData firstRecord = intruptions.get(0);
				t2TargetBalls = firstRecord.getBallsAtStartOfPlay();
			}
			if (t2TargetBalls == 0) {
				t2TargetBalls = CommonUtility.oversToBalls(match.getT2RevisedOvers() + "");
			}
			t2BallsLeft = CommonUtility.oversToBalls((match.getT2RevisedOvers() + "")) - t2BallsPlayed;
			try {
				t2TotalResLoss = CommonUtility.getT2TotalResLoss(clubId, match, intruptions);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error(e.getMessage() + " getParScoareData MatchID - " + match != null ? match.getMatchID() + ""
						: " Match not found " + " and ClubId " + clubId);
			}
		} else {
			t2TargetBalls = CommonUtility.oversToBalls(match.getOvers() + "");
			t2BallsLeft = CommonUtility.oversToBalls((match.getOvers() + "")) - t2BallsPlayed;
		}

		int ballStr = CommonUtility.stringToInt(t2TargetBalls + "0" + matchType);

		if (percentagesMap.get(ballStr) != null) {
			t2ResAvailable = percentagesMap.get(ballStr);
		}
		t2ResAvailable = t2ResAvailable - t2TotalResLoss;
		for (int i = 0; i < t2BallsLeft; i++) {

			psBall = new ParScoreBall();
			data = new ArrayList<String>();

			psBall.setOversBowled(CommonUtility.ballsToOvers(t2BallsPlayed + i));
			psBall.setOversRemaining(CommonUtility.ballsToOvers(t2BallsLeft - i));

			for (int j = 0; j < 10; j++) {
				if (j >= t2Wickets) {

					int ballsLeftRS = t2BallsLeft - i;
					int ballStr1 = CommonUtility.stringToInt(ballsLeftRS + "" + j + "" + matchType);
					Double rs1 = 0.0;

					if (percentagesMap.get(ballStr1) != null) {
						rs1 = percentagesMap.get(ballStr1);
					}

					t2ResAvaiableSpc = t2ResAvailable - rs1;

					if (t2ResAvaiableSpc > t1ResAvailable) {
						parScore = t1Score + (g50Value * ((t2ResAvaiableSpc - t1ResAvailable) / 100.0d));
					} else if (t2ResAvaiableSpc < t1ResAvailable) {
						parScore = t1Score * (t2ResAvaiableSpc / t1ResAvailable);
					} else {
						parScore = t1Score;
					}

					data.add(Math.round(parScore) + "");
				} else {
					data.add("--");
				}
			}
			psBall.setData(data);

			psBalls.add(psBall);

		}
		psData.setBalls(psBalls);

		return psData;
	}

	public static String getParScoareByBall(MatchDto match, int clubId) {

		int t2TargetBalls = 0;
		int t2BallsLeft = 0;

		int t1Score = match.getT1_1total();
		int t2BallsPlayed = match.getT2balls();
		int t2Wickets = match.getT2wickets();

		double parScore = 0.0;
		double t2TotalResLoss = 0.0;
		double t1ResAvailable = 100.0d;
		double t2ResAvailable = 0.0;
		double t2ResAvaiableSpc = 0.0;

		Map<Integer, Double> percentagesMap = null;
		try {
			percentagesMap = DLSCalculationFactory.getPercentagesFromDLSChart();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(e.getMessage() + " getParScoareByBall MatchID - " + match != null ? match.getMatchID() + ""
					: " Match not found " + " and ClubId " + clubId);
		}

		String matchType = "50";
		if (match.getOvers() <= 20) {
			matchType = "20";
		}

		double g50Value = ApplicationConstants.G_50_VALUE;

		if (match.isDls()) {
			if (match.getR1ResAvailable() > 0) {
				t1ResAvailable = match.getR1ResAvailable();
			}
			List<DLSInputData> intruptions = new ArrayList<DLSInputData>();

			List<MatchDLRecord> matchDLRecords2 = null;
			try {
				matchDLRecords2 = MatchDLRecordsFactory.getDLRecords(match.getMatchID(), 2, clubId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (matchDLRecords2 != null && matchDLRecords2.size() > 0) {
				for (MatchDLRecord rec : matchDLRecords2) {
					intruptions.add(CommonUtility.convertIntoDlsInputList(rec));
				}
				DLSInputData firstRecord = intruptions.get(0);
				t2TargetBalls = firstRecord.getBallsAtStartOfPlay();
			}
			if (t2TargetBalls == 0) {
				t2TargetBalls = CommonUtility.oversToBalls(match.getT2RevisedOvers() + "");
			}
			t2BallsLeft = CommonUtility.oversToBalls((match.getT2RevisedOvers() + "")) - t2BallsPlayed;
			try {
				t2TotalResLoss = CommonUtility.getT2TotalResLoss(clubId, match, intruptions);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error(e.getMessage() + " getParScoareByBall MatchID - " + match != null ? match.getMatchID() + ""
						: " Match not found " + " and ClubId " + clubId);
			}
		} else {
			t2TargetBalls = CommonUtility.oversToBalls(match.getOvers() + "");
			t2BallsLeft = CommonUtility.oversToBalls((match.getOvers() + "")) - t2BallsPlayed;
		}

		int ballStr = CommonUtility.stringToInt(t2TargetBalls + "0" + matchType);

		if (percentagesMap.get(ballStr) != null) {
			t2ResAvailable = percentagesMap.get(ballStr);
		}
		t2ResAvailable = t2ResAvailable - t2TotalResLoss;

		int ballsLeftRS = t2BallsLeft;
		int ballStr1 = CommonUtility.stringToInt(ballsLeftRS + "" + t2Wickets + "" + matchType);
		Double rs1 = 0.0;

		if (percentagesMap.get(ballStr1) != null) {
			rs1 = percentagesMap.get(ballStr1);
		}

		t2ResAvaiableSpc = t2ResAvailable - rs1;

		if (t2ResAvaiableSpc > t1ResAvailable) {
			parScore = t1Score + (g50Value * ((t2ResAvaiableSpc - t1ResAvailable) / 100.0d));
		} else if (t2ResAvaiableSpc < t1ResAvailable) {
			parScore = t1Score * (t2ResAvaiableSpc / t1ResAvailable);
		} else {
			parScore = t1Score;
		}
		return Math.round(parScore) + "";
	}

	public static List<BallBean> consolidateSessionRecords(List<BallDto> ballsDto, SessionPlayerDto sessionPlayerDto,
			int clubId) {
		return null;
	}

	public static List<BallBean> genrateCommentrySessionRecords(List<BallDto> ballsDto,
			SessionPlayerDto sessionPlayerDto, int clubId, int sessionId, Map<String, PlayerDto> playerMap,
			Map<String, String> playerNameMap) throws Exception {

		List<BallBean> balls = new ArrayList<BallBean>();

		for (BallDto b : ballsDto) {
			BallBean ballBean = ballToBallBeanSession(clubId, playerMap, playerNameMap, b);
			balls.add(ballBean);
		}

		return balls;
	}

	public static Map<String, Map<String, Object>> getPracticeSessionPlayerRecordBal(List<BallDto> ballsDto,
			SessionPlayerDto sessionPlayerDto, int clubId, int sessionId, Map<String, PlayerDto> playerMap,
			Map<String, String> playerMapName) throws Exception {

		List<Integer> yorker = new ArrayList<>(Arrays.asList(1, 2, 3));
		List<Integer> fullLength = new ArrayList<>(Arrays.asList(4, 5, 6));
		List<Integer> goodLength = new ArrayList<>(Arrays.asList(7, 8, 9));
		List<Integer> shortPitch = new ArrayList<>(Arrays.asList(10, 11, 12));
		List<Integer> veryShort = new ArrayList<>(Arrays.asList(13, 14, 15));

		Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
		List<BallBean> balls;

		for (BallDto b : ballsDto) {
			BallBean ballBean = ballToBallBeanSession(clubId, playerMap, playerMapName, b);

			Map<String, Object> pres = res.get(ballBean.getBowler() + "");
			if (pres == null) {
				pres = new HashMap<String, Object>();
				res.put(ballBean.getBowler() + "", pres);
				pres.put("wide", 0);
				pres.put("noBall", 0);
				pres.put("wicket", 0);
				pres.put("shot", 0);
				pres.put("dot", 0);
				pres.put("yorkers", 0);
				pres.put("fullLength", 0);
				pres.put("goodLength", 0);
				pres.put("short", 0);
				pres.put("veryshort", 0);
				pres.put("runs", 0);

			}

			balls = (List<BallBean>) pres.get("ballRecors");
			if (balls == null) {
				balls = new ArrayList<BallBean>();
				pres.put("ballRecors", balls);
			}
			pres.put("balls", CommonUtility.stringToInt(String.valueOf(pres.get("balls"))) + 1);
			pres.put("name", playerMapName.get(ballBean.getBowler() + ""));
			pres.put("profilePic", ballBean.getBowlerPic());
			pres.put("playingRole", ballBean.getBowlerPlayingRole());
			pres.put("runs", CommonUtility.stringToInt(String.valueOf(pres.get("runs"))) + ballBean.getRuns());

			if ("dot".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("dot", CommonUtility.stringToInt(String.valueOf(pres.get("dot"))) + 1);
			} else if ("shot".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("shot", CommonUtility.stringToInt(String.valueOf(pres.get("shot"))) + 1);
			} else if ("wicket".equalsIgnoreCase(ballBean.getBallType())
					&& !ballBean.getOutMethod().equalsIgnoreCase("ro")) {
				pres.put("wicket", CommonUtility.stringToInt(String.valueOf(pres.get("wicket"))) + 1);
			} else if ("noBall".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("noBall", CommonUtility.stringToInt(String.valueOf(pres.get("noBall"))) + 1);
			} else if ("wide".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("wide", CommonUtility.stringToInt(String.valueOf(pres.get("wide"))) + 1);
			}

			try {
				if (!CommonUtility.isNullOrEmptyOrNULL(ballBean.getPitchMap())
						&& ballBean.getPitchMap().split(";").length > 1) {
					String ballPitched = ballBean.getPitchMap().split(";")[0];
					int intString=0;
					try {
					 intString = Integer.parseInt(null != ballPitched ? ballPitched.split("-")[1]:"");
					}catch(Exception e) {
						continue;
					}
					if (yorker.contains(intString)) {
						pres.put("yorkers", CommonUtility.stringToInt(String.valueOf(pres.get("yorkers"))) + 1);
					} else if (fullLength.contains(intString)) {
						pres.put("fullLength", CommonUtility.stringToInt(String.valueOf(pres.get("fullLength"))) + 1);
					} else if (goodLength.contains(intString)) {
						pres.put("goodLength", CommonUtility.stringToInt(String.valueOf(pres.get("goodLength"))) + 1);
					} else if (shortPitch.contains(intString)) {
						pres.put("short", CommonUtility.stringToInt(String.valueOf(pres.get("short"))) + 1);
					} else if (veryShort.contains(intString)) {
						pres.put("veryshort", CommonUtility.stringToInt(String.valueOf(pres.get("veryshort"))) + 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			balls.add(ballBean);
		}

		return res;
	}

	public static Map<String, Map<String, Object>> getPracticeSessionPlayerFieldingRecords(List<BallDto> ballsDto,
			SessionPlayerDto sessionPlayerDto, int clubId, int sessionId, Map<String, PlayerDto> playerMap,
			Map<String, String> playerMapName) throws Exception {

		Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();

		List<BallBean> balls;

		for (BallDto b : ballsDto) {
			BallBean ballBean = ballToBallBeanSession(clubId, playerMap, playerMapName, b);

			Map<String, Object> pres = res.get(ballBean.getWicketTaker2() + "");
			if (pres == null) {
				pres = new HashMap<String, Object>();
				res.put(ballBean.getWicketTaker2() + "", pres);
				pres.put("catches", 0);
				pres.put("stumped", 0);
				pres.put("runout", 0);
				pres.put("wicketkeepercatches", 0);
			}

			balls = (List<BallBean>) pres.get("ballRecors");
			if (balls == null) {
				balls = new ArrayList<BallBean>();
				pres.put("ballRecors", balls);
			}
			pres.put("balls", CommonUtility.stringToInt(String.valueOf(pres.get("balls"))) + 1);
			pres.put("name", playerMapName.get(ballBean.getWicketTaker2() + ""));
			// pres.put("profilePic", ballBean.getBowlerPic());
			// pres.put("playingRole",ballBean.getBowlerPlayingRole());

			if ("ro".equalsIgnoreCase(ballBean.getOutMethod())) {
				pres.put("runout", CommonUtility.stringToInt(String.valueOf(pres.get("runout"))) + 1);
			} else if ("ct".equalsIgnoreCase(ballBean.getOutMethod())) {
				pres.put("catches", CommonUtility.stringToInt(String.valueOf(pres.get("catches"))) + 1);
			} else if ("ctw".equalsIgnoreCase(ballBean.getOutMethod())) {
				pres.put("wicketkeepercatches",
						CommonUtility.stringToInt(String.valueOf(pres.get("wicketkeepercatches"))) + 1);
			} else if ("st".equalsIgnoreCase(ballBean.getOutMethod())) {
				pres.put("stumped", CommonUtility.stringToInt(String.valueOf(pres.get("stumped"))) + 1);
			}

			balls.add(ballBean);
		}

		return res;
	}

	public static Map<String, Map<String, Object>> getPracticeSessionPlayerRecordBat(List<BallDto> ballsDto,
			SessionPlayerDto sessionPlayerDto, int clubId, int sessionId, Map<String, PlayerDto> playerMap,
			Map<String, String> playerMapName) throws Exception {

		Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();

		List<BallBean> balls;

		for (BallDto b : ballsDto) {
			BallBean ballBean = ballToBallBeanSession(clubId, playerMap, playerMapName, b);
			Map<String, Object> pres = res.get(ballBean.getStriker() + "");
			if (pres == null) {
				pres = new HashMap<String, Object>();
				res.put(ballBean.getStriker() + "", pres);
				pres.put("wide", 0);
				pres.put("noBall", 0);
				pres.put("wicket", 0);
				pres.put("shot", 0);
				pres.put("dot", 0);
				pres.put("runs", 0);
			}

			balls = (List<BallBean>) pres.get("ballRecors");
			if (balls == null) {
				balls = new ArrayList<BallBean>();
				pres.put("ballRecors", balls);
			}
			pres.put("balls", CommonUtility.stringToInt(String.valueOf(pres.get("balls"))) + 1);
			pres.put("name", playerMapName.get(ballBean.getStriker() + ""));
			pres.put("profilePic", ballBean.getStrikerPic());
			pres.put("playingRole", ballBean.getStrikerPlayingRole());
			pres.put("runs", CommonUtility.stringToInt(String.valueOf(pres.get("runs"))) + ballBean.getRuns());

			if ("dot".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("dot", CommonUtility.stringToInt(String.valueOf(pres.get("dot"))) + 1);
			} else if ("shot".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("shot", CommonUtility.stringToInt(String.valueOf(pres.get("shot"))) + 1);
			} else if ("wicket".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("wicket", CommonUtility.stringToInt(String.valueOf(pres.get("wicket"))) + 1);
			} else if ("noBall".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("noBall", CommonUtility.stringToInt(String.valueOf(pres.get("noBall"))) + 1);
			} else if ("wide".equalsIgnoreCase(ballBean.getBallType())) {
				pres.put("wide", CommonUtility.stringToInt(String.valueOf(pres.get("wide"))) + 1);
			}
			balls.add(ballBean);
		}

		return res;
	}

	private static BallBean ballToBallBeanSession(int clubId, Map<String, PlayerDto> playersMap,
			Map<String, String> playerNameMap, BallDto ball) {

		BallBean ballBean = new BallBean();
		ballBean.setBallId(ball.getBallId());
		ballBean.setOver(ball.getOver());
		ballBean.setBall(ball.getBall());
		ballBean.setRuns(ball.getRuns());
		ballBean.setBowler(ball.getBowler());
		ballBean.setStriker(ball.getBatsman());
		ballBean.setNonStriker(ball.getRunner());
		ballBean.setRunsDisplay(ball.getRunsDisplay());
		ballBean.setBallType(ball.getBallType());
		ballBean.setOutMethod(ball.getOutMethod());
		ballBean.setWicketTaker2(ball.getWicketTaker2());

		PlayerDto bowler = playersMap.get(ball.getBowler() + "");
		if(bowler!=null) {
		ballBean.setBowlerName(
				CommonLogicFB.getTrimedName(bowler.getFullName(), 13) + CommonLogicFB.getJerseyNumberString(bowler));
		ballBean.setBowlerPic(bowler.getProfilepic_file_path());
		ballBean.setBowlerPlayingRole(bowler.getPlayingRole());
		}

		PlayerDto batsman = playersMap.get(ball.getBatsman() + "");
		if(batsman!=null) {
		ballBean.setStrikerName(
				CommonLogicFB.getTrimedName(CommonUtility.isNullOrEmpty(batsman.getFullName()) ? batsman.getFullName():"", 13) + CommonLogicFB.getJerseyNumberString(batsman));
		ballBean.setStrikerPic(batsman.getProfilepic_file_path());
		ballBean.setStrikerPlayingRole(batsman.getPlayingRole());
		}
		if (ball.getRunner() > 0) {
			PlayerDto runner = playersMap.get(ball.getRunner() + "");
			if(runner!=null) {
			ballBean.setNonStrikerName(
					CommonLogicFB.getTrimedName(runner.getFullName(), 13) + CommonLogicFB.getJerseyNumberString(runner));
			ballBean.setNonStrikerPic(runner.getProfilepic_file_path());
			ballBean.setNonStrikerPlayingRole(runner.getPlayingRole());
			}
		}
		String commentary = "";
		commentary = ball.getBallInfoDisplayPracticeSession(playerNameMap, ball);

		ballBean.setCommentary(commentary);
		ballBean.setPitchMap(ball.getPitchMap());
		ballBean.setDirection(ball.getDirection());
		return ballBean;
	}

	public static void populateDataForUI(ScoreCardBean fullScorecard) {

		if (fullScorecard != null) {

			populateBattingStats(fullScorecard.getTeam1Batting());
			populateBattingStats(fullScorecard.getTeam2Batting());
			populateBattingStats(fullScorecard.getTeam1_2Batting());
			populateBattingStats(fullScorecard.getTeam2_2Batting());

			populateBowlingStats(fullScorecard.getTeam1Bowling());
			populateBowlingStats(fullScorecard.getTeam2Bowling());
			populateBowlingStats(fullScorecard.getTeam1_2Bowling());
			populateBowlingStats(fullScorecard.getTeam2_2Bowling());

			MatchDto match = fullScorecard.getMatchInfo();
			if (match != null) {
				match.setRunsEquation(match.getRunsNeededEquation());
				match.setRunRate(match.getRunRateForMatch());
			}

		}

	}

	private static void populateBowlingStats(List<BowlingDto> bowlings) {

		if (bowlings != null && !bowlings.isEmpty()) {
			for (BowlingDto bl : bowlings) {
				bl.setPlayerEconRate(bl.getEconomyRate());
			}
		}

	}

	private static void populateBattingStats(List<BattingDto> battings) {
		if (battings != null && !battings.isEmpty()) {

			for (BattingDto bt : battings) {
				bt.setPlayerStrikeRate(bt.getStrikeRate());
			}

		}
	}
	
	public static void sendFixtureUpdateChanges(List<FixtureDto> updatedFixtures,ClubDtoLite club, String leagueName) throws Exception {
        
        List<Integer> umpireScorerIdList = new ArrayList<Integer>();
        for(FixtureDto fixture : updatedFixtures) {
            if(!umpireScorerIdList.contains(fixture.getUmpire1Id()) && fixture.getUmpire1Id() != 0) {
                umpireScorerIdList.add(fixture.getUmpire1Id());
            }
            if(!umpireScorerIdList.contains(fixture.getUmpire2Id()) && fixture.getUmpire2Id() != 0){
                umpireScorerIdList.add(fixture.getUmpire2Id());
            }
            if(!umpireScorerIdList.contains(fixture.getScorerId()) && fixture.getScorerId() != 0){
                umpireScorerIdList.add(fixture.getScorerId());
            }
        }
        for(int userId: umpireScorerIdList) {
            List<FixtureDto> emailFixtures = new ArrayList<FixtureDto>();
            List<FixtureDto> emailScorerFixtures = new ArrayList<FixtureDto>();
            for(FixtureDto fixture : updatedFixtures) {
                if(userId == fixture.getUmpire1Id() || userId == fixture.getUmpire2Id()) {
                    emailFixtures.add(fixture);
                }
                if(userId == fixture.getScorerId()) {
                    emailScorerFixtures.add(fixture);
                }
            }
            sendEmailToUmpire(emailFixtures, emailScorerFixtures, userId, club, leagueName);
        }
    }
	public static void sendFixturesEmails(List<FixtureDto> updatedFixtures,ClubDtoLite club,String leagueName) throws Exception {
		for(FixtureDto fixture:updatedFixtures) {
            checkForEmptyValues(fixture);
        }
		
		 Map<String,String> emailsMap= new HashMap<>();
		 emailsMap= UserFactory.getUmpireAdministratorsEmailsMap(club.getClubId());
		 
			if (!CommonUtility.isListNullEmpty(updatedFixtures)) {
				for (FixtureDto formFixture : updatedFixtures) {
					FixtureDto dbFixture=new FixtureDto();
					
					if (0 != formFixture.getFixtureId()) {
						dbFixture = FixturesFactory.getFixture(formFixture.getFixtureId(), club.getClubId());
						setUpdatedFlag(club, emailsMap, formFixture, dbFixture);
						setformFixture(club, formFixture, dbFixture,emailsMap,"Update");
					}else {
					setformFixture(club, formFixture, dbFixture,emailsMap,"Create");
					}
					if(formFixture.getDate().equals("TBD")) {
						formFixture.setDate(null);
					}

				}
				NotificationHelper.sendFixturesEmails(updatedFixtures, emailsMap, club, leagueName);
			}
		}

	private static void setformFixture(ClubDtoLite club, FixtureDto formFixture, FixtureDto dbFixture, Map<String,String> emailsMap,String type)
			throws Exception {
		List<GroundDto> grounds = GroundFactory.getGrounds(club.getClubId());
		for (GroundDto ground : grounds) {
			if (formFixture.getGroundId() == ground.getGroundId()) {
				formFixture.setLocation(ground.getName());
			} 

		}

		
		
		setUmpireScorer(club, formFixture, dbFixture,emailsMap,type);
		TeamDto team1 = null;
		if(formFixture.getTeamOne()!=0) {
			 team1 = TeamFactory.getTeamByTeamId(formFixture.getTeamOne(), club.getClubId());
		}
		
		
		if(team1!=null) {
			formFixture.setTeamOneName(team1.getTeamName());
			formFixture.setLeagueName(team1.getLeagueName());
			if("Create".equalsIgnoreCase(type) || ("Update".equalsIgnoreCase(type) && (formFixture.isTeam1Updated() || formFixture.isTeam2Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
			{
			  emailsMap.put(team1.getCaptainEmail(), team1.getCaptainName());
			  emailsMap.put(team1.getViceCaptainEmail(), team1.getViceCaptainName());
			 
			}
		}
			
		
		TeamDto team2 =null;
		if(formFixture.getTeamTwo()!=0) {
			 team2 = TeamFactory.getTeamByTeamId(formFixture.getTeamTwo(), club.getClubId());
		}
		
			if(team2!=null) {
			
			formFixture.setTeamTwoName(team2.getTeamName());
			formFixture.setLeagueName(team2.getLeagueName());
			if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isTeam2Updated() || formFixture.isTeam1Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
			{
			  emailsMap.put(team2.getCaptainEmail(), team2.getCaptainName());
			  emailsMap.put(team2.getViceCaptainEmail(), team2.getViceCaptainName());
			}
		
		}
		
		
		if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && formFixture.isTeam1Updated()))
		{
		if(dbFixture.getTeamOne()!=0) {
		if(formFixture.getTeamOne()!=dbFixture.getTeamOne()) {
			TeamDto dbTeam1 = TeamFactory.getTeamByTeamId(dbFixture.getTeamOne(), club.getClubId());
			if(dbTeam1!=null) {
				

				  emailsMap.put(dbTeam1.getCaptainEmail(), dbTeam1.getCaptainName());
				  emailsMap.put(dbTeam1.getViceCaptainEmail(), dbTeam1.getViceCaptainName());
				 
				}
		}
		}
		}
		if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && formFixture.isTeam2Updated()))
		{
		if(dbFixture.getTeamTwo()!=0) {
		if(formFixture.getTeamTwo()!=dbFixture.getTeamTwo()) {
			TeamDto dbTeam2 = TeamFactory.getTeamByTeamId(dbFixture.getTeamTwo(), club.getClubId());
			if(dbTeam2!=null) {
				 

				  emailsMap.put(dbTeam2.getCaptainEmail(), dbTeam2.getCaptainName());
				  emailsMap.put(dbTeam2.getViceCaptainEmail(), dbTeam2.getViceCaptainName());
				 
				}
		}
		}
		}
	}

	private static void setUmpireScorer(ClubDtoLite club, FixtureDto formFixture, FixtureDto dbFixture,
			Map<String, String> emailsMap,String type) throws Exception {
		
		if (formFixture.getUmpire1Id() != 0) {
			if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire1Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
			{
			if (dbFixture.getUmpire1Id() != 0 && formFixture.getUmpire1Id() != dbFixture.getUmpire1Id()) {

				UserDto user = UserFactory.getUserById(dbFixture.getUmpire1Id(), club.getClubId());
				if (user != null) {

					emailsMap.put(user.getEmail(), user.getFullName());
				}
			}
			}
			UserDto user = UserFactory.getUserById(formFixture.getUmpire1Id(), club.getClubId());
			if (user != null) {
				formFixture.setUmpire1Name(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire1Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}

		} else if (dbFixture.getUmpire1Id() != 0) {
			UserDto user = UserFactory.getUserById(dbFixture.getUmpire1Id(), club.getClubId());
			if (user != null) {
				//formFixture.setUmpire1Name(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire1Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}
		}
		
		
		if (formFixture.getUmpire2Id() != 0) {
			if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire2Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
			{
			if (dbFixture.getUmpire2Id() != 0 && formFixture.getUmpire2Id() != dbFixture.getUmpire2Id()) {
				UserDto user = UserFactory.getUserById(dbFixture.getUmpire2Id(), club.getClubId());
				if (user != null) {

					emailsMap.put(user.getEmail(), user.getFullName());
				}

			}
			}
			UserDto user = UserFactory.getUserById(formFixture.getUmpire2Id(), club.getClubId());
			if (user != null) {
				formFixture.setUmpire2Name(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire2Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}

		} else if (dbFixture.getUmpire2Id() != 0) {
			UserDto user = UserFactory.getUserById(dbFixture.getUmpire2Id(), club.getClubId());
			if (user != null) {
				//formFixture.setUmpire2Name(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isUmpire2Updated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}
		}
		
	
		if (formFixture.getScorerId() != 0) {
			if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isScorerUpdated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
			{
			if (dbFixture.getScorerId() != 0 && formFixture.getScorerId() != dbFixture.getScorerId()) {
				UserDto user = UserFactory.getUserById(dbFixture.getScorerId(), club.getClubId());
				if (user != null) {

					emailsMap.put(user.getEmail(), user.getFullName());
				}
			}
			}
			UserDto user = UserFactory.getUserById(formFixture.getScorerId(), club.getClubId());
			if (user != null) {
				formFixture.setScorerName(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isScorerUpdated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}

		} else if (dbFixture.getScorerId() != 0) {
			UserDto user = UserFactory.getUserById(dbFixture.getScorerId(), club.getClubId());
			if (user != null) {
				//formFixture.setScorerName(user.getFullName());
				if("Create".equalsIgnoreCase(type)|| ("Update".equalsIgnoreCase(type) && (formFixture.isScorerUpdated() || formFixture.isDateUpdated() || formFixture.isTimeUpdated() || formFixture.isGroundUpdated())))
				{
				emailsMap.put(user.getEmail(), user.getFullName());
				}
			}
		}
		
	}

	private static void setUpdatedFlag(ClubDtoLite club, Map<String, String> emailsMap, FixtureDto formFixture,
			FixtureDto dbFixture) throws Exception {
		if ((formFixture.getTeamOne() != dbFixture.getTeamOne())) {
			formFixture.setTeam1Updated(true);
		}
		if (formFixture.getTeamTwo() != dbFixture.getTeamTwo()) {
			formFixture.setTeam2Updated(true);
		}
		if (!(formFixture.getDate().equals((CommonUtility.isNullOrEmptyOrNULL(dbFixture.getDate())?"TBD":dbFixture.getDate())))) {
			formFixture.setDateUpdated(true);
		}
		if (!(formFixture.getTime().equals(dbFixture.getTime()))) {
			formFixture.setTimeUpdated(true);
		}if (formFixture.getGroundId()!=dbFixture.getGroundId()) {
			formFixture.setGroundUpdated(true);
		}
		if (formFixture.getUmpire1Id() != dbFixture.getUmpire1Id()) {
			formFixture.setUmpire1Updated(true);
		}
		if (formFixture.getUmpire2Id() != dbFixture.getUmpire2Id()) {
			formFixture.setUmpire2Updated(true);
		}
		if (formFixture.getScorerId() != dbFixture.getScorerId()) {
			formFixture.setScorerUpdated(true);
		}
	}
	
	
    private static void sendEmailToUmpire(List<FixtureDto> emailFixtures, List<FixtureDto> emailScorerFixtures, int userId, ClubDtoLite club, String leagueName) throws Exception {
		
			for (FixtureDto fixture : emailFixtures) {
				checkForEmptyValues(fixture);
			}
		
		
			for (FixtureDto fixture : emailScorerFixtures) {
				checkForEmptyValues(fixture);
			}
		
        UserDto user = UserFactory.getUserById(userId,  club.getClubId());
        
        if(user != null && !CommonUtility.isNullOrEmpty(user.getEmail())) {
            NotificationHelper.sendBulkFixtureAssignmentToUmpire(emailFixtures,emailScorerFixtures, user.getFullName() ,club,user.getEmail(),leagueName);
        }
        
    }

    private static void checkForEmptyValues(FixtureDto fixture) {
        if(CommonUtility.isNullOrEmpty(fixture.getDate())) {
            fixture.setDate("TBD");
        }if(CommonUtility.isNullOrEmpty(fixture.getLocation())) {
            fixture.setLocation("TBD");
        }if(CommonUtility.isNullOrEmpty(fixture.getTeamOneName())) {
            fixture.setTeamOneName("TBD");
        }if(CommonUtility.isNullOrEmpty(fixture.getTeamTwoName())) {
            fixture.setTeamTwoName("TBD");
        }if(CommonUtility.isNullOrEmpty(fixture.getTime())) {
            fixture.setTime("TBD");
        }
    }

}
