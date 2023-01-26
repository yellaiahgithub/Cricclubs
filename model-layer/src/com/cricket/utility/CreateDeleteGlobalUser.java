package com.cricket.utility;

import java.sql.Connection;
import java.sql.Statement;

public class CreateDeleteGlobalUser {
	/**
	 * @param args
	 *            the command line arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		int user_id = 1013277;
		
		makeUserGlobal(user_id);
		
	//	deleteGlobalUser(user_id);
		
	}	
	private static void makeUserGlobal(int user_id){
		
		if(user_id > 0) {
			
			Connection conn = null;
			Statement st = null;			
			String query = "";
			try {				
				conn = DButility.getDefaultConnection();			
				st = conn.createStatement();
				query = " delete from mcc.user_club where user_id = "+user_id+" and access_level = 1";
				st.execute(query);
							
			for (int i = 1; i <= 12000; i++) {				
				query = " insert into mcc.user_club(user_id, club_id, access_level) values("+user_id+","+i+",1)";
				st.addBatch(query);				
				query = null;
				
				if(i%1000 == 0 ) {
					st.executeBatch();
					st.clearBatch();
				}
			}	
			st.executeBatch();			
			
			}catch (Exception e1) {				
				e1.printStackTrace();
			}finally{
				DButility.closeConnectionAndStatement(conn, st);
			}
		}
	
	/*private static void deleteGlobalUser(int user_id) throws Exception {
		
		Connection conn = null;
		PreparedStatement st = null;
			
		try {						
			conn = DButility.getDefaultConnection();
				
			String query = " delete from mcc.user_club where user_id = "+user_id+" and access_level = 1";
			st = conn.prepareStatement(query);
				
			st.executeUpdate();			
						
			} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}		
	}*/
	}
}