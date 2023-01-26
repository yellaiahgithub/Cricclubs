package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cricket.dao.UmpireFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.UmpireDto;
import com.cricket.dto.UserDto;

public class UpdateDiskUmpireImagePaths {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		List<UmpireDto> umpires = null;
		try {
			umpires = UmpireFactory.getAllUmpiresList();
		} catch (Exception e1) {
			e1.printStackTrace();
		}			
		Connection conn = DButility.getDefaultReadConnection();	
		int imageCount = 0;
		try {
		
		for (UmpireDto umpire : umpires) {

			PreparedStatement pst = null;
			
			String url = "https://www-test.cricclubs.com/documentsRep/" + umpire.getProfileImagePath();
			
			CloseableHttpClient client = HttpClients.createDefault();			
			HttpGet httpGet = new HttpGet(url);            
            CloseableHttpResponse response = client.execute(httpGet);
			
            if( response.getStatusLine().getStatusCode() == 200) {
            	imageCount = imageCount+1;
            	System.out.println(" ========= Image exists for umpire " + umpire.getUmpireID() + " =============== ");
            }else {
					System.out.println(" ========= Image Not found for the umpire " + umpire.getUmpireID() + " =============== ");
					
					System.out.println(" ========= Image Not found for the umpire " + url + " =============== ");

					UserDto user = UserFactory.getUserById(umpire.getUserID(), umpire.getClubId());

					if (user != null && user.getPlayerID() == 0 && user.getUmpireID() > 0 && ( user.getUserID() == umpire.getUserID()) && (user.getUmpireID() == umpire.getUmpireID()) ) {

						String query = "update mcc.user set profile_image_path = '' where user_id = " + umpire.getUserID();
						pst = conn.prepareStatement(query);
						pst.executeUpdate();
						
						System.out.println(" ========= Image set to empty for the umpire " + umpire.getUmpireID() + " =============== ");
					}else {
						System.out.println(" ========= User not found for the umpire " + umpire.getUmpireID() + " =============== ");
					}
				}
			} 
		System.out.println(" Total Images exists are " + imageCount);
		
		}catch (Exception e) {
				System.err.println(" The error is " + e.getMessage());
			} finally {
				DButility.closeConnection(conn);
			}

		}	
}