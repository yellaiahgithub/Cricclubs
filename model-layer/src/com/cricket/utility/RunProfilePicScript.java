package com.cricket.utility;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;



public class RunProfilePicScript {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

	
			Connection con = DButility.getDefaultConnection();

			try {

		        File directory = new File("/home/nganeshreddi/documentsRep/profilePics/");
		        //get all the files from a directory
		        File[] fList = directory.listFiles();
		        for (File file : fList){
		            if (file.isFile()){
		                
		                
		                if(file.getName().contains("-player"))
		                { 
		                	String id[] = file.getName().replace(".jpg", "").split("-playerImages");
		                	
			                String query = "update player set profilepic_file_path = '/documentsRep/profilePics/"+ file.getName() +"' where player_id = "+id[1];
			                Statement st1 = con.createStatement();
			                st1.executeUpdate(query);
		                }
		                else{
		                String player_id = file.getName().replace(".jpg", "");
		                String query = "update player set profilepic_file_path = '/documentsRep/profilePics/"+ file.getName() +"' where player_id = "+player_id;
		                Statement st = con.createStatement();
		                st.executeUpdate(query);
		                
		                String query_audit = "update player_audit set profilepic_file_path = '/documentsRep/profilePics/"+ file.getName() +"' where player_id = "+player_id;
		                Statement stquery_audit = con.createStatement();
		                stquery_audit.executeUpdate(query_audit);
		                
		                }
		                
		            }
		        }
		    

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}
