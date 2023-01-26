package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.PlayerFactory;
import com.cricket.dto.PlayerDto;
public class MergeDuplicatePlayersByTeamName {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// Change Club ID
		int ccClubId = 14202;
		int playerId = 0;
		// Change Merge Type. Values Can be SRC_PLAYER_ID or PLAYER_NAME or PLAYER_EMAIL
		String mergeType = "PLAYER_NAME";
		// Run the program again and again till players list is empty
		System.out.println("Program Start...");
		String player1details = "";
		String player2details = "";

		List<PlayerDto> allPlayers = PlayerFactory.getPlayers(new PlayerDto(), ccClubId);
		List<PlayerDto> players = new ArrayList<PlayerDto>();

		List<Integer> primaryPlayerIds = new ArrayList<Integer>();
		List<Integer> secondaryPlayerIds = new ArrayList<Integer>();

		if (playerId > 0) {
			PlayerDto player = PlayerFactory.getPlayerById(playerId, ccClubId);
			players.add(player);
		} else {
			players.addAll(allPlayers);
		}

		if (allPlayers.size() > 0 && players.size() > 0) {

			for (PlayerDto player1 : players) {
				
				if(!primaryPlayerIds.contains(player1.getPlayerID()) && !secondaryPlayerIds.contains(player1.getPlayerID())) {
					
					primaryPlayerIds.add(player1.getPlayerID());
					
					if (!CommonUtility.isNullOrEmpty(player1.getTeamName().toString())) {

						player1details = player1.getFirstName() + " " + player1.getLastName() + " " + player1.getTeamName();

						player1details = player1details.trim().toUpperCase();

						for (PlayerDto player2 : allPlayers) {

							if (player1.getPlayerID() != player2.getPlayerID() && !secondaryPlayerIds.contains(player2.getPlayerID())) {

								player2details = player2.getFirstName() + " " + player2.getLastName() + " " + player2.getTeamName();
								player2details = player2details.trim().toUpperCase();

								if (player1details.equals(player2details)) {
									secondaryPlayerIds.add(player2.getPlayerID());
									System.out.println(player1.getPlayerID() + "-" + player1details + "-" + player2details + "-" + player2.getPlayerID());
									PlayerFactory.mergePlayers(player1.getPlayerID(),player2.getPlayerID() , ccClubId, "GUEST");
								}

							}
						}
					}
				}

			}
		}

	}

}
