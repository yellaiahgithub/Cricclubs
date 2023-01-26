package com.cricket.utility;

import java.util.ArrayList;

import com.cricket.dao.PlayerFactory;

public class MergeDuplicatePlayers {
	
	public static void main(String[] args) throws Exception {
		// Change Club ID
		int ccClubId = 14202;
		// Change Merge Type. Values Can be SRC_PLAYER_ID or PLAYER_NAME or PLAYER_EMAIL
		String mergeType = "PLAYER_NAME_EMAIL";
		// Run the program again and again till players list is empty
		System.out.println("Program Start...");
		ArrayList<Integer[]> players = new ArrayList<>();		 
		
			players = PlayerFactory.getDuplicatePlayersMap(ccClubId, mergeType);
			if (players.size() > 0) {				
				System.out.println("Number of Players Identified for Merging " + players.size());
				for (int i = 0; i < players.size(); i++) {
					try {
						Boolean success = true;//PlayerFactory.mergePlayers(players.get(i)[0], players.get(i)[1], ccClubId,"Guest");
						if (success) {
					
							System.out.println("player firstname"+players.get(i)[0] + "\t" +"player lastname "+ players.get(i)[1]+  "\t" +"player email "+ players.get(i)[2]+ " Merge Done");
						} else {
							System.out.println(players.get(i)[0] + "\t" + players.get(i)[1]+ " Merge Not Done");
						}
					}catch(Exception e) {
						System.out.println(players.get(i)[0] + "\t" + players.get(i)[1]+ " Merge Issue");
					}
				}
			}
		System.out.println("Program End!! ");
	}
}