package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.SponsorFactory;
import com.cricket.dto.SponsorDto;

public class UpdateDiskSponsorImagePaths {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		int maxClubID = 0;			
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {			
			e1.printStackTrace();
		}		
		for (int i = 1; i <= maxClubID; i++) {			
			Connection con = DButility.getConnection(i);
			PreparedStatement pst = null;
			System.out.println(" ======================== Updating Sponsors for the club " + i + " =========================== ");
			try {
				List<SponsorDto> sponsors = SponsorFactory.getSponsors(i);
				
				if( !CommonUtility.isListNullEmpty(sponsors)){
					
					System.out.println(" ========= Sponsors found for the club " + i + " =============== ");
					
					for( SponsorDto sponsor : sponsors) {
						
						String url = "https://www-test.cricclubs.com/" + sponsor.getImagePath();
						
						CloseableHttpClient client = HttpClients.createDefault();			
						HttpGet httpGet = new HttpGet(url);            
			            CloseableHttpResponse response = client.execute(httpGet);
						
			            if( response.getStatusLine().getStatusCode() != 200) {
							String query = "update sponsors set image_path = '' where sponsor_id = " + sponsor.getSponsorId();
							pst = con.prepareStatement(query);
							pst.executeUpdate();
							System.out.println(" ========= Sponsor image path updated to empty for the club " + i + " and Sponsor id is "+ sponsor.getSponsorId() + " =============== ");
						}
					}					
				}
			} catch (Exception e) {
				System.err.println("Failed to Execute for club " + i	+ " The error is " + e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}	
}