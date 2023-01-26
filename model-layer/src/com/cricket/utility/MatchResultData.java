package com.cricket.utility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dto.MatchDto;

public class MatchResultData {
//	private static String EXTRACT_HEADERS = "match_date,scheduledMatch,matchType,matchResult,winningTeam,manOfTheMatch";
	public static void main(String[] args) throws IOException, SQLException {
		int clubId = 10304;
		String winningTeam = "";
		try {
			List<MatchDto> matches = MatchesFactory.getMatchesByLeague(11, clubId, null);
			for (MatchDto dto : matches) {
				String match_date = dto.getMatchDate();
				String scheduledMatch = dto.getTeamOneName() + " v/s " + dto.getTeamTwoName();
				String matchType = dto.getMatchType();
				String matchResult = dto.getResult();
				String teamOneName = dto.getTeamOneName();
				String teamTwoName = dto.getTeamTwoName();
				int teamOneScores = dto.getT1total();
				int teamTwoScores = dto.getT2total();
				String scoreSummary = dto.getTeamOneName() + " Team: " + dto.getT1total()+"/"+dto.getT1wickets()+"("+dto.getT1balls()+")" +"\t"+
						dto.getTeamTwoName() + " Team: " + dto.getT2total()+"/"+dto.getT2wickets()+"("+dto.getT2balls()+")";
				if (dto.getTeamOne() == dto.getWinner()) {
					winningTeam = dto.getTeamOneName();
				} else {
					winningTeam = dto.getTeamTwoName();
				}
				System.out.println(
						matchType  + "," + match_date + "," + teamOneName+","
				+  teamTwoName+","+matchResult + "," + PlayerFactory.getPlayerById(dto.getManOfTheMatch(), clubId).getFullName()+","
				+teamOneScores+"," +teamTwoScores+"," +scoreSummary);
			}
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
		}
	}
}
