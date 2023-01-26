package com.cricket.utility;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;



public class RunTeamLogoScript {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

	
			Connection con = null;

			try {
				
				
		        
		        File directory = new File("/home/nganeshreddi/documentsRep/teamLogos/");
		        //get all the files from a directory
		        File[] fList = directory.listFiles();
		        for (File file : fList){
		            if (file.isFile()){
		                
		                String id[] = file.getName().replace(".jpg", "").split("-teamLogo-");
		                con = DButility.getConnection(Integer.parseInt(id[0].trim()));
		                String query = "update team set logo_file_path = '/documentsRep/teamLogos/"+ file.getName() +"' where team_id = "+id[1];
		                Statement st = con.createStatement();
		                st.executeUpdate(query);
		                DButility.closeConnection(con);
		                
		            }
		        }


			} catch (Exception e) {
				System.out.println(e.getMessage());
			}finally{
				//DButility.closeConnection(con);
			}
		}
	}
