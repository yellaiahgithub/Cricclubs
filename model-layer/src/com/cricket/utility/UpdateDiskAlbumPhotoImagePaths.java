package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cricket.dao.AlbumsFactory;
import com.cricket.dao.ClubFactory;
import com.cricket.dto.PhotoDto;

public class UpdateDiskAlbumPhotoImagePaths {

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
			System.out.println(" ======================== Updating photos for the club " + i + " =========================== ");
			try {
				List<PhotoDto> photos = AlbumsFactory.getPhotos(i);
				
				if( !CommonUtility.isListNullEmpty(photos)){
					
					System.out.println(" ========= photos found for the club " + i + " =============== ");
					
					for( PhotoDto photo : photos) {
						// File dir = new File(CommonUtility.getDocRepPath() + photo.getImagePath());
						String url = "https://www-test.cricclubs.com/" + photo.getImagePath();
						
						CloseableHttpClient client = HttpClients.createDefault();			
						HttpGet httpGet = new HttpGet(url);            
			            CloseableHttpResponse response = client.execute(httpGet);
						
			            if( response.getStatusLine().getStatusCode() != 200) {
							String query = "update photos set image_path = '' where photo_id = " + photo.getPhotoId();
							pst = con.prepareStatement(query);
							pst.executeUpdate();
							System.out.println(" ========= photos image path updated to empty for the club " + i + " and photo id is "+ photo.getPhotoId() + " =============== ");
						}
						// File dir1 = new File(CommonUtility.getDocRepPath() + photo.getThumbnailPath());
			            String url1 = "https://www-test.cricclubs.com/" + photo.getThumbnailPath();
						
						CloseableHttpClient client1 = HttpClients.createDefault();			
						HttpGet httpGet1 = new HttpGet(url1);            
			            CloseableHttpResponse response1 = client1.execute(httpGet1);
						
			            if( response1.getStatusLine().getStatusCode() != 200) {
							String query = "update photos set thumbnail_path = '' where photo_id = " + photo.getPhotoId();
							pst = con.prepareStatement(query);
							pst.executeUpdate();
							System.out.println(" ========= photos thumbnail path updated to empty for the club " + i + " and photo id is "+ photo.getPhotoId() + " =============== ");
						}
					}					
				}
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + i	+ " The error is " + e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}	
}