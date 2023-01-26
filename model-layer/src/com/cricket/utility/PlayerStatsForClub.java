package com.cricket.utility;

import java.util.List;

//import com.cricket.dao.CareerStatsSummaryFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerStatisticsFactory;
import com.cricket.dao.TopPerformersSummaryFactory;
import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.statistics.PlayerStatisticSummaryDto;

public class PlayerStatsForClub {
	
	public static void main(String[] args) throws Exception {		
		int clubId = 14364;			
		populateStatsForPlayer(clubId);			
	}
	public static void populateStatsForPlayer(int clubId) throws InterruptedException {
		int matchId = 0;
		try {
			List<MatchDto> matches = MatchesFactory.getAllMatchs(clubId);			
			PlayerStatisticsFactory.deleteAllPlayerSumary(clubId);
			int totalMatch = matches.size();
			System.out.println("Starting: "+clubId+ ":" + totalMatch);
			for (MatchDto match : matches) {
				System.out.println(clubId+ "-"+match.getMatchID());
				matchId = match.getMatchID();
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
				//PlayerStatisticsFactory.deleteAllPlayerSumary(match.getMatchID(), clubId);
				PlayerStatisticsFactory.saveAllPlayerSumary(playerStatisticsSummaryDtos, clubId);
				
				//Top performer update to Match Stats Summary (Remove and Add)
				TopPerformersSummaryFactory.deleteTopPerformersSummary(matchId, clubId); 
				TopPerformersSummaryFactory.saveTopPerformersSummary(matchId, clubId, 1);
				
				//Also call the career stats for all players of the match
				//CareerStatsSummaryFactory.updateCareerStatsSummary(match, clubId);
							
			}
			System.out.println("Finishing: "+clubId+ ":" + totalMatch);
		} catch (Exception e) {
			
			System.out.println("Exception for club - " + clubId + "-" + matchId);
			System.out.println(e.getMessage());			
		}
	}
}
