package com.cricket.utility;

import java.util.List;

import com.cricket.dao.PlayerFactory;

public class MergePlayersByIds {

	public static void main(String[] args) throws Exception {
		// Change Club ID
		int primaryPlayerId = 660702;		
		int secondaryPlayerId = 2258737;
		
		// Note: Add primary player to secondary club if already not there
		
		List<Integer> secondaryPlayerClubIds = PlayerFactory.getAllPlayerClubIds(secondaryPlayerId);
		
		for(Integer secondaryClubId : secondaryPlayerClubIds) {
			
			Boolean success = PlayerFactory.mergePlayers(primaryPlayerId, secondaryPlayerId, secondaryClubId, "Guest");
			
			System.out.println(primaryPlayerId + "\t" + secondaryPlayerId);
			
			if (success) {
				System.out.println("Done");
			} else {
				System.out.println("Not Done");
			}
		}
	}
}