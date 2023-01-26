package com.cricket.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dto.BallDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.PartnershipDto;
import com.cricket.dto.PlayerDto;

public class PartnershipData {

	public static void main(String[] args) {
		int clubId = 13309;
		String winningTeam = "";
		int[] arrayMatches = { 35, 34, 32, 31, 29, 27, 26, 25, 24, 23 };
		try {
			List<MatchDto> matches = new ArrayList<MatchDto>();
			List<MatchDto> totalMatches = new ArrayList<MatchDto>();
			for (int i = 0; i < arrayMatches.length; i++) {
				matches = MatchesFactory.getMatchesByLeague(arrayMatches[i], clubId, null);
				totalMatches.addAll(matches);
			}
			for (MatchDto dto : totalMatches) {

				List<BallDto> balls = ScoringFactory.getAllBallsOfMatch(dto.getMatchID(), clubId);
				Map<String, Object> dataMap = CommonLogic.consolidateMatchRecords(balls, dto);

				List<PartnershipDto> partList = new ArrayList<PartnershipDto>();

				List<PlayerDto> t1Players = TeamFactory.getTeamPlayersList(dto.getTeamOne(), clubId);
				List<PlayerDto> t2Players = TeamFactory.getTeamPlayersList(dto.getTeamTwo(), clubId);

				Map<Integer, String> playerIdNameMap = new HashMap<Integer, String>();

				List<Integer> t1PlayerIds = new ArrayList<Integer>();
				List<Integer> t2PlayerIds = new ArrayList<Integer>();

				for (PlayerDto pdto : t1Players) {
					t1PlayerIds.add(pdto.getPlayerID());
					playerIdNameMap.put(pdto.getPlayerID(), pdto.getFullName());
				}
				for (PlayerDto pdto : t2Players) {
					t2PlayerIds.add(pdto.getPlayerID());
					playerIdNameMap.put(pdto.getPlayerID(), pdto.getFullName());
				}

				Map<String, List<PartnershipDto>> partnershipMap = (Map<String, List<PartnershipDto>>) dataMap
						.get("partnershipMap");

				for (String str : partnershipMap.keySet()) {
					partList.addAll(partnershipMap.get(str));
				}

				for (PartnershipDto pdto : partList) {
					if (pdto.getPartnershipTotalRuns() >= 100) {
						int player1Id = pdto.getOtherPlayerId();
						int player2Id = pdto.getOutPlayerId();
						String player1TeamName = "";
						String player2TeamName = "";
						String player1Name = playerIdNameMap.get(player1Id);
						String player2Name = playerIdNameMap.get(player2Id);

						if (t1PlayerIds.contains(player1Id)) {
							player1TeamName = dto.getTeamOneName();
						} else if (t2PlayerIds.contains(player1Id)) {
							player1TeamName = dto.getTeamTwoName();
						}

						if (t1PlayerIds.contains(player2Id)) {
							player2TeamName = dto.getTeamOneName();
						} else if (t2PlayerIds.contains(player2Id)) {
							player2TeamName = dto.getTeamTwoName();
						}
						String location = "";
						FixtureDto fixture = FixturesFactory.getFixtureForMatch(dto.getMatchID(),13309);
						if (fixture != null) {
							location = fixture.getLocation();

							if (fixture.getGroundId() != 0) {
								GroundDto ground = GroundFactory.getGround(fixture.getGroundId(), 13309);
								location = (ground != null) ? ground.getName() : location;
							}
						} 
						
						System.out.println(dto.getSeriesName()+", "+dto.getLeagueId()+
								" , "+dto.getMatchID()+
								" ,  "+dto.getTeamOneName()+" ,  "+dto.getTeamTwoName()+
								",  "+location+ " , "+pdto.getPartnershipTotalRuns()+
								" ,  "+player1Name+" ,  "+pdto.getOtherPlayerScore()
								+" ,  "+player2Name+",  "+pdto.getOutPlayerScore());
					}
				}
			}
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
		}
	}
}
