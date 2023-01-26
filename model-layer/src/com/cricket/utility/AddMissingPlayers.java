package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cricket.dao.ClubFactory;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.lite.ClubDtoLite;

/**
 * @author ccdeveloper-SR
 *
 */
public class AddMissingPlayers {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws SQLException {

		/*
		 * requirement 
		 * #Table[Temp] ResidentId math with mcc.player.custom_id
		 *  if not match create player profile in database using Temp table user information.
		 */
		int newPlayerCount = 0;
		System.out.println("----program started----");
		int clubId = 14202;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		int customId = 0;
		try {
			String fetchTempTable = "SELECT * FROM Oman_update_JerseyNumber_temp";
			String getCustomIdQuery = "select custom_id,player_id from mcc.player where custom_id=";
			ClubDtoLite club = ClubFactory.getLiteClubFromDB(clubId, true);
			connection = DButility.getConnection(clubId);
			preparedStatement = connection.prepareStatement(fetchTempTable);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String surName = resultSet.getString("sur_name");
				int jerseyNumber = resultSet.getInt("jersy_no");
				int residentId = resultSet.getInt("resident_card");

				if (residentId == 50) {
					preparedStatement1 = connection.prepareStatement(getCustomIdQuery + residentId + " limit 1");
					resultSet1 = preparedStatement1.executeQuery();
					while (resultSet1.next()) {
						customId = resultSet1.getInt("custom_id");
					}
					if (customId == 0) {
						PlayerDto player = new PlayerDto();
						player.setFirstName(firstName);
						player.setLastName(surName);
						player.setJerseyNumber(jerseyNumber + "");
						player.setCustomId(residentId + "");
						player.setPlayingRole("All Rounder");
						int newPlayerId = CommonLogic.registerNewPlayer(club, player, firstName + " " + surName,
								"CLIENT_REQUEST_BY_PROGRAM");
						newPlayerCount++;
						System.out.println(" playerId = " + newPlayerId);
					}

				}
				System.out.println("data inserted for id " + customId);
			}
			System.out.println("new Players Count =" + newPlayerCount);

			/*
			 * Requirement [add a player into team_palyer table] 
				 * get Temp table and player table, check with residentId and customId & match 
				 * if found then match with,
				 * player table player_id & Temp table team_id in the team_player table
				 * if row found
				 * then insert a row else skip.
				 */
			int playerId = 0;
			int teamId = 0;
			ResultSet teamPlayerResultSet = null;
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				boolean ignoreLoop = false;
				int residentCard = resultSet.getInt("resident_card");
				int jerseyNumber= resultSet.getInt("jersy_no");
				teamId = resultSet.getInt("team_id");
				if (residentCard > 0) {
					preparedStatement1 = connection.prepareStatement(getCustomIdQuery + residentCard + " limit 1");
					resultSet1 = preparedStatement1.executeQuery();
					while (resultSet1.next()) {
						playerId = resultSet1.getInt("player_id");
					}
					preparedStatement = connection.prepareStatement(
							"SELECT * FROM team_player WHERE player_id=" + playerId + " AND team_id=" + teamId + "");
					teamPlayerResultSet = preparedStatement.executeQuery();
					while (!teamPlayerResultSet.next() && ignoreLoop == false) {
						preparedStatement = connection
								.prepareStatement("insert into team_player(team_id ,player_id) values (" + teamId + ","
										+ playerId + ") ");
						boolean isInserted = preparedStatement.execute();
						ignoreLoop = true;
						System.out.println("data inserted status= " + isInserted + " for the teamId= " + teamId
								+ " and playerId = " + playerId);
						
						//update jerseyNumbers 
						
						String updatejerseyNumberOnClientRequest="UPDATE mcc.player SET jersey_number = "+jerseyNumber+" WHERE custom_id = "+residentCard+"";
						preparedStatement.execute(updatejerseyNumberOnClientRequest);
					}
				}
			}
			
			
			
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

	}

}
