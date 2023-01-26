package com.cricket.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.cricket.dao.PlayerFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.UserDto;


public class MergePlayersFromFile {
	
	public static void main(String[] args) throws Exception {

		System.out.println("Program Start...");

		int primaryClubId = 5256;
		int secondaryClubId = 9966;

		ArrayList<Integer[]> players = new ArrayList<>();
		
		ArrayList<String[]> records = new ArrayList<String[]>();
		String[] record = new String[2];	
		
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\jira2518.csv"))) {
		    String line;
		    String COMMA_DELIMITER = ",";
		    while ((line = br.readLine()) != null) {		        
		    	record = line.split(COMMA_DELIMITER);		    	
		        records.add(record);
		    }
		}
		int player1 = 0;
		int player2 = 0;
		
		 for(int i = 0; i < records.size(); i++)
		    {
			 player1 = 0;
			 player2 = 0;   
			 for(int j = 0; j < records.get(i).length; j++)
		        {
		            if(player1 == 0) {
		            	player1 = CommonUtility.stringToInt(records.get(i)[j]);		            	
		            }else if(player2 == 0) {
		            	player2 = CommonUtility.stringToInt(records.get(i)[j]);		            	
		            } 
		        }
			 	players.add(new Integer[] { player1, player2 });		        
		    }
				for (int i = 0; i < players.size(); i++) {

					int pPlayerId = players.get(i)[0];
					int sPlayerId = players.get(i)[1];
					
					Boolean success = PlayerFactory.mergePlayers(pPlayerId, sPlayerId, secondaryClubId,"Guest");	
					
					PlayerDto primaryPlayer = PlayerFactory.getPlayerById(pPlayerId, primaryClubId);
					UserDto primaryUser = UserFactory.getUserByPlayerId(pPlayerId, primaryClubId);
					
					PlayerDto existingPlayer = PlayerFactory.getPlayerById(pPlayerId, secondaryClubId);
					
					if (existingPlayer == null) {
						PlayerFactory.addPlayerToClub(primaryPlayer, secondaryClubId, "Guest");
					}
					UserDto existingUserDto = UserFactory.getUserById(primaryUser.getUserID(), secondaryClubId);
					if (existingUserDto == null) {
						UserFactory.registerExistingUser(primaryUser, secondaryClubId, "Guest");
					}	

					System.out.println(players.get(i)[0] + "\t" + players.get(i)[1]);
					
					if (success) {
						System.out.println("Done : "+i);
					} else {
						System.out.println("Not Done :"+i);
					}
				}
		System.out.println("Program End!! ");
	}	
}
