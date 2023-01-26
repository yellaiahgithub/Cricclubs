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

public class UpdatePlayerStatsForMatch {
	
	public static void main(String[] args) throws Exception {		
		int clubId = 501;
		int matchId = 3501;
		MatchDto match = MatchesFactory.getMatch(matchId, clubId);
		if(match != null) {
			populateStatsForMatch(clubId, match);	
		}
	}
	public static void populateStatsForMatch(int clubId, MatchDto match) throws InterruptedException {
		int matchId = match.getMatchID();
		try {			
			System.out.println("Starting: " + clubId + ":" + matchId);
			List<BattingDto> team1Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
					match.getBattingFirst(), clubId);;
			List<BattingDto> team2Batting = PlayerStatisticsFactory.getPlayersBattingByMatchIdTeamId(match.getMatchID(),
					(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(), clubId);;
			List<BowlingDto> team1Bowling = PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(match.getMatchID(),
					match.getBattingFirst(), clubId);;
			List<BowlingDto> team2Bowling = PlayerStatisticsFactory.getPlayersBowlingByMatchIdTeamId(match.getMatchID(),
					(match.getTeamOne() == match.getBattingFirst()) ? match.getTeamTwo() : match.getTeamOne(), clubId);;

			List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos = PlayerStatisticsSummaryGenrator
					.getPlayerStatisticSummaryDtos(team1Batting, team2Batting, team1Bowling, team2Bowling, match,
							clubId);
			PlayerStatisticsFactory.deletePlayerSumaryForMatch(clubId, matchId);			
			// PlayerStatisticsFactory.deleteAllPlayerSumary(match.getMatchID(), clubId);
			PlayerStatisticsFactory.saveAllPlayerSumary(playerStatisticsSummaryDtos, clubId);

			// Top performer update to Match Stats Summary (Remove and Add)
			TopPerformersSummaryFactory.deleteTopPerformersSummary(matchId, clubId);
			TopPerformersSummaryFactory.saveTopPerformersSummary(matchId, clubId, 1);

			System.out.println("Finishing: " + clubId + ":" + matchId);
			
		} catch (Exception e) {			
			System.out.println("Exception for club - " + clubId + "-" + matchId);
			System.out.println(e.getMessage());			
		}
	}
}
