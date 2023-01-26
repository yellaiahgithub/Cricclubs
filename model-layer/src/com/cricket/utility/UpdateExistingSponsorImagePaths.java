package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.SponsorFactory;
import com.cricket.dto.SponsorDto;

public class UpdateExistingSponsorImagePaths {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		int maxClubID = 0;
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for (int i = 1; i <= maxClubID; i++) {
			
			Connection conn = DButility.getConnection(i);
			
			PreparedStatement pst = null;
			try {
				System.out.println(" ========= Sponsors Update running for the club id "+i+" =============== ");
				
				List<SponsorDto> sponsors = SponsorFactory.getSponsors(i);
				
				if (!CommonUtility.isListNullEmpty(sponsors)) {
					
					System.out.println("Sponsors are there and update running for the club id "+i);
					
					for (SponsorDto sponsor : sponsors) {
						
						String query = "update sponsors set image_path='/documentsRep/sponsors/"+i+"-"+sponsor.getSponsorId()+"-sponsor.jpg' where sponsor_id = "+sponsor.getSponsorId();
						
						pst = conn.prepareStatement(query);
						pst.executeUpdate();
					}
				}else {
					
					System.out.println("No Sponsors found for the club id "+i);
				}
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + i + " The error is " + e.getMessage());
			} finally {
				DButility.closeConnection(conn);
			}
		}
	}
}