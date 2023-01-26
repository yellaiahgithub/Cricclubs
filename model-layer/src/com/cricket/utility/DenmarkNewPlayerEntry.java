package com.cricket.utility;

import java.util.List;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.DenmarkPlayersDAO;
import com.cricket.dao.UserFactory;
import com.cricket.dto.DenmarkPlayersDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.lite.ClubDtoLite;

public class DenmarkNewPlayerEntry {
	
	public static void main(String[] args) throws Exception {

		int clubId = 21070;
		ClubDtoLite club = null;
		try {
			club = ClubFactory.getLiteClubDetails(clubId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DenmarkPlayersDAO dao = new DenmarkPlayersDAO();
		List<DenmarkPlayersDto> denmarkPlayers=dao.denmarkPlayersList(clubId);
		
		for(DenmarkPlayersDto playerDP : denmarkPlayers) {
			
			PlayerDto player = new PlayerDto();
			
			player.setFirstName(playerDP.getFirstName());
			player.setLastName(playerDP.getLastName());
			player.setDateOfBirth(playerDP.getDob());
			player.setIsActive("1");
			int playerId = CommonLogic.registerNewPlayer(club, player, "Guest", "SUPPORT_TEAM_EXCEL_FILE");
			System.out.println("player_id = "+playerId);
			Thread.sleep(1000);
			
			UserDto dbUser = UserFactory.getUserByPlayerId(playerId, clubId);
			
			dbUser.setState(playerDP.getState());
			dbUser.setPostalCode(playerDP.getPostalCode()+"");
			
			UserFactory.updateUser(dbUser, clubId, "GUEST");
			
			ClubFactory.addPlayerToClub(playerId, playerDP.getInternalClubId(), clubId, "GUEST");
		}

	}
}
