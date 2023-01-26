package com.cricket.utility;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cricket.dto.BattingDto;
import com.cricket.dto.BowlingDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.statistics.PlayerStatisticSummaryDto;

public class PlayerStatisticsSummaryGenrator {
	
	public static List<PlayerStatisticSummaryDto> getPlayerStatisticSummaryDtos(List<BattingDto> battingRecords1, List<BattingDto> battingRecords2, List<BowlingDto> bowlingRecords1, List<BowlingDto> bowlingRecords2, MatchDto match,int clubId){
		List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos = new ArrayList<PlayerStatisticSummaryDto>();
		
		match.setClubID(clubId);
		
		List<BattingDto> battingRecords = new ArrayList<BattingDto>();
		List<BowlingDto> bowlingRecords = new ArrayList<BowlingDto>();
		if(battingRecords1 != null) {
			
			for(BattingDto dto : battingRecords1) {
				dto.setTeamId(match.getTeamOne());
			}
			battingRecords.addAll(battingRecords1);
		}
		
		if(battingRecords2 != null) {
			for(BattingDto dto : battingRecords2) {
				dto.setTeamId(match.getTeamTwo());
			}
			battingRecords.addAll(battingRecords2);
		}
		
		if(bowlingRecords1 != null) {
			for(BowlingDto dto : bowlingRecords1) {
				dto.setTeamId(match.getTeamOne());
			}
			bowlingRecords.addAll(bowlingRecords1);
		}
		
		if(bowlingRecords2 != null) {
			for(BowlingDto dto : bowlingRecords2) {
				dto.setTeamId(match.getTeamTwo());
			}
			bowlingRecords.addAll(bowlingRecords2);
		}
		
		for(BattingDto battingDto : battingRecords) {
			setBattingRecords(playerStatisticsSummaryDtos, battingDto, match);
		}
		
		for(BowlingDto bowlingDto : bowlingRecords) {
			setBowlingRecords(playerStatisticsSummaryDtos, bowlingDto, match);
		}
		
		for(BattingDto battingDto : battingRecords) {
			setFeildingRedords(playerStatisticsSummaryDtos, battingDto, match);
		}
		
		if(match.getManOfTheMatch() >0) {
			PlayerStatisticSummaryDto playerStatisticSummaryDto =  new PlayerStatisticSummaryDto();
			playerStatisticSummaryDto.setPlayerId(match.getManOfTheMatch());
			playerStatisticSummaryDto.setMatchId(match.getMatchID());
			int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
			
			if(indexOfPlayer >= 0) {
				playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
			}
			addMOM(playerStatisticSummaryDto, match);
			///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
			if(indexOfPlayer < 0) {
				playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
			}
			//addMOM(match.getManOfTheMatch());
		}
		
		List<Integer> psIds = new ArrayList<Integer>();
		
		for(PlayerStatisticSummaryDto pssDto: playerStatisticsSummaryDtos) {
			
			psIds.add(pssDto.getPlayerId());			
		}
		
		for (Integer pid : match.getPlayers1()) {
			if (!psIds.contains(pid)) {
				PlayerStatisticSummaryDto pssDto = getDefaultPlayerStatsSummary(match, pid);
				pssDto.setTeamId(match.getTeamOne());
				playerStatisticsSummaryDtos.add(pssDto);
			}
		}

		for (Integer pid : match.getPlayers2()) {
			if (!psIds.contains(pid)) {
				PlayerStatisticSummaryDto pssDto = getDefaultPlayerStatsSummary(match, pid);
				pssDto.setTeamId(match.getTeamTwo());
				playerStatisticsSummaryDtos.add(pssDto);
			}
		}

		
		return playerStatisticsSummaryDtos;
	}

