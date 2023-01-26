package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.UserDto;

public class UserProfileChanges {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

	
			Connection con = DButility.getDefaultConnection();
			PreparedStatement st = null;
			PreparedStatement st1 = null;
			ResultSet rs = null;
			UserDto user = null;
			try {
				
				List<UserDto> users = new ArrayList<UserDto>();
				
				String query = " select user_id, password from mcc.user where password  is not null && password  != '' and update_date is null order by user_id ";

				st = con.prepareStatement(query);			
				rs = st.executeQuery();
				System.out.println("users found and populating ....  ");
				while (rs.next()) {
					user = new UserDto();
					user.setUserID(rs.getInt("user_id"));
					user.setPassword(rs.getString("password"));
					users.add(user);
				}
				System.out.println("Total users found and populating ....   " + users.size());
				String query1= "update mcc.user set password = ?, update_date = sysdate() where user_id = ?";
				st1 = con.prepareStatement(query1);
				int i = 0;
				for(UserDto ur : users) {
					st1.setString(1, CommonUtility.encrypt(ur.getPassword()));
					st1.setInt(2, ur.getUserID());
					st1.addBatch();
					i++;
					if(i >= 1000) {
						st1.executeBatch();
						st1.clearBatch();
						i = 0;
						System.out.println("Batch update ... ");
					}
				}
				st1.executeBatch();
				st1.clearBatch();
				System.out.println("Batch update Done ... ");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}finally{
				DButility.dbCloseAll(con, st, rs);
				DButility.dbCloseAll(con, st1, rs);
			}
		}
	
}
