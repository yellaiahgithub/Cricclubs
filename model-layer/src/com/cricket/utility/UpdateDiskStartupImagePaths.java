package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cricket.dao.ClubFactory;
import com.cricket.dto.ClubDto;

public class UpdateDiskStartupImagePaths {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		int maxClubID = 0;
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int cid = 0;
		int imgCount = 0;
		Connection con = null;
		try {
			con = DButility.getDefaultConnection();
			for (int i = 1; i <= maxClubID; i++) {
				
				cid = i;

				PreparedStatement pst = null;
				System.out.println(" ======================== Updating Startup Image for the club " + i
						+ " =========================== ");

				System.out.println(" ========= Startup Image found for the club " + i + " =============== ");

				ClubDto club = ClubFactory.getClub(i);
				
				if ( club != null ) {
					
					String url = "https://www-test.cricclubs.com/" + club.getStartupImagePath();
					
					CloseableHttpClient client = HttpClients.createDefault();			
					HttpGet httpGet = new HttpGet(url);            
		            CloseableHttpResponse response = client.execute(httpGet);
					
		            if( response.getStatusLine().getStatusCode() != 200) {
						String query = "update club set startup_image_path = '' where club_id = " + i;
						pst = con.prepareStatement(query);
						pst.executeUpdate();
						System.out.println(" ========= Startup Image updated to empty for the club " + i + " =============== ");
					}else {
						imgCount++;
						System.out.println(" ========= Startup Image exists for the club Id " + i + " =============== ");
					}
				}
			}
			System.out.println("Total Images found are "+imgCount);
		} catch (Exception e) {
			System.err.println("Failed to Execute for club" + cid + " The error is " + e.getMessage());
		} finally {
			DButility.closeConnection(con);
		}
	}

}