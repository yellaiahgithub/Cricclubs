package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.PlayerFactory;
import com.cricket.dto.PlayerDto;

public class AdhocRunner {

	public static void mergePlayers(int clubId) throws Exception {

		String pfName = "";
		String plname = "";
		List<PlayerDto> players = PlayerFactory.getAllPlayerofLeague(clubId);

		int i = 0;
		for (PlayerDto player : players) {
			if (player.getFirstName().equals(pfName) && player.getLastName().equalsIgnoreCase(plname)) {
				continue;
			} else {
				pfName = player.getFirstName();
				plname = player.getLastName();
			}

			List<PlayerDto> matchList = getMatchingPlayers(players, player.getFirstName(), player.getLastName());
			if (matchList.size() < 2) {
				continue;
			}
			PlayerDto primaryPlayer = null;
			for (PlayerDto matchPlayer : matchList) {
				if (matchPlayer.isVerified()) {
					primaryPlayer = matchPlayer;
				}
			}

			if (primaryPlayer == null) {
				primaryPlayer = matchList.get(0);
			}

			for (PlayerDto matchPlayer : matchList) {
				if (matchPlayer.getPlayerID() != primaryPlayer.getPlayerID()) {
				}
			}

		}

	}

	private static List<PlayerDto> getMatchingPlayers(List<PlayerDto> players, String firstName, String lastName) {
		List<PlayerDto> matchList = new ArrayList<PlayerDto>();
		for (PlayerDto player : players) {
			if (player.getFirstName().equals(firstName) && player.getLastName().equalsIgnoreCase(lastName)) {
				matchList.add(player);
			}
		}
		return matchList;
	}

	public static void main(String[] args) throws Exception {
		mergePlayers(343);
	}

}
