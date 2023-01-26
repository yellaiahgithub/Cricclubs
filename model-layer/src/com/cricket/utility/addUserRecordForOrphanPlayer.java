package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class addUserRecordForOrphanPlayer {

	public static void main(String[] args) throws Exception {

		String query = "select player_id, f_name, l_name, email FROM mcc.player "
				+ "WHERE player_id NOT IN ( SELECT player_id FROM mcc.user WHERE player_id>0) ";
		
		Connection conn = null;
		Connection conn1 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Statement st = null;
		ResultSet rs1 = null;
		
		String userName = "";
		String password = CommonUtility.encrypt("cricclubs");
		
		try {
			conn1 = DButility.getDefaultConnection();
			conn = DButility.getDefaultReadConnection();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			
			st = conn1.createStatement();
			
			int noOfPlayers = 0;

			while (rs.next()) {
				
				noOfPlayers++;
				
				userName = UUID.randomUUID().toString();
				//System.out.println("PlayerId is - "+rs.getInt("player_id"));

				String query1 = "insert into user(user_name, password, f_name, l_name, email, player_id) " + " values('"
						+ userName + "','" + password + "','" + DButility.escapeLine(rs.getString("f_name")) + "','" + DButility.escapeLine(rs.getString("l_name"))
						+ "','" + rs.getString("email") + "'," + rs.getInt("player_id") + ");";
				// System.out.println(query1);
				 try {
					st.executeUpdate(query1);
				}catch (SQLException e) {
					System.out.println(e.getMessage());
				} 
			}
			System.out.println("No of Players - "+noOfPlayers);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DButility.closeConnection(conn1);
			DButility.closeStatement(st);
			DButility.closeRs(rs1);
			DButility.dbCloseAll(conn, pst, rs);
		}
	}
}