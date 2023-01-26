package com.cricket.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dao.ArticlesFactory;
import com.cricket.dao.ClubFactory;
import com.cricket.dto.ArticleDto;

public class ArticlesUpdateImagePath {
	static Logger log = LoggerFactory.getLogger(ArticlesUpdateImagePath.class);
	public static void main(String[] args) throws Exception {
		updateImagePaths();
	}
	public static void updateImagePaths() throws FileNotFoundException, Exception,
			IOException {

		int maxClubID = 0;	
		String imagePath = "";		
		boolean imageExists;
		
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {			
			e1.printStackTrace();
		}	
		//maxClubID = 1;
		
		for (int i = 1; i <= maxClubID; i++) {			
			Connection con = DButility.getConnection(i);
			
			 System.out.println("### Image Paths update running for clubID ####  "+i);
			
			try {
				List<ArticleDto> articles = ArticlesFactory.getArticles(new ArticleDto(),i);				
				
				if(!CommonUtility.isListNullEmpty(articles)) {
					
					 System.out.println("### Number of Articles exists for clubID ####  "+i + "  ## "+articles.size());						
					
					for(ArticleDto article : articles) {
														
						if(!CommonUtility.isNullOrEmptyOrNULL(article.getImage())) {
							
							try{								
								imagePath = "/mnt/efs"+article.getImage();
								
								 System.out.println("### Image Path is ####  "+imagePath);
								
						        File file =  new File(imagePath);
						        
						        imageExists = file.exists();
						        
						        if(!imageExists) {
						        	
						        	 System.out.println("### Image not exists in server ####  ");
						        	
						        	String query = "update articles set image='' where article_id = "+article.getArticleId();
						    		PreparedStatement pst = null;
						    		try {
						    			pst = con.prepareStatement(query);
						    			pst.executeUpdate();
						    			
						    			System.out.println("### Image updated in database ####  ");
						    			
						    		} catch (SQLException e) {
						    			System.err.println(" Issue with update image path for Article ID ## "+article.getArticleId() + " for the Club ID ###" +i
												+ " The error is " + e.getMessage());
						    		} 
						        }
						        System.out.println("### Image exists in server ####  ");
						        
						    }catch(Exception ex){
						    	ex.printStackTrace();
						    }
						}
						 System.out.println("### Image not exists in database ####  ");
					}
					
				}
				System.out.println("### Articles not exists for clubID ####  "+i );
				 
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				System.err.println("Failed to Execute for club" + i	+ " The error is " + e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}

}
