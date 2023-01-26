package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.cricket.dao.AlbumsFactory;
import com.cricket.dao.ArticlesFactory;
import com.cricket.dao.ClubFactory;
import com.cricket.dto.ArticleDto;
import com.cricket.dto.PhotoDto;

public class UpdateOldPhotoImagePaths {

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
				System.out.println("========= Photos Update running for the club id "+i+" =============== ");
				
				List<PhotoDto> photos = AlbumsFactory.getPhotos(i);
				if (!CommonUtility.isListNullEmpty(photos)) {
					
					System.out.println("Photos are there and update running for the club id "+i);
					
					for (PhotoDto photo : photos) {
						
						String query1 = "update photos set image_path='/documentsRep/photos/" + i 
								+ "-"+ photo.getAlbumId() + "-"+ photo.getPhotoId()+ ".jpg' where photo_id = "+photo.getPhotoId();
						String query2 = "update photos set thumbnail_path='/documentsRep/photos/thumbnails/" + i 
								+ "-"+ photo.getAlbumId() + "-"+ photo.getPhotoId()+ ".jpg' where photo_id = "+photo.getPhotoId();
						
						pst = conn.prepareStatement(query1);
						pst.executeUpdate();
						pst = conn.prepareStatement(query2);						
						pst.executeUpdate();
					}
				}else {
					
					System.out.println("No Photos found for the club id "+i);
				}
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + i + " The error is " + e.getMessage());
			} finally {
				DButility.closeConnection(conn);
			}
		}
	}
}