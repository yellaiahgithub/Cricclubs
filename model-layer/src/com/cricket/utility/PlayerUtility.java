package com.cricket.utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cricket.dao.ClubFactory;
import com.cricket.dto.BattingDto;
import com.cricket.dto.lite.ClubDtoLite;

public class PlayerUtility {

	
	public static Map<String, String> createPlayerMap(List<BattingDto> team1Batting,
			List<BattingDto> team2Batting,int clubId) throws Exception {
		Map<String, String> players = new HashMap<String, String>();
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		int firstNameFirst = 0;
		if(club != null){
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
		players.put("-1", "Substitute");
		return players;
	}
	
	public static Map<String, String> createPlayerNickNameMap(List<BattingDto> team1Batting,
			List<BattingDto> team2Batting,int clubId) throws Exception {
		
		Map<String, String> players = new HashMap<String, String>();
		ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
		int firstNameFirst = 0;
		if(club != null){
			firstNameFirst = club.getFirstNameFirst();
		}
		if (team1Batting != null) {
			Iterator<BattingDto> itr = team1Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				if(!CommonUtility.isNullOrEmpty(dto.getNickName())) {
					players.put(dto.getPlayerID() + "", dto.getNickName());
				}else {
					players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
				}
			}
		}
		if (team2Batting != null) {
			Iterator<BattingDto> itr = team2Batting.iterator();
			while (itr.hasNext()) {
				BattingDto dto = itr.next();
				if(!CommonUtility.isNullOrEmpty(dto.getNickName())) {
					players.put(dto.getPlayerID() + "", dto.getNickName());
				}else {
					players.put(dto.getPlayerID() + "", dto.getPlayerShortName(firstNameFirst));
				}
			}
		}
		players.put("-1", "Substitute");
		return players;
	}
	
}
