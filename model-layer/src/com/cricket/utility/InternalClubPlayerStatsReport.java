package com.cricket.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.cricket.dao.PlayerStatisticsFactory;
import com.cricket.dto.PlayerBatBowlStatsDto;

public class InternalClubPlayerStatsReport {
	
	public static void main(String[] args) {
		
		File file = new File("C:\\output\\internal_club_player_stats_report.csv");
		
		int clubId = 1809;
		int internalClubId = 10;
		
		try {
			
			List<PlayerBatBowlStatsDto> players = PlayerStatisticsFactory.getInternalClubPlayersBatBowlStats(clubId, internalClubId);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bw.write("Player Id, Player Name, Series Name, Matches, Batting Innings, Not Outs, Runs Scored, Balls Faced, Batting Average, Batting SR, HS, 100s, 50s, Fours, Sixers,  Bowling Innings, Overs, Wickets, Bowling Average, Bowling Economy, Bowling SR, 5W");
			bw.write("\n");
			
			for (PlayerBatBowlStatsDto player : players) {
				playerStatsReport(player, bw);	
			}
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void playerStatsReport(PlayerBatBowlStatsDto player, BufferedWriter bw) {

		
		try {
			bw.write("\""+player.getPlayerId() +"\",");
			bw.write("\""+player.getPlayerName() +"\",");
			bw.write("\""+player.getSeriesName() +"\",");
			bw.write("\""+player.getMatches() +"\",");
			bw.write("\""+player.getInningsBatting() +"\",");
			bw.write("\""+player.getNotOuts() +"\",");
			bw.write("\""+player.getRunsScored() +"\",");
			bw.write("\""+player.getBallsFaced() +"\",");
			bw.write("\""+player.getBattingAverage() +"\",");
			bw.write("\""+player.getBattingStrikeRate() +"\",");
			bw.write("\""+player.getHighestScore() +"\",");
			bw.write("\""+player.getHundreds() +"\",");
			bw.write("\""+player.getFifties() +"\",");
			bw.write("\""+player.getFours() +"\",");
			bw.write("\""+player.getSixers() +"\",");
			bw.write("\""+player.getInningsBowling() +"\",");
			bw.write("\""+CommonUtility.ballsToOvers(player.getBallsBowled()) +"\",");			
			bw.write("\""+player.getWickets() +"\",");
			bw.write("\""+player.getBowlingAverage() +"\",");
			bw.write("\""+player.getBowlingEconomy(6) +"\",");
			bw.write("\""+player.getBowlingStrikeRate() +"\",");			
			bw.write("\""+player.getFiveWickets() +"\"");
			
			bw.write("\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
