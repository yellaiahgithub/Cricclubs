package com.cricket.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.BallDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.MatchDto;

public class LeagueMatchStatsReport {
	
	public static void main(String[] args) {
		
		File file = new File("C:\\output\\match_stats_report.csv");
		
		int clubId = 1512;
		List<MatchDto> matches = new ArrayList<MatchDto>();
		List<FixtureDto> fixtures = new ArrayList<FixtureDto>();
		List<GroundDto> grounds = new ArrayList<GroundDto>();
		
		try {
			
			ClubDto club = ClubFactory.getClub(clubId);
			
			Map<Integer,String> playerIdNameMap = PlayerFactory.getAllPlayerIdNameMapOfLeague(clubId);
			Map<Integer,String> userIdNameMap = UserFactory.getUserIdNameMap(clubId);
			grounds = GroundFactory.getGrounds(clubId);
			Map<Integer,Integer> matchUmpire1IdMap = new HashMap<Integer, Integer>();
			Map<Integer,Integer> matchUmpire2IdMap = new HashMap<Integer, Integer>();
			Map<Integer,String> groundIdNameMap = new HashMap<Integer, String>();
			Map<Integer,Integer> matchGroundIdMap = new HashMap<Integer, Integer>();
			
			matches = MatchesFactory.getAllMatches(0, clubId);
			fixtures = FixturesFactory.getAllFixtures(clubId);
			
			for(GroundDto ground: grounds) {
				groundIdNameMap.put(ground.getGroundId(), ground.getName());
			}
			
			for(FixtureDto fixture: fixtures) {
				if(fixture.getMatchID()>0 && fixture.getUmpire1Id()>0) {
					matchUmpire1IdMap.put(fixture.getMatchID(), fixture.getUmpire1Id());
				}
				if(fixture.getMatchID()>0 && fixture.getUmpire2Id()>0) {
					matchUmpire2IdMap.put(fixture.getMatchID(), fixture.getUmpire2Id());
				}
				if(fixture.getMatchID()>0 && fixture.getGroundId()>0) {
					matchGroundIdMap.put(fixture.getMatchID(), fixture.getGroundId());
				}
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bw.write("Match Date, Series, Match Number,  Match Type, Team One, Team Two, Result, Scores Summary T1, Scores Summary T2, Toss, Man of the Match, Location, T1 Points Earned, T2 Points Earned, 1st Innings Start, 1st Innings End, Innings Break, 2nd Innings Start, 2nd Innings End, Umpire1, Umpire2, Updated By");
			bw.write("\n");
			
			for (MatchDto match : matches) {
				if(match.getLeagueId() >= 5 && match.getLeagueId() <=20) {
					matchStatsReport(club, match, bw, playerIdNameMap, userIdNameMap, matchUmpire1IdMap, matchUmpire2IdMap, groundIdNameMap, matchGroundIdMap);	
				}
			}
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void matchStatsReport(ClubDto club, MatchDto match, BufferedWriter bw,  Map<Integer,String> playerIdNameMap, 
			Map<Integer,String> userIdNameMap, Map<Integer,Integer> matchUmpire1IdMap, Map<Integer,Integer> matchUmpire2IdMap, 
			Map<Integer, String> groundIdNameMap, Map<Integer, Integer> matchGroundIdMap ) {

		
		try {
			bw.write("\""+match.getMatchDate() +"\",");
			bw.write("\""+match.getSeriesName() +"\",");
			bw.write("\""+match.getMatchID() +"\",");
			bw.write("\""+match.getMatchType() +"\",");
			bw.write("\""+match.getTeamOneName() +"\",");
			bw.write("\""+match.getTeamTwoName() +"\",");
			bw.write("\""+match.getResult() +"\",");
			bw.write("\""+match.getT1total()+"|"+match.getT1wickets() +"\",");
			bw.write("\""+match.getT2total()+"|"+match.getT2wickets() +"\",");
			
			if(match.getTossWon()>0) {
				if(match.getTossWon() == match.getTeamOne()) {
					bw.write("\""+match.getTeamOneName() +"\",");
				}else {
					bw.write("\""+match.getTeamTwoName() +"\",");
				}
			}else {
				bw.write("\""+" " +"\",");
			}
			
			if(match.getManOfTheMatch()>0) {
				bw.write("\""+playerIdNameMap.get(match.getManOfTheMatch()) +"\",");
			}else {
				bw.write("\""+" " +"\",");
			}
			int groundId = 0;
			if(matchGroundIdMap.containsKey(match.getMatchID())) {
				groundId = matchGroundIdMap.get(match.getMatchID());
			}
			if(groundId>0) {
				bw.write("\""+groundIdNameMap.get(groundId) +"\",");
			}else {
				bw.write("\""+" " +"\",");
			}
			LeagueDto series = new LeagueDto();
			try {
				series = club.getLeague(match.getLeagueId());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			bw.write("\""+match.getTeam1Points(series) +"\",");
			bw.write("\""+match.getTeam2Points(series) +"\",");
			
			int secondInningsIndex = 0;
			int index = 0;
			int validBallIndex = 0;
			String firstInnStart = " ";
			String firstInnEnd =" ";
			long breakDiffMin = 0;
			String secondInnStart = " ";
			String secondInnEnd = " ";
			
			try {
				List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(match.getMatchID(), club.getClubId());
				
				if (balls != null && match != null && !balls.isEmpty()) {

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
						firstInnEnd = CommonUtility.dateToString(balls.get(secondInningsIndex - 1).getCreateDate(),
								"HH:mm", club.getTimeZone());
						long firstDiff = (balls.get(secondInningsIndex - 1).getCreateDate().getTime())
								- (balls.get(validBallIndex).getCreateDate().getTime());
						long firtsDiffMin = (firstDiff / 1000) / 60;

						secondInnStart = CommonUtility.dateToString(balls.get(secondInningsIndex).getCreateDate(),
								"HH:mm", club.getTimeZone());
						long breakDiff = (balls.get(secondInningsIndex).getCreateDate().getTime())
								- (balls.get(secondInningsIndex - 1).getCreateDate().getTime());
						breakDiffMin = (breakDiff / 1000) / 60;		
						secondInnEnd = CommonUtility.dateToString(balls.get(balls.size() - 1).getCreateDate(),
								"HH:mm", club.getTimeZone());
					}
				
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			bw.write("\""+firstInnStart +"\",");
			bw.write("\""+firstInnEnd +"\",");
			if(breakDiffMin>0) {
				bw.write("\""+breakDiffMin+" mins" +"\",");
			}else {
				bw.write("\""+" " +"\",");
			}
			bw.write("\""+secondInnStart +"\",");
			bw.write("\""+secondInnEnd+"\",");
			
			int umpire1Id = 0;
			if(matchUmpire1IdMap.containsKey(match.getMatchID())) {
				umpire1Id = matchUmpire1IdMap.get(match.getMatchID());
			}
			if(umpire1Id>0) {
				bw.write("\""+userIdNameMap.get(umpire1Id) +"\",");
			}else {
				bw.write("\""+" "+"\",");
			}
			int umpire2Id = 0;
			if(matchUmpire2IdMap.containsKey(match.getMatchID())) {
				umpire2Id = matchUmpire2IdMap.get(match.getMatchID());
			}
			if(umpire2Id>0) {
				bw.write("\""+userIdNameMap.get(umpire2Id) +"\",");
			}else {
				bw.write("\""+" "+"\",");
			}
			if(match.getLastUpdatedBy()>0) {
				bw.write("\""+userIdNameMap.get(match.getLastUpdatedBy()) +"\"");
			}else {
				bw.write("\""+" " +"\"");
			}
			bw.write("\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("match Report : "+ match.getMatchID());
		
	}
}
