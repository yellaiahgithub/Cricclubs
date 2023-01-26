package com.cricket.utility;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;



public class RunClubLogoScript {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

	
			Connection con = DButility.getDefaultConnection();

			try {
				
				File directory = new File("/home/nganeshreddi/documentsRep/logos/");
		        //get all the files from a directory
		        File[] fList = directory.listFiles();
		        for (File file : fList){
		            if (file.isFile()){
		                
		                String club_id = file.getName().replace("logo", "").replace(".jpg", "");
		                
		                String query = "update club set logo_file_path = '/documentsRep/logos/"+ file.getName() +"' where club_id = "+club_id;
		                Statement st = con.createStatement();
		                st.executeUpdate(query);
		                
		            }
		        }


			} catch (Exception e) {
				System.out.println(e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}