	private static PlayerStatisticSummaryDto getDefaultPlayerStatsSummary(MatchDto match, Integer pid) {
		PlayerStatisticSummaryDto pssDto = new PlayerStatisticSummaryDto();
		pssDto.setPlayerId(pid);
		pssDto.setSeriesId(match.getLeagueId());
		pssDto.setMatchId(match.getMatchID());
		pssDto.setMatchType(match.getMatchType());
		try {
			pssDto.setMatchDate(CommonUtility.stringToDate(match.getMatchDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return pssDto;
	}
	
	private static void setFeildingRedords(List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos, BattingDto battingDto, MatchDto match) {

		PlayerStatisticSummaryDto playerStatisticSummaryDto = new PlayerStatisticSummaryDto();
		int wt1 = CommonUtility.stringToInt(battingDto.getWicketTaker1());
		int wt2 = CommonUtility.stringToInt(battingDto.getWicketTaker2());
		
		if("ro".equals(battingDto.getHowOut())) {
			if(wt2 == 0 && wt1>0) {
				playerStatisticSummaryDto.setPlayerId(wt1);
				playerStatisticSummaryDto.setMatchId(match.getMatchID());
				
				if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
					playerStatisticSummaryDto.setTeamId(match.getTeamOne());
				}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
					playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
				}else {
					playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
				}
				
				int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
				if(indexOfPlayer >= 0) {
					playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
				}
				addDirectRunOut(playerStatisticSummaryDto, match);
				///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
				if(indexOfPlayer < 0) {
					playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
				}
				
			}else if(wt2 > 0 && wt1==0){

				playerStatisticSummaryDto.setPlayerId(wt2);
				playerStatisticSummaryDto.setMatchId(match.getMatchID());

				if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
					playerStatisticSummaryDto.setTeamId(match.getTeamOne());
				}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
					playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
				}else {
					playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
				}
				
				int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
				if(indexOfPlayer >= 0) {
					playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
				}
				addDirectRunOut(playerStatisticSummaryDto, match);
				///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
				if(indexOfPlayer < 0) {
					playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
				}
				
			} else if (wt1>0 && wt2>0) {
				playerStatisticSummaryDto.setPlayerId(wt1);
				playerStatisticSummaryDto.setMatchId(match.getMatchID());

				if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
					playerStatisticSummaryDto.setTeamId(match.getTeamOne());
				}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
					playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
				}else {
					playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
				}
				
				int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
				if(indexOfPlayer >= 0) {
					playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
				}
				addInDirectRunOut(playerStatisticSummaryDto, match);
				///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
				if(indexOfPlayer < 0) {
					playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
				}
				
				playerStatisticSummaryDto = new PlayerStatisticSummaryDto();
				playerStatisticSummaryDto.setPlayerId(wt2);
				playerStatisticSummaryDto.setMatchId(match.getMatchID());

				if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
					playerStatisticSummaryDto.setTeamId(match.getTeamOne());
				}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
					playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
				}else {
					playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
				}
				
				indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
				if(indexOfPlayer >= 0) {
					playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
				}
				addInDirectRunOut(playerStatisticSummaryDto, match);
				///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
				if(indexOfPlayer < 0) {
					playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
				}
				
			}
		}else if("ct".equals(battingDto.getHowOut())) {
			playerStatisticSummaryDto.setPlayerId(wt2);
			playerStatisticSummaryDto.setMatchId(match.getMatchID());

			if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
				playerStatisticSummaryDto.setTeamId(match.getTeamOne());
			}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
				playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
			}else {
				playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
			}
			
			int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
			if(indexOfPlayer >= 0) {
				playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
			}
			addCatch(playerStatisticSummaryDto, match);
			///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
			if(indexOfPlayer < 0) {
				playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
			}
		}else if("ctw".equals(battingDto.getHowOut())) {
			playerStatisticSummaryDto.setPlayerId(wt2);
			playerStatisticSummaryDto.setMatchId(match.getMatchID());

			if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
				playerStatisticSummaryDto.setTeamId(match.getTeamOne());
			}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
				playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
			}else {
				playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
			}
			
			int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
			if(indexOfPlayer >= 0) {
				playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
			}
			addCatchW(playerStatisticSummaryDto, match);
			///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
			if(indexOfPlayer < 0) {
				playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
			}
		}else if("st".equals(battingDto.getHowOut())) {

			playerStatisticSummaryDto.setPlayerId(wt2);
			playerStatisticSummaryDto.setMatchId(match.getMatchID());

			if(match.getPlayers1() != null && !match.getPlayers1().isEmpty() && match.getPlayers1().contains(wt1)) {
				playerStatisticSummaryDto.setTeamId(match.getTeamOne());
			}else if(match.getPlayers2() != null && !match.getPlayers2().isEmpty() && match.getPlayers2().contains(wt1)){
				playerStatisticSummaryDto.setTeamId(match.getTeamTwo());
			}else {
				playerStatisticSummaryDto.setTeamId(battingDto.getTeamId());
			}
			
			int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
			if(indexOfPlayer >= 0) {
				playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
			}
			addStumping(playerStatisticSummaryDto, match);
			///meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
			if(indexOfPlayer < 0) {
				playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
			}
		//addStumping(wt2);
		}
	}

	private static void addInDirectRunOut( PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setIndirectRo(playerStatisticSummaryDto.getIndirectRo() +1);
	}

	private static void setBowlingRecords(List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos, BowlingDto bowlingDto, MatchDto match) {

		PlayerStatisticSummaryDto playerStatisticSummaryDto = new PlayerStatisticSummaryDto();
		playerStatisticSummaryDto.setPlayerId(bowlingDto.getPlayerID());
		playerStatisticSummaryDto.setMatchId(match.getMatchID());
		// Check if already exist in the List
		int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
		if(indexOfPlayer >= 0) {
			playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
		}
		meargePlayerBowling(playerStatisticSummaryDto,bowlingDto, match);
		if(indexOfPlayer < 0) {
			playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
		}
	
	}

	private static void setBattingRecords(List<PlayerStatisticSummaryDto> playerStatisticsSummaryDtos, BattingDto battingDto, MatchDto match) {

		PlayerStatisticSummaryDto playerStatisticSummaryDto = new PlayerStatisticSummaryDto();
		playerStatisticSummaryDto.setPlayerId(battingDto.getPlayerID());
		playerStatisticSummaryDto.setMatchId(match.getMatchID());
		// Check if already exist in the List
		int indexOfPlayer = playerStatisticsSummaryDtos.indexOf(playerStatisticSummaryDto);
		if(indexOfPlayer >= 0) {
			playerStatisticSummaryDto = playerStatisticsSummaryDtos.get(indexOfPlayer);
		}
		meargePlayerBatting(playerStatisticSummaryDto,battingDto, match);
		//meargePlayerFeilding(playerStatisticSummaryDto,battingDto); // TODO 
		if(indexOfPlayer < 0) {
			playerStatisticsSummaryDtos.add(playerStatisticSummaryDto);
		}
	
	}

	private static void addMOM(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setManOfTheMatch(true);
	}

	private static void addStumping(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setStumpings(playerStatisticSummaryDto.getStumpings() +1);
	}

	private static void addCatchW(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setCatchesW(playerStatisticSummaryDto.getCatchesW() +1);
	}

	private static void addCatch(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setCatches(playerStatisticSummaryDto.getCatches() +1);
	}

	private static void addDirectRunOut(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setDirectRo(playerStatisticSummaryDto.getDirectRo() +1);
	}

	private static void meargePlayerBatting(PlayerStatisticSummaryDto playerStatisticSummaryDto, BattingDto battingRecord, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setTeamId(battingRecord.getTeamId());
		playerStatisticSummaryDto.setBallsFaced(battingRecord.getBallsFaced());
		playerStatisticSummaryDto.setFours(battingRecord.getFours());
		playerStatisticSummaryDto.setSixers(battingRecord.getSixers());
		playerStatisticSummaryDto.setBattingPoints(battingRecord.getPoints(match.getClubID()));
		playerStatisticSummaryDto.setRunsScored(battingRecord.getRunsScored());
		playerStatisticSummaryDto.setHowOut(battingRecord.getHowOut());
		playerStatisticSummaryDto.setWicketTaker1(CommonUtility.stringToInt(battingRecord.getWicketTaker1()));
		playerStatisticSummaryDto.setWicketTaker2(CommonUtility.stringToInt(battingRecord.getWicketTaker2()));
	}
	
	private static void meargePlayerFeilding(PlayerStatisticSummaryDto playerStatisticSummaryDto, BattingDto battingRecord, MatchDto match) {
		//
		
		
		
	}
	
	private static void meargePlayerBowling(PlayerStatisticSummaryDto playerStatisticSummaryDto, BowlingDto bowlingDto, MatchDto match) {
		setCommonMatchData(playerStatisticSummaryDto, match);
		playerStatisticSummaryDto.setTeamId(bowlingDto.getTeamId());
		playerStatisticSummaryDto.setBallsBowled(bowlingDto.getBalls());
		playerStatisticSummaryDto.setHatricks(bowlingDto.getHattricks());
		playerStatisticSummaryDto.setMaidens(bowlingDto.getMaidens());
		playerStatisticSummaryDto.setNoBalls(bowlingDto.getNoBalls());
		playerStatisticSummaryDto.setBowlingPoints(bowlingDto.getPoints(match.getClubID()));
		playerStatisticSummaryDto.setRunsGiven(bowlingDto.getRuns());
		playerStatisticSummaryDto.setWickets(bowlingDto.getWickets());
		playerStatisticSummaryDto.setWides(bowlingDto.getWides());
		playerStatisticSummaryDto.setDotBalls(bowlingDto.getDotBalls());
		
	}
	
	private static void setCommonMatchData(PlayerStatisticSummaryDto playerStatisticSummaryDto, MatchDto match) {
		playerStatisticSummaryDto.setMatchId(match.getMatchID());
		playerStatisticSummaryDto.setMatchDate(new Date(match.getMatchDate()));
		playerStatisticSummaryDto.setMatchType(match.getMatchType());
		playerStatisticSummaryDto.setSeriesId(match.getLeagueId());
	}
}
